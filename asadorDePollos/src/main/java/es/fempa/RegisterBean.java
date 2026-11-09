package es.fempa;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class RegisterBean implements Serializable {
    private String nombre;
    private String email;
    private String usuario;
    private String password = "";
    private String password2 = "";

    @Inject UserService userService;

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String registrar() {

        // comprobar que todos los campos esten llenos
        if(usuario == null || usuario.isBlank() || password == null || password.isBlank() || nombre == null || nombre.isBlank() || email == null || email.isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Todos los campos son obligatorios", null));
            return null;
        }

        // comprobar que el usuario no exista
        if (userService.getUsers() != null) {
            for (User u : userService.getUsers()) {
                if (u.getUsuario().equalsIgnoreCase(usuario.trim())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario ya existe", null));
                    return null;
                }
            }
        }

        // comprobar que las contraseñas coincidan
        if (!password2.equals(password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Las contraseñas no coinciden", null));
            return null;
        }

        // quitar espacios al principio y al final por si se ponen por accidente
        usuario = usuario.trim();
        email = email.trim();

        User user = new User(usuario, password, nombre, 100.0f, email, "user");
        userService.register(user);
        return "/login.xhtml?faces-redirect=true";

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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