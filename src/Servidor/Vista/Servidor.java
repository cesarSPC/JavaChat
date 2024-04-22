/*
 * Server.java
 *
 * Created on 21 de marzo de 2008, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Servidor.Vista;

/**
 *
 * @author Administrador
 */
import Cliente.Modelo.ArchivoPropiedades;
import Cliente.Vista.FileChooser;
import Servidor.Controlador.threadServidor;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Servidor extends JFrame {

    public JTextArea txaMostrar;
    private ArchivoPropiedades listaNegra;

    public Servidor() {
        super("Consola servidor");
        txaMostrar = new JTextArea();
        listaNegra = new ArchivoPropiedades(new FileChooser("Eliga lista negra").getFile());

        this.setContentPane(new JScrollPane(txaMostrar));
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void mostrar(String msg) {
        txaMostrar.append(msg + "\n");
    }

    public void runServer() {
        ServerSocket serv = null;//para comunicacion
        ServerSocket serv2 = null;//para enviar mensajes
        boolean listening = true;
        try {
            serv = new ServerSocket(8081);
            serv2 = new ServerSocket(8082);
            mostrar(".::Servidor activo :");
            while (listening) {
                Socket sock = null, sock2 = null;
                try {
                    mostrar("Esperando Usuarios");
                    sock = serv.accept();
                    sock2 = serv2.accept();
                } catch (IOException e) {
                    mostrar("Accept failed: " + serv + ", " + e.getMessage());
                    continue;
                }
                threadServidor user = new threadServidor(sock, sock2, this, listaNegra);
                user.start();
            }

        } catch (IOException e) {
            mostrar("error :" + e);
        }
    }

    public static void main(String abc[]) throws IOException {
        Servidor ser = new Servidor();
        ser.runServer();
    }
}
