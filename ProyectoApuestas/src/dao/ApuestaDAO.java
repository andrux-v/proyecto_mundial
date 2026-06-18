package dao;

import modelo.Apuesta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApuestaDAO {

    public boolean guardarApuesta(Apuesta a) {
        String sql = "INSERT INTO apuestas (usuario_documento, partido_id, goles_local, goles_visitante) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE goles_local = ?, goles_visitante = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, a.getUsuarioDocumento());
            pstmt.setInt(2, a.getPartidoId());
            pstmt.setInt(3, a.getGolesLocal());
            pstmt.setInt(4, a.getGolesVisitante());
            pstmt.setInt(5, a.getGolesLocal());
            pstmt.setInt(6, a.getGolesVisitante());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar apuesta: " + e.getMessage());
            return false;
        }
    }

    public List<Apuesta> obtenerApuestasPorUsuario(String usuarioDocumento) {
        List<Apuesta> apuestas = new ArrayList<>();
        String sql = "SELECT id, usuario_documento, partido_id, goles_local, goles_visitante FROM apuestas WHERE usuario_documento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuarioDocumento);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    apuestas.add(new Apuesta(
                        rs.getInt("id"),
                        rs.getString("usuario_documento"),
                        rs.getInt("partido_id"),
                        rs.getInt("goles_local"),
                        rs.getInt("goles_visitante")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener apuestas por usuario: " + e.getMessage());
        }
        return apuestas;
    }

    public Apuesta obtenerApuesta(String usuarioDocumento, int partidoId) {
        String sql = "SELECT id, usuario_documento, partido_id, goles_local, goles_visitante FROM apuestas WHERE usuario_documento = ? AND partido_id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuarioDocumento);
            pstmt.setInt(2, partidoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Apuesta(
                        rs.getInt("id"),
                        rs.getString("usuario_documento"),
                        rs.getInt("partido_id"),
                        rs.getInt("goles_local"),
                        rs.getInt("goles_visitante")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener apuesta: " + e.getMessage());
        }
        return null;
    }
}
