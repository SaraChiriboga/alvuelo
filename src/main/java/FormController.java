import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormController {
    @FXML
    private TextField idTextField;
    @FXML
    private Label labelAviso;
    @FXML
    private Button cancelarButton;
    @FXML
    private ToggleButton estadoToggle;
    @FXML
    private TextField nombre;
    @FXML
    private TextField ingredientes;
    @FXML
    private TextField precio;

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    public void initialize() {
        cancelarButton.setOnAction(e -> ((Stage) cancelarButton.getScene().getWindow()).close());
    }

    public void delete(MouseEvent event) {
        String id = idTextField.getText();
        if (id.isEmpty()) {
            labelAviso.setText("Por favor, introduce un ID.");
            return;
        }

        // Aquí se llamaría al metodo de eliminación de la base de datos
        String sql = "DELETE FROM platos WHERE id = ?";
        try (PreparedStatement preparedStatement = alvuelo.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                labelAviso.setText("Registro con ID " + id + " eliminado correctamente.");
                idTextField.clear();
            } else {
                labelAviso.setText("No se encontró un registro con ID " + id + ".");
            }
        } catch (SQLException e) {
            labelAviso.setText("Error al eliminar el registro: " + e.getMessage());
        }
    }

    @FXML
    public void alternarEstado(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if (estadoToggle.isSelected()) {
            estadoToggle.setText("Agotado");
        } else {
            estadoToggle.setText("Disponible");
        }
    }

    public void edit(MouseEvent event) {
        String id = idTextField.getText();
        if (id.isEmpty()) {
            labelAviso.setText("Por favor, introduce un ID.");
            return;
        }

        String query = "SELECT * FROM platos WHERE id = ?";
        try (PreparedStatement preparedStatement = alvuelo.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nombre.setText(resultSet.getString("nombre"));
                ingredientes.setText(resultSet.getString("ingredientes"));
                precio.setText(String.valueOf(resultSet.getDouble("precio")));
                estadoToggle.setSelected(!resultSet.getBoolean("disponibilidad"));
                if (estadoToggle.isSelected()) {
                    estadoToggle.setText("Agotado");
                } else {
                    estadoToggle.setText("Disponible");
                }
            } else {
                labelAviso.setText("No se encontró un registro con ID " + id + ".");
            }
        } catch (SQLException e) {
            labelAviso.setText("Error al buscar el registro: " + e.getMessage());
        }
    }

    public void sendCambios(MouseEvent event) {
        String sq = "UPDATE platos SET nombre = ?, ingredientes = ?, precio = ?, disponibilidad = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = alvuelo.prepareStatement(sq)) {
            preparedStatement.setString(1, nombre.getText());
            preparedStatement.setString(2, ingredientes.getText());
            preparedStatement.setDouble(3, Double.parseDouble(precio.getText()));
            preparedStatement.setBoolean(4, !estadoToggle.isSelected());
            preparedStatement.setString(5, idTextField.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                labelAviso.setText("Registro actualizado correctamente.");
            } else {
                labelAviso.setText("No se encontró un registro con ese ID.");
            }
        } catch (SQLException e) {
            labelAviso.setText("Error al actualizar el registro: " + e.getMessage());
        }

    }
}
