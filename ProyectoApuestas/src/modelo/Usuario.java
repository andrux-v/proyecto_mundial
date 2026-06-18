package modelo;

public class Usuario {
    private String documento;
    private String nombre;
    private int edad;
    private boolean esAdmin;
    private int puntos; // helper for leaderboard ranking

    public Usuario(String documento, String nombre, int edad, boolean esAdmin) {
        this.documento = documento;
        this.nombre = nombre;
        this.edad = edad;
        this.esAdmin = esAdmin;
        this.puntos = 0;
    }

    public Usuario(String documento, String nombre, int edad) {
        this(documento, nombre, edad, false);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
