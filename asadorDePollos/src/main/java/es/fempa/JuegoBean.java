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
        // imagen: ruta relativa a resources/imgs/
        juegos.add(new Juego("Ruleta en vivo", "Live roulette table, live croupier.",
                "juegos/ruleta.jpg", "/juegos/ruleta.html"));
        juegos.add(new Juego("Blackjack Royal", "Cartas, chips y dealer en vivo.",
                "juegos/blackjack.jpg", "/juegos/blackjack.html"));
        juegos.add(new Juego("Póker Classic", "Mesa de póker con jugadores en vivo.",
                "juegos/poker.jpg", "/juegos/poker.html"));
        juegos.add(new Juego("Pirots 3", "Tragaperras del viejo oeste con premios increíbles.",
                "juegos/tragaperras.jpg", "/juegos/pirots.html"));
        juegos.add(new Juego("Ruleta Europea", "Un solo cero y apuestas desde 1 €.",
                "imagen_Ruleta.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Blackjack VIP", "Mesa privada con límites altos.",
                "imagen_BlackJack.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Texas Hold'em", "Torneos diarios con botes garantizados.",
                "imagen_Poker.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Gold Rush Slots", "Tragaperras de 5 rodillos con jackpot progresivo.",
                "imagen_Pirots.jpg", "/juegos/juego.html"));

        // PrimeFaces aplica la opcion cuyo breakpoint (max-width) cubre el ancho de la ventana:
        //   >= 1200px -> numVisible del carrusel (3)
        //   768-1199px -> 2 visibles
        //   < 768px   -> 1 visible
        // numScroll = 1 en todos los casos
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1199px", 2, 1));
        responsiveOptions.add(new ResponsiveOption("767px", 1, 1));
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
