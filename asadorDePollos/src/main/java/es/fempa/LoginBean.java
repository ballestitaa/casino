package es.fempa;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
// poner comprobaciones en el post-construct
@Named
@ViewScoped
public class LoginBean implements Serializable {
    @Inject UserBean userBean;

    private String usuario;
    private String password;
    private String mensaje;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void login() {
        if(usuario.isEmpty() || password.isEmpty()) {
            mensaje = "El usuario y la contraseña son obligatorios";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El usuario y la contraseña son obligatorios", null));
            return;
        }

        if(usuario.equals("user") && password.equals("pass")) {
            mensaje = "Login exitoso";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login exitoso", null));

            /*
            userBean.setUsuario(usuario);
            userBean.setNombre("Nombre del Usuario");
            userBean.setLoggedIn(true);
            */

        } else {
            mensaje = "Usuario o contraseña incorrectos";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrectos", null));
        }
    }


}
