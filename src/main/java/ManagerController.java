import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import restaurantes.Restaurante;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    @FXML
    private Button menus;
    @FXML
    private Button restaurantes;
    @FXML
    private Button usuarios;
    @FXML
    private StackPane stackContentArea;
    @FXML
    private Button agregarButton;
    @FXML
    private Button editarButton;
    @FXML
    private Button eliminarButton;

    // Referencia a la tabla cargada dinámicamente
    @FXML
    private TableView<Restaurante> tablaRestaurantes;
    @FXML
    private TableColumn<Restaurante, String> id;
    @FXML
    private TableColumn<Restaurante, String> nombre;
    @FXML
    private TableColumn<Restaurante, String> ubi;
    @FXML
    private TableColumn<Restaurante, String> horario;
    @FXML
    private TableColumn<Restaurante, Boolean> activo;

    @FXML
    private Button guardarCambios;

    // Lista de datos
    private ObservableList<Restaurante> listaRestaurantes = FXCollections.observableArrayList();

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font.loadFont(getClass().getResourceAsStream("Poppins-ExtraBold.ttf"), 24);
    }

    // MÉTODOS PARA MOSTRAR LAS DIFERENTES APARTADOS
    @FXML
    void mostrarMenus(MouseEvent event) {
        changeButtonColor(menus, "#c23939", "WHITE");
        changeButtonColor(restaurantes, "#CB5353", "WHITE");
        changeButtonColor(usuarios, "#CB5353", "WHITE");
        // Aquí cargarías la vista de menús si es necesario
    }

    /* IMPORTANTE: por alguna razon, las columnas se intercambian y por consiguiente, el contenido de cada atributo de Restaurante
    * ID = Horario*/
    @FXML
    void mostrarRestaurantes(MouseEvent event) throws IOException, SQLException {
        // Cargar el FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("restaurantesManager.fxml"));
        Parent fxml = loader.load();

        // Limpiar y mostrar el nuevo contenido
        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);

        // Cambiar colores de botones
        changeButtonColor(restaurantes, "#c23939", "WHITE");
        changeButtonColor(usuarios, "#CB5353", "WHITE");
        changeButtonColor(menus, "#CB5353", "WHITE");

        // Obtener la tabla del FXML cargado
        tablaRestaurantes = (TableView<Restaurante>) fxml.lookup("#tablaRestaurantes");

        // Configurar las columnas
        if (tablaRestaurantes != null && !tablaRestaurantes.getColumns().isEmpty()) {
            // Configurar cada columna
            TableColumn<Restaurante, String> columnaId = new TableColumn<>("ID");
            TableColumn<Restaurante, String> columnaNombre = new TableColumn<>("Nombre");
            TableColumn<Restaurante, String> columnaUbi = new TableColumn<>("Ubicación");
            TableColumn<Restaurante, String> columnaHorario = new TableColumn<>("Horario");
            TableColumn<Restaurante, Boolean> columnaActivo = new TableColumn<>("Activo");
            tablaRestaurantes.getColumns().setAll(columnaId, columnaNombre, columnaUbi, columnaHorario, columnaActivo); //ESTO DEFINE EL ORDEN DE LAS COLUMNAS!!!!!!!

            // Asignar los PropertyValueFactory correspondientes
            columnaId.setCellValueFactory(new PropertyValueFactory<>("idRestaurante"));
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaUbi.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
            columnaHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
            columnaActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        }

        // Cargar los datos
        llenarTablaDatosRestaurantes();
    }

    // metodo auxiliar para cargar datos (extrae la lógica de carga)
    private void llenarTablaDatosRestaurantes() throws SQLException {
        listaRestaurantes.clear();
        String query = "SELECT * FROM alvuelo_db.restaurantes";

        try (PreparedStatement statement = alvuelo.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Restaurante restaurante = new Restaurante(
                        resultSet.getString("idrestaurante"),
                        resultSet.getString("nombre"),
                        resultSet.getString("ubicacion"),
                        resultSet.getString("horario"),
                        resultSet.getBoolean("activo")
                );
                listaRestaurantes.add(restaurante);
            }

            if (tablaRestaurantes != null) {
                tablaRestaurantes.setItems(listaRestaurantes);
            }
        }
    }

    @FXML
    void mostrarUsuarios(MouseEvent event) {
        changeButtonColor(usuarios, "#c23939", "WHITE");
        changeButtonColor(restaurantes, "#CB5353", "WHITE");
        changeButtonColor(menus, "#CB5353", "WHITE");
        // Aquí cargarías la vista de usuarios si es necesario
    }

    //APARTADO DE RESTAURANTES
    //AGREGAR RESTAURANTE MEDIANTE NUEVA FILA
    @FXML
    void agregarRestaurante(MouseEvent event) throws IOException, SQLException {
        //agregar un restaurante nuevo a la tabla con campos modificables
        Restaurante extra = new Restaurante("xx:00am - xx:00pm", "Ubicacion", "Nuevo", "UPOx", false);
        llenarTablaDatosRestaurantes();
        extra.setNuevo(true); // <- MARCA COMO NUEVO
        listaRestaurantes.add(extra);


        //crear una fila de simulacion
        listaRestaurantes.add(extra);
        tablaRestaurantes.setItems(listaRestaurantes);
        tablaRestaurantes.scrollTo(extra); //para que se dirija a la nueva fila

        //los campos se vuelven editables
        tablaRestaurantes.setEditable(true);
        TableColumn<Restaurante, String> columnaId = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(0);
        TableColumn<Restaurante, String> columnaNombre = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(1);
        TableColumn<Restaurante, String> columnaUbi = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(2);
        TableColumn<Restaurante, String> columnaHorario = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(3);
        TableColumn<Restaurante, Boolean> columnaActivo = (TableColumn<Restaurante, Boolean>) tablaRestaurantes.getColumns().get(4);
        
        columnaId.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaUbi.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaHorario.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaActivo.setCellFactory(CheckBoxTableCell.forTableColumn(columnaActivo));

        columnaId.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setIdRestaurante(editEvent.getNewValue()); // Asegura que se actualiza correctamente
            tablaRestaurantes.refresh(); // Refresca la tabla para aplicar el cambio
        });
        columnaNombre.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setNombre(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaUbi.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setUbicacion(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaHorario.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setHorario(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaActivo.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setActivo(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        guardarCambios.setVisible(true);
    }

    @FXML
    void refreshDB(MouseEvent event) throws SQLException {
        if (!listaRestaurantes.isEmpty()) {
            alvuelo.setAutoCommit(false);
            boolean cambiosRealizados = false;

            try {
                String checkExistSQL = "SELECT COUNT(*) FROM alvuelo_db.restaurantes WHERE idrestaurante = ?";
                String updateSQL = "UPDATE alvuelo_db.restaurantes SET nombre = ?, ubicacion = ?, horario = ?, activo = ? WHERE idrestaurante = ?";
                String insertSQL = "INSERT INTO alvuelo_db.restaurantes (idrestaurante, nombre, ubicacion, horario, activo) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement checkStmt = alvuelo.prepareStatement(checkExistSQL);
                     PreparedStatement updateStmt = alvuelo.prepareStatement(updateSQL);
                     PreparedStatement insertStmt = alvuelo.prepareStatement(insertSQL)) {

                    List<Restaurante> yaProcesados = new ArrayList<>();

                    for (Restaurante restaurante : listaRestaurantes) {
                        boolean necesitaGuardar = restaurante.isNuevo() || restaurante.isModificado();

                        if (necesitaGuardar) {
                            checkStmt.setString(1, restaurante.getIdRestaurante());
                            ResultSet rs = checkStmt.executeQuery();
                            rs.next();
                            boolean existe = rs.getInt(1) > 0;

                            if (existe) {
                                updateStmt.setString(1, restaurante.getNombre());
                                updateStmt.setString(2, restaurante.getUbicacion());
                                updateStmt.setString(3, restaurante.getHorario());
                                updateStmt.setBoolean(4, restaurante.isActivo());
                                updateStmt.setString(5, restaurante.getIdRestaurante());
                                updateStmt.executeUpdate();
                            } else {
                                insertStmt.setString(1, restaurante.getIdRestaurante());
                                insertStmt.setString(2, restaurante.getNombre());
                                insertStmt.setString(3, restaurante.getUbicacion());
                                insertStmt.setString(4, restaurante.getHorario());
                                insertStmt.setBoolean(5, restaurante.isActivo());
                                insertStmt.executeUpdate();
                            }

                            cambiosRealizados = true;
                            restaurante.setModificado(false);
                            restaurante.setNuevo(false);
                            yaProcesados.add(restaurante);
                        }
                    }
                }

                if (cambiosRealizados) {
                    alvuelo.commit();
                    System.out.println("Cambios guardados exitosamente");
                    guardarCambios.setVisible(false);
                    tablaRestaurantes.setEditable(false);
                } else {
                    System.out.println("No había cambios pendientes");
                }

            } catch (SQLException e) {
                alvuelo.rollback();
                System.err.println("Error al guardar: " + e.getMessage());
                throw e;
            } finally {
                alvuelo.setAutoCommit(true);
            }
        }

        // Limpia y vuelve a cargar desde la base
        llenarTablaDatosRestaurantes();
    }



    //editar restaurante directamente de la tabla
    @FXML
    void editarRestaurante(MouseEvent event) {
        tablaRestaurantes.setEditable(true); //habilita los cambios en la tabla
        //los campos se vuelven editables
        tablaRestaurantes.setEditable(true);
        TableColumn<Restaurante, String> columnaId = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(0);
        TableColumn<Restaurante, String> columnaNombre = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(1);
        TableColumn<Restaurante, String> columnaUbi = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(2);
        TableColumn<Restaurante, String> columnaHorario = (TableColumn<Restaurante, String>) tablaRestaurantes.getColumns().get(3);
        TableColumn<Restaurante, Boolean> columnaActivo = (TableColumn<Restaurante, Boolean>) tablaRestaurantes.getColumns().get(4);

        columnaId.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaUbi.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaHorario.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaActivo.setCellFactory(CheckBoxTableCell.forTableColumn(columnaActivo));

        columnaId.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setIdRestaurante(editEvent.getNewValue()); // Asegura que se actualiza correctamente
            tablaRestaurantes.refresh(); // Refresca la tabla para aplicar el cambio
        });
        columnaNombre.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setNombre(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaUbi.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setUbicacion(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaHorario.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setHorario(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        columnaActivo.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setActivo(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        guardarCambios.setVisible(true); //guarda los cambios de la tabla
    }

    @FXML
    void eliminarRestaurante(MouseEvent event) {
        Restaurante seleccionado = tablaRestaurantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("No hay restaurante seleccionado.");
            return;
        }

        String id = seleccionado.getIdRestaurante();

        // Eliminar de la base de datos
        try {
            String sql = "DELETE FROM restaurantes WHERE idrestaurante = ?";
            PreparedStatement stmt = alvuelo.prepareStatement(sql);
            stmt.setString(1, id);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                // solo si se borró de la BD, lo borra de la tabla
                tablaRestaurantes.getItems().remove(seleccionado);
            } else {
                System.out.println("No se encontró el restaurante en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // cambiar el color de los botones al presionarlos
    private void changeButtonColor(Button boton, String hexBack, String hexText) {
        boton.setStyle("-fx-background-color: " + hexBack + "; -fx-text-fill: " + hexText + ";");
    }
}