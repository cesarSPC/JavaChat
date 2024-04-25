package Cliente.Controlador;

import Cliente.Vista.VentCliente;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class threadCliente extends Thread {

    private DataInputStream entrada;
    private Control control;

    public threadCliente(DataInputStream entrada, Control control){
        this.entrada = entrada;
        this.control = control;
    }

    @Override
    public void run() {

        String mensajeServ = "", nombreAmigo = "";
        int opcion = 0;
        while (true) {
            try {
                opcion = entrada.readInt();
                switch (opcion) {
                    case 1://mensage enviado
                        mensajeServ = entrada.readUTF();
                        control.getVista().mostrarMsg(mensajeServ);
                        break;
                    case 2://se agrega o se sale un usuario
                        mensajeServ = entrada.readUTF();
                        control.agregarUser(mensajeServ);
                        break;
                    case 3://mensage de amigo
                        nombreAmigo = entrada.readUTF();
                        mensajeServ = entrada.readUTF();
                        control.mensageAmigo(nombreAmigo, mensajeServ);
                        control.setAmigoActual(nombreAmigo);
                        
                        break;
                }
            } catch (IOException e) {
                control.getVista().mensajeConsola("Se corto la comunicacion");
                break;
            }
        }
        
        System.exit(0);
        
    }

    public DataInputStream getEntrada() {
        return entrada;
    }
    
}
