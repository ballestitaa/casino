package es.fempa;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
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
    private int saldo = 1000;

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
        return saldo;
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
        if (fichaSeleccionada > saldo) {
            mensaje = "Saldo insuficiente para esa ficha";
            ultimaGanada = false;
            return;
        }
        apuestas.merge(casilla, fichaSeleccionada, Integer::sum);
        saldo -= fichaSeleccionada;
        mensaje = "";
    }

    // Clear: devuelve las fichas al saldo
    public void limpiar() {
        saldo += getTotalApostado();
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
        if (total > saldo + getTotalApostado()) {
            mensaje = "Saldo insuficiente para repetir la apuesta";
            ultimaGanada = false;
            return;
        }
        limpiar();
        apuestas.putAll(ultimaApuesta);
        saldo -= total;
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
        saldo += premio;
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
        return "salaRuleta?faces-redirect=true";
    }

    // Botón "Salir de la sala": vuelve a la página del juego
    public String salir() {
        return "ruleta?faces-redirect=true";
    }
}
