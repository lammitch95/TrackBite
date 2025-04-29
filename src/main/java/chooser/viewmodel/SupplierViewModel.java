package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import javafx.beans.property.*;
import java.util.HashMap;
import java.util.Map;

public class SupplierViewModel {

    private final StringProperty supplierId = new SimpleStringProperty("");
    private final StringProperty supplierName = new SimpleStringProperty("");
    private final StringProperty personFirstName = new SimpleStringProperty("");
    private final StringProperty personLastName = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");
    private final StringProperty emailAddress = new SimpleStringProperty("");
    private final StringProperty websiteLink = new SimpleStringProperty("");
    private final StringProperty businessAddress = new SimpleStringProperty("");
    private final StringProperty warehouseAddress = new SimpleStringProperty("");
    private final StringProperty deliveryArea = new SimpleStringProperty("");

    public SupplierViewModel() {
        supplierId.set(generateSupplierId());
    }

    public StringProperty supplierIdProperty() { return supplierId; }
    public StringProperty supplierNameProperty() { return supplierName; }
    public StringProperty personFirstNameProperty() { return personFirstName; }
    public StringProperty personLastNameProperty() { return personLastName; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty emailAddressProperty() { return emailAddress; }
    public StringProperty websiteLinkProperty() { return websiteLink; }
    public StringProperty businessAddressProperty() { return businessAddress; }
    public StringProperty warehouseAddressProperty() { return warehouseAddress; }
    public StringProperty deliveryAreaProperty() { return deliveryArea; }

    private String generateSupplierId() {
        return "SUP-" + (int) (Math.random() * 1000);
    }

    public boolean onSubmit() {
        System.out.println("onSubmit called");

        Map<String, Object> supplierData = new HashMap<>();
        supplierData.put("supplierID", supplierId.get());
        supplierData.put("supplierName", supplierName.get());
        supplierData.put("contactPerson", personFirstName.get() + " " + personLastName.get());
        supplierData.put("phoneNumber", phoneNumber.get());
        supplierData.put("emailAddress", emailAddress.get());
        supplierData.put("websiteLink", websiteLink.get());
        supplierData.put("businessAddress", businessAddress.get());
        supplierData.put("warehouseAddress", warehouseAddress.get());
        supplierData.put("deliveryArea", deliveryArea.get());

        try {
            FirestoreUtils.writeDoc("Suppliers", supplierId.get(), supplierData);
            System.out.println("Supplier data successfully written to Firestore");
            clearInputs();
            return true;
        } catch (Exception e) {
            System.out.println("Error writing to Firestore: " + e.getMessage());
            return false;
        }
    }

    public void clearInputs() {
        supplierName.set("");
        personFirstName.set("");
        personLastName.set("");
        phoneNumber.set("");
        emailAddress.set("");
        websiteLink.set("");
        businessAddress.set("");
        warehouseAddress.set("");
        deliveryArea.set("");
        supplierId.set(generateSupplierId());
    }
}