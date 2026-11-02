package es.fempa;

import java.io.Serializable;

public class User implements Serializable {
    private String usuario;
    private String password;
    private String nombre;
    private float saldo;
    private String email;
    private String rol;

    public User() {
    }
    public User(String usuario, String password, String nombre, float saldo, String email, String rol) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.saldo = saldo;
        this.email = email;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
}
