import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Label labelId;

    @FXML
    private StackPane stackContentArea;

    @FXML
    void home(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appStart.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }

}
