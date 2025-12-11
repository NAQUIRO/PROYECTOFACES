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
import java.time.LocalDate;
@Entity
@Table(name = "orden_compra")
public class Orden_compra implements Serializable {

    private static final long serialVersionUID = 1L;

    public Orden_compra() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String codigo;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @Enumerated(EnumType.STRING)
    private Moneda moneda = Moneda.PEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago = FormaPago.Transferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_pago")
    private CondicionPago condicionPago = CondicionPago.Contado;

    @Column(name = "lugar_entrega", length = 255)
    private String lugarEntrega;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Pendiente;

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal igv;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 255)
    private String observaciones;

    /**
     * Currency enumeration. Mirrors ENUM('PEN','USD').
     */
    public enum Moneda { PEN, USD }

    /**
     * Payment method enumeration. Mirrors ENUM('Cheque','Efectivo','Pagare','Transferencia').
     */
    public enum FormaPago { Cheque, Efectivo, Pagare, Transferencia }

    /**
     * Payment condition enumeration. Mirrors ENUM('Contado','ContraEntrega','Factura').
     */
    public enum CondicionPago { Contado, ContraEntrega, Factura }

    /**
     * State enumeration for an order. Mirrors ENUM('Pendiente','VistoBueno','Aprobado','Enviada','Anulada').
     */
    public enum Estado { Pendiente, VistoBueno, Aprobado, Enviada, Anulada }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public CondicionPago getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(CondicionPago condicionPago) {
        this.condicionPago = condicionPago;
    }

    public String getLugarEntrega() {
        return lugarEntrega;
    }

    public void setLugarEntrega(String lugarEntrega) {
        this.lugarEntrega = lugarEntrega;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "OrdenCompra{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fecha=" + fecha +
                ", proveedorId=" + (proveedor != null ? proveedor.getId() : null) +
                ", solicitanteId=" + (solicitante != null ? solicitante.getId() : null) +
                ", moneda=" + moneda +
                ", formaPago=" + formaPago +
                ", condicionPago=" + condicionPago +
                ", lugarEntrega='" + lugarEntrega + '\'' +
                ", fechaEntrega=" + fechaEntrega +
                ", estado=" + estado +
                ", subtotal=" + subtotal +
                ", igv=" + igv +
                ", total=" + total +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}