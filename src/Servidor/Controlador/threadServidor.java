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
import Servidor.Modelo.Conexion;
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
    private final Conexion conexCliente;

    public ThreadServidor(Control control, Conexion conexCliente) {
        this.control = control;
        this.nameUser = "";
        
        // Se hace la asignación solo una vez, pues es cuando en conexion estan los sockets de este cliente
        this.conexCliente = conexCliente;
        
        this.control.getVista().mostrar("Cliente agregado: " + this);
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }

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
        control.manejoDeAcciones(this);
        
        control.getVista().mostrar(this.nameUser + " ha sido baneado");
        try {
            conexCliente.getSalida2().writeInt(4);
        } catch (IOException ex) {

        }

        control.getVista().mostrar("Se removio un usuario");
        control.getClientesActivos().remove(this);
        try {
            control.getVista().mostrar("Se desconecto un usuario");
            conexCliente.getSock().close();
            conexCliente.getSock2().close();
        } catch (Exception et) {
            control.getVista().mostrar("no se puede cerrar el socket");
        }
    }

    public void enviaMsg(String mencli2) {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            control.getVista().mostrar("MENSAJE DEVUELTO:" + mencli2);
            try {
                user = control.getClientesActivos().get(i);
                user.conexCliente.getSalida2().writeInt(1);//opcion de mensage 
                user.conexCliente.getSalida2().writeUTF("" + this.getNameUser() + " >" + mencli2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviaUserActivos() {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            try {
                user = control.getClientesActivos().get(i);
                if (user == this) {
                    continue;//ya se lo envie
                }
                user.conexCliente.getSalida2().writeInt(2);//opcion de agregar 
                user.conexCliente.getSalida2().writeUTF(this.getNameUser());
            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
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

    public Conexion getConexCliente() {
        return conexCliente;
    }
    
    
}
