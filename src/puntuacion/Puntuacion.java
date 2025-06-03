package puntuacion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Clase encargada de manejar la Puntuacion del juego
 */
public class Puntuacion {
    private String puntuacion;
    private final String textoPuntuacion;
    public static ArrayList<Integer> puntuaciones;

    public Puntuacion() {
        textoPuntuacion = "Puntuacion";
        puntuacion = "0";
        this.puntuaciones = new ArrayList<>();
        agregarPuntuacion();
        leerArchivoDePuntuaciones();
    }

    /**
     * Incrementa la puntuación del juego
     *
     * @param valor valor a incrementar la puntuacion.
     */
    public void incrementarPuntuacion(int valor) {
        int nuevoValor = Integer.parseInt(puntuacion) + valor;
        puntuacion = String.valueOf(nuevoValor);
    }

    /**
     * Agrega una nueva puntuación al archivo y actualiza la lista de puntuaciones
     */
    public void agregarPuntuacion() {
        try {
            FileWriter writer = new FileWriter("src/resources/archivos/puntuaciones.txt", true);
            writer.write(puntuacion + "\n");
            writer.close();
            this.puntuaciones.add(Integer.valueOf(puntuacion));
            Collections.sort(this.puntuaciones, Collections.reverseOrder());
        } catch (IOException e) {
            System.out.println("Error al agregar la puntuación al archivo");
            e.printStackTrace();
        }
    }

    /**
     * Lee las puntuaciones almacenadas en el archivo y las carga en la lista
     */
    private void leerArchivoDePuntuaciones() {
        try {
            File file = new File("src/resources/archivos/puntuaciones.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int puntaje = 0;
                puntaje = Integer.parseInt(scanner.nextLine());
                this.puntuaciones.add(puntaje);
            }
            scanner.close();
            Collections.sort(this.puntuaciones, Collections.reverseOrder());
        } catch (IOException e) {
            System.out.println("Error al leer las puntuaciones del archivo");
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la puntuacion actual
     *
     * @return la puntuacion actual.
     */
    public String getPuntuacion() {
        return puntuacion;
    }


    /**
     * Obtiene las posiciones almacenadas en el ArrayList
     *
     * @param i interador de posiciones
     * @return lista de puntuaciones alamcenadas por ID
     */
    public Integer getPuntuacionesIndividuales(int i) {
        return this.puntuaciones.get(i);
    }

    /**
     * Resetea el valor a 0 (por ejemplo al reiniciar el juego)
     */
    public void resetScore() {
        puntuacion = "0";
    }

    /**
     * Devuelve el literal del texto de puntuacion.
     *
     * @return literal del texto de puntuacion.
     */
    public String getPuntuacionText() {
        return textoPuntuacion;
    }
}

