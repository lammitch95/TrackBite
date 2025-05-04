package chooser.model;

public class Suppliers {
    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String phoneNumber;
    private String emailAddress;
    private String websiteLink;
    private String businessAddress;
    private String warehouseAddress;
    private String deliveryArea;

    public Suppliers(String supplierId, String supplierName, String contactPerson, String phoneNumber,
                    String emailAddress, String websiteLink, String businessAddress,
                    String warehouseAddress, String deliveryArea) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.websiteLink = websiteLink;
        this.businessAddress = businessAddress;
        this.warehouseAddress = warehouseAddress;
        this.deliveryArea = deliveryArea;
    }


    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getContactPerson() { return contactPerson; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getWebsiteLink() { return websiteLink; }
    public String getBusinessAddress() { return businessAddress; }
    public String getWarehouseAddress() { return warehouseAddress; }
    public String getDeliveryArea() { return deliveryArea; }
}