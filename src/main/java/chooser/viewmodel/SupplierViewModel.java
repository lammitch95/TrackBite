package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import javafx.beans.property.*;
import chooser.model.Suppliers;
import chooser.utils.TableViewUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.HashMap;
import java.util.Map;

public class SupplierViewModel {


    public SupplierViewModel() {
        supplierId.set(generateSupplierId());
    }

    // Property getters
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

    public boolean deleteSupplier() {
        System.out.println("deleteSupplier called");
        try {
            FirestoreUtils.deleteDoc("Suppliers", supplierId.get());
            System.out.println("Supplier successfully deleted from Firestore");
            clearFields();
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting from Firestore: " + e.getMessage());
            return false;
        }
    }

    public static void populateTextFields() {
        Object selectedData = TableViewUtils.retrieveDocumentData(
                TableViewUtils.getStoredCollectionName(),
                TableViewUtils.getSelectedRowID()
        );

        if (selectedData instanceof Suppliers obj) {
            supplierId.set(obj.getSupplierId() != null ? obj.getSupplierId() : "");
            supplierName.set(obj.getSupplierName() != null ? obj.getSupplierName() : "");

            // Split contactPerson into first and last names
            String contactPerson = obj.getContactPerson() != null ? obj.getContactPerson() : "";
            String[] names = contactPerson.trim().split("\\s+", 2);
            personFirstName.set(names.length > 0 ? names[0] : "");
            personLastName.set(names.length > 1 ? names[1] : "");

            phoneNumber.set(obj.getPhoneNumber() != null ? obj.getPhoneNumber() : "");
            emailAddress.set(obj.getEmailAddress() != null ? obj.getEmailAddress() : "");
            websiteLink.set(obj.getWebsiteLink() != null ? obj.getWebsiteLink() : "");
            businessAddress.set(obj.getBusinessAddress() != null ? obj.getBusinessAddress() : "");
            warehouseAddress.set(obj.getWarehouseAddress() != null ? obj.getWarehouseAddress() : "");
            deliveryArea.set(obj.getDeliveryArea() != null ? obj.getDeliveryArea() : "");

            // Clear TableViewUtils state
            TableViewUtils.setStoreCollectionName("");
            TableViewUtils.setSelectedRowID(null);
        } else {
            System.out.println("No valid supplier data found to populate fields");
        }
    }
}