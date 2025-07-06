import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainPageUserController implements Initializable {

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    private StackPane stackContentArea;

    @FXML
    private HBox bar;

    @FXML
    private ComboBox<String> comboCampus;

    @FXML
    private ComboBox<String> comboServicio;

    @FXML
    private Label labelBienvenida;

    @FXML
    private TextField searchBar;

    @FXML
    private VBox vCont;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        comboCampus.getItems().addAll("UdlaPark", "Granados", "Colón"); // Agrega los campus disponibles al
        comboCampus.getSelectionModel().select("UdlaPark"); // Selecciona UdlaPark por defecto
        comboServicio.getItems().addAll("Pick-up", "Entrega");
        comboServicio.getSelectionModel().select("Pick-up"); // Selecciona Pick-up por defecto

        try {
            loadRestaurantes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void mostrarRestaurantesPorCampus(ActionEvent event) throws SQLException {
        loadRestaurantes();
    }

    @FXML
    void search(ActionEvent event) {

    }

    public void loadRestaurantes() throws SQLException {
        vCont.getChildren().clear();
        vCont.setAlignment(Pos.TOP_CENTER);
        vCont.setSpacing(10);

        String query = "SELECT * FROM alvuelo_db.restaurantes WHERE campus = ?";
        PreparedStatement statement = alvuelo.prepareStatement(query);
        statement.setString(1, comboCampus.getSelectionModel().getSelectedItem());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            boolean abierto = rs.getBoolean("activo");
            if (abierto) {
                // Extract idrestaurante before closing ResultSet
                String restauranteId = rs.getString("idrestaurante");

                AnchorPane card = new AnchorPane();
                card.setPrefSize(290, 115);
                card.setMinSize(290, 115);
                card.setMaxSize(290, 115);
                card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.637); -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);");

                HBox fullContent = new HBox();
                fullContent.setPadding(new Insets(10, 10, 10, 10));
                fullContent.setSpacing(10);
                fullContent.setAlignment(Pos.CENTER);

                StackPane imageContainer = new StackPane();
                imageContainer.setMinSize(103, 97);
                imageContainer.setMaxSize(103, 97);
                imageContainer.setStyle("-fx-background-color: transparent;");

                ImageView imagen = new ImageView();
                try {
                    Blob imagenBlob = rs.getBlob("logo");
                    if (imagenBlob != null) {
                        InputStream inputStream = imagenBlob.getBinaryStream();
                        Image image = new Image(inputStream);
                        imagen.setImage(image);
                        inputStream.close();
                    } else {
                        Image defaultImage = new Image(
                                getClass().getResourceAsStream("photo-gallery.png"),
                                103, 97, true, true
                        );
                        imagen.setImage(defaultImage);
                    }
                    imagen.setPreserveRatio(true);
                    imagen.setSmooth(true);
                    if (imagen.getImage().getWidth() / imagen.getImage().getHeight() > 103.0/97.0) {
                        imagen.setFitWidth(103);
                    } else {
                        imagen.setFitHeight(97);
                    }
                } catch (SQLException | IOException e) {
                    System.err.println("Error al cargar imagen: " + e.getMessage());
                    Image defaultImage = new Image(
                            getClass().getResourceAsStream("/images/photo-gallery.png"),
                            103, 97, true, true
                    );
                    imagen.setImage(defaultImage);
                    imagen.setPreserveRatio(true);
                    imagen.setFitWidth(103);
                }

                imageContainer.getChildren().add(imagen);
                HBox.setMargin(imageContainer, new Insets(0, 10, 0, 0));

                VBox content = new VBox(6);
                content.setAlignment(Pos.CENTER_LEFT);
                content.setPrefWidth(124);

                Label nombreRestaurante = new Label(rs.getString("nombre"));
                nombreRestaurante.setWrapText(true);
                nombreRestaurante.setFont(Font.font("Poppins", FontWeight.BOLD, 17));
                nombreRestaurante.setMaxWidth(Double.MAX_VALUE);

                Label ubicacion = new Label(rs.getString("ubicacion"));
                ubicacion.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
                ubicacion.setWrapText(true);
                ubicacion.setMaxWidth(Double.MAX_VALUE);

                Label horario = new Label(rs.getString("horario"));
                horario.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 12));
                horario.setMaxWidth(Double.MAX_VALUE);

                content.getChildren().addAll(nombreRestaurante, ubicacion, horario);
                fullContent.getChildren().addAll(imageContainer, content);
                card.getChildren().add(fullContent);
                vCont.getChildren().add(card);

                card.setOnMouseEntered(e -> {
                    card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 3, 3);");
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), card);
                    scaleTransition.setToX(1.01);
                    scaleTransition.play();
                });

                card.setOnMouseExited(e -> {
                    card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.637); -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);");
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), card);
                    scaleTransition.setToX(1.0);
                    scaleTransition.setToY(1.0);
                    scaleTransition.play();
                    card.setTranslateY(0);
                });

                // Use the local variable restauranteId here
                card.setOnMouseClicked(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuRestaurante.fxml"));
                        Parent fxml = loader.load();
                        MenuRestauranteController controller = loader.getController();
                        controller.setRestauranteId(restauranteId);

                        stackContentArea.getChildren().clear();
                        stackContentArea.getChildren().add(fxml);

                    } catch (IOException ex) {
                        System.err.println("Error al cargar menuRestaurante.fxml: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
            }
        }

        statement.close();
        rs.close();
    }


}

