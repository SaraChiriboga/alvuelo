package restaurantes;

import menus.Menu;

import java.util.LinkedList;
import java.util.List;

public class Restaurante{
    private String nombre;
    private String ubicacion;
    private String horario;
    private LinkedList<Menu> menus;
    private String idRestaurante;
    private boolean activo;

    public boolean modificado;
    private boolean esNuevo = false;

    public boolean isNuevo() {
        return esNuevo;
    }

    public void setNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public Restaurante() {
        super();
    }


    //constructor
    public Restaurante(String idRestaurante, String nombre, String ubicacion, String horario, boolean activo) {
        this.horario = horario;
        this.ubicacion = ubicacion;
        this.nombre = nombre;
        this.idRestaurante = idRestaurante;
        this.activo = activo;
    }

    //getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isModificado() {
        return modificado;
    }
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    //AGREGAR RESTAURANTE
    public void addRestaurante(){

    }

    //EDITAR RESTAURANTE
    public void editRestaurante(LinkedList<Restaurante> restaurantes, String id){
    }

    //HABILITAR RESTAURANTE
    public void activarRestaurante(){

    }

    //INHABILITAR RESTAURANTE
    public void desactivarRestauranteTemp(){

    }

    //ELIMINAR RESTAURANTE
    public void deleteRestaurante(){

    }
}
