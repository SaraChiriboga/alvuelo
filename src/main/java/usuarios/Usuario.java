package usuarios;

public class Usuario {
    String idBanner;
    String nombre;
    String correo;
    private String password;

    public Usuario(String idBanner, String nombre, String correo) {
        this.idBanner = idBanner;
        this.nombre = nombre;
        this.correo = correo;
    }
}
