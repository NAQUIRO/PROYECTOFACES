package com.uns.controllers;

import com.uns.data.UsuarioDao;
import com.uns.data.RolDao;
import com.uns.data.AreaDao;
import com.uns.entities.Usuario;
import com.uns.entities.Rol;
import com.uns.entities.Area;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

@Named("usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioBean.class);

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private RolDao rolDao;

    @Inject
    private AreaDao areaDao;

    private Usuario usuario = new Usuario();
    private Long rolId;
    private Long areaId;
    private String confirmarPassword;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public List<Usuario> getAll() {
        try {
            return usuarioDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los usuarios");
            return null;
        }
    }

    public List<Rol> getAllRoles() {
        try {
            return rolDao.getAll();
        } catch (Exception e) {
            logger.error("Error al obtener roles: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar los roles");
            return null;
        }
    }

    public List<Area> getAllAreas() {
        try {
            return areaDao.getAllActivos();
        } catch (Exception e) {
            logger.error("Error al obtener áreas: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudieron cargar las áreas");
            return null;
        }
    }

    public String create() {
        try {
            // Validaciones
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre de usuario es requerido");
                return null;
            }

            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                addErrorMessage("Validación", "La contraseña es requerida");
                return null;
            }

            if (confirmarPassword == null || confirmarPassword.isEmpty()) {
                addErrorMessage("Validación", "Debe confirmar la contraseña");
                return null;
            }

            if (!usuario.getPassword().equals(confirmarPassword)) {
                addErrorMessage("Validación", "Las contraseñas no coinciden");
                return null;
            }

            if (rolId == null) {
                addErrorMessage("Validación", "El rol es requerido");
                return null;
            }

            usuario.setEstado(Usuario.Estado.Activo);

            Rol rol = rolDao.getById(rolId);
            if (rol == null) {
                addErrorMessage("Error", "Rol no encontrado");
                return null;
            }
            usuario.setRol(rol);

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                if (area == null) {
                    addErrorMessage("Error", "Área no encontrada");
                    return null;
                }
                usuario.setArea(area);
            }

            usuarioDao.create(usuario);
            addInfoMessage("Éxito", "Usuario creado correctamente");
            logger.info("Usuario creado: {}", usuario.getUsername());
            return "/pages/usuarios/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo crear el usuario");
            return null;
        }
    }

    public String update() {
        try {
            // Validaciones
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                addErrorMessage("Validación", "El nombre de usuario es requerido");
                return null;
            }

            if (rolId == null) {
                addErrorMessage("Validación", "El rol es requerido");
                return null;
            }

            Rol rol = rolDao.getById(rolId);
            if (rol == null) {
                addErrorMessage("Error", "Rol no encontrado");
                return null;
            }
            usuario.setRol(rol);

            if (areaId != null) {
                Area area = areaDao.getById(areaId);
                if (area == null) {
                    addErrorMessage("Error", "Área no encontrada");
                    return null;
                }
                usuario.setArea(area);
            }

            // Manejo de contraseña
            if (confirmarPassword != null && !confirmarPassword.isEmpty()) {
                if (!confirmarPassword.equals(usuario.getPassword())) {
                    addErrorMessage("Validación", "Las contraseñas no coinciden");
                    return null;
                }
                // La contraseña ya está en usuario.password, se actualizará
            } else {
                // Si no se proporciona password, mantener el actual
                Usuario usuarioActual = usuarioDao.getById(usuario.getId());
                if (usuarioActual != null) {
                    usuario.setPassword(usuarioActual.getPassword());
                }
            }

            usuarioDao.update(usuario);
            addInfoMessage("Éxito", "Usuario actualizado correctamente");
            logger.info("Usuario actualizado: {}", usuario.getUsername());
            return "/pages/usuarios/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo actualizar el usuario");
            return null;
        }
    }

    public String erase() {
        try {
            Long id = this.usuario.getId();
            this.usuario = usuarioDao.getById(id);
            if (usuario == null) {
                addErrorMessage("Error", "Usuario no encontrado");
                return null;
            }
            usuarioDao.delete(usuario);
            addInfoMessage("Éxito", "Usuario eliminado correctamente");
            logger.info("Usuario eliminado: {}", id);
            return "/pages/usuarios/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo eliminar el usuario");
            return null;
        }
    }

    public String add() {
        return "/pages/usuarios/add?faces-redirect=true";
    }

    public String edit() {
        try {
            Long id = this.usuario.getId();
            this.usuario = usuarioDao.getById(id);
            if (usuario == null) {
                addErrorMessage("Error", "Usuario no encontrado");
                return "/pages/usuarios/index?faces-redirect=true";
            }
            if (usuario.getRol() != null) {
                this.rolId = usuario.getRol().getId();
            }
            if (usuario.getArea() != null) {
                this.areaId = usuario.getArea().getId();
            }
            // Limpiar password para edición
            confirmarPassword = null;
            return "/pages/usuarios/edit";
        } catch (Exception e) {
            logger.error("Error al cargar usuario para edición: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el usuario");
            return "/pages/usuarios/index?faces-redirect=true";
        }
    }

    public String show() {
        try {
            Long id = this.usuario.getId();
            this.usuario = usuarioDao.getById(id);
            if (usuario == null) {
                addErrorMessage("Error", "Usuario no encontrado");
                return "/pages/usuarios/index?faces-redirect=true";
            }
            return "/pages/usuarios/show";
        } catch (Exception e) {
            logger.error("Error al cargar usuario: {}", e.getMessage(), e);
            addErrorMessage("Error", "No se pudo cargar el usuario");
            return "/pages/usuarios/index?faces-redirect=true";
        }
    }

    public String index() {
        return "/pages/usuarios/index";
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