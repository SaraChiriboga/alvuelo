import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenusManagerController implements Initializable {
    @FXML
    private ComboBox<String> comboRestaurantes;
    @FXML
    private ComboBox<?> comboCampus;

    @FXML
    private TextField nombre;
    @FXML
    private TextField ingredientes;
    @FXML
    private TextField precio;
    @FXML
    private TextField id;
    @FXML
    private ToggleButton estadoToggle;
    @FXML
    private Label labelRestaurante;
    @FXML
    private GridPane gridPlatos;
    @FXML
    private TabPane tabPaneMenus;

    //observable list para el combo box
    ObservableList<String> restaurantes = FXCollections.observableArrayList();

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font.loadFont(getClass().getResourceAsStream("Poppins-ExtraBold.ttf"), 24);
        try {
            llenarComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlato(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        String query = "INSERT INTO alvuelo_db.platos (id, nombre, ingredientes, precio, disponibilidad, restaurante, campus, categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = alvuelo.prepareStatement(query);

        insertStmt.setInt(1, Integer.parseInt(id.getText()));
        insertStmt.setString(2, nombre.getText());
        insertStmt.setString(3, ingredientes.getText());
        insertStmt.setDouble(4, Double.parseDouble(precio.getText()));
        boolean estado = estadoToggle.isSelected();
        insertStmt.setBoolean(5, !estado);
        insertStmt.setString(6, (String) comboRestaurantes.getSelectionModel().getSelectedItem());
        insertStmt.setString(7, "UdlaPark"); //por ahora es fijo, no hay mas campus
        insertStmt.setString(8, String.valueOf(tabPaneMenus.getSelectionModel().getSelectedItem().getText()));
        insertStmt.executeUpdate();

        clearCampos();
    }

    @FXML
    public void alternarEstado(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if (estadoToggle.isSelected()) {
            estadoToggle.setText("Agotado");
        } else {
            estadoToggle.setText("Disponible");
        }
    }

    public void llenarComboBox() throws SQLException {
        String q = "SELECT * FROM restaurantes";
        PreparedStatement statement = alvuelo.prepareStatement(q);
        ResultSet rs = statement.executeQuery();

        while(rs.next()){
            restaurantes.add(rs.getString("nombre"));
        }

        comboRestaurantes.setItems(restaurantes);
    }

    public void clearCampos(){
        id.clear();
        ingredientes.clear();
        precio.clear();
        nombre.clear();
        estadoToggle.setSelected(false);
    }
}
