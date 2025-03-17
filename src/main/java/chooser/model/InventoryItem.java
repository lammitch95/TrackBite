package chooser.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryItem {
    private final String itemId;
    private final String itemName;
    private final String unit;
    private final String category;
    private final String quantity;

    //constructor for invItem class
    public InventoryItem(String itemId, String ItemName, String unit, String category, String quantity) {

        this.itemId = itemId;  // itemId = String, letters and 3 random digits
        this.itemName = ItemName;
        this.unit = unit;
        this.category = category;
        this.quantity = quantity;
        // initialize quantity to 0
    }

    // getter methods
    public String itemIdProperty() {
        return itemId;
    }

    public String itemNameProperty() {
        return itemName;
    }

    public String unitProperty() {
        return unit;
    }

    public String categoryProperty() {
        return category;
    }

    public String quantityProperty() {
        return quantity;
    }


    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getStockStatus() {
        float qty = 0;
        try {
            qty = Float.parseFloat(quantity);
        } catch (NumberFormatException e) {
            System.err.println("Invalid quantity value: " + quantity);
        }
        if (qty == 0) {
            return "Out of Stock";
        } else if (qty < 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

}
