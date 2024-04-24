/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor.Controlador;

import Servidor.Modelo.ArchivoPropiedades;
import Servidor.Modelo.Conexion;
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
    public Conexion servidor;
    public ArchivoPropiedades puertos;
    public ArchivoPropiedades listaNegra;
    public ArrayList<ThreadServidor> clientesActivos;

    public Control() {
        vista = new VentanaPrincipal();
        servidor = new Conexion();
        puertos = new ArchivoPropiedades(new FileChooser("Seleccione puertos").getFile());
        listaNegra = new ArchivoPropiedades(new FileChooser("Elija lista negra").getFile());
        clientesActivos = new ArrayList<ThreadServidor>();

        loopServidor();
    }

    private void loopServidor() {
        try {

            int puerto1 = Integer.parseInt(puertos.getData("Puerto.1"));
            int puerto2 = Integer.parseInt(puertos.getData("Puerto.2"));
            servidor.runServer(puerto1, puerto2);
            vista.mostrar(".::Servidor activo :");
            while (true) {
                
                vista.mostrar("Esperando Usuarios");
                servidor.aceptarClientes();
                ThreadServidor user;
                
                // Se le mandan los datos del cliente que se conectó, al thread
                user = new ThreadServidor(this, servidor);
                user.start();
                clientesActivos.add(user);
            }
        } catch (NumberFormatException ex) {
            vista.mostrar("No se pudo arrancar servidor. Revisar puertos ingresados.");
        } catch (IOException ex) {
            vista.mostrar("Accept failed: " + vista + ", " + ex.getMessage());
        }

    }

    public void manejoDeAcciones(ThreadServidor t) {
        while (t.getBanStrikes() > 0) {
            try {
                int opcion = t.getConexCliente().getEntrada().readInt();
                String mensajeCliente, nombreAmigo;
                int numUsers;
                switch (opcion) {
                    case 1://envio de mensage a todos
                        mensajeCliente = t.comprobarBaneo(t.getConexCliente().getEntrada().readUTF());
                        vista.mostrar("mensaje recibido " + mensajeCliente);
                        t.enviaMsg(mensajeCliente);
                        break;
                    case 2://envio de lista de activos
                        numUsers = clientesActivos.size();
                        t.getConexCliente().getSalida().writeInt(numUsers);
                        for (int i = 0; i < numUsers; i++) {
                            t.getConexCliente().getSalida().writeUTF(clientesActivos.get(i).getNameUser());
                        }
                        break;
                    case 3: // envia mensage a uno solo
                        nombreAmigo = t.getConexCliente().getEntrada().readUTF();//captura nombre de amigo
                        mensajeCliente = t.getConexCliente().getEntrada().readUTF();//mensage enviado
                        t.enviaMsg(nombreAmigo, mensajeCliente);
                        break;
                }
            } catch (IOException e) {
                System.out.println("El cliente termino la conexion");
                break;
            }
        }
    }

    public VentanaPrincipal getVista() {
        return vista;
    }

    public Conexion getServidor() {
        return servidor;
    }

    public ArchivoPropiedades getListaNegra() {
        return listaNegra;
    }

    public ArrayList<ThreadServidor> getClientesActivos() {
        return clientesActivos;
    }
}
