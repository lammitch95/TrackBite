package chooser;

import javafx.application.Application;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;



public class Inventory extends Application {

    // define InventoryItem class
    public static class InventoryItem {
        private final StringProperty itemId;
        private final StringProperty itemName;
        private final StringProperty unit;
        private final StringProperty category;
        private final FloatProperty quantityTotal;

        //constructor for invItem class
        public InventoryItem(String itemId, String itemName, String unit, String category) {
            this.itemId = new SimpleStringProperty(itemId);  // itemId = String, letters and 3 random digits
            this.itemName = new SimpleStringProperty(itemName);
            this.unit = new SimpleStringProperty(unit);
            this.category = new SimpleStringProperty(category);
            this.quantityTotal = new SimpleFloatProperty(0); // initialize quantity to 0
        }

        // getter methods
        public StringProperty itemIdProperty() {
            return itemId;
        }

        public StringProperty itemNameProperty() {
            return itemName;
        }

        public StringProperty unitProperty() {
            return unit;
        }

        public StringProperty categoryProperty() {
            return category;
        }

        public FloatProperty quantityTotalProperty() {
            return quantityTotal;
        }


        public String getItemName() {
            return itemName.get();
        }
        //calculates the total quantity to be delievered plus current
        public void addDelivery(InventoryDelivery delivery) {
            quantityTotal.set(quantityTotal.get() + delivery.getQuantity());
        }

        @Override
        public String toString() {
            return "Item ID=" + itemId.get() + " , Item Name='" + itemName.get() + "' , Total Quantity=" + quantityTotal.get() +
                    " " + unit.get() + ", Category='" + category.get() + "'";
        }

        public String getItemId() {
            return itemId.get();
        }
    }

    // InventoryDelivery class
    public static class InventoryDelivery {
        private final String itemId;
        private final String itemName;
        private final float quantity;
        private final LocalDate deliveryDate;
        private final LocalDate expDate;
        private final float pricePerUnit;

        public InventoryDelivery(String itemId, String itemName, float quantity, LocalDate deliveryDate, LocalDate expDate, float pricePerUnit) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.deliveryDate = deliveryDate;
            this.expDate = expDate;
            this.pricePerUnit = pricePerUnit;
        }

        public String getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public float getQuantity() {
            return quantity;
        }

        public LocalDate getDeliveryDate() {
            return deliveryDate;
        }

        public LocalDate getExpDate() {
            return expDate;
        }

