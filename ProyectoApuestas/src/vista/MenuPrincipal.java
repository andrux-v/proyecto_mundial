package vista;

import controlador.UsuarioControlador;
import controlador.ApuestaControlador;
import controlador.ApuestaControlador.FilaPosicion;
import controlador.ApuestaControlador.InformacionEquipo;
import controlador.ApuestaControlador.PartidoComparacion;
import modelo.Usuario;
import modelo.Partido;
import modelo.Apuesta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends JFrame {
    private Usuario usuarioLogueado;
    private UsuarioControlador usuarioControlador;
    private ApuestaControlador apuestaControlador;

    // Header Components
    private JLabel lblPuntosHeader;

    // Tabs
    private JTabbedPane tabbedPane;

    // Comboboxes
    private JComboBox<String> cbGruposPronosticos;
    private JComboBox<String> cbGruposTablas;
    private JComboBox<String> cbGruposAdmin;

    // Pronósticos Components
    private JPanel panelPronosticosContenido;
    private List<Partido> partidosPronosticoActual;
    private List<Apuesta> apuestasPronosticoActual;
    private List<JSpinner> spinnersLocal;
    private List<JSpinner> spinnersVisitante;

    // Standings Table Components
    private JTable tblStandings;
    private DefaultTableModel modelStandings;
    private JRadioButton radStandingsReales;
    private JRadioButton radStandingsPronosticos;

    // Leaderboard Components
    private JTable tblLeaderboard;
    private DefaultTableModel modelLeaderboard;

    // Admin Components
    private JPanel panelAdminContenido;
    private List<Partido> partidosAdminActual;
    private List<JSpinner> spinnersRealLocal;
    private List<JSpinner> spinnersRealVisitante;

    // Search Components
    private JTextField txtBusquedaEquipo;
    private JLabel lblSearchInfo;
    private JTable tblSearchMatches;
    private DefaultTableModel modelSearchMatches;

    // Historial Components
    private JTable tblHistorial;
    private DefaultTableModel modelHistorial;

    // Colors
    private final Color primaryGreen = new Color(27, 67, 50);
    private final Color secondaryGreen = new Color(45, 106, 79);
    private final Color bgLight = new Color(240, 245, 241);

    public MenuPrincipal(Usuario usuario) {
        this.usuarioLogueado = usuario;
        this.usuarioControlador = new UsuarioControlador();
        this.apuestaControlador = new ApuestaControlador();

        this.spinnersLocal = new ArrayList<>();
        this.spinnersVisitante = new ArrayList<>();
        this.spinnersRealLocal = new ArrayList<>();
        this.spinnersRealVisitante = new ArrayList<>();

        setTitle("Sistema de Apuestas - Mundial");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        inicializarComponentes();
        actualizarPuntosHeader();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel panelHeader = new JPanel(new BorderLayout(15, 0));
        panelHeader.setBackground(primaryGreen);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel panelUserInfo = new JPanel(new GridLayout(2, 1, 0, 3));
        panelUserInfo.setOpaque(false);
        JLabel lblNombre = new JLabel("Bienvenido, " + usuarioLogueado.getNombre(), SwingConstants.LEFT);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(Color.WHITE);
        
        String rol = usuarioLogueado.isEsAdmin() ? "Administrador" : "Usuario";
        JLabel lblRol = new JLabel("Rol: " + rol + " | Documento: " + usuarioLogueado.getDocumento(), SwingConstants.LEFT);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(200, 220, 205));
        
        panelUserInfo.add(lblNombre);
        panelUserInfo.add(lblRol);
        panelHeader.add(panelUserInfo, BorderLayout.WEST);

        JPanel panelHeaderActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelHeaderActions.setOpaque(false);

        lblPuntosHeader = new JLabel("Puntos: 0", SwingConstants.RIGHT);
        lblPuntosHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPuntosHeader.setForeground(new Color(250, 215, 100)); // Gold
        panelHeaderActions.add(lblPuntosHeader);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(150, 30, 30));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            this.dispose();
        });
        panelHeaderActions.add(btnLogout);
        panelHeader.add(panelHeaderActions, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- TABBED PANE (CENTER) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        if (usuarioLogueado.isEsAdmin()) {
            // Admin Tabs: Grupos y Equipos, Tablas de Posiciones, Ranking General, Resultados Oficiales
            tabbedPane.addTab("Grupos y Equipos", crearPanelGrupos());
            tabbedPane.addTab("Tablas de Posiciones", crearPanelTablas());
            tabbedPane.addTab("Ranking General", crearPanelLeaderboard());
            tabbedPane.addTab("Resultados Oficiales (Admin)", crearPanelAdmin());
        } else {
            // User Tabs: Grupos y Equipos, Mis Pronósticos, Tablas de Posiciones, Ranking General, Buscar Equipo, Historial de Actividad
            tabbedPane.addTab("Grupos y Equipos", crearPanelGrupos());
            tabbedPane.addTab("Mis Pronósticos", crearPanelPronosticos());
            tabbedPane.addTab("Tablas de Posiciones", crearPanelTablas());
            tabbedPane.addTab("Ranking General", crearPanelLeaderboard());
            tabbedPane.addTab("Buscar Equipo", crearPanelBuscarEquipo());
            tabbedPane.addTab("Historial de Actividad", crearPanelHistorial());
        }

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                String title = tabbedPane.getTitleAt(selectedIndex);
                if ("Historial de Actividad".equals(title)) {
                    cargarHistorial();
                } else if ("Tablas de Posiciones".equals(title)) {
                    cargarTablaPosicionesActual();
                } else if ("Ranking General".equals(title)) {
                    cargarLeaderboard();
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- VIEW MAKER METHODS ---

    private JComponent crearPanelGrupos() {
        JPanel panelGrupos = new JPanel(new GridLayout(4, 3, 15, 15));
        panelGrupos.setBackground(bgLight);
        panelGrupos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] grupos = {
            "Grupo A", "Grupo B", "Grupo C", "Grupo D", 
            "Grupo E", "Grupo F", "Grupo G", "Grupo H",
            "Grupo I", "Grupo J", "Grupo K", "Grupo L"
        };

        String[][] equipos = {
            {"México", "Sudáfrica", "Corea del Sur", "República Checa"},
            {"Canadá", "Bosnia-Herzegovina", "Qatar", "Suiza"},
            {"Brasil", "Marruecos", "Haití", "Escocia"},
            {"EE. UU.", "Paraguay", "Australia", "Turquía"},
            {"Alemania", "Curazao", "Costa de Marfil", "Ecuador"},
            {"Países Bajos", "Japón", "Suecia", "Túnez"},
            {"Bélgica", "Egipto", "Irán", "Nueva Zelanda"},
            {"España", "Cabo Verde", "Arabia Saudita", "Uruguay"},
            {"Francia", "Senegal", "Irak", "Noruega"},
            {"Argentina", "Argelia", "Austria", "Jordania"},
            {"Portugal", "RD Congo", "Uzbekistán", "Colombia"},
            {"Inglaterra", "Croacia", "Ghana", "Panamá"}
        };

        for (int i = 0; i < 12; i++) {
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryGreen, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));

            JLabel title = new JLabel(grupos[i], SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            title.setForeground(primaryGreen);
            card.add(title, BorderLayout.NORTH);

            JPanel list = new JPanel(new GridLayout(4, 1, 3, 3));
            list.setBackground(Color.WHITE);
            for (int j = 0; j < 4; j++) {
                JLabel eq = new JLabel((j + 1) + ". " + equipos[i][j], SwingConstants.LEFT);
                eq.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                list.add(eq);
            }
            card.add(list, BorderLayout.CENTER);
            panelGrupos.add(card);
        }

        return new JScrollPane(panelGrupos);
    }

    private JPanel crearPanelPronosticos() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        // Control Panel Top
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelControl.setBackground(bgLight);
        panelControl.add(new JLabel("Seleccione el Grupo:"));

        String[] nombresGrupos = obtenerNombresGrupos();
        cbGruposPronosticos = new JComboBox<>(nombresGrupos);
        cbGruposPronosticos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbGruposPronosticos.addActionListener(e -> {
            String selected = (String) cbGruposPronosticos.getSelectedItem();
            if (selected != null) {
                cargarPronosticosGrupo(selected);
            }
        });
        panelControl.add(cbGruposPronosticos);
        mainPanel.add(panelControl, BorderLayout.NORTH);

        // Matches Panel Center
        panelPronosticosContenido = new JPanel();
        panelPronosticosContenido.setBackground(Color.WHITE);
        panelPronosticosContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollMatches = new JScrollPane(panelPronosticosContenido);
        mainPanel.add(scrollMatches, BorderLayout.CENTER);

        // Save Button Bottom
        JPanel panelSave = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelSave.setBackground(bgLight);
        JButton btnGuardar = new JButton("Guardar Pronósticos");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(primaryGreen);
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(220, 40));
        btnGuardar.addActionListener(this::accionGuardarPronosticos);
        panelSave.add(btnGuardar);
        mainPanel.add(panelSave, BorderLayout.SOUTH);

        // Initialize with Group A
        cargarPronosticosGrupo(nombresGrupos[0]);

        return mainPanel;
    }

    private JPanel crearPanelTablas() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        // Top Panel
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelTop.setBackground(bgLight);
        panelTop.add(new JLabel("Grupo:"));

        String[] nombresGrupos = obtenerNombresGrupos();
        cbGruposTablas = new JComboBox<>(nombresGrupos);
        cbGruposTablas.addActionListener(e -> cargarTablaPosicionesActual());
        panelTop.add(cbGruposTablas);

        if (!usuarioLogueado.isEsAdmin()) {
            panelTop.add(new JLabel("    Ver posiciones basadas en:"));
            ButtonGroup btnGroup = new ButtonGroup();
            radStandingsReales = new JRadioButton("Resultados Reales (Oficiales)", true);
            radStandingsReales.setOpaque(false);
            radStandingsReales.addActionListener(e -> cargarTablaPosicionesActual());

            radStandingsPronosticos = new JRadioButton("Mis Pronósticos", false);
            radStandingsPronosticos.setOpaque(false);
            radStandingsPronosticos.addActionListener(e -> cargarTablaPosicionesActual());

            btnGroup.add(radStandingsReales);
            btnGroup.add(radStandingsPronosticos);
            panelTop.add(radStandingsReales);
            panelTop.add(radStandingsPronosticos);
        } else {
            // For admin, we instantiate them so they aren't null, but we don't add them to the UI
            radStandingsReales = new JRadioButton("Resultados Reales (Oficiales)", true);
            radStandingsPronosticos = new JRadioButton("Mis Pronósticos", false);
        }

        mainPanel.add(panelTop, BorderLayout.NORTH);

        // Table Panel Center
        modelStandings = new DefaultTableModel(
            new Object[]{"Puesto", "Equipo", "PTS", "PJ", "PG", "PE", "PP", "GF", "GC", "DG"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblStandings = new JTable(modelStandings);
        styleTable(tblStandings);
        JScrollPane scrollTable = new JScrollPane(tblStandings);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        mainPanel.add(scrollTable, BorderLayout.CENTER);

        // Load initially
        cargarTablaPosicionesActual();

        return mainPanel;
    }

    private JPanel crearPanelLeaderboard() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        JLabel lblTitle = new JLabel("Líderes de la Polla - Ranking de Puntajes", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(primaryGreen);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        modelLeaderboard = new DefaultTableModel(
            new Object[]{"Puesto", "Nombre Completo", "Documento", "Edad", "Puntos Totales"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblLeaderboard = new JTable(modelLeaderboard);
        styleTable(tblLeaderboard);
        JScrollPane scrollTable = new JScrollPane(tblLeaderboard);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        mainPanel.add(scrollTable, BorderLayout.CENTER);

        // Actions South
        JPanel panelBajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelBajo.setBackground(bgLight);
        JButton btnRefrescar = new JButton("Actualizar Ranking");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescar.setBackground(secondaryGreen);
        btnRefrescar.setForeground(Color.BLACK);
        btnRefrescar.setPreferredSize(new Dimension(180, 35));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarLeaderboard());
        panelBajo.add(btnRefrescar);
        mainPanel.add(panelBajo, BorderLayout.SOUTH);

        // Load initially
        cargarLeaderboard();

        return mainPanel;
    }

    private JPanel crearPanelBuscarEquipo() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        // Top Panel Search
        JPanel panelSearchControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panelSearchControl.setBackground(bgLight);
        panelSearchControl.add(new JLabel("Nombre del equipo:"));

        txtBusquedaEquipo = new JTextField(18);
        txtBusquedaEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelSearchControl.add(txtBusquedaEquipo);

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSearch.setBackground(primaryGreen);
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setPreferredSize(new Dimension(90, 28));
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(this::accionBuscarEquipo);
        panelSearchControl.add(btnSearch);

        mainPanel.add(panelSearchControl, BorderLayout.NORTH);

        // Results Panel Center
        JPanel panelResultados = new JPanel(new BorderLayout(0, 10));
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblSearchInfo = new JLabel("Ingrese el nombre de un equipo para ver su historial y compararlo con sus predicciones.");
        lblSearchInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelResultados.add(lblSearchInfo, BorderLayout.NORTH);

        modelSearchMatches = new DefaultTableModel(
            new Object[]{"Local", "Resultado Real", "vs", "Resultado Pronóstico", "Visitante", "Puntos Ganados"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblSearchMatches = new JTable(modelSearchMatches);
        styleTable(tblSearchMatches);
        // Centering vs column
        tblSearchMatches.getColumnModel().getColumn(2).setPreferredWidth(40);
        tblSearchMatches.getColumnModel().getColumn(2).setMaxWidth(40);
        tblSearchMatches.getColumnModel().getColumn(2).setMinWidth(40);

        JScrollPane scrollSearchTable = new JScrollPane(tblSearchMatches);
        panelResultados.add(scrollSearchTable, BorderLayout.CENTER);

        mainPanel.add(panelResultados, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel crearPanelAdmin() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        // Control Panel Top
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelControl.setBackground(bgLight);
        panelControl.add(new JLabel("Grupo a Administrar:"));

        String[] nombresGrupos = obtenerNombresGrupos();
        cbGruposAdmin = new JComboBox<>(nombresGrupos);
        cbGruposAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbGruposAdmin.addActionListener(e -> {
            String selected = (String) cbGruposAdmin.getSelectedItem();
            if (selected != null) {
                cargarPartidosAdmin(selected);
            }
        });
        panelControl.add(cbGruposAdmin);
        mainPanel.add(panelControl, BorderLayout.NORTH);

        // Matches Panel Center
        panelAdminContenido = new JPanel();
        panelAdminContenido.setBackground(Color.WHITE);
        panelAdminContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollMatches = new JScrollPane(panelAdminContenido);
        mainPanel.add(scrollMatches, BorderLayout.CENTER);

        // Save Button Bottom
        JPanel panelSave = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelSave.setBackground(bgLight);
        JButton btnGuardar = new JButton("Guardar Resultados Oficiales");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(150, 80, 0));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(240, 40));
        btnGuardar.addActionListener(this::accionGuardarResultadosAdmin);
        panelSave.add(btnGuardar);
        mainPanel.add(panelSave, BorderLayout.SOUTH);

        // Initialize with Group A
        cargarPartidosAdmin(nombresGrupos[0]);

        return mainPanel;
    }

    // --- REFRESH AND ACTION LOGIC ---

    private String[] obtenerNombresGrupos() {
        return new String[]{
            "Grupo A", "Grupo B", "Grupo C", "Grupo D",
            "Grupo E", "Grupo F", "Grupo G", "Grupo H",
            "Grupo I", "Grupo J", "Grupo K", "Grupo L"
        };
    }

    private void actualizarPuntosHeader() {
        new Thread(() -> {
            List<Usuario> ranking = usuarioControlador.obtenerRanking();
            int userPuntos = 0;
            for (Usuario u : ranking) {
                if (u.getDocumento().equals(usuarioLogueado.getDocumento())) {
                    userPuntos = u.getPuntos();
                    break;
                }
            }
            final int pts = userPuntos;
            SwingUtilities.invokeLater(() -> {
                lblPuntosHeader.setText("Puntos: " + pts);
            });
        }).start();
    }

    private void cargarPronosticosGrupo(String grupo) {
        panelPronosticosContenido.removeAll();
        spinnersLocal.clear();
        spinnersVisitante.clear();
        partidosPronosticoActual = apuestaControlador.obtenerPartidosGrupo(grupo);
        apuestasPronosticoActual = apuestaControlador.obtenerApuestasGrupoUsuario(grupo, usuarioLogueado.getDocumento());

        panelPronosticosContenido.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < partidosPronosticoActual.size(); i++) {
            Partido p = partidosPronosticoActual.get(i);
            Apuesta a = apuestasPronosticoActual.get(i);

            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lblLocal = new JLabel(p.getEquipoLocal(), SwingConstants.RIGHT);
            lblLocal.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panelPronosticosContenido.add(lblLocal, gbc);

            gbc.gridx = 1; gbc.weightx = 0.08;
            JSpinner spinLocal = new JSpinner(new SpinnerNumberModel(a.getGolesLocal(), 0, 25, 1));
            spinLocal.setPreferredSize(new Dimension(50, 25));
            panelPronosticosContenido.add(spinLocal, gbc);
            spinnersLocal.add(spinLocal);

            gbc.gridx = 2; gbc.weightx = 0.04;
            JLabel lblVs = new JLabel("vs", SwingConstants.CENTER);
            panelPronosticosContenido.add(lblVs, gbc);

            gbc.gridx = 3; gbc.weightx = 0.08;
            JSpinner spinVisitante = new JSpinner(new SpinnerNumberModel(a.getGolesVisitante(), 0, 25, 1));
            spinVisitante.setPreferredSize(new Dimension(50, 25));
            panelPronosticosContenido.add(spinVisitante, gbc);
            spinnersVisitante.add(spinVisitante);

            gbc.gridx = 4; gbc.weightx = 0.35;
            JLabel lblVisitante = new JLabel(p.getEquipoVisitante(), SwingConstants.LEFT);
            lblVisitante.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panelPronosticosContenido.add(lblVisitante, gbc);

            // Real outcome display
            gbc.gridx = 5; gbc.weightx = 0.15;
            String realStr = (p.getGolesLocal() == -1) ? "Pendiente" : p.getGolesLocal() + " - " + p.getGolesVisitante();
            JLabel lblReal = new JLabel("(Oficial: " + realStr + ")", SwingConstants.CENTER);
            lblReal.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblReal.setForeground(Color.GRAY);
            panelPronosticosContenido.add(lblReal, gbc);
        }

        panelPronosticosContenido.revalidate();
        panelPronosticosContenido.repaint();
    }

    private void accionGuardarPronosticos(ActionEvent e) {
        if (apuestasPronosticoActual == null || apuestasPronosticoActual.size() < 6) return;
        
        for (int i = 0; i < 6; i++) {
            Apuesta a = apuestasPronosticoActual.get(i);
            int gl = (int) spinnersLocal.get(i).getValue();
            int gv = (int) spinnersVisitante.get(i).getValue();
            a.setGolesLocal(gl);
            a.setGolesVisitante(gv);
        }

        new Thread(() -> {
            boolean ok = apuestaControlador.guardarApuestas(apuestasPronosticoActual);
            SwingUtilities.invokeLater(() -> {
                if (ok) {
                    JOptionPane.showMessageDialog(this, "¡Pronósticos guardados exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarPuntosHeader();
                    cargarTablaPosicionesActual();
                    cargarHistorial();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar pronósticos en la base de datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private void cargarTablaPosicionesActual() {
        if (cbGruposTablas == null) return;
        String grupo = (String) cbGruposTablas.getSelectedItem();
        if (grupo == null) return;

        boolean reales = radStandingsReales.isSelected();

        new Thread(() -> {
            List<FilaPosicion> filas = apuestaControlador.calcularTablaPosiciones(
                grupo, reales, usuarioLogueado.getDocumento()
            );
            
            SwingUtilities.invokeLater(() -> {
                modelStandings.setRowCount(0);
                int puesto = 1;
                for (FilaPosicion f : filas) {
                    modelStandings.addRow(new Object[]{
                        puesto++,
                        f.getEquipo(),
                        f.getPuntos(),
                        f.getPj(),
                        f.getPg(),
                        f.getPe(),
                        f.getPp(),
                        f.getGf(),
                        f.getGc(),
                        f.getDif() >= 0 ? "+" + f.getDif() : f.getDif()
                    });
                }
            });
        }).start();
    }

    private void cargarLeaderboard() {
        new Thread(() -> {
            List<Usuario> ranking = usuarioControlador.obtenerRanking();
            SwingUtilities.invokeLater(() -> {
                modelLeaderboard.setRowCount(0);
                int puesto = 1;
                for (Usuario u : ranking) {
                    modelLeaderboard.addRow(new Object[]{
                        puesto++,
                        u.getNombre() + (u.isEsAdmin() ? " (Admin)" : ""),
                        u.getDocumento(),
                        u.getEdad(),
                        u.getPuntos()
                    });
                }
            });
        }).start();
    }

    private void accionBuscarEquipo(ActionEvent e) {
        String query = txtBusquedaEquipo.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de un equipo.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Thread(() -> {
            InformacionEquipo info = apuestaControlador.buscarEquipo(query, usuarioLogueado.getDocumento());
            SwingUtilities.invokeLater(() -> {
                modelSearchMatches.setRowCount(0);
                if (info == null) {
                    lblSearchInfo.setText("No se encontró el equipo \"" + query + "\" en el mundial.");
                    lblSearchInfo.setForeground(new Color(150, 30, 30));
                } else {
                    lblSearchInfo.setText("Equipo: " + info.getEquipo() + " | Grupo: " + info.getGrupo());
                    lblSearchInfo.setForeground(primaryGreen);

                    for (PartidoComparacion pc : info.getPartidos()) {
                        modelSearchMatches.addRow(new Object[]{
                            pc.getLocal(),
                            pc.getResultadoReal(),
                            "vs",
                            pc.getPronostico(),
                            pc.getVisitante(),
                            pc.getPuntosObtenidos()
                        });
                    }
                }
            });
        }).start();
    }

    private void cargarPartidosAdmin(String grupo) {
        panelAdminContenido.removeAll();
        spinnersRealLocal.clear();
        spinnersRealVisitante.clear();
        partidosAdminActual = apuestaControlador.obtenerPartidosGrupo(grupo);

        panelAdminContenido.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < partidosAdminActual.size(); i++) {
            Partido p = partidosAdminActual.get(i);

            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lblLocal = new JLabel(p.getEquipoLocal(), SwingConstants.RIGHT);
            lblLocal.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panelAdminContenido.add(lblLocal, gbc);

            gbc.gridx = 1; gbc.weightx = 0.08;
            JSpinner spinLocal = new JSpinner(new SpinnerNumberModel(p.getGolesLocal(), -1, 25, 1));
            spinLocal.setPreferredSize(new Dimension(55, 25));
            panelAdminContenido.add(spinLocal, gbc);
            spinnersRealLocal.add(spinLocal);

            gbc.gridx = 2; gbc.weightx = 0.04;
            JLabel lblVs = new JLabel("vs", SwingConstants.CENTER);
            panelAdminContenido.add(lblVs, gbc);

            gbc.gridx = 3; gbc.weightx = 0.08;
            JSpinner spinVisitante = new JSpinner(new SpinnerNumberModel(p.getGolesVisitante(), -1, 25, 1));
            spinVisitante.setPreferredSize(new Dimension(55, 25));
            panelAdminContenido.add(spinVisitante, gbc);
            spinnersRealVisitante.add(spinVisitante);

            gbc.gridx = 4; gbc.weightx = 0.35;
            JLabel lblVisitante = new JLabel(p.getEquipoVisitante(), SwingConstants.LEFT);
            lblVisitante.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panelAdminContenido.add(lblVisitante, gbc);

            gbc.gridx = 5; gbc.weightx = 0.15;
            JLabel lblInfo = new JLabel("(-1 = Por jugar)", SwingConstants.CENTER);
            lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblInfo.setForeground(Color.GRAY);
            panelAdminContenido.add(lblInfo, gbc);
        }

        panelAdminContenido.revalidate();
        panelAdminContenido.repaint();
    }

    private void accionGuardarResultadosAdmin(ActionEvent e) {
        if (partidosAdminActual == null || partidosAdminActual.size() < 6) return;

        for (int i = 0; i < 6; i++) {
            Partido p = partidosAdminActual.get(i);
            int gl = (int) spinnersRealLocal.get(i).getValue();
            int gv = (int) spinnersRealVisitante.get(i).getValue();
            p.setGolesLocal(gl);
            p.setGolesVisitante(gv);
        }

        new Thread(() -> {
            boolean ok = apuestaControlador.guardarResultadosReales(partidosAdminActual);
            SwingUtilities.invokeLater(() -> {
                if (ok) {
                    JOptionPane.showMessageDialog(this, "¡Resultados oficiales actualizados con éxito!", "Administración", JOptionPane.INFORMATION_MESSAGE);
                    actualizarPuntosHeader();
                    cargarTablaPosicionesActual();
                    
                    // Si el grupo cargado en pronósticos es el mismo, recargarlo
                    if (cbGruposPronosticos != null) {
                        String grupoSelPronos = (String) cbGruposPronosticos.getSelectedItem();
                        String grupoSelAdmin = (String) cbGruposAdmin.getSelectedItem();
                        if (grupoSelPronos != null && grupoSelPronos.equals(grupoSelAdmin)) {
                            cargarPronosticosGrupo(grupoSelPronos);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar resultados oficiales en la base de datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    private JPanel crearPanelHistorial() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgLight);

        JLabel lblTitle = new JLabel("Historial de Predicciones Realizadas", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(primaryGreen);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        modelHistorial = new DefaultTableModel(
            new Object[]{"Fecha/Hora", "Acción", "Partido", "Mi Predicción"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblHistorial = new JTable(modelHistorial);
        styleTable(tblHistorial);
        JScrollPane scrollTable = new JScrollPane(tblHistorial);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        mainPanel.add(scrollTable, BorderLayout.CENTER);

        // Actions South
        JPanel panelBajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panelBajo.setBackground(bgLight);
        JButton btnRefrescar = new JButton("Actualizar Historial");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescar.setBackground(secondaryGreen);
        btnRefrescar.setForeground(Color.BLACK);
        btnRefrescar.setPreferredSize(new Dimension(180, 35));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarHistorial());
        panelBajo.add(btnRefrescar);
        mainPanel.add(panelBajo, BorderLayout.SOUTH);

        // Load initially
        cargarHistorial();

        return mainPanel;
    }

    private void cargarHistorial() {
        if (modelHistorial == null) return;
        new Thread(() -> {
            List<Object[]> datos = apuestaControlador.obtenerHistorialUsuario(usuarioLogueado.getDocumento());
            SwingUtilities.invokeLater(() -> {
                modelHistorial.setRowCount(0);
                for (Object[] fila : datos) {
                    java.sql.Timestamp fecha = (java.sql.Timestamp) fila[0];
                    String accion = (String) fila[1];
                    String local = (String) fila[2];
                    String visitante = (String) fila[3];
                    int golesLocal = (int) fila[4];
                    int golesVisitante = (int) fila[5];

                    String fechaStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
                    String partido = local + " vs " + visitante;
                    String prediccion = golesLocal + " - " + golesVisitante;

                    modelHistorial.addRow(new Object[]{
                        fechaStr,
                        accion,
                        partido,
                        prediccion
                    });
                }
            });
        }).start();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(primaryGreen);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(new Color(220, 225, 220));
        table.setSelectionBackground(secondaryGreen);
        table.setSelectionForeground(Color.WHITE);
    }
}
