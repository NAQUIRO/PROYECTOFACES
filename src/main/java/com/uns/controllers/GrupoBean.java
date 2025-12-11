package com.uns.controllers;

import com.uns.data.GrupoDao;
import com.uns.entities.Grupo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("grupoBean")
@RequestScoped
public class GrupoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private GrupoDao grupoDao;

    private Grupo grupo = new Grupo();

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Grupo> getAll() {
        try {
            return grupoDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Grupo> getAllActivos() {
        try {
            return grupoDao.getAllActivos();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String create() {
        grupo.setEstado(Grupo.Estado.Activo);
        grupoDao.create(grupo);
        return "/pages/grupos/index.xhtml?faces-redirect=true";
    }

    public String update() {
        grupoDao.update(grupo);
        return "/pages/grupos/index.xhtml?faces-redirect=true";
    }

    public String erase() {
        Long id = this.grupo.getId();
        this.grupo = grupoDao.getById(id);
        grupoDao.delete(grupo);
        return "/pages/grupos/index.xhtml?faces-redirect=true";
    }

    public String add() {
        return "/pages/grupos/add?faces-redirect=true";
    }

    public String edit() {
        Long id = this.grupo.getId();
        this.grupo = grupoDao.getById(id);
        return "/pages/grupos/edit";
    }

    public String show() {
        Long id = this.grupo.getId();
        this.grupo = grupoDao.getById(id);
        return "/pages/grupos/show";
    }

    public String index() {
        return "/pages/grupos/index";
    }
}