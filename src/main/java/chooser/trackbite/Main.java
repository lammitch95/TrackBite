package chooser.trackbite;  // class location

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        AnchorPane root = loader.load();

        // Get the ImageView from FXML
        ImageView backgroundImage = (ImageView) root.lookup("#backgroundImage");

        // Match the window size, where the app is opened.
        if (backgroundImage != null) {
            backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
            backgroundImage.fitHeightProperty().bind(primaryStage.heightProperty());
        }
        // Set the window properly to view
        Scene scene = new Scene(root);
        primaryStage.setTitle("TrackBite - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Open the window full-screen mode
        primaryStage.show();  // To display
    }

    public static void main(String[] args) {
        launch(args);
    }
}
