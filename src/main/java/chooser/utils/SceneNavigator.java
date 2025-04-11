package chooser.utils;

import chooser.model.CurrentPageOptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private static ObjectProperty<Parent> currentView = new SimpleObjectProperty<>(null);
    private static final Map<String, String> pageMappings = new HashMap<>();

    static{
        pageMappings.put("New User", "NewUserForm.fxml");
        pageMappings.put("View Accounts", "TableView.fxml");
        pageMappings.put("New Menu Item","NewMenuItemForm.fxml");
        pageMappings.put("View Menu Items", "TableView.fxml");
        pageMappings.put("Add Inventory Item","addItemForm.fxml");
        pageMappings.put("View Inventory", "TableView.fxml");
    }

    public static ObjectProperty<Parent> currentViewProperty() {
        return currentView;
    }


    public static void removePreviousView() {
        if (currentView.get() != null) {
            currentView.set(null);
        }
    }

    public static void loadView(String buttonLabel) {
        try {
            CurrentPageOptions.setCurrPageOption(buttonLabel);
            System.out.println("Current Page Option: "+CurrentPageOptions.getCurrPageOption());
            String fxmlPathFull = "/chooser/trackbite/" + pageMappings.get(buttonLabel);
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPathFull));
            Parent view = loader.load();

            if (currentView.get() != null) {
                currentView.set(null);
            }

            currentView.set(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchScene(String fxmlFile, String title, double width, double height, boolean resizable) {

        ProgressLoadUtils.resetProgessLoadNode();

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

            if (root instanceof AnchorPane) {
                ProgressLoadUtils.setUpProgressLoad((AnchorPane) root);
            }

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

    public static void showProgressLoad() {
        ProgressLoadUtils.showProgressLoad();
    }

    public static void hideProgressLoad() {
        ProgressLoadUtils.hideProgressLoad();
    }
}
