package com.uns.controllers;

import com.uns.data.MaterialDao;
import com.uns.data.GrupoDao;
import com.uns.data.UnidadDao;
import com.uns.entities.Material;
import com.uns.entities.Grupo;
import com.uns.entities.Unidad;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named("materialBean")
@SessionScoped
public class MaterialBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MaterialBean.class);

    @Inject
    private MaterialDao materialDao;

    @Inject
    private GrupoDao grupoDao;

    @Inject
    private UnidadDao unidadDao;

    private Material material = new Material();
    private Long grupoId;
    private Long unidadId;
    private List<Material> materiales;

    @PostConstruct
    public void init() {
        logger.info("Inicializando MaterialBean...");
        material = new Material();
        cargarMateriales();
    }

    private void cargarMateriales() {
        try {
            materiales = materialDao.getAll();
            if (materiales == null) {
                materiales = new ArrayList<>();
            }
            logger.info("Materiales cargados: {}", materiales.size());
        } catch (Exception e) {
            logger.error("Error al cargar materiales: {}", e.getMessage(), e);
            materiales = new ArrayList<>();
            addErrorMessage("Error", "No se pudieron cargar los materiales: " + e.getMessage());
        }
    }

    // Getters
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    // Métodos de listado
    public List<Material> getAll() {
        if (materiales == null) {
            cargarMateriales();
        }
        return materiales;
    }

    public List<Grupo> getAllGrupos() {
        try {
            List<Grupo> result = grupoDao.getAllActivos();
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener grupos: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los grupos");
            return new ArrayList<>();
        }
    }

    public List<Unidad> getAllUnidades() {
        try {
            List<Unidad> result = unidadDao.getAllActivos();
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener unidades: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las unidades");
            return new ArrayList<>();
        }
    }

    // Operaciones CRUD
    public String create() {
        try {
            logger.info("Creando material: {}", material.getNombre());
            
            // Validaciones
            if (material.getNombre() == null || material.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            if (grupoId == null) {
                addErrorMessage("Validación", "El grupo es requerido");
                return null;
            }

            if (unidadId == null) {
                addErrorMessage("Validación", "La unidad es requerida");
                return null;
            }

            material.setFechaCreacion(LocalDate.now());
            material.setEstado(Material.Estado.Activo);

            Grupo grupo = grupoDao.getById(grupoId);
            if (grupo == null) {
                addErrorMessage("Error", "Grupo no encontrado");
                return null;
            }
            material.setGrupo(grupo);

            Unidad unidad = unidadDao.getById(unidadId);
            if (unidad == null) {
                addErrorMessage("Error", "Unidad no encontrada");
                return null;
            }
            material.setUnidad(unidad);

            materialDao.create(material);
            addInfoMessage("Éxito", "Material creado correctamente");
            logger.info("Material creado exitosamente: ID={}", material.getId());
            
            // Limpiar y recargar
            material = new Material();
            grupoId = null;
            unidadId = null;
            cargarMateriales();
            
            return "/pages/materiales/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear material: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el material: " + e.getMessage());
            return null;
        }
    }

    public String update() {
        try {
            logger.info("Actualizando material ID: {}", material.getId());
            
            // Validaciones
            if (material.getNombre() == null || material.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            if (grupoId == null) {
                addErrorMessage("Validación", "El grupo es requerido");
                return null;
            }

            if (unidadId == null) {
                addErrorMessage("Validación", "La unidad es requerida");
                return null;
            }

            Grupo grupo = grupoDao.getById(grupoId);
            if (grupo == null) {
                addErrorMessage("Error", "Grupo no encontrado");
                return null;
            }
            material.setGrupo(grupo);

            Unidad unidad = unidadDao.getById(unidadId);
            if (unidad == null) {
                addErrorMessage("Error", "Unidad no encontrada");
                return null;
            }
            material.setUnidad(unidad);

            materialDao.update(material);
            addInfoMessage("Éxito", "Material actualizado correctamente");
            logger.info("Material actualizado: {}", material.getNombre());
            
            // Limpiar y recargar
            material = new Material();
            grupoId = null;
            unidadId = null;
            cargarMateriales();
            
            return "/pages/materiales/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar material: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el material: " + e.getMessage());
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.material.getId();
            logger.info("Eliminando material ID: {}", id);
            
            this.material = materialDao.getById(id);
            if (material == null) {
                addErrorMessage("Error", "Material no encontrado");
                return null;
            }
            
            materialDao.delete(material);
            addInfoMessage("Éxito", "Material eliminado correctamente");
            logger.info("Material eliminado: {}", id);
            
            // Limpiar y recargar
            material = new Material();
            grupoId = null;
            unidadId = null;
            cargarMateriales();
            
            return "/pages/materiales/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar material: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el material: " + e.getMessage());
            return null;
        }
    }

    // Navegación
    public String add() {
        material = new Material();
        grupoId = null;
        unidadId = null;
        return "/pages/materiales/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.material.getId();
            logger.info("Cargando material para editar ID: {}", id);
            
            this.material = materialDao.getById(id);
            if (material == null) {
                addErrorMessage("Error", "Material no encontrado");
                return "/pages/materiales/index?faces-redirect=true";
            }
            
            if (material.getGrupo() != null) {
                this.grupoId = material.getGrupo().getId();
            }
            if (material.getUnidad() != null) {
                this.unidadId = material.getUnidad().getId();
            }
            
            return "/pages/materiales/edit";
        } catch (Exception e) {
            logger.error("Error al cargar material para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el material");
            return "/pages/materiales/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.material.getId();
            logger.info("Mostrando material ID: {}", id);
            
            this.material = materialDao.getById(id);
            if (material == null) {
                addErrorMessage("Error", "Material no encontrado");
                return "/pages/materiales/index?faces-redirect=true";
            }
            return "/pages/materiales/show";
        } catch (Exception e) {
            logger.error("Error al cargar material: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el material");
            return "/pages/materiales/index?faces-redirect=true";
        }
    }

    public String index() {
        cargarMateriales();
        return "/pages/materiales/index";
    }

    // Métodos de mensajes
    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }
}