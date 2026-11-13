package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named
@SessionScoped
public class PokerBean implements Serializable {

    private String nombre = "Póker Classic";
    @Inject
    private UserBean userBean;

    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return (int) userBean.getSaldo();
    }

    // Botón "Jugar" de poker.xhtml: entra en la sala
    public String entrar() {
        resultado = "";
        return "salaPoker?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "poker?faces-redirect=true";
    }

    // Campo de apuesta del componente comun (comp:apuesta)
    private Integer apuesta;
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

    public Integer getApuesta() {
        return apuesta;
    }

    public void setApuesta(Integer apuesta) {
        this.apuesta = apuesta;
    }


    // Boton "Jugar" del componente: resuelve la apuesta al azar y actualiza el saldo

    // Apostar: el resultado queda decidido, pero el saldo y el mensaje esperan a revelar()
    public String jugar() {
        apuestaJugada = apuesta;
        ganado = Math.random() < 0.5;
        retardoMs = (int) (1000 + Math.random() * 4001);
        pendiente = true;
        resultado = "";
        importe = "";
        apuesta = null;
        return "salaPoker?faces-redirect=true";
    }

    // Se llama al terminar el retardo: aplica el saldo y muestra el mensaje
    public String revelar() {
        if (!pendiente) {
            return "salaPoker?faces-redirect=true";
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
        pendiente = false;
        return "salaPoker?faces-redirect=true";
    }
}
