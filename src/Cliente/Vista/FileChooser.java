/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente.Vista;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 * Esta clase proporciona una interfaz gráfica para seleccionar un archivo del sistema de archivos.
 * Utiliza el componente JFileChooser de la biblioteca Swing de Java.
 *
 * @author cesar
 */
public class FileChooser {
    /**
     * Instancia de JFileChooser para seleccionar el archivo.
     */
    private JFileChooser fc;
    /**
     * Título de la ventana del selector de archivos.
     */
    private String nombreVentana;
    /**
     * Constructor de la clase FileChooser.
     *
     * @param nombreVentana Título de la ventana del selector de archivos.
     */
    public FileChooser(String nombreVentana) {
        this.nombreVentana = nombreVentana;
    }
    /**
     * Muestra el selector de archivos y devuelve el archivo seleccionado por el usuario.
     *
     * @return El archivo seleccionado por el usuario, o null si no se seleccionó ninguno.
     */
    public File getFile() {
        UIManager.put("FileChooser.openDialogTitleText", nombreVentana);
        fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showOpenDialog(fc);
        return fc.getSelectedFile();
    }
}