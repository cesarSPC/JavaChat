/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente.Modelo;

/**
 *
 * @author Familia Mora
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;

/**
 * Clase ArchivoPropiedades
 *
 * Esta clase maneja la lectura de un archivo de propiedades y proporciona
 * acceso a los datos almacenados en él.
 *
 * @author Familia Mora
 */
public class ArchivoPropiedades {

    /**
     * Archivo de propiedades a leer.
     */
    private File archivo;
    /**
     * Objeto Properties que contiene las propiedades leídas del archivo.
     */
    private Properties propiedades = new Properties();
    /**
     * Flujo de entrada para leer el archivo de propiedades.
     */
    private InputStream entrada = null;

    /**
     * Constructor de la clase ArchivoPropiedades.
     *
     * @param archivo Archivo de propiedades a leer.
     */
    public ArchivoPropiedades(File archivo) {
        this.archivo = archivo;
        this.leerArchivo();
    }
    
    /**
     * Método que lee el archivo de propiedades y carga sus datos en el objeto
     * Properties.
     */
    public void leerArchivo() {
        if (archivo == null) {
            System.exit(0);
            return;
        }
        try {
            entrada = new FileInputStream(archivo);
            propiedades.load(entrada);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (entrada == null) {
                return;
            }
            try {
                entrada.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData(String key) {
        return this.propiedades.getProperty(key);
    }

}