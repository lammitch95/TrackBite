package chooser.model;

import java.util.List;

public class PurchaseOrder {

   private String id;
   private String orderStatus;
   private String estDelivery;
   private String orderDate;
   private String signedBy;

   private String supplierId;
   private String supplierName;
   private PrimaryContactInfo primaryContact;
   private AddressInfo addressInfo;
   private List<RequestItem> requestItemList;


   public PurchaseOrder(
           String id,
           String orderStatus,
           String estDelivery,
           String orderDate,
           String signedBy,
           String supplierId,
           String supplierName,
           PrimaryContactInfo primaryContact,
           AddressInfo addressInfo,
           List<RequestItem> requestItemList
   ){
        this.id = id;
        this.orderStatus = orderStatus;
        this.estDelivery = estDelivery;
        this.orderDate = orderDate;
        this.signedBy = signedBy;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.primaryContact = primaryContact;
        this.addressInfo = addressInfo;
        this.requestItemList = requestItemList;
   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstDelivery() {
        return estDelivery;
    }

    public void setEstDelivery(String estDelivery) {
        this.estDelivery = estDelivery;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


    public List<RequestItem> getRequestItemList() {
        return requestItemList;
    }

    public void setRequestItemList(List<RequestItem> requestItemList) {
        this.requestItemList = requestItemList;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public PrimaryContactInfo getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(PrimaryContactInfo primaryContact) {
        this.primaryContact = primaryContact;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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
}
