package es.fempa;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginBean implements Serializable {

    @Inject
    private UserBean userBean;

    @Inject
    private UserService userService;

    private String usuario;
    private String password;

    @PostConstruct
    public void init() {
        if (userBean != null && userBean.isLoggedIn()) {
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                String contextPath = facesContext.getExternalContext().getRequestContextPath();
                facesContext.getExternalContext().redirect(contextPath + "/home.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String login() {
        if (usuario == null || usuario.isBlank() || password == null || password.isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "El usuario y la contraseña son obligatorios", null));
            return null;
        }
        if (userService != null && userService.getUsers() != null) {
            for (User u : userService.getUsers()) {
                if (u.getUsuario().equals(usuario) && u.getPassword().equals(password)) {
                    userBean.setUsuario(u.getUsuario());
                    userBean.setNombre(u.getNombre());
                    userBean.setEmail(u.getEmail());
                    userBean.setRol(u.getRol());
                    userBean.setSaldo(u.getSaldo());
                    userBean.setLoggedIn(true);

                    return "/home.xhtml?faces-redirect=true";
                }
            }
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrectos", null));
        return null;
    }

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

}