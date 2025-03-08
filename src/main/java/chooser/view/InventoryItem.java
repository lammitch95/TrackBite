package chooser.view;

import javafx.beans.value.ObservableValue;

public class InventoryItem {
    private String itemName;
    private int quantity;
    private double price;

    public InventoryItem(String itemName, int quantity, double price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public InventoryItem(String itemId, String itemName, String unit, String category) {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addDelivery(InventoryDelivery delivery) {

    }

    public float getQuantityTotal() {
        return 0;
    }



    public ObservableValue<String> itemIdProperty() {
        return null;
    }

    public ObservableValue<String> itemNameProperty() {
        return null;
    }

    public ObservableValue<String> unitProperty() {
        return null;
    }
}

