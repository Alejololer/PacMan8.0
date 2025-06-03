package animacion;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Esta clase tiene el propósito de gestionar animaciones de los
 * personajes del juego.
 */
public class ControladorDeAnimacion {
    private String animacionActual;
    private final HashMap<String, Animacion> animaciones;

    public ControladorDeAnimacion() {
        animaciones = new HashMap<>();
    }

    /**
     * Método para insertar una animación en el gestor de animaciones.
     *
     * @param nombre   Nombre de la animación a insertar
     * @param duracion Duración en milisegundos.
     * @param vuelta   Número de vueltas que dará la animacion (0 infinito)
     * @param archivos Array con los ficheros pertenecientes a la animación.
     * @throws FileNotFoundException
     */
    public void agregarAnimacion(String nombre, int duracion, int vuelta, String[] archivos) throws FileNotFoundException {
        Animacion anim = new Animacion(nombre, duracion, vuelta, archivos);
        animaciones.put(nombre, anim);
    }

    /**
     * Metodo para obtener la imagen de una animación.
     *
     * @param animacion El nombre de la animación de la que se
     *                  quiere obtener la imagen.
     * @return La imagen de la animación especificada.
     * @see Image
     */
    public Image getImagen(String animacion) {
        animacionActual = animacion;
        return animaciones.get(animacion).getImagen();
    }
}
