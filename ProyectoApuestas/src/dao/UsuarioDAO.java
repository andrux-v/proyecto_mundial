package dao;

import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario buscarUsuarioPorDocumento(String documento) {
        String sql = "SELECT documento, nombre, edad, es_admin, contrasena FROM usuarios WHERE documento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, documento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getBoolean("es_admin"),
                        rs.getString("contrasena")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por documento: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios (documento, nombre, edad, es_admin, contrasena) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, u.getDocumento());
            pstmt.setString(2, u.getNombre());
            pstmt.setInt(3, u.getEdad());
            pstmt.setBoolean(4, u.isEsAdmin());
            pstmt.setString(5, u.getContrasena());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT documento, nombre, edad, es_admin, contrasena FROM usuarios";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getString("documento"),
                    rs.getString("nombre"),
                    rs.getInt("edad"),
                    rs.getBoolean("es_admin"),
                    rs.getString("contrasena")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuarios SET nombre = ?, edad = ?, es_admin = ?, contrasena = ? WHERE documento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, u.getNombre());
            pstmt.setInt(2, u.getEdad());
            pstmt.setBoolean(3, u.isEsAdmin());
            pstmt.setString(4, u.getContrasena());
            pstmt.setString(5, u.getDocumento());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
