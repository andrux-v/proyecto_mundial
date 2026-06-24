import dao.ConexionBD;
import vista.Login;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main2 {
    public static void main(String[] args) {
        // Set native Look & Feel for a clean, premium desktop aesthetic
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo establecer el Look & Feel del sistema: " + e.getMessage());
        }

        // Test database connection on startup
        System.out.println("Probando conexion con la base de datos...");
        try (java.sql.Connection conn = ConexionBD.obtenerConexion()) {
            System.out.println("Conexion exitosa con la base de datos: " + conn.getCatalog());
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "No se pudo establecer conexion con MySQL.\n" +
                "Verifique que el servidor MySQL este activo en el puerto 3306,\n" +
                "que la base de datos 'mundial_db' este creada y sembrada,\n" +
                "y que las credenciales en ConexionBD.java sean correctas.",
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch login GUI
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
