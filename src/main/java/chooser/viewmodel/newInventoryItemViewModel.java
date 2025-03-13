package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class newInventoryItemViewModel {
    public StringProperty itemName = new SimpleStringProperty();
    public StringProperty unit = new SimpleStringProperty();
    public StringProperty category = new SimpleStringProperty();

    public StringProperty itemNameProperty() {
        return itemName;
    }




    private String generateItemId(String itemName) {
        String prefix = itemName.length() >= 3 ? itemName.substring(0, 3).toUpperCase() : itemName.toUpperCase();
        String suffix = String.format("%03d", new Random().nextInt(1000));
        return prefix + suffix;
    }


    public void onSubmit(){

        if (itemName.get().isEmpty() ||  unit.get().isEmpty() || category.get().isEmpty()) {
            System.out.println("Invalid Submission, Please fill out all the fields");
        }  else {
            System.out.println("Submission Successful");

            String itemId = generateItemId(itemName.get());

            Map<String, Object>  map = new HashMap<>();
            map.put("InventoryItemID", itemId);
            map.put("unit", unit.get());
            map.put("category", category.get());
            map.put("itemName", itemName.get());
            map.put("quantity", 0);

            FirestoreUtils.writeDoc("Inventory", itemId, map);

            System.out.println("Successfully Storing Date for Inventory Item");
        }
    }

    private void FirestoreUtils() {
    }

}
