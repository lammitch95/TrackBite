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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.model.InventoryItem;
import chooser.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.*;

public class addDeliveryController {
    @FXML private VBox deliveryItemsContainer;
    @FXML private Button backToMainSelected;

    private final Map<String, InventoryItem> itemMap = new HashMap<>();
    private final List<DeliveryRow> deliveryRows = new ArrayList<>();

    @FXML
    private void initialize() {
        List<InventoryItem> items = FirestoreUtils.readCollection("Inventory");
        for (InventoryItem item : items) {
            itemMap.put(item.getItemId(), item);
        }
        handleAddItemRow(); // Start with one row
    }

    @FXML
    private void handleAddItemRow() {
        DeliveryRow row = new DeliveryRow();
        deliveryRows.add(row);
        deliveryItemsContainer.getChildren().add(row.rowUI);
    }

    @FXML
    private void handleSubmitDeliveries() {
        for (DeliveryRow row : deliveryRows) {
            try {
                InventoryDelivery delivery = row.toDelivery();
                String docId = UUID.randomUUID().toString();
                FirestoreUtils.writeDoc("inventoryDeliveries", docId, Map.of(
                        "itemId", delivery.getItemId(),
                        "itemName", delivery.getItemName(),
                        "quantity", delivery.getQuantity(),
                        "deliveryDate", delivery.getDeliveryDate().toString(),
                        "expirationDate", delivery.getExpDate().toString(),
                        "pricePerUnit", delivery.getPricePerUnit(),
                        "supplier", delivery.getSupplier(),
                        "poNumber", delivery.getPoNumber()
                ));
                updateInventoryQuantityAndSupplier(delivery.getItemId(), delivery.getQuantity(), delivery.getSupplier());
            } catch (Exception e) {
                showAlert("Error", "Invalid data in a row: " + e.getMessage());
                return;
            }
        }
        showAlert("Success", "Deliveries recorded successfully.");
        deliveryItemsContainer.getChildren().clear();
        deliveryRows.clear();
        handleAddItemRow(); // Add a new blank row after submit
    }

    @FXML
    private void backToMainSelected(javafx.event.ActionEvent event) {
        SceneNavigator.switchScene("Homepage", "TrackBite/Homepage", -1, -1, true);
    }

    private void updateInventoryQuantityAndSupplier(String itemId, float quantityAdded, String newSupplier) {
        Map<String, Object> itemData = FirestoreUtils.readDoc("Inventory", itemId);
        if (itemData != null) {
            float currentQuantity = getFloatForValue(itemData.get("quantity"));
            float newQuantity = currentQuantity + quantityAdded;
            itemData.put("quantity", newQuantity);
            itemData.put("supplier", newSupplier);
            FirestoreUtils.writeDoc("Inventory", itemId, itemData);
            System.out.println("Updated quantity to " + newQuantity + " and supplier to " + newSupplier);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private float getFloatForValue(Object value) {
        if (value == null) return 0.0f;
        if (value instanceof Float) return (Float) value;
        if (value instanceof Double) return ((Double) value).floatValue();
        if (value instanceof Long) return ((Long) value).floatValue();
        if (value instanceof String) return Float.parseFloat((String) value);
        return Float.parseFloat(String.valueOf(value));
    }

    // Inner class for each delivery row
    private class DeliveryRow {
        ComboBox<String> itemId = new ComboBox<>();
        TextField itemName = new TextField();
        TextField quantity = new TextField();
        TextField pricePerUnit = new TextField();
        TextField supplier = new TextField();
        DatePicker deliveryDate = new DatePicker();
        DatePicker expirationDate = new DatePicker();
        TextField poNumber = new TextField();

        HBox rowUI = new HBox(10, itemId, itemName, quantity, pricePerUnit, supplier, deliveryDate, expirationDate, poNumber);

        DeliveryRow() {
            itemId.getItems().addAll(itemMap.keySet());
            itemName.setPromptText("Item Name");
            quantity.setPromptText("Qty");
            pricePerUnit.setPromptText("Price");
            supplier.setPromptText("Supplier");
            poNumber.setPromptText("PO Number");
            deliveryDate.setPromptText("Delivery Date");
            expirationDate.setPromptText("Expiration");

            // Optional: set preferred widths
            itemId.setPrefWidth(100);
            itemName.setPrefWidth(150);
            quantity.setPrefWidth(60);
            pricePerUnit.setPrefWidth(80);
            supplier.setPrefWidth(120);
            deliveryDate.setPrefWidth(120);
            expirationDate.setPrefWidth(120);
            poNumber.setPrefWidth(100);

            // Auto-fill on item selection
            itemId.setOnAction(e -> {
                InventoryItem item = itemMap.get(itemId.getValue());
                if (item != null) {
                    itemName.setText(item.getItemName());
                    pricePerUnit.setText(String.valueOf(item.getPricePerUnit()));
                    supplier.setText(item.getSupplier());
                }
            });
        }

        InventoryDelivery toDelivery() {
            if (itemId.getValue() == null || itemId.getValue().isEmpty()) throw new IllegalArgumentException("Item ID is required.");
            if (deliveryDate.getValue() == null) throw new IllegalArgumentException("Delivery Date is required.");
            if (expirationDate.getValue() == null) throw new IllegalArgumentException("Expiration Date is required.");

            return new InventoryDelivery(
                    itemId.getValue(),
                    itemName.getText(),
                    Float.parseFloat(quantity.getText()),
                    deliveryDate.getValue(),
                    expirationDate.getValue(),
                    Float.parseFloat(pricePerUnit.getText()),
                    supplier.getText(),
                    poNumber.getText()
            );
        }
    }
}




        




