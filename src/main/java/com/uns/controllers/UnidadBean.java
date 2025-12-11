package com.uns.controllers;

import com.uns.data.UnidadDao;
import com.uns.entities.Unidad;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("unidadBean")
@RequestScoped
public class UnidadBean implements Serializable {

    private static final long serialVersionUID = 1L;

// DAO Implementation
    @Inject
    private UnidadDao unidadDao;
// Model
    private Unidad unidad = new Unidad();

    public Unidad getunidad() {
        return unidad;
    }

    public void setunidad(Unidad unidad) {
        this.unidad = unidad;
    }

// CRUD
    public List<Unidad> getAll() {
        try {
            List<Unidad> list = unidadDao.getAll();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String create() {
        unidad.setEstado("Activo");
        unidadDao.create(unidad);
        return "/pages/unidades/index.xhtml?faces-redirect=true";
    }

    public String update() {
        unidadDao.update(unidad);
        return "/pages/unidades/index.xhtml?faces-redirect=true";
    }

    public String erase() {
        Long id = this.unidad.getId();
        this.unidad = unidadDao.getById(id);
        unidadDao.delete(unidad);
        return "/pages/unidades/index.xhtml?faces-redirect=true";
    }

// Metodos para redireccionar y pasar parametros
    public String add() {
          return "/pages/unidades/add?faces-redirect=true";
    }

    public String edit() {
        Long id = this.unidad.getId();
        this.unidad = unidadDao.getById(id);
        return "/pages/unidades/edit";
    }

    public String show() {
         Long id = this.unidad.getId();
        this.unidad = unidadDao.getById(id);
        return "/pages/unidades/show";
      
    }
    
// Rutas
    public String index() {
        return "/pages/unidad/index";
    }
}
