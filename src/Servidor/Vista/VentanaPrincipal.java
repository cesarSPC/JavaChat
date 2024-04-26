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
import Servidor.Controlador.ThreadServidor;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public JTextArea txaMostrar;

    public VentanaPrincipal() {
        super("Consola servidor");
        txaMostrar = new JTextArea();

        this.setContentPane(new JScrollPane(txaMostrar));
        setLocationRelativeTo(null);
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void mostrar(String msg) {
        txaMostrar.append(msg + "\n");
    }
    
    public void mensajeConsola(String msj){
        System.out.println(msj);
    }
    
    public String recibirMensaje(){
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        return command;
    }
    
}
