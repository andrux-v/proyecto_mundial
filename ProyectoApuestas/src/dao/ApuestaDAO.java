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

    public List<Object[]> obtenerHistorialPorUsuario(String usuarioDocumento) {
        List<Object[]> historial = new ArrayList<>();
        String sql = "SELECT h.fecha_registro, h.tipo_accion, p.equipo_local, p.equipo_visitante, h.goles_local, h.goles_visitante " +
                     "FROM historial_apuestas h " +
                     "JOIN partidos p ON h.partido_id = p.id " +
                     "WHERE h.usuario_documento = ? " +
                     "ORDER BY h.fecha_registro DESC";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuarioDocumento);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    historial.add(new Object[]{
                        rs.getTimestamp("fecha_registro"),
                        rs.getString("tipo_accion"),
                        rs.getString("equipo_local"),
                        rs.getString("equipo_visitante"),
                        rs.getInt("goles_local"),
                        rs.getInt("goles_visitante")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial de apuestas: " + e.getMessage());
        }
        return historial;
    }
}
