package nivel;

import comestibles.Comestible;
import configuraciones.Configuracion;
import configuraciones.FantasmaConfiguracion;
import interfazGrafica.VentanaJuego;
import personajes.Fantasma;
import personajes.Jugador;
import personajes.Movil;
import puntuacion.Puntuacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Clase que lleva la logica principal del juego: render y actualizacion
 * de los objetos. Obtiene la configuración principal de los fantasmas,
 * tamaño de las celdas del objeto Config que accede a un fichero JSON.
 */
@SuppressWarnings("serial")
public class Tablero extends JPanel  implements Serializable, ActionListener {
    public static Timer timer;
    public static Jugador jugador;
    public static Font fuenteDeTexto;
    private static Configuracion configuracionDelJuego;
    private Nivel nivel;
    public static Puntuacion puntuacion;
    private ArrayList<Fantasma> fantasmas;
    private boolean modoInvisible;
    private long empezarModoInvisible;
    public static int contadorNivel = 1;

    public Tablero() {
        // Cargamos las opciones del JPanel y aniadimos el listener para los eventos de teclado
        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        setFuenteDeTexto();
        arrancarJuego();
    }

    /**
     * Se encarga de iniciar el juego con todos sus componentes
     */
    public void arrancarJuego() {
        configuracionDelJuego = Configuracion.getInstance();// Cargamos una instancia de la configuración (fantasmas y tamanio de pixels)
        cargarJuego();// Arrancamos con la pantalla de inicio
        puntuacion = new Puntuacion();
        timer = new Timer(5, this);// Arrancamos el timer del juego.
    }

    /**
     * Carga y configuración del nivel
     */
    private void configurarJuego() {
        modoInvisible = false;
        empezarModoInvisible = 0;
        // Cargamos el nivel
        nivel = new CargadorDeNivel().cargarNivel(contadorNivel);
        // Cargamos fantasmas
        cargarFantasmas();
        // Obtenemos la posicion inicial del jugador...
        int[] posicionDelJugador = nivel.getPosicioninicialDelJugador();
        // ... e instanciamos al jugador
        jugador = new Jugador(posicionDelJugador[0], posicionDelJugador[1], configuracionDelJuego.getTamañoPanel());
    }

    /**
     * Carga y configuración de los fantasmas
     */
    private void cargarFantasmas() {
        fantasmas = new ArrayList<>();
        String[] fantasmasEnElNivel = nivel.getFantasmasEnELNivel();
        for (String fantasma : fantasmasEnElNivel) {
            FantasmaConfiguracion configuracioDelJuego = configuracionDelJuego.getFantasmaConfig(fantasma);
            int[] posicion = nivel.getPosicionInicialDelFantasma(configuracioDelJuego.getId());
            Fantasma fantasma1 = new Fantasma(posicion[0], posicion[1], configuracionDelJuego.getTamañoPanel(),
                    configuracioDelJuego.getId(), configuracioDelJuego.getImageSet(), configuracioDelJuego.getHvsV(),
                    configuracioDelJuego.getVelocidad(), configuracioDelJuego.getAccuracyThreshold());
            fantasmas.add(fantasma1);
        }
    }

