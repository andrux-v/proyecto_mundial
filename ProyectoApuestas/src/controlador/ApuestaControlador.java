package controlador;

import dao.PartidoDAO;
import dao.ApuestaDAO;
import modelo.Partido;
import modelo.Apuesta;

import java.util.*;

public class ApuestaControlador {
    private PartidoDAO partidoDAO;
    private ApuestaDAO apuestaDAO;

    public ApuestaControlador() {
        this.partidoDAO = new PartidoDAO();
        this.apuestaDAO = new ApuestaDAO();
    }

    public List<Partido> obtenerPartidosGrupo(String grupo) {
        return partidoDAO.obtenerPartidosPorGrupo(grupo);
    }

    public List<Apuesta> obtenerApuestasGrupoUsuario(String grupo, String documento) {
        List<Partido> partidos = partidoDAO.obtenerPartidosPorGrupo(grupo);
        List<Apuesta> apuestas = new ArrayList<>();
        for (Partido p : partidos) {
            Apuesta a = apuestaDAO.obtenerApuesta(documento, p.getId());
            if (a == null) {
                // Return a default empty bet so the UI can bind spinners
                a = new Apuesta(documento, p.getId(), 0, 0);
            }
            apuestas.add(a);
        }
        return apuestas;
    }

    public boolean guardarApuestas(List<Apuesta> apuestas) {
        boolean exito = true;
        for (Apuesta a : apuestas) {
            if (!apuestaDAO.guardarApuesta(a)) {
                exito = false;
            }
        }
        return exito;
    }

    public boolean guardarResultadosReales(List<Partido> partidos) {
        boolean exito = true;
        for (Partido p : partidos) {
            if (!partidoDAO.actualizarResultadoReal(p.getId(), p.getGolesLocal(), p.getGolesVisitante())) {
                exito = false;
            }
        }
        return exito;
    }

    // Standings Table Row Helper
    public static class FilaPosicion {
        private String equipo;
        private int puntos;
        private int pj;
        private int pg;
        private int pe;
        private int pp;
        private int gf;
        private int gc;

        public FilaPosicion(String equipo) {
            this.equipo = equipo;
            this.puntos = 0;
            this.pj = 0;
            this.pg = 0;
            this.pe = 0;
            this.pp = 0;
            this.gf = 0;
            this.gc = 0;
        }

        public String getEquipo() { return equipo; }
        public int getPuntos() { return puntos; }
        public int getPj() { return pj; }
        public int getPg() { return pg; }
        public int getPe() { return pe; }
        public int getPp() { return pp; }
        public int getGf() { return gf; }
        public int getGc() { return gc; }
        public int getDif() { return gf - gc; }

        public void registrarPartido(int golesFavor, int golesContra) {
            this.pj++;
            this.gf += golesFavor;
            this.gc += golesContra;
            if (golesFavor > golesContra) {
                this.puntos += 3;
                this.pg++;
            } else if (golesFavor < golesContra) {
                this.pp++;
            } else {
                this.puntos += 1;
                this.pe++;
            }
        }
    }

    public List<FilaPosicion> calcularTablaPosiciones(String grupo, boolean usarResultadosReales, String usuarioDocumento) {
        List<Partido> partidos = partidoDAO.obtenerPartidosPorGrupo(grupo);
        Map<String, FilaPosicion> tabla = new HashMap<>();

        // Initialize unique teams in this group (should be exactly 4)
        for (Partido p : partidos) {
            tabla.putIfAbsent(p.getEquipoLocal(), new FilaPosicion(p.getEquipoLocal()));
            tabla.putIfAbsent(p.getEquipoVisitante(), new FilaPosicion(p.getEquipoVisitante()));
        }

        for (Partido p : partidos) {
            int gl = -1;
            int gv = -1;

            if (usarResultadosReales) {
                gl = p.getGolesLocal();
                gv = p.getGolesVisitante();
            } else {
                Apuesta a = apuestaDAO.obtenerApuesta(usuarioDocumento, p.getId());
                if (a != null) {
                    gl = a.getGolesLocal();
                    gv = a.getGolesVisitante();
                }
            }

            // Only register if match scores are set (i.e. not -1)
            if (gl != -1 && gv != -1) {
                FilaPosicion local = tabla.get(p.getEquipoLocal());
                FilaPosicion visitante = tabla.get(p.getEquipoVisitante());
                if (local != null && visitante != null) {
                    local.registrarPartido(gl, gv);
                    visitante.registrarPartido(gv, gl);
                }
            }
        }

        List<FilaPosicion> filas = new ArrayList<>(tabla.values());
        
        // Sort standings table: PTS desc, GD desc, GF desc
        filas.sort((f1, f2) -> {
            if (f1.getPuntos() != f2.getPuntos()) {
                return Integer.compare(f2.getPuntos(), f1.getPuntos());
            }
            int dif1 = f1.getDif();
            int dif2 = f2.getDif();
            if (dif1 != dif2) {
                return Integer.compare(dif2, dif1);
            }
            return Integer.compare(f2.getGf(), f1.getGf());
        });

        return filas;
    }

