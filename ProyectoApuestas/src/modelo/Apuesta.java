package modelo;

public class Apuesta {
    private int id;
    private String usuarioDocumento;
    private int partidoId;
    private int golesLocal;
    private int golesVisitante;

    public Apuesta(int id, String usuarioDocumento, int partidoId, int golesLocal, int golesVisitante) {
        this.id = id;
        this.usuarioDocumento = usuarioDocumento;
        this.partidoId = partidoId;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    public Apuesta(String usuarioDocumento, int partidoId, int golesLocal, int golesVisitante) {
        this(-1, usuarioDocumento, partidoId, golesLocal, golesVisitante);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioDocumento() {
        return usuarioDocumento;
    }

    public void setUsuarioDocumento(String usuarioDocumento) {
        this.usuarioDocumento = usuarioDocumento;
    }

    public int getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(int partidoId) {
        this.partidoId = partidoId;
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
