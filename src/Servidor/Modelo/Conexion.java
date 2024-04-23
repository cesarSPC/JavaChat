
import Servidor.Controlador.threadServidor;
import Servidor.Vista.Servidor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {

    private Servidor servidor;

    public void runServer() {
        ServerSocket serv = null;//para comunicacion
        ServerSocket serv2 = null;//para enviar mensajes
        boolean listening = true;
        try {
            serv = new ServerSocket(8081);
            serv2 = new ServerSocket(8082);
            servidor.mostrar(".::Servidor activo :");
            while (listening) {
                Socket sock = null, sock2 = null;
                try {
                    servidor.mostrar("Esperando Usuarios");
                    sock = serv.accept();
                    sock2 = serv2.accept();
                } catch (IOException e) {
                    servidor.mostrar("Accept failed: " + serv + ", " + e.getMessage());
                    continue;
                }
                /*
                threadServidor user = new threadServidor(sock, sock2, this, listaNegra);
                user.start();
                 */
            }

        } catch (IOException e) {
            servidor.mostrar("error :" + e);
        }
    }
}
