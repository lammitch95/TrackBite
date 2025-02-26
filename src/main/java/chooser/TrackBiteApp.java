package chooser;

import chooser.database.FirestoreContext;
import chooser.database.FirestoreUtils;
import chooser.utils.SceneNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TrackBiteApp extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FirestoreContext.getFirestore();
        SceneNavigator.setMainStage(stage);
        SceneNavigator.switchScene("LoginPage", "TrackBite/Login", 1114, 632, true);
    }

    public static void main(String[] args) {
        launch();
    }
}