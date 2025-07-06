import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import usuarios.Admin;
import usuarios.Cliente;
import usuarios.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersManagerController {
    @FXML
    private TextField barraBusqueda;

    @FXML
    private TableColumn<Admin, String> correoA;

    @FXML
    private TableColumn<Repartidor, String> correoR;

    @FXML
    private TableColumn<Cliente, String> correoU;

    @FXML
    private TableColumn<Admin, String> idA;

    @FXML
    private TableColumn<Repartidor, String> idR;

    @FXML
    private TableColumn<Cliente, String> idU;

    @FXML
    private TableColumn<Admin, String> nombreA;

    @FXML
    private TableColumn<Repartidor, String> nombreR;

    @FXML
    private TableColumn<Cliente, String> nombreU;

    @FXML
    private TableColumn<Cliente, String> rolU;

    @FXML
    private TableView<Admin> tablaAdmins;

    @FXML
    private TableView<Repartidor> tablaRepartidores;

    @FXML
    private TableView<Cliente> tablaUsuarios;

    @FXML
    private Button guardarCambios;

    @FXML
    private Button guardarCambios1;

    // Conexión a base de datos
    private DataBaseAlvuelo db = new DataBaseAlvuelo();
    private Connection alvuelo = db.getConnection();

    @FXML
    void initialize() {
        // Inicializar las columnas de la tabla de Administradores
        idA.setCellValueFactory(new PropertyValueFactory<>("idBanner"));
        nombreA.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        correoA.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tablaUsuarios.setEditable(false);

        // Inicializar las columnas de la tabla de Repartidores
        idR.setCellValueFactory(new PropertyValueFactory<>("idBanner"));
        nombreR.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        correoR.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tablaRepartidores.setEditable(false);

        // Inicializar las columnas de la tabla de Usuarios
        idU.setCellValueFactory(new PropertyValueFactory<>("idBanner"));
        nombreU.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        correoU.setCellValueFactory(new PropertyValueFactory<>("correo"));
        rolU.setCellValueFactory(new PropertyValueFactory<>("rol"));
        tablaUsuarios.setEditable(false);

        loadDBAdmins(); //cargar los administradores desde la base de datos
        loadDBRepartidores(); //cargar los repartidores desde la base de datos
        loadDBUsuarios(); //cargar los usuarios desde la base de datos

    }

    @FXML
    public void addAdmin(ActionEvent event) {
        // Create a new Admin with empty fields
        Admin newAdmin = new Admin("xxxxx", "Nuevo", "E-mail");
        newAdmin.setNuevo(true);
        tablaAdmins.getItems().add(newAdmin);
        tablaAdmins.setEditable(true);

        // Set up cell factories for editing
        idA.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreA.setCellFactory(TextFieldTableCell.forTableColumn());
        correoA.setCellFactory(TextFieldTableCell.forTableColumn());

        // Only allow editing on the last row
        idA.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaAdmins.getItems().size() - 1) {
                Admin admin = editEvent.getRowValue();
                admin.setIdBanner(editEvent.getNewValue());
                tablaAdmins.refresh();
            }
        });
        nombreA.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaAdmins.getItems().size() - 1) {
                Admin admin = editEvent.getRowValue();
                admin.setNombre(editEvent.getNewValue());
                tablaAdmins.refresh();
            }
        });
        correoA.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaAdmins.getItems().size() - 1) {
                Admin admin = editEvent.getRowValue();
                admin.setCorreo(editEvent.getNewValue());
                tablaAdmins.refresh();
            }
        });

        guardarCambios.setVisible(true);
    }

    @FXML
    void addRepartidor(ActionEvent event) {
        Repartidor newRepartidor = new Repartidor("xxxxx", "Nuevo", "E-mail");
        newRepartidor.setNuevo(true);
        tablaRepartidores.getItems().add(newRepartidor);
        tablaRepartidores.setEditable(true);

        // Set up cell factories for editing
        idR.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreR.setCellFactory(TextFieldTableCell.forTableColumn());
        correoR.setCellFactory(TextFieldTableCell.forTableColumn());

        // Only allow editing on the last row
        idR.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaRepartidores.getItems().size() - 1) {
                Repartidor repartidor = editEvent.getRowValue();
                repartidor.setIdBanner(editEvent.getNewValue());
                tablaRepartidores.refresh();
            }
        });

        nombreR.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaRepartidores.getItems().size() - 1) {
                Repartidor repartidor = editEvent.getRowValue();
                repartidor.setNombre(editEvent.getNewValue());
                tablaRepartidores.refresh();
            }
        });

        correoR.setOnEditCommit(editEvent -> {
            if (editEvent.getTablePosition().getRow() == tablaRepartidores.getItems().size() - 1) {
                Repartidor repartidor = editEvent.getRowValue();
                repartidor.setCorreo(editEvent.getNewValue());
                tablaRepartidores.refresh();
            }
        });

        guardarCambios1.setVisible(true);
    }

    @FXML
    void deleteAdmin(ActionEvent event) {
        // Eliminar el restaurante seleccionado de la tabla y de la base de datos
        Admin selectedAdmin = tablaAdmins.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            tablaUsuarios.getItems().remove(selectedAdmin);
            String deleteQuery = "DELETE FROM alvuelo_db.admins WHERE id = ?";
            try (PreparedStatement preparedStatement = alvuelo.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, selectedAdmin.getIdBanner());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Recargar la tabla para mostrar los cambios
            tablaAdmins.getItems().clear();
            loadDBAdmins();
        } else {
            // Mostrar un mensaje si no hay restaurante seleccionado
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un admin para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteRepartidor(ActionEvent event) {
        // Eliminar el restaurante seleccionado de la tabla y de la base de datos
        Repartidor selectedRepartidor = tablaRepartidores.getSelectionModel().getSelectedItem();
        if (selectedRepartidor != null) {
            tablaUsuarios.getItems().remove(selectedRepartidor);
            String deleteQuery = "DELETE FROM alvuelo_db.repartidores WHERE id = ?";
            try (PreparedStatement preparedStatement = alvuelo.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, selectedRepartidor.getIdBanner());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Recargar la tabla para mostrar los cambios
            tablaRepartidores.getItems().clear();
            loadDBRepartidores();
        } else {
            // Mostrar un mensaje si no hay restaurante seleccionado
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un repartidor para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    void editAdmin(ActionEvent event) {
        tablaAdmins.setEditable(true); // hacer editable la tabla

        idA.setEditable(false);
        nombreA.setCellFactory(TextFieldTableCell.forTableColumn());
        correoA.setCellFactory(TextFieldTableCell.forTableColumn());

        nombreA.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Admin admin = editEvent.getRowValue();
            admin.setNombre(editEvent.getNewValue());
            tablaAdmins.refresh();
        });

        correoA.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Admin admin = editEvent.getRowValue();
            admin.setCorreo(editEvent.getNewValue());
            tablaAdmins.refresh();
        });

        guardarCambios.setVisible(true); // mostrar el botón de guardar cambios
    }

    @FXML
    void editRepartidor(ActionEvent event) {
        tablaRepartidores.setEditable(true); // hacer editable la tabla

        idR.setEditable(false);
        nombreR.setCellFactory(TextFieldTableCell.forTableColumn());
        correoR.setCellFactory(TextFieldTableCell.forTableColumn());

        nombreR.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Repartidor repartidor = editEvent.getRowValue();
            repartidor.setNombre(editEvent.getNewValue());
            tablaRepartidores.refresh();
        });

        correoR.setOnEditCommit(editEvent -> {
            editEvent.getRowValue().setModificado(true);
            Repartidor repartidor = editEvent.getRowValue();
            repartidor.setCorreo(editEvent.getNewValue());
            tablaRepartidores.refresh();
        });

        guardarCambios1.setVisible(true); // mostrar el botón de guardar cambios
    }

    @FXML
    void search(ActionEvent event) {
        // Wrap the items in FilteredLists
        FilteredList<Admin> filteredAdmins = new FilteredList<>(tablaAdmins.getItems(), p -> true);
        tablaAdmins.setItems(filteredAdmins);

        FilteredList<Repartidor> filteredRepartidores = new FilteredList<>(tablaRepartidores.getItems(), p -> true);
        tablaRepartidores.setItems(filteredRepartidores);

        FilteredList<Cliente> filteredUsuarios = new FilteredList<>(tablaUsuarios.getItems(), p -> true);
        tablaUsuarios.setItems(filteredUsuarios);

        barraBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();

            filteredAdmins.setPredicate(admin ->
                    admin.getIdBanner().toLowerCase().contains(lower) ||
                            admin.getNombre().toLowerCase().contains(lower) ||
                            admin.getCorreo().toLowerCase().contains(lower)
            );

            filteredRepartidores.setPredicate(rep ->
                    rep.getIdBanner().toLowerCase().contains(lower) ||
                            rep.getNombre().toLowerCase().contains(lower) ||
                            rep.getCorreo().toLowerCase().contains(lower)
            );

            filteredUsuarios.setPredicate(user ->
                    user.getIdBanner().toLowerCase().contains(lower) ||
                            user.getNombre().toLowerCase().contains(lower) ||
                            user.getCorreo().toLowerCase().contains(lower) ||
                            (user.getRol() != null && user.getRol().toLowerCase().contains(lower))
            );
        });
    }

    public void loadDBAdmins(){
        String query = "SELECT * FROM admins";
        try {
            // Execute the query and populate the table
            var statement = alvuelo.createStatement();
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String idBanner = resultSet.getString("id");
                String nombre = resultSet.getString("nombre");
                String correo = resultSet.getString("correo");
                Admin admin = new Admin(idBanner, nombre, correo);
                tablaAdmins.getItems().add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDBRepartidores() {
        String query = "SELECT * FROM repartidores";
        try {
            // Execute the query and populate the table
            var statement = alvuelo.createStatement();
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String idBanner = resultSet.getString("id");
                String nombre = resultSet.getString("nombre");
                String correo = resultSet.getString("correo");
                Repartidor repartidor = new Repartidor(idBanner, nombre, correo);
                tablaRepartidores.getItems().add(repartidor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDBUsuarios() {
        String query = "SELECT * FROM users";
        try {
            // Execute the query and populate the table
            var statement = alvuelo.createStatement();
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String idBanner = resultSet.getString("id");
                String nombre = resultSet.getString("nombre");
                String correo = resultSet.getString("correo");
                String rol = resultSet.getString("rol");
                Cliente usuario = new Cliente(idBanner, nombre, correo, rol);
                tablaUsuarios.getItems().add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAdmin(MouseEvent event) throws SQLException {
        if (!tablaUsuarios.getItems().isEmpty()) {
            alvuelo.setAutoCommit(false);
            boolean cambiosRealizados = false;

            try {
                String checkExistSQL = "SELECT COUNT(*) FROM alvuelo_db.admins WHERE id = ?";
                String updateSQL = "UPDATE alvuelo_db.admins SET nombre = ?, correo = ? WHERE id = ?";
                String insertSQL = "INSERT INTO alvuelo_db.admins (id, nombre, correo) VALUES (?, ?, ?)";

                // Mover las declaraciones fuera del bucle para reutilizarlas
                try (PreparedStatement checkStmt = alvuelo.prepareStatement(checkExistSQL);
                     PreparedStatement updateStmt = alvuelo.prepareStatement(updateSQL);
                     PreparedStatement insertStmt = alvuelo.prepareStatement(insertSQL)) {

                    for (Admin admin : tablaAdmins.getItems()) {
                        boolean necesitaGuardar = admin.isNuevo() || admin.isModificado();

                        if (necesitaGuardar) {
                            // Verificar si existe
                            checkStmt.setString(1, admin.getIdBanner());
                            try (ResultSet rs = checkStmt.executeQuery()) {
                                boolean existe = rs.next() && rs.getInt(1) > 0;

                                if (existe) {
                                    // Actualizar
                                    updateStmt.setString(1, admin.getNombre());
                                    updateStmt.setString(2, admin.getCorreo());
                                    updateStmt.setString(3, admin.getIdBanner());
                                    updateStmt.executeUpdate();
                                } else {
                                    // Insertar
                                    insertStmt.setString(1, admin.getIdBanner());
                                    insertStmt.setString(2, admin.getNombre());
                                    insertStmt.setString(3, admin.getCorreo());
                                    insertStmt.executeUpdate();
                                }

                                cambiosRealizados = true;
                                admin.setModificado(false);
                                admin.setNuevo(false);
                            }
                        }
                    }
                }

                if (cambiosRealizados) {
                    alvuelo.commit();
                    System.out.println("Cambios guardados exitosamente");
                    // Recargar los datos después de guardar
                    tablaAdmins.getItems().clear();
                    loadDBAdmins();
                    guardarCambios.setVisible(false);
                    tablaAdmins.setEditable(false);
                } else {
                    alvuelo.rollback();
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
    }

    public void saveRepartidor(MouseEvent event) throws SQLException {
        if (!tablaRepartidores.getItems().isEmpty()) {
            alvuelo.setAutoCommit(false);
            boolean cambiosRealizados = false;

            try {
                String checkExistSQL = "SELECT COUNT(*) FROM alvuelo_db.repartidores WHERE id = ?";
                String updateSQL = "UPDATE alvuelo_db.repartidores SET nombre = ?, correo = ? WHERE id = ?";
                String insertSQL = "INSERT INTO alvuelo_db.repartidores (id, nombre, correo) VALUES (?, ?, ?)";

                // Mover las declaraciones fuera del bucle para reutilizarlas
                try (PreparedStatement checkStmt = alvuelo.prepareStatement(checkExistSQL);
                     PreparedStatement updateStmt = alvuelo.prepareStatement(updateSQL);
                     PreparedStatement insertStmt = alvuelo.prepareStatement(insertSQL)) {

                    for (Repartidor repartidor : tablaRepartidores.getItems()) {
                        boolean necesitaGuardar = repartidor.isNuevo() || repartidor.isModificado();

                        if (necesitaGuardar) {
                            // Verificar si existe
                            checkStmt.setString(1, repartidor.getIdBanner());
                            try (ResultSet rs = checkStmt.executeQuery()) {
                                boolean existe = rs.next() && rs.getInt(1) > 0;

                                if (existe) {
                                    // Actualizar
                                    updateStmt.setString(1, repartidor.getNombre());
                                    updateStmt.setString(2, repartidor.getCorreo());
                                    updateStmt.setString(3, repartidor.getIdBanner());
                                    updateStmt.executeUpdate();
                                } else {
                                    // Insertar
                                    insertStmt.setString(1, repartidor.getIdBanner());
                                    insertStmt.setString(2, repartidor.getNombre());
                                    insertStmt.setString(3, repartidor.getCorreo());
                                    insertStmt.executeUpdate();
                                }

                                cambiosRealizados = true;
                                repartidor.setModificado(false);
                                repartidor.setNuevo(false);
                            }
                        }
                    }
                }

                if (cambiosRealizados) {
                    alvuelo.commit();
                    System.out.println("Cambios guardados exitosamente");
                    // Recargar los datos después de guardar
                    tablaRepartidores.getItems().clear();
                    loadDBRepartidores();
                    guardarCambios1.setVisible(false);
                    tablaRepartidores.setEditable(false);
                } else {
                    alvuelo.rollback();
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
    }

    public void deleteUsuario(MouseEvent event) {
        // Eliminar el restaurante seleccionado de la tabla y de la base de datos
        Cliente selectedUsuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUsuario != null) {
            tablaUsuarios.getItems().remove(selectedUsuario);
            String deleteQuery = "DELETE FROM alvuelo_db.users WHERE id = ?";
            try (PreparedStatement preparedStatement = alvuelo.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, selectedUsuario.getIdBanner());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Recargar la tabla para mostrar los cambios
            tablaUsuarios.getItems().clear();
            loadDBUsuarios();
        } else {
            // Mostrar un mensaje si no hay restaurante seleccionado
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un admin para eliminar.");
            alert.showAndWait();
        }
    }
}