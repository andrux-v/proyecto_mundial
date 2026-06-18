import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EjercicioPolla {

    static class Usuario {
        String nombre;
        String documento;
        int edad;
        int[][] golesLocal;
        int[][] golesVisitante;
        int puntos;

        public Usuario(String nombre, String documento, int edad) {
            this.nombre = nombre;
            this.documento = documento;
            this.edad = edad;
            this.golesLocal = new int[12][6];
            this.golesVisitante = new int[12][6];
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 6; j++) {
                    this.golesLocal[i][j] = -1;
                    this.golesVisitante[i][j] = -1;
                }
            }
            this.puntos = 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

        int[][] realGolesLocal = new int[12][6];
        int[][] realGolesVisitante = new int[12][6];

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                realGolesLocal[i][j] = -1;
                realGolesVisitante[i][j] = -1;
            }
        }

        ArrayList<Usuario> usuarios = new ArrayList<>();

        int opcion = 0;
        do {
            System.out.println("\n=======================================================");
            System.out.println("             SISTEMA DE APUESTAS - MUNDIAL             ");
            System.out.println("=======================================================");
            System.out.println("1. Ver estructura de grupos y equipos (48 equipos)");
            System.out.println("2. Registrar nuevo usuario (Nombre, Documento, Edad)");
            System.out.println("3. Registrar/Editar pronósticos de un usuario");
            System.out.println("4. Registrar resultados reales de partidos (Admin)");
            System.out.println("5. Ver ranking y puntaje de usuarios (Leaderboard)");
            System.out.println("6. Ver tabla de posiciones reales o de un usuario");
            System.out.println("7. Buscar un equipo y ver sus partidos/pronósticos");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción (1-8): ");
            
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Por favor, ingrese un número válido del menú.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n--- DISTRIBUCIÓN DE LOS 12 GRUPOS Y 48 EQUIPOS ---");
                    for (int i = 0; i < 12; i++) {
                        System.out.printf("%-10s: ", grupos[i]);
                        for (int j = 0; j < 4; j++) {
                            System.out.print(equipos[i][j]);
                            if (j < 3) System.out.print(" | ");
                        }
                        System.out.println();
                    }
                    break;

                case 2:
                    System.out.println("\n--- REGISTRAR NUEVO USUARIO ---");
                    System.out.print("Ingrese el nombre completo: ");
                    String nombre = scanner.nextLine().trim();
                    if (nombre.isEmpty()) {
                        System.out.println("El nombre no puede estar vacío.");
                        break;
                    }
                    System.out.print("Ingrese el número de documento: ");
                    String documento = scanner.nextLine().trim();
                    if (documento.isEmpty()) {
                        System.out.println("El número de documento no puede estar vacío.");
                        break;
                    }
                    Usuario usuarioExistente = buscarUsuario(usuarios, documento);
                    if (usuarioExistente != null) {
                        System.out.println("Error: Ya existe un usuario registrado con el documento: " + documento);
                        break;
                    }
                    System.out.print("Ingrese la edad: ");
                    int edad = -1;
                    if (scanner.hasNextInt()) {
                        edad = scanner.nextInt();
                        scanner.nextLine();
                        if (edad <= 0) {
                            System.out.println("La edad debe ser mayor a 0.");
                            break;
                        }
                    } else {
                        System.out.println("Entrada inválida para la edad.");
                        scanner.nextLine();
                        break;
                    }

                    usuarios.add(new Usuario(nombre, documento, edad));
                    System.out.println("¡Usuario " + nombre + " registrado con éxito!");
                    break;

                case 3:
                    System.out.println("\n--- REGISTRAR/EDITAR PRONÓSTICOS ---");
                    if (usuarios.isEmpty()) {
                        System.out.println("No hay usuarios registrados. Registre un usuario primero.");
                        break;
                    }
                    System.out.print("Ingrese el documento del usuario: ");
                    String docPronostico = scanner.nextLine().trim();
                    Usuario uPronostico = buscarUsuario(usuarios, docPronostico);
                    if (uPronostico == null) {
                        System.out.println("Usuario no encontrado.");
                        break;
                    }

                    System.out.println("\nRegistrando pronósticos para: " + uPronostico.nombre);
                    for (int i = 0; i < 12; i++) {
                        System.out.println((i + 1) + ". " + grupos[i]);
                    }
                    System.out.print("Seleccione el número del grupo (1-12): ");
                    int grupoSel = -1;
                    if (scanner.hasNextInt()) {
                        grupoSel = scanner.nextInt() - 1;
                        scanner.nextLine();
                    } else {
                        System.out.println("Entrada inválida.");
                        scanner.nextLine();
                        break;
                    }

                    if (grupoSel < 0 || grupoSel >= 12) {
                        System.out.println("Grupo no válido.");
                        break;
                    }

                    System.out.println("\nRegistrando pronósticos en " + grupos[grupoSel] + " para " + uPronostico.nombre + ":");
                    registrarGolesParaGrupo(scanner, grupoSel, uPronostico.golesLocal, uPronostico.golesVisitante, equipos, enfrentamientos);
                    System.out.println("\n¡Pronósticos guardados exitosamente para " + uPronostico.nombre + "!");
                    break;

                case 4:
                    System.out.println("\n--- REGISTRAR RESULTADOS REALES (OFICIALES) ---");
                    for (int i = 0; i < 12; i++) {
                        System.out.println((i + 1) + ". " + grupos[i]);
                    }
                    System.out.print("Seleccione el número del grupo (1-12): ");
                    int grupoRealSel = -1;
                    if (scanner.hasNextInt()) {
                        grupoRealSel = scanner.nextInt() - 1;
                        scanner.nextLine();
                    } else {
                        System.out.println("Entrada inválida.");
                        scanner.nextLine();
                        break;
                    }

                    if (grupoRealSel < 0 || grupoRealSel >= 12) {
                        System.out.println("Grupo no válido.");
                        break;
                    }

                    System.out.println("\nRegistrando resultados REALES oficiales para el " + grupos[grupoRealSel] + ":");
                    registrarGolesParaGrupo(scanner, grupoRealSel, realGolesLocal, realGolesVisitante, equipos, enfrentamientos);
                    System.out.println("\n¡Resultados oficiales reales guardados exitosamente!");
                    break;

                case 5:
                    System.out.println("\n======================================================================");
                    System.out.println("               RANKING DE USUARIOS (LEADERBOARD)                      ");
                    System.out.println("======================================================================");
                    if (usuarios.isEmpty()) {
                        System.out.println("No hay usuarios registrados en el sistema.");
                        System.out.println("======================================================================");
                        break;
                    }

                    for (Usuario u : usuarios) {
                        u.puntos = calcularPuntosUsuario(u, realGolesLocal, realGolesVisitante);
                    }

                    ArrayList<Usuario> ranking = new ArrayList<>(usuarios);
                    Collections.sort(ranking, new Comparator<Usuario>() {
                        @Override
                        public int compare(Usuario u1, Usuario u2) {
                            if (u2.puntos != u1.puntos) {
                                return Integer.compare(u2.puntos, u1.puntos);
                            }
                            return u1.nombre.compareToIgnoreCase(u2.nombre);
                        }
                    });

                    System.out.printf("%-6s | %-20s | %-12s | %-4s | %-10s%n", "Puesto", "Nombre", "Documento", "Edad", "Puntos");
                    System.out.println("----------------------------------------------------------------------");
                    int puesto = 1;
                    for (Usuario u : ranking) {
                        System.out.printf("%-6d | %-20s | %-12s | %-4d | %-10d%n", puesto++, u.nombre, u.documento, u.edad, u.puntos);
                    }
                    System.out.println("======================================================================");
                    break;

                case 6:
                    System.out.println("\n--- VER TABLA DE POSICIONES Y RESULTADOS ---");
                    System.out.println("1. Ver posiciones basadas en Resultados Reales (Oficiales)");
                    System.out.println("2. Ver posiciones basadas en Pronósticos de un Usuario");
                    System.out.print("Seleccione una opción (1-2): ");
                    int tipoTabla = -1;
                    if (scanner.hasNextInt()) {
                        tipoTabla = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Entrada inválida.");
                        scanner.nextLine();
                        break;
                    }

                    int[][] srcGolesLocal;
                    int[][] srcGolesVisitante;
                    String tituloResultados;

                    if (tipoTabla == 1) {
                        srcGolesLocal = realGolesLocal;
                        srcGolesVisitante = realGolesVisitante;
                        tituloResultados = "Resultados Oficiales (Reales)";
                    } else if (tipoTabla == 2) {
                        if (usuarios.isEmpty()) {
                            System.out.println("No hay usuarios registrados.");
                            break;
                        }
                        System.out.print("Ingrese el documento del usuario: ");
                        String docU = scanner.nextLine().trim();
                        Usuario u = buscarUsuario(usuarios, docU);
                        if (u == null) {
                            System.out.println("Usuario no encontrado.");
                            break;
                        }
                        srcGolesLocal = u.golesLocal;
                        srcGolesVisitante = u.golesVisitante;
                        tituloResultados = "Pronósticos de " + u.nombre;
                    } else {
                        System.out.println("Opción inválida.");
                        break;
                    }

                    for (int i = 0; i < 12; i++) {
                        System.out.println((i + 1) + ". " + grupos[i]);
                    }
                    System.out.print("Seleccione el número del grupo (1-12): ");
                    int gSel = -1;
                    if (scanner.hasNextInt()) {
                        gSel = scanner.nextInt() - 1;
                        scanner.nextLine();
                    } else {
                        System.out.println("Entrada inválida.");
                        scanner.nextLine();
                        break;
                    }

                    if (gSel < 0 || gSel >= 12) {
                        System.out.println("Grupo no válido.");
                        break;
                    }

                    int[] puntos = new int[4];
                    int[] pg = new int[4];
                    int[] pe = new int[4];
                    int[] pp = new int[4];
                    int[] gf = new int[4];
                    int[] gc = new int[4];

                    boolean hayDatos = false;

                    System.out.println("\n" + tituloResultados + " del " + grupos[gSel] + ":");
                    for (int p = 0; p < 6; p++) {
                        int idxLocal = enfrentamientos[p][0];
                        int idxVisitante = enfrentamientos[p][1];
                        String equipoL = equipos[gSel][idxLocal];
                        String equipoV = equipos[gSel][idxVisitante];

                        int gl = srcGolesLocal[gSel][p];
                        int gv = srcGolesVisitante[gSel][p];

                        if (gl != -1 && gv != -1) {
                            hayDatos = true;
                            System.out.println("  " + equipoL + " [" + gl + "] - [" + gv + "] " + equipoV);

                            gf[idxLocal] += gl;
                            gc[idxLocal] += gv;
                            gf[idxVisitante] += gv;
                            gc[idxVisitante] += gl;

                            if (gl > gv) {
                                puntos[idxLocal] += 3;
                                pg[idxLocal]++;
                                pp[idxVisitante]++;
                            } else if (gl < gv) {
                                puntos[idxVisitante] += 3;
                                pg[idxVisitante]++;
                                pp[idxLocal]++;
                            } else {
                                puntos[idxLocal] += 1;
                                puntos[idxVisitante] += 1;
                                pe[idxLocal]++;
                                pe[idxVisitante]++;
                            }
                        } else {
                            System.out.println("  " + equipoL + " [ - ] vs [ - ] " + equipoV + " (Sin registro)");
                        }
                    }

                    if (!hayDatos) {
                        System.out.println("\nNota: Aún no se han registrado datos para este grupo.");
                    }

                    System.out.println("\nTabla de Posiciones de " + grupos[gSel] + ":");
                    System.out.println("------------------------------------------------------------------");
                    System.out.printf("%-18s | %-3s | %-3s | %-3s | %-3s | %-3s | %-3s | %-3s%n", 
                                      "Equipo", "PTS", "PJ", "PG", "PE", "PP", "GF", "GC");
                    System.out.println("------------------------------------------------------------------");

                    int[] orden = {0, 1, 2, 3};
                    for (int x = 0; x < 4; x++) {
                        for (int y = x + 1; y < 4; y++) {
                            int difX = gf[orden[x]] - gc[orden[x]];
                            int difY = gf[orden[y]] - gc[orden[y]];
                            
                            if (puntos[orden[y]] > puntos[orden[x]] || 
                                (puntos[orden[y]] == puntos[orden[x]] && difY > difX)) {
                                int temp = orden[x];
                                orden[x] = orden[y];
                                orden[y] = temp;
                            }
                        }
                    }

                    for (int idx : orden) {
                        int pj = pg[idx] + pe[idx] + pp[idx];
                        System.out.printf("%-18s | %-3d | %-3d | %-3d | %-3d | %-3d | %-3d | %-3d%n",
                                          equipos[gSel][idx], puntos[idx], pj, pg[idx], pe[idx], pp[idx], gf[idx], gc[idx]);
                    }
                    System.out.println("------------------------------------------------------------------");
                    break;

                case 7:
                    System.out.print("\nIngrese el nombre del equipo a buscar: ");
                    String busqueda = scanner.nextLine().trim();
                    boolean encontrado = false;
                    
                    for (int i = 0; i < 12; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (equipos[i][j].equalsIgnoreCase(busqueda)) {
                                encontrado = true;
                                
                                String nombreEquipo = equipos[i][j];
                                String grupoEncontrado = grupos[i];
                                int posicionInicial = j + 1;
                                
                                System.out.println("\n--- INFORMACIÓN DEL EQUIPO BUSCADO ---");
                                System.out.println("Equipo: " + nombreEquipo);
                                System.out.println("Pertenece al: " + grupoEncontrado);
                                System.out.println("Posición inicial de sorteo: " + posicionInicial);
                                
                                System.out.print("\n¿Desea comparar con los pronósticos de un usuario? (S/N): ");
                                String compResp = scanner.nextLine().trim();
                                Usuario uComp = null;
                                if (compResp.equalsIgnoreCase("S") || compResp.equalsIgnoreCase("SI")) {
                                    if (usuarios.isEmpty()) {
                                        System.out.println("No hay usuarios registrados.");
                                    } else {
                                        System.out.print("Ingrese el documento del usuario: ");
                                        String docC = scanner.nextLine().trim();
                                        uComp = buscarUsuario(usuarios, docC);
                                        if (uComp == null) {
                                            System.out.println("Usuario no encontrado. Se mostrarán solo los resultados reales.");
                                        }
                                    }
                                }

                                if (uComp != null) {
                                    System.out.println("\nHistorial y pronósticos de " + nombreEquipo + " (Comparación con " + uComp.nombre + "):");
                                    System.out.println("------------------------------------------------------------------------------------------");
                                    System.out.printf("%-35s | %-16s | %-16s | %-8s%n", "Partido", "Resultado Real", "Pronóstico", "Puntos");
                                    System.out.println("------------------------------------------------------------------------------------------");
                                    for (int p = 0; p < 6; p++) {
                                        if (enfrentamientos[p][0] == j || enfrentamientos[p][1] == j) {
                                            int idxLocal = enfrentamientos[p][0];
                                            int idxVisitante = enfrentamientos[p][1];
                                            
                                            String eqLocal = equipos[i][idxLocal];
                                            String eqVisitante = equipos[i][idxVisitante];
                                            
                                            int realL = realGolesLocal[i][p];
                                            int realV = realGolesVisitante[i][p];
                                            int predL = uComp.golesLocal[i][p];
                                            int predV = uComp.golesVisitante[i][p];

                                            String partidoStr = eqLocal + " vs " + eqVisitante;
                                            String realStr = (realL == -1 || realV == -1) ? "Por jugar" : realL + " - " + realV;
                                            String predStr = (predL == -1 || predV == -1) ? "Sin pronóstico" : predL + " - " + predV;
                                            String ptsStr = "0";

                                            if (realL != -1 && realV != -1 && predL != -1 && predV != -1) {
                                                if (predL == realL && predV == realV) {
                                                    ptsStr = "5 (Exacto)";
                                                } else {
                                                    int rOut = Integer.compare(realL, realV);
                                                    int pOut = Integer.compare(predL, predV);
                                                    if (rOut == pOut) {
                                                        ptsStr = "3 (Ganador)";
                                                    }
                                                }
                                            }

                                            System.out.printf("%-35s | %-16s | %-16s | %-8s%n", partidoStr, realStr, predStr, ptsStr);
                                        }
                                    }
                                    System.out.println("------------------------------------------------------------------------------------------");
                                } else {
                                    System.out.println("\nHistorial de partidos y resultados REALES de " + nombreEquipo + ":");
                                    for (int p = 0; p < 6; p++) {
                                        if (enfrentamientos[p][0] == j || enfrentamientos[p][1] == j) {
                                            int idxLocal = enfrentamientos[p][0];
                                            int idxVisitante = enfrentamientos[p][1];
                                            
                                            String eqLocal = equipos[i][idxLocal];
                                            String eqVisitante = equipos[i][idxVisitante];
                                            
                                            int rl = realGolesLocal[i][p];
                                            int rv = realGolesVisitante[i][p];
                                            
                                            System.out.print("  " + eqLocal + " ");
                                            if (rl == -1) System.out.print("[ - ]");
                                            else System.out.print("[" + rl + "]");
                                            System.out.print(" vs ");
                                            if (rv == -1) System.out.print("[ - ]");
                                            else System.out.print("[" + rv + "]");
                                            System.out.println(" " + eqVisitante);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (encontrado) break;
                    }
                    
                    if (!encontrado) {
                        System.out.println("El equipo \"" + busqueda + "\" no se encuentra en ninguno de los grupos del Mundial.");
                    }
                    break;

                case 8:
                    System.out.println("\n¡Gracias por participar en la polla del mundial! Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 8);

        scanner.close();
    }

    private static Usuario buscarUsuario(ArrayList<Usuario> lista, String documento) {
        for (Usuario u : lista) {
            if (u.documento.equalsIgnoreCase(documento)) {
                return u;
            }
        }
        return null;
    }

    private static void registrarGolesParaGrupo(Scanner scanner, int grupoIdx, int[][] golesLocal, int[][] golesVisitante, String[][] equipos, int[][] enfrentamientos) {
        for (int p = 0; p < 6; p++) {
            int idxLocal = enfrentamientos[p][0];
            int idxVisitante = enfrentamientos[p][1];
            
            String equipoL = equipos[grupoIdx][idxLocal];
            String equipoV = equipos[grupoIdx][idxVisitante];

            System.out.println("\nPartido " + (p + 1) + ": " + equipoL + " vs " + equipoV);
            
            int gl = -1;
            int gv = -1;
            
            while (gl < 0) {
                System.out.print("Goles para " + equipoL + ": ");
                if (scanner.hasNextInt()) {
                    gl = scanner.nextInt();
                    scanner.nextLine();
                    if (gl < 0) System.out.println("Los goles no pueden ser negativos.");
                } else {
                    System.out.println("Por favor ingrese un número entero válido.");
                    scanner.nextLine();
                }
            }

            while (gv < 0) {
                System.out.print("Goles para " + equipoV + ": ");
                if (scanner.hasNextInt()) {
                    gv = scanner.nextInt();
                    scanner.nextLine();
                    if (gv < 0) System.out.println("Los goles no pueden ser negativos.");
                } else {
                    System.out.println("Por favor ingrese un número entero válido.");
                    scanner.nextLine();
                }
            }

            golesLocal[grupoIdx][p] = gl;
            golesVisitante[grupoIdx][p] = gv;
        }
    }

    private static int calcularPuntosUsuario(Usuario u, int[][] realGolesLocal, int[][] realGolesVisitante) {
        int total = 0;
        for (int g = 0; g < 12; g++) {
            for (int p = 0; p < 6; p++) {
                int rl = realGolesLocal[g][p];
                int rv = realGolesVisitante[g][p];
                int pl = u.golesLocal[g][p];
                int pv = u.golesVisitante[g][p];

                if (rl != -1 && rv != -1 && pl != -1 && pv != -1) {
                    if (pl == rl && pv == rv) {
                        total += 5;
                    } else {
                        int resultadoReal = Integer.compare(rl, rv);
                        int resultadoPred = Integer.compare(pl, pv);
                        if (resultadoReal == resultadoPred) {
                            total += 3;
                        }
                    }
                }
            }
        }
        return total;
    }
}