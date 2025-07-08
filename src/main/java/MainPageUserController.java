import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import pedidos.Pedido;
import servicios.EnRestaurante;
import servicios.Entrega;
import servicios.Retiro;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainPageUserController implements Initializable {
    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();
    private Pedido pedidoActual;

    @FXML
    private AnchorPane mainPane;

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
                String campus = rs.getString("campus");
                String nombre = rs.getString("nombre");

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
                            getClass().getResourceAsStream("photo-gallery.png"),
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

            }
        }

        statement.close();
        rs.close();
    }

    private void animacionEntrada() {
        // Asegurarse de que todos los nodos empiecen invisibles
        for (Node node : mainPane.getChildren()) {
            node.setOpacity(0); // Opacidad inicial en 0
        }

        // Aplicar animación a cada hijo del contenedor principal
        for (Node node : mainPane.getChildren()) {
            // FadeIn más lento (duración aumentada)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), node); // 1 segundo de fade
            fadeIn.setFromValue(0); // Empieza transparente
            fadeIn.setToValue(1);   // Termina completamente visible

            // Desplazamiento hacia arriba (más suave)
            TranslateTransition slideUp = new TranslateTransition(Duration.millis(800), node);
            slideUp.setFromY(30);  // Empieza 30 píxeles más abajo
            slideUp.setToY(0);     // Termina en su posición original

            // Retraso escalonado para un efecto secuencial
            int index = mainPane.getChildren().indexOf(node);
            fadeIn.setDelay(Duration.millis(150 * index));  // Cada elemento aparece 150ms después
            slideUp.setDelay(Duration.millis(150 * index)); // Sincronizado con el fade

            // Combinar animaciones y ejecutar
            ParallelTransition combinedAnimation = new ParallelTransition(fadeIn, slideUp);
            combinedAnimation.play();
        }
    }

    @FXML
    private void irAlCarrito(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("carrito.fxml"));
            Parent root = loader.load();

            // Aquí puedes pasar el pedido actual si lo tienes
            CarritoManagerController controller = loader.getController();
            controller.cargarCarrito(pedidoActual.getCarrito());

            stackContentArea.getChildren().clear();
            stackContentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

