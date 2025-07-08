import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    private StackPane stackContentArea;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void home() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appStart.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }

    public void entrarManagerMode(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("loginAdmin.fxml"));
        stage.setScene(new Scene(root,900, 600));
        stage.show();
    }

    public void ingresarApp(MouseEvent event) {
        if (!email.getText().isEmpty() && !password.getText().isEmpty()) {
            // Obtener credenciales ingresadas
            String inputEmail = email.getText();
            String inputPassword = password.getText();

            try {
                // 1. Verificar credenciales en la base de datos
                boolean credencialesValidas = verificarCredencialesEnBD(inputEmail, inputPassword);

                if (credencialesValidas) {
                    // Cargar la pantalla principal
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPageUser.fxml"));
                    Parent root = loader.load();

                    stackContentArea.getChildren().clear();
                    stackContentArea.getChildren().add(root);
                } else {
                    mostrarAlerta("Error", "Credenciales incorrectas", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo conectar a la base de datos", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, complete todos los campos", Alert.AlertType.WARNING);
        }
    }

    // Metodo para verificar credenciales en BD (ejemplo con JDBC)
    private boolean verificarCredencialesEnBD(String email, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE correo = ? AND password = ?";

        try (PreparedStatement stmt = alvuelo.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
