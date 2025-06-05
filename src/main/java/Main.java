import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    double x, y;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("manager.fxml"));
        stage.setTitle("Al Vuelo Manager");
        stage.setScene(new Scene(root,900, 600));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
