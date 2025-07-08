import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private StackPane stackContentArea;

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
}
