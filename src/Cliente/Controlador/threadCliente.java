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
    private VentCliente vcli;

    public threadCliente(DataInputStream entrada, VentCliente vcli) throws IOException {
        this.entrada = entrada;
        this.vcli = vcli;
    }

    public void run() {
        String menser = "", amigo = "";
        int opcion = 0;
        while (true) {
            try {
                opcion = entrada.readInt();
                switch (opcion) {
                    case 1://mensage enviado
                        menser = entrada.readUTF();
                        vcli.mostrarMsg("ECO del servidor:" + menser);
                        vcli.mostrarMsg(menser);
                        break;
                    case 2://se agrega
                        menser = entrada.readUTF();
                        vcli.agregarUser(menser);
                        break;
                    case 3://mensage de amigo
                        amigo = entrada.readUTF();
                        menser = entrada.readUTF();
                        vcli.mensageAmigo(amigo, menser);
                        vcli.mostrarMsg("ECO del servidor:" + menser);
                        break;
                }
            } catch (IOException e) {
                vcli.mostrarMsg("Error en la comunicaci�n " + "Informaci�n para el usuario");
                break;
            }
        }
        vcli.mostrarMsg("se desconecto el servidor");
    }

}