    // Comparison helper
    public static class PartidoComparacion {
        private String local;
        private String visitante;
        private String resultadoReal;
        private String pronostico;
        private String puntosObtenidos;

        public PartidoComparacion(String local, String visitante, String resultadoReal, String pronostico, String puntosObtenidos) {
            this.local = local;
            this.visitante = visitante;
            this.resultadoReal = resultadoReal;
            this.pronostico = pronostico;
            this.puntosObtenidos = puntosObtenidos;
        }

        public String getLocal() { return local; }
        public String getVisitante() { return visitante; }
        public String getResultadoReal() { return resultadoReal; }
        public String getPronostico() { return pronostico; }
        public String getPuntosObtenidos() { return puntosObtenidos; }
    }

    public static class InformacionEquipo {
        private String equipo;
        private String grupo;
        private List<PartidoComparacion> partidos;

        public InformacionEquipo(String equipo, String grupo) {
            this.equipo = equipo;
            this.grupo = grupo;
            this.partidos = new ArrayList<>();
        }

        public String getEquipo() { return equipo; }
        public String getGrupo() { return grupo; }
        public List<PartidoComparacion> getPartidos() { return partidos; }
    }

    public InformacionEquipo buscarEquipo(String nombreEquipo, String usuarioDocumento) {
        if (nombreEquipo == null || nombreEquipo.trim().isEmpty()) {
            return null;
        }
        nombreEquipo = nombreEquipo.trim();

        List<Partido> todosPartidos = partidoDAO.obtenerTodosLosPartidos();
        Partido coincidencia = null;
        String nombreRealEquipo = null;

        for (Partido p : todosPartidos) {
            if (p.getEquipoLocal().equalsIgnoreCase(nombreEquipo)) {
                coincidencia = p;
                nombreRealEquipo = p.getEquipoLocal();
                break;
            }
            if (p.getEquipoVisitante().equalsIgnoreCase(nombreEquipo)) {
                coincidencia = p;
                nombreRealEquipo = p.getEquipoVisitante();
                break;
            }
        }

        if (coincidencia == null) {
            return null;
        }

        InformacionEquipo info = new InformacionEquipo(nombreRealEquipo, coincidencia.getGrupo());
        List<Partido> partidosGrupo = partidoDAO.obtenerPartidosPorGrupo(coincidencia.getGrupo());
        
        for (Partido p : partidosGrupo) {
            if (p.getEquipoLocal().equals(nombreRealEquipo) || p.getEquipoVisitante().equals(nombreRealEquipo)) {
                int rl = p.getGolesLocal();
                int rv = p.getGolesVisitante();
                
                String realStr = (rl == -1 || rv == -1) ? "Por jugar" : rl + " - " + rv;
                String predStr = "Sin pronóstico";
                String ptsStr = "0";

                if (usuarioDocumento != null) {
                    Apuesta a = apuestaDAO.obtenerApuesta(usuarioDocumento, p.getId());
                    if (a != null) {
                        predStr = a.getGolesLocal() + " - " + a.getGolesVisitante();
                        
                        if (rl != -1 && rv != -1) {
                            int pl = a.getGolesLocal();
                            int pv = a.getGolesVisitante();
                            if (pl == rl && pv == rv) {
                                ptsStr = "5 (Exacto)";
                            } else {
                                int rOut = Integer.compare(rl, rv);
                                int pOut = Integer.compare(pl, pv);
                                if (rOut == pOut) {
                                    ptsStr = "3 (Ganador)";
                                }
                            }
                        }
                    }
                }

                info.getPartidos().add(new PartidoComparacion(
                    p.getEquipoLocal(),
                    p.getEquipoVisitante(),
                    realStr,
                    predStr,
                    ptsStr
                ));
            }
        }

        return info;
    }

    public List<Object[]> obtenerHistorialUsuario(String documento) {
        return apuestaDAO.obtenerHistorialPorUsuario(documento);
    }
}
