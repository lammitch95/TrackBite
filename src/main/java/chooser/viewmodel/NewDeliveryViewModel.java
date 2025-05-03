package chooser.viewmodel;

import com.google.cloud.firestore.Firestore;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class NewDeliveryViewModel {

    private final StringProperty deliveryId = new SimpleStringProperty();
    private final StringProperty orderNumber = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> deliveryDate = new SimpleObjectProperty<>();
    private final StringProperty deliveryTime = new SimpleStringProperty();
    private final StringProperty deliveryAddress = new SimpleStringProperty();
    private final StringProperty itemName = new SimpleStringProperty();
    private final IntegerProperty itemQuantity = new SimpleIntegerProperty();
    private final StringProperty unit = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final StringProperty supplierFirstName = new SimpleStringProperty();
    private final StringProperty supplierLastName = new SimpleStringProperty();
    private final StringProperty supplierContactNum = new SimpleStringProperty();
    private final StringProperty supplierAddress = new SimpleStringProperty();

    private final BooleanBinding allowSave;
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public NewDeliveryViewModel() {
        // Generate unique delivery ID and order number in format DEL-XXX and ORD-XXX
        int nextId = idCounter.incrementAndGet();
        deliveryId.set(String.format("DEL-%03d", nextId));
        orderNumber.set(String.format("ORD-%03d", nextId));

        // Define conditions for enabling save
        allowSave = deliveryId.isNotEmpty()
                .and(orderNumber.isNotEmpty())
                .and(deliveryDate.isNotNull())
                .and(deliveryTime.isNotEmpty())
                .and(deliveryAddress.isNotEmpty())
                .and(itemName.isNotEmpty())
                .and(itemQuantity.greaterThan(0))
                .and(unit.isNotEmpty())
                .and(category.isNotEmpty())
                .and(priority.isNotEmpty())
                .and(supplierFirstName.isNotEmpty())
                .and(supplierLastName.isNotEmpty())
                .and(supplierContactNum.isNotEmpty())
                .and(supplierAddress.isNotEmpty());
    }

    public StringProperty deliveryIdProperty() {
        return deliveryId;
    }

    public StringProperty orderNumberProperty() {
        return orderNumber;
    }

    public ObjectProperty<LocalDate> deliveryDateProperty() {
        return deliveryDate;
    }

    public StringProperty deliveryTimeProperty() {
        return deliveryTime;
    }

    public StringProperty deliveryAddressProperty() {
        return deliveryAddress;
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public IntegerProperty itemQuantityProperty() {
        return itemQuantity;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public StringProperty supplierFirstNameProperty() {
        return supplierFirstName;
    }

    public StringProperty supplierLastNameProperty() {
        return supplierLastName;
    }

    public StringProperty supplierContactNumProperty() {
        return supplierContactNum;
    }

    public StringProperty supplierAddressProperty() {
        return supplierAddress;
    }

    public BooleanBinding allowSaveProperty() {
        return allowSave;
    }

    public boolean onSubmit() {
        try {
            // Save to Firestore (implement as needed)
            Firestore db = com.google.firebase.cloud.FirestoreClient.getFirestore();
            // Example: db.collection("Deliveries").document(deliveryId.get()).set(...);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}