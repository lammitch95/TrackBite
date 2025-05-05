package chooser.viewmodel;

import chooser.model.DeliveryItemTable;
import chooser.database.FirestoreUtils;
import com.google.cloud.Timestamp;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NewDeliveryViewModel {
    // Delivery info
    private final StringProperty deliveryId      = new SimpleStringProperty("");
    private final StringProperty orderNumber     = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> deliveryDate = new SimpleObjectProperty<>();
    private final StringProperty deliveryTime    = new SimpleStringProperty("");
    private final StringProperty deliveryTimeUnit= new SimpleStringProperty("");
    private final StringProperty deliveryAddress = new SimpleStringProperty("");
    // Delivery List
    private final StringProperty itemName        = new SimpleStringProperty("");
    private final IntegerProperty itemQuantity   = new SimpleIntegerProperty(0);
    private final StringProperty unit            = new SimpleStringProperty("");
    private final StringProperty priority        = new SimpleStringProperty("");
    private final StringProperty notes           = new SimpleStringProperty("");
    //private final DoubleProperty price           = new SimpleDoubleProperty(0);
    // Supplier Info
    private final StringProperty supplierSearch = new SimpleStringProperty(this, "");
    private final StringProperty supplierFirstName = new SimpleStringProperty(this, "");
    private final StringProperty supplierLastName = new SimpleStringProperty(this, "");
    private final StringProperty supplierContactNum = new SimpleStringProperty(this, "");
    private final StringProperty supplierAddress = new SimpleStringProperty(this, "");

    // Property accessors
    public StringProperty deliveryIdProperty() { return deliveryId; }
    public StringProperty orderNumberProperty() { return orderNumber; }
    public ObjectProperty<LocalDate> deliveryDateProperty() { return deliveryDate; }
    public StringProperty deliveryTimeProperty() { return deliveryTime; }
    public StringProperty deliveryTimeUnitProperty() { return deliveryTimeUnit; }
    public StringProperty deliveryAddressProperty() { return deliveryAddress; }
    public StringProperty itemNameProperty() { return itemName; }
    public IntegerProperty itemQuantityProperty() { return itemQuantity; }
    public StringProperty unitProperty() { return unit; }
    //public StringProperty categoryProperty() { return category; }
    public StringProperty priorityProperty() { return priority; }
    public StringProperty notesProperty() { return notes; }
    public StringProperty supplierSearchProperty() { return supplierSearch; }
    public StringProperty supplierFirstNameProperty() { return supplierFirstName; }
    public StringProperty supplierLastNameProperty() { return supplierLastName; }
    public StringProperty supplierContactNumProperty() { return supplierContactNum; }
    public StringProperty supplierAddressProperty() { return supplierAddress; }

    public BooleanProperty formValidProperty() { return formValid; }
    public BooleanProperty allowSaveProperty() { return allowSave; }
    public BooleanProperty hasChangedProperty() { return hasChanged; }
    public BooleanProperty isNewRecordProperty() { return isNewRecord; }

    // Supplier list for the combo box (this could also be loaded dynamically)
    private final ObservableList<String> supplierList = FXCollections.observableArrayList();


    // Inner class for items
    public static class DeliveryItem {
        //private final StringProperty inventoryId;
        private final StringProperty itemName;
        private final StringProperty quantity;
        private final StringProperty uom;
        private final StringProperty priority;
        //private final DoubleProperty price;


        public DeliveryItem(String inventoryId, String itemName, String quantity, String uom, double price, String priority) {
            //this.inventoryId = new SimpleStringProperty(this, "inventoryId", inventoryId);
            this.itemName    = new SimpleStringProperty(this, "itemName",    itemName);
            this.quantity    = new SimpleStringProperty(this, "quantity",    quantity);
            this.uom         = new SimpleStringProperty(this, "uom",         uom);
            //this.price       = new SimpleDoubleProperty(this, "price",       price);
            this.priority    = new SimpleStringProperty(this, "priority",    priority);
        }

        //public StringProperty inventoryIdProperty() { return inventoryId; }
        public StringProperty itemNameProperty()    { return itemName;    }
        public StringProperty quantityProperty()    { return quantity;    }
        public StringProperty uomProperty()         { return uom;         }
        //public DoubleProperty priceProperty()       { return price;       }
        public StringProperty priorityProperty()    { return priority;    }
    }

    // List for TableView
    private final ObservableList<DeliveryItemTable> deliveryItems = FXCollections.observableArrayList();
    public ObservableList<DeliveryItemTable> getDeliveryItems() { return deliveryItems; }

    // Add item including priority
    public void addDeliveryItem(String name, String qty, String uom, String priority) {
        if (    name           != null && !name       .isBlank()                           //inventoryId != null && !inventoryId.isBlank()
                && qty         != null && !qty        .isBlank()
                && uom         != null && !uom        .isBlank()
                && priority    != null && !priority   .isBlank()) {
            //double fetchedPrice = fetchPriceForInventory(inventoryId);
            deliveryItems.add(new DeliveryItemTable(name, qty, uom, priority));    //fetchedPrice,
        }
    }

    // Clear items
    public void clearDeliveryItems() {
        deliveryItems.clear();
        hasChanged.set(true);
    }


    // Validation flags
    private final BooleanProperty validOrderNumber = new SimpleBooleanProperty(false);
    private final BooleanProperty validDeliveryDate = new SimpleBooleanProperty(false);
    private final BooleanProperty validDeliveryTime = new SimpleBooleanProperty(false);
    private final BooleanProperty validItemName = new SimpleBooleanProperty(false);
    private final BooleanProperty validItemQuantity = new SimpleBooleanProperty(false);

    // Form state
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(true);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);

    public NewDeliveryViewModel() {
        deliveryId.set(generateDeliveryID());
        orderNumber.set(generateOrderNumber());

        List<BooleanProperty> validityList = List.of(
                validOrderNumber,
                validDeliveryDate,
                validDeliveryTime,
                validItemName,
                validItemQuantity
        );
        List<Property<?>> inputList = List.of(
                orderNumber,
                deliveryDate,
                deliveryTime,
                deliveryAddress,
                itemName,
                itemQuantity.asObject()
        );

        // Listeners
        orderNumber.addListener((obs, oldV, newV) -> {
            validOrderNumber.set(newV != null && !newV.isEmpty());
            hasChanged.set(true);
        });
        deliveryDate.addListener((obs, oldV, newV) -> {
            validDeliveryDate.set(newV != null);
            hasChanged.set(true);
        });
        deliveryTime.addListener((obs, oldV, newV) -> {
            validDeliveryTime.set(newV != null);
            hasChanged.set(true);
        });
        itemName.addListener((obs, oldV, newV) -> {
            validItemName.set(newV != null && !newV.isEmpty());
            hasChanged.set(true);
        });
        itemQuantity.addListener((obs, oldV, newV) -> {
            validItemQuantity.set(newV != null && newV.intValue() > 0);
            hasChanged.set(true);
        });

        // Bind formValid & allowSave
        formValid.bind(
                validDeliveryDate
                        .and(validDeliveryTime)
                        .and(validItemName)
                        .and(validItemQuantity)
        );
        allowSave.bind(formValid.and(hasChanged));
    }

    // Generates a unique delivery ID e.g. "DEL-1234"
    private String generateDeliveryID() {
        Random rand = new Random();
        int num = rand.nextInt(9000) + 1000;
        return "DEL-" + num;
    }

    // Generates a unique order number e.g. "ORD-1234"
    private String generateOrderNumber() {
        Random rand = new Random();
        int num = rand.nextInt(9000) + 1000;
        return "ORD-" + num;
    }

