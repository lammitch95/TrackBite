package chooser.view;


import chooser.Inventory;
import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.model.InventoryItem;
import chooser.utils.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class addDeliveryController {
    @FXML
    private Button addDeliveryButton;

    @FXML
    private Button backToMainSelected;

    @FXML
    private TextField itemID;

    @FXML
    private TextField itemName;

    @FXML
    private TextField pricePerUnit;

    @FXML
    private TextField quantity;

    @FXML
    private DatePicker deliveryDate;

    @FXML
    private DatePicker expirationDate;

    private InventoryItem selectedItem;


    @FXML
    void itemIdEntered() {
        String itemId = itemID.getText().trim();
        System.out.println("itemId: " + itemId);
        if (!itemId.isEmpty()) {
            System.out.println("itemId" + itemId);
            Map<String, Object> itemData = FirestoreUtils.readDoc("Inventory", itemId);
            if (itemData != null) {
                selectedItem = createInvItemFromMap(itemData);
                System.out.println("Selected item loaded: " + selectedItem.getItemName() + " (Qty: " + selectedItem.getQuantity() + ")");
                itemName.setText(selectedItem.getItemName());
                pricePerUnit.setText(String.valueOf(selectedItem.getPricePerUnit()));
            } else {
                showAlert("Item Not Found", "No item found with this ID.");
            }
        }
    }


    @FXML
    void itemNameEntered(ActionEvent event) {

    }

    @FXML
    void quantityEntered(ActionEvent event) {

    }

    @FXML
    void deliveryDateSelected(ActionEvent event) {

    }

    @FXML
    void expirationDateSelected(ActionEvent event) {

    }

    @FXML
    void pricePerUnitEntered(ActionEvent event) {

    }


    @FXML
    void backToMainSelected
            (ActionEvent event) {
        SceneNavigator.switchScene(
                "Homepage",
                "TrackBite/Homepage",
                -1,
                -1,
                true);

    }

    @FXML
    void addDeliverySelected(ActionEvent event) {
        itemIdEntered();
        System.out.println("Add Delivery button clicked");
        if (selectedItem != null) {
            try {
                String itemId = itemID.getText().trim();
                String itemNameValue = itemName.getText().trim();
                float quantityValue = Float.parseFloat(quantity.getText().trim());
                LocalDate deliveryDateValue = deliveryDate.getValue();
                LocalDate expirationDateValue = expirationDate.getValue();
                float pricePerUnitValue = Float.parseFloat(pricePerUnit.getText().trim());

                InventoryDelivery newDelivery = new InventoryDelivery(
                        itemId, itemNameValue, quantityValue, deliveryDateValue, expirationDateValue, pricePerUnitValue
                );
                System.out.println("Writing to Firestore: " + itemId);
                FirestoreUtils.writeDoc("inventoryDeliveries", itemId, newDeliveryToMap(newDelivery));
                System.out.println("Write operation completed");

                updateInventoryQuantity(selectedItem.getItemId(), quantityValue);


                showAlert("Delivery Added", "The delivery has been successfully added.");
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please ensure that quantity and price per unit are valid numbers.");
            }
        } else {
            showAlert("Item Not Selected", "Please select a valid item before adding a delivery.");
        }
    }

    @FXML
    void editItemButtonPressed(ActionEvent event) {
        if (selectedItem != null) {
            // Create an instance of the edit form controller
            editItemFormController controller = new editItemFormController();
            controller.setItemToEdit(selectedItem); // Pass the selected item to the edit form

            // Switch to the edit item form
            SceneNavigator.switchScene(
                    "EditItemPage",
                    "TrackBite/EditItemPage",
                    -1,
                    -1,
                    true
            );
        } else {
            showAlert("Item Not Selected", "Please select a valid item to edit.");
        }
    }




    private void updateInventoryQuantity(String itemId, float quantityAdded) {
        Map<String, Object> itemData = FirestoreUtils.readDoc("Inventory", itemId);
        if (itemData != null) {
            float currentQuantity = getFloatForValue((itemData.get("quantity")));

            float newQuantity = (currentQuantity + quantityAdded);

            itemData.put("quantity", newQuantity);

            FirestoreUtils.writeDoc("Inventory", itemId, itemData);
            System.out.println("Current qty: " + currentQuantity + ", Adding: " + quantityAdded + ", New total: " + newQuantity);
        }
    }

    private Map<String, Object> newDeliveryToMap(InventoryDelivery delivery) {
        return Map.of(
                "itemId", delivery.getItemId(),
                "itemName", delivery.getItemName(),
                "quantity", Float.parseFloat(String.valueOf(delivery.getQuantity())),
                "deliveryDate", delivery.getDeliveryDate().toString(),
                "expirationDate", delivery.getExpDate().toString(),
                "pricePerUnit", Float.parseFloat(String.valueOf(delivery.getPricePerUnit()))
        );
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private InventoryItem createInvItemFromMap(Map<String, Object> itemData) {
       // System.out.println(itemData.get("quantity").getClass().getSimpleName());
        String itemId = (String) itemData.get("InventoryItemID");
        String itemName = (String) itemData.get("itemName");
        String unit = (String) itemData.get("unit");
        String category = (String) itemData.get("category");
        String quantity = getStringForValue(itemData.get("quantity"));
        float pricePerUnit = getFloatForValue(itemData.get("pricePerUnit"));

        return new InventoryItem(itemId, itemName, unit, category, quantity, pricePerUnit);
    }
//Im changing this
private String getStringForValue(Object value) {
    return String.valueOf(value);
}


    //end change
    private float getFloatForValue(Object value) {
        if (value == null) return 0.0f;

        if (value instanceof Float) return (Float) value;
        if (value instanceof Double) return ((Double) value).floatValue(); // ✅ FIX HERE
        if (value instanceof Long) return ((Long) value).floatValue();
        if (value instanceof String) return Float.parseFloat((String) value);

        return Float.parseFloat(String.valueOf(value)); // fallback for safety
    }


}










        




