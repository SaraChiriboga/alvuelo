package menus;

import javafx.beans.property.SimpleStringProperty;

public class Producto {
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty precio;

    public Producto(String nombre, String precio) {
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleStringProperty(precio);
    }

    public String getNombre() { return nombre.get(); }
    public String getPrecio() { return precio.get(); }
}
