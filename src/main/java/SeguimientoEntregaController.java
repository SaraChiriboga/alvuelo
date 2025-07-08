import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SeguimientoEntregaController implements Initializable {

    @FXML
    private HBox bar;

    @FXML
    private ProgressBar barraProgreso;

    @FXML
    private ImageView homeButton;

    @FXML
    private HBox pinEntrega;

    @FXML
    private StackPane stackContentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animacionBarraProgreso();
    }

    public void animacionBarraProgreso() {
        // Animación con Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(barraProgreso.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(barraProgreso.progressProperty(), 0.5)),
                new KeyFrame(Duration.seconds(2), new KeyValue(barraProgreso.progressProperty(), 1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }
}

