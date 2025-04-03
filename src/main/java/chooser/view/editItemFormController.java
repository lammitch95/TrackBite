package chooser.view;

import chooser.database.FirestoreUtils;
import chooser.model.InventoryItem;
import chooser.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class editItemFormController {
    @FXML
    private TextField editItemID;

    @FXML
    private TextField ItemName;

    @FXML
    private ComboBox<String> UnitDropDown;

    @FXML
    private ComboBox<String> CategoryDropDown;

    @FXML
    private TextField PricePerUnit;

    @FXML
    private TextField Quantity;

    private InventoryItem itemToEdit;

    public void setItemToEdit(InventoryItem item) {
         this.itemToEdit = item;
    }

    @FXML
    private void initialize() {
        UnitDropDown.getItems().addAll("Lbs", "Oz", "Gal", "Pieces");
        CategoryDropDown.getItems().addAll("Meat", "Dairy", "Bread", "Produce", "Condiments");

        String selectedItemId = SceneNavigator.getSelectedItemId();

        if (selectedItemId != null && !selectedItemId.isEmpty()) {
            System.out.println("Editing item with ID: " + selectedItemId);

            // Fetch item details using the working approach
            Map<String, Object> itemData = FirestoreUtils.readDoc("Inventory", selectedItemId);

            if (itemData != null) {
                itemToEdit = createInvItemFromMap(itemData); // Convert Firestore map to InventoryItem

                editItemID.setText(itemToEdit.getItemId());
                editItemID.setDisable(true);
                editItemID.setDisable(true); // Prevent editing of item ID
                ItemName.setText(itemToEdit.getItemName());
                UnitDropDown.setValue(itemToEdit.getUnit());
                CategoryDropDown.setValue(itemToEdit.getCategory());
                PricePerUnit.setText(String.valueOf(itemToEdit.getPricePerUnit()));
                Quantity.setText(itemToEdit.getQuantity());
            } else {
                showAlert("Error", "Item not found in inventory.");
            }
        }
    }


    @FXML
    void saveChanges(ActionEvent event) {
        if (isValidInput()) {
            String existingItemId = editItemID.getText().trim();
            itemToEdit = new InventoryItem(
                    existingItemId,
                    ItemName.getText(),
                    UnitDropDown.getValue(),
                    CategoryDropDown.getValue(),
                    Quantity.getText(),
                    Float.parseFloat(PricePerUnit.getText())
            );

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("InventoryItemID", existingItemId);
            itemData.put("itemName", itemToEdit.getItemName());
            itemData.put("unit", itemToEdit.getUnit());
            itemData.put("category", itemToEdit.getCategory());
            itemData.put("quantity", Float.parseFloat(itemToEdit.getQuantity()));
            itemData.put("pricePerUnit", itemToEdit.getPricePerUnit());

            System.out.println("Updating Firestore with data: " + itemData);
            FirestoreUtils.writeDoc("Inventory", existingItemId, itemData);
            System.out.println("Firestore update complete.");

            showAlert("Success", itemToEdit.getItemName() + " has been updated.");
            SceneNavigator.switchScene("Homepage", "TrackBite/Homepage", -1, -1, true);
        }
    }


    private boolean isValidInput() {
        return !ItemName.getText().isEmpty() &&
                !UnitDropDown.getValue().isEmpty() &&
                !CategoryDropDown.getValue().isEmpty() &&
                !PricePerUnit.getText().isEmpty() &&
                !Quantity.getText().isEmpty();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private InventoryItem createInvItemFromMap(Map<String, Object> itemData) {
        return new InventoryItem(
                (String) itemData.get("InventoryItemID"),
                (String) itemData.get("itemName"),
                (String) itemData.get("unit"),
                (String) itemData.get("category"),
                (String) itemData.get("quantity"),
                getFloatForValue(itemData.get("pricePerUnit"))
        );
    }

    private float getFloatForValue(Object value) {
        if (value == null) return 0.0f;
        if (value instanceof Float) return (Float) value;
        if (value instanceof String) return Float.parseFloat((String) value);
        if (value instanceof Long) return ((Long) value).floatValue();
        return 0.0f;

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
}




