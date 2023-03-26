
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("cm1.fxml"));
        stage.setTitle("Calcul Mental");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Controller.startTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}