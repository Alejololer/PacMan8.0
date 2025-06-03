package interfazGrafica;

import nivel.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Esta clase Se encarga de administrar todas las clases jPanel del programa, ademas que inicializa el tablero.
 */

public class VentanaJuego extends JFrame {
    /**
     * En el constructor se declaran las dimensiones, titulo y ubicacion que va a tener la pantalla
     */
    public VentanaJuego() {
        setTitle("POLI PAC-MAN");
        setSize(500, 676);
        setLocationRelativeTo(null);
        iniciarComponentes();
        setMinimumSize(new Dimension(570, 676));
        setMaximumSize(new Dimension(570, 676));
        this.getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Este metodo se utiliza para mostrar la pantalla GameOver, parar el tablero y ocultar el panel tablero
     */
    public static void cargarPantallaGameOver() {
        pantallaGameOver.setVisible(true);
        Tablero.timer.stop();
        tablero.setVisible(false);
    }

    /**
     * Este metodo se utiliza para mostrar la pantalla  ganado y ocultar la pantalla tablero
     */
    public static void cargarPantallaDeGanador() {
        pantallaGanador.setVisible(true);
        tablero.setVisible(false);
    }

    /**
     * Este metodo es encargado de llamar a los metodos que se encargar de iniciar cada componente de la ventana
     */
    private void iniciarComponentes() {
        colocarPanel();
        colocarBotones();
        colocarLabel();
        eventosDelTeclado();
    }

    /**
     * Este metodo es el encargado de administrar las funciones del teclado y se le asigna a una de las pantallas.
     */
    private void eventosDelTeclado() {
        KeyListener eventoDelTeclado = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (Tablero.jugador != null) {
                    Tablero.jugador.keyPressed(e);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Tablero.timer.stop();
                    pantallaPausa.setVisible(true);
                    tablero.setVisible(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        tablero.addKeyListener(eventoDelTeclado);
    }

    /**
     * Este metodo es encargado de inicializar los botones y agregarlos a la ventana
     */
    private void colocarBotones() {
        jBJugar = new JButton();
        jBSalir = new JButton();

        jBJugar.setBounds(230, 330, 140, 60);
        ImageIcon imagenJugar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/jugar.png"));
        jBJugar.setIcon(new ImageIcon(imagenJugar.getImage().getScaledInstance(jBJugar.getWidth(), jBJugar.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        jPanel.add(jBJugar);
        jBJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                jBJugarActionPerformed(evt);
            }
        });

        jBSalir.setBounds(230, 400, 140, 60);
        ImageIcon imagenSalir = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/salir.png"));
        jBSalir.setIcon(new ImageIcon(imagenSalir.getImage().getScaledInstance(jBSalir.getWidth(), jBSalir.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        jPanel.add(jBSalir);
        jBSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBSalirActionPerformed(evt);
            }
        });


    }

    /**
     * Estos metodos son encargados de agregar acciones a los botones
     */
    private void jBSalirActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    public static void jBJugarActionPerformed(ActionEvent evt) {
        partidas = new ArrayList<>();


        String[] botones = {"SI", "NO", "CANCEL"};
        int opcion = JOptionPane.showOptionDialog(null, "¿Desea cargar partida anterior?", "mensaje",
                0, JOptionPane.QUESTION_MESSAGE, null, botones, "SI");
        if (opcion == 0) {
            try {
                InputStream is = new FileInputStream("src/resources/archivos/partidas.bin");
                ObjectInputStream ois = new ObjectInputStream(is);
                Tablero tableroGuardado = (Tablero) ois.readObject();
                partidas.add(tableroGuardado);
                tableroGuardado.arrancarJuego();
            } catch (Exception e) {
                System.out.println("Error al leer la partida del archivo");
                e.printStackTrace();
            }
            tablero.setVisible(false);
            jPanel.setVisible(false);
        }
        if (opcion == 1) {
            tablero.timer.start();
            tablero.setVisible(true);
            jPanel.setVisible(false);
        }


    }

    /**
     * Este metodo es el que instancia a todos los paneles y clases jPanel
     */
    private void colocarPanel() {
        jPanel = new JPanel();
        tablero = new Tablero();
        pantallaGameOver = new PantallaGameOver();
        pantallaGanador = new PantallaGanador();
        pantallaPausa = new PantallaPausa();
        pantallaPuntuacion = new PantallaPuntuacion();

        jPanel.setLayout(null);
        jPanel.setBounds(0, 0, 560, 640);
        jPanel.setBackground(Color.BLACK);

        add(jPanel);
        add(pantallaGameOver);
        add(pantallaGanador);
        add(pantallaPausa);
        add(pantallaPuntuacion);
        add(tablero);
        pantallaPausa.setVisible(false);
        pantallaGameOver.setVisible(false);
        pantallaPuntuacion.setVisible(false);
        pantallaGanador.setVisible(false);
    }

    /**
     * Este metodo es el encargado de colocar una etiqueta que sirve principalmente para colocar una imagen
     */
    private void colocarLabel() {
        jLPortada = new JLabel();
        ImageIcon imagen = new ImageIcon(VentanaJuego.class.getResource("/resources/images/pantallas/pantallaInicio.png"));
        jLPortada.setBounds(0, 0, 560, 640);
        jLPortada.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(jLPortada.getWidth(), jLPortada.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        jPanel.add(jLPortada);
    }

    /**
     * Se instancia todas las variables que se van a utilizar en la clase
     */
    public static PantallaPuntuacion pantallaPuntuacion;
    public static PantallaGameOver pantallaGameOver;
    public static Tablero tablero;
    public static ArrayList<Tablero> partidas;
    private static PantallaGanador pantallaGanador;
    private PantallaPausa pantallaPausa;
    private static JPanel jPanel;
    private JButton jBJugar;
    private JButton jBSalir;
    private JLabel jLPortada;
}

