package chooser.view;


import chooser.Inventory;
import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.model.InventoryItem;
import chooser.utils.SceneNavigator;
import com.google.cloud.firestore.DocumentSnapshot;
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
    @FXML
    private ComboBox<String> deliveryIdDropdown;
    @FXML
    private VBox deliveryItemsContainer;
    @FXML
    private Button backToMainSelected;


    private final Map<String, InventoryItem> itemMap = new HashMap<>();
    private final List<DeliveryRow> deliveryRows = new ArrayList<>();
    private Map<String, DocumentSnapshot> deliveryMap = new HashMap<>();


    @FXML
    private void initialize() {
        List<InventoryItem> items = FirestoreUtils.readCollection("Inventory");
        System.out.println("Loaded inventory items: " + (items != null ? items.size() : "null"));
        for (InventoryItem item : items) {
            itemMap.put(item.getItemId(), item);
        }
        handleAddItemRow();

        List<DocumentSnapshot> unsubmitted = FirestoreUtils.getUnsubmittedDeliveries();
        System.out.println("Loaded unsubmitted deliveries: " + (unsubmitted != null ? unsubmitted.size() : "null"));
        for (DocumentSnapshot doc : unsubmitted) {
            String id = doc.getId();
            deliveryIdDropdown.getItems().add(id);
            deliveryMap.put(id, doc);
            deliveryIdDropdown.setOnAction(e -> loadDeliveryItems(deliveryIdDropdown.getValue()));
        }

    }


    private void loadDeliveryItems(String deliveryId) {
        deliveryItemsContainer.getChildren().clear();
        deliveryRows.clear();

        DocumentSnapshot doc = deliveryMap.get(deliveryId);
        if (doc == null) return;

        List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");
        String supplierName = "Unknown";
        Map<String, Object> supplierData = (Map<String, Object>) doc.get("supplier");
        if (supplierData != null && supplierData.get("supplierName") != null) {
            supplierName = supplierData.get("supplierName").toString();
        }


        System.out.println("SUPPLIER NAME FROM FIRESTORE: " + supplierName);

        for (Map<String, Object> item : items) {
            DeliveryRow row = new DeliveryRow();

            String name = (String) item.get("name");
            float quantity = ((Number) item.get("quantity")).floatValue();
            String unit = (String) item.get("unit");

            // lookup or create itemId
            String itemId = findOrCreateInventoryItem(name, unit, item);

            InventoryItem invItem = itemMap.get(itemId);
            if (invItem != null) {
                row.itemId.setValue(itemId);
                row.itemName.setText(invItem.getItemName());
                row.quantity.setText(String.valueOf(quantity));
                row.pricePerUnit.setText(String.valueOf(invItem.getPricePerUnit()));
                row.supplier.setText(supplierName);
                row.deliveryDate.setValue(LocalDate.now());
                row.expirationDate.setValue(LocalDate.now().plusDays(7));
            }

            deliveryRows.add(row);
            deliveryItemsContainer.getChildren().add(row.rowUI);
        }
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
                        "supplier", delivery.getSupplier()

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
        handleAddItemRow();
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


    private class DeliveryRow {
        ComboBox<String> itemId = new ComboBox<>();
        TextField itemName = new TextField();
        TextField quantity = new TextField();
        TextField pricePerUnit = new TextField();
        TextField supplier = new TextField();
        DatePicker deliveryDate = new DatePicker();
        DatePicker expirationDate = new DatePicker();


        HBox rowUI = new HBox(10, itemId, itemName, quantity, pricePerUnit, supplier, deliveryDate, expirationDate);

        DeliveryRow() {
            itemId.getItems().addAll(itemMap.keySet());
            itemName.setPromptText("Item Name");
            quantity.setPromptText("Qty");
            pricePerUnit.setPromptText("Price");
            supplier.setPromptText("Supplier");
            deliveryDate.setPromptText("Delivery Date");
            expirationDate.setPromptText("Expiration");


            itemId.setPrefWidth(100);
            itemName.setPrefWidth(150);
            quantity.setPrefWidth(60);
            pricePerUnit.setPrefWidth(80);
            supplier.setPrefWidth(120);
            deliveryDate.setPrefWidth(120);
            expirationDate.setPrefWidth(120);


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
            if (itemId.getValue() == null || itemId.getValue().isEmpty())
                throw new IllegalArgumentException("Item ID is required.");
            if (deliveryDate.getValue() == null) throw new IllegalArgumentException("Delivery Date is required.");
            if (expirationDate.getValue() == null) throw new IllegalArgumentException("Expiration Date is required.");

            return new InventoryDelivery(
                    itemId.getValue(),
                    itemName.getText(),
                    Float.parseFloat(quantity.getText()),
                    deliveryDate.getValue(),
                    expirationDate.getValue(),
                    Float.parseFloat(pricePerUnit.getText()),
                    supplier.getText()
            );
        }
    }


        private String findOrCreateInventoryItem(String name, String unit, Map<String, Object> item) {
            for (InventoryItem inv : itemMap.values()) {
                if (inv.getItemName().equalsIgnoreCase(name)) {
                    // Update unit/category
                    boolean updated = false;
                    if (!inv.getUnit().equals(unit)) {
                        inv = new InventoryItem(inv.getItemId(), name, unit,
                                inv.getCategory(), inv.getQuantity(),
                                inv.getPricePerUnit(), inv.getSupplier());
                        updated = true;
                    }
                    if (updated) {
                        FirestoreUtils.writeDoc("Inventory", inv.getItemId(), Map.of(
                                "InventoryItemID", inv.getItemId(),
                                "itemName", inv.getItemName(),
                                "unit", inv.getUnit(),
                                "category", inv.getCategory(),
                                "quantity", inv.getQuantity(),
                                "pricePerUnit", inv.getPricePerUnit(),
                                "supplier", inv.getSupplier()
                        ));
                    }
                    return inv.getItemId();
                }
            }

            // if not found, create new one
            String itemId = name.substring(0, 3).toUpperCase() + String.format("%03d", new Random().nextInt(1000));
            InventoryItem newItem = new InventoryItem(itemId, name, unit, "Unknown", "0", 0f, "Unknown");
            FirestoreUtils.writeDoc("Inventory", itemId, Map.of(
                    "InventoryItemID", itemId,
                    "itemName", name,
                    "unit", unit,
                    "category", "Unknown",
                    "quantity", 0,
                    "pricePerUnit", 0.0,
                    "supplier", "Unknown"
            ));
            itemMap.put(itemId, newItem);
            return itemId;
        }


    }





        




