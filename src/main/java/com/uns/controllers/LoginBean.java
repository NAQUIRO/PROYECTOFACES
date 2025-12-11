package com.uns.controllers;

import com.uns.data.UsuarioDao;
import com.uns.entities.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginBean.class);

    @Inject
    private UsuarioDao usuarioDao;

    private String username;
    private String password;
    private Usuario usuarioActual;

    public String login() {
        try {
            logger.info("========================================");
            logger.info("Intento de login para usuario: {}", username);
            logger.info("========================================");

            // Validar campos
            if (username == null || username.trim().isEmpty()) {
                logger.warn("Campo usuario vacío");
                addErrorMessage("Usuario requerido", "Debe ingresar un nombre de usuario");
                return null;
            }

            if (password == null || password.trim().isEmpty()) {
                logger.warn("Campo contraseña vacío");
                addErrorMessage("Contraseña requerida", "Debe ingresar una contraseña");
                return null;
            }

            // Buscar usuario
            logger.info("Buscando usuario en base de datos: {}", username);
            Usuario usuario = usuarioDao.findByUsername(username.trim());

            if (usuario == null) {
                logger.warn("✗ Usuario no encontrado en base de datos: {}", username);
                addErrorMessage("Error de autenticación", "Usuario o contraseña incorrectos");
                return null;
            }

            logger.info("✓ Usuario encontrado: {} (ID: {})", usuario.getUsername(), usuario.getId());

            // Verificar contraseña
            if (!usuario.getPassword().equals(password)) {
                logger.warn("✗ Contraseña incorrecta para usuario: {}", username);
                addErrorMessage("Error de autenticación", "Usuario o contraseña incorrectos");
                return null;
            }

            logger.info("✓ Contraseña válida");

            // Verificar estado
            if (usuario.getEstado() != Usuario.Estado.Activo) {
                logger.warn("✗ Usuario inactivo intentó iniciar sesión: {}", username);
                addErrorMessage("Usuario inactivo", "Su cuenta ha sido desactivada. Contacte al administrador.");
                return null;
            }

            logger.info("✓ Usuario activo");

            // Login exitoso
            usuarioActual = usuario;
            logger.info("========================================");
            logger.info("✓ LOGIN EXITOSO para usuario: {}", username);
            logger.info("Rol: {}", usuario.getRol() != null ? usuario.getRol().getNombre() : "N/A");
            logger.info("Área: {}", usuario.getArea() != null ? usuario.getArea().getNombre() : "N/A");
            logger.info("========================================");
            addInfoMessage("Bienvenido", "Inicio de sesión exitoso: " + usuarioActual.getNombres());

            // Limpiar campos
            username = null;
            password = null;

            return "/pages/dashboard/index?faces-redirect=true";

        } catch (Exception e) {
            logger.error("========================================");
            logger.error("✗ ERROR CRÍTICO durante el login");
            logger.error("========================================");
            logger.error("Usuario: {}", username);
            logger.error("Tipo de error: {}", e.getClass().getName());
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Stack trace:", e);
            logger.error("========================================");
            
            addErrorMessage("Error del sistema",
                    "Ha ocurrido un error al intentar iniciar sesión. Verifique los logs para más detalles.");
            return null;
        }
    }

    public String logout() {
        try {
            if (usuarioActual != null) {
                logger.info("Usuario {} cerró sesión", usuarioActual.getUsername());
            }
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "/login?faces-redirect=true";
        } catch (Exception e) {
            logger.error("Error durante logout: {}", e.getMessage(), e);
            return "/login?faces-redirect=true";
        }
    }

    public boolean isLoggedIn() {
        return usuarioActual != null;
    }

    public String getNombreCompleto() {
        if (usuarioActual != null) {
            return usuarioActual.getNombres() + " " + usuarioActual.getApellidos();
        }
        return "";
    }

    // Métodos de mensajes
    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}