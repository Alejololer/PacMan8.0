package comestibles;

import java.awt.*;
import java.io.Serializable;

/**
 * Esta clase abstracta es la clase principal de los objetos recolectables.
 * Establece los atributos y métodos minimos para cada uno de ellos.
 */
public abstract class Comestible {
    private final int posicionX;
    private final int posicionY;
    private int ancho;
    private int altura;
    protected Image image;
    protected int puntos;
    protected String tipo;

    /**
     * Constructor. Inicializa la posicion inicial en el eje XY
     *
     * @param posicionX posicion X inicial del objeto
     * @param posicionY posicion Y inicial del objeto
     */
    public Comestible(int posicionX, int posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    /**
     * Devuelve la imagen del objeto recolectable
     *
     * @return imagen del objeto recolectable.
     * @see Image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Devuelve la posicion "x" del objeto recolectable en el eje X
     *
     * @return posicion x del elemento
     */
    public int getPosicionX() {
        return posicionX;
    }

    /**
     * Devuelve la posicion "y" del objeto recolectable en el eje Y
     *
     * @return posicion y del elemento
     */
    public int getPosicionY() {
        return posicionY;
    }

    /**
     * Devuelve el tipo de elemento
     *
     * @return tipo de elemento.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Devuelve los puntos del elemento
     *
     * @return puntos del elemento.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Establece los puntos del elemento
     *
     * @param puntos puntos
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    /**
     * Establece el tamaño virtual del recolectable.
     *
     * @param size Tamaño en pixels.
     */
    public void setSize(int size) {
        ancho = (int) (((long) size) * 0.90);
        altura = (int) (((long) size) * 0.90);
    }

    /**
     * Devuelve el tamaño virtual y la posicion del recolectable.
     *
     * @return el tamaño virtual y la posición del recolectable
     * @see Rectangle
     */
    public Rectangle getLimite() {
        Rectangle rectangulo = new Rectangle(posicionX, posicionY, ancho, altura);
        return rectangulo;
    }
}
