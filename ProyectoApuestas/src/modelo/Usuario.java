package modelo;

public class Usuario {
    private String documento;
    private String nombre;
    private int edad;
    private boolean esAdmin;
    private String contrasena;
    private int puntos; // helper for leaderboard ranking

    public Usuario(String documento, String nombre, int edad, boolean esAdmin, String contrasena) {
        this.documento = documento;
        this.nombre = nombre;
        this.edad = edad;
        this.esAdmin = esAdmin;
        this.contrasena = contrasena;
        this.puntos = 0;
    }

    public Usuario(String documento, String nombre, int edad, boolean esAdmin) {
        this(documento, nombre, edad, esAdmin, "12345");
    }

    public Usuario(String documento, String nombre, int edad) {
        this(documento, nombre, edad, false, "12345");
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
