/*
 * Bucle infinito real para p:carousel (PrimeFaces 15.0.9).
 *
 * Con numVisible=2 y numScroll=1, el p:carousel de PrimeFaces:
 *   - solo tiene N - numVisible + 1 paginas (3 con 4 juegos), asi que
 *     nunca muestra "ultimo + primero" (Pirots 3 + Ruleta);
 *   - en autoplay, al llegar al final vuelve a la pagina 0 animando hacia atras;
 *   - con el boton "siguiente", al dar la vuelta salta numVisible tarjetas.
 *
 * Este parche solo afecta a los carruseles con la clase "carrusel-infinito".
 * Usa los clones que PrimeFaces ya crea en modo circular:
 *   [ultimos V clonados] [N items reales] [primeros V clonados]
 * La pagina p (0..N-1) se muestra con el desplazamiento -(p + V).
 * Al pasar de la ultima a la primera se desliza hasta los clones y, al
 * acabar la transicion, se recoloca sin animacion en la posicion real.
 */
(function () {
    var Carousel = window.PrimeFaces && PrimeFaces.widget && PrimeFaces.widget.Carousel;
    if (!Carousel || Carousel.prototype._infinitoPatched) {
        return;
    }

    var proto = Carousel.prototype;
    var original = {
        getTotalIndicators: proto.getTotalIndicators,
        step: proto.step,
        onTransitionEnd: proto.onTransitionEnd,
        startAutoplay: proto.startAutoplay,
        navForward: proto.navForward,
        navBackward: proto.navBackward
    };

    function esInfinito(w) {
        return w.jq && w.jq.hasClass('carrusel-infinito') && w.cfg.circular
            && w.itemsCount > w.numVisible;
    }

    proto._infinitoPatched = true;

    // Una pagina por cada juego (4 indicadores en lugar de 3)
    proto.getTotalIndicators = function () {
        return esInfinito(this) ? this.itemsCount : original.getTotalIndicators.call(this);
    };

    // dir = -1 adelante, 1 atras; index = pagina destino (clic en un indicador)
    proto.step = function (dir, index) {
        if (!esInfinito(this)) {
            return original.step.call(this, dir, index);
        }
        var n = this.itemsCount, v = this.numVisible, pos, page;

        // si quedaba una recolocacion pendiente, se aplica ya sin animacion
        this._aplicarRecolocacion();

        if (index != null) {
            page = index;
            pos = -(page + v);
        } else if (dir === -1) {
            if (this.page === n - 1) {
                pos = -(n + v);          // hacia los clones del principio
                page = 0;
                this._recolocar = true;
            } else {
                page = this.page + 1;
                pos = -(page + v);
            }
        } else {
            if (this.page === 0) {
                pos = -(v - 1);          // hacia el clon del ultimo
                page = n - 1;
                this._recolocar = true;
            } else {
                page = this.page - 1;
                pos = -(page + v);
            }
        }

        this.itemsContainer.removeClass('ui-items-hidden');
        this.changePosition(pos);
        this.itemsContainer.get(0).style.transition = 'transform 500ms ease 0s';
        this.totalShiftedItems = pos;
        this.page = page;

        if (this.cfg.onPageChange) {
            this.cfg.onPageChange.call(this, page);
        }
        if (this.hasBehavior('pageChange')) {
            this.callBehavior('pageChange', {params: [{name: this.id + '_pageValue', value: page}]});
        }

        this.updateNavigators();
        this.updateIndicators();
        this.styleActiveItems();
    };

    proto._aplicarRecolocacion = function () {
        if (!this._recolocar) {
            return;
        }
        this._recolocar = false;
        var el = this.itemsContainer.get(0);
        el.style.transition = '';
        this.totalShiftedItems = -(this.page + this.numVisible);
        this.changePosition(this.totalShiftedItems);
        void el.offsetWidth; // fuerza el reflow antes de la siguiente animacion
        this.styleActiveItems();
    };

    proto.onTransitionEnd = function () {
        if (!esInfinito(this)) {
            return original.onTransitionEnd.call(this);
        }
        this.itemsContainer.addClass('ui-items-hidden');
        this.itemsContainer.get(0).style.transition = '';
        this._aplicarRecolocacion();
    };

    // Autoplay: siempre una tarjeta hacia delante
    proto.startAutoplay = function () {
        if (!esInfinito(this)) {
            return original.startAutoplay.call(this);
        }
        var self = this;
        this.stopAutoplay();
        this.interval = setInterval(function () {
            self.step(-1);
        }, this.cfg.autoplayInterval);
    };

    // Navegar a mano no detiene el autoplay: solo reinicia el contador
    function navegar(nombre, dir) {
        return function (event, index) {
            if (!esInfinito(this)) {
                return original[nombre].call(this, event, index);
            }
            this.step(dir, index);
            if (this.cfg.autoplayInterval) {
                this.startAutoplay();
            }
            if (event && event.cancelable) {
                event.preventDefault();
            }
        };
    }

    proto.navForward = navegar('navForward', -1);
    proto.navBackward = navegar('navBackward', 1);
})();
