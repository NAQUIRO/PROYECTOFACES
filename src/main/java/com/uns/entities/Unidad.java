package com.uns.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unidad")
public class Unidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "abreviatura", length = 30)
    private String abreviatura;
    
    @Column(name = "descripcion", length = 50)
    private String descripcion;
    
    @Column(name = "estado", length = 20)
    private String estado;

    public Unidad() {
        super();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Unidad{" +
                "id=" + id +
                ", abreviatura='" + abreviatura + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}