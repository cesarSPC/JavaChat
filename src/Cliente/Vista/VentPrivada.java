/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente.Vista;

import Cliente.Controlador.Control;
import Cliente.Modelo.ConnCliente;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

/**
 * la clase VentPrivada se encargara de manejar los aspectos GUI del cliente
 * pero en un contexto de chat privado
 *
 * @author Administrador
 */
public class VentPrivada extends JFrame {

    /**
     * Área de texto para mostrar mensajes privados.
     */
    public JTextArea panMostrar;
    /**
     * Campo de texto para ingresar mensajes privados.
     */
    public JTextField txtMensage;
    /**
     * Botón para enviar mensajes privados.
     */
    public JButton butEnviar;
    /**
     * Instancia de la clase Control que maneja la lógica del cliente.
     */
    private Control control;

    /**
     * Constructor de la clase VentPrivada. Inicializa los componentes de la
     * interfaz gráfica y configura el diseño.
     *
     * @param control Instancia de la clase Control que maneja la lógica del
     * cliente.
     */
    public VentPrivada(Control control) {
        super("Amigo");
        this.control = control;
        txtMensage = new JTextField(30);
        butEnviar = new JButton("Enviar");
        panMostrar = new JTextArea();
        panMostrar.setEditable(false);
        txtMensage.requestFocus();

        JPanel panAbajo = new JPanel();
        panAbajo.setLayout(new BorderLayout());
        panAbajo.add(new JLabel("  Ingrese mensage a enviar:"),
                BorderLayout.NORTH);
        panAbajo.add(txtMensage, BorderLayout.CENTER);
        panAbajo.add(butEnviar, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(new JScrollPane(panMostrar), BorderLayout.CENTER);
        add(panAbajo, BorderLayout.SOUTH);

        this.addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent e) {
                cerrarVentana();
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

        });

        setSize(300, 300);
        setLocation(570, 90);
    }

    /**
     * Establece el título de la ventana con el nombre del amigo.
     *
     * @param ami Nombre del amigo.
     */
    public void setAmigo(String ami) {
        this.setTitle(ami);
    }

    /**
     * Cierra la ventana y la oculta.
     */
    private void cerrarVentana() {
        this.setVisible(false);
    }

    /**
     * Muestra un mensaje en el área de texto panMostrar.
     *
     * @param msg Mensaje a mostrar.
     */
    public void mostrarMsg(String msg) {
        this.panMostrar.append(msg + "\n");
    }
    
    /**
    * Muestra la ventana privada y los mensajes correspondientes
    *@param amigo Es el usuario especifico al que se le envia el mensaje
    *@param msg es el mensaje a mostar
    */    
    public void mensageAmigo(String amigo, String msg) {
        setAmigo(amigo);
        mostrarMsg(msg);
        setVisible(true);
    }

}
