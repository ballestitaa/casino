package es.fempa;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * La aplicacion ya no usa el Flash de Jakarta Faces.
 *
 * Pero si el navegador conserva la cookie de Flash de Mojarra ("csfcfc") de un
 * despliegue anterior, Mojarra 4.1 falla en cualquier pagina con:
 *   Cannot invoke "ELFlash$LifetimeMarker.encode()" because ... getLifetimeMarker() is null
 * porque el Flash al que apunta esa cookie solo existia en la memoria del despliegue viejo.
 *
 * Este filtro oculta esa cookie a Mojarra y le pide al navegador que la borre,
 * asi la pagina carga tras redesplegar sin tener que borrar cookies a mano.
 */
@WebFilter(urlPatterns = "*.xhtml")
public class FlashCookieFilter implements Filter {

    private static final String FLASH_COOKIE = "csfcfc";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        Cookie[] cookies = request.getCookies();

        if (cookies != null && Arrays.stream(cookies).anyMatch(c -> FLASH_COOKIE.equals(c.getName()))) {
            // borrar la cookie en el navegador
            Cookie borrar = new Cookie(FLASH_COOKIE, "");
            borrar.setMaxAge(0);
            borrar.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            borrar.setHttpOnly(true);
            ((HttpServletResponse) res).addCookie(borrar);

            // y que Mojarra no la vea en esta peticion
            Cookie[] sinFlash = Arrays.stream(cookies)
                    .filter(c -> !FLASH_COOKIE.equals(c.getName()))
                    .toArray(Cookie[]::new);
            request = new HttpServletRequestWrapper(request) {
                @Override
                public Cookie[] getCookies() {
                    return sinFlash.length == 0 ? null : sinFlash;
                }
            };
        }
        chain.doFilter(request, res);
    }
}