        public float getPricePerUnit() {
            return pricePerUnit;
        }
    }

    // add,view,delete inventory items
    public static class InventoryManager {
        private static final List<InventoryItem> inventoryItems = new ArrayList<>();

        public static void addInventoryItem(String itemId, String itemName, String unit, String category) {
            InventoryItem newItem = new InventoryItem(itemId, itemName, unit, category);
            inventoryItems.add(newItem);
        }

        public static List<InventoryItem> getInventoryItems() {
            return inventoryItems;
        }

        public static InventoryItem getInventoryItemById(String itemId) {
            for (InventoryItem item : inventoryItems) {
                if (item.getItemId().equals(itemId)) {
                    return item;
                }
            }
            return null;
        }

        public static void deleteInventoryItem(String itemId) {
            inventoryItems.removeIf(item -> item.getItemId().equals(itemId));
        }
    }

    // calculation to determine stock status
    private String getStockStatus(float quantity) {
        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity < 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Main Menu
        Button newItemButton = new Button("Enter a new Inventory Item");
        Button enterDeliveryButton = new Button("Enter a Delivery");
        Button viewInventoryButton = new Button("View Current Inventory");


        // Add Item, Enter Delivery, View Inventory
        newItemButton.setOnAction(e -> showNewItemForm(primaryStage));
        enterDeliveryButton.setOnAction(e -> showEnterDeliveryForm(primaryStage));
        viewInventoryButton.setOnAction(e -> showInventoryView(primaryStage));


        // Main Menu Screen
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(
                newItemButton, enterDeliveryButton, viewInventoryButton);


        Scene mainScene = new Scene(mainLayout, 500, 800);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // Add a new item
    private void showNewItemForm(Stage primaryStage) {
        Label itemIdLabel = new Label("Item ID:");
        TextField itemIdField = new TextField();
        itemIdField.setDisable(true);  // need to delete
        Label itemNameLabel = new Label("Item Name:");
        TextField itemNameField = new TextField();

        //drop down for unit
        Label unitLabel = new Label("Unit:");
        ComboBox<String> unitComboBox = new ComboBox<>();
        unitComboBox.getItems().addAll("Lbs", "Oz", "Gal", "Pieces");  // Add the unit options

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Meat", "Dairy", "Bread", "Produce", "Condiments");

        Button addButton = new Button("Add Inventory Item");
        Button backButton = new Button("Back to Main Menu");

        addButton.setOnAction(e -> {
            try {
                String itemName = itemNameField.getText();
                String unit = unitComboBox.getValue(); // unit selection from drop down
                String category = categoryComboBox.getValue();

                if (!itemName.isEmpty() && unit != null && category != null) {
                    //  generate itemId
                    String itemId = generateItemId(itemName);
                    InventoryManager.addInventoryItem(itemId, itemName, unit, category);
                    clearInputFields(itemNameField, unitComboBox, categoryComboBox);
                } else {
                    showAlert("Error", "All fields must be filled!");
                }
            } catch (Exception ex) {
                showAlert("Error", "Please enter valid data!");
            }
        });

        backButton.setOnAction(e -> showMainMenu(primaryStage));

        // Switch to New Item Form Layout
        VBox newItemLayout = new VBox(10);
        newItemLayout.getChildren().addAll(
                itemIdLabel, itemIdField, itemNameLabel, itemNameField, unitLabel, unitComboBox, categoryLabel, categoryComboBox, addButton, backButton);

        switchToLayout(primaryStage, newItemLayout);
    }

    // generate Item ID with first 3 letters and 3 random digits
    private String generateItemId(String itemName) {
        String prefix = itemName.length() >= 3 ? itemName.substring(0, 3).toUpperCase() : itemName.toUpperCase();
        String suffix = String.format("%03d", new Random().nextInt(1000));
        return prefix + suffix;
    }

    // Enter delivery
    private void showEnterDeliveryForm(Stage primaryStage) {

        Label itemIdLabel = new Label("Item ID:");
        TextField itemIdField = new TextField();
        Label itemNameLabel = new Label("Item Name:");
        TextField itemNameField = new TextField();
        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        Label deliveryDateLabel = new Label("Delivery Date:");
        DatePicker deliveryDatePicker = new DatePicker();
        Label expDateLabel = new Label("Expiration Date:");
        DatePicker expDatePicker = new DatePicker();
        Label pricePerUnitLabel = new Label("Price per Unit:");
        TextField pricePerUnitField = new TextField();

        Button addDeliveryButton = new Button("Add Delivery");
        Button backButton = new Button("Back to Main Menu");

        addDeliveryButton.setOnAction(e -> {
            try {
                String itemId = itemIdField.getText();
                String itemName = itemNameField.getText();
                float quantity = Float.parseFloat(quantityField.getText());
                LocalDate deliveryDate = deliveryDatePicker.getValue();
                LocalDate expDate = expDatePicker.getValue();
                float pricePerUnit = Float.parseFloat(pricePerUnitField.getText());

                InventoryDelivery delivery = new InventoryDelivery(itemId, itemName, quantity, deliveryDate, expDate, pricePerUnit);
                InventoryItem item = InventoryManager.getInventoryItemById(itemId);
                if (item != null) {
                    item.addDelivery(delivery);
                    showAlert("Success", "Delivery added successfully!");
                } else {
                    showAlert("Error", "Item not found!");
                }
            } catch (Exception ex) {
                showAlert("Error", "Please enter valid data!");
            }
        });

        backButton.setOnAction(e -> showMainMenu(primaryStage));

        VBox deliveryLayout = new VBox(10);
        deliveryLayout.getChildren().addAll(
                itemIdLabel, itemIdField, itemNameLabel, itemNameField, quantityLabel, quantityField,
                deliveryDateLabel, deliveryDatePicker, expDateLabel, expDatePicker, pricePerUnitLabel, pricePerUnitField,
                addDeliveryButton, backButton);

        switchToLayout(primaryStage, deliveryLayout);
    }

    // View Current Inventory
    private void showInventoryView(Stage primaryStage) {
        // Table of Inventory view
        TableView<InventoryItem> tableView = new TableView<>();

        // table layout
        TableColumn<InventoryItem, String> itemIdColumn = new TableColumn<>("Item ID");
        itemIdColumn.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty());

        TableColumn<InventoryItem, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        TableColumn<InventoryItem, String> unitColumn = new TableColumn<>("Unit");
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());

        TableColumn<InventoryItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<InventoryItem, Float> quantityColumn = new TableColumn<>("Total Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityTotalProperty().asObject());

        TableColumn<InventoryItem, String> stockStatusColumn = new TableColumn<>("Stock Status");
        stockStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getStockStatus(cellData.getValue().quantityTotalProperty().get())));

        // delete feature moved to this table not its own form
        TableColumn<InventoryItem, Void> deleteColumn = new TableColumn<>("Action");
        deleteColumn.setCellFactory(param -> new TableCell<InventoryItem, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    InventoryItem item = getTableView().getItems().get(getIndex());
                    showDeleteConfirmationDialog(item, primaryStage);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        // columns for table in view inventory
        tableView.getColumns().add(itemIdColumn);
        tableView.getColumns().add(itemNameColumn);
        tableView.getColumns().add(unitColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(quantityColumn);
        tableView.getColumns().add(stockStatusColumn);
        tableView.getColumns().add(deleteColumn);

        // Add inventory items to the table
        tableView.getItems().addAll(InventoryManager.getInventoryItems());

        //  back button
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> showMainMenu(primaryStage));

        // Inventory View
        VBox inventoryLayout = new VBox(10);
        inventoryLayout.getChildren().addAll(tableView, backButton);


        switchToLayout(primaryStage, inventoryLayout);
    }
    private String getStockStatus(InventoryItem item) {
        float quantity = item.quantityTotal.get();
        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity < 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

    //  confirmation before deleting the item
    private void showDeleteConfirmationDialog(InventoryItem item, Stage primaryStage) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete the item?");
        confirmationAlert.setContentText("Item: " + item.getItemName() + "\nTotal in stock: " + item.quantityTotal.get());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // if confirmed, delete the item
            InventoryManager.deleteInventoryItem(item.getItemId());
            showAlert("Success", "Item deleted successfully!");
            showInventoryView(primaryStage); // updates the view inventory after deletion
        }
    }



    private void switchToLayout(Stage primaryStage, VBox layout) {
        Scene newScene = new Scene(layout, 500, 800);
        primaryStage.setScene(newScene);
    }

    // main menu
    private void showMainMenu(Stage primaryStage) {
        Button newItemButton = new Button("Enter a new Inventory Item");
        Button enterDeliveryButton = new Button("Enter a Delivery");
        Button viewInventoryButton = new Button("View Current Inventory");


        // for buttons
        newItemButton.setOnAction(e -> showNewItemForm(primaryStage));
        enterDeliveryButton.setOnAction(e -> showEnterDeliveryForm(primaryStage));
        viewInventoryButton.setOnAction(e -> showInventoryView(primaryStage));


        // main screen
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(
                newItemButton, enterDeliveryButton, viewInventoryButton);


        Scene mainScene = new Scene(mainLayout, 500, 800);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // clear fields
    private void clearInputFields(Control... controls) {
        for (Control control : controls) {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                ((ComboBox<?>) control).setValue(null); // Clear ComboBox
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


