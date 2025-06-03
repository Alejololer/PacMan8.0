package personajes;

import animacion.ControladorDeAnimacion;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Esta clase maneja las instancias de los fantasmas y hereda
 * de la clase Movil que gestiona la acción en general del movimiento.
 * En esta clase se gestiona la inteligencia artificial de los fantasmas
 * con base en los parametros HvsV (Horizontal vs. Vertical) y accuracy_threshold (límite
 * de eficacia).
 * <p>
 * El ancho y el alto son virtuales. No corresponden con el
 * ancho y alto de la imagen del fantasma. De esta manera podemos permitir cierto
 * solapamiento para dar la sensación de que efectivamente han tocado al jugador, ya que
 * han invadido el espacio del mismo.
 * <p>
 * Gracias al sistema de configuración vía JSON y al algoritmo de búsqueda mediante
 * los parámetros anteriormente citados, no es necesario crear una clase
 * específica por cada fantasma.
 *
 * @see Movil
 */

public class Fantasma extends Movil implements Serializable {
    /**
     * Atributos de la clase:
     *
     * @seColisono Utilizada para determinar si ha colisionado con otro
     * fantasma, agilizar las busquedas de colisiones, y evitar
     * problemas con dobles colisiones.
     * @inicioEstadoMuerto Hora en milisegundos a la que empezó el estado DEAD
     * @estado - Estado actual del fantasma.
     * @ancho - Ancho virtual del fantasma
     * @altura - Altura virtual del fantasma
     * @HvsV - Preferencia a la hora de elegir buscar en horizontal o
     * vertical. (A menor valor, es más probable que elija horizontal
     * y viceversa)
     * @accuracy_threshold - límite o umbral de eficacia. Cuanto menor sea el valor menos
     * se preferirá buscar al jugador. De esta manera se evitan
     * atascos del fantasma recalculando la próxima dirección.
     * @id - Identificador del fantasma (red, blue, orange y pink)
     * @imageset - Set de imagenes del fantasma
     * @nombreFantasma - Nombre del fantasma (definido en el fichero JSON)
     */

    private boolean seColisiono;
    private final ArrayList<String> choque;
    private long inicioEstadoMuerto;
    private int estado;
    private final int ancho;
    private final int altura;
    private final int HvsV;
    private final int umbralDePresicion;
    private final String id;
    private final String imageSet;
    private ControladorDeAnimacion controlDeAnimacion;

    /**
     * Variables estáticas para definir los estados del fantasma.
     *
     * @PERSIGUE Persiguiendo
     * @ASUSTADO Asustado
     * @MUERTO Muerto
     */
    public static final int PERSIGUE = 1;
    public static final int ASUSTADO = 2;
    public static final int MUERTO = 3;

    /**
     * Constructor Inicializa los valores iniciales del fantasma. El ancho y alto
     * se asignan automáticamente por el parametro size. Se reduce en un 90% el tamaño
     * para dar la sensacion de solapamiento sobre el jugador u otros fantasmas.
     *
     * @param x                  Posicion X inicial del fantasma
     * @param y                  Posicion Y inicial del fantasma
     * @param size               Tamanio virtual del fantasma. Se asigna a ancho y alto automaticamente
     *                           transformandolo a un 90% del valor dado.
     * @param id                 Identificador del fantasma
     * @param imageSet           Set de imagenes
     * @param HvsV               Preferencia de horizontal vs vertical
     * @param umbralDePresicion límite de precisión del fantasma.
     * @param accuracyThreshold
     */
    public Fantasma(int x, int y, int size, String id, String imageSet, int HvsV, int umbralDePresicion, int accuracyThreshold) {
        super(x, y);
        estado = PERSIGUE;
        this.id = id;
        this.imageSet = imageSet;
        this.HvsV = HvsV;
        this.umbralDePresicion = umbralDePresicion;
        this.ancho = (int) (((long) size) * 0.95);
        this.altura = (int) (((long) size) * 0.95);
        this.seColisiono = false;
        this.choque = new ArrayList<>();
        cargarSetDeImagenes();
    }

    /**
     * Devuelve el identificador del fantasma.
     *
     * @return id del fantasma
     */
    public String getID() {
        return id;
    }

    /**
     * Establece si el fantasma ha chocado o no.
     *
     * @param chocado boolean que define si ha colisionado o ha dejado de colisionar
     */
    public void setTieneColision(boolean chocado) {
        seColisiono = chocado;
    }

    /**
     * Metodo para saber si esta colisionando con un fantasma en concreto
     *
     * @param id id del fantasma
     * @return true si esta colisionando con el fantasma informado
     */
    public boolean estaColisionadoConFanstasma(String id) {
        return (choque.contains(id));
    }

    /**
     * Metodo para quitar de la lista el fantasma con el que está colisionando
     *
     * @param id identificador del fantasma a quitar la colision.
     */
    public void removerFantasmaColisionado(String id) {
        int idFantasama = choque.indexOf(id);
        if (idFantasama >= 0) {
            choque.remove(idFantasama);
        }
    }

    /**
     * Anadir a la lista de fantasmas colisionados
     *
     * @param id identificador del fantasma a la lista de fantasmas colisionados
     */
    public void addFantasmaColisionado(String id) {
        int idFanstama = choque.indexOf(id);
        if (idFanstama < 0) {
            choque.add(id);
        }
    }

    /**
     * Devuelve el número de fantasmas con los que está colisionando
     *
     * @return número de fantasmas con el que está colisionando
     */
    public int getFantasmasColisionados() {
        return choque.size();
    }

    /**
     * Devuelve el estado del fantasma. Comparar con los atributos
     * estáticos de la clase.
     *
     * @return entero con el estado del fantasma
     * @see Movil
     */
    public int getEstado() {
        return estado;
    }


    /**
     * Establece el estado del personaje. En caso de ser el estado DEAD,
     * inicializa la hora a la que murio, para que empiece a hacerse la comprobacion
     * de si debe volver a la vida.
     *
     * @param estadoNuevo nuevo estado, los valores posibles son los establecidos
     *                    como variables estaticas de la clase.
     */
    public void setEstado(int estadoNuevo) {
        estado = estadoNuevo;
        if (estadoNuevo == MUERTO) {
            inicioEstadoMuerto = new Date().getTime();
        }
    }

    /**
     * Comprueba el tiempo que lleva en estado DEAD.
     * En caso de llevar 5 segundos o más, inicializa el valor
     * de la hora a 0, y cambia el estado a CHASING.
     */
    public void comprobarTiempoEstadoMuerto() {
        long tiempoNuevo = new Date().getTime();
        if (tiempoNuevo >= inicioEstadoMuerto + 5000) {
            estado = PERSIGUE;
            inicioEstadoMuerto = 0;
        }
    }

    /**
     * Metodo para saber si el fantasma ya ha colisionado con otro fantasma
     * previamente.
     *
     * @return Devuelve un valor booleano indicando si ha colisionado
     * o no.
     */
    public boolean tieneColision() {
        return seColisiono;
    }

    /**
     * Método con la lógica principal de búsqueda del fantasma.
     * Se utilizan números aleatorios y los valores de los atributos HvsV
     * (horizontal vs. vertical) y accuracy_threshold.
     * Cuanto menor sea la configuración de HvsV, es posible que el fantasma
     * prefiera buscar en horizontal. Y cuanto menor sea el número de
     * accuracy_threshold será menos probable que el fantasma decida buscar al jugador.
     *
     * @param jugadorPosX posicion X actual del jugador
     * @param jugadorPosY posicion Y actual del jugador
     */
    public void calcularSiguientePosicion(int jugadorPosX, int jugadorPosY) {
        int random;
        Random aleatorio = new Random();
        // Se ha establecido un umbral de error para no preferir siempre horizontal o vertical
        // para evitar estancamientos de los fantasmas.
        // Cuanto más bajo sea el número aleatorio más se preferira horizontal con base en el atributo HvsV
        random = aleatorio.nextInt(100);
        boolean buscarVertical = (HvsV < random);
        if (buscarVertical) {
            if (jugadorPosY > getPosicionY()) {
                setSiguienteDireccion(ABAJO);
            } else {
                setSiguienteDireccion(ARRIBA);
            }
        } else {
            if (jugadorPosX > getPosicionX()) {
                setSiguienteDireccion(DERECHA);
            } else {
                setSiguienteDireccion(IZQUIERDA);
            }
        }
        // Para evitar estancamientos, ahora decidiremos si vamos a ir en busca del jugador
        random = aleatorio.nextInt(100);
        if (umbralDePresicion < random) {
            // No buscamos a pacman.
            int siguienteDirec;
            do {
                siguienteDirec = aleatorio.nextInt(4);
            } while (siguienteDirec == getActualDireccion());
            setSiguienteDireccion(siguienteDirec);
        }
    }

