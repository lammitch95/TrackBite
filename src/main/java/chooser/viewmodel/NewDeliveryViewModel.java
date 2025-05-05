package chooser.viewmodel;

import chooser.view.NewDeliveryFormController.DeliveryItem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class NewDeliveryViewModel {
    private final StringProperty deliveryId = new SimpleStringProperty("");
    private final StringProperty orderNumber = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> deliveryDate = new SimpleObjectProperty<>();
    private final StringProperty deliveryTime = new SimpleStringProperty("");
    private final StringProperty deliveryTimeUnit = new SimpleStringProperty("");
    private final StringProperty deliveryAddress = new SimpleStringProperty("");
    private final StringProperty supplierFirstName = new SimpleStringProperty("");
    private final StringProperty supplierLastName = new SimpleStringProperty("");
    private final StringProperty supplierContactNum = new SimpleStringProperty("");
    private final StringProperty supplierAddress = new SimpleStringProperty("");
    private final ObservableList<DeliveryItem> deliveryItems = FXCollections.observableArrayList();
    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);

    public NewDeliveryViewModel() {
        // Enable submit button when items are present
        allowSave.bind(Bindings.createBooleanBinding(
                () -> !deliveryItems.isEmpty(),
                deliveryItems
        ));
        deliveryItems.addListener((ListChangeListener<DeliveryItem>) change -> {
            System.out.println("deliveryItems changed, size: " + deliveryItems.size() + ", allowSave: " + allowSave.get());
        });
    }

    public StringProperty deliveryIdProperty() { return deliveryId; }
    public StringProperty orderNumberProperty() { return orderNumber; }
    public ObjectProperty<LocalDate> deliveryDateProperty() { return deliveryDate; }
    public StringProperty deliveryTimeProperty() { return deliveryTime; }
    public StringProperty deliveryTimeUnitProperty() { return deliveryTimeUnit; }
    public StringProperty deliveryAddressProperty() { return deliveryAddress; }
    public StringProperty supplierFirstNameProperty() { return supplierFirstName; }
    public StringProperty supplierLastNameProperty() { return supplierLastName; }
    public StringProperty supplierContactNumProperty() { return supplierContactNum; }
    public StringProperty supplierAddressProperty() { return supplierAddress; }
    public ObservableList<DeliveryItem> getDeliveryItems() { return deliveryItems; }
    public BooleanProperty allowSaveProperty() { return allowSave; }

    public void reset() {
        System.out.println("Resetting ViewModel");
        deliveryId.set("");
        orderNumber.set("");
        deliveryDate.set(null);
        deliveryTime.set("");
        deliveryTimeUnit.set("");
        deliveryAddress.set("");
        supplierFirstName.set("");
        supplierLastName.set("");
        supplierContactNum.set("");
        supplierAddress.set("");
        deliveryItems.clear();
        System.out.println("ViewModel reset, deliveryItems size: " + deliveryItems.size());
    }
}