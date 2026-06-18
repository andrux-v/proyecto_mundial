package dao;

import modelo.Partido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAO {

    public List<Partido> obtenerPartidosPorGrupo(String grupo) {
        List<Partido> partidos = new ArrayList<>();
        String sql = "SELECT id, grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante FROM partidos WHERE grupo = ? ORDER BY partido_nro";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, grupo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    partidos.add(new Partido(
                        rs.getInt("id"),
                        rs.getString("grupo"),
                        rs.getInt("partido_nro"),
                        rs.getString("equipo_local"),
                        rs.getString("equipo_visitante"),
                        rs.getInt("goles_local"),
                        rs.getInt("goles_visitante")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener partidos por grupo: " + e.getMessage());
        }
        return partidos;
    }

    public List<Partido> obtenerTodosLosPartidos() {
        List<Partido> partidos = new ArrayList<>();
        String sql = "SELECT id, grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante FROM partidos ORDER BY grupo, partido_nro";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                partidos.add(new Partido(
                    rs.getInt("id"),
                    rs.getString("grupo"),
                    rs.getInt("partido_nro"),
                    rs.getString("equipo_local"),
                    rs.getString("equipo_visitante"),
                    rs.getInt("goles_local"),
                    rs.getInt("goles_visitante")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los partidos: " + e.getMessage());
        }
        return partidos;
    }

    public Partido obtenerPartidoPorId(int id) {
        String sql = "SELECT id, grupo, partido_nro, equipo_local, equipo_visitante, goles_local, goles_visitante FROM partidos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Partido(
                        rs.getInt("id"),
                        rs.getString("grupo"),
                        rs.getInt("partido_nro"),
                        rs.getString("equipo_local"),
                        rs.getString("equipo_visitante"),
                        rs.getInt("goles_local"),
                        rs.getInt("goles_visitante")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener partido por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarResultadoReal(int partidoId, int golesLocal, int golesVisitante) {
        String sql = "UPDATE partidos SET goles_local = ?, goles_visitante = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, golesLocal);
            pstmt.setInt(2, golesVisitante);
            pstmt.setInt(3, partidoId);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar resultado real del partido: " + e.getMessage());
            return false;
        }
    }
}
