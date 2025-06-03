import interfazGrafica.VentanaJuego;

import javax.swing.*;

/**
 * Clase principal del programa Pacman.
 *
 * @see JFrame
 */
@SuppressWarnings("serial")
public class Pacman extends JFrame {
    /**
     * Inicializa los atributos correspondientes a JFrame
     * y aniade un objeto Tablero (hijo de JPanel).
     */
    public static void main(String[] args) {
        VentanaJuego ventana = new VentanaJuego();
        ventana.setVisible(true);
    }
}





