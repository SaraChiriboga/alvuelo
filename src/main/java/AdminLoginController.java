import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {
    public void home(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("appStart.fxml"));
        stage.setScene(new Scene(root,375, 812));
        stage.show();
    }
}
