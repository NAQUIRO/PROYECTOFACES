package com.uns.controllers;

import com.uns.data.DetalleOrdenCompraDao;
import com.uns.data.MaterialDao;
import com.uns.data.OrdenCompraDao;
import com.uns.data.ProveedorDao;
import com.uns.data.UnidadDao;
import com.uns.data.UsuarioDao;
import com.uns.entities.Detalle_orden_compra;
import com.uns.entities.Material;
import com.uns.entities.Orden_compra;
import com.uns.entities.Proveedor;
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

@Named("ordenCompraBean")
@SessionScoped
public class OrdenCompraBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OrdenCompraDao ordenCompraDao;

    @Inject
    private ProveedorDao proveedorDao;

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private DetalleOrdenCompraDao detalleOrdenCompraDao;

    @Inject
    private MaterialDao materialDao;

    @Inject
    private UnidadDao unidadDao;

    private Orden_compra ordenCompra = new Orden_compra();
    private Long proveedorId;
    private Long solicitanteId;

    // Gestión de detalles
    private List<Detalle_orden_compra> detalles = new ArrayList<>();
    private Detalle_orden_compra detalleTemp = new Detalle_orden_compra();
    private Long materialId;
    private Long unidadId;

    @PostConstruct
    public void init() {
        limpiarFormulario();
    }

    // Getters y Setters
    public Orden_compra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(Orden_compra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public List<Detalle_orden_compra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle_orden_compra> detalles) {
        this.detalles = detalles;
    }

    public Detalle_orden_compra getDetalleTemp() {
        return detalleTemp;
    }

    public void setDetalleTemp(Detalle_orden_compra detalleTemp) {
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
    public List<Orden_compra> getAll() {
        try {
            List<Orden_compra> result = ordenCompraDao.getAll();
            if (result == null || result.isEmpty()) {
                return new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            addErrorMessage("Error al obtener órdenes de compra: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Proveedor> getAllProveedores() {
        try {
            return proveedorDao.getAll();
        } catch (Exception e) {
            addErrorMessage("Error al obtener proveedores: " + e.getMessage());
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
            Detalle_orden_compra nuevoDetalle = new Detalle_orden_compra();

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
            nuevoDetalle.setPrecioUnitario(detalleTemp.getPrecioUnitario());

            // Calcular subtotal
            BigDecimal subtotal = detalleTemp.getCantidad().multiply(detalleTemp.getPrecioUnitario());
            nuevoDetalle.setSubtotal(subtotal);

            // Agregar a la lista
            detalles.add(nuevoDetalle);

            // Recalcular totales
            recalcularTotales();

            // Limpiar formulario temporal
            limpiarDetalleTemp();

            addInfoMessage("Ítem agregado correctamente");
        } catch (Exception e) {
            addErrorMessage("Error al agregar ítem: " + e.getMessage());
        }
    }

    public void eliminarDetalle(Detalle_orden_compra detalle) {
        try {
            detalles.remove(detalle);
            recalcularTotales();
            addInfoMessage("Ítem eliminado correctamente");
        } catch (Exception e) {
            addErrorMessage("Error al eliminar ítem: " + e.getMessage());
        }
    }

    public void recalcularTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Detalle_orden_compra detalle : detalles) {
            if (detalle.getSubtotal() != null) {
                subtotal = subtotal.add(detalle.getSubtotal());
            }
        }

        ordenCompra.setSubtotal(subtotal);

        // Calcular IGV (18%)
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        ordenCompra.setIgv(igv);

        // Calcular total
        BigDecimal total = subtotal.add(igv);
        ordenCompra.setTotal(total);
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
        if (detalleTemp.getPrecioUnitario() == null
                || detalleTemp.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            addErrorMessage("El precio unitario debe ser mayor a cero");
            return false;
        }
        return true;
    }

    private void limpiarDetalleTemp() {
        detalleTemp = new Detalle_orden_compra();
        materialId = null;
        unidadId = null;
    }

    // Métodos CRUD
    public String create() {
        try {
            if (!validarOrden()) {
                return null;
            }

            ordenCompra.setFecha(LocalDate.now());
            ordenCompra.setEstado(Orden_compra.Estado.Pendiente);

            if (proveedorId != null) {
                Proveedor proveedor = proveedorDao.getById(proveedorId);
                ordenCompra.setProveedor(proveedor);
            }

            if (solicitanteId != null) {
                Usuario solicitante = usuarioDao.getById(solicitanteId);
                ordenCompra.setSolicitante(solicitante);
            }

            // Guardar orden
            ordenCompraDao.create(ordenCompra);

            // Guardar detalles
            for (Detalle_orden_compra detalle : detalles) {
                detalle.setOrdenCompra(ordenCompra);
                detalleOrdenCompraDao.create(detalle);
            }

            addInfoMessage("Orden de compra creada exitosamente");
            limpiarFormulario();
            return "/pages/ordenes/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al crear orden de compra: " + e.getMessage());
            return null;
        }
    }

    public String update() {
        try {
            if (!validarOrden()) {
                return null;
            }

            if (proveedorId != null) {
                Proveedor proveedor = proveedorDao.getById(proveedorId);
                ordenCompra.setProveedor(proveedor);
            }

            if (solicitanteId != null) {
                Usuario solicitante = usuarioDao.getById(solicitanteId);
                ordenCompra.setSolicitante(solicitante);
            }

            // Actualizar orden
            ordenCompraDao.update(ordenCompra);

            // Eliminar detalles antiguos
            detalleOrdenCompraDao.deleteByOrdenCompra(ordenCompra.getId());

            // Guardar nuevos detalles
            for (Detalle_orden_compra detalle : detalles) {
                detalle.setOrdenCompra(ordenCompra);
                detalleOrdenCompraDao.create(detalle);
            }

            addInfoMessage("Orden de compra actualizada exitosamente");
            return "/pages/ordenes/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al actualizar orden de compra: " + e.getMessage());
            return null;
        }
    }

    public String aprobar() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);
            ordenCompra.setEstado(Orden_compra.Estado.Aprobado);
            ordenCompraDao.update(ordenCompra);
            addInfoMessage("Orden de compra aprobada");
            return "/pages/ordenes/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al aprobar orden: " + e.getMessage());
            return null;
        }
    }

    public String enviar() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);
            ordenCompra.setEstado(Orden_compra.Estado.Enviada);
            ordenCompraDao.update(ordenCompra);
            addInfoMessage("Orden de compra enviada");
            return "/pages/ordenes/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al enviar orden: " + e.getMessage());
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);

            // Eliminar detalles primero
            detalleOrdenCompraDao.deleteByOrdenCompra(id);

            // Eliminar orden
            ordenCompraDao.delete(ordenCompra);

            addInfoMessage("Orden de compra eliminada");
            return "/pages/ordenes/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErrorMessage("Error al eliminar orden: " + e.getMessage());
            return null;
        }
    }

    public String add() {
        limpiarFormulario();
        return "/pages/ordenes/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);

            if (ordenCompra.getProveedor() != null) {
                this.proveedorId = ordenCompra.getProveedor().getId();
            }
            if (ordenCompra.getSolicitante() != null) {
                this.solicitanteId = ordenCompra.getSolicitante().getId();
            }

            // Cargar detalles
            this.detalles = detalleOrdenCompraDao.getByOrdenCompra(id);

            return "/pages/ordenes/edit";
        } catch (Exception e) {
            addErrorMessage("Error al cargar orden: " + e.getMessage());
            return null;
        }
    }

    public String show() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);

            // Cargar detalles
            this.detalles = detalleOrdenCompraDao.getByOrdenCompra(id);

            return "/pages/ordenes/show";
        } catch (Exception e) {
            addErrorMessage("Error al cargar orden: " + e.getMessage());
            return null;
        }
    }

    public String imprimir() {
        try {
            Long id = this.ordenCompra.getId();
            this.ordenCompra = ordenCompraDao.getById(id);

            // Cargar detalles
            this.detalles = detalleOrdenCompraDao.getByOrdenCompra(id);

            return "/pages/ordenes/imprimir";
        } catch (Exception e) {
            addErrorMessage("Error al cargar orden: " + e.getMessage());
            return null;
        }
    }

    public String index() {
        return "/pages/ordenes/index";
    }

    // Métodos de validación
    private boolean validarOrden() {
        if (proveedorId == null) {
            addErrorMessage("Debe seleccionar un proveedor");
            return false;
        }
        if (solicitanteId == null) {
            addErrorMessage("Debe seleccionar un solicitante");
            return false;
        }
        if (detalles == null || detalles.isEmpty()) {
            addErrorMessage("Debe agregar al menos un ítem a la orden");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        ordenCompra = new Orden_compra();
        proveedorId = null;
        solicitanteId = null;
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