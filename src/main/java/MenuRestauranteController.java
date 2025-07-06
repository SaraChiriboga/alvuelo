import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MenuRestauranteController {
    private String restauranteId;

    @FXML
    private StackPane stackContentArea;

    public void setRestauranteId(String id) {
        this.restauranteId = id;
    }

    public void home(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPageUser.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }
}
