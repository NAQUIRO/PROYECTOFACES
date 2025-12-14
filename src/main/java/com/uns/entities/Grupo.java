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
import jakarta.persistence.Table;
import java.io.*;
@Entity
@Table(name = "grupo")
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;

    public Grupo() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3, name = "codgrupo")
    private String codgrupo;

    @Column(length = 85, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Activo;

    /**
     * Enumeration of possible group states. Mirrors the ENUM('Activo','Baja')
     * definition in the database. Persisted as VARCHAR.
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

    public String getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(String codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", codgrupo='" + codgrupo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}