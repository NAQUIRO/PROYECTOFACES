package com.uns.controllers;

import com.uns.data.ProyectoDao;
import com.uns.data.AreaDao;
import com.uns.entities.Proyecto;
import com.uns.entities.Area;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("proyectoBean")
@SessionScoped
public class ProyectoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoBean.class);

    @Inject
    private ProyectoDao proyectoDao;

    @Inject
    private AreaDao areaDao;

    private Proyecto proyecto = new Proyecto();
    private Long areaId;

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<Proyecto> getAll() {
        try {
            List<Proyecto> result = proyectoDao.getAll();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener proyectos: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los proyectos");
            return List.of();
        }
    }

    public List<Proyecto> getAllActivos() {
        try {
            List<Proyecto> result = proyectoDao.getAllActivos();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener proyectos activos: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los proyectos activos");
            return List.of();
        }
    }

    public List<Area> getAllAreas() {
        try {
            List<Area> result = areaDao.getAllActivos();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener áreas: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las áreas");
            return List.of();
        }
    }

    public String create() {
        try {
            if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            proyecto.setEstado(Proyecto.Estado.Activo);

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                if (area == null) {
                    addErrorMessage("Error", "Área no encontrada");
                    return null;
                }
                proyecto.setArea(area);
            }

            proyectoDao.create(proyecto);
            addInfoMessage("Éxito", "Proyecto creado correctamente");
            logger.info("Proyecto creado: {}", proyecto.getNombre());
            return "/pages/proyectos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear proyecto: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el proyecto");
            return null;
        }
    }

    public String update() {
        try {
            if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                if (area == null) {
                    addErrorMessage("Error", "Área no encontrada");
                    return null;
                }
                proyecto.setArea(area);
            }

            proyectoDao.update(proyecto);
            addInfoMessage("Éxito", "Proyecto actualizado correctamente");
            logger.info("Proyecto actualizado: {}", proyecto.getNombre());
            return "/pages/proyectos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar proyecto: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el proyecto");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.proyecto.getId();
            this.proyecto = proyectoDao.getById(id);
            if (proyecto == null) {
                addErrorMessage("Error", "Proyecto no encontrado");
                return null;
            }
            proyectoDao.delete(proyecto);
            addInfoMessage("Éxito", "Proyecto eliminado correctamente");
            logger.info("Proyecto eliminado: {}", id);
            return "/pages/proyectos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar proyecto: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el proyecto");
            return null;
        }
    }

    public String add() {
        return "/pages/proyectos/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.proyecto.getId();
            this.proyecto = proyectoDao.getById(id);
            if (proyecto == null) {
                addErrorMessage("Error", "Proyecto no encontrado");
                return "/pages/proyectos/index?faces-redirect=true";
            }
            if (proyecto.getArea() != null) {
                this.areaId = proyecto.getArea().getId();
            }
            return "/pages/proyectos/edit";
        } catch (Exception e) {
            logger.error("Error al cargar proyecto para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el proyecto");
            return "/pages/proyectos/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.proyecto.getId();
            this.proyecto = proyectoDao.getById(id);
            if (proyecto == null) {
                addErrorMessage("Error", "Proyecto no encontrado");
                return "/pages/proyectos/index?faces-redirect=true";
            }
            return "/pages/proyectos/show";
        } catch (Exception e) {
            logger.error("Error al cargar proyecto: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el proyecto");
            return "/pages/proyectos/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/proyectos/index";
    }

    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }
}