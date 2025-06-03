package nivel;

import comestibles.Comestible;
import comestibles.Dot;
import comestibles.SuperDot;
import configuraciones.Configuracion;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta clase almacena la información referente al nivel:
 * - Estructura del nivel
 * - Los objetos recolectables
 * - Número de filas y columnas
 * - Los identificadores de los fantasmas disponibles en el nivel
 * - Posicion inicial de cada fantasma y del jugador.
 * - El patrón de imágenes del nivel.
 */
public class Nivel implements Serializable {
    /**
     * Carpeta con el set de imágenes del nivel
     */
    private String patronDeImagenDeNivel; // "default";
    private int[][] celdas;
    private int numeroDeFilas;
    private int numeroDeColumnas;
    private String[] fantasmasEnNivel;
    private final HashMap<String, Image> imagenes;
    private final ArrayList<Comestible> comestibles;

    /**
     * Las posiciones inciciales se guardan en arrays de 2 huecos.
     * - Hueco 0 para eje X
     * - Hueco 1 para eje Y
     */
    private int[] posicionInicialDelJugador;
    private int[] posicionInicialFantasmaRojo;
    private int[] posicionInicialFantasmaCian;
    private int[] posicionInicialFantasmaNaranja;
    private int[] posicionInicialFantasmaRosado;
    private final Configuracion configuracion;

    /**
     * Constructor con valores por defecto.
     */
    public Nivel() {
        patronDeImagenDeNivel = "default";
        imagenes = new HashMap<>();
        comestibles = new ArrayList<>();
        posicionInicialDelJugador = new int[2];
        posicionInicialDelJugador[0] = 0;
        posicionInicialDelJugador[1] = 0;
        configuracion = Configuracion.getInstance();
    }

    /**
     * Establece la carpeta con el set de imagenes.
     *
     * @param patron la carpeta con el set de imagenes
     */
    public void setNombreDeCarpetaConPatronDeImagenes(String patron) {
        patronDeImagenDeNivel = patron;
    }

    /**
     * Devuelve la ruta con el set de imagenes
     *
     * @return ruta con set de imagenes
     */
    public String getRutaDeAccesoConPatronDeImagenes() {
        return "../resources/images/tilesets/" + patronDeImagenDeNivel + "/";
    }

    /**
     * Inicializa el array bidimensional para almacenar el patron
     * del nivel
     *
     * @param filas    Número de filas
     * @param columnas Numero de columnas
     */
    public void crearTableroDeCeldas(int filas, int columnas) {
        numeroDeFilas = filas;
        numeroDeColumnas = columnas;
        celdas = new int[filas][columnas];
    }

    /**
     * Devuelve el número de filas del nivel
     *
     * @return numero de filas
     */
    public int getNumeroDeFilas() {
        return numeroDeFilas;
    }

    /**
     * Devuelve el numero de columnas del nivel
     *
     * @return numero de columnas
     */
    public int getNumeroDeColumnas() {
        return numeroDeColumnas;
    }

    /**
     * Devuelve los objetos recolectables
     *
     * @return objetos recolectables
     */
    public ArrayList<Comestible> getComestibles() {
        return comestibles;
    }


    /**
     * Inicializa una celda
     *
     * @param fila    fila de la celda
     * @param columna columna de la celda
     * @param valor   valor de la celda.
     */
    public void setCelda(int fila, int columna, int valor) {
        celdas[fila][columna] = valor;
        int[] posicionPixeles;
        switch (valor) {
            case 1: // Dot
                posicionPixeles = convertirFilasAPixeles(fila, columna);
                Dot dot = new Dot(posicionPixeles[0], posicionPixeles[1]);
                dot.setSize(configuracion.getTamañoPanel());
                dot.setPuntos(10);
                comestibles.add(dot);
                break;
            case 2: // SuperDot
                posicionPixeles = convertirFilasAPixeles(fila, columna);
                SuperDot s_dot = new SuperDot(posicionPixeles[0], posicionPixeles[1]);
                s_dot.setSize(configuracion.getTamañoPanel());
                s_dot.setPuntos(20);
                comestibles.add(s_dot);
                break;
            case 3: // Posicion inicial de jugador
                posicionInicialDelJugador = convertirFilasAPixeles(fila, columna);
                break;
            case 4: // Posicion inicial de fantasma rojo
                posicionInicialFantasmaRojo = convertirFilasAPixeles(fila, columna);
                break;
            case 5: // Posicion inicial de fantasma naranja
                posicionInicialFantasmaNaranja = convertirFilasAPixeles(fila, columna);
                break;
            case 6: // Posicion inicial de fantasma azul
                posicionInicialFantasmaCian = convertirFilasAPixeles(fila, columna);
                break;
            case 7: // Posicion inicial de fantasma rosa
                posicionInicialFantasmaRosado = convertirFilasAPixeles(fila, columna);
                break;
        }
    }

    /**
     * Devuelve el valor de la posicion inicial del jugador
     *
     * @return int[] de 2 posiciones con la posicion x e y
     */
    public int[] getPosicioninicialDelJugador() {
        return posicionInicialDelJugador;
    }

