package com.uns.controllers;

import com.uns.data.*;
import com.uns.entities.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("requerimientoBean")
@SessionScoped
public class RequerimientoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RequerimientoBean.class);

    @Inject
    private RequerimientoDao requerimientoDao;

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private ProyectoDao proyectoDao;

    @Inject
    private AreaDao areaDao;

    @Inject
    private CentroCostoDao centroCostoDao;

    @Inject
    private DetalleRequerimientoDao detalleRequerimientoDao;

    @Inject
    private MaterialDao materialDao;

    @Inject
    private UnidadDao unidadDao;

    private Requerimiento requerimiento;
    private Long solicitanteId;
    private Long proyectoId;
    private Long areaId;
    private Long centroCostoId;
    private Long aprobadorId;

    private List<Detalle_requerimiento> detalles;
    private Detalle_requerimiento detalleTemp;
    private Long materialId;
    private Long unidadId;

    public RequerimientoBean() {
        this.requerimiento = new Requerimiento();
        this.detalles = new ArrayList<>();
        this.detalleTemp = new Detalle_requerimiento();
    }

    @PostConstruct
    public void init() {
        logger.info("Inicializando RequerimientoBean");
        limpiarFormulario();
    }

    // Getters y Setters
    public Requerimiento getRequerimiento() {
        if (requerimiento == null) {
            requerimiento = new Requerimiento();
        }
        return requerimiento;
    }

    public void setRequerimiento(Requerimiento requerimiento) {
        this.requerimiento = requerimiento;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getCentroCostoId() {
        return centroCostoId;
    }

    public void setCentroCostoId(Long centroCostoId) {
        this.centroCostoId = centroCostoId;
    }

    public Long getAprobadorId() {
        return aprobadorId;
    }

    public void setAprobadorId(Long aprobadorId) {
        this.aprobadorId = aprobadorId;
    }

    public List<Detalle_requerimiento> getDetalles() {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        return detalles;
    }

    public void setDetalles(List<Detalle_requerimiento> detalles) {
        this.detalles = detalles;
    }

    public Detalle_requerimiento getDetalleTemp() {
        if (detalleTemp == null) {
            detalleTemp = new Detalle_requerimiento();
        }
        return detalleTemp;
    }

    public void setDetalleTemp(Detalle_requerimiento detalleTemp) {
        this.detalleTemp = detalleTemp;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    // Métodos de consulta
    public List<Requerimiento> getAll() {
        try {
            List<Requerimiento> result = requerimientoDao.getAll();
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener requerimientos: {}", e.getMessage(), e);
            addErrorMessage("Error al obtener requerimientos");
            return new ArrayList<>();
        }
    }

    public List<Usuario> getAllUsuarios() {
        try {
            return usuarioDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Proyecto> getAllProyectos() {
        try {
            return proyectoDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener proyectos: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Area> getAllAreas() {
        try {
            return areaDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener áreas: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Centro_costo> getAllCentrosCosto() {
        try {
            return centroCostoDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener centros de costo: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Material> getAllMateriales() {
        try {
            return materialDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener materiales: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Unidad> getAllUnidades() {
        try {
            return unidadDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener unidades: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void agregarDetalle() {
        try {
            if (!validarDetalle()) {
                return;
            }

            Detalle_requerimiento nuevoDetalle = new Detalle_requerimiento();

            if (materialId != null) {
                Material material = materialDao.getById(materialId);
                nuevoDetalle.setMaterial(material);
            }

            if (unidadId != null) {
                Unidad unidad = unidadDao.getById(unidadId);
                nuevoDetalle.setUnidad(unidad);
            }

            nuevoDetalle.setCantidad(detalleTemp.getCantidad());
            nuevoDetalle.setObservacion(detalleTemp.getObservacion());
            nuevoDetalle.setEstado(Detalle_requerimiento.Estado.Pendiente);

            detalles.add(nuevoDetalle);
            limpiarDetalleTemp();
            addInfoMessage("Ítem agregado correctamente");
        } catch (Exception e) {
            logger.error("Error al agregar detalle: {}", e.getMessage(), e);
            addErrorMessage("Error al agregar ítem");
        }
    }

    public void eliminarDetalle(Detalle_requerimiento detalle) {
        try {
            detalles.remove(detalle);
            addInfoMessage("Ítem eliminado correctamente");
        } catch (Exception e) {
            logger.error("Error al eliminar detalle: {}", e.getMessage(), e);
            addErrorMessage("Error al eliminar ítem");
        }
    }

    private boolean validarDetalle() {
        if (materialId == null) {
            addErrorMessage("Debe seleccionar un material");
            return false;
        }
        if (unidadId == null) {
            addErrorMessage("Debe seleccionar una unidad");
            return false;
        }
        if (detalleTemp.getCantidad() == null || detalleTemp.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
            addErrorMessage("La cantidad debe ser mayor a cero");
            return false;
        }
        return true;
    }

    private void limpiarDetalleTemp() {
        detalleTemp = new Detalle_requerimiento();
        materialId = null;
        unidadId = null;
    }

    public String create() {
        try {
            if (!validarRequerimiento()) {
                return null;
            }

            requerimiento.setFechaSolicitud(LocalDate.now());
            requerimiento.setEstado(Requerimiento.Estado.Pendiente);

            if (solicitanteId != null) {
                Usuario solicitante = usuarioDao.getById(solicitanteId);
                requerimiento.setSolicitante(solicitante);
            }

            if (proyectoId != null) {
                Proyecto proyecto = proyectoDao.getById(proyectoId);
                requerimiento.setProyecto(proyecto);
            }

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                requerimiento.setArea(area);
            }

            if (centroCostoId != null) {
                Centro_costo centroCosto = centroCostoDao.getById(centroCostoId);
                requerimiento.setCentroCosto(centroCosto);
            }

            requerimientoDao.create(requerimiento);

            for (Detalle_requerimiento detalle : detalles) {
                detalle.setRequerimiento(requerimiento);
                detalleRequerimientoDao.create(detalle);
            }

            addInfoMessage("Requerimiento creado exitosamente");
            limpiarFormulario();
            return "index?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear requerimiento: {}", e.getMessage(), e);
            addErrorMessage("Error al crear requerimiento");
            return null;
        }
    }

    public String update() {
        try {
            if (!validarRequerimiento()) {
                return null;
            }

            if (solicitanteId != null) {
                Usuario solicitante = usuarioDao.getById(solicitanteId);
                requerimiento.setSolicitante(solicitante);
            }

            if (proyectoId != null) {
                Proyecto proyecto = proyectoDao.getById(proyectoId);
                requerimiento.setProyecto(proyecto);
            }

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                requerimiento.setArea(area);
            }

            if (centroCostoId != null) {
                Centro_costo centroCosto = centroCostoDao.getById(centroCostoId);
                requerimiento.setCentroCosto(centroCosto);
            }

            requerimientoDao.update(requerimiento);
            detalleRequerimientoDao.deleteByRequerimiento(requerimiento.getId());

            for (Detalle_requerimiento detalle : detalles) {
                detalle.setRequerimiento(requerimiento);
                detalleRequerimientoDao.create(detalle);
            }

            addInfoMessage("Requerimiento actualizado exitosamente");
            return "index?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar requerimiento: {}", e.getMessage(), e);
            addErrorMessage("Error al actualizar requerimiento");
            return null;
        }
    }

    public String aprobar() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            requerimiento.setEstado(Requerimiento.Estado.Aprobado);
            requerimiento.setFechaAprobacion(LocalDate.now());

            requerimientoDao.update(requerimiento);
            addInfoMessage("Requerimiento aprobado");
            return "index?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al aprobar: {}", e.getMessage(), e);
            addErrorMessage("Error al aprobar requerimiento");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            detalleRequerimientoDao.deleteByRequerimiento(id);
            requerimientoDao.delete(requerimiento);

            addInfoMessage("Requerimiento eliminado");
            return "index?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar: {}", e.getMessage(), e);
            addErrorMessage("Error al eliminar requerimiento");
            return null;
        }
    }

    public String add() {
        limpiarFormulario();
        return "add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);

            if (requerimiento.getSolicitante() != null) {
                this.solicitanteId = requerimiento.getSolicitante().getId();
            }
            if (requerimiento.getProyecto() != null) {
                this.proyectoId = requerimiento.getProyecto().getId();
            }
            if (requerimiento.getArea() != null) {
                this.areaId = requerimiento.getArea().getId();
            }
            if (requerimiento.getCentroCosto() != null) {
                this.centroCostoId = requerimiento.getCentroCosto().getId();
            }

            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);
            return "edit?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al cargar requerimiento: {}", e.getMessage(), e);
            addErrorMessage("Error al cargar requerimiento");
            return null;
        }
    }

    public String show() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);
            return "show?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al cargar: {}", e.getMessage(), e);
            addErrorMessage("Error al cargar requerimiento");
            return null;
        }
    }

    public String verAprobar() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);
            return "aprobar?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage(), e);
            addErrorMessage("Error al cargar requerimiento");
            return null;
        }
    }

    public String index() {
        return "index?faces-redirect=true";
    }

    private boolean validarRequerimiento() {
        if (solicitanteId == null) {
            addErrorMessage("Debe seleccionar un solicitante");
            return false;
        }
        if (detalles == null || detalles.isEmpty()) {
            addErrorMessage("Debe agregar al menos un ítem");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        requerimiento = new Requerimiento();
        solicitanteId = null;
        proyectoId = null;
        areaId = null;
        centroCostoId = null;
        aprobadorId = null;
        detalles = new ArrayList<>();
        limpiarDetalleTemp();
    }

    private void addInfoMessage(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje));
    }

    private void addErrorMessage(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje));
    }
}