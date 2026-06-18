package vista;

import controlador.UsuarioControlador;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
    private UsuarioControlador usuarioControlador;
    
    // Cards
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Login Panel Components
    private JTextField txtDocLogin;
    
    // Register Panel Components
    private JTextField txtDocReg;
    private JTextField txtNombreReg;
    private JSpinner spinEdadReg;

    public Login() {
        this.usuarioControlador = new UsuarioControlador();
        
        setTitle("Polla del Mundial - Acceso");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Custom color palette
        Color primaryGreen = new Color(27, 67, 50); // Deep forest green
        Color backgroundLight = new Color(240, 245, 241);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(backgroundLight);

        // --- LOGIN PANEL ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(backgroundLight);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitulo = new JLabel("POLLA MUNDIALISTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(primaryGreen);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Ingresa tu documento para comenzar", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        loginPanel.add(lblSub, gbc);

        // Document Label
        JLabel lblDoc = new JLabel("Documento:");
        lblDoc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoc.setForeground(primaryGreen);
        gbc.gridy = 2; gbc.gridwidth = 1;
        loginPanel.add(lblDoc, gbc);

        // Document Input
        txtDocLogin = new JTextField(15);
        txtDocLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDocLogin.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        loginPanel.add(txtDocLogin, gbc);

        // Button Login
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(primaryGreen);
        btnIngresar.setForeground(Color.BLACK);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(150, 35));
        btnIngresar.addActionListener(this::accionLogin);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        loginPanel.add(btnIngresar, gbc);

        // Switch to register link
        JButton btnIrARegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnIrARegistro.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        btnIrARegistro.setForeground(primaryGreen);
        btnIrARegistro.setBorderPainted(false);
        btnIrARegistro.setContentAreaFilled(false);
        btnIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrARegistro.addActionListener(e -> cardLayout.show(mainPanel, "REGISTRO"));
        gbc.gridy = 4;
        loginPanel.add(btnIrARegistro, gbc);

        // --- REGISTRATION PANEL ---
        JPanel regPanel = new JPanel(new GridBagLayout());
        regPanel.setBackground(backgroundLight);
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(8, 8, 8, 8);
        gbcReg.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRegTitulo = new JLabel("CREAR NUEVO USUARIO", SwingConstants.CENTER);
        lblRegTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRegTitulo.setForeground(primaryGreen);
        gbcReg.gridx = 0; gbcReg.gridy = 0; gbcReg.gridwidth = 2;
        regPanel.add(lblRegTitulo, gbcReg);

        // Document
        JLabel lblDocReg = new JLabel("Documento:");
        lblDocReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDocReg.setForeground(primaryGreen);
        gbcReg.gridy = 1; gbcReg.gridwidth = 1;
        regPanel.add(lblDocReg, gbcReg);

        txtDocReg = new JTextField(15);
        txtDocReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDocReg.setPreferredSize(new Dimension(200, 30));
        gbcReg.gridx = 1;
        regPanel.add(txtDocReg, gbcReg);

        // Nombre
        JLabel lblNombreReg = new JLabel("Nombre Completo:");
        lblNombreReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombreReg.setForeground(primaryGreen);
        gbcReg.gridx = 0; gbcReg.gridy = 2;
        regPanel.add(lblNombreReg, gbcReg);

        txtNombreReg = new JTextField(15);
        txtNombreReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombreReg.setPreferredSize(new Dimension(200, 30));
        gbcReg.gridx = 1;
        regPanel.add(txtNombreReg, gbcReg);

        // Edad
        JLabel lblEdadReg = new JLabel("Edad:");
        lblEdadReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEdadReg.setForeground(primaryGreen);
        gbcReg.gridx = 0; gbcReg.gridy = 3;
        regPanel.add(lblEdadReg, gbcReg);

        spinEdadReg = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
        spinEdadReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinEdadReg.setPreferredSize(new Dimension(200, 30));
        gbcReg.gridx = 1;
        regPanel.add(spinEdadReg, gbcReg);

        // Register Button
        JButton btnRegistrar = new JButton("Registrar e Ingresar");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setBackground(primaryGreen);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(150, 35));
        btnRegistrar.addActionListener(this::accionRegistro);
        gbcReg.gridx = 0; gbcReg.gridy = 4; gbcReg.gridwidth = 2;
        regPanel.add(btnRegistrar, gbcReg);

        // Switch to login link
        JButton btnIrALogin = new JButton("¿Ya tienes cuenta? Inicia sesión aquí");
        btnIrALogin.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        btnIrALogin.setForeground(primaryGreen);
        btnIrALogin.setBorderPainted(false);
        btnIrALogin.setContentAreaFilled(false);
        btnIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrALogin.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        gbcReg.gridy = 5;
        regPanel.add(btnIrALogin, gbcReg);

        // Add panels to CardLayout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(regPanel, "REGISTRO");
        
        add(mainPanel);
        
        // Show login by default
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void accionLogin(ActionEvent e) {
        String documento = txtDocLogin.getText().trim();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su número de documento.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Thread(() -> {
            Usuario usuario = usuarioControlador.iniciarSesion(documento);
            SwingUtilities.invokeLater(() -> {
                if (usuario != null) {
                    JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuario.getNombre() + "!", "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);
                    abrirMenuPrincipal(usuario);
                } else {
                    JOptionPane.showMessageDialog(this, "El documento ingresado no está registrado.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private void accionRegistro(ActionEvent e) {
        String documento = txtDocReg.getText().trim();
        String nombre = txtNombreReg.getText().trim();
        int edad = (int) spinEdadReg.getValue();

        if (documento.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Thread(() -> {
            boolean exito = usuarioControlador.registrarUsuario(documento, nombre, edad);
            SwingUtilities.invokeLater(() -> {
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                    Usuario recienRegistrado = usuarioControlador.iniciarSesion(documento);
                    if (recienRegistrado != null) {
                        abrirMenuPrincipal(recienRegistrado);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar: el documento ya podría estar en uso.", "Error de registro", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private void abrirMenuPrincipal(Usuario usuario) {
        MenuPrincipal menu = new MenuPrincipal(usuario);
        menu.setVisible(true);
        this.dispose(); // Close login window
    }
}
