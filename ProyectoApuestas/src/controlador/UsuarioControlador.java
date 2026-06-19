package controlador;

import dao.UsuarioDAO;
import dao.PartidoDAO;
import dao.ApuestaDAO;
import modelo.Usuario;
import modelo.Partido;
import modelo.Apuesta;

import java.util.List;

public class UsuarioControlador {
    private UsuarioDAO usuarioDAO;
    private PartidoDAO partidoDAO;
    private ApuestaDAO apuestaDAO;

    public UsuarioControlador() {
        this.usuarioDAO = new UsuarioDAO();
        this.partidoDAO = new PartidoDAO();
        this.apuestaDAO = new ApuestaDAO();
    }

    public Usuario iniciarSesion(String documento, String contrasena) {
        if (documento == null || documento.trim().isEmpty() || contrasena == null) {
            return null;
        }
        documento = documento.trim();
        if (!documento.matches("\\d+")) {
            return null; // Solo se permiten números
        }
        Usuario u = usuarioDAO.buscarUsuarioPorDocumento(documento);
        if (u != null && u.getContrasena().equals(contrasena)) {
            return u;
        }
        return null;
    }

    public boolean registrarUsuario(String documento, String nombre, int edad, boolean esAdmin, String contrasena) {
        if (documento == null || documento.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() || edad <= 0 ||
            contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }
        
        documento = documento.trim();
        if (!documento.matches("\\d+")) {
            return false; // Solo se permiten números
        }
        
        nombre = nombre.trim();
        contrasena = contrasena.trim();

        // Verificar si el usuario ya existe
        if (usuarioDAO.buscarUsuarioPorDocumento(documento) != null) {
            return false;
        }

        Usuario nuevo = new Usuario(documento, nombre, edad, esAdmin, contrasena);
        return usuarioDAO.registrarUsuario(nuevo);
    }

    public List<Usuario> obtenerRanking() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
        usuarios.removeIf(Usuario::isEsAdmin);
        List<Partido> partidos = partidoDAO.obtenerTodosLosPartidos();
        
        for (Usuario u : usuarios) {
            List<Apuesta> apuestas = apuestaDAO.obtenerApuestasPorUsuario(u.getDocumento());
            int puntos = calcularPuntos(partidos, apuestas);
            u.setPuntos(puntos);
        }

        // Ordenar: Puntos de forma descendente, y en caso de empate, Nombre de forma ascendente
        usuarios.sort((u1, u2) -> {
            if (u2.getPuntos() != u1.getPuntos()) {
                return Integer.compare(u2.getPuntos(), u1.getPuntos());
            }
            return u1.getNombre().compareToIgnoreCase(u2.getNombre());
        });

        return usuarios;
    }

    private int calcularPuntos(List<Partido> partidos, List<Apuesta> apuestas) {
        int totalPuntos = 0;
        for (Partido p : partidos) {
            // Si el partido no tiene resultado real registrado, omitir
            if (p.getGolesLocal() == -1 || p.getGolesVisitante() == -1) {
                continue;
            }
            
            // Buscar si el usuario hizo una apuesta para este partido
            Apuesta a = buscarApuestaEnLista(apuestas, p.getId());
            if (a != null) {
                int rl = p.getGolesLocal();
                int rv = p.getGolesVisitante();
                int pl = a.getGolesLocal();
                int pv = a.getGolesVisitante();

                if (pl == rl && pv == rv) {
                    totalPuntos += 5; // Exacto
                } else {
                    int resultadoReal = Integer.compare(rl, rv);
                    int resultadoPred = Integer.compare(pl, pv);
                    if (resultadoReal == resultadoPred) {
                        totalPuntos += 3; // Acierta ganador/empate
                    }
                }
            }
        }
        return totalPuntos;
    }

    private Apuesta buscarApuestaEnLista(List<Apuesta> apuestas, int partidoId) {
        for (Apuesta a : apuestas) {
            if (a.getPartidoId() == partidoId) {
                return a;
            }
        }
        return null;
    }
}
