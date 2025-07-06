package usuarios;

public class Admin extends Usuario {
    private boolean modificado = false;
    private boolean nuevo = false;
    public Admin(String idBanner, String nombre, String correo) {
        super(idBanner, nombre, correo);
    }

    // Métodos getters necesarios
    public String getIdBanner() {
        return this.idBanner;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getCorreo() {
        return this.correo;
    }

    // Métodos setters (los que ya tienes)
    public void setIdBanner(String idBanner) {
        this.idBanner = idBanner;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setModificado(boolean b) {
        this.modificado = b;
    }

    public boolean isModificado() {
        return this.modificado;
    }

    public boolean isNuevo() {
        return this.nuevo;
    }

    public void setNuevo(boolean b) {
        this.nuevo = b;
    }
}
