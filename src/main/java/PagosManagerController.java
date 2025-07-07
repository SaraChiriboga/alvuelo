import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import menus.Plato;
import pagos.*;
import pedidos.Pedido;

import java.net.URL;
import java.util.ResourceBundle;

public class PagosManagerController implements Initializable {

    @FXML
    private ComboBox<String> metodoPagoCombo;

    @FXML
    private Button confirmarPagoBtn;

    @FXML
    private Label totalLabel;

    @FXML
    private TableView<Plato> tablaCarrito;

    @FXML
    private TableColumn<Plato, String> nombrePlatoCol;

    @FXML
    private TableColumn<Plato, Double> precioPlatoCol;

    @FXML
    private Label estadoPagoLabel;

    private Pedido pedidoActual;

    @FXML
    private Label nombreRestauranteLabel;

    @FXML
    private Label nombreClienteLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        metodoPagoCombo.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
        metodoPagoCombo.getSelectionModel().selectFirst();

        // Aquí podrías inicializar la tabla
        nombrePlatoCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
        precioPlatoCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrecio()).asObject());
    }

    @FXML
    public void procesarPago(MouseEvent event) {
        String metodo = metodoPagoCombo.getValue();
        Pago pago;

        switch (metodo) {
            case "Efectivo":
                pago = new Efectivo();
                break;
            case "Tarjeta":
                pago = new Tarjeta();
                break;
            case "Transferencia":
                pago = new Transferencia();
                break;
            default:
                estadoPagoLabel.setText("Método de pago no válido.");
                return;
        }

        pago.confirmarPago(pago.procesarPago());
        if (pago.procesarPago()) {
            estadoPagoLabel.setText("Pago procesado exitosamente.");
            Factura factura = new Factura();
            factura.emitirFactura();
        } else {
            estadoPagoLabel.setText("Fallo al procesar el pago.");
        }
    }

    public void cargarPedido(Pedido pedido) {
        this.pedidoActual = pedido;
        tablaCarrito.getItems().setAll(pedido.getCarrito());
        totalLabel.setText("Total: $" + calcularTotal());
        nombreRestauranteLabel.setText(pedido.getNombre());
        nombreClienteLabel.setText("Cliente: " + pedido.getCliente().getNombre());

    }

    private double calcularTotal() {
        return tablaCarrito.getItems().stream().mapToDouble(Plato::getPrecio).sum();
    }
}
