package chooser.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InventoryMenu {

    @FXML
    private Button newItemButton;

    @FXML
    private Button enterDeliveryButton;

    @FXML
    private Button viewInventoryButton;

    private Stage primaryStage;
    private InventoryView inventoryView;  // Use lowercase for the instance variable

    public static InventoryItem getAllInventoryItems() {
        return null;
    }


    @FXML
    public void initialize() {
        // Handle button actions
        newItemButton.setOnAction(e -> showNewItemForm());
        enterDeliveryButton.setOnAction(e -> showEnterDeliveryForm());
        viewInventoryButton.setOnAction(e -> showInventoryView());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void showNewItemForm() {
        // Logic to switch to the New Item Form
        InventoryAdd addItemController = new InventoryAdd();
        addItemController.setPrimaryStage(primaryStage);
    }

    private void showEnterDeliveryForm() {
        // Logic to switch to the Enter Delivery Form
        InventoryDelivery deliveryController = new InventoryDelivery();
        deliveryController.setPrimaryStage(primaryStage);
    }

    private void showInventoryView() {
        // Logic to switch to the Inventory View
        inventoryView = new InventoryView();  // Correct variable name
        inventoryView.showInventoryView(primaryStage);  // Fixed the reference
    }

    // Static methods to handle inventory item management
    public static void addInventoryItem(String itemId, String itemName, String unit, String category) {
        // Implement adding an item to the inventory
        // For example, create an InventoryItem and add it to a list
        InventoryItem newItem = new InventoryItem(itemId, itemName, unit, category);
        InventoryMenu.addInventoryItem(newItem);  // Assuming you have InventoryManager to handle the inventory
    }

    private static void addInventoryItem(InventoryItem newItem) {
    }

    public static InventoryItem getInventoryItemById(String itemId) {
        // Return the item from InventoryManager or similar class
        return InventoryMenu.getInventoryItemById(itemId);  // Assuming this method is in InventoryManager
    }
}