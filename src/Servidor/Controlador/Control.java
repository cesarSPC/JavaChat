/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor.Controlador;

import Servidor.Modelo.ArchivoPropiedades;
import Servidor.Modelo.ConnServerSocket;
import Servidor.Modelo.ConnSocket;
import Servidor.Vista.FileChooser;
import Servidor.Vista.VentanaPrincipal;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author cesar
 */
public class Control {

    private VentanaPrincipal vista;
    public ConnServerSocket servidor;
    public ArchivoPropiedades puertos;
    public ArchivoPropiedades listaNegra;
    public ArrayList<ThreadServidor> clientesActivos;
    private ThreadComando manejoComando;

    /**
     * Metodo contructor del controler 
    */
    public Control() {
        vista = new VentanaPrincipal();
        servidor = new ConnServerSocket();
        puertos = new ArchivoPropiedades(new FileChooser("Seleccione puertos").getFile());
        listaNegra = new ArchivoPropiedades(new FileChooser("Seleccione lista negra de palabras").getFile());
        vista.setVisible(true);
        clientesActivos = new ArrayList<ThreadServidor>();

        loopServidor();
        
        
    }

    
    /**
     * Metodo loop servidor, crea el servidor y queda en un bucle esperando nuevos usarios.
     * Cuando llega un usuario habre un nuevo hilo con su sockect propio para su comunicación
     */
    private void loopServidor() {
        manejoComando = new ThreadComando(this);
        manejoComando.start();
        try {

            int puerto1 = Integer.parseInt(puertos.getData("Puerto.1"));
            int puerto2 = Integer.parseInt(puertos.getData("Puerto.2"));
            servidor.runServer(puerto1, puerto2);
            vista.mostrar(".::Servidor activo :");
            while (true) {
                
                ConnSocket tempCon = new ConnSocket();
                
                vista.mostrar("Esperando Usuarios");
                tempCon.aceptarClientes(servidor.getServ(),servidor.getServ2());
                ThreadServidor user;
                
                // Se le mandan los datos del cliente que se conectó, al thread
                user = new ThreadServidor(this, tempCon);
                user.start();
                clientesActivos.add(user);
            }
        } catch (NumberFormatException ex) {
            vista.mostrar("No se pudo arrancar servidor. Revisar puertos ingresados.");
        } catch (IOException ex) {
            vista.mensajeConsola("Accept failed: " + vista + ", " + ex.getMessage());
            vista.mostrar("Fallo al conectar");
        }

    }

    

    public VentanaPrincipal getVista() {
        return vista;
    }

    public ConnServerSocket getServidor() {
        return servidor;
    }

    public ArchivoPropiedades getListaNegra() {
        return listaNegra;
    }

    public ArrayList<ThreadServidor> getClientesActivos() {
        return clientesActivos;
    }
}
