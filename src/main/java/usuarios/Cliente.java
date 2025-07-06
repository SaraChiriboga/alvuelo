package usuarios;

public class Cliente extends Usuario{
    private String rol;

    public Cliente(String idBanner, String nombre, String correo, String rol) {
        super(idBanner, nombre, correo);
        this.rol = rol;
    }

    public String getIdBanner() {
        return this.idBanner;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getCorreo() {
        return this.correo;
    }
    public String getRol() {
        return this.rol;
    }
}
