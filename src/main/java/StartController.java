import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    @FXML
    private StackPane stackContentArea;

    @FXML
    private AnchorPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animacionEntrada();
    }

    private void animacionEntrada() {
        // Asegurarse de que todos los nodos empiecen invisibles
        for (Node node : mainPane.getChildren()) {
            node.setOpacity(0); // Opacidad inicial en 0
        }

        // Aplicar animación a cada hijo del contenedor principal
        for (Node node : mainPane.getChildren()) {
            // FadeIn más lento (duración aumentada)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), node); // 1 segundo de fade
            fadeIn.setFromValue(0); // Empieza transparente
            fadeIn.setToValue(1);   // Termina completamente visible

            // Desplazamiento hacia arriba (más suave)
            TranslateTransition slideUp = new TranslateTransition(Duration.millis(800), node);
            slideUp.setFromY(30);  // Empieza 30 píxeles más abajo
            slideUp.setToY(0);     // Termina en su posición original

            // Retraso escalonado para un efecto secuencial
            int index = mainPane.getChildren().indexOf(node);
            fadeIn.setDelay(Duration.millis(150 * index));  // Cada elemento aparece 150ms después
            slideUp.setDelay(Duration.millis(150 * index)); // Sincronizado con el fade

            // Combinar animaciones y ejecutar
            ParallelTransition combinedAnimation = new ParallelTransition(fadeIn, slideUp);
            combinedAnimation.play();
        }
    }

    public void login(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginUser.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }

    public void register(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerUser.fxml"));
        Parent fxml = loader.load();

        stackContentArea.getChildren().clear();
        stackContentArea.getChildren().add(fxml);
    }
}
