/*
 * threadServidor.java
 *
 * Created on 23 de marzo de 2008, 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Servidor.Controlador;

import Cliente.Modelo.ArchivoPropiedades;
import Servidor.Vista.Servidor;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Administrador
 */
public class threadServidor extends Thread {

    private Socket scli = null;
    private Socket scli2 = null;
    private int banStrikes = 3; //intentos faltantes para ban
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private DataOutputStream salida2 = null;
    public static Vector<threadServidor> clientesActivos = new Vector();
    private String nameUser;
    private Servidor serv;
    ArchivoPropiedades properties = null;


    public threadServidor(Socket scliente, Socket scliente2, Servidor serv, ArchivoPropiedades properties) {
        this.properties = properties;
        scli = scliente;
        scli2 = scliente2;
        this.serv = serv;
        nameUser = "";
        clientesActivos.add(this);
        serv.mostrar("cliente agregado: " + this);
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }

    public void run() {
        serv.mostrar(".::Esperando Mensajes :");

        try {
            entrada = new DataInputStream(scli.getInputStream());
            salida = new DataOutputStream(scli.getOutputStream());
            salida2 = new DataOutputStream(scli2.getOutputStream());
            this.setNameUser(entrada.readUTF());
            enviaUserActivos();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int opcion = 0, numUsers = 0;
        String amigo = "", mencli = "";

        while (this.banStrikes > 0) {
            try {
                opcion = entrada.readInt();
                switch (opcion) {
                    case 1://envio de mensage a todos
                        mencli = comprobarBaneo(entrada.readUTF());
                        serv.mostrar("mensaje recibido " + mencli);
                        enviaMsg(mencli);
                        break;
                    case 2://envio de lista de activos
                        numUsers = clientesActivos.size();
                        salida.writeInt(numUsers);
                        for (int i = 0; i < numUsers; i++) {
                            salida.writeUTF(clientesActivos.get(i).nameUser);
                        }
                        break;
                    case 3: // envia mensage a uno solo
                        amigo = entrada.readUTF();//captura nombre de amigo
                        mencli = entrada.readUTF();//mensage enviado
                        enviaMsg(amigo, mencli);
                        break;
                }
            } catch (IOException e) {
                System.out.println("El cliente termino la conexion");
                break;
            }
        }
        
        if(this.banStrikes <= 0){
            serv.mostrar(this.nameUser + " ha sido baneado");
            try{
                this.salida2.writeInt(4);
            }
            catch(IOException ex){
                
            }
            
        }
        
        serv.mostrar("Se removio un usuario");
        clientesActivos.removeElement(this);
        try {
            serv.mostrar("Se desconecto un usuario");
            scli.close();
        } catch (Exception et) {
            serv.mostrar("no se puede cerrar el socket");
        }
    }

    public void enviaMsg(String mencli2) {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            serv.mostrar("MENSAJE DEVUELTO:" + mencli2);
            try {
                user = clientesActivos.get(i);
                user.salida2.writeInt(1);//opcion de mensage 
                user.salida2.writeUTF("" + this.getNameUser() + " >" + mencli2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviaUserActivos() {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            try {
                user = clientesActivos.get(i);
                if (user == this) {
                    continue;//ya se lo envie
                }
                user.salida2.writeInt(2);//opcion de agregar 
                user.salida2.writeUTF(this.getNameUser());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviaMsg(String amigo, String mencli) {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            try {
                user = clientesActivos.get(i);
                if (user.nameUser.equals(amigo)) {
                    user.salida2.writeInt(3);//opcion de mensage amigo   
                    user.salida2.writeUTF(this.getNameUser());
                    user.salida2.writeUTF("" + this.getNameUser() + ">" + mencli);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private String comprobarBaneo(String msg){
        String rta = msg; 
        for(String i : msg.toLowerCase().split(" ")){
            if(properties.getData(i) != null){
                this.banStrikes -= Integer.parseInt(properties.getData(i));
                rta = "*********";
                break;
            }
        }    
       
        
        
        return rta;
        
    }


}
