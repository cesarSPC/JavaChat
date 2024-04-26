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
 * Clase ConnCliente que maneja toda la conexion del cliente con el servidor
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

    /*
    *Crea las conexiones con los puertos 1 y 2, asi como tambien instancia las entradas
    *y salidas de los mensajes
    *@param puerto1 recibe un puerto conexion
    *@param puerto2 recibe un puerto conexion
     */
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
    
    /*
    *Cierra las comunicaciones para evitar vulnerabilidades en el sistema
    */
    public void cerrar() {
        try {
            comunication.close();
            comunication2.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *Recibe el nombre del cliente
    */
    public String getNombre() {
        return nomCliente;
    }
    
    /*
    *recibe la en etrada
    */
    public DataInputStream getEntrada() {
        return entrada;
    }
    /*
    *recibe la salida
    */
    public DataOutputStream getSalida() {
        return salida;
    }
    /*
    *recibe la entrada2
    */
    public DataInputStream getEntrada2() {
        return entrada2;
    }

}
