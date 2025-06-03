import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import restaurantes.Restaurante;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    @FXML
    private Button menus;
    @FXML
    private Button restaurantes;
    @FXML
    private Button usuarios;
    @FXML
    private StackPane stackContentArea;

    // Referencia a la tabla cargada dinámicamente
    private TableView<Restaurante> tablaRestaurantes;

    // Lista de datos
    private ObservableList<Restaurante> listaRestaurantes = FXCollections.observableArrayList();

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización básica si es necesaria
    }

    // MÉTODOS PARA MOSTRAR LAS DIFERENTES APARTADOS
    @FXML
    void mostrarMenus(MouseEvent event) {
        changeButtonColor(menus, "#DAF7A6", "#222d3a");
        changeButtonColor(restaurantes, "#222d3a", "WHITE");
        changeButtonColor(usuarios, "#222d3a", "WHITE");
        // Aquí cargarías la vista de menús si es necesario
    }

    @FXML
    void mostrarRestaurantes(MouseEvent event) throws IOException, SQLException {
        // Cargar el FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurantesManager.fxml"));
        Parent fxml = loader.load();

        // Limpiar y mostrar el nuevo contenido
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);

        // Cambiar colores de botones
        changeButtonColor(restaurantes, "#DAF7A6", "#222d3a");
        changeButtonColor(usuarios, "#222d3a", "WHITE");
        changeButtonColor(menus, "#222d3a", "WHITE");

        // Obtener la tabla del FXML cargado
        tablaRestaurantes = (TableView<Restaurante>) fxml.lookup("#tablaRestaurantes");

        // Configurar las columnas
        configurarColumnas();

        // Cargar los datos
        cargarDatosRestaurantes();
    }

    @FXML
    void mostrarUsuarios(MouseEvent event) {
        changeButtonColor(usuarios, "#DAF7A6", "#222d3a");
        changeButtonColor(restaurantes, "#222d3a", "WHITE");
        changeButtonColor(menus, "#222d3a", "WHITE");
        // Aquí cargarías la vista de usuarios si es necesario
    }

    // MÉTODOS AUXILIARES
    private void changeButtonColor(Button boton, String hexBack, String hexText) {
        boton.setStyle("-fx-background-color: " + hexBack + "; -fx-text-fill: " + hexText + ";");
    }

    private void configurarColumnas() {
        if (tablaRestaurantes != null && !tablaRestaurantes.getColumns().isEmpty()) {
            // Configurar cada columna
            TableColumn<Restaurante, ?> columnaId = tablaRestaurantes.getColumns().get(3);
            TableColumn<Restaurante, ?> columnaNombre = tablaRestaurantes.getColumns().get(2);
            TableColumn<Restaurante, ?> columnaUbi = tablaRestaurantes.getColumns().get(1);
            TableColumn<Restaurante, ?> columnaHorario = tablaRestaurantes.getColumns().get(0);
            TableColumn<Restaurante, ?> columnaActivo = tablaRestaurantes.getColumns().get(4);

            // Asignar los PropertyValueFactory correspondientes
            columnaId.setCellValueFactory(new PropertyValueFactory<>("idRestaurante"));
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaUbi.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            columnaHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
            columnaActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        }
    }

    private void cargarDatosRestaurantes() throws SQLException {
        listaRestaurantes.clear();
        String query = "SELECT * FROM alvuelo_db.restaurantes";

        try (PreparedStatement statement = alvuelo.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Crear un nuevo objeto para cada fila
                Restaurante restaurante = new Restaurante(
                        resultSet.getString("idrestaurante"),
                        resultSet.getString("nombre"),
                        resultSet.getString("ubicacion"),
                        resultSet.getString("horario"),
                        resultSet.getBoolean("activo")
                );
                listaRestaurantes.add(restaurante);
            }

            if (tablaRestaurantes != null) {
                tablaRestaurantes.setItems(listaRestaurantes);
            }
        }
    }
}