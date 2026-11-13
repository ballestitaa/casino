package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named  //
@SessionScoped  //ciclo de vida del Bean
public class BlackjackBean implements Serializable {

    private String nombre = "Blackjack en Vivo";
    @Inject
    private UserBean userBean;
    private Integer apuesta;        // lo que escribe el jugador en el campo
    // Texto del resultado de la ultima apuesta, se muestra debajo del boton Jugar
    private String resultado = "";
    private String importe = "";

    public String getImporte() {
        return importe;
    }
    // Apuesta pendiente de revelar (el saldo cambia cuando se muestra el resultado)
    private int apuestaJugada;
    private boolean pendiente;

    public boolean isPendiente() {
        return pendiente;
    }
    // Si la ultima apuesta gano y cuantos milisegundos tarda en mostrarse (1 a 5 s)
    private boolean ganado;
    private int retardoMs;

    public boolean isGanado() {
        return ganado;
    }

    public int getRetardoMs() {
        return retardoMs;
    }

    public String getResultado() {
        return resultado;
    }
    private int apuestaEnMesa = 0;  // lo que ya ha apostado y está en la mesa


    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return (int) userBean.getSaldo();
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



    // Botón "Jugar" de blackjack.xhtml: entra en la sala
    public String entrar() {
        resultado = "";
        return "salaBlackjack?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "blackjack?faces-redirect=true";
    }
    // Boton "Jugar" de la sala: pone la apuesta en la mesa y la resuelve al azar

    // Apostar: el resultado queda decidido, pero el saldo y el mensaje esperan a revelar()
    public String jugar() {
        apuestaJugada = apuesta;
        ganado = Math.random() < 0.5;
        retardoMs = (int) (1000 + Math.random() * 4001);
        pendiente = true;
        resultado = "";
        importe = "";
        apuesta = null;
        apuestaEnMesa = apuestaJugada;
        return "salaBlackjack?faces-redirect=true";
    }

    // Se llama al terminar el retardo: aplica el saldo y muestra el mensaje
    public String revelar() {
        if (!pendiente) {
            return "salaBlackjack?faces-redirect=true";
        }
        int ap = apuestaJugada;
        if (ganado) {
            userBean.setSaldo(userBean.getSaldo() + ap);
            resultado = "¡Has ganado!";
            importe = "+" + ap + " €";
        } else {
            userBean.setSaldo(userBean.getSaldo() - ap);
            resultado = "Has perdido";
            importe = "−" + ap + " €";
        }
        apuestaEnMesa = 0;
        pendiente = false;
        return "salaBlackjack?faces-redirect=true";
    }
}
