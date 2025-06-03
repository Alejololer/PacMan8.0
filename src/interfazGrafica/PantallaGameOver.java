package interfazGrafica;

import nivel.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Es la encargada de administrar los componentes cuando un jugador pierde
 */
public class PantallaGameOver extends JPanel {
    /**
     * En el constructor se declaran las dimensiones y fondo que va a tener la pantalla
     */
    public PantallaGameOver() {
        setSize(600, 640);
        iniciarComponentes();
        setMinimumSize(new Dimension(570, 676));
        setMaximumSize(new Dimension(570, 676));
        setLayout(null);
    }

    /**
     * Este metodo es encargado de llamar a los metodos que se encargar de iniciar cada componente de la ventana
     */
    private void iniciarComponentes() {
        colocarBotones();
        colocarLabel();
    }

    /**
     * Este metodo es encargado de inicializar los botones y agregarlos a el panel
     */
    private void colocarBotones() {
        jBVolverAJugar = new JButton();
        jBVolverAJugar.setBounds(45, 480, 150, 60);
        ImageIcon imagenVolverAJugar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/volverAJugar.png"));
        jBVolverAJugar.setIcon(new ImageIcon(imagenVolverAJugar.getImage().getScaledInstance(jBVolverAJugar.getWidth(), jBVolverAJugar.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBVolverAJugar);
        jBVolverAJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBVolverAJugarActionPerformed(evt);
            }
        });

        jBMenuPrincipal = new JButton();
        jBMenuPrincipal.setBounds(205, 560, 150, 60);
        ImageIcon imagenMenuPrincipal = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/menuPrincipal.png"));
        jBMenuPrincipal.setIcon(new ImageIcon(imagenMenuPrincipal.getImage().getScaledInstance(jBMenuPrincipal.getWidth(), jBMenuPrincipal.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBMenuPrincipal);
        jBMenuPrincipal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBMenuPrincipalActionPerformed(evt);
            }
        });

        jBPuntuacion = new JButton();
        jBPuntuacion.setBounds(375, 480, 150, 60);
        ImageIcon imagenJugar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/puntuacion.png"));
        jBPuntuacion.setIcon(new ImageIcon(imagenJugar.getImage().getScaledInstance(jBPuntuacion.getWidth(), jBPuntuacion.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBPuntuacion);
        jBPuntuacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBPuntuacionActionPerformed(evt);
            }
        });

    }

    /**
     * Estos metodos son encargados de agregar acciones a los botones
     */
    private void jBPuntuacionActionPerformed(ActionEvent evt) {
        VentanaJuego.pantallaPuntuacion.setVisible(true);
        this.setVisible(false);
    }

    public static void jBMenuPrincipalActionPerformed(ActionEvent evt) {
        VentanaJuego ventanaJuego = new VentanaJuego();
        ventanaJuego.setVisible(true);
    }

    private void jBVolverAJugarActionPerformed(ActionEvent evt) {
        VentanaJuego.tablero.setVisible(true);
        VentanaJuego.tablero.arrancarJuego();
        Tablero.timer.start();
        this.setVisible(false);
    }

    /**
     * Este metodo es el encargado de colocar una etiqueta que sirve principalmente para colocar una imagen
     */
    private void colocarLabel() {
        jLGameOver = new JLabel();
        ImageIcon imagen = new ImageIcon(VentanaJuego.class.getResource("/resources/images/pantallas/pantallaGameOver.png"));
        jLGameOver.setBounds(0, 0, 560, 640);
        jLGameOver.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(jLGameOver.getWidth(), jLGameOver.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        this.add(jLGameOver);
    }

    /**
     * Se instancia todas las variables que se van a utilizar en la clase
     */
    private JLabel jLGameOver;
    private JButton jBVolverAJugar;
    private JButton jBMenuPrincipal;
    private JButton jBPuntuacion;
}
