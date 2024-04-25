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
public class ConnServerSocket{

    private ServerSocket serv = null; // Para comunicacion
    private ServerSocket serv2 = null; // Para enviar mensajes

    public ConnServerSocket() {
    }

    public void runServer(int puerto1, int puerto2) throws IOException {

        serv = new ServerSocket(puerto1);
        serv2 = new ServerSocket(puerto2);
        
    }

    public ServerSocket getServ() {
        return serv;
    }

    public ServerSocket getServ2() {
        return serv2;
    }
    
    
    
}
