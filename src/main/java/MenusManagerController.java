import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MenusManagerController implements Initializable {
    @FXML
    private ComboBox<String> comboRestaurantes;
    @FXML
    private ComboBox<String> comboCampus;

    private File imagenPlato;

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
    private GridPane gridPlatos;
    @FXML
    private TabPane tabPaneMenus;
    @FXML
    private TextField barraBusqueda;

    //observable list para el combo box
    ObservableList<String> restaurantes = FXCollections.observableArrayList();

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font.loadFont(getClass().getResourceAsStream("Poppins-ExtraBold.ttf"), 24);

        comboCampus.getItems().addAll("UdlaPark", "Granados", "Colón"); // Agrega los campus disponibles al
        comboCampus.getSelectionModel().select("UdlaPark"); // Selecciona UdlaPark por defecto

        try {
            llenarComboBox("UdlaPark"); // Llenar el combo de restaurantes por defecto
            comboRestaurantes.getSelectionModel().selectFirst(); // Selecciona el primer restaurante por defecto
            listPlatos("UdlaPark", "Chía", "Desayuno"); // Cargar platos por defecto
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        barraBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            search(null);
        });
    }

    public void addPlato(javafx.scene.input.MouseEvent mouseEvent) throws SQLException, IOException {
        String query = "INSERT INTO alvuelo_db.platos (id, nombre, ingredientes, precio, disponibilidad, restaurante, campus, categoria, imagenPlato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertStmt = alvuelo.prepareStatement(query)) {
            insertStmt.setInt(1, Integer.parseInt(id.getText()));
            insertStmt.setString(2, nombre.getText());
            insertStmt.setString(3, ingredientes.getText());
            insertStmt.setDouble(4, Double.parseDouble(precio.getText()));
            boolean estado = estadoToggle.isSelected();
            insertStmt.setBoolean(5, !estado);
            insertStmt.setString(6, (String) comboRestaurantes.getSelectionModel().getSelectedItem());
            insertStmt.setString(7, (String) comboCampus.getSelectionModel().getSelectedItem());
            insertStmt.setString(8, String.valueOf(tabPaneMenus.getSelectionModel().getSelectedItem().getText()));

            // ingreso de la imagen del plato
            if (imagenPlato != null) {
                FileInputStream fis = new FileInputStream(imagenPlato);
                insertStmt.setBinaryStream(9, fis, (int) imagenPlato.length());
            } else {
                insertStmt.setNull(9, java.sql.Types.BLOB);
            }

            insertStmt.executeUpdate();

            clearCampos();
            listPlatos(comboCampus.getSelectionModel().getSelectedItem(),
                    comboRestaurantes.getSelectionModel().getSelectedItem(),
                    tabPaneMenus.getSelectionModel().getSelectedItem().getText());
        }
    }

    public void listPlatos(String campus, String restaurante, String menu) throws SQLException {
        gridPlatos.getChildren().clear();
        gridPlatos.setAlignment(Pos.CENTER);

        // espaciado entre los platos
        gridPlatos.setHgap(15);
        gridPlatos.setVgap(30);

        String query = "SELECT * FROM alvuelo_db.platos WHERE campus = ? AND restaurante = ? AND categoria = ?";
        PreparedStatement statement = alvuelo.prepareStatement(query);
        statement.setString(1, campus);
        statement.setString(2, restaurante);
        statement.setString(3, menu);
        ResultSet rs = statement.executeQuery();

        int row = 0;
        int col = 0;

        while (rs.next()) {
            AnchorPane card = new AnchorPane();
            card.setPrefSize(140, 120);
            card.setMinSize(140, 120);
            card.setMaxSize(140, 120);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);");

            // Use VBox for vertical layout inside the card
            VBox content = new VBox(6);
            content.setPadding(new Insets(10, 8, 10, 8));
            content.setPrefWidth(124);

            Label nombrePlato = new Label(rs.getString("nombre"));
            nombrePlato.setWrapText(true);
            nombrePlato.setFont(Font.font("Poppins", FontWeight.BOLD, 14));
            nombrePlato.setMaxWidth(Double.MAX_VALUE);

            Label idPlato = new Label("ID: " + String.valueOf(rs.getInt("id")));
            idPlato.setFont(Font.font("Poppins", FontWeight.NORMAL, 10));
            idPlato.setMaxWidth(Double.MAX_VALUE);

            Label ingredientesPlato = new Label(rs.getString("ingredientes"));
            ingredientesPlato.setFont(Font.font("Poppins", FontWeight.NORMAL, 10));
            ingredientesPlato.setWrapText(true);
            ingredientesPlato.setMaxWidth(Double.MAX_VALUE);

            Label precioPlato = new Label("$" + String.format("%.2f", rs.getDouble("precio")));
            precioPlato.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 12));
            precioPlato.setTextFill(Color.web("#27ae60"));
            precioPlato.setMaxWidth(Double.MAX_VALUE);

            content.getChildren().addAll(nombrePlato, idPlato, ingredientesPlato, precioPlato);
            card.getChildren().add(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);

            gridPlatos.add(card, col, row);
            GridPane.setMargin(card, new Insets(2));

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    public void showMenu(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        listPlatos(comboCampus.getSelectionModel().getSelectedItem(),
                comboRestaurantes.getSelectionModel().getSelectedItem(),
                tabPaneMenus.getSelectionModel().getSelectedItem().getText());
    }

    @FXML
    public void alternarEstado(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if (estadoToggle.isSelected()) {
            estadoToggle.setText("Agotado");
        } else {
            estadoToggle.setText("Disponible");
        }
    }

    public void clearCampos() {
        id.clear();
        ingredientes.clear();
        precio.clear();
        nombre.clear();
        estadoToggle.setSelected(false);
    }

    public void llenarComboBox(String campus) throws SQLException {
        restaurantes.clear();
        // restaurantes dsiponibles en la base de datos
        // no se deben repetir, tomando en consideracion que hay corteza en otros campus
        String q = "SELECT * FROM restaurantes WHERE campus = ?";
        PreparedStatement statement = alvuelo.prepareStatement(q);
        statement.setString(1, campus);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            restaurantes.add(rs.getString("nombre"));
        }

        comboRestaurantes.setItems(restaurantes);
    }

    public void desplegarRestaurantesPorCampus(ActionEvent actionEvent) throws SQLException {
        String campus = (String) comboCampus.getSelectionModel().getSelectedItem();
        if (campus != null && !campus.isEmpty()) {
            llenarComboBox(campus);
        } else {
            // Si no hay campus seleccionado, puedes mostrar todos los restaurantes o manejarlo como desees
            llenarComboBox("UdlaPark"); // Por ejemplo, cargar todos los restaurantes de UdlaPark
        }

        listPlatos(comboCampus.getSelectionModel().getSelectedItem(),
                comboRestaurantes.getSelectionModel().getSelectedItem(),
                tabPaneMenus.getSelectionModel().getSelectedItem().getText()); //en caso de cambios en los combo box, actualizar la lista de platos
    }

    public void deletePlato(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deletePlatoForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Eliminar Plato");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            listPlatos(comboCampus.getSelectionModel().getSelectedItem(),
                    comboRestaurantes.getSelectionModel().getSelectedItem(),
                    tabPaneMenus.getSelectionModel().getSelectedItem().getText()); //actualización inmediata);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editPlato(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editPlatoForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Editar Plato");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            listPlatos(comboCampus.getSelectionModel().getSelectedItem(),
                    comboRestaurantes.getSelectionModel().getSelectedItem(),
                    tabPaneMenus.getSelectionModel().getSelectedItem().getText()); //actualización inmediata);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(ActionEvent actionEvent) {
        String busqueda = barraBusqueda.getText().trim();
        gridPlatos.getChildren().clear();
        gridPlatos.setAlignment(Pos.CENTER);
        gridPlatos.setHgap(15);
        gridPlatos.setVgap(30);

        String campus = (String) comboCampus.getSelectionModel().getSelectedItem();
        String restaurante = (String) comboRestaurantes.getSelectionModel().getSelectedItem();
        String categoria = tabPaneMenus.getSelectionModel().getSelectedItem().getText();

        String query = "SELECT * FROM alvuelo_db.platos WHERE campus = ? AND restaurante = ? AND categoria = ? " +
                "AND (LOWER(nombre) LIKE ? OR LOWER(ingredientes) LIKE ?)";
        try (PreparedStatement statement = alvuelo.prepareStatement(query)) {
            statement.setString(1, campus);
            statement.setString(2, restaurante);
            statement.setString(3, categoria);
            statement.setString(4, "%" + busqueda.toLowerCase() + "%");
            statement.setString(5, "%" + busqueda.toLowerCase() + "%");
            ResultSet rs = statement.executeQuery();

            int row = 0, col = 0;
            while (rs.next()) {
                AnchorPane card = new AnchorPane();
                card.setPrefSize(140, 120);
                card.setMinSize(140, 120);
                card.setMaxSize(140, 120);
                card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);");

                VBox content = new VBox(6);
                content.setPadding(new Insets(10, 8, 10, 8));
                content.setPrefWidth(124);

                Label nombrePlato = new Label(rs.getString("nombre"));
                nombrePlato.setWrapText(true);
                nombrePlato.setFont(Font.font("Poppins", FontWeight.BOLD, 14));
                nombrePlato.setMaxWidth(Double.MAX_VALUE);

                Label idPlato = new Label("ID: " + rs.getInt("id"));
                idPlato.setFont(Font.font("Poppins", FontWeight.NORMAL, 10));
                idPlato.setMaxWidth(Double.MAX_VALUE);

                Label ingredientesPlato = new Label(rs.getString("ingredientes"));
                ingredientesPlato.setFont(Font.font("Poppins", FontWeight.NORMAL, 10));
                ingredientesPlato.setWrapText(true);
                ingredientesPlato.setMaxWidth(Double.MAX_VALUE);

                Label precioPlato = new Label("$" + String.format("%.2f", rs.getDouble("precio")));
                precioPlato.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 12));
                precioPlato.setTextFill(Color.web("#27ae60"));
                precioPlato.setMaxWidth(Double.MAX_VALUE);

                content.getChildren().addAll(nombrePlato, idPlato, ingredientesPlato, precioPlato);
                card.getChildren().add(content);
                AnchorPane.setTopAnchor(content, 0.0);
                AnchorPane.setLeftAnchor(content, 0.0);
                AnchorPane.setRightAnchor(content, 0.0);
                AnchorPane.setBottomAnchor(content, 0.0);

                gridPlatos.add(card, col, row);
                GridPane.setMargin(card, new Insets(2));

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarImagenPlato(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Insertar imagen del plato");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            JOptionPane.showMessageDialog(null, "Imagen seleccionada: " + file.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo.");
        }

        imagenPlato = file; // Guardar la imagen seleccionada
    }
}
