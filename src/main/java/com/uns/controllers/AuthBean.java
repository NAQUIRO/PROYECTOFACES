package com.uns.controllers;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named("authBean")
@SessionScoped
public class AuthBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean loggedIn;
    private String displayName;

    private final Map<String, String> demoUsers = new HashMap<>() {{
        put("admin", "admin123");
        put("operador", "compras2024");
    }};

    public String login() {
        if (username != null && demoUsers.containsKey(username) && demoUsers.get(username).equals(password)) {
            loggedIn = true;
            displayName = username.substring(0, 1).toUpperCase() + username.substring(1);
            addMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", "Autenticación exitosa");
            return "/pages/unidades/index?faces-redirect=true";
        }

        loggedIn = false;
        addMessage(FacesMessage.SEVERITY_WARN, "Acceso denegado", "Credenciales inválidas. Intente nuevamente");
        return null;
    }

    public String logout() {
        loggedIn = false;
        username = null;
        password = null;
        displayName = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        return "/pages/login?faces-redirect=true";
    }

    public void requireLogin() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String viewId = context.getViewRoot() != null ? context.getViewRoot().getViewId() : "";

        if (viewId != null && viewId.contains("login")) {
            return;
        }

        if (!loggedIn) {
            externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login.xhtml?faces-redirect=true");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getDisplayName() {
        return displayName;
    }
}
