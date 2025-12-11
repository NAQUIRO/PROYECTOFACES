package com.uns.entities;

/**
 *
 * @author Dayan
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.*;

@Entity
@Table(name = "unidad")
public class Unidad implements Serializable {

    private static final long serialVersionUID = 1L;

    public Unidad() {
        super();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String abreviatura;
    @Column
    private String descripcion;
    @Column
    private String estado;

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
        return "Unidad{"
                + "id=" + id
                + ", abreviatura='" + abreviatura + '\''
                + ", descripcion='" + descripcion + '\''
                + ", estado='" + estado + '\''
                + '}';
    }

}
