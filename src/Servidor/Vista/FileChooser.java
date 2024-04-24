/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor.Vista;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author cesar
 */
public class FileChooser {

    private JFileChooser fc;
    private String nombreVentana;

    public FileChooser(String nombreVentana) {
        this.nombreVentana = nombreVentana;
    }

    public File getFile() {
        UIManager.put("FileChooser.openDialogTitleText", nombreVentana);
        fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showOpenDialog(fc);
        return fc.getSelectedFile();
    }
}