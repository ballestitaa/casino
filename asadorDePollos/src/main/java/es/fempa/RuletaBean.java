package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Named
@SessionScoped
public class RuletaBean implements Serializable {

    // Números rojos de la ruleta europea; el resto (menos el 0) son negros
    private static final Set<Integer> ROJOS = Set.of(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);

    private final Random random = new Random();

    private String nombre = "Ruleta en Vivo";
    @Inject
    private UserBean userBean;

    // Ficha elegida actualmente (50, 100, 500 o 1000)
    private int fichaSeleccionada = 50;

    // Apuesta actual: casilla -> importe. Clave String: "0".."36" (y más adelante "rojo", "negro"...)
    private Map<String, Integer> apuestas = new LinkedHashMap<>();

    // Apuesta de la tirada anterior (para el botón Repeat)
    private Map<String, Integer> ultimaApuesta = new LinkedHashMap<>();

    // Mensaje para el jugador (errores y resultado de la tirada)
    private String mensaje = "";
    private boolean ultimaGanada = false;

    // ---------- Getters ----------

    public String getNombre() {
        return nombre;
    }

    public int getSaldo() {
        return (int) userBean.getSaldo();
    }

    public int getFichaSeleccionada() {
        return fichaSeleccionada;
    }

    public Map<String, Integer> getApuestas() {
        return apuestas;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isUltimaGanada() {
        return ultimaGanada;
    }

    // Valores de las fichas que se muestran en la mesa
    public List<Integer> getFichas() {
        return List.of(50, 100, 500, 1000);
    }

    // Total de fichas puestas ahora mismo en la mesa
    public int getTotalApostado() {
        return apuestas.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Importe apostado en una casilla (0 si no hay nada)
    public int importe(String casilla) {
        return apuestas.getOrDefault(casilla, 0);
    }

    // Números del tapete por columnas (3, 2, 1, 6, 5, 4...), como en una mesa real
    public List<Integer> getNumeros() {
        List<Integer> numeros = new ArrayList<>();
        for (int columna = 0; columna < 12; columna++) {
            numeros.add(columna * 3 + 3);
            numeros.add(columna * 3 + 2);
            numeros.add(columna * 3 + 1);
        }
        return numeros;
    }

    // Clase CSS de cada casilla: "rojo" o "negro"
    public String color(int numero) {
        return ROJOS.contains(numero) ? "rojo" : "negro";
    }

    // ---------- Acciones de la mesa ----------

    // Clic en una ficha (50, 100, 500, 1000)
    public void seleccionarFicha(int valor) {
        this.fichaSeleccionada = valor;
    }

    // Clic en una casilla: coloca la ficha seleccionada
    public void apostar(String casilla) {
        if (fichaSeleccionada > (int) userBean.getSaldo()) {
            mensaje = "Saldo insuficiente para esa ficha";
            ultimaGanada = false;
            return;
        }
        apuestas.merge(casilla, fichaSeleccionada, Integer::sum);
        userBean.setSaldo(userBean.getSaldo() - fichaSeleccionada);
        mensaje = "";
    }

    // Clear: devuelve las fichas al saldo
    public void limpiar() {
        userBean.setSaldo(userBean.getSaldo() + getTotalApostado());
        apuestas.clear();
        mensaje = "";
    }

    // Repeat: vuelve a colocar la apuesta de la tirada anterior
    public void repetir() {
        if (ultimaApuesta.isEmpty()) {
            mensaje = "Todavía no hay ninguna apuesta anterior";
            ultimaGanada = false;
            return;
        }
        int total = ultimaApuesta.values().stream().mapToInt(Integer::intValue).sum();
        if (total > (int) userBean.getSaldo() + getTotalApostado()) {
            mensaje = "Saldo insuficiente para repetir la apuesta";
            ultimaGanada = false;
            return;
        }
        limpiar();
        apuestas.putAll(ultimaApuesta);
        userBean.setSaldo(userBean.getSaldo() - total);
    }

    // Spin: sortea el número, paga y deja la mesa limpia
    public void girar() {
        if (apuestas.isEmpty()) {
            mensaje = "Haz una apuesta antes de girar";
            ultimaGanada = false;
            return;
        }
        int ganador = random.nextInt(37); // 0..36
        int premio = 0;
        for (Map.Entry<String, Integer> e : apuestas.entrySet()) {
            String casilla = e.getKey();
            int importe = e.getValue();
            if (casilla.equals(String.valueOf(ganador))) {
                premio += importe * 36;             // pleno: paga 35 a 1 (más tu ficha)
            } else if (ganador != 0 && casilla.equals(color(ganador))) {
                premio += importe * 2;              // rojo/negro: paga 1 a 1 (más tu ficha)
            }
        }
        int apostado = getTotalApostado();
        userBean.setSaldo(userBean.getSaldo() + premio);
        ultimaApuesta = new LinkedHashMap<>(apuestas);
        apuestas.clear();

        String colorTxt = ganador == 0 ? "verde" : color(ganador);
        ultimaGanada = premio > apostado;
        mensaje = "Ha salido el " + ganador + " (" + colorTxt + "). "
                + (premio > 0 ? "Recibes " + premio + " €" : "Pierdes " + apostado + " €");
    }

    // ---------- Navegación ----------

    // Botón "Jugar" de ruleta.xhtml: entra en la sala
    public String entrar() {
        resultado = "";
        return "salaRuleta?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "ruleta?faces-redirect=true";
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
        return "salaRuleta?faces-redirect=true";
    }

    // Se llama al terminar el retardo: aplica el saldo y muestra el mensaje
    public String revelar() {
        if (!pendiente) {
            return "salaRuleta?faces-redirect=true";
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
        return "salaRuleta?faces-redirect=true";
    }
}