    /**************************************************
     *       Logica de movimiento/actualizacion       *
     **************************************************/
    /**
     * Accion por defecto al suscribirse al Timer
     *
     * @param e objeto ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        comprobarModoInvisible();
        // Deteccion de colisiones
        comprobarComestibles();
        comprobarColisionJugadorFantasma();
        // mover fantasmas
        moverFantasma();
        // mover jugador
        moverJugador();
        // Redibujar escenario
        repaint();
    }

    /**
     * Logica para gestionar la proxima direccion del fantasma en caso de colisionar.
     */
    private void calcularLaSiguienteDireccionEnColision(Fantasma fantasma1, Fantasma fantasma2) {
        if (fantasma1.getActualDireccion() == fantasma2.getActualDireccion()) {
            if (fantasma1.getActualDireccion() == Movil.IZQUIERDA) {
                if (fantasma1.getPosicionX() < fantasma2.getPosicionX()) {
                    fantasma2.setDireccionOpuesta();
                } else {
                    fantasma1.setDireccionOpuesta();
                }
                return;
            }
            if (fantasma1.getActualDireccion() == Movil.DERECHA) {
                if (fantasma1.getPosicionX() > fantasma2.getPosicionX()) {
                    fantasma2.setDireccionOpuesta();
                } else {
                    fantasma1.setDireccionOpuesta();
                }
            }
            if (fantasma1.getActualDireccion() == Movil.ARRIBA) {
                if (fantasma1.getPosicionY() < fantasma2.getPosicionY()) {
                    fantasma2.setDireccionOpuesta();
                } else {
                    fantasma1.setDireccionOpuesta();
                }
            }
            if (fantasma1.getActualDireccion() == Movil.ABAJO) {
                if (fantasma1.getPosicionX() > fantasma2.getPosicionX()) {
                    fantasma2.setDireccionOpuesta();
                } else {
                    fantasma1.setDireccionOpuesta();
                }
            }
        } else {
            if (fantasma1.getActualDireccion() == Movil.IZQUIERDA) {
                if (fantasma1.getPosicionX() > fantasma2.getPosicionX()) {
                    fantasma1.setDireccionOpuesta();
                    fantasma2.setDireccionOpuesta();
                }
            }
            if (fantasma1.getActualDireccion() == Movil.DERECHA) {
                if (fantasma1.getPosicionX() < fantasma2.getPosicionX()) {
                    fantasma1.setDireccionOpuesta();
                }
            }
            if (fantasma1.getActualDireccion() == Movil.ARRIBA) {
                if (fantasma1.getPosicionY() > fantasma2.getPosicionY()) {
                    fantasma1.setDireccionOpuesta();
                }
            }
            if (fantasma1.getActualDireccion() == Movil.ABAJO) {
                if (fantasma1.getPosicionX() < fantasma2.getPosicionX()) {
                    fantasma1.setDireccionOpuesta();
                }
            }
        }
    }

    /**
     * Logica para gestionar la colision de los fantasmas entre ellos
     */
    private void comprobarColisionesDeFantasmas(Fantasma fantasma) {
        boolean colisionado = false;
        for (Fantasma fant : fantasmas) {
            // Comprobamos que no se chequee el fantasma consigomismo.
            if (fantasma.getID().equals(fant.getID())) {
                continue;
            } else {
                Rectangle espacioFantasma1 = fantasma.getLimites();
                Rectangle espacioFantasma2 = fant.getLimites();
                if (espacioFantasma1.intersects(espacioFantasma2)) {
                    if (fantasma.estaColisionadoConFanstasma(fant.getID())) {
                        continue;
                    }
                    colisionado = true;
                    fantasma.addFantasmaColisionado(fant.getID());
                    fant.addFantasmaColisionado(fantasma.getID());
                    if (!fantasma.tieneColision()) {
                        fantasma.setTieneColision(true);
                    }
                    if (!fant.tieneColision()) {
                        fant.setTieneColision(true);
                    }
                    // se fuerza un cálculo especial de direccion del fantasma
                    calcularLaSiguienteDireccionEnColision(fantasma, fant);
                } else {
                    // Si no se están solapando, se quita en caso de existir la relacion entre
                    // los fantasmas colisionados.
                    fantasma.removerFantasmaColisionado(fant.getID());
                    fant.removerFantasmaColisionado(fantasma.getID());
                }
            }
        }
        if (!colisionado) {
            fantasma.setTieneColision(false);
        }
    }

