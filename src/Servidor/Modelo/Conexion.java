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

/**
 *
 * @author cesar
 */
public class Conexion {

    private Socket sock = null;
    private Socket sock2 = null;
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private DataOutputStream salida2 = null;
    private ServerSocket serv = null; // Para comunicacion
    private ServerSocket serv2 = null; // Para enviar mensajes

    public Conexion() {
    }

    public void runServer(int puerto1, int puerto2) throws IOException {

        serv = new ServerSocket(puerto1);
        serv2 = new ServerSocket(puerto2);
        
    }
    
    public void aceptarClientes() throws IOException{
        sock = serv.accept();
        sock2 = serv2.accept();
        
        entrada = new DataInputStream(sock.getInputStream());
        salida = new DataOutputStream(sock.getOutputStream());
        salida2 = new DataOutputStream(sock2.getOutputStream());
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
