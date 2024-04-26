/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente.Controlador;

import Cliente.Modelo.ArchivoPropiedades;
import Cliente.Modelo.ConnCliente;
import Cliente.Vista.FileChooser;
import Cliente.Vista.VentCliente;
import Cliente.Vista.VentPrivada;
import Cliente.Vista.VentanaAyuda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author cesar Clase Control, se encarga de manejar la comunicacion entre el
 * modelo y la vista del cliente y realizar procesos que permitean su correcta
 * operacion
 *
 */
public class Control implements ActionListener {

    /**
    * Comunicacion con la clase VentCliente de la vista
     */
    private VentCliente vista;
    /**
    * Comunicacion con la clase VentanaAyuda de la vista
     */
    private VentanaAyuda va;
    /**
    * Comunicacion con la clase ConnCliente del modelo
     */
    private ConnCliente cliente;
    /**
    * Comunicacion con la clase ArchivoPropiedades que permitira llamar los datos de ip y puerto
    * de manera que no rompemos el principio abierto cerrado
     */
    private ArchivoPropiedades puertos;
    /**
    * Comunicacion con la clase VentPrivada de la vista
     */
    private VentPrivada ventPrivada;
    /**
    * Guarda el amigoActual como String (Es la comunicacion privada que se esta ejecutando)
     */
    private String amigoActual;
    /**
    * Atributo diseñado para guardar en un arraylist todos los usuarios
     */
    private ArrayList<String> nomUsers;
    /**
    * Objeto hilo cliente guardado con el alias de t
     */
    private threadCliente t;

    /**
    * Controlador que se encarga de instanciar las clases principales del sistema, para su funcionamiento
    * inicializa la vista e hilos correspondientes para la ejecucion del programa
     */
    public Control() {
        amigoActual = "";
        nomUsers = new ArrayList<String>();

        vista = new VentCliente(this);

        ConnCliente.IP_SERVER = vista.inputEmergente("Introducir IP_SERVER :", "localhost");
        puertos = new ArchivoPropiedades(new FileChooser("Seleccione puertos para el cliente").getFile());
        iniciarCliente();

        t = new threadCliente(cliente.getEntrada2(), this);
        t.start();

        vista.setVisible(true);

        vista.txtMensage.addActionListener(this);
        vista.butEnviar.addActionListener(this);
        vista.butPrivado.addActionListener(this);
        vista.help.addActionListener(this);
        vista.acercaD.addActionListener(this);
        vista.butSalir.addActionListener(this);

        ponerDatosList();

        ventPrivada = new VentPrivada(this);
        ventPrivada.butEnviar.addActionListener(this);
    }

    /**
    * Inicializa el cliente establciendo los puertos de comunicacion (enviar y comunicar)
    * obtiene los puertos del archivo properties
     */
    private void iniciarCliente() {
        try {
            int puerto1 = Integer.parseInt(puertos.getData("Puerto.1"));
            int puerto2 = Integer.parseInt(puertos.getData("Puerto.2"));

            cliente = new ConnCliente(this);
            cliente.conexion(puerto1, puerto2);
        } catch (IOException ex) {
            vista.mensajeEmergente("No se pudo conectar al servidor");
            vista.mensajeConsola(ex.getMessage() + " <----");
            System.exit(0);
        } catch (NumberFormatException ex) {
            vista.mensajeEmergente("Los puertos especificados no son validos");
            vista.mensajeConsola(ex.getMessage() + " <----");
            System.exit(0);
        }

    }
    
    /**
    * Maneja los eventos de la vista
    *@param evt evento
    */
    @Override
    public void actionPerformed(ActionEvent evt) {

        String comand = (String) evt.getActionCommand();
        if (comand.compareTo("help") == 0) {

            va  = new VentanaAyuda();
            va.setVisible(true);

        }

        if (comand.compareTo("Acerca") == 0) {

            vista.mensajeEmergente("Desarrollado por: Jos� Valdez/Javier Vargas");

        }
        if (evt.getSource() == vista.butEnviar || evt.getSource() == vista.txtMensage) {

            String mensaje = vista.txtMensage.getText();
            Control.this.mensajeEnviado(mensaje);
            vista.txtMensage.setText("");
        } else if (evt.getSource() == vista.butPrivado) {

            int pos = vista.lstActivos.getSelectedIndex();
            if (pos >= 0) {
                amigoActual = nomUsers.get(pos);
                ventPrivada.setAmigo(amigoActual);
                ventPrivada.setVisible(true);
            }
        } else if (evt.getSource() == ventPrivada.butEnviar) {
            String mensaje = ventPrivada.txtMensage.getText();
            ventPrivada.mostrarMsg(getCliente().getNombre() + ">" + mensaje);
            mensajeEnviado(amigoActual, mensaje);
            ventPrivada.txtMensage.setText("");
        } else if (evt.getSource() == vista.butSalir) {
            cliente.cerrar();
        }
    }

    /**
    * Actualiza una lista de nombres de usuarios
    */
    public void ponerDatosList() {
        ArrayList<String> datos = nomUsers;
        try {

            nomUsers.clear();
            cliente.getSalida().writeInt(2);
            int numUsers = cliente.getEntrada().readInt();
            for (int i = 0; i < numUsers; i++) {
                nomUsers.add(cliente.getEntrada().readUTF());
            }
        } catch (IOException ex) {
            vista.mensajeConsola("Hubo un error al actualizar");
            nomUsers = datos;
        }

        vista.lstActivos.setModel(new AbstractListModel() {
            @Override
            public int getSize() {
                return nomUsers.size();
            }

            @Override
            public Object getElementAt(int i) {
                return nomUsers.get(i);
            }
        });
    }

    /**
    * Recibe un mensaje del cliente y lo envia al servidor
    *@param mens recibe un mensaje Sring
    */
    public void mensajeEnviado(String mens) {
        try {
            vista.mensajeConsola("el mensaje enviado desde el cliente es :"
                    + mens);
            cliente.getSalida().writeInt(1);
            cliente.getSalida().writeUTF(mens);
        } catch (IOException e) {
            vista.mensajeConsola("error...." + e);
        }
    }
    
    /**
    * Permite enviar un mensaje privado a un amigo
    *@param amigo Es el usuario especifico al que se le envia el mensaje
    *@param mens es el mensaje a enviar
    */
    public void mensajeEnviado(String amigo, String mens) {
        try {
            vista.mensajeConsola("el mensaje enviado desde el cliente es :"
                    + mens);
            cliente.getSalida().writeInt(3);//opcion de mensage a amigo
            cliente.getSalida().writeUTF(amigo);
            cliente.getSalida().writeUTF(mens);
        } catch (IOException e) {
            vista.mensajeConsola("error...." + e);
        }
    }

    /**
    * Agrega los nuevos usuarios a una lista
    *@param recibe el usuario
    */
    public void agregarUser(String user) {
        nomUsers.add(user);
        ponerDatosList();
    }

    /**
    * Retira al usuario de la lista de usuarios
    *@param recibe el usuario
    */
    public void retirarUser(String user) {
        nomUsers.remove(user);
        ponerDatosList();
    }

    
    
    /**
    * Obtiene la vista del cliente
    *@return retorna la vista
    */
    public VentCliente getVista() {
        return vista;
    }
    /**
    *Obtienen la conexión con el cliente
    *@return retorna el cliente
    */
    public ConnCliente getCliente() {
        return cliente;
    }

    /**
    *Obtienen la ventana privada
    *@return retorna ventana privada
    */
    public VentPrivada getVentPrivada() {
        return ventPrivada;
    }
    
    /**
    * Cambia el amigo actual con el que se esta comunicando
     * @param amigoActual Es el valor a asignar a amigoActual
    */
    public void setAmigoActual(String amigoActual) {
        this.amigoActual = amigoActual;
    }

}
