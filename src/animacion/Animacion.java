package animacion;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta clase tiene por finalidad gestionar la animación.
 */
public class Animacion {
    /**
     * Los parametros de la clase son los siguientes:
     *
     * @nombre: nombre de la animación.
     * @duracion: duración en milisegundos de la animación
     * @vuelta: repeticiones de la animación (0 para infinito)
     * @vueltaActual: Loop de la animación actual.
     * @numeroDeImagen: Número de imágenes de la animación.
     * @intervaloDeImagen: Milisegundos que se muestra cada frame de la animación (calculado
     * basándonos en el número de imagenes y la duración)
     * @tiempoInicioAnimation: Milisegundos en el que empezó la animación.
     * @imagenesAnimation: Listado de las imagenes de la animación.
     */
    private final String nombre;
    private final int duracion;
    private final int vuelta;
    private int vueltaActual;
    private final int numeroDeImagen;
    private final int intervaloDeImagen;
    private long tiempoInicioAnimation;
    private final ArrayList<Image> imagenesAnimation;

    /**
     * Constructor de la animación:
     *
     * @param nombreAnimacion   Nombre de la animación
     * @param duracionAnimacion Duración de la animación
     * @param vueltaAnimacion   Número de vueltas de la animación (0 para infinito)
     * @param rutaImagenes      Array con las rutas a las imágenes
     */
    public Animacion(String nombreAnimacion, int duracionAnimacion, int vueltaAnimacion, String[] rutaImagenes) throws FileNotFoundException {
        nombre = nombreAnimacion;
        duracion = duracionAnimacion;
        vuelta = vueltaAnimacion;
        vueltaActual = 0;
        imagenesAnimation = new ArrayList<>();
        for (String i : rutaImagenes) {
            ImageIcon imagen = new ImageIcon(this.getClass().getResource(i));
            imagenesAnimation.add(imagen.getImage());
        }
        // Cálculo del número de imágenes y el intervalo de las mismas en la animación.
        numeroDeImagen = rutaImagenes.length;
        intervaloDeImagen = duracion / numeroDeImagen;
        // Inicialización del parámetro a 0 para saber que la animación
        // empieza desde el primer frame.
        tiempoInicioAnimation = 0;
    }

    /**
     * Devuelve la imagen que corresponda con el intervalo de la animación
     * en el que nos encontremos.
     *
     * @return imagen correspondiente de la animación.
     */
    public Image getImagen() {
        if (tiempoInicioAnimation == 0) {
            reiniciarAnimacion();
        }
        return imagenesAnimation.get(getNumeroDeFrame());
    }

    /**
     * Reinicia la animación, obteniendo la hora en milisegundos
     * en la que ha comenzado la nueva animación.
     */
    public void reiniciarAnimacion() {
        tiempoInicioAnimation = new Date().getTime();
    }

    /**
     * Obtiene el número del frame correspondiente a la animación.
     *
     * @return numero del frame
     */
    private int getNumeroDeFrame() {
        long milisegundosActuales = new Date().getTime();
        int intervalo = (int) (milisegundosActuales - tiempoInicioAnimation);
        // Si se ha superado el número de vueltas máximo, y se ha llegado al final
        // de la animación, se devuelve siempre el último frame.
        if ((intervalo >= duracion) && (vuelta != 0) && (vueltaActual >= vuelta)) {
            return numeroDeImagen - 1;
        }
        // Si se ha llegado al final de la animacion, se reinicia para
        // que el siguiente frame sea de nuevo el primero de la animación.
        // En este caso es necesario volver a coger los milisegundos actuales
        // después de reiniciar la animación.
        if (intervalo >= duracion) {
            reiniciarAnimacion();
            if (vuelta != 0) {
                vueltaActual++;
                // Nueva comprobación para saber si se ha llegado al tope de
                // vueltas
                if (vueltaActual >= vuelta) {
                    return numeroDeImagen - 1;
                }
            }
            milisegundosActuales = new Date().getTime();
            intervalo = (int) (milisegundosActuales - tiempoInicioAnimation);
        }
        // Por último, calculamos el frame a mostrar
        if (intervalo == 0) {
            return 0;
        }
        int numeroFrame = intervalo / intervaloDeImagen;
        return numeroFrame;
    }
}
