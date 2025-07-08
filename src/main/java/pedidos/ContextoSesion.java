package pedidos;

public class ContextoSesion {
    private static ContextoSesion instancia;
    private Pedido pedidoActual;

    private ContextoSesion() {}

    public static ContextoSesion getInstancia() {
        if (instancia == null) {
            instancia = new ContextoSesion();
        }
        return instancia;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public void setPedidoActual(Pedido pedido) {
        this.pedidoActual = pedido;
    }
}
