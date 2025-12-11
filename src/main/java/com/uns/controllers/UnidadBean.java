package com.uns.controllers;

import com.uns.data.UnidadDao;
import com.uns.entities.Unidad;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("unidadBean")
@RequestScoped
public class UnidadBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UnidadBean.class);

    @Inject
    private UnidadDao unidadDao;

    private Unidad unidad = new Unidad();

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public List<Unidad> getAll() {
        try {
            return unidadDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener unidades: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las unidades");
            return null;
        }
    }

    public List<Unidad> getAllActivos() {
        try {
            return unidadDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener unidades activas: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las unidades activas");
            return null;
        }
    }

    public String create() {
        try {
            if (unidad.getAbreviatura() == null || unidad.getAbreviatura().trim().isEmpty()) {
                addErrorMessage("Validación", "La abreviatura es requerida");
                return null;
            }

            unidad.setEstado("Activo");
            unidadDao.create(unidad);
            addInfoMessage("Éxito", "Unidad creada correctamente");
            logger.info("Unidad creada: {}", unidad.getAbreviatura());
            return "/pages/unidades/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear unidad: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear la unidad");
            return null;
        }
    }

    public String update() {
        try {
            if (unidad.getAbreviatura() == null || unidad.getAbreviatura().trim().isEmpty()) {
                addErrorMessage("Validación", "La abreviatura es requerida");
                return null;
            }

            unidadDao.update(unidad);
            addInfoMessage("Éxito", "Unidad actualizada correctamente");
            logger.info("Unidad actualizada: {}", unidad.getAbreviatura());
            return "/pages/unidades/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar unidad: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar la unidad");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.unidad.getId();
            this.unidad = unidadDao.getById(id);
            if (unidad == null) {
                addErrorMessage("Error", "Unidad no encontrada");
                return null;
            }
            unidadDao.delete(unidad);
            addInfoMessage("Éxito", "Unidad eliminada correctamente");
            logger.info("Unidad eliminada: {}", id);
            return "/pages/unidades/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar unidad: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar la unidad");
            return null;
        }
    }

    public String add() {
        return "/pages/unidades/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.unidad.getId();
            this.unidad = unidadDao.getById(id);
            if (unidad == null) {
                addErrorMessage("Error", "Unidad no encontrada");
                return "/pages/unidades/index?faces-redirect=true";
            }
            return "/pages/unidades/edit";
        } catch (Exception e) {
            logger.error("Error al cargar unidad para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar la unidad");
            return "/pages/unidades/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.unidad.getId();
            this.unidad = unidadDao.getById(id);
            if (unidad == null) {
                addErrorMessage("Error", "Unidad no encontrada");
                return "/pages/unidades/index?faces-redirect=true";
            }
            return "/pages/unidades/show";
        } catch (Exception e) {
            logger.error("Error al cargar unidad: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar la unidad");
            return "/pages/unidades/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/unidades/index";
    }

    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }
}