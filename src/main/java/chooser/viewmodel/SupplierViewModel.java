package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.Suppliers;
import chooser.utils.TableViewUtils;
import javafx.beans.property.*;
import java.util.HashMap;
import java.util.Map;

public class SupplierViewModel {

    private static final StringProperty supplierId = new SimpleStringProperty("");
    private static final StringProperty supplierName = new SimpleStringProperty("");
    private final StringProperty personFirstName = new SimpleStringProperty("");
    private final StringProperty personLastName = new SimpleStringProperty("");
    private static final StringProperty phoneNumber = new SimpleStringProperty("");
    private static final StringProperty emailAddress = new SimpleStringProperty("");
    private static final StringProperty websiteLink = new SimpleStringProperty("");
    private static final StringProperty businessAddress = new SimpleStringProperty("");
    private static final StringProperty warehouseAddress = new SimpleStringProperty("");
    private static final StringProperty deliveryArea = new SimpleStringProperty("");

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
        supplierData.put("supplierId", supplierId.get());
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
            clearFields(); // Updated to call clearFields()
            return true;
        } catch (Exception e) {
            System.out.println("Error writing to Firestore: " + e.getMessage());
            return false;
        }
    }

    public void clearFields() { // Renamed from clearInputs()
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

    public boolean deleteSupplier() {
        System.out.println("deleteSupplier called");
        try {
            FirestoreUtils.deleteDoc("Suppliers", supplierId.get());
            System.out.println("Supplier successfully deleted from Firestore");
            clearFields(); // Clear fields after deletion
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting from Firestore: " + e.getMessage());
            return false;
        }
    }


}