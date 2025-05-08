package chooser.model;

import java.util.List;

public class ReceiveItems {

    private String id;
    private String deliveredDate;
    private String deliveredTime;
    private String receivedBy;
    private String purchaseOrderId;
    private String supplierId;
    private String supplierName;
    private PrimaryContactInfo primaryContact;
    private AddressInfo addressInfo;
    private List<RequestItem> requestItemList;

    public ReceiveItems(
            String id,
            String deliveredDate,
            String deliveredTime,
            String receivedBy,
            String purchaseOrderId,
            String supplierId,
            String supplierName,
            PrimaryContactInfo primaryContact,
            AddressInfo addressInfo,
            List<RequestItem> requestItemList
    ){
        this.id = id;
        this.deliveredDate = deliveredDate;
        this.deliveredTime = deliveredTime;
        this.receivedBy = receivedBy;
        this.purchaseOrderId = purchaseOrderId;
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

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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

    public List<RequestItem> getRequestItemList() {
        return requestItemList;
    }

    public void setRequestItemList(List<RequestItem> requestItemList) {
        this.requestItemList = requestItemList;
    }
}
