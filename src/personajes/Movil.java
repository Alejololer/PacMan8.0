package personajes;

/**
 * Clase principal que gestiona el movimiento y la posición de
 * los objetos que hereden de ella. La lógica de si puede moverse
 * en una dirección u otra, o la elección de la dirección se delega
 * en otras clases.
 */
public abstract class Movil {
    /**
     * Atributos de la clase:
     *
     * @posicionX: posicion x actual del objeto
     * @posicionY: posicion y actual del objeto
     * @dx: X delta cantidad de pixeles a desplazarse en el eje x
     * @dy: Y delta cantidad de pixeles a desplazarse en el eje y
     * @estaEnMovimiento: devuelve si el objeto esta moviendose.
     * @velocidad: velocidad del objeto.
     */
    private int posicionX;
    private int posicionY;
    private final int deltaX;  // Delta x
    private final int deltaY;  // Delta y
    private final boolean estaEnMovimiento;
    private final double velocidad;

    /**
     * Variables estáticas con las posibles direcciones
     */
    public static final int ARRIBA = 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;
    public static final int IZQUIERDA = 4;

    /**
     * Variables con la dirección actual y la próxima.
     * Almacenando estos valores se consigue un movimiento fluido sin
     * que el jugador tenga que estar pulsando constantemente.
     */
    private int direccionActual;
    private int direccionSiguiente;

    /**
     * Constructor, inicializa todos los atributos por defecto
     * y la posicion inicial vía parametros
     *
     * @param xInicial posición x inicial
     * @param yInicial posicion y inicial
     */
    public Movil(int xInicial, int yInicial) {
        this.posicionX = xInicial;
        this.posicionY = yInicial;
        deltaY = 1;
        deltaX = 1;
        estaEnMovimiento = false;
        velocidad = 1.0;
        direccionActual = 0;
        direccionSiguiente = 0;
    }

    /**
     * Mueve el objeto con base en la dirección actual.
     */
    public void mover() {
        switch (direccionActual) {
            case ARRIBA:
                moverArriba();
                break;
            case ABAJO:
                moverAbajo();
                break;
            case DERECHA:
                moverDerecha();
                break;
            case IZQUIERDA:
                moverIzquierda();
                break;
            default:
                break;
        }
    }

    /**
     * Devuelve una matriz de 2 huecos con la posición.
     *
     * @return Matriz de enteros, posicion 0: x, posicion 1: y
     */
    public int[] getPosicion() {
        int[] valoresRetornados = new int[2];
        valoresRetornados[0] = this.posicionX;
        valoresRetornados[1] = this.posicionY;
        return valoresRetornados;
    }

    /**
     * Desplaza al objeto arriba
     */
    private void moverArriba() {
        this.posicionY = this.posicionY - this.deltaY;
    }


    /**
     * Desplaza al objeto abajo
     */
    private void moverAbajo() {
        this.posicionY = this.posicionY + this.deltaY;
    }


    /**
     * Desplaza al objeto a la derecha
     */
    private void moverDerecha() {
        this.posicionX = this.posicionX + this.deltaX;
    }


    /**
     * Desplaza al objeto a la izquierda
     */
    private void moverIzquierda() {
        this.posicionX = this.posicionX - this.deltaX;
    }

    /**
     * Devuelve la posicion x del objeto
     *
     * @return posición X del objeto
     */
    public int getPosicionX() {
        return this.posicionX;
    }

    /**
     * Establece la nueva posición x del objeto
     *
     * @param posicionX nueva posicion en el eje x del objeto
     */
    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }


    /**
     * Devuelve la posicion y del objeto
     *
     * @return posición Y del objeto
     */
    public int getPosicionY() {
        return this.posicionY;
    }


    /**
     * Establece la nueva posición posicioY del objeto
     *
     * @param posicioY nueva posicion en el eje posicioY del objeto.
     */
    public void setPosicionY(int posicioY) {
        this.posicionY = posicioY;
    }

    /**
     * Devuelve la dirección actual.
     *
     * @return entero con la direccion
     */
    public int getActualDireccion() {
        return direccionActual;
    }


    /**
     * Devuelve la próxima dirección del objeto.
     *
     * @return entero con la direccion siguiente.
     */
    public int getSiguienteDireccion() {
        return direccionSiguiente;
    }


    /**
     * Establece la proxima dirección del objeto. Si la actual
     * es 0, establece la actual.
     *
     * @param direccion La proxima dirección del objeto.
     */
    public void setSiguienteDireccion(int direccion) {
        if (direccionActual == 0) {
            direccionActual = direccion;
            direccionSiguiente = 0;
        } else {
            direccionSiguiente = direccion;
        }
    }

    /**
     * Comprueba si la proxima dirección es la opuesta a la actual.
     *
     * @return devuelve true si la proxima dirección es la opuesta a la actual.
     */
    public boolean esPosicionSiguienteOpuestaAPosicionActual() {
        return (direccionActual == ARRIBA && direccionSiguiente == ABAJO) ||
                (direccionActual == ABAJO && direccionSiguiente == ARRIBA) ||
                (direccionActual == DERECHA && direccionSiguiente == IZQUIERDA) ||
                (direccionActual == IZQUIERDA && direccionSiguiente == DERECHA);
    }

    /**
     * Cambia la dirección actual por la proxima dirección.
     */
    public void cambiarADireccionSiguiente() {
        direccionActual = direccionSiguiente;
        direccionSiguiente = 0;
    }

    /**
     * Cambia la dirección a la dirección contraria. Usada
     * en las colisiones entre fantasmas.
     */
    public void setDireccionOpuesta() {
        switch (direccionActual) {
            case ARRIBA:
                direccionActual = ABAJO;
                break;
            case ABAJO:
                direccionActual = ARRIBA;
                break;
            case IZQUIERDA:
                direccionActual = DERECHA;
                break;
            case DERECHA:
                direccionActual = IZQUIERDA;
                break;
        }
    }
}

