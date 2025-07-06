import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

import java.io.*;
import java.sql.*;

public class FormController {
    private File imagenPlato; // Variable para almacenar la imagen del plato

    @FXML
    private ImageView imagen;
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
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Establecer los valores básicos
                nombre.setText(resultSet.getString("nombre"));
                ingredientes.setText(resultSet.getString("ingredientes"));
                precio.setText(String.valueOf(resultSet.getDouble("precio")));
                estadoToggle.setSelected(!resultSet.getBoolean("disponibilidad"));

                // Manejo de la imagen
                try {
                    Blob imagenBlob = resultSet.getBlob("imagenPlato");
                    if (imagenBlob != null) {
                        // Convertir Blob a Image
                        InputStream inputStream = imagenBlob.getBinaryStream();
                        Image image = new Image(inputStream);
                        imagen.setImage(image);
                        inputStream.close();
                    } else {
                        // Cargar imagen por defecto
                        Image defaultImage = new Image(getClass().getResourceAsStream("photo-gallery.png"));
                        imagen.setImage(defaultImage);
                    }
                } catch (SQLException | IOException e) {
                    // Si hay error al cargar la imagen, usar la por defecto
                    Image defaultImage = new Image(getClass().getResourceAsStream("photo-gallery.png"));
                    imagen.setImage(defaultImage);
                }

                // Establecer texto del toggle
                if (estadoToggle.isSelected()) {
                    estadoToggle.setText("Agotado");
                } else {
                    estadoToggle.setText("Disponible");
                }

                labelAviso.setText(""); // Limpiar mensaje de aviso si todo fue bien
            } else {
                labelAviso.setText("No se encontró un registro con ID " + id + ".");
            }
        } catch (SQLException e) {
            labelAviso.setText("Error al buscar el registro: " + e.getMessage());
        }
    }

    public void sendCambios(MouseEvent event) {
        String sq = "UPDATE platos SET nombre = ?, ingredientes = ?, precio = ?, disponibilidad = ?, imagenPlato = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = alvuelo.prepareStatement(sq)) {
            // Establecer parámetros en el orden correcto
            preparedStatement.setString(1, nombre.getText());
            preparedStatement.setString(2, ingredientes.getText());
            preparedStatement.setDouble(3, Double.parseDouble(precio.getText()));
            preparedStatement.setBoolean(4, !estadoToggle.isSelected());

            // Manejo de la imagen (ahora parámetro 5)
            if (imagenPlato != null) {
                FileInputStream fis = new FileInputStream(imagenPlato);
                preparedStatement.setBinaryStream(5, fis, (int) imagenPlato.length());
            } else {
                preparedStatement.setNull(5, java.sql.Types.BLOB);
            }

            // ID como parámetro 6 (último)
            preparedStatement.setString(6, idTextField.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                labelAviso.setText("Registro actualizado correctamente.");
            } else {
                labelAviso.setText("No se encontró un registro con ese ID.");
            }
        } catch (SQLException | FileNotFoundException e) {
            labelAviso.setText("Error al actualizar el registro: " + e.getMessage());
            e.printStackTrace(); // Mejor para depuración
        }
    }

    public void replaceImagenPlato(MouseEvent event) {
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

        //mostrar imagen en el ImageView
        Image image = new Image(file.toURI().toString());
        imagen.setImage(image);

        imagenPlato = file; // Guardar la imagen seleccionada
    }
}
