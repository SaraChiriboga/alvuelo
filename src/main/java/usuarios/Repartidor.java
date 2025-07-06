package usuarios;

public class Repartidor extends Usuario{
    private boolean nuevo;
    private boolean modificado = false;

    public Repartidor(String idBanner, String nombre, String correo) {
        super(idBanner, nombre, correo);
    }

    public void setIdBanner(String newValue) {
        this.idBanner = newValue;
    }

    public void setNombre(String newValue) {
        this.nombre = newValue;
    }

    public void setCorreo(String newValue) {
        this.correo = newValue;
    }

    public void setNuevo(boolean b) {
        this.nuevo = b;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public String getIdBanner() {
        return idBanner;
    }

    public boolean isModificado() {
        return modificado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setModificado(boolean b) {
        this.modificado = b;
    }
}
