package comestibles;

import javax.swing.*;

/**
 * Clase que se encarga de generar los SuperDots que permite comer a los fantasmas y hereda de Comestible.
 */
public class SuperDot extends Comestible {
    /**
     * Constructor que inicializa el tipo, los puntos y la imagen
     *
     * @param x posicion inicial x
     * @param y posicion inicial y
     */
    public SuperDot(int x, int y) {
        super(x, y);
        ImageIcon imagen = new ImageIcon(this.getClass().getResource("../resources/images/pickups/bigdot.png"));
        image = imagen.getImage();
        puntos = 15;
        tipo = "SuperDot";
    }
}
