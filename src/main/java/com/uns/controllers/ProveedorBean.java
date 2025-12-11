package com.uns.controllers;

import com.uns.data.ProveedorDao;
import com.uns.entities.Proveedor;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("proveedorBean")
@RequestScoped
public class ProveedorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProveedorDao proveedorDao;

    private Proveedor proveedor = new Proveedor();

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Proveedor> getAll() {
        try {
            return proveedorDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String create() {
        proveedor.setEstado(Proveedor.Estado.Activo);
        proveedorDao.create(proveedor);
        return "/pages/proveedores/index.xhtml?faces-redirect=true";
    }

    public String update() {
        proveedorDao.update(proveedor);
        return "/pages/proveedores/index.xhtml?faces-redirect=true";
    }

    public String erase() {
        Long id = this.proveedor.getId();
        this.proveedor = proveedorDao.getById(id);
        proveedorDao.delete(proveedor);
        return "/pages/proveedores/index.xhtml?faces-redirect=true";
    }

    public String add() {
        return "/pages/proveedores/add?faces-redirect=true";
    }

    public String edit() {
        Long id = this.proveedor.getId();
        this.proveedor = proveedorDao.getById(id);
        return "/pages/proveedores/edit";
    }

    public String show() {
        Long id = this.proveedor.getId();
        this.proveedor = proveedorDao.getById(id);
        return "/pages/proveedores/show";
    }
}