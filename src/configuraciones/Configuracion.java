package configuraciones;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Esta clase guarda la configuración general de la aplicación.
 * Guardamos:
 * - Tamaño en pixels de las celdas
 * - Fantasmas disponibles y su configuración.
 * Sé instancia siguiendo un patrón Singleton "Es un patrón de diseño creacional
 * que garantiza que tan solo exista un objeto de su tipo y proporciona un único punto de acceso a él
 * para cualquier otro código", ya que su información es sensible de ser utilizada en cualquier punto de la aplicación,
 * y solo es necesario cargar su información 1 vez.
 * Recoge la información de un fichero JSON situado en la carpeta:
 * - src/resources/config.json
 */

public class Configuracion {
    private final int tamañoTablero;
    private final long duracionModoInvicible;
    HashMap<String, FantasmaConfiguracion> fantasmas;
    private static Configuracion instance;

    /**
     * Este constructor declarado como protegido para obligar a utilizar el método
     * getInstance().
     * Recoge el fichero JSON
     *
     * @tamañoTablero: Tamaño de las celdas del juego en píxeles.
     * @fantasmas: HashMap que contiene la configuración de cada fantasma.
     */
    protected Configuracion() {
        instance = null;
        fantasmas = new HashMap<>();
        JSONObject obj = getConfigFile();
        Long size = (Long) obj.get("level_box_size");
        tamañoTablero = size.intValue();
        duracionModoInvicible = (Long) obj.get("invincible_mode_duration");
        JSONArray fantasmas = (JSONArray) obj.get("fantasmas");
        Iterator<JSONObject> iterator = fantasmas.iterator();
        while (iterator.hasNext()) {
            JSONObject config = iterator.next();
            String id = (String) config.get("id");
            String nombre = (String) config.get("nombre");
            String imageset = (String) config.get("imageset");
            Long HvsV = (Long) config.get("HvsV");
            Long velocidad = (Long) config.get("velocidad");
            Long accuracy = (Long) config.get("accuracy_threshold");
            FantasmaConfiguracion fantasma = new FantasmaConfiguracion(id, nombre, imageset, velocidad.intValue(), accuracy.intValue(), HvsV.intValue());
            this.fantasmas.put(id, fantasma);
        }
    }

    /**
     * Método estático que gestiona la instancia Singleton de la clase.
     *
     * @return una instancia inicializada de la clase.
     */
    public static Configuracion getInstance() {
        if (instance == null) {
            instance = new Configuracion();
        }
        return instance;
    }

    /**
     * Devuelve el tamaño en pixels de las celdas de la aplicacion
     *
     * @return tamaño en pixels de las celdas de la aplicacion
     */
    public int getTamañoPanel() {
        return tamañoTablero;
    }

    /**
     * Devuelve el valor en milisegundos del modo invencible
     *
     * @return milisegundos de la duracion de modo invencible.
     */
    public long getInvincibleModeDuration() {
        return duracionModoInvicible;
    }

    /**
     * @param id identificador del fantasma
     * @return objeto FantasmaConfig con la configuracion del fantasma solicitado
     * @see FantasmaConfiguracion
     */
    public FantasmaConfiguracion getFantasmaConfig(String id) {
        return fantasmas.get(id);
    }

    /**
     * Método para obtener un objeto JSONObject con la información
     * del fichero cargada.
     *
     * @return objeto JSONObject con la informacion del fichero cargado.
     * @see JSONObject
     */
    private JSONObject getConfigFile() {
        try {
            String path = new File(".").getCanonicalPath();
            String pathFile = path + "/src/resources/config/config.json";
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(pathFile));
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
