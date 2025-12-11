/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uns.entities;

/**
 *
 * @author aanto
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.*;
import java.math.BigDecimal;
@Entity
@Table(name = "detalle_orden_compra")
public class Detalle_orden_compra implements Serializable {

    private static final long serialVersionUID = 1L;

    public Detalle_orden_compra (){
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_compra_id")
    private Orden_compra ordenCompra;

    @ManyToOne
    @JoinColumn(name = "detalle_requerimiento_id")
    private Detalle_requerimiento detalleRequerimiento;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(precision = 10, scale = 2)
    private BigDecimal cantidad;

    @ManyToOne
    @JoinColumn(name = "unidad_id")
    private Unidad unidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orden_compra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(Orden_compra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Detalle_requerimiento getDetalleRequerimiento() {
        return detalleRequerimiento;
    }

    public void setDetalleRequerimiento(Detalle_requerimiento detalleRequerimiento) {
        this.detalleRequerimiento = detalleRequerimiento;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleOrdenCompra{" +
                "id=" + id +
                ", ordenCompraId=" + (ordenCompra != null ? ordenCompra.getId() : null) +
                ", detalleRequerimientoId=" + (detalleRequerimiento != null ? detalleRequerimiento.getId() : null) +
                ", materialId=" + (material != null ? material.getId() : null) +
                ", cantidad=" + cantidad +
                ", unidadId=" + (unidad != null ? unidad.getId() : null) +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}