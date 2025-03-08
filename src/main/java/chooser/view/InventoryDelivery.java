package chooser.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;

public class InventoryDelivery {

    @FXML
    private TextField itemIdField;

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private DatePicker deliveryDatePicker;

    @FXML
    private DatePicker expDatePicker;

    @FXML
    private TextField pricePerUnitField;

    @FXML
    private Button addDeliveryButton;

    @FXML
    private Button backButton;

    private Stage primaryStage;

    public InventoryDelivery() {
    }

    @FXML
    public void initialize() {
        addDeliveryButton.setOnAction(e -> addDelivery());
        backButton.setOnAction(e -> showMainMenu());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void addDelivery() {
        // Get input data from form fields
        String itemId = itemIdField.getText();
        String itemName = itemNameField.getText();
        float quantity = Float.parseFloat(quantityField.getText());
        LocalDate deliveryDate = deliveryDatePicker.getValue();
        LocalDate expDate = expDatePicker.getValue();
        float pricePerUnit = Float.parseFloat(pricePerUnitField.getText());


        InventoryDelivery delivery = new InventoryDelivery();


        InventoryItem item = InventoryMenu.getInventoryItemById(itemId);

        if (item != null) {

            item.addDelivery(delivery);
            showAlert("Success", "Delivery added successfully!");
        } else {
            showAlert("Error", "Item not found!");
        }
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