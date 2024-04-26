/*
 * Cliente.java
 *
 * Created on 12 de marzo de 2008, 06:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Cliente.Vista;

import Cliente.Controlador.Control;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;

/**
 *la clase Vent cliente se encargara de manejar los aspectos GUI del cliente
 * @author Administrador
 */
public class VentCliente extends JFrame{
    
    /**
     * Almacena el mensaje ingresado por el cliente.
     */
    private String mensajeCliente;
    /**
     * Área de texto para mostrar mensajes.
     */
    public JTextArea panMostrar;
    /**
     * Campo de texto para ingresar mensajes.
     */
    public JTextField txtMensage;
    /**
     * Botón para enviar mensajes.
     */
    public JButton butEnviar;
    /**
     * Etiqueta para mostrar el nombre de usuario.
     */
    public JLabel lblNomUser;
    /**
     * Lista de usuarios activos.
     */
    public JList lstActivos;
    /**
     * Botón para iniciar una conversación privada.
     */
    public JButton butPrivado;
    /**
     * Botón para salir de la aplicación.
     */
    public JButton butSalir;

    public JMenuBar barraMenu;
    public JMenu JMAyuda;
    public JMenuItem help;
    public JMenu JMAcerca;
    public JMenuItem acercaD;

    private Control control;
    
    /**
     * Constructor de la clase VentCliente.
     * Inicializa los componentes de la interfaz gráfica y configura el diseño.
     *
     * @param control Instancia de la clase Control que maneja la lógica del cliente.
     */
    public VentCliente(Control control) {
        super("Cliente Chat");
        
        this.control = control;
        txtMensage = new JTextField(30);
        butEnviar = new JButton("Enviar");
        butSalir = new JButton("Salir");
        lblNomUser = new JLabel("Usuario <<  >>");
        lblNomUser.setHorizontalAlignment(JLabel.CENTER);
        panMostrar = new JTextArea();
        panMostrar.setColumns(25);
        
        lstActivos = new JList();
        butPrivado = new JButton("Privado");
        

        barraMenu = new JMenuBar();
        JMAyuda = new JMenu("Ayuda");
        help = new JMenuItem("Ayuda");
        help.setActionCommand("help");
        

        JMAcerca = new JMenu("Acerca de");
        acercaD = new JMenuItem("Creditos");
        acercaD.setActionCommand("Acerca");
        

        JMAyuda.add(help);
        JMAcerca.add(acercaD);
        barraMenu.add(JMAcerca);
        barraMenu.add(JMAyuda);

        panMostrar.setEditable(false);
        panMostrar.setForeground(Color.BLUE);
        panMostrar.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(25, 10, 80)));

        JPanel panAbajo = new JPanel();
        panAbajo.setLayout(new BorderLayout());
        panAbajo.add(new JLabel("  Ingrese mensage a enviar:"), BorderLayout.NORTH);
        panAbajo.add(txtMensage, BorderLayout.CENTER);
        panAbajo.add(butEnviar, BorderLayout.EAST);
        
        JPanel panRight = new JPanel();
        panRight.setLayout(new BorderLayout());
        
        
        panRight.add(lblNomUser, BorderLayout.NORTH);
        panRight.add(new JScrollPane(panMostrar), BorderLayout.CENTER);
        panRight.add(panAbajo, BorderLayout.SOUTH);
        JPanel panLeft = new JPanel();
        panLeft.setLayout(new BorderLayout());
        panLeft.add(new JScrollPane(this.lstActivos), BorderLayout.CENTER);
        panLeft.add(this.butPrivado, BorderLayout.NORTH);
        panLeft.add(butSalir, BorderLayout.SOUTH);
        
        JSplitPane sldCentral = new JSplitPane();
        sldCentral.setDividerLocation(100);
        sldCentral.setDividerSize(7);
        sldCentral.setOneTouchExpandable(true);
        sldCentral.setLeftComponent(panLeft);
        sldCentral.setRightComponent(panRight);

        setLayout(new BorderLayout());
        add(sldCentral, BorderLayout.CENTER);
        add(barraMenu, BorderLayout.NORTH);

        txtMensage.requestFocus(); // Pedir el focus

        setSize(450, 430);
        setLocation(120, 90);
        setDefaultCloseOperation(0);
    }

    /**
     * Establece el nombre de usuario en el JLabel lblNomUser.
     *
     * @param user Nombre de usuario a mostrar.
     */
    public void setNombreUser(String user) {
        lblNomUser.setText("Usuario " + user);
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
     * Muestra un mensaje en la consola.
     *
     * @param msj Mensaje a mostrar en la consola.
     */
    public void mensajeConsola(String msj){
        System.out.println(msj);
    }
    /**
     * Muestra un mensaje emergente utilizando JOptionPane.
     *
     * @param msj Mensaje a mostrar en el cuadro de diálogo.
     */
    public void mensajeEmergente(String msj){
        JOptionPane.showMessageDialog(null,msj);
    }
    /**
     * Muestra un cuadro de diálogo de entrada utilizando JOptionPane.
     *
     * @param msj Mensaje a mostrar en el cuadro de diálogo.
     * @return La cadena ingresada por el usuario, o null si se canceló la operación.
     */
    public String inputEmergente(String msj){
        return JOptionPane.showInputDialog(null,msj);
    }
    /**
     * Muestra un cuadro de diálogo de entrada con un valor por defecto utilizando JOptionPane.
     *
     * @param msj  Mensaje a mostrar en el cuadro de diálogo.
     * @param msj2 Valor por defecto para el campo de entrada.
     * @return La cadena ingresada por el usuario, o null si se canceló la operación.
     */
    public String inputEmergente(String msj, String msj2){
        return JOptionPane.showInputDialog(msj, msj2);
    }

    
}
