package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "mundial_db";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el driver de MySQL: " + e.getMessage());
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static boolean inicializarBaseDeDatos() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            // 1. Crear Base de Datos si no existe
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Base de datos '" + DB_NAME + "' verificada/creada.");
            
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor MySQL para inicializar: " + e.getMessage());
            return false;
        }

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement()) {

            // Migración: Cambiar documento a BIGINT y eliminar admin de texto antiguo
            try {
                stmt.executeUpdate("DELETE FROM usuarios WHERE documento = 'admin'");
                
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
                stmt.executeUpdate("ALTER TABLE usuarios MODIFY COLUMN documento BIGINT");
                stmt.executeUpdate("ALTER TABLE apuestas MODIFY COLUMN usuario_documento BIGINT");
                stmt.executeUpdate("ALTER TABLE historial_apuestas MODIFY COLUMN usuario_documento BIGINT");
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            } catch (SQLException e) {
                // Si falla o no aplica, continuar
            }

            // 2. Crear Tabla Usuarios
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "  documento BIGINT PRIMARY KEY," +
                "  nombre VARCHAR(100) NOT NULL," +
                "  edad INT NOT NULL," +
                "  es_admin BOOLEAN DEFAULT FALSE," +
                "  contrasena VARCHAR(255) NOT NULL" +
                ")"
            );

            // Migración: añadir columna contrasena si no existe
            try {
                stmt.executeUpdate("ALTER TABLE usuarios ADD COLUMN contrasena VARCHAR(255) NOT NULL DEFAULT '12345'");
            } catch (SQLException e) {
                if (e.getErrorCode() != 1060) { // 1060 = Duplicate column name
                    throw e;
                }
            }

            // 3. Crear Tabla Partidos
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS partidos (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  grupo VARCHAR(50) NOT NULL," +
                "  partido_nro INT NOT NULL," +
                "  equipo_local VARCHAR(100) NOT NULL," +
                "  equipo_visitante VARCHAR(100) NOT NULL," +
                "  goles_local INT DEFAULT -1," +
                "  goles_visitante INT DEFAULT -1," +
                "  UNIQUE KEY unique_partido (grupo, partido_nro)" +
                ")"
            );

            // 4. Crear Tabla Apuestas
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS apuestas (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  usuario_documento BIGINT NOT NULL," +
                "  partido_id INT NOT NULL," +
                "  goles_local INT NOT NULL," +
                "  goles_visitante INT NOT NULL," +
                "  FOREIGN KEY (usuario_documento) REFERENCES usuarios(documento) ON DELETE CASCADE," +
                "  FOREIGN KEY (partido_id) REFERENCES partidos(id) ON DELETE CASCADE," +
                "  UNIQUE KEY unique_usuario_partido (usuario_documento, partido_id)" +
                ")"
            );

            // 4.1 Crear Tabla Historial de Apuestas
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS historial_apuestas (" +
                "  id_historial INT AUTO_INCREMENT PRIMARY KEY," +
                "  usuario_documento BIGINT NOT NULL," +
                "  partido_id INT NOT NULL," +
                "  goles_local INT NOT NULL," +
                "  goles_visitante INT NOT NULL," +
                "  fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "  tipo_accion VARCHAR(50) NOT NULL" +
                ")"
            );

            // 4.2 Crear Trigger Nueva Apuesta
            try {
                stmt.executeUpdate(
                    "CREATE TRIGGER trigger_nueva_apuesta AFTER INSERT ON apuestas " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "  INSERT INTO historial_apuestas (usuario_documento, partido_id, goles_local, goles_visitante, tipo_accion) " +
                    "  VALUES (NEW.usuario_documento, NEW.partido_id, NEW.goles_local, NEW.goles_visitante, 'NUEVA PREDICCIÓN'); " +
                    "END"
                );
            } catch (SQLException e) {
                if (e.getErrorCode() != 1359) { // 1359 = Trigger already exists
                    throw e;
                }
            }

            // 4.3 Crear Trigger Actualizar Apuesta
            try {
                stmt.executeUpdate("DROP TRIGGER IF EXISTS trigger_actualiza_apuesta");
                stmt.executeUpdate(
                    "CREATE TRIGGER trigger_actualiza_apuesta AFTER UPDATE ON apuestas " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "  IF OLD.goles_local != NEW.goles_local OR OLD.goles_visitante != NEW.goles_visitante THEN " +
                    "    UPDATE historial_apuestas " +
                    "    SET goles_local = NEW.goles_local, " +
                    "        goles_visitante = NEW.goles_visitante, " +
                    "        fecha_registro = CURRENT_TIMESTAMP, " +
                    "        tipo_accion = 'PREDICCIÓN MODIFICADA' " +
                    "    WHERE usuario_documento = NEW.usuario_documento AND partido_id = NEW.partido_id; " +
                    "  END IF; " +
                    "END"
                );
            } catch (SQLException e) {
                if (e.getErrorCode() != 1359) { // 1359 = Trigger already exists
                    throw e;
                }
            }

            System.out.println("Tablas de la base de datos verificadas/creadas con éxito.");

            // 5. Pre-sembrar el usuario Admin si no existe
            preSembrarAdmin(conn);

            // 6. Pre-sembrar los partidos si la tabla partidos está vacía
            preSembrarPartidos(conn);

            return true;
        } catch (SQLException e) {
            System.err.println("Error al inicializar tablas en la base de datos: " + e.getMessage());
            return false;
        }
    }

    private static void preSembrarAdmin(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM usuarios WHERE documento = 1";
        String sqlInsert = "INSERT INTO usuarios (documento, nombre, edad, es_admin, contrasena) VALUES (1, 'Administrador', 30, true, 'admin')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate(sqlInsert);
                System.out.println("Usuario admin pre-sembrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al pre-sembrar usuario admin: " + e.getMessage());
        }
    }

    private static void preSembrarPartidos(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM partidos";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Sembrando la tabla de partidos del mundial (72 partidos)...");
                
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

                int[][] enfrentamientos = {
                    {0, 1},
                    {2, 3},
                    {0, 2},
                    {1, 3},
                    {0, 3},
                    {1, 2}
                };

                String sqlInsert = "INSERT INTO partidos (grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante) VALUES (?, ?, ?, ?, -1, -1)";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                    for (int i = 0; i < 12; i++) {
                        for (int p = 0; p < 6; p++) {
                            int idxLocal = enfrentamientos[p][0];
                            int idxVisitante = enfrentamientos[p][1];
                            
                            pstmt.setString(1, grupos[i]);
                            pstmt.setInt(2, p);
                            pstmt.setString(3, equipos[i][idxLocal]);
                            pstmt.setString(4, equipos[i][idxVisitante]);
                            pstmt.addBatch();
                        }
                    }
                    pstmt.executeBatch();
                    System.out.println("Se sembraron los 72 partidos exitosamente.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al sembrar partidos: " + e.getMessage());
        }
    }
}
