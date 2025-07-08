
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import menus.Plato;

import java.io.IOException;
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

    @FXML
    private StackPane stackContentArea;

    @FXML
    private ComboBox<String> comboMetodoPago;

    @FXML
    private Label mensajeTarjeta;

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
    private void verificarMetodoPago() {
        String metodo = comboMetodoPago.getValue();
        if ("Tarjeta".equals(metodo)) {
            mensajeTarjeta.setVisible(true);
        } else {
            mensajeTarjeta.setVisible(false);
        }
    }
    @FXML
    private void continuarAPago() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("seguimientoEntrega.fxml"));
            Parent root = loader.load();
            stackContentArea.getChildren().setAll(root);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