    /**
     * Logica para gestionar la colision de los fantasmas y el jugador
     */
    private void comprobarColisionJugadorFantasma() {
        Rectangle espacioJugador = jugador.getBounds();
        Iterator<Fantasma> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            Fantasma fant = iterator.next();
            Rectangle jugadorEspacio = fant.getLimites();
            if (espacioJugador.intersects(jugadorEspacio)) {
                if (fant.getEstado() == Fantasma.MUERTO) {
                    continue;
                }
                if (modoInvisible) {
                    puntuacion.incrementarPuntuacion(100);
                    fant.setEstado(Fantasma.MUERTO);
                    // Regenerar el fantasma en su posicion inicial.
                    int[] posicionFantasma = nivel.getPosicionInicialDelFantasma(fant.getID());
                    fant.setPosicionX(posicionFantasma[0]);
                    fant.setPosicionY(posicionFantasma[1]);
                } else {
                    puntuacion.agregarPuntuacion();
                    VentanaJuego.cargarPantallaGameOver();
                }
            }
        }
    }

    /**
     * Logica de movimiento de los fantasmas. Aqui se gestiona la decision de
     * calcular el proximo movimiento del fantasma, pero esa logica se gestiona
     * dentro del propio fantasma
     *
     * @see Fantasma
     */
    private void moverFantasma() {
        // Mover fantasmas
        Iterator<Fantasma> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            Fantasma fantasma = iterator.next();
            if (fantasma.getEstado() == Fantasma.MUERTO) {
                fantasma.comprobarTiempoEstadoMuerto();
                continue;
            }
            // Comprueba las colisiones con otros fantasmas
            comprobarColisionesDeFantasmas(fantasma);
            // Si colisiona con mas de 1 a la vez, paramos  al fantasma.
            if (fantasma.getFantasmasColisionados() > 1) {
                continue;
            }
            // Si esta parado calcula la proxima direccion.
            if (fantasma.getActualDireccion() == 0 || fantasma.getSiguienteDireccion() == 0) {
                fantasma.calcularSiguientePosicion(jugador.getPosicionX(), jugador.getPosicionY());
            }
            if (puedeMoverse(fantasma)) {
                if (!fantasma.tieneColision()) {
                    if (tieneQueCambiarALaSiguienteDireccion(fantasma)) {
                        fantasma.cambiarADireccionSiguiente();
                        fantasma.calcularSiguientePosicion(jugador.getPosicionX(), jugador.getPosicionY());
                    }
                }
                fantasma.mover();
            } else {
                fantasma.calcularSiguientePosicion(jugador.getPosicionX(), jugador.getPosicionY());
            }
        }
    }

    /**
     * Logica basica para saber si el jugador se puede mover con base en las reglas.
     */
    private void moverJugador() {
        boolean puedeMoverse;
        // Manejamos la logica de movimiento del actor (En este caso jugador)
        // y comprobamos si puede moverse con base en las reglas
        if (tieneQueCambiarALaSiguienteDireccion(jugador)) {
            jugador.cambiarADireccionSiguiente();
        }
        puedeMoverse = puedeMoverse(jugador);
        if (puedeMoverse) {
            jugador.mover();
        }
    }

    /**
     * Logica de colision del jugador con los elementos recolectables.
     * Por cada elemento recolectable se comprueba si el jugador
     * está chocando con ellos.
     * En caso de encontrar un recolectable de poder, establece
     * el modo invencible.
     */
    private void comprobarComestibles() {
        Rectangle jugadorB = jugador.getBounds();
        ArrayList<Comestible> comestibles = nivel.getComestibles();
        Iterator<Comestible> iterator = comestibles.iterator();
        while (iterator.hasNext()) {
            Comestible comestible = iterator.next();
            Rectangle comestibleB = comestible.getLimite();
            if (jugadorB.intersects(comestibleB)) {
                puntuacion.incrementarPuntuacion(comestible.getPuntos());
                if (comestible.getTipo().equals("SuperDot")) {
                    setModoInvisible();
                }
                iterator.remove();
            }
        }
        if (nivel.getComestibles().isEmpty()) {
            VentanaJuego.cargarPantallaDeGanador();
        }
    }

    /**
     * Logica para decidir si el actor debe cambiar la direccion.
     * Por ejemplo sí ha llegado a una interseccion. O debe cambiar de direccion a la opuesta
     * porque 2 fantasmas han colisionado, o el jugador ha decidido cambiar de direccion.
     *
     * @param personaje jugador o fantasma
     * @return valor booleano indicando si el actor debe cambiar la direccion.
     */
    private boolean tieneQueCambiarALaSiguienteDireccion(Movil personaje) {
        int direccionSiguiente = personaje.getSiguienteDireccion();
        int[] posicionDelPersonaje = personaje.getPosicion();
        // Si el actor está centrado en una celda de la rejilla
        // comprobamos si podemos cambiar de direccion
        if (estaCentradoElPersonaje(posicionDelPersonaje)) {
            // Comprobamos que la proxima celda sea navegable.
            if (esNavegableLaSiguienteCelda(posicionDelPersonaje, direccionSiguiente)) {
                // Si lo es, cambiamos la direccion del actor:
                return true;
            }
        }
        // Si no está centrado o la proxima celda del cambio de direccion no es navegable
        // comprobamos si la siguiente direccion es la opuesta:
        if (personaje instanceof Jugador) {
            if (personaje.esPosicionSiguienteOpuestaAPosicionActual()) {
                // Si lo es, cambiamos la direccion del actor:
                return esNavegableLaSiguienteCelda(posicionDelPersonaje, direccionSiguiente);
            }
        }
        return false;
    }

    /**
     * Logica principal para saber si el jugador puede moverse.
     *
     * @param personaje fantasma o jugador.
     * @return boolean con la decision de sí el jugador puede moverse.
     */
    private boolean puedeMoverse(Movil personaje) {
        if (personaje instanceof Fantasma) {
        }
        int direccionActual = personaje.getActualDireccion();
        int[] posicionPersonaje = personaje.getPosicion();
        if (tieneQueCambiarALaSiguienteDireccion(personaje)) {
            return true;
        }
        // Por último comprobamos si se puede mover en la direccion que iba:
        return esNavegableLaSiguienteCelda(posicionPersonaje, direccionActual) || (!estaCentradoElPersonaje(posicionPersonaje));
        // En caso de que no sea navegable la siguiente zona
    }

    /**
     * Arranca el modo invencible. Se utiliza un timestamp para saber
     * cuando empezo el modo invencible.
     * Cambia el estado del fantasma hacia asustado en caso de no estar muerto.
     */
    private void setModoInvisible() {
        empezarModoInvisible = new Date().getTime();
        modoInvisible = true;
        Iterator<Fantasma> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            Fantasma fant = iterator.next();
            if (fant.getEstado() != Fantasma.MUERTO) {
                fant.setEstado(Fantasma.ASUSTADO);
            }
        }
    }

    /**
     * Comprueba si debe finalizar el modo invencible.
     */
    private void comprobarModoInvisible() {
        long milisegundosActual = new Date().getTime();
        if (milisegundosActual >= (empezarModoInvisible + configuracionDelJuego.getInvincibleModeDuration())) {
            desactivarModoInvisible();
        }
    }

    /**
     * Desactiva el modo invencible
     */
    private void desactivarModoInvisible() {
        modoInvisible = false;
        Iterator<Fantasma> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            Fantasma fant = iterator.next();
            if (fant.getEstado() != Fantasma.MUERTO) {
                fant.setEstado(Fantasma.PERSIGUE);
            }
        }
    }

    /**
     * Determina si el jugador esta centrado en la celda.
     *
     * @param posicionXY int[] de 2 huecos con la posicion XY del jugador
     * @return true si el actor esta centrado en la celda.
     */
    private boolean estaCentradoElPersonaje(int[] posicionXY) {
        int[] filaYColumna = nivel.convertirPixelesEnCeldas(posicionXY[0], posicionXY[1]);
        int[] filaYColumnaXY = nivel.convertirFilasAPixeles(filaYColumna[0], filaYColumna[1]);
        return ((posicionXY[0] == filaYColumnaXY[0]) && (posicionXY[1] == filaYColumnaXY[1]));
    }

    /**
     * Logica para saber si la proxima celda es navegable.
     *
     * @param posicionPerosnaje  int[] de 2 huecos con la posicion x,y del jugador
     * @param direccionSiguiente entero con la direccion.
     * @return verdadero si la proxima celda es navegable.
     */
    private boolean esNavegableLaSiguienteCelda(int[] posicionPerosnaje, int direccionSiguiente) {
        int centradoEnX = posicionPerosnaje[0];
        int centradoEnY = posicionPerosnaje[1];
        int[] filaYColumna = nivel.convertirPixelesEnCeldas(centradoEnX, centradoEnY);
        // inicializamos la celda a la celda 0,0 (esquina superior izq, que siempre sera
        // no navegable
        int[] celdaSiguiente = new int[2];
        celdaSiguiente[0] = 0;
        celdaSiguiente[1] = 0;
        switch (direccionSiguiente) {
            case Movil.ARRIBA:
                celdaSiguiente = nivel.getCeldaSuperior(filaYColumna[0], filaYColumna[1]);
                break;
            case Movil.ABAJO:
                celdaSiguiente = nivel.getCeldaInferior(filaYColumna[0], filaYColumna[1]);
                break;
            case Movil.IZQUIERDA:
                celdaSiguiente = nivel.getCeldaIzquierda(filaYColumna[0], filaYColumna[1]);
                break;
            case Movil.DERECHA:
                celdaSiguiente = nivel.getCeldaDerecha(filaYColumna[0], filaYColumna[1]);
                break;
        }
        return nivel.esNavegableLaCelda(celdaSiguiente[0], celdaSiguiente[1]);
    }

    /**
     * Carga el juego.
     */
    public void cargarJuego() {
        configurarJuego();

    }

    /**************************************************
     *           Llamadas de renderizado              *
     **************************************************/
    /**
     * Llamada principal para dibujar cada frame. Se ha dividido la logica
     * lo en funciones para facilitar la lectura del codigo.
     *
     * @param grafico Objeto Graphics
     */
    public void paint(Graphics grafico) {
        super.paint(grafico);
        Graphics2D grafico2D = (Graphics2D) grafico;
        grafico2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        grafico2D.setFont(fuenteDeTexto.deriveFont((float) ((float) Tablero.configuracionDelJuego.getTamañoPanel() * 0.70)));
        pintarFondo(grafico2D);// dibujamos el fondo
        pintarComestibles(grafico2D);// pintamos los objetos recolectables
        grafico2D.drawImage(jugador.getImage(), jugador.getPosicionX(), jugador.getPosicionY(), configuracionDelJuego.getTamañoPanel(), configuracionDelJuego.getTamañoPanel(), this);// Dibujamos al jugador
        pintarFantasmas(grafico2D);// Dibujamos a los fantasmas
        pintarPuntajeEnElJuego(grafico2D);
        Toolkit.getDefaultToolkit().sync();
        grafico.dispose();
    }

    /**
     * Dibuja a los fantasmas
     *
     * @param g2d: Objeto Graphics2D
     */
    private void pintarFantasmas(Graphics2D g2d) {
        Iterator<Fantasma> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            Fantasma g = iterator.next();
            g2d.drawImage(g.getImage(), g.getPosicionX(), g.getPosicionY(), configuracionDelJuego.getTamañoPanel(), configuracionDelJuego.getTamañoPanel(), this);
        }
    }

    /**
     * Dibuja  el fondo de pantalla.
     *
     * @param g2d: Objeto Graphics2D
     */
    private void pintarFondo(Graphics2D g2d) {
        int filas = nivel.getNumeroDeFilas();
        int columnas = nivel.getNumeroDeColumnas();
        int fila, columna, bg_x, bg_y = 0;
        for (fila = 0; fila < filas; fila++) {
            bg_x = 0;
            for (columna = 0; columna < columnas; columna++) {
                Image bg_image = nivel.getImagenDeCelda(fila, columna);
                g2d.drawImage(bg_image, bg_x, bg_y, configuracionDelJuego.getTamañoPanel(), configuracionDelJuego.getTamañoPanel(), this);
                bg_x = bg_x + configuracionDelJuego.getTamañoPanel();
            }
            bg_y = bg_y + configuracionDelJuego.getTamañoPanel();
        }
    }

    /**
     * Dibuja los objetos recolectables del nivel.
     *
     * @param g2d Objeto Graphics2D
     */
    private void pintarComestibles(Graphics2D g2d) {
        ArrayList<Comestible> comestiblesDibujo = nivel.getComestibles();
        Iterator<Comestible> iterator = comestiblesDibujo.iterator();
        while (iterator.hasNext()) {
            Comestible pick = iterator.next();
            Image img = pick.getImage();
            int x = pick.getPosicionX();
            int y = pick.getPosicionY();
            g2d.drawImage(img, x, y, configuracionDelJuego.getTamañoPanel(), configuracionDelJuego.getTamañoPanel(), this);
        }
    }

    /**
     * Dibuja los textos de la puntuacion
     *
     * @param g2d Objeto Graphics2D
     */
    private void pintarPuntajeEnElJuego(Graphics2D g2d) {
        Color color = (Color.WHITE);
        g2d.setColor(color);
        pintarString(g2d, puntuacion.getPuntuacionText(), 20, 2);
        pintarString(g2d, puntuacion.getPuntuacion(), 160, 2);
        pintarString(g2d, "ENTER PARA PAUSAR", 350,2);
    }

    /**
     * Funcion de ayuda para dividir en varias líneas las cadenas de texto
     * que tengan saltos de linea (con \n).
     *
     * @param g2d Objeto Graphics2D
     */
    public static void pintarString(Graphics2D g2d, String text, int x, int y) {
        for (String line : text.split("\n"))
            g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
    }

    /**
     * Devuelve la fuente de texto
     *
     * @return
     */
    private void setFuenteDeTexto() {
        try {
            String nombreDeArchivo = "src/resources/fonts/HFFFireDancer.ttf";
            File fuenteDeTextoArchivo = new File(nombreDeArchivo);
            fuenteDeTexto = Font.createFont(Font.TRUETYPE_FONT, fuenteDeTextoArchivo);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuenteDeTexto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}