package chooser.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    private static Stage mainStage;
    private static double currWidth;
    private static double currHeight;
    private static String selectedItemId; // Stores the selected item ID

    public static void setSelectedItemId(String itemId) {
        selectedItemId = itemId;
    }

    public static String getSelectedItemId() {
        return selectedItemId;
    }

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchScene(String fxmlFile, String title, double width, double height, boolean resizable) {
        try {

            boolean wasFullscreen = mainStage.isFullScreen();

            // Removed debugging
            String resourcePath = "/chooser/trackbite/" + fxmlFile + ".fxml";
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(resourcePath));

            if (SceneNavigator.class.getResource(resourcePath) == null) {
                throw new IOException("FXML file not found at: " + resourcePath);
            }
            Parent root = loader.load();

            if (width > 0.0 && height > 0.0) {
                currWidth = width;
                currHeight = height;
            }

            Scene newScene = new Scene(root, currWidth, currHeight);
            mainStage.setScene(newScene);
            mainStage.setTitle(title);
            mainStage.setResizable(resizable);

            if (wasFullscreen) {
                mainStage.setFullScreen(true);
            }
            mainStage.show();
        } catch (IOException e) {
            // Removed debugging
            e.printStackTrace();
        }
    }
}
