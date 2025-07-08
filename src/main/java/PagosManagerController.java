import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import menus.Plato;
import pagos.*;
import pedidos.Pedido;

import java.net.URL;
import java.util.ResourceBundle;

public class PagosManagerController implements Initializable {

    private Pedido pedidoActual;

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

    @FXML
    private Label nombreRestauranteLabel;

    @FXML
    private Label nombreClienteLabel;

    @FXML
    private VBox panelEntrega;
    @FXML
    private VBox panelEnRestaurante;
    @FXML
    private Button btnEntrega;
    @FXML
    private Button btnEnRestaurante;

    @FXML private RadioButton pagoEfectivo;
    @FXML private RadioButton pagoTarjeta;
    @FXML private RadioButton pagoTransferencia;

    private ToggleGroup grupoPago;
    private String metodoPagoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grupoPago = new ToggleGroup();

        pagoEfectivo.setToggleGroup(grupoPago);
        pagoTarjeta.setToggleGroup(grupoPago);
        pagoTransferencia.setToggleGroup(grupoPago);

        // Selecciona efectivo por defecto
        pagoEfectivo.setSelected(true);
        metodoPagoSeleccionado = "Efectivo";

        // Escucha los cambios de selección
        grupoPago.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton seleccionado = (RadioButton) newToggle;
                metodoPagoSeleccionado = seleccionado.getText(); // Guarda el texto del botón seleccionado
                System.out.println("Método de pago seleccionado: " + metodoPagoSeleccionado); // Para debug
            }
        });
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

        boolean exito = pago.procesarPago();
        pago.confirmarPago(exito);

        if (exito) {
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
    @FXML
    private void seleccionarEntrega() {
        panelEntrega.setVisible(true);
        panelEnRestaurante.setVisible(false);

        btnEntrega.setStyle("-fx-background-color: #C23939; -fx-text-fill: white;");
        btnEnRestaurante.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
    }

    @FXML
    private void seleccionarEnRestaurante() {
        panelEntrega.setVisible(false);
        panelEnRestaurante.setVisible(true);

        btnEnRestaurante.setStyle("-fx-background-color: #C23939; -fx-text-fill: white;");
        btnEntrega.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
    }

    private double calcularTotal() {
        return tablaCarrito.getItems().stream().mapToDouble(Plato::getPrecio).sum();
    }
}
