/*
 * Cliente.java
 *
 * Created on 12 de marzo de 2008, 06:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Cliente.Vista;

import Cliente.Modelo.ConnCliente;
import Cliente.Controlador.Control;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.JOptionPane.*;

/**
 *
 * @author Administrador
 */
public class VentCliente extends JFrame{

    private String mensajeCliente;
    public JTextArea panMostrar;
    public JTextField txtMensage;
    public JButton butEnviar;
    public JLabel lblNomUser;
    public JList lstActivos;
    public JButton butPrivado;
    public JButton butSalir;

    public JMenuBar barraMenu;
    public JMenu JMAyuda;
    public JMenuItem help;
    public JMenu JMAcerca;
    public JMenuItem acercaD;
    public VentanaAyuda va;

    private JOptionPane AcercaDe;

    private Control control;
    

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

    public void setNombreUser(String user) {
        lblNomUser.setText("Usuario " + user);
    }

    public void mostrarMsg(String msg) {
        this.panMostrar.append(msg + "\n");
    }
    
    public void mensajeConsola(String msj){
        System.out.println(msj);
    }
    
    public void mensajeEmergente(String msj){
        JOptionPane.showMessageDialog(null,msj);
    }
    
    public String inputEmergente(String msj){
        return JOptionPane.showInputDialog(null,msj);
    }
    
    public String inputEmergente(String msj, String msj2){
        return JOptionPane.showInputDialog(msj, msj2);
    }

    
}
