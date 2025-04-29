package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;

public class SupplierTableViewModel {

    private final ObservableList<SupplierData> supplierList = FXCollections.observableArrayList();

    public ObservableList<SupplierData> getSupplierList() {
        return supplierList;
    }

    public void fetchSuppliers() {
        try {
            List<Map<String, Object>> suppliers = FirestoreUtils.readCollection("Suppliers");  // No parameter needed
            supplierList.clear();
            for (Map<String, Object> supplier : suppliers) {
                supplierList.add(new SupplierData(
                        (String) supplier.get("supplierID"),
                        (String) supplier.get("supplierName"),
                        (String) supplier.get("contactPerson"),
                        (String) supplier.get("phoneNumber"),
                        (String) supplier.get("emailAddress"),
                        (String) supplier.get("websiteLink"),
                        (String) supplier.get("businessAddress"),
                        (String) supplier.get("warehouseAddress"),
                        (String) supplier.get("deliveryArea")
                ));
            }
            System.out.println("Suppliers fetched successfully: " + supplierList.size());
        } catch (Exception e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }
    }

    public static class SupplierData {
        private final StringProperty supplierId = new SimpleStringProperty();
        private final StringProperty supplierName = new SimpleStringProperty();
        private final StringProperty contactPerson = new SimpleStringProperty();
        private final StringProperty phoneNumber = new SimpleStringProperty();
        private final StringProperty emailAddress = new SimpleStringProperty();
        private final StringProperty websiteLink = new SimpleStringProperty();
        private final StringProperty businessAddress = new SimpleStringProperty();
        private final StringProperty warehouseAddress = new SimpleStringProperty();
        private final StringProperty deliveryArea = new SimpleStringProperty();

        public SupplierData(String supplierId, String supplierName, String contactPerson, String phoneNumber,
                            String emailAddress, String websiteLink, String businessAddress,
                            String warehouseAddress, String deliveryArea) {
            this.supplierId.set(supplierId);
            this.supplierName.set(supplierName);
            this.contactPerson.set(contactPerson);
            this.phoneNumber.set(phoneNumber);
            this.emailAddress.set(emailAddress);
            this.websiteLink.set(websiteLink);
            this.businessAddress.set(businessAddress);
            this.warehouseAddress.set(warehouseAddress);
            this.deliveryArea.set(deliveryArea);
        }

        public StringProperty supplierIdProperty() { return supplierId; }
        public StringProperty supplierNameProperty() { return supplierName; }
        public StringProperty contactPersonProperty() { return contactPerson; }
        public StringProperty phoneNumberProperty() { return phoneNumber; }
        public StringProperty emailAddressProperty() { return emailAddress; }
        public StringProperty websiteLinkProperty() { return websiteLink; }
        public StringProperty businessAddressProperty() { return businessAddress; }
        public StringProperty warehouseAddressProperty() { return warehouseAddress; }
        public StringProperty deliveryAreaProperty() { return deliveryArea; }
    }
}