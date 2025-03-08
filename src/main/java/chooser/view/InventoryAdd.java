package chooser.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Random;

public class InventoryAdd {

    @FXML
    private TextField itemNameField;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    private Stage primaryStage;

    @FXML
    public void initialize() {
        unitComboBox.getItems().addAll("Lbs", "Oz", "Gal", "Pieces");
        categoryComboBox.getItems().addAll("Meat", "Dairy", "Bread", "Produce", "Condiments");

        addButton.setOnAction(e -> addItem());
        backButton.setOnAction(e -> showMainMenu());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void addItem() {
        // Get input data from form fields and add new inventory item
        String itemName = itemNameField.getText();
        String unit = unitComboBox.getValue();
        String category = categoryComboBox.getValue();

        if (!itemName.isEmpty() && unit != null && category != null) {
            String itemId = generateItemId(itemName);


            InventoryMenu.addInventoryItem(itemId, itemName, unit, category);

            clearInputFields();
            showAlert("Item added successfully!", "New item has been added to the inventory.");
        } else {
            showAlert("Error", "All fields must be filled!");
        }
    }

    private String generateItemId(String itemName) {
        String prefix = itemName.length() >= 3 ? itemName.substring(0, 3).toUpperCase() : itemName.toUpperCase();
        String suffix = String.format("%03d", new Random().nextInt(1000));
        return prefix + suffix;
    }

    private void clearInputFields() {
        itemNameField.clear();
        unitComboBox.setValue(null);
        categoryComboBox.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMainMenu() {

        InventoryMenu mainMenuController = new InventoryMenu();
        mainMenuController.setPrimaryStage(primaryStage);
    }
}