package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class PokerBean implements Serializable {

    private String nombre = "Póker Classic";
    private int saldo = 1000;

    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    // Botón "Jugar" de poker.xhtml: entra en la sala
    public String entrar() {
        return "salaPoker?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "poker?faces-redirect=true";
    }
}
