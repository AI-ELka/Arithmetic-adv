
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cm1.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        controller.startTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}