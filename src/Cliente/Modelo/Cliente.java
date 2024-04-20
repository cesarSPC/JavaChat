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
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Cliente {

    public static String IP_SERVER;
    private VentCliente vent;
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private DataInputStream entrada2 = null;
    private Socket comunication = null;//para la comunicacion
    private Socket comunication2 = null;//para recivir msg

    private String nomCliente;

    /**
     * Creates a new instance of Cliente
     */
    public Cliente(VentCliente vent) throws IOException {
        this.vent = vent;
    }

    public void conexion() throws IOException {
        try {
            comunication = new Socket(Cliente.IP_SERVER, 8081);
            comunication2 = new Socket(Cliente.IP_SERVER, 8082);
            entrada = new DataInputStream(comunication.getInputStream());
            salida = new DataOutputStream(comunication.getOutputStream());
            entrada2 = new DataInputStream(comunication2.getInputStream());
            nomCliente = vent.inputEmergente("Introducir Nick :");
            vent.setNombreUser(nomCliente);
            salida.writeUTF(nomCliente);
        } catch (IOException e) {
            vent.mensajeConsola("\t=============================");
        }
        new threadCliente(entrada2, vent).start();
    }

    public String getNombre() {
        return nomCliente;
    }

    public Vector<String> pedirUsuarios() {
        Vector<String> users = new Vector();
        try {
            salida.writeInt(2);
            int numUsers = entrada.readInt();
            for (int i = 0; i < numUsers; i++) {
                users.add(entrada.readUTF());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    public void flujo(String mens) {
        try {
            vent.mensajeConsola("el mensaje enviado desde el cliente es :"
                    + mens);
            salida.writeInt(1);
            salida.writeUTF(mens);
        } catch (IOException e) {
            vent.mensajeConsola("error...." + e);
        }
    }

    public void flujo(String amigo, String mens) {
        try {
            vent.mensajeConsola("el mensaje enviado desde el cliente es :"
                    + mens);
            salida.writeInt(3);//opcion de mensage a amigo
            salida.writeUTF(amigo);
            salida.writeUTF(mens);
        } catch (IOException e) {
            vent.mensajeConsola("error...." + e);
        }
    }

}
