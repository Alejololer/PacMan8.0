package interfazGrafica;

import nivel.Tablero;
import puntuacion.Puntuacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Es la encargada de administrar los componentes cuando un jugador quiera ver las puntuaciones guardadas
 */
public class PantallaPuntuacion extends JPanel {
    /**
     * En el constructor se declaran las dimensiones y fondo que va a tener la pantalla
     */
    public PantallaPuntuacion() {
        puntos = new Puntuacion();
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
    }

    /**
     * Este metodo es encargado de inicializar los botones y agregarlos a el panel
     */
    private void colocarBotones() {
        jBRegresar = new JButton();
        jBRegresar.setBounds(350, 380, 150, 60);
        ImageIcon imagenRegresar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/regresar.png"));
        jBRegresar.setIcon(new ImageIcon(imagenRegresar.getImage().getScaledInstance(jBRegresar.getWidth(), jBRegresar.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBRegresar);
        jBRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jBRegresarActionPerformed(evt);
            }
        });
        jBSalir = new JButton();
        jBSalir.setBounds(350, 460, 150, 60);
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

    private void jBRegresarActionPerformed(ActionEvent evt) {
        VentanaJuego.pantallaGameOver.setVisible(true);
        this.setVisible(false);
    }

    /**
     * Este metodo es el encargado de dibujar las puntuaciones en la pantalla
     *
     * @param g2d Objeto Graphics2D
     */
    private void pintarPuntuacion(Graphics2D g2d) {
        Rectangle rect = getBounds();
        Color color = Color.WHITE;
        g2d.setColor(color);
        Tablero.pintarString(g2d, "Top" + "\nPuntuaciones", (rect.width / 2) - 270, 100);
        for (int i = 0; i < 5; i++) {
            Tablero.pintarString(g2d, String.valueOf(i + 1) + ".-   " + String.valueOf(puntos.getPuntuacionesIndividuales(i)), (rect.width / 2) - 270, 250 + (i * 60));
        }
    }

    /**
     * Este metodo es el encargado de configurar la manera en que se va a pintar la pantalla incluyendo
     * la fuente de texto
     *
     * @param grafico Objeto Graphics
     */
    public void paint(Graphics grafico) {
        super.paint(grafico);
        Graphics2D grafico2D = (Graphics2D) grafico;
        grafico2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        grafico2D.setFont(Tablero.fuenteDeTexto.deriveFont((float) ((float) 100 * 0.50)));
        pintarPuntuacion(grafico2D);
        Toolkit.getDefaultToolkit().sync();
        grafico.dispose();
    }

    /**
     * Se instancia todas las variables que se van a utilizar en la clase
     */
    private Puntuacion puntos;
    private JButton jBRegresar;
    private JButton jBSalir;

}