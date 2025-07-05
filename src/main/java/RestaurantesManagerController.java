import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import restaurantes.Restaurante;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantesManagerController {
    // Atributos
    //conexión a la base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    private Button agregarButton;

    @FXML
    private ComboBox<String> comboCampus;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button guardarCambios;

    @FXML
    private TableView<Restaurante> tablaRestaurantes;

    @FXML
    private TableColumn<Restaurante, String> id;

    @FXML
    private TableColumn<Restaurante, String> nombre;

    @FXML
    private TableColumn<Restaurante, String > ubi;

    @FXML
    private TableColumn<Restaurante, String> horario;

    @FXML
    private TableColumn<Restaurante, Boolean> activo;

    // lista de restaurantes
    ObservableList<Restaurante> listaRestaurantes = FXCollections.observableArrayList();


    //METODOS
    @FXML
    public void initialize() {
        comboCampus.getItems().addAll("UdlaPark", "Granados", "Colón"); // Agrega los campus disponibles al ComboBox
        // configurar las columnas solo una vez
        id.setCellValueFactory(new PropertyValueFactory<>("idRestaurante"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ubi.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        horario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        activo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        activo.setCellFactory(column -> new TableCell<Restaurante, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setDisable(true); // Por defecto deshabilitado
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);

                    // Habilitar solo si es la última fila y es nuevo
                    Restaurante restaurante = getTableView().getItems().get(getIndex());
                    boolean esUltimaFilaEditable = getIndex() == getTableView().getItems().size() - 1 && restaurante.isNuevo();
                    checkBox.setDisable(!esUltimaFilaEditable);

                    // Solo agregar listener si es editable
                    if (esUltimaFilaEditable) {
                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            restaurante.setActivo(newVal);
                            restaurante.setModificado(true);
                        });
                    }

                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        });

    }

    @FXML
    public void llenarTabla(String campus) {
        listaRestaurantes.clear(); // Limpia la lista antes de llenarla
        String query = "SELECT * FROM alvuelo_db.restaurantes WHERE campus = ?";
        try (PreparedStatement preparedStatement = alvuelo.prepareStatement(query)) {
            preparedStatement.setString(1, campus);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tablaRestaurantes.getColumns().setAll(id, nombre, ubi, horario, activo); //ESTO DEFINE EL ORDEN DE LAS COLUMNAS!!!!!!!
        tablaRestaurantes.setItems(listaRestaurantes);
    }

    // mostrar resultados filtrados
    public void mostrarRestaurantesPorCampus(javafx.event.ActionEvent actionEvent) {
        String campus = (String) comboCampus.getSelectionModel().getSelectedItem();
        if (campus != null && !campus.isEmpty()) {
            llenarTabla(campus);
        } else {
            // Si no hay campus seleccionado, puedes mostrar todos los restaurantes o manejarlo como desees
            llenarTabla("UdlaPark"); // Por ejemplo, cargar todos los restaurantes de UdlaPark
        }
    }

    // metodo para agregar un nuevo restaurante
    public void addRestaurante(MouseEvent event) {
        // Crear un nuevo restaurante con valores predeterminados
        Restaurante extra = new Restaurante("", "", "", "", false);
        extra.setNuevo(true); // Marca como nuevo
        listaRestaurantes.add(extra);
        tablaRestaurantes.setItems(listaRestaurantes);
        tablaRestaurantes.scrollTo(extra);

        // Hacer la tabla editable
        tablaRestaurantes.setEditable(true);

        // Configurar StringConverter para todas las columnas de texto
        StringConverter<String> converter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object != null ? object : "";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        };

        // Configurar columna ID
        id.setCellFactory(column -> {
            TextFieldTableCell<Restaurante, String> cell = new TextFieldTableCell<>(converter) {
                @Override
                public void startEdit() {
                    if (getIndex() == listaRestaurantes.size() - 1) {
                        super.startEdit();
                    }
                }
            };
            return cell;
        });

        // Configurar columna Nombre
        nombre.setCellFactory(column -> {
            TextFieldTableCell<Restaurante, String> cell = new TextFieldTableCell<>(converter) {
                @Override
                public void startEdit() {
                    if (getIndex() == listaRestaurantes.size() - 1) {
                        super.startEdit();
                    }
                }
            };
            return cell;
        });

        // Configurar columna Ubicación
        ubi.setCellFactory(column -> {
            TextFieldTableCell<Restaurante, String> cell = new TextFieldTableCell<>(converter) {
                @Override
                public void startEdit() {
                    if (getIndex() == listaRestaurantes.size() - 1) {
                        super.startEdit();
                    }
                }
            };
            return cell;
        });

        // Configurar columna Horario
        horario.setCellFactory(column -> {
            TextFieldTableCell<Restaurante, String> cell = new TextFieldTableCell<>(converter) {
                @Override
                public void startEdit() {
                    if (getIndex() == listaRestaurantes.size() - 1) {
                        super.startEdit();
                    }
                }
            };
            return cell;
        });

        // Configurar columna Activo (CheckBox) - SOLUCIÓN COMPLETA
        activo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        activo.setCellFactory(column -> new CheckBoxTableCell<Restaurante, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        Restaurante restaurante = (Restaurante) getTableRow().getItem();
                        restaurante.setActivo(isSelected);
                        restaurante.setModificado(true);
                    }
                });
            }

            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    // Solo permitir edición en la última fila
                    checkBox.setDisable(getIndex() != listaRestaurantes.size() - 1);
                }
            }
        });

        // Configurar eventos de edición para las columnas de texto
        id.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == listaRestaurantes.size() - 1) {
                Restaurante restaurante = editEvent.getRowValue();
                restaurante.setModificado(true);
                restaurante.setIdRestaurante(editEvent.getNewValue());
                tablaRestaurantes.refresh();
            }
        });

        nombre.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == listaRestaurantes.size() - 1) {
                Restaurante restaurante = editEvent.getRowValue();
                restaurante.setModificado(true);
                restaurante.setNombre(editEvent.getNewValue());
                tablaRestaurantes.refresh();
            }
        });

        ubi.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == listaRestaurantes.size() - 1) {
                Restaurante restaurante = editEvent.getRowValue();
                restaurante.setModificado(true);
                restaurante.setUbicacion(editEvent.getNewValue());
                tablaRestaurantes.refresh();
            }
        });

        horario.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == listaRestaurantes.size() - 1) {
                Restaurante restaurante = editEvent.getRowValue();
                restaurante.setModificado(true);
                restaurante.setHorario(editEvent.getNewValue());
                tablaRestaurantes.refresh();
            }
        });

        // Habilitar el botón de guardar cambios
        guardarCambios.setVisible(true);
    }

    //guardar cambios
    public void save(MouseEvent event) throws SQLException {
        if (!listaRestaurantes.isEmpty()) {
            alvuelo.setAutoCommit(false);
            boolean cambiosRealizados = false;
            String campusSeleccionado = comboCampus.getValue(); // Obtener el campus seleccionado

            try {
                String checkExistSQL = "SELECT COUNT(*) FROM alvuelo_db.restaurantes WHERE idrestaurante = ?";
                String updateSQL = "UPDATE alvuelo_db.restaurantes SET nombre = ?, ubicacion = ?, horario = ?, activo = ?, campus = ? WHERE idrestaurante = ?";
                String insertSQL = "INSERT INTO alvuelo_db.restaurantes (idrestaurante, nombre, ubicacion, horario, activo, campus) VALUES (?, ?, ?, ?, ?, ?)";

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
                                updateStmt.setString(5, campusSeleccionado); // Añadir campus
                                updateStmt.setString(6, restaurante.getIdRestaurante());
                                updateStmt.executeUpdate();
                            } else {
                                insertStmt.setString(1, restaurante.getIdRestaurante());
                                insertStmt.setString(2, restaurante.getNombre());
                                insertStmt.setString(3, restaurante.getUbicacion());
                                insertStmt.setString(4, restaurante.getHorario());
                                insertStmt.setBoolean(5, restaurante.isActivo());
                                insertStmt.setString(6, campusSeleccionado); // Añadir campus
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

        llenarTabla(comboCampus.getValue());
    }

    // eliminar restaurantes
    public void deleteRestaurante(MouseEvent event) {
        // Eliminar el restaurante seleccionado de la tabla y de la base de datos
        Restaurante selectedRestaurante = tablaRestaurantes.getSelectionModel().getSelectedItem();
        if (selectedRestaurante != null) {
            listaRestaurantes.remove(selectedRestaurante);
            String deleteQuery = "DELETE FROM alvuelo_db.restaurantes WHERE idrestaurante = ?";
            try (PreparedStatement preparedStatement = alvuelo.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, selectedRestaurante.getIdRestaurante());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Recargar la tabla para mostrar los cambios
            llenarTabla(comboCampus.getValue());
        } else {
            // Mostrar un mensaje si no hay restaurante seleccionado
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un restaurante para eliminar.");
            alert.showAndWait();
        }
    }

    // editar restaurantess
    public void editRestaurantes(MouseEvent event) {
        tablaRestaurantes.setEditable(true); // Hacer la tabla editable

        id.setCellFactory(TextFieldTableCell.forTableColumn());
        nombre.setCellFactory(TextFieldTableCell.forTableColumn());
        ubi.setCellFactory(TextFieldTableCell.forTableColumn());
        horario.setCellFactory(TextFieldTableCell.forTableColumn());
        activo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        activo.setCellFactory(column -> new CheckBoxTableCell<Restaurante, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        Restaurante restaurante = (Restaurante) getTableRow().getItem();
                        restaurante.setActivo(isSelected);
                        restaurante.setModificado(true);
                    }
                });
            }

            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    // Solo permitir edición en la última fila
                    checkBox.setDisable(getIndex() != listaRestaurantes.size() - 1);
                }
            }
        });

        id.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setIdRestaurante(editEvent.getNewValue()); // Asegura que se actualiza correctamente
            tablaRestaurantes.refresh(); // Refresca la tabla para aplicar el cambio
        });
        nombre.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setNombre(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        ubi.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setUbicacion(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        horario.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setHorario(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        activo.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Restaurante restaurante = editEvent.getRowValue();
            restaurante.setActivo(editEvent.getNewValue());
            tablaRestaurantes.refresh();
        });
        guardarCambios.setVisible(true); //guarda los cambios de la tabla
    }
}
