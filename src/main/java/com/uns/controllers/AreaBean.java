package com.uns.controllers;

import com.uns.data.AreaDao;
import com.uns.data.CentroCostoDao;
import com.uns.entities.Area;
import com.uns.entities.Centro_costo;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("areaBean")
@SessionScoped
public class AreaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AreaBean.class);

    @Inject
    private AreaDao areaDao;

    @Inject
    private CentroCostoDao centroCostoDao;

    private Area area = new Area();
    private Long centroCostoId;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Long getCentroCostoId() {
        return centroCostoId;
    }

    public void setCentroCostoId(Long centroCostoId) {
        this.centroCostoId = centroCostoId;
    }

    public List<Area> getAll() {
        try {
            List<Area> result = areaDao.getAll();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener áreas: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las áreas");
            return List.of();
        }
    }

    public List<Area> getAllActivos() {
        try {
            List<Area> result = areaDao.getAllActivos();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener áreas activas: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las áreas activas");
            return List.of();
        }
    }

    public List<Centro_costo> getAllCentrosCosto() {
        try {
            List<Centro_costo> result = centroCostoDao.getAllActivos();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener centros de costo: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los centros de costo");
            return List.of();
        }
    }

    public String create() {
        try {
            if (area.getNombre() == null || area.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            area.setEstado(Area.Estado.Activo);

            if (centroCostoId != null) {
                Centro_costo centroCosto = centroCostoDao.getById(centroCostoId);
                if (centroCosto == null) {
                    addErrorMessage("Error", "Centro de costo no encontrado");
                    return null;
                }
                area.setCentroCosto(centroCosto);
            }

            areaDao.create(area);
            addInfoMessage("Éxito", "Área creada correctamente");
            logger.info("Área creada: {}", area.getNombre());
            return "/pages/areas/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear área: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el área");
            return null;
        }
    }

    public String update() {
        try {
            if (area.getNombre() == null || area.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            if (centroCostoId != null) {
                Centro_costo centroCosto = centroCostoDao.getById(centroCostoId);
                if (centroCosto == null) {
                    addErrorMessage("Error", "Centro de costo no encontrado");
                    return null;
                }
                area.setCentroCosto(centroCosto);
            }

            areaDao.update(area);
            addInfoMessage("Éxito", "Área actualizada correctamente");
            logger.info("Área actualizada: {}", area.getNombre());
            return "/pages/areas/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar área: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el área");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.area.getId();
            this.area = areaDao.getById(id);
            if (area == null) {
                addErrorMessage("Error", "Área no encontrada");
                return null;
            }
            areaDao.delete(area);
            addInfoMessage("Éxito", "Área eliminada correctamente");
            logger.info("Área eliminada: {}", id);
            return "/pages/areas/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar área: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el área");
            return null;
        }
    }

    public String add() {
        return "/pages/areas/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.area.getId();
            this.area = areaDao.getById(id);
            if (area == null) {
                addErrorMessage("Error", "Área no encontrada");
                return "/pages/areas/index?faces-redirect=true";
            }
            if (area.getCentroCosto() != null) {
                this.centroCostoId = area.getCentroCosto().getId();
            }
            return "/pages/areas/edit";
        } catch (Exception e) {
            logger.error("Error al cargar área para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el área");
            return "/pages/areas/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.area.getId();
            this.area = areaDao.getById(id);
            if (area == null) {
                addErrorMessage("Error", "Área no encontrada");
                return "/pages/areas/index?faces-redirect=true";
            }
            return "/pages/areas/show";
        } catch (Exception e) {
            logger.error("Error al cargar área: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el área");
            return "/pages/areas/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/areas/index";
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