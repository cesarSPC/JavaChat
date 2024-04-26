/*
 * threadServidor.java
 *
 * Created on 23 de marzo de 2008, 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Servidor.Controlador;

import Servidor.Modelo.ArchivoPropiedades;
import Servidor.Modelo.ConnServerSocket;
import Servidor.Modelo.ConnSocket;
import Servidor.Vista.VentanaPrincipal;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Administrador
 */
public class ThreadServidor extends Thread {

    private int banStrikes = 3; //intentos faltantes para ban
    private String nameUser;
    private Control control;
    private final ConnSocket conexCliente;

    public ThreadServidor(Control control, ConnSocket conexCliente) {
        this.control = control;
        this.nameUser = "";
        
        // Se hace la asignación solo una vez, pues es cuando en conexion estan los sockets de este cliente
        this.conexCliente = conexCliente;
        
        this.control.getVista().mostrar("Cliente agregado: " + this);
    }

    @Override
    public void run() {
        control.getVista().mostrar(".::Esperando Mensajes :");
        
        try {
            nameUser = conexCliente.getEntrada().readUTF();
            enviaUserActivos();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int opcion = 0, numUsers = 0;
        String amigo = "", mencli = "";

        //Control
        manejoDeAcciones();
        
        control.getClientesActivos().remove(this);
        enviaUserActivos();
        control.getVista().mostrar(this.nameUser + " ha sido baneado");
        
        try {
            conexCliente.cerrar();
            control.getVista().mostrar("Se desconecto un usuario");
        } catch (Exception et) {
            control.getVista().mostrar("no se puede cerrar el socket");
        }
    }
    
    public void manejoDeAcciones() {
        while (banStrikes > 0) {
            try {
                int opcion = conexCliente.getEntrada().readInt();
                String mensajeCliente, nombreAmigo;
                int numUsers;
                switch (opcion) {
                    case 1:// Envio de mensaje a todos
                        mensajeCliente = comprobarBaneo(conexCliente.getEntrada().readUTF());
                        control.getVista().mostrar("mensaje recibido " + mensajeCliente);
                        enviaMsg(mensajeCliente);
                        break;
                    case 2:// Envio de lista de activos
                        numUsers = control.getClientesActivos().size();
                        conexCliente.getSalida().writeInt(numUsers);
                        for (int i = 0; i < numUsers; i++) {
                            conexCliente.getSalida().writeUTF(control.getClientesActivos().get(i).getNameUser());
                        }
                        break;
                    case 3: // Envia mensage a uno solo
                        nombreAmigo = conexCliente.getEntrada().readUTF(); // Captura nombre de amigo
                        mensajeCliente = conexCliente.getEntrada().readUTF(); // Mensage enviado
                        enviaMsg(nombreAmigo, mensajeCliente);
                        break;
                }
            } catch (IOException e) {
                
                control.getVista().mensajeConsola("El cliente termino la conexion");
                break;
            }
        }
    }
    
    public void enviaMsg(String mencli2) {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            control.getVista().mostrar("MENSAJE DEVUELTO:" + mencli2);
            try {
                user = control.getClientesActivos().get(i);
                user.conexCliente.getSalida2().writeInt(1); // Opcion de mensaje 
                user.conexCliente.getSalida2().writeUTF("" + this.getNameUser() + " >" + mencli2);
            } catch (IOException e) {
                control.getVista().mensajeConsola("Error ->" + e.getMessage());
            }
        }
    }

    public void enviaUserActivos() {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            try {
                user = control.getClientesActivos().get(i);
                if (user == this) {
                    continue; // ya se lo envie
                }
                user.conexCliente.getSalida2().writeInt(2);//opcion de agregar 
                user.conexCliente.getSalida2().writeUTF(this.getNameUser());
            } catch (IOException e) {
                control.getVista().mensajeConsola("Error ->" + e.getMessage());
            }
        }
    }

    public void enviaMsg(String amigo, String mencli) {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            try {
                user = control.getClientesActivos().get(i);
                if (user.nameUser.equals(amigo)) {
                    user.conexCliente.getSalida2().writeInt(3);//opcion de mensage amigo   
                    user.conexCliente.getSalida2().writeUTF(this.getNameUser());
                    user.conexCliente.getSalida2().writeUTF("" + this.getNameUser() + ">" + mencli);
                }
            } catch (IOException e) {
                control.getVista().mensajeConsola("Error ->" + e.getMessage());
            }
        }
    }

    public String comprobarBaneo(String msg) {
        String rta = msg;
        for (String i : msg.toLowerCase().split(" ")) {
            if (control.getListaNegra().getData(i) != null) {
                this.banStrikes -= Integer.parseInt(control.getListaNegra().getData(i));
                rta = "*********";
                break;
            }
        }

        return rta;

    }

    public int getBanStrikes() {
        return banStrikes;
    }

    public ConnSocket getConexCliente() {
        return conexCliente;
    }
    
    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }

    public void setBanStrikes(int banStrikes) {
        this.banStrikes = banStrikes;
    }
    
}
