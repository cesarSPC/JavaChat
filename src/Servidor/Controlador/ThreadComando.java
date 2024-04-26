/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor.Controlador;

/**
 *
 * @author cesar
 */
public class ThreadComando extends Thread {

    private Control control;

    public ThreadComando(Control control) {
        this.control = control;

    }

    @Override
    public void run() {
        while (true) {
            control.getVista().mensajeConsola("Esperando comando // Disponible(s): \"ban [nombreUsuario]\"");

            String mensaje = control.getVista().recibirMensaje();

            control.getVista().mensajeConsola("Comando recibido: " + mensaje);

            String[] comando = mensaje.split(" ");

            if (!control.getClientesActivos().isEmpty() && comando[0].equals("ban") && comando.length < 3 && comando.length > 1) {
                for (ThreadServidor t : control.getClientesActivos()) {
                    
                    if (t.getNameUser().equals(comando[1])) {
                        t.getConexCliente().cerrar();
                        control.getVista().mensajeConsola(t.getNameUser() + " baneado");
                        break;
                    }
                    
                }
                control.getVista().mensajeConsola("No se encontró el usuario");
            }
        }
    }

}
