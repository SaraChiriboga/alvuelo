package menus;

import java.util.LinkedList;

public class Plato {
    private String nombre;
    private LinkedList<String> ingredientes;
    private double precio;
    private boolean disponibilidad;
    private int idPlato;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public void setCantidad(int quantity) {
    }
}