package interfazGrafica;

import nivel.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

/**
 * Es la encargada de administrar los componentes cuando un jugador ponga el juego en pausa
 */
public class PantallaPausa extends JPanel {
    /**
     * En el constructor se declaran las dimensiones y fondo que va a tener la pantalla
     */
    public PantallaPausa() {
        setSize(500, 646);
        iniciarComponentes();
        setMinimumSize(new Dimension(570, 676));
        setMaximumSize(new Dimension(570, 676));
        setBackground(Color.BLACK);
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
        jBReanudar = new JButton();
        jBReanudar.setBounds(75, 500, 150, 60);
        ImageIcon imagenReanudar = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/reanudar.png"));
        jBReanudar.setIcon(new ImageIcon(imagenReanudar.getImage().getScaledInstance(jBReanudar.getWidth(), jBReanudar.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBReanudar);
        jBReanudar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBReanudarActionPerformed(evt);
            }
        });
        jBGuardarYSalir = new JButton();
        jBGuardarYSalir.setBounds(325, 500, 150, 60);
        ImageIcon imagenGuardarYSalir = new ImageIcon(VentanaJuego.class.getResource("/resources/images/iconosDeBoton/salir.png"));
        jBGuardarYSalir.setIcon(new ImageIcon(imagenGuardarYSalir.getImage().getScaledInstance(jBGuardarYSalir.getWidth(), jBGuardarYSalir.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        add(jBGuardarYSalir);
        jBGuardarYSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGuardarYSalirActionPerformed(evt);
            }
        });

    }

    /**
     * Estos metodos son encargados de agregar acciones a los botones
     */
    private void jBGuardarYSalirActionPerformed(ActionEvent evt) {
        try {
            FileOutputStream writer = new FileOutputStream("src/resources/archivos/partidas.bin", true);
            ObjectOutputStream oos = new ObjectOutputStream(writer);
            oos.writeObject(VentanaJuego.tablero + "\n");
            oos.close();
            System.out.println("Se a guardado la partida");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error al agregar la puntuación al archivo");
            e.printStackTrace();
        }
    }

    public void jBReanudarActionPerformed(ActionEvent e) {
        VentanaJuego.tablero.setVisible(true);
        Tablero.timer.start();
        this.setVisible(false);

    }

    /**
     * Este metodo es el encargado de colocar una etiqueta que sirve principalmente para colocar una imagen
     */
    private void colocarLabel() {
        jLPausa = new JLabel();
        ImageIcon imagen = new ImageIcon(VentanaJuego.class.getResource("/resources/images/pantallas/pantallaPausa.png"));
        jLPausa.setBounds(0, 0, 560, 640);
        jLPausa.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(jLPausa.getWidth(), jLPausa.getHeight(), Image.SCALE_SMOOTH))); // NOI18N
        this.add(jLPausa);
    }

    /**
     * Se instancia todas las variables que se van a utilizar en la clase
     */
    private JButton jBReanudar;
    private JButton jBGuardarYSalir;
    private JLabel jLPausa;
}
