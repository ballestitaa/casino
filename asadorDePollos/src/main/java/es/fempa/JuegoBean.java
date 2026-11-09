package es.fempa;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.ResponsiveOption;

@Named
@ViewScoped
public class JuegoBean implements Serializable {

    private List<Juego> juegos;
    private List<ResponsiveOption> responsiveOptions;

    @PostConstruct
    public void init() {
        juegos = new ArrayList<>();
        juegos.add(new Juego("Ruleta en vivo", "Live roulette table, live croupier.",
                "ruleta.jpg", "/juegos/ruleta.html"));
        juegos.add(new Juego("Blackjack Royal", "Cartas, chips y dealer en vivo.",
                "blackjack.jpg", "/juegos/blackjack.html"));
        juegos.add(new Juego("Póker Classic", "Mesa de póker con jugadores en vivo.",
                "poker.jpg", "/juegos/poker.html"));
        juegos.add(new Juego("Pirots 3", "Tragaperras del viejo oeste con premios increíbles.",
                "tragaperras.jpg", "/juegos/pirots.html"));

        // En pantallas de menos de 768px se muestra 1 tarjeta y se avanza de 1 en 1
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("768px", 1, 1));
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }
}
