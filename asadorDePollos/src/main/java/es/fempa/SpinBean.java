package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SpinBean implements Serializable {

    private String nombre = "Pirots 3";
    private int saldo = 1000;

    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    // Botón "Jugar" de pirots.xhtml: entra en la sala de máquinas
    public String entrar() {
        return "salaSpin?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "pirots?faces-redirect=true";
    }
}
