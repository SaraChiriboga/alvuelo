package pedidos;

import menus.Plato;
import restaurantes.Restaurante;
import usuarios.Usuario;

import java.util.LinkedList;

public abstract class Pedido extends Restaurante {
    private int idPedido;
    private Usuario cliente;
    private LinkedList<Plato> carrito;
    private String estado;

    public Pedido(String horario, String ubicacion, String nombre, String idRestaurante, boolean activo) {
        super(horario, ubicacion, nombre, idRestaurante, activo);
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
}
