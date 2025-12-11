
package com.uns.entities;

/**
 *
 * @author aanto
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.*;
import java.math.BigDecimal;
@Entity
@Table(name = "detalle_requerimiento")
public class Detalle_requerimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Detalle_requerimiento() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requerimiento_id")
    private Requerimiento requerimiento;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @ManyToOne
    @JoinColumn(name = "unidad_id")
    private Unidad unidad;

    @Column(length = 255)
    private String observacion;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Pendiente;

    /**
     * Possible states of a requisition detail. Mirrors ENUM('Pendiente','Comprado','Cancelado').
     */
    public enum Estado {
        Pendiente, Comprado, Cancelado
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requerimiento getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(Requerimiento requerimiento) {
        this.requerimiento = requerimiento;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "DetalleRequerimiento{" +
                "id=" + id +
                ", requerimientoId=" + (requerimiento != null ? requerimiento.getId() : null) +
                ", materialId=" + (material != null ? material.getId() : null) +
                ", cantidad=" + cantidad +
                ", unidadId=" + (unidad != null ? unidad.getId() : null) +
                ", observacion='" + observacion + '\'' +
                ", estado=" + estado +
                '}';
    }
}