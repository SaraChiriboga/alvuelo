import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    private StackPane stackContentArea;

    @FXML
    private TextField celular;

    @FXML
    private ComboBox<String> comboRol;

    @FXML
    private PasswordField confirmarPassword;

    @FXML
    private TextField email;

    @FXML
    private TextField idbanner;

    @FXML
    private TextField nombre;

    @FXML
    private PasswordField password;

    @FXML
    public void registrarYAcceder(MouseEvent event) {
        // 1. Validar campos obligatorios
        if (nombre.getText().isEmpty() ||
                idbanner.getText().isEmpty() ||
                email.getText().isEmpty() ||
                password.getText().isEmpty() ||
                confirmarPassword.getText().isEmpty() ||
                celular.getText().isEmpty() ||
                comboRol.getValue() == null) {

            mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        // 2. Validar formato de correo institucional (ejemplo: nombre@universidad.edu)
        if (!email.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            mostrarAlerta("Error", "Formato de correo institucional inválido", Alert.AlertType.ERROR);
            return;
        }

        // 3. Validar coincidencia de contraseñas
        if (!password.getText().equals(confirmarPassword.getText())) {
            mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            return;
        }

        try {
            // 6. Verificar si el usuario ya existe
            if (usuarioExiste(email.getText(), idbanner.getText())) {
                mostrarAlerta("Error", "El usuario ya está registrado", Alert.AlertType.ERROR);
                return;
            }

            // 7. Registrar nuevo usuario
            if (registrarUsuario(
                    nombre.getText(),
                    idbanner.getText(),
                    email.getText(),
                    password.getText(),
                    celular.getText(),
                    comboRol.getValue().toString()
            )) {
                // 8. Acceder al sistema
                cargarPantallaPrincipal(event);
            } else {
                mostrarAlerta("Error", "Error al registrar usuario", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de conexión a la base de datos", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Métodos auxiliares
    private boolean usuarioExiste(String correo, String idBanner) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE correo = ? OR id = ?";
        try (PreparedStatement stmt = alvuelo.prepareStatement(query)) {

            stmt.setString(1, correo);
            stmt.setString(2, idBanner);

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private boolean registrarUsuario(String nombre, String idBanner, String correo,
                                     String password, String celular, String rol) throws SQLException {
        String query = "INSERT INTO users (nombre, id, correo, password, celular, rol) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = alvuelo.prepareStatement(query)) {

            stmt.setString(1, nombre);
            stmt.setString(2, idBanner);
            stmt.setString(3, correo);
            stmt.setString(4, password); // Asegúrate de hashear la contraseña
            stmt.setString(5, celular);
            stmt.setString(6, rol);

            return stmt.executeUpdate() > 0;
        }
    }

    private void cargarPantallaPrincipal(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPageUser.fxml"));
        Parent root = loader.load();
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(root);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void home(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appStart.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboRol.getItems().setAll("Estudiante", "Profesor");
    }
}
