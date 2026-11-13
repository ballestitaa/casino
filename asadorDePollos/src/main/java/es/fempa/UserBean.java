package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private String usuario;
    private String nombre;
    private boolean loggedIn;
    private float saldo = 100.0f;
    private String email;
    private String rol;
    // Mensaje que se muestra una sola vez en home tras el login (sustituye al Flash)
    private String mensajeBienvenida;

    public String getMensajeBienvenida() {
        return mensajeBienvenida;
    }

    public void setMensajeBienvenida(String mensajeBienvenida) {
        this.mensajeBienvenida = mensajeBienvenida;
    }

    /**
     * viewAction de home: si hay mensaje pendiente lo añade al growl y lo borra,
     * para que solo se vea una vez.
     */
    public void mostrarBienvenida() {
        if (mensajeBienvenida != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, mensajeBienvenida, null));
            mensajeBienvenida = null;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void logout() {
        this.usuario = null;
        this.nombre = null;
        this.loggedIn = false;
        this.saldo = 0.0f;
        this.email = null;
        this.rol = null;
        this.mensajeBienvenida = null;

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String comprobarSesion() {
        if (!loggedIn) {
            return "/sesion/login.xhtml?faces-redirect=true";
        }
        return null;
    }
}
