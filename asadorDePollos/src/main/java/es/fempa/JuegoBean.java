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
        juegos.add(new Juego("Ruleta Europea", "Ruleta clásica de un solo cero, con croupier en vivo.",
                "juegos/ruleta.jpg", "/juegos/ruleta.html"));
        juegos.add(new Juego("Blackjack Royal", "Cartas, chips y dealer en vivo.",
                "juegos/blackjack.jpg", "/juegos/blackjack.html"));
        juegos.add(new Juego("Póker Classic", "Mesa de póker con jugadores en vivo.",
                "juegos/poker.jpg", "/juegos/poker.html"));
        juegos.add(new Juego("Pirots 3", "Tragaperras del viejo oeste con premios increíbles.",
                "juegos/tragaperras.jpg", "/juegos/pirots.html"));
        juegos.add(new Juego("Ruleta MegaFire", "Ruleta potenciada con multiplicadores de hasta x500.",
                "juegos/Ruleta_potenciada.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Blackjack Lightning", "Blackjack con cartas relámpago y pagos multiplicados.",
                "juegos/blackjack_potenciado.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Póker Mega Stack", "Torneos con fichas extra y botes garantizados.",
                "juegos/Poker_Pot.jpg", "/juegos/juego.html"));
        juegos.add(new Juego("Pirots 3 Turbo", "La tragaperras del oeste con giros extra y jackpot.",
                "juegos/tragaperras_potenciada.jpg", "/juegos/juego.html"));

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
