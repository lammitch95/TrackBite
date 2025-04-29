package chooser.model;

import java.time.LocalDate;

public class InventoryItem {
    private final String itemId;
    private final String itemName;
    private final String unit;
    private final String category;
    private final String quantity;
    private final float pricePerUnit;
    private final String supplier;

    //constructor for invItem class
    public InventoryItem(String itemId, String ItemName, String unit, String category, String quantity, float pricePerUnit, String supplier) {

        this.itemId = itemId;  // itemId = String, letters and 3 random digits
        this.itemName = ItemName;
        this.unit = unit;
        this.category = category;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.supplier = (supplier != null) ? supplier : "N/A";
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

    public String supplierProperty() { return supplier; }


    public String getSupplier() {
        return supplier;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public String getUnit() {
        return unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCategory() { return category; }

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
