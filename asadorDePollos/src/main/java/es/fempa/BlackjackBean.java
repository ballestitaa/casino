package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named  //
@SessionScoped  //ciclo de vida del Bean
public class BlackjackBean implements Serializable {

    private String nombre = "Blackjack en Vivo";
    private int saldo = 1000;
    private Integer apuesta;        // lo que escribe el jugador en el campo
    private int apuestaEnMesa = 0;  // lo que ya ha apostado y está en la mesa


    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    public int getApuestaEnMesa() {
        return apuestaEnMesa;
    }

    // Getter y setter del campo de apuesta
    public Integer getApuesta() {
        return apuesta;
    }

    public void setApuesta(Integer apuesta) {
        this.apuesta = apuesta;
    }

    // Botón "Jugar" de la sala: pone la apuesta en la mesa y la resta del saldo
    public String jugar() {
        FacesContext contexto = FacesContext.getCurrentInstance();

        // La página ya valida, pero se vuelve a comprobar aquí por seguridad
        /*if (apuesta == null || apuesta <= 0 || apuesta > saldo) {
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "La apuesta debe estar entre 1 y " + saldo + " €", null));
            return null; // se queda en la página mostrando el error
        }*/

        saldo -= apuesta;
        apuestaEnMesa += apuesta;
        contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Apuesta realizada", "Has apostado " + apuesta + " €. ¡Suerte!"));
        apuesta = null;  // vacía el campo para la siguiente apuesta
        // El mensaje sobrevive a la redirección
        contexto.getExternalContext().getFlash().setKeepMessages(true);

        // Post/Redirect/Get: el navegador vuelve a cargar la sala con un GET,
        // así que refrescar (F5) ya no repite la apuesta
        return "salaBlackjack?faces-redirect=true";
    }

    // Botón "Jugar" de blackjack.xhtml: entra en la sala
    public String entrar() {
        return "salaBlackjack?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "blackjack?faces-redirect=true";
    }
}
