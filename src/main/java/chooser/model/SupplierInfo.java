package chooser.model;

public class SupplierInfo {
     private String supplierId;
     private String supplierName;
     private String description;
     private PrimaryContactInfo contactInfo;
     private AddressInfo businessAddressInfo;
     private AddressInfo warehouseAddressInfo;

     public SupplierInfo(String supplierId, String supplierName, String description, PrimaryContactInfo contactInfo, AddressInfo businessAddressInfo, AddressInfo warehouseAddressInfo){
          this.supplierId = supplierId;
          this.supplierName = supplierName;
          this.description = description;
          this.contactInfo = contactInfo;
          this.businessAddressInfo = businessAddressInfo;
          this.warehouseAddressInfo = warehouseAddressInfo;
     }

     public String getSupplierId() {
          return supplierId;
     }

     public void setSupplierId(String supplierId) {
          this.supplierId = supplierId;
     }

     public String getSupplierName() {
          return supplierName;
     }

     public void setSupplierName(String supplierName) {
          this.supplierName = supplierName;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public PrimaryContactInfo getContactInfo() {
          return contactInfo;
     }

     public void setContactInfo(PrimaryContactInfo contactInfo) {
          this.contactInfo = contactInfo;
     }

     public AddressInfo getBusinessAddressInfo() {
          return businessAddressInfo;
     }

     public void setBusinessAddressInfo(AddressInfo businessAddressInfo) {
          this.businessAddressInfo = businessAddressInfo;
     }

     public AddressInfo getWarehouseAddressInfo() {
          return warehouseAddressInfo;
     }

     public void setWarehouseAddressInfo(AddressInfo warehouseAddressInfo) {
          this.warehouseAddressInfo = warehouseAddressInfo;
     }
}
