package servicios;

import restaurantes.Restaurante;
import pedidos.Pedido;

public class EnRestaurante extends Pedido {
    private int numPersonas;
    private Restaurante restaurante;
    private int codigo;
    private String nuevoEstado;

    public EnRestaurante(String horario, String ubicacion, String nombre, String idRestaurante, boolean activo,
                         int numPersonas, Restaurante restaurante, int codigo, String nuevoEstado) {
        super(horario, ubicacion, nombre, idRestaurante, activo);
        this.numPersonas = numPersonas;
        this.restaurante = restaurante;
        this.codigo = codigo;
        this.nuevoEstado = nuevoEstado;
    }
    public EnRestaurante() {
        super();
        this.numPersonas = 0;
        this.codigo = 0;
        this.nuevoEstado = "";
    }

    // Getters y setters


    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    @Override
    public void confirmarEntrega() {
        System.out.println("Pedido para consumir en restaurante confirmado con código: " + codigo);
    }
}
