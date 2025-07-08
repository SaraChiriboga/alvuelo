import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import menus.Plato;
import pedidos.ContextoSesion;
import pedidos.Pedido;
import servicios.Retiro;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MenuRestauranteController implements Initializable {
    private String campus;
    private String nombreRestaurante;

    String nombreAux;

    LinkedList<Plato> carrito = new LinkedList<>();

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    private Pedido pedidoActual; // inicializado dinámicamente

    @FXML
    private Label nombre;

    @FXML
    private Label ubi;

    @FXML
    private Label horario;

    @FXML
    private VBox listCompleta;

    @FXML
    private ScrollPane pGeneral;

    @FXML
    private VBox listaAlmuerzos;

    @FXML
    private VBox listaBebibas;

    @FXML
    private VBox listaDesayunos;

    @FXML
    private VBox listaMeriendas;

    @FXML
    private VBox listaSnacks;

    @FXML
    private StackPane stackContentArea;

    @FXML
    private ImageView logo;

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public void setPedido(Pedido pedido) {
        this.pedidoActual = pedido;
    }
    @FXML
    private void irAlCarrito(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CarritoView.fxml"));
            Parent root = loader.load();

            CarritoManagerController controller = loader.getController();

            Pedido pedido = ContextoSesion.getInstancia().getPedidoActual(); // <- debe estar bien inicializado
            controller.cargarCarrito(pedido.getCarrito());

            stackContentArea.getChildren().setAll(root); // <- debe estar inyectado correctamente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void home(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPageUser.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);


        System.out.println(campus);
        System.out.println(nombreRestaurante);
    }

    public void loadMenus() throws SQLException {
        listCompleta.getChildren().clear();
        listCompleta.setSpacing(25);
        listaAlmuerzos.getChildren().clear();
        listaBebibas.getChildren().clear();
        listaDesayunos.getChildren().clear();
        listaMeriendas.getChildren().clear();
        listaSnacks.getChildren().clear();

        loadCategoria("Desayuno");
        loadCategoria("Almuerzo");
        loadCategoria("Merienda");
        loadCategoria("Snacks");
        loadCategoria("Bebidas");

        listCompleta.getChildren().addAll(listaDesayunos, listaAlmuerzos, listaMeriendas, listaSnacks, listaBebibas);
    }

    public void loadCategoria(String cat) throws SQLException {
        VBox selectedList = null;
        if(cat.equals("Desayuno")){
            selectedList = listaDesayunos;
        } else if(cat.equals("Almuerzo")){
            selectedList = listaAlmuerzos;
        } else if(cat.equals("Merienda")){
            selectedList = listaMeriendas;
        } else if(cat.equals("Snacks")){
            selectedList = listaSnacks;
        } else if(cat.equals("Bebidas")){
            selectedList = listaBebibas;
        } else {
            System.out.println("Categoría no válida: " + cat);
            return;
        }

        selectedList.getChildren().clear();
        selectedList.setAlignment(Pos.TOP_LEFT);
        selectedList.setSpacing(10);

        // Título de menú
        Label title = new Label(cat);
        title.setFont(Font.font("Poppins", FontWeight.EXTRA_BOLD, 20));
        title.setTextFill(Color.web("#333333"));

        // Separador
        Separator separator = new Separator();
        separator.setMinWidth(305);
        separator.setMaxWidth(305);
        separator.setPrefWidth(305);
        separator.setStyle("-fx-background-color: white; -fx-opacity: 0.7");
        selectedList.getChildren().addAll(title, separator);

        String query = "SELECT * FROM alvuelo_db.platos WHERE campus = ? AND restaurante = ? AND categoria = ?";
        PreparedStatement statement = alvuelo.prepareStatement(query);
        statement.setString(1, campus);
        statement.setString(2, nombreRestaurante);
        statement.setString(3, cat);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            nombreAux = rs.getString("restaurante");

            int plateId = rs.getInt("id"); // Obtener ID del plato
            boolean disponible = rs.getBoolean("disponibilidad");

            AnchorPane card = new AnchorPane();
            card.setPrefSize(305, 150); // Aumenté la altura para los controles
            card.setMinSize(305, 150);
            card.setMaxSize(305, 175);
            card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.637); -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);");

            HBox fullContent = new HBox();
            fullContent.setPadding(new Insets(10, 10, 10, 10));
            fullContent.setSpacing(10);
            fullContent.setAlignment(Pos.CENTER);

            // Contenedor de imagen (código existente)
            StackPane imageContainer = new StackPane();
            imageContainer.setMinSize(87, 80);
            imageContainer.setMaxSize(87, 80);
            imageContainer.setStyle("-fx-background-color: transparent;");

            ImageView imagen = new ImageView();
            try {
                Blob imagenBlob = rs.getBlob("imagenPlato");
                if (imagenBlob != null) {
                    InputStream inputStream = imagenBlob.getBinaryStream();
                    Image image = new Image(inputStream);
                    imagen.setImage(image);
                    inputStream.close();
                } else {
                    Image defaultImage = new Image(
                            getClass().getResourceAsStream("photo-gallery.png"),
                            87, 80, true, true
                    );
                    imagen.setImage(defaultImage);
                }
                imagen.setPreserveRatio(true);
                imagen.setSmooth(true);
                if (imagen.getImage().getWidth() / imagen.getImage().getHeight() > 87.0/80.0) {
                    imagen.setFitWidth(87);
                } else {
                    imagen.setFitHeight(80);
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error al cargar imagen: " + e.getMessage());
                Image defaultImage = new Image(
                        getClass().getResourceAsStream("photo-gallery.png"),
                        87, 80, true, true
                );
                imagen.setImage(defaultImage);
                imagen.setPreserveRatio(true);
                imagen.setFitWidth(87);
            }

            imageContainer.getChildren().add(imagen);
            HBox.setMargin(imageContainer, new Insets(0, 10, 0, 0));

            VBox content = new VBox(6);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPrefWidth(234);

            // Información del plato
            Label nombrePlato = new Label(rs.getString("nombre"));
            nombrePlato.setWrapText(true);
            nombrePlato.setFont(Font.font("Poppins", FontWeight.BOLD, 14));
            nombrePlato.setMaxWidth(Double.MAX_VALUE);

            Label ingredientes = new Label(rs.getString("ingredientes"));
            ingredientes.setFont(Font.font("Poppins", FontWeight.NORMAL, 12));
            ingredientes.setWrapText(true);
            ingredientes.setMaxWidth(150);

            Label disponibilidad = new Label(disponible ? "En stock" : "Agotado");
            disponibilidad.setStyle(disponible ? "-fx-text-fill: green;" : "-fx-text-fill: #F44336;");
            disponibilidad.setFont(Font.font("Poppins", FontWeight.NORMAL, 12));

            Label precio = new Label(String.format("$%.2f", rs.getDouble("precio")));
            double precioValue = rs.getDouble("precio");
            precio.setFont(Font.font("Poppins Bold", FontWeight.BOLD, 12));

            // Controles de cantidad y carrito
            HBox controlsBox = new HBox(5);
            controlsBox.setAlignment(Pos.CENTER);
            controlsBox.setPadding(new Insets(5, 0, 0, 0));

            // Botón para disminuir cantidad
            Button minusBtn = new Button("-");
            minusBtn.getStyleClass().add("quantity-btn");
            minusBtn.setDisable(!disponible);

            // Label para cantidad
            Label quantityLabel = new Label("1");
            quantityLabel.getStyleClass().add("quantity-label");

            // Botón para aumentar cantidad
            Button plusBtn = new Button("+");
            plusBtn.getStyleClass().add("quantity-btn");
            plusBtn.setDisable(!disponible);

            // Botón para agregar al carrito
            Button addBtn = new Button(disponible ? "Agregar" : "Agotado");
            addBtn.getStyleClass().add(disponible ? "add-to-cart-btn" : "disabled-btn");
            addBtn.setDisable(!disponible);

            // Event handlers
            minusBtn.setOnAction(e -> {
                int qty = Integer.parseInt(quantityLabel.getText());
                if (qty > 1) {
                    quantityLabel.setText(String.valueOf(qty - 1));
                }
            });

            plusBtn.setOnAction(e -> {
                int qty = Integer.parseInt(quantityLabel.getText());
                quantityLabel.setText(String.valueOf(qty + 1));
            });

            addBtn.setOnAction(e -> {
                int qty = Integer.parseInt(quantityLabel.getText());
                addToCart(plateId, nombrePlato.getText(), precioValue, qty);

                // Feedback visual
                addBtn.setText("✓ Listo!");
                addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                // Reset después de 2 segundos
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(evt -> {
                    if (disponible) {
                        addBtn.setText("Agregar");
                        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    }
                });
                pause.play();
            });

            controlsBox.getChildren().addAll(minusBtn, quantityLabel, plusBtn, addBtn);

            // Agregar todos los componentes al content
            content.getChildren().addAll(nombrePlato, ingredientes, disponibilidad, precio, controlsBox);
            fullContent.getChildren().addAll(imageContainer, content);
            card.getChildren().add(fullContent);

            AnchorPane.setTopAnchor(fullContent, 0.0);
            AnchorPane.setLeftAnchor(fullContent, 0.0);
            AnchorPane.setRightAnchor(fullContent, 0.0);
            AnchorPane.setBottomAnchor(fullContent, 0.0);

            selectedList.getChildren().add(card);

            // Efectos hover
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

        statement.close();
        rs.close();
    }

    // Metodo para agregar al carrito (implementación básica)
    private void addToCart(int plateId, String nombre, double precio, int quantity) {
        // Si no hay pedido aún, se crea uno del tipo que desees (por ahora Retiro como ejemplo)
        if (pedidoActual == null) {
            pedidoActual = new Retiro(); // o Entrega o EnRestaurante, según el default
            pedidoActual.setNombreRestaurante(nombreRestaurante);
            pedidoActual.setCampus(campus);
            ContextoSesion.getInstancia().setPedidoActual(pedidoActual); // ✅ GUARDA EN CONTEXTO
        }

        // Crear el plato con la cantidad correspondiente
        Plato item = new Plato();
        item.setIdPlato(plateId);
        item.setNombre(nombre);
        item.setPrecio(precio);
        item.setCantidad(quantity);

        // Añadir al pedido
        pedidoActual.agregarPlato(item); // Asegúrate que esta función exista en Pedido

        System.out.println("Agregado al pedido: " + item.getNombre() + " x" + quantity);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadMenus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void scrollToDesayunos(MouseEvent event) {
        if (listaDesayunos != null && pGeneral != null) {
            // Calcular la posición Y relativa dentro del contenido
            Bounds bounds = listaDesayunos.getBoundsInParent();
            double viewportHeight = pGeneral.getViewportBounds().getHeight();
            double contentHeight = pGeneral.getContent().getBoundsInLocal().getHeight();

            // Calcular el valor de vvalue (0.0 a 1.0)
            double targetVvalue = bounds.getMinY() / (contentHeight - viewportHeight);

            // Aplicar el scroll con animación suave
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(pGeneral.vvalueProperty(), targetVvalue)
                    ));
            timeline.play();
        }
    }

    public void scrollToAlmuerzos(MouseEvent event) {
        if (listaAlmuerzos != null && pGeneral != null) {
            // Calcular la posición Y relativa dentro del contenido
            Bounds bounds = listaAlmuerzos.getBoundsInParent();
            double viewportHeight = pGeneral.getViewportBounds().getHeight();
            double contentHeight = pGeneral.getContent().getBoundsInLocal().getHeight();

            // Calcular el valor de vvalue (0.0 a 1.0)
            double targetVvalue = bounds.getMinY() / (contentHeight - viewportHeight);

            // Aplicar el scroll con animación suave
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(pGeneral.vvalueProperty(), targetVvalue)
                    ));
            timeline.play();
        }
    }

    public void scrollToMeriendas(MouseEvent event) {
        if (listaMeriendas != null && pGeneral != null) {
            // Calcular la posición Y relativa dentro del contenido
            Bounds bounds = listaMeriendas.getBoundsInParent();
            double viewportHeight = pGeneral.getViewportBounds().getHeight();
            double contentHeight = pGeneral.getContent().getBoundsInLocal().getHeight();

            // Calcular el valor de vvalue (0.0 a 1.0)
            double targetVvalue = bounds.getMinY() / (contentHeight - viewportHeight);

            // Aplicar el scroll con animación suave
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(pGeneral.vvalueProperty(), targetVvalue)
                    ));
            timeline.play();
        }
    }

    public void scrollToSnacks(MouseEvent event) {
        if (listaSnacks != null && pGeneral != null) {
            // Calcular la posición Y relativa dentro del contenido
            Bounds bounds = listaSnacks.getBoundsInParent();
            double viewportHeight = pGeneral.getViewportBounds().getHeight();
            double contentHeight = pGeneral.getContent().getBoundsInLocal().getHeight();

            // Calcular el valor de vvalue (0.0 a 1.0)
            double targetVvalue = bounds.getMinY() / (contentHeight - viewportHeight);

            // Aplicar el scroll con animación suave
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(pGeneral.vvalueProperty(), targetVvalue)
                    ));
            timeline.play();
        }
    }

    public void scrollToBebidas(MouseEvent event) {
        if (listaBebibas != null && pGeneral != null) {
            // Calcular la posición Y relativa dentro del contenido
            Bounds bounds = listaBebibas.getBoundsInParent();
            double viewportHeight = pGeneral.getViewportBounds().getHeight();
            double contentHeight = pGeneral.getContent().getBoundsInLocal().getHeight();

            // Calcular el valor de vvalue (0.0 a 1.0)
            double targetVvalue = bounds.getMinY() / (contentHeight - viewportHeight);

            // Aplicar el scroll con animación suave
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(pGeneral.vvalueProperty(), targetVvalue)
                    ));
            timeline.play();
        }
    }

    public void loadLogo(String restaurante) {
        String query = "SELECT logo FROM alvuelo_db.restaurantes WHERE nombre = ?";
        try (PreparedStatement statement = alvuelo.prepareStatement(query)) {
            statement.setString(1, restaurante);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                try {
                    Blob imagenBlob = rs.getBlob("logo");
                    if (imagenBlob != null) {
                        InputStream inputStream = imagenBlob.getBinaryStream();
                        Image image = new Image(inputStream);
                        logo.setImage(image);
                        inputStream.close();
                    } else {
                        Image defaultImage = new Image(
                                getClass().getResourceAsStream("photo-gallery.png"),
                                92, 84, true, true
                        );
                        logo.setImage(defaultImage);
                    }
                } catch (SQLException | IOException e) {
                    System.err.println("Error al cargar imagen: " + e.getMessage());
                    Image defaultImage = new Image(
                            getClass().getResourceAsStream("photo-gallery.png"),
                            92, 84, true, true
                    );
                    logo.setImage(defaultImage);
                }
                // Always preserve aspect ratio and smooth
                logo.setPreserveRatio(true);
                logo.setSmooth(true);
                // Set fit size
                logo.setFitWidth(92);
                logo.setFitHeight(84);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadInfo(String nombreRestaurante){
        String sql = "SELECT * FROM alvuelo_db.restaurantes WHERE nombre = ?";
        try (PreparedStatement statement = alvuelo.prepareStatement(sql)) {
            statement.setString(1, nombreRestaurante);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                nombre.setText(rs.getString("nombre"));
                ubi.setText(rs.getString("ubicacion"));
                horario.setText(rs.getString("horario"));
            } else {
                System.out.println("No se encontró el restaurante: " + nombreRestaurante);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar información del restaurante: " + e.getMessage());
        }
    }

<<<<<<< HEAD
    @FXML
    private void irAlCarrito(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CarritoView.fxml"));
            Parent root = loader.load();

            PagosManagerController controller = loader.getController();
            Pedido pedido = ContextoSesion.getInstancia().getPedidoActual();
            controller.cargarPedido(pedido);
            stackContentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

=======
>>>>>>> 859006d9303c23df2d6deffcf86eb6c550f80994
}
