package interfazGrafica;

import nivel.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Es la encargada de administrar los componentes cuando un jugador pasa un nivel
 */
public class PantallaGanador extends JPanel {
    /**
     * En el constructor se declaran las dimensiones y fondo que va a tener la pantalla
     */
    public PantallaGanador() {
        setSize(600, 640);
        setBackground(Color.BLACK);
        setMinimumSize(new Dimension(570, 676));
        setMaximumSize(new Dimension(570, 676));
        setLayout(null);
        iniciarComponentes();
    }

    /**
     * Este metodo es encargado de llamar a los metodos que se encargar de iniciar cada componente de la ventana
     */
    private void iniciarComponentes() {
        colocarBotones();
        colocarLabel();
    }

    /**
     * Este metodo es el encargado de colocar una etiqueta que sirve principalmente para colocar una imagen
     */
    private void colocarLabel() {
        jLGanador = new JLabel();
        ImageIcon imagen = new ImageIcon(VentanaJuego.class.getResource("/resources/images/pantallas/pantallaGanador.png"));
        jLGanador.setBounds(0, 0, 560, 640);
        jLGanador.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(jLGanador.getWidth(), jLGanador.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        this.add(jLGanador);
    }

    /**
     * Este metodo es encargado de inicializar los botones y agregarlos a el panel
     */
    private void colocarBotones() {
        jBContinuar = new JButton();
        jBContinuar.setBounds(75, 500, 150, 60);
        ImageIcon imagenContinuar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/continuar.png"));
        jBContinuar.setIcon(new ImageIcon(imagenContinuar.getImage().getScaledInstance(jBContinuar.getWidth(), jBContinuar.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBContinuar);
        jBContinuar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBVolverAJugarActionPerformed(evt);
            }
        });
        jBSalir = new JButton();
        jBSalir.setBounds(325, 500, 150, 60);
        ImageIcon imagenSalir = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/salir.png"));
        jBSalir.setIcon(new ImageIcon(imagenSalir.getImage().getScaledInstance(jBSalir.getWidth(), jBSalir.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBSalir);
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

    private void jBVolverAJugarActionPerformed(ActionEvent evt) {
        VentanaJuego.tablero.setVisible(true);
        Tablero.contadorNivel = Tablero.contadorNivel + 1;
        VentanaJuego.tablero.arrancarJuego();
        VentanaJuego.tablero.timer.start();
        this.setVisible(false);
    }

    /**
     * Se instancia todas las variables que se van a utilizar en la clase
     */
    private JLabel jLGanador;
    private JButton jBContinuar;
    private JButton jBSalir;
}
