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


    private static ObjectProperty<Parent> currentView = new SimpleObjectProperty<>(null);
    private static final Map<String, String> pageMappings = new HashMap<>();

    static{

        pageMappings.put("New User", "NewUserForm.fxml");
        pageMappings.put("View Accounts", "TableView.fxml");

        pageMappings.put("New Menu Item","NewMenuItemForm.fxml");
        pageMappings.put("View Menu Items", "TableView.fxml");

        pageMappings.put("New Received Items","ReceivedItemsForm.fxml");
        pageMappings.put("View Received Items", "TableView.fxml");

        pageMappings.put("Order Menu", "OrderMenuView.fxml");

        pageMappings.put("New Inventory","NewInventoryItemForm.fxml");
        pageMappings.put("View Inventory", "TableView.fxml");

        pageMappings.put("Log Order","LogOrderForm.fxml");
        pageMappings.put("View Order History","TableView.fxml");

        pageMappings.put("New Supplier","SupplierInfoForm.fxml");
        pageMappings.put("View Suppliers","TableView.fxml");

        pageMappings.put("New Purchase Order","PurchaseOrderForm.fxml");
        pageMappings.put("View Purchase Orders","TableView.fxml");

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


            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/chooser/trackbite/"+fxmlFile+".fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root, mainStage.getWidth(), mainStage.getHeight());

            if (root instanceof AnchorPane) {
                ProgressLoadUtils.setUpProgressLoad((AnchorPane) root);
            }

            mainStage.setScene(newScene);
            mainStage.setTitle(title);
            mainStage.setResizable(resizable);


            mainStage.show();
        } catch (IOException e) {
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
