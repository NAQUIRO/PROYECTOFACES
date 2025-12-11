package com.uns.controllers;

import com.uns.data.AreaDao;
import com.uns.data.CentroCostoDao;
import com.uns.data.DetalleRequerimientoDao;
import com.uns.data.MaterialDao;
import com.uns.data.ProyectoDao;
import com.uns.data.RequerimientoDao;
import com.uns.data.UnidadDao;
import com.uns.data.UsuarioDao;
import com.uns.entities.Area;
import com.uns.entities.Centro_costo;
import com.uns.entities.Detalle_requerimiento;
import com.uns.entities.Material;
import com.uns.entities.Proyecto;
import com.uns.entities.Requerimiento;
import com.uns.entities.Unidad;
import com.uns.entities.Usuario;
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

@Named("requerimientoBean")
@SessionScoped
public class RequerimientoBean implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Requerimiento requerimiento = new Requerimiento();
    private Long solicitanteId;
    private Long proyectoId;
    private Long areaId;
    private Long centroCostoId;
    private Long aprobadorId;

    // Gestión de detalles
    private List<Detalle_requerimiento> detalles = new ArrayList<>();
    private Detalle_requerimiento detalleTemp = new Detalle_requerimiento();
    private Long materialId;
    private Long unidadId;

    @PostConstruct
    public void init() {
        limpiarFormulario();
    }

    // Getters y Setters
    public Requerimiento getRequerimiento() {
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
        return detalles;
    }

    public void setDetalles(List<Detalle_requerimiento> detalles) {
        this.detalles = detalles;
    }

    public Detalle_requerimiento getDetalleTemp() {
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
            if (result == null || result.isEmpty()) {
                return new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            addErrorMessage("Error al obtener requerimientos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Usuario> getAllUsuarios() {
        try {
            return usuarioDao.getAll();
        } catch (Exception e) {
            addErrorMessage("Error al obtener usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Proyecto> getAllProyectos() {
        try {
            return proyectoDao.getAllActivos();
        } catch (Exception e) {
            addErrorMessage("Error al obtener proyectos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Area> getAllAreas() {
        try {
            return areaDao.getAllActivos();
        } catch (Exception e) {
            addErrorMessage("Error al obtener áreas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Centro_costo> getAllCentrosCosto() {
        try {
            return centroCostoDao.getAllActivos();
        } catch (Exception e) {
            addErrorMessage("Error al obtener centros de costo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Material> getAllMateriales() {
        try {
            return materialDao.getAllActivos();
        } catch (Exception e) {
            addErrorMessage("Error al obtener materiales: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Unidad> getAllUnidades() {
        try {
            return unidadDao.getAll();
        } catch (Exception e) {
            addErrorMessage("Error al obtener unidades: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Métodos de gestión de detalles
    public void agregarDetalle() {
        try {
            if (!validarDetalle()) {
                return;
            }

            // Crear nuevo detalle
            Detalle_requerimiento nuevoDetalle = new Detalle_requerimiento();

            // Asignar material
            if (materialId != null) {
                Material material = materialDao.getById(materialId);
                nuevoDetalle.setMaterial(material);
            }

            // Asignar unidad
            if (unidadId != null) {
                Unidad unidad = unidadDao.getById(unidadId);
                nuevoDetalle.setUnidad(unidad);
            }

            nuevoDetalle.setCantidad(detalleTemp.getCantidad());
            nuevoDetalle.setObservacion(detalleTemp.getObservacion());
            nuevoDetalle.setEstado(Detalle_requerimiento.Estado.Pendiente);

            // Agregar a la lista
            detalles.add(nuevoDetalle);

            // Limpiar formulario temporal
            limpiarDetalleTemp();

            addInfoMessage("Ítem agregado correctamente");
        } catch (Exception e) {
            addErrorMessage("Error al agregar ítem: " + e.getMessage());
        }
    }

    public void eliminarDetalle(Detalle_requerimiento detalle) {
        try {
            detalles.remove(detalle);
            addInfoMessage("Ítem eliminado correctamente");
        } catch (Exception e) {
            addErrorMessage("Error al eliminar ítem: " + e.getMessage());
        }
    }

    public void cambiarEstadoDetalle(Detalle_requerimiento detalle, Detalle_requerimiento.Estado nuevoEstado) {
        try {
            detalle.setEstado(nuevoEstado);
            if (detalle.getId() != null) {
                detalleRequerimientoDao.update(detalle);
            }
            addInfoMessage("Estado del ítem actualizado");
        } catch (Exception e) {
            addErrorMessage("Error al cambiar estado: " + e.getMessage());
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

    // Métodos CRUD
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

            // Guardar requerimiento
            requerimientoDao.create(requerimiento);

            // Guardar detalles
            for (Detalle_requerimiento detalle : detalles) {
                detalle.setRequerimiento(requerimiento);
                detalleRequerimientoDao.create(detalle);
            }

            addInfoMessage("Requerimiento creado exitosamente");
            limpiarFormulario();
            return "/pages/requerimientos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al crear requerimiento: " + e.getMessage());
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

            if (aprobadorId != null) {
                Usuario aprobador = usuarioDao.getById(aprobadorId);
                requerimiento.setAprobador(aprobador);
            }

            // Actualizar requerimiento
            requerimientoDao.update(requerimiento);

            // Eliminar detalles antiguos
            detalleRequerimientoDao.deleteByRequerimiento(requerimiento.getId());

            // Guardar nuevos detalles
            for (Detalle_requerimiento detalle : detalles) {
                detalle.setRequerimiento(requerimiento);
                detalleRequerimientoDao.create(detalle);
            }

            addInfoMessage("Requerimiento actualizado exitosamente");
            return "/pages/requerimientos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al actualizar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String aprobar() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            requerimiento.setEstado(Requerimiento.Estado.Aprobado);
            requerimiento.setFechaAprobacion(LocalDate.now());

            if (aprobadorId != null) {
                Usuario aprobador = usuarioDao.getById(aprobadorId);
                requerimiento.setAprobador(aprobador);
            }

            requerimientoDao.update(requerimiento);
            addInfoMessage("Requerimiento aprobado");
            return "/pages/requerimientos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al aprobar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String rechazar() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);
            requerimiento.setEstado(Requerimiento.Estado.Rechazado);

            requerimientoDao.update(requerimiento);
            addInfoMessage("Requerimiento rechazado");
            return "/pages/requerimientos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al rechazar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);

            // Eliminar detalles primero
            detalleRequerimientoDao.deleteByRequerimiento(id);

            // Eliminar requerimiento
            requerimientoDao.delete(requerimiento);

            addInfoMessage("Requerimiento eliminado");
            return "/pages/requerimientos/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al eliminar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String add() {
        limpiarFormulario();
        return "/pages/requerimientos/add?faces-redirect=true";
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

            // Cargar detalles
            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);

            return "/pages/requerimientos/edit";
        } catch (Exception e) {
            addErrorMessage("Error al cargar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String show() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);

            // Cargar detalles
            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);

            return "/pages/requerimientos/show";
        } catch (Exception e) {
            addErrorMessage("Error al cargar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String verAprobar() {
        try {
            Long id = this.requerimiento.getId();
            this.requerimiento = requerimientoDao.getById(id);

            // Cargar detalles
            this.detalles = detalleRequerimientoDao.getByRequerimiento(id);

            return "/pages/requerimientos/aprobar";
        } catch (Exception e) {
            addErrorMessage("Error al cargar requerimiento: " + e.getMessage());
            return null;
        }
    }

    public String index() {
        return "/pages/requerimientos/index";
    }

    // Métodos de validación
    private boolean validarRequerimiento() {
        if (solicitanteId == null) {
            addErrorMessage("Debe seleccionar un solicitante");
            return false;
        }
        if (detalles == null || detalles.isEmpty()) {
            addErrorMessage("Debe agregar al menos un ítem al requerimiento");
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

    // Métodos de mensajes
    private void addInfoMessage(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje));
    }

    private void addErrorMessage(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje));
    }
}