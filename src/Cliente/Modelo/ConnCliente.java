/*
 * Cliente.java
 *
 * Created on 21 de marzo de 2008, 12:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Cliente.Modelo;

import Cliente.Vista.VentCliente;
import Cliente.Controlador.threadCliente;
import Cliente.Controlador.Control;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class ConnCliente {

    public static String IP_SERVER;
    private Control control;
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private DataInputStream entrada2 = null;
    private Socket comunication = null;//para la comunicacion
    private Socket comunication2 = null;//para recivir msg

    private String nomCliente;

    /**
     * Creates a new instance of Cliente
     */
    public ConnCliente(Control control) {
        this.control = control;
    }

    public void conexion(int puerto1, int puerto2) throws IOException {
        comunication = new Socket(ConnCliente.IP_SERVER, puerto1);
        comunication2 = new Socket(ConnCliente.IP_SERVER, puerto2);
        entrada = new DataInputStream(comunication.getInputStream());
        salida = new DataOutputStream(comunication.getOutputStream());
        entrada2 = new DataInputStream(comunication2.getInputStream());
        nomCliente = control.getVista().inputEmergente("Introducir Nick :");
        control.getVista().setNombreUser(nomCliente);
        salida.writeUTF(nomCliente);
    }

    public void cerrar() {
        try {
            comunication.close();
            comunication2.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombre() {
        return nomCliente;
    }

    public DataInputStream getEntrada() {
        return entrada;
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    public DataInputStream getEntrada2() {
        return entrada2;
    }

}
