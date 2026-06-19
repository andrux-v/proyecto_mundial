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
    private JPasswordField txtPassLogin;
    private JComboBox<String> cbRoleLogin;
    
    // Register Panel Components
    private JTextField txtDocReg;
    private JTextField txtNombreReg;
    private JSpinner spinEdadReg;
    private JPasswordField txtPassReg;
    private JComboBox<String> cbRoleReg;

    public Login() {
        this.usuarioControlador = new UsuarioControlador();
        
        setTitle("Mundial 2026 - Acceso");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Custom elegant color palette
        Color primaryColor = new Color(24, 43, 73); // Deep navy blue
        Color backgroundLight = new Color(245, 247, 250); // Soft grey-white
        
        // Set layout to BorderLayout to place the header at the top
        setLayout(new BorderLayout());

        // --- HEADER PANEL (Similar to MenuPrincipal) ---
        JPanel panelHeader = new JPanel(new BorderLayout(15, 0));
        panelHeader.setBackground(primaryColor);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblHeaderTitulo = new JLabel("MUNDIAL 2026", SwingConstants.LEFT);
        lblHeaderTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblHeaderTitulo, BorderLayout.WEST);

        JLabel lblHeaderSub = new JLabel("Acceso al Sistema", SwingConstants.RIGHT);
        lblHeaderSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHeaderSub.setForeground(new Color(220, 230, 240));
        panelHeader.add(lblHeaderSub, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(backgroundLight);

        // --- LOGIN PANEL ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(backgroundLight);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Subtitle inside loginPanel
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(primaryColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Ingresa tu documento para comenzar", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        loginPanel.add(lblSub, gbc);

        // Document Group Panel
        JPanel docGroup = new JPanel(new BorderLayout(0, 4));
        docGroup.setBackground(backgroundLight);
        JLabel lblDoc = new JLabel("Documento:");
        lblDoc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoc.setForeground(primaryColor);
        txtDocLogin = new JTextField(15);
        txtDocLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDocLogin.setPreferredSize(new Dimension(280, 30));
        restringirSoloNumeros(txtDocLogin);
        docGroup.add(lblDoc, BorderLayout.NORTH);
        docGroup.add(txtDocLogin, BorderLayout.CENTER);
        gbc.gridy = 2;
        loginPanel.add(docGroup, gbc);

        // Password Group Panel
        JPanel passGroup = new JPanel(new BorderLayout(0, 4));
        passGroup.setBackground(backgroundLight);
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(primaryColor);
        txtPassLogin = new JPasswordField(15);
        txtPassLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassLogin.setPreferredSize(new Dimension(280, 30));
        passGroup.add(lblPass, BorderLayout.NORTH);
        passGroup.add(txtPassLogin, BorderLayout.CENTER);
        gbc.gridy = 3;
        loginPanel.add(passGroup, gbc);

        // Role Group Panel
        JPanel roleGroup = new JPanel(new BorderLayout(0, 4));
        roleGroup.setBackground(backgroundLight);
        JLabel lblRoleSelect = new JLabel("Tipo de Usuario:");
        lblRoleSelect.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRoleSelect.setForeground(primaryColor);
        cbRoleLogin = new JComboBox<>(new String[]{"Usuario", "Administrador"});
        cbRoleLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbRoleLogin.setPreferredSize(new Dimension(280, 30));
        roleGroup.add(lblRoleSelect, BorderLayout.NORTH);
        roleGroup.add(cbRoleLogin, BorderLayout.CENTER);
        gbc.gridy = 4;
        loginPanel.add(roleGroup, gbc);

        // Button Login
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(primaryColor);
        btnIngresar.setForeground(Color.BLACK);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(280, 35));
        btnIngresar.addActionListener(this::accionLogin);
        gbc.gridy = 5;
        loginPanel.add(btnIngresar, gbc);

        // Switch to register link
        JButton btnIrARegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnIrARegistro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnIrARegistro.setForeground(primaryColor);
        btnIrARegistro.setBorderPainted(false);
        btnIrARegistro.setContentAreaFilled(false);
        btnIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrARegistro.addActionListener(e -> cardLayout.show(mainPanel, "REGISTRO"));
        gbc.gridy = 6;
        loginPanel.add(btnIrARegistro, gbc);

        // --- REGISTRATION PANEL ---
        JPanel regPanel = new JPanel(new GridBagLayout());
        regPanel.setBackground(backgroundLight);
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(6, 8, 6, 8);
        gbcReg.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRegTitulo = new JLabel("CREAR NUEVA CUENTA", SwingConstants.CENTER);
        lblRegTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRegTitulo.setForeground(primaryColor);
        gbcReg.gridx = 0; gbcReg.gridy = 0; gbcReg.gridwidth = 2;
        regPanel.add(lblRegTitulo, gbcReg);

        // Document Group Panel
        JPanel docRegGroup = new JPanel(new BorderLayout(0, 4));
        docRegGroup.setBackground(backgroundLight);
        JLabel lblDocReg = new JLabel("Documento:");
        lblDocReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDocReg.setForeground(primaryColor);
        txtDocReg = new JTextField(15);
        txtDocReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDocReg.setPreferredSize(new Dimension(280, 30));
        restringirSoloNumeros(txtDocReg);
        docRegGroup.add(lblDocReg, BorderLayout.NORTH);
        docRegGroup.add(txtDocReg, BorderLayout.CENTER);
        gbcReg.gridy = 1;
        regPanel.add(docRegGroup, gbcReg);

        // Nombre Group Panel
        JPanel nombreRegGroup = new JPanel(new BorderLayout(0, 4));
        nombreRegGroup.setBackground(backgroundLight);
        JLabel lblNombreReg = new JLabel("Nombre Completo:");
        lblNombreReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombreReg.setForeground(primaryColor);
        txtNombreReg = new JTextField(15);
        txtNombreReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombreReg.setPreferredSize(new Dimension(280, 30));
        nombreRegGroup.add(lblNombreReg, BorderLayout.NORTH);
        nombreRegGroup.add(txtNombreReg, BorderLayout.CENTER);
        gbcReg.gridy = 2;
        regPanel.add(nombreRegGroup, gbcReg);

        // Edad Group Panel
        JPanel edadRegGroup = new JPanel(new BorderLayout(0, 4));
        edadRegGroup.setBackground(backgroundLight);
        JLabel lblEdadReg = new JLabel("Edad:");
        lblEdadReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEdadReg.setForeground(primaryColor);
        spinEdadReg = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
        spinEdadReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinEdadReg.setPreferredSize(new Dimension(280, 30));
        restringirSoloNumeros(spinEdadReg);
        edadRegGroup.add(lblEdadReg, BorderLayout.NORTH);
        edadRegGroup.add(spinEdadReg, BorderLayout.CENTER);
        gbcReg.gridy = 3;
        regPanel.add(edadRegGroup, gbcReg);

        // Password Group Panel
        JPanel passRegGroup = new JPanel(new BorderLayout(0, 4));
        passRegGroup.setBackground(backgroundLight);
        JLabel lblPassReg = new JLabel("Contraseña:");
        lblPassReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassReg.setForeground(primaryColor);
        txtPassReg = new JPasswordField(15);
        txtPassReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassReg.setPreferredSize(new Dimension(280, 30));
        passRegGroup.add(lblPassReg, BorderLayout.NORTH);
        passRegGroup.add(txtPassReg, BorderLayout.CENTER);
        gbcReg.gridy = 4;
        regPanel.add(passRegGroup, gbcReg);

        // Role Group Panel (Register)
        JPanel roleRegGroup = new JPanel(new BorderLayout(0, 4));
        roleRegGroup.setBackground(backgroundLight);
        JLabel lblRoleReg = new JLabel("Registrarse como:");
        lblRoleReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRoleReg.setForeground(primaryColor);
        cbRoleReg = new JComboBox<>(new String[]{"Usuario", "Administrador"});
        cbRoleReg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbRoleReg.setPreferredSize(new Dimension(280, 30));
        roleRegGroup.add(lblRoleReg, BorderLayout.NORTH);
        roleRegGroup.add(cbRoleReg, BorderLayout.CENTER);
        gbcReg.gridy = 5;
        regPanel.add(roleRegGroup, gbcReg);

        // Register Button
        JButton btnRegistrar = new JButton("Registrar e Ingresar");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setBackground(primaryColor);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(280, 35));
        btnRegistrar.addActionListener(this::accionRegistro);
        gbcReg.gridy = 6;
        regPanel.add(btnRegistrar, gbcReg);

        // Switch to login link
        JButton btnIrALogin = new JButton("¿Ya tienes cuenta? Inicia sesión aquí");
        btnIrALogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnIrALogin.setForeground(primaryColor);
        btnIrALogin.setBorderPainted(false);
        btnIrALogin.setContentAreaFilled(false);
        btnIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrALogin.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        gbcReg.gridy = 7;
        regPanel.add(btnIrALogin, gbcReg);

        // Add panels to CardLayout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(regPanel, "REGISTRO");
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Show login by default
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void accionLogin(ActionEvent e) {
        String documento = txtDocLogin.getText().trim();
        String contrasena = new String(txtPassLogin.getPassword()).trim();
        if (documento.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su documento y contraseña.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!documento.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El documento debe contener solo números.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean seleccionoAdmin = cbRoleLogin.getSelectedItem().equals("Administrador");
        new Thread(() -> {
            Usuario usuario = usuarioControlador.iniciarSesion(documento, contrasena);
            SwingUtilities.invokeLater(() -> {
                if (usuario != null) {
                    if (usuario.isEsAdmin() != seleccionoAdmin) {
                        String rolEsperado = seleccionoAdmin ? "Administrador" : "Usuario";
                        JOptionPane.showMessageDialog(this, "El usuario con documento " + documento + " no está registrado como " + rolEsperado + ".", "Rol Incorrecto", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuario.getNombre() + "!", "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);
                    abrirMenuPrincipal(usuario);
                } else {
                    JOptionPane.showMessageDialog(this, "Documento o contraseña incorrectos.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private void accionRegistro(ActionEvent e) {
        String documento = txtDocReg.getText().trim();
        String nombre = txtNombreReg.getText().trim();
        int edad = (int) spinEdadReg.getValue();
        String contrasena = new String(txtPassReg.getPassword()).trim();

        if (documento.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!documento.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El documento debe contener solo números.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean registrarComoAdmin = cbRoleReg.getSelectedItem().equals("Administrador");
        new Thread(() -> {
            boolean exito = usuarioControlador.registrarUsuario(documento, nombre, edad, registrarComoAdmin, contrasena);
            SwingUtilities.invokeLater(() -> {
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                    Usuario recienRegistrado = usuarioControlador.iniciarSesion(documento, contrasena);
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

    private void restringirSoloNumeros(JTextField textField) {
        ((javax.swing.text.AbstractDocument) textField.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text == null) return;
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private void restringirSoloNumeros(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != '\b' && c != '\u007F') {
                        e.consume();
                    }
                }
            });
        }
    }
}
