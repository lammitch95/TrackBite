package chooser.viewmodel;

import javafx.beans.property.*;

import java.time.LocalDate;

public class DeliveryInputRow {
    private final SimpleStringProperty itemId = new SimpleStringProperty();
    private final SimpleStringProperty itemName = new SimpleStringProperty();
    private final SimpleFloatProperty quantity = new SimpleFloatProperty();
    private final SimpleFloatProperty pricePerUnit = new SimpleFloatProperty();
    private final SimpleStringProperty supplier = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> deliveryDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> expirationDate = new SimpleObjectProperty<>();
    private final SimpleStringProperty poNumber = new SimpleStringProperty();

    // Item ID
    public String getItemId() {
        return itemId.get();
    }

    public void setItemId(String value) {
        itemId.set(value);
    }

    public StringProperty itemIdProperty() {
        return itemId;
    }

    // Item Name
    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String value) {
        itemName.set(value);
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    // Quantity
    public float getQuantity() {
        return quantity.get();
    }

    public void setQuantity(float value) {
        quantity.set(value);
    }

    public FloatProperty quantityProperty() {
        return quantity;
    }

    // Price Per Unit
    public float getPricePerUnit() {
        return pricePerUnit.get();
    }

    public void setPricePerUnit(float value) {
        pricePerUnit.set(value);
    }

    public FloatProperty pricePerUnitProperty() {
        return pricePerUnit;
    }

    // Supplier
    public String getSupplier() {
        return supplier.get();
    }

    public void setSupplier(String value) {
        supplier.set(value);
    }

    public StringProperty supplierProperty() {
        return supplier;
    }

    // Delivery Date
    public LocalDate getDeliveryDate() {
        return deliveryDate.get();
    }

    public void setDeliveryDate(LocalDate value) {
        deliveryDate.set(value);
    }

    public ObjectProperty<LocalDate> deliveryDateProperty() {
        return deliveryDate;
    }

    // Expiration Date
    public LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public void setExpirationDate(LocalDate value) {
        expirationDate.set(value);
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }

    // PO Number
    public String getPoNumber() {
        return poNumber.get();
    }

    public void setPoNumber(String value) {
        poNumber.set(value);
    }

    public StringProperty poNumberProperty() {
        return poNumber;
    }
}
