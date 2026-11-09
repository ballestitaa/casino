package es.fempa;

import java.io.Serializable;

public class Juego implements Serializable {

    private String nombre;
    private String descripcion;
    private String imagen; // ruta relativa a resources/imgs/ (p. ej. "juegos/ruleta.jpg")
    private String url;    // ruta relativa al contexto

    public Juego() {
    }

    public Juego(String nombre, String descripcion, String imagen, String url) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
