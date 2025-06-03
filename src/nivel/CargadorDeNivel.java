package nivel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

/**
 * Cargador de niveles recooge la informacion de un fichero JSON, y lo carga en un objeto
 * de tipo Nivel.
 * La información a recoger es de momento:
 * - La estructura del mapa
 * - Fantasmas disponibles en el mapa.
 */
public class CargadorDeNivel implements Serializable {
    private Nivel tablero;

    /**
     * Constructor
     */
    public CargadorDeNivel() {
        tablero = new Nivel();
    }

    /**
     * Carga la información del nivel en un objeto Nivel.
     *
     * @param numeroDeNivel El numero del nivel a cargar.
     * @return Devuelve un objeto Nivel con la información del nivel.
     * @see Nivel
     */
    public Nivel cargarNivel(int numeroDeNivel) {
        JSONObject obj = getArchivoJSON(numeroDeNivel);
        tablero.setNombreDeCarpetaConPatronDeImagenes((String) obj.get("level_image_pattern"));
        tablero.crearSetDeImagenes();
        JSONArray patrones = (JSONArray) obj.get("level_pattern");
        // Obtenemos el primer elemento para saber cuantas columnas tiene
        JSONArray primerElemento = (JSONArray) patrones.get(0);
        int columnas = primerElemento.size();
        // Vaciamos primerElemento ya que no lo necesitamos más
        primerElemento = null;
        // Obtenemos las filas las filas
        int filas = patrones.size();
        // Inicializamos las celdas basándonos en el tamaño
        tablero.crearTableroDeCeldas(filas, columnas);
        Iterator<JSONArray> filaIterador = patrones.iterator();
        int fil = 0;
        while (filaIterador.hasNext()) {
            JSONArray filaActual = filaIterador.next();
            Iterator<Long> columnaIterador = filaActual.iterator();
            int col = 0;
            while (columnaIterador.hasNext()) {
                int valor = columnaIterador.next().intValue();
                tablero.setCelda(fil, col, valor);
                col++;
            }
            fil++;
        }
        JSONArray fantasmasEnNivel = (JSONArray) obj.get("fantasmas");
        String[] fantasmas = new String[fantasmasEnNivel.size()];
        int i = 0;
        Iterator<String> fantasmaIterator = fantasmasEnNivel.iterator();
        while (fantasmaIterator.hasNext()) {
            fantasmas[i] = fantasmaIterator.next();
            i++;
        }
        tablero.setFantasmas(fantasmas);
        return tablero;
    }

    /**
     * Obtiene el objeto json con la información del nivel.
     *
     * @param numeroDeNivel
     * @return devuelve el objeto json con la información del nivel.
     * @see JSONObject
     */
    private JSONObject getArchivoJSON(int numeroDeNivel) {
        try {
            String ruta = new File(".").getCanonicalPath();
            String archivoDeNivel = "nivel" + numeroDeNivel + ".json";
            String rutaDeArchivo = ruta + "/src/resources/levels/" + archivoDeNivel;
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(rutaDeArchivo));
            JSONObject retornarValor = (JSONObject) obj;
            return retornarValor;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
