import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import menus.Plato;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoCarritoController {

    @FXML private ImageView imagenProducto;
    @FXML private Label nombreProducto;
    @FXML private Label precioProducto;
    @FXML private Label cantidadLabel;
    @FXML private Button eliminarBtn;
    @FXML private Button aumentarBtn;
    @FXML private Button disminuirBtn;
    @FXML private VBox contenedorProductos;
    @FXML private Label totalLabel;


    private Plato plato;
    private int cantidad = 1;
    private Runnable onActualizar;

    public void setPlato(Plato plato, Runnable onActualizar) {
        this.plato = plato;
        this.onActualizar = onActualizar;

        nombreProducto.setText(plato.getNombre());
        precioProducto.setText("$" + String.format("%.2f", plato.getPrecio()));
        cantidadLabel.setText(String.valueOf(cantidad));

        aumentarBtn.setOnAction(e -> {
            cantidad++;
            cantidadLabel.setText(String.valueOf(cantidad));
            onActualizar.run();
        });

        disminuirBtn.setOnAction(e -> {
            if (cantidad > 1) {
                cantidad--;
                cantidadLabel.setText(String.valueOf(cantidad));
                onActualizar.run();
            }
        });

        eliminarBtn.setOnAction(e -> {
            cantidad = 0;
            onActualizar.run(); // Indica que se debe eliminar
        });
    }

    public Plato getPlato() {
        return plato;
    }

    public int getCantidad() {
        return cantidad;
    }
    private List<ProductoCarritoController> controladoresProducto = new ArrayList<>();

    public void cargarCarrito(List<Plato> platos) {
        contenedorProductos.getChildren().clear();
        controladoresProducto.clear();

        for (Plato plato : platos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/productoCarrito.fxml"));
                HBox productoItem = loader.load();
                ProductoCarritoController controller = loader.getController();

                controller.setPlato(plato, this::actualizarTotal);
                controladoresProducto.add(controller);

                contenedorProductos.getChildren().add(productoItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        actualizarTotal();
    }
    private void actualizarTotal() {
        double total = 0.0;
        Iterator<ProductoCarritoController> it = controladoresProducto.iterator();

        while (it.hasNext()) {
            ProductoCarritoController controller = it.next();
            int cantidad = controller.getCantidad();

            if (cantidad == 0) {
                it.remove(); // Eliminado
                contenedorProductos.getChildren().removeIf(nodo ->
                        ((HBox) nodo).getChildren().contains(controller));
            } else {
                total += controller.getPlato().getPrecio() * cantidad;
            }
        }

        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }



}
