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
@Entity
@Table(name = "area")
public class Area implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String codigo;

    @Column(length = 100, nullable = false)
    private String nombre;

    /**
     * Relación con el centro de costo al que pertenece esta área.
     */
    @ManyToOne
    @JoinColumn(name = "centro_costo_id")
    private Centro_costo centroCosto;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Activo;

    /**
     * Estados permitidos para un área de negocio.
     */
    public enum Estado {
        Activo,
        Inactivo
    }

    public Area() {
        // Constructor por defecto
    }

    // Getters y setters
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Centro_costo getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(Centro_costo centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", centroCosto=" + (centroCosto != null ? centroCosto.getId() : null) +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                '}';
    }
}