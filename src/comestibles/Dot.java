package comestibles;

import javax.swing.*;
import java.io.Serializable;

/**
 * Clase que se encarga de generar los dots "puntos" que come el pac man en el nivel y hereda de Comestible
 *
 * @see Comestible
 */
public class Dot extends Comestible {
    /**
     * Constructor que inicializa el tipo, los puntos y la imagen
     *
     * @param posx posicion inicial x
     * @param posy posicion inicial y
     */
    public Dot(int posx, int posy) {
        super(posx, posy);
        ImageIcon imagen = new ImageIcon(this.getClass().getResource("../resources/images/pickups/dot.png"));
        image = imagen.getImage();
        puntos = 10;
        tipo = "Dot";
    }
}