//    // Adds a new item to the list if both name and quantity are not empty.
//    public void addDeliveryItem(String name, String quantity) {
//        if (name != null && !name.trim().isEmpty() && quantity != null && !quantity.trim().isEmpty()) {
//            deliveryItems.add(new DeliveryItem(name, quantity));
//        }
//    }

    // Clears all items from the delivery items list.
//    public void clearDeliveryItems() {
//        deliveryItems.clear();
//        hasChanged.set(true);
//    }


    // Submits the delivery form by validating basic fields and writing to Firestore.
    public boolean onSubmit() {
        if (!formValid.get()) {
            System.out.println("Form validation failed.");
            return false;
        }

        // Prepare data map
        Map<String, Object> data = new HashMap<>();
        data.put("deliveryID", deliveryId.get());
        data.put("orderNumber", orderNumber.get());
        data.put("deliveryDate", deliveryDate.get() != null ? Timestamp.of(java.sql.Date.valueOf(deliveryDate.get())) : null);
        data.put("deliveryTime", deliveryTime.get());
        data.put("deliveryTimeUnit", deliveryTimeUnit.get());
        data.put("deliveryAddress", deliveryAddress.get());
        // Supplier fields
        data.put("supplierName", supplierSearch.get());
        data.put("supplierFirstName", supplierFirstName.get());
        data.put("supplierLastName", supplierLastName.get());
        data.put("supplierContactNum", supplierContactNum.get());
        data.put("supplierAddress", supplierAddress.get());
        // Delivery list fields
        data.put("itemName", itemName.get());
        data.put("itemQuantity", itemQuantity.get());
        data.put("unit", unit.get());
        //data.put("category", category.get());
        data.put("priority", priority.get());
        data.put("notes", notes.get());

        // Combine delivery items
        List<Map<String,Object>> itemsList = new java.util.ArrayList<>();
        for (DeliveryItemTable di : deliveryItems) {
            Map<String,Object> m = new HashMap<>();
            //m.put("inventoryId", di.getInventoryId());
            m.put("itemName",    di.getItemName());
            m.put("quantity",    di.getQuantity());
            m.put("uom",         di.getUom());
            //m.put("price",       di.getPrice());
            m.put("priority",    di.getPriority());
            itemsList.add(m);
        }
        data.put("deliveryItems", itemsList);

        // Debug output
        System.out.println("Writing to Firestore 'Deliveries' collection with data:");
        System.out.println("Delivery ID: " + deliveryId.get());
        System.out.println("Order Number: " + orderNumber.get());
        System.out.println("Delivery Date: " + (deliveryDate.get() != null ? deliveryDate.get() : "null"));
        System.out.println("Delivery Time: " + deliveryTime.get());
        System.out.println("Delivery Time Unit: " + deliveryTimeUnit.get());
        System.out.println("Delivery Address: " + deliveryAddress.get());
        System.out.println("Supplier Name: " + supplierSearch.get());
        System.out.println("Supplier First Name: " + supplierFirstName.get());
        System.out.println("Supplier Last Name: " + supplierLastName.get());
        System.out.println("Supplier Contact Num: " + supplierContactNum.get());
        System.out.println("Supplier Address: " + supplierAddress.get());
        System.out.println("Item Name: " + itemName.get());
        System.out.println("Item Quantity: " + itemQuantity.get());
        System.out.println("Unit: " + unit.get());
        //System.out.println("Category: " + category.get());
        System.out.println("Priority: " + priority.get());
        System.out.println("Notes: " + notes.get());
        System.out.println("Delivery Items: " + itemsList);

        try {
            FirestoreUtils.writeDoc("Deliveries", deliveryId.get(), data);
            System.out.println("Delivery data successfully written to Firestore 'Deliveries' collection.");
            resetForm();
            return true;
        } catch (Exception e) {
            System.err.println("Error writing to Firestore 'Deliveries' collection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Resets all form fields and generates a new Delivery ID.
    public void resetForm() {
        deliveryId.set(generateDeliveryID());
        orderNumber.set(generateOrderNumber());
        deliveryDate.set(null);
        deliveryTime.set("");
        deliveryTimeUnit.set("");
        deliveryAddress.set("");
        supplierSearch.set("");
        supplierFirstName.set("");
        supplierLastName.set("");
        supplierContactNum.set("");
        supplierAddress.set("");
        itemName.set("");
        itemQuantity.set(0);
        unit.set("");
        //category.set("");
        priority.set("");
        notes.set("");
        deliveryItems.clear();
        hasChanged.set(false);
    }
}