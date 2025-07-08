import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    @FXML
    private Button menus;
    @FXML
    private Button restaurantes;
    @FXML
    private Button usuarios;
    @FXML
    public StackPane stackContentArea;
    @FXML
    public StackPane stack;
    @FXML
    private Button cerrar;
    @FXML
    private Button minimizar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font.loadFont(getClass().getResourceAsStream("Poppins-ExtraBold.ttf"), 24);
    }

    // ============================================= //
    // MÉTODOS PARA MOSTRAR LAS DIFERENTES APARTADOS //
    // ============================================= //
    @FXML
    void mostrarMenus(MouseEvent event) throws IOException {
        changeButtonColor(menus, "#c23939", "WHITE");
        changeButtonColor(restaurantes, "#CB5353", "WHITE");
        changeButtonColor(usuarios, "#CB5353", "WHITE");

        // Cargar el FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menusManager.fxml"));
        Parent fxml = loader.load();
        // Limpiar y mostrar el nuevo contenido
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }

    @FXML
    void mostrarRestaurantes(MouseEvent event) throws IOException, SQLException {
        // Cargar el FXML dashboard de restaurantes
        FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurantesManager.fxml"));
        Parent fxml = null;
        try {
            fxml = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Limpiar y mostrar el nuevo contenido
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
        // Cambiar colores de botones
        changeButtonColor(restaurantes, "#c23939", "WHITE");
        changeButtonColor(usuarios, "#CB5353", "WHITE");
        changeButtonColor(menus, "#CB5353", "WHITE");
    }

    @FXML
    void mostrarUsuarios(MouseEvent event) throws IOException {
        // Cargar el FXML dashboard de usuarios
        FXMLLoader loader = new FXMLLoader(getClass().getResource("usersManager.fxml"));
        Parent fxml = null;
        try {
            fxml = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Limpiar y mostrar el nuevo contenido
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
        // Cambiar colores de botones
        changeButtonColor(usuarios, "#c23939", "WHITE");
        changeButtonColor(restaurantes, "#CB5353", "WHITE");
        changeButtonColor(menus, "#CB5353", "WHITE");
    }

    //========================//
    //        EXTRAS          //
    //========================//

    //cerrar el programa
    @FXML
    void cerrarPrograma(MouseEvent event){
        Stage stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }

    //minimizar el programa
    @FXML
    void minimizarPrograma(MouseEvent event){
        Stage stage = (Stage) minimizar.getScene().getWindow();
        stage.setIconified(true);
    }

    // cambiar el color de los botones al presionarlos
    private void changeButtonColor(Button boton, String hexBack, String hexText) {
        boton.setStyle("-fx-background-color: " + hexBack + "; -fx-text-fill: " + hexText + ";");
    }

    public void logout(MouseEvent event) throws IOException {
        // Cargar la pantalla principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginAdmin.fxml"));
        Parent root = loader.load();

        stack.getChildren().clear();
        stack.getChildren().add(root);
    }
}