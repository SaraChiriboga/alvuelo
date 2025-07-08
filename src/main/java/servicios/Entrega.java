package servicios;

import pedidos.Pedido;

public class Entrega extends Pedido {
    private String campusEntrega;
    private String referenciaEntrega;
    private int codigo;
    private String nuevoEstado;
    private String eta; // Estimated Time of Arrival

    // Constructor
    public Entrega(String horario, String ubicacion, String nombre, String idRestaurante, boolean activo,
                   String campusEntrega, String referenciaEntrega, int codigo, String nuevoEstado, String eta) {
        super(horario, ubicacion, nombre, idRestaurante, activo);
        this.campusEntrega = campusEntrega;
        this.referenciaEntrega = referenciaEntrega;
        this.codigo = codigo;
        this.nuevoEstado = nuevoEstado;
        this.eta = eta;
    }

    // Constructor vacío
    public Entrega() {
        super();
        this.campusEntrega = "";
        this.referenciaEntrega = "";
        this.codigo = 0;
        this.nuevoEstado = "";
        this.eta = "";
    }

    @Override
    public void confirmarEntrega() {
        System.out.println("Pedido para entrega confirmado en " + campusEntrega + ", ETA: " + eta);
    }

    // Getters y setters


    public String getCampusEntrega() {
        return campusEntrega;
    }

    public void setCampusEntrega(String campusEntrega) {
        this.campusEntrega = campusEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
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

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }
}