    /**
     * Carga las imágenes del fantasma
     */
    private void cargarSetDeImagenes() {
        controlDeAnimacion = new ControladorDeAnimacion();
        try {

            String[] anim = new String[2];
            anim[0] = getCarpetaDeSetDeImagenes() + "up-1.png";
            anim[1] = getCarpetaDeSetDeImagenes() + "up-2.png";

            controlDeAnimacion.agregarAnimacion("up", 200, 0, anim);

            anim[0] = getCarpetaDeSetDeImagenes() + "down-1.png";
            anim[1] = getCarpetaDeSetDeImagenes() + "down-2.png";

            controlDeAnimacion.agregarAnimacion("down", 200, 0, anim);

            anim[0] = getCarpetaDeSetDeImagenes() + "left-1.png";
            anim[1] = getCarpetaDeSetDeImagenes() + "left-2.png";

            controlDeAnimacion.agregarAnimacion("left", 200, 0, anim);

            anim[0] = getCarpetaDeSetDeImagenes() + "right-1.png";
            anim[1] = getCarpetaDeSetDeImagenes() + "right-2.png";

            controlDeAnimacion.agregarAnimacion("right", 200, 0, anim);

            anim[0] = getCarpetaDeSetDeImagenes("scary") + "scary-1.png";
            anim[1] = getCarpetaDeSetDeImagenes("scary") + "scary-2.png";

            controlDeAnimacion.agregarAnimacion("scary", 200, 0, anim);

            anim = new String[1];
            anim[0] = getCarpetaDeSetDeImagenes("dead") + "dead.png";

            controlDeAnimacion.agregarAnimacion("dead", 1000, 0, anim);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }


    /**
     * Devuelve la ruta del set de imagenes del fantasma.
     *
     * @return Cadena de texto con la ruta de las imagenes.
     */
    private String getCarpetaDeSetDeImagenes() {
        return "../resources/images/actors/ghosts/" + imageSet + "/";
    }

    /**
     * Devuelve la cadena de texto con la ruta de la imagen. Esta version
     * fuerza a otro set de imagenes.
     *
     * @param otraImageSet
     * @return Cadena de texto con la ruta de la imagenes.
     */
    private String getCarpetaDeSetDeImagenes(String otraImageSet) {
        return "../resources/images/actors/ghosts/" + otraImageSet + "/";
    }

    /**
     * Devuelve la imagen basándonos en el estado actual del fantasma.
     *
     * @return Devuelve la imagen.
     * @see Image
     */
    public Image getImage() {
        switch (estado) {
            default:
            case PERSIGUE:
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
            case ASUSTADO:
                return controlDeAnimacion.getImagen("scary");
            case MUERTO:
                return controlDeAnimacion.getImagen("dead");
        }
    }

    /**
     * Devuelve un objeto Bounds con la posicion actual y tamaño virtual
     * del fantasma.
     *
     * @return objeto Bounds con la posicion y tamaño del objeto.
     * @see Rectangle
     */
    public Rectangle getLimites() {
        Rectangle tamaño = new Rectangle(getPosicionX(), getPosicionY(), ancho, altura);
        return tamaño;
    }
}

