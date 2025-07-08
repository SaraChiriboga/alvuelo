package servicios;

import pedidos.Pedido;

public class Retiro extends Pedido {

    private String campusRetiro;
    private int codigo;
    private String nuevoEstado;

    public Retiro() {
        super();
    }

    // Getters y setters
    public String getCampusRetiro() {
        return campusRetiro;
    }

    public void setCampusRetiro(String campusRetiro) {
        this.campusRetiro = campusRetiro;
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
        // Implementación específica para retiro
        System.out.println("Pedido para retiro confirmado en " + campusRetiro);
    }
}
