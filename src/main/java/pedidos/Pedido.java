package pedidos;

import menus.Plato;
import restaurantes.Restaurante;
import usuarios.Usuario;

import java.util.LinkedList;

public abstract class Pedido extends Restaurante {
    private int idPedido;
    private Usuario cliente;
    private LinkedList<Plato> carrito = new LinkedList<>();
    private String estado;
    private String campus;
    private String nombreRestaurante;

    public Pedido(String horario, String ubicacion, String nombre, String idRestaurante, boolean activo) {
        super(horario, ubicacion, nombre, idRestaurante, activo);
    }

    public Pedido() {
        super();
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public String getCampus() {
        return campus;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public LinkedList<Plato> getCarrito() {
        return carrito;
    }

    //REALIZAR PEDIDO
    public void realizarPedido(){

    }

    //ACTUALIZAR ESTADO DE PEDIDO
    public String updateEstado(int idPedido){
        return null;
    }

    //TIEMPO ESTIMADO
    public void tiempoEstimado(){

    }

    //GENERAR CODIGO DE PEDIDO
    public void generateCodigo(){

    }

    //VISUALIZAR PEDIDO
    public void visualizarPedido(){

    }
    public abstract void confirmarEntrega();

    public void agregarPlato(Plato p) {
        carrito.add(p);
    }
    public int generarCodigo() {
        return (int) (Math.random() * 9000) + 1000; // Genera un número entre 1000 y 9999
    }

}
