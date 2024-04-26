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

    /**
     * @param control injeccion de dependencia con el controlador
     * @param conexCliente Conexion especifica con cada cliente conectado
     */
    public ThreadServidor(Control control, ConnSocket conexCliente) {
        this.control = control;
        this.nameUser = "";
        
        // Se hace la asignación solo una vez, pues es cuando en conexion estan los sockets de este cliente
        this.conexCliente = conexCliente;
        
        this.control.getVista().mostrar("Cliente agregado: " + this);
    }


    /**
     * Metodo concurrente de mi clase. 
     * Este metodo se encarga de iniciar la conexion del cliente con el servidor.
     * Luego llama a una funcion que manejara el envio de datos del usuario al servidor
     * Cuando termina la ciclo de while es porque el usuario ha sido baniado.
     * Entonces maneja la logica nescesaria
   */
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
    
    
    /**
     * Este metodo se encarga del manejo de instrucciones enviadas entre el servidor y el cliente
     * Primero y dentreo del while, lee varias veces la entrada de números desde el cliente 
     * Evalua el caso y ejecuta la instruccion dependiendo lo que haya pedido el cliente
     * Cuando se pierde la conexion de cualquier forma el catch salta y mustra que la conexion con el cliente termino
     */
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
    
    
    /**
     * Este metodo se encarga de enviar un mensaje por el chat grupal a todos los usuarios activos
     * @param mencli2 es el string para un mensaje de chat grupal
     */
    
    public void enviaMsg(String mencli2) {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            control.getVista().mostrar("MENSAJE DEVUELTO:" + mencli2);
            try {
                user = control.getClientesActivos().get(i);
                user.conexCliente.getSalida2().writeInt(1); // Opcion de mensaje 
                
                //escribe la salida del sockect con el cliente para enviar mensaje
                user.conexCliente.getSalida2().writeUTF("" + this.getNameUser() + " >" + mencli2); 
                
            } catch (IOException e) {
                control.getVista().mensajeConsola("Error ->" + e.getMessage());
            }
        }
    }

    
    
    /**
     * Este metodo envia la lista de usuarios activos al cliente para que actualice su vista y parametros
     */
    public void enviaUserActivos() {
        ThreadServidor user = null;
        for (int i = 0; i < control.getClientesActivos().size(); i++) {
            try {
                user = control.getClientesActivos().get(i);
                if (user == this) {
                    continue; // esta parte permite que el cliente no se encuentre a su mismo en la lista de usuarios
                }
                user.conexCliente.getSalida2().writeInt(2);//opcion de agregar 
                user.conexCliente.getSalida2().writeUTF(this.getNameUser());
            } catch (IOException e) {
                control.getVista().mensajeConsola("Error ->" + e.getMessage());
            }
        }
    }

   
    
    /**
     * Este metodo lo que hace es buscar al usuario especifico por nombre para mandar un mensaje
     * Una vez lo encuentra le manda el mensaje por su sockect y le escribe el remitente - this.getNameUser -
     * @param amigo nombre del usuario a quien mandar mensaje
     * @param mencli string con el mensaje para usuario
     */
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

    
    /**
     * Esta funcion es la implementacion de baneo con la lista negra.
     * Recorre el mensaje palabra por palabra y si una palabra equivale a una de la lista negra, 
     * aplica la penitencia de puntos de ban.
     * En caso de detectar una palabra que esta en la lista negra, la funcion retorna un string diferente al mensaje original
     * @param msg mensaje a evaluar para determinar baneo
     * @return devuelve censurado el mensjae en caso de que una palabra este en la lista negra
     */
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
