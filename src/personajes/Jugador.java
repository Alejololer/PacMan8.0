package personajes;

import animacion.ControladorDeAnimacion;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

/**
 * Esta clase tiene la finalidad de gestionar la animación, posición
 * y movimiento del jugador. Se le transmite la pulsación del teclado
 * desde la clase que la instancia.
 * <p>
 * El ancho y el alto son virtuales. No corresponden con el
 * ancho y alto de la imagen del fantasma. De esta manera podemos permitir cierto
 * solapamiento para dar la sensación de que efectivamente han tocado al jugador, ya que
 * han invadido el espacio del mismo.
 */

public class Jugador extends Movil {
    /**
     * Atributos de la clase:
     *
     * @ancho ancho virtual de la clase.
     * @altura alto virtual de la clase
     * @animacionControl objeto con el gestor de animación.
     */
    private int ancho, altura;
    private final ControladorDeAnimacion controlDeAnimacion;

    /**
     * Se inicializa la posición en el eje X,Y
     *
     * @param x    posicion x inicial del jugador.
     * @param y    posicion y inicial del jugador
     * @param size tamaño virtual del objeto.
     */
    public Jugador(int x, int y, int size) {
        super(x, y);
        setSize(size);
        controlDeAnimacion = new ControladorDeAnimacion();
        setImagenesDeJugador();
    }

    /**
     * Establece el set de imagenes del jugador
     */
    private void setImagenesDeJugador() {
        try {
            String[] anim = new String[2];
            anim[0] = "../resources/images/actors/player/up-1.png";
            anim[1] = "../resources/images/actors/player/up-2.png";

            controlDeAnimacion.agregarAnimacion("up", 200, 0, anim);

            anim[0] = "../resources/images/actors/player/down-1.png";
            anim[1] = "../resources/images/actors/player/down-2.png";

            controlDeAnimacion.agregarAnimacion("down", 200, 0, anim);

            anim[0] = "../resources/images/actors/player/left-1.png";
            anim[1] = "../resources/images/actors/player/left-2.png";

            controlDeAnimacion.agregarAnimacion("left", 200, 0, anim);

            anim[0] = "../resources/images/actors/player/right-1.png";
            anim[1] = "../resources/images/actors/player/right-2.png";

            controlDeAnimacion.agregarAnimacion("right", 200, 0, anim);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    /**
     * Devuelve la imagen que corresponda a la dirección actual del jugador.
     *
     * @return devuelve la imagen correspondiente al jugador
     * @see Image
     */
    public Image getImage() {
        switch (getActualDireccion()) {
            case Movil.ARRIBA:
                return controlDeAnimacion.getImagen("up");
            case Movil.ABAJO:
                return controlDeAnimacion.getImagen("down");
            case Movil.IZQUIERDA:
                return controlDeAnimacion.getImagen("left");
            case Movil.DERECHA:
            default:
                return controlDeAnimacion.getImagen("right");
        }
    }

    /**
     * Dependiendo de la tecla pulsada establece la dirección
     * del jugador.
     *
     * @param e
     * @see KeyEvent
     * @see Movil
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                setSiguienteDireccion(IZQUIERDA);
                break;
            case KeyEvent.VK_RIGHT:
                setSiguienteDireccion(DERECHA);
                break;
            case KeyEvent.VK_UP:
                setSiguienteDireccion(ARRIBA);
                break;
            case KeyEvent.VK_DOWN:
                setSiguienteDireccion(ABAJO);
                break;
        }
    }

    /**
     * Devuelve el tamaño virtual y la posicion del jugador.
     *
     * @return el tamaño virtual y posicion del jugador.
     * @see Rectangle
     */
    public Rectangle getBounds() {
        Rectangle tamaño = new Rectangle(getPosicionX(), getPosicionY(), ancho, altura);
        return tamaño;
    }

    /**
     * Establece el tamaño virtual del jugador.
     *
     * @param size Tamaño en pixels.
     */
    private void setSize(int size) {
        ancho = (int) (((long) size) * 0.90);
        altura = (int) (((long) size) * 0.90);
    }
}
