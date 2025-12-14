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
@Table(name = "material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    public Material() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, name = "codcorrelativo")
    private String codcorrelativo;

    @Column(length = 75, nullable = false)
    private String nombre;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(length = 85)
    private String descripcion;

    @Column(length = 85)
    private String observacion;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Activo;

    @ManyToOne
    @JoinColumn(name = "idgrupo")
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "idunidad")
    private Unidad unidad;

    /**
     * Enumeration representing the state of a material. Mirrors
     * ENUM('Activo','Baja') in the database.
     */
    public enum Estado {
        Activo, Baja
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodcorrelativo() {
        return codcorrelativo;
    }

    public void setCodcorrelativo(String codcorrelativo) {
        this.codcorrelativo = codcorrelativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", codcorrelativo='" + codcorrelativo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", descripcion='" + descripcion + '\'' +
                ", observacion='" + observacion + '\'' +
                ", estado=" + estado +
                ", grupoId=" + (grupo != null ? grupo.getId() : null) +
                ", unidadId=" + (unidad != null ? unidad.getId() : null) +
                '}';
    }
}