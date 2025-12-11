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
import java.time.LocalDate;
@Entity
@Table(name = "requerimiento")
public class Requerimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Requerimiento() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String codigo;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "centro_costo_id")
    private Centro_costo centroCosto;

    @Column(length = 100)
    private String etapa;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Pendiente;

    @Column(name = "fecha_aprobacion")
    private LocalDate fechaAprobacion;

    @ManyToOne
    @JoinColumn(name = "aprobador_id")
    private Usuario aprobador;

    @Column(length = 255)
    private String observaciones;

    /**
     * Possible states of a requisition. Mirrors ENUM('Pendiente','Aprobado','Rechazado','Cancelado').
     */
    public enum Estado {
        Pendiente, Aprobado, Rechazado, Cancelado
    }

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

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Centro_costo getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(Centro_costo centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Usuario getAprobador() {
        return aprobador;
    }

    public void setAprobador(Usuario aprobador) {
        this.aprobador = aprobador;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Requerimiento{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fechaSolicitud=" + fechaSolicitud +
                ", solicitanteId=" + (solicitante != null ? solicitante.getId() : null) +
                ", proyectoId=" + (proyecto != null ? proyecto.getId() : null) +
                ", areaId=" + (area != null ? area.getId() : null) +
                ", centroCostoId=" + (centroCosto != null ? centroCosto.getId() : null) +
                ", etapa='" + etapa + '\'' +
                ", estado=" + estado +
                ", fechaAprobacion=" + fechaAprobacion +
                ", aprobadorId=" + (aprobador != null ? aprobador.getId() : null) +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}