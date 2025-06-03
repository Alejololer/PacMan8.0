package configuraciones;


import java.io.Serializable;

/**
 * En esta clase almacenaremos la configuración de los fantasmas.
 * Para evitar utilizar objetos json cada vez que se requiere la información
 * de un fantasma complicando la legibilidad y escritura del código, se ha
 * optado por la creación de esta clase de ayuda.
 * Como su finalidad es de solo lectura, solo se crean los "getters" de los
 * diferentes atributos
 */
public class FantasmaConfiguracion {
    String id;
    String nombre;
    String imageset;
    int velocidad;
    int accuracy_threshold;
    int HvsV;

    public FantasmaConfiguracion(String id, String nombre, String imageset, int velocidad, int accuracy_threshold, int HvsV) {
        this.id = id;
        this.nombre = nombre;
        this.imageset = imageset;
        this.velocidad = velocidad;
        this.accuracy_threshold = accuracy_threshold;
        this.HvsV = HvsV;
    }

    /**
     * Devuelve el id de la configuración del fantasma
     *
     * @return id del fantasma
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el set de imagenes anotado en la configuración del fantasma
     *
     * @return set de imagenes del fantasma
     */
    public String getImageSet() {
        return imageset;
    }

    /**
     * Devuelve la velocidad de la configuración del fantasma
     *
     * @return velocidad del fantasma
     */
    public int getVelocidad() {
        return velocidad;
    }


    /**
     * Devuelve el hilo de precisión anotado en la configuración del fantasma
     *
     * @return hilo de precisión del fantasma
     */
    public int getAccuracyThreshold() {
        return accuracy_threshold;
    }

    /**
     * Devuelve el valor del parametro HvsV de la configuración del fantasma
     *
     * @return valor HvsV del fantasma
     */
    public int getHvsV() {
        return HvsV;
    }
}