    /**
     * Devuelve la imagen correspondiente a la celda
     *
     * @param row fila de la celda
     * @param col columna de la celda
     * @return imagen de la celda
     */
    public Image getImagenDeCelda(int row, int col) {
        if (imagenes == null) {
            crearSetDeImagenes();
        }
        if (celdas[row][col] < 0) {
            if (!imagenes.containsKey("No navegable" + celdas[row][col])) {
                createImage("No navegable" + celdas[row][col], celdas[row][col]);
            }
            return imagenes.get("No navegable" + celdas[row][col]);
        } else {
            return imagenes.get("Navegable");
        }
    }

    /**
     * Crea una imagen en caso de no existir
     *
     * @param nombreDeImagen  nombre de la imagen en el HashMap
     * @param nombreDeArchivo nombre del fichero (sin extension)
     */
    private void createImage(String nombreDeImagen, int nombreDeArchivo) {
        String carpetaDeImagenes = getRutaDeAccesoConPatronDeImagenes();
        String resource = nombreDeArchivo + ".png";
        ImageIcon imagenIcon = new ImageIcon(this.getClass().getResource(carpetaDeImagenes + resource));
        imagenes.put(nombreDeImagen, imagenIcon.getImage());
    }

    /**
     * Crea el set de imagenes para almacenarlo en memoria.
     */
    public void crearSetDeImagenes() {
        String carpetaDeImagenes = getRutaDeAccesoConPatronDeImagenes();
        String resource = "0.png";
        ImageIcon imagenIcon = new ImageIcon(this.getClass().getResource(carpetaDeImagenes + resource));
        imagenes.put("navigable", imagenIcon.getImage());

    }

    /**
     * Funcion de ayuda para convertir la posicion de filas-columnas en eje XY
     *
     * @param fila    fila
     * @param columna columna
     * @return int[] de 2 posiciones con la posicion XY
     */
    public int[] convertirFilasAPixeles(int fila, int columna) {
        int[] posicionPixeles = new int[2];
        posicionPixeles[0] = columna * configuracion.getTamañoPanel();
        posicionPixeles[1] = fila * configuracion.getTamañoPanel();
        return posicionPixeles;
    }

    /**
     * Funcion de ayuda para obtener la fila y columna en base a
     * eje xy
     *
     * @param x posicion X a obtener
     * @param y posicion Y a obtener
     * @return int[] de 2 posiciones con la fila y la columna
     */
    public int[] convertirPixelesEnCeldas(int x, int y) {
        int[] celda = new int[2];
        // filas
        celda[0] = y / configuracion.getTamañoPanel();
        // columnas
        celda[1] = x / configuracion.getTamañoPanel();
        return celda;
    }

    /**
     * Devuelve el valor de la celda superior
     *
     * @return entero con el valor de la celda superior
     */
    public int[] getCeldaSuperior(int fila, int columna) {
        int[] celda = new int[2];
        celda[0] = fila - 1;
        celda[1] = columna;
        return celda;
    }

    /**
     * Devuelve el valor de la celda inferior
     *
     * @return entero con el valor de la celda inferior
     */
    public int[] getCeldaInferior(int fila, int columna) {
        int[] celda = new int[2];
        celda[0] = fila + 1;
        celda[1] = columna;
        return celda;
    }

    /**
     * Devuelve el valor de la celda a la izquierda
     *
     * @return entero con el valor de la celda a la izquierda
     */
    public int[] getCeldaIzquierda(int fila, int columna) {
        int[] celda = new int[2];
        celda[0] = fila;
        celda[1] = columna - 1;
        return celda;
    }

    /**
     * Devuelve el valor de la celda a la derecha
     *
     * @return entero con el valor de la celda a la derecha
     */
    public int[] getCeldaDerecha(int fila, int columna) {
        int[] celda = new int[2];
        celda[0] = fila;
        celda[1] = columna + 1;
        return celda;
    }

    /**
     * Determina si una celda es navegable por fantasmas o jugador.
     *
     * @return true si la celda es navegable.
     */
    public boolean esNavegableLaCelda(int fila, int columna) {
        return (celdas[fila][columna] >= 0);
    }

    /**
     * Establece los ids de los fantasmas disponibles en el nivel
     *
     * @param fantasmas
     */
    public void setFantasmas(String[] fantasmas) {
        fantasmasEnNivel = fantasmas;
    }

    /**
     * Devuelve el set de ids de los fantasmas disponibles en el nivel
     *
     * @return set de ids de los fantasmas disponibles en el nivel
     */
    public String[] getFantasmasEnELNivel() {
        return fantasmasEnNivel;
    }

    /**
     * Posicion inicial del fantasma solicitado
     *
     * @param id Identificador del fantasma a solicitar
     * @return int[] de 2 huecos con posiciones XY
     */
    public int[] getPosicionInicialDelFantasma(String id) {
        int[] posicionInicialFantasma = new int[2];
        posicionInicialFantasma[0] = 0;
        posicionInicialFantasma[1] = 0;
        if (id.equals("rojo")) {
            posicionInicialFantasma = posicionInicialFantasmaRojo;
        }
        if (id.equals("cian")) {
            posicionInicialFantasma = posicionInicialFantasmaCian;
        }
        if (id.equals("naranja")) {
            posicionInicialFantasma = posicionInicialFantasmaNaranja;
        }
        if (id.equals("rosado")) {
            posicionInicialFantasma = posicionInicialFantasmaRosado;
        }
        return posicionInicialFantasma;
    }
}
