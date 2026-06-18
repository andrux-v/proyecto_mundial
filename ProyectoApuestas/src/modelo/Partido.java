package modelo;

public class Partido {
    private int id;
    private String grupo;
    private int partidoNro;
    private String equipoLocal;
    private String equipoVisitante;
    private int golesLocal; // -1 means not played yet
    private int golesVisitante; // -1 means not played yet

    public Partido(int id, String grupo, int partidoNro, String equipoLocal, String equipoVisitante, int golesLocal, int golesVisitante) {
        this.id = id;
        this.grupo = grupo;
        this.partidoNro = partidoNro;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    public Partido(String grupo, int partidoNro, String equipoLocal, String equipoVisitante) {
        this(-1, grupo, partidoNro, equipoLocal, equipoVisitante, -1, -1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getPartidoNro() {
        return partidoNro;
    }

    public void setPartidoNro(int partidoNro) {
        this.partidoNro = partidoNro;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }
}
