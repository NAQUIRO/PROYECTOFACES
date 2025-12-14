package com.uns.controllers;

import com.uns.data.RolDao;
import com.uns.entities.Rol;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("rolBean")
@SessionScoped
public class RolBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RolBean.class);

    @Inject
    private RolDao rolDao;

    private Rol rol = new Rol();

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getAll() {
        try {
            List<Rol> result = rolDao.getAll();
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener roles: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los roles");
            return List.of();
        }
    }

    public List<Rol> getAllActivos() {
        try {
            List<Rol> result = rolDao.getAll(); // RolDao no tiene getAllActivos, usa getAll
            return result != null ? result : List.of();
        } catch (Exception e) {
            logger.error("Error al obtener roles activos: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los roles activos");
            return List.of();
        }
    }

    public String create() {
        try {
            if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            rol.setEstado(Rol.Estado.Activo);
            rolDao.create(rol);
            addInfoMessage("Éxito", "Rol creado correctamente");
            logger.info("Rol creado: {}", rol.getNombre());
            return "/pages/roles/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear rol: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el rol");
            return null;
        }
    }

    public String update() {
        try {
            if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre es requerido");
                return null;
            }

            rolDao.update(rol);
            addInfoMessage("Éxito", "Rol actualizado correctamente");
            logger.info("Rol actualizado: {}", rol.getNombre());
            return "/pages/roles/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar rol: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el rol");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.rol.getId();
            this.rol = rolDao.getById(id);
            if (rol == null) {
                addErrorMessage("Error", "Rol no encontrado");
                return null;
            }
            rolDao.delete(rol);
            addInfoMessage("Éxito", "Rol eliminado correctamente");
            logger.info("Rol eliminado: {}", id);
            return "/pages/roles/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar rol: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el rol");
            return null;
        }
    }

    public String add() {
        return "/pages/roles/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.rol.getId();
            this.rol = rolDao.getById(id);
            if (rol == null) {
                addErrorMessage("Error", "Rol no encontrado");
                return "/pages/roles/index?faces-redirect=true";
            }
            return "/pages/roles/edit";
        } catch (Exception e) {
            logger.error("Error al cargar rol para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el rol");
            return "/pages/roles/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.rol.getId();
            this.rol = rolDao.getById(id);
            if (rol == null) {
                addErrorMessage("Error", "Rol no encontrado");
                return "/pages/roles/index?faces-redirect=true";
            }
            return "/pages/roles/show";
        } catch (Exception e) {
            logger.error("Error al cargar rol: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el rol");
            return "/pages/roles/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/roles/index";
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