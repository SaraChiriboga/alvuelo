
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import menus.Plato;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CarritoManagerController implements Initializable {

    @FXML
    private TableView<Plato> tablaCarrito;

    @FXML
    private TableColumn<Plato, String> nombreCol;

    @FXML
    private TableColumn<Plato, Double> precioCol;

    @FXML
    private Label totalLabel;

    @FXML
    private Button eliminarPlatoBtn;

    @FXML
    private Button continuarPagoBtn;

    private LinkedList<Plato> carrito = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nombreCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
        precioCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrecio()).asObject());

        actualizarTabla();
    }

    public void cargarCarrito(LinkedList<Plato> carrito) {
        this.carrito = carrito;
        actualizarTabla();
    }

    private void actualizarTabla() {
        tablaCarrito.getItems().setAll(carrito);
        double total = carrito.stream().mapToDouble(Plato::getPrecio).sum();
        totalLabel.setText("Total: $" + total);
    }

    @FXML
    public void eliminarPlatoSeleccionado() {
        Plato seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            carrito.remove(seleccionado);
            actualizarTabla();
        }
    }

    @FXML
    public void continuarAPago() {
        System.out.println("Continuar a la pantalla de pagos");
        // faltafaltaa
    }

}
