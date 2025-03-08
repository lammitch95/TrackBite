package chooser.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<InventoryItem> inventory;

    public Inventory() {
        this.inventory = FXCollections.observableArrayList();
    }

    public void addItem(String itemName, int quantity, double price) {
        inventory.add(new InventoryItem(itemName, quantity, price));
    }

    public ObservableList<InventoryItem> getInventory() {
        return inventory;
    }
}
