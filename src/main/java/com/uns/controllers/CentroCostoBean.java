package com.uns.controllers;

import com.uns.data.CentroCostoDao;
import com.uns.entities.Centro_costo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("centroCostoBean")
@RequestScoped
public class CentroCostoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CentroCostoBean.class);

    @Inject
    private CentroCostoDao centroCostoDao;

    private Centro_costo centroCosto = new Centro_costo();

    public Centro_costo getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(Centro_costo centroCosto) {
        this.centroCosto = centroCosto;
    }

    public List<Centro_costo> getAll() {
        try {
            return centroCostoDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener centros de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los centros de costo");
            return null;
        }
    }

    public List<Centro_costo> getAllActivos() {
        try {
            return centroCostoDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener centros de costo activos: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los centros de costo activos");
            return null;
        }
    }

    public String create() {
        try {
            if (centroCosto.getNombre() == null || centroCosto.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            centroCosto.setEstado(Centro_costo.Estado.Activo);
            centroCostoDao.create(centroCosto);
            addInfoMessage("Éxito", "Centro de costo creado correctamente");
            logger.info("Centro de costo creado: {}", centroCosto.getNombre());
            return "/pages/centros-costo/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear centro de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el centro de costo");
            return null;
        }
    }

    public String update() {
        try {
            if (centroCosto.getNombre() == null || centroCosto.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            centroCostoDao.update(centroCosto);
            addInfoMessage("Éxito", "Centro de costo actualizado correctamente");
            logger.info("Centro de costo actualizado: {}", centroCosto.getNombre());
            return "/pages/centros-costo/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar centro de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el centro de costo");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.centroCosto.getId();
            this.centroCosto = centroCostoDao.getById(id);
            if (centroCosto == null) {
                addErrorMessage("Error", "Centro de costo no encontrado");
                return null;
            }
            centroCostoDao.delete(centroCosto);
            addInfoMessage("Éxito", "Centro de costo eliminado correctamente");
            logger.info("Centro de costo eliminado: {}", id);
            return "/pages/centros-costo/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar centro de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el centro de costo");
            return null;
        }
    }

    public String add() {
        return "/pages/centros-costo/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.centroCosto.getId();
            this.centroCosto = centroCostoDao.getById(id);
            if (centroCosto == null) {
                addErrorMessage("Error", "Centro de costo no encontrado");
                return "/pages/centros-costo/index?faces-redirect=true";
            }
            return "/pages/centros-costo/edit";
        } catch (Exception e) {
            logger.error("Error al cargar centro de costo para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el centro de costo");
            return "/pages/centros-costo/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.centroCosto.getId();
            this.centroCosto = centroCostoDao.getById(id);
            if (centroCosto == null) {
                addErrorMessage("Error", "Centro de costo no encontrado");
                return "/pages/centros-costo/index?faces-redirect=true";
            }
            return "/pages/centros-costo/show";
        } catch (Exception e) {
            logger.error("Error al cargar centro de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el centro de costo");
            return "/pages/centros-costo/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/centros-costo/index";
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