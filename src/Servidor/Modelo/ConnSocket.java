/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor.Modelo;

import Servidor.Controlador.Control;
import Servidor.Vista.VentanaPrincipal;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cesar
 */
public class ConnSocket implements Cloneable{

    private Socket sock = null;
    private Socket sock2 = null;
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private DataOutputStream salida2 = null;

    public ConnSocket() {
    }
    
    
    /**
     * Esta funcion crea la conexion unica entre el servidor y el cliente.
     * Acepta al cliente y seguidamente crea sus stream tanto de entrada como de salida
     * @param serv recibe sockect servidor entrada 
     * @param serv2 recibe sockecte servido salida
     * @throws IOException 
     */
    public void aceptarClientes(ServerSocket serv, ServerSocket serv2) throws IOException{
        sock = serv.accept();
        sock2 = serv2.accept();
        
        entrada = new DataInputStream(sock.getInputStream());
        salida = new DataOutputStream(sock.getOutputStream());
        salida2 = new DataOutputStream(sock2.getOutputStream());
    }
    
    public void cerrar(){
        try {
            sock.close();
            sock2.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSock() {
        return sock;
    }

    public Socket getSock2() {
        return sock2;
    }
    
    public DataInputStream getEntrada() {
        return entrada;
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    public DataOutputStream getSalida2() {
        return salida2;
    }
    
    

}
