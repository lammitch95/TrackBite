package chooser.model;

import java.time.LocalDate;

public class ViewDelivery {
    private final String deliveryID;
    private final String orderNumber;
    private final LocalDate deliveryDate;
    private final String deliveryTime;
    private final String deliveryAddress;
    private final String supplier;
    private final String supplierAddress;
    private final String supplierFirstName;
    private final String supplierLastName;
    private final String supplierContactNum;

    public ViewDelivery(String deliveryID, String orderNumber, LocalDate deliveryDate, String deliveryTime, String deliveryAddress, String supplier, String supplierAddress,
                        String supplierFirstName, String supplierLastName, String supplierContactNum) {
        this.deliveryID       = deliveryID;
        this.orderNumber      = orderNumber;
        this.deliveryDate     = deliveryDate;
        this.deliveryTime     = deliveryTime;
        this.deliveryAddress  = deliveryAddress;
        this.supplier         = supplier;
        this.supplierAddress  = supplierAddress;
        this.supplierFirstName  = supplierFirstName;
        this.supplierLastName   = supplierLastName;
        this.supplierContactNum = supplierContactNum;
    }

    public String    getDeliveryID()      { return deliveryID; }
    public String    getOrderNumber()     { return orderNumber; }
    public LocalDate getDeliveryDate()    { return deliveryDate; }
    public String    getDeliveryTime()    { return deliveryTime; }
    public String    getDeliveryAddress() { return deliveryAddress; }
    public String    getSupplier()        { return supplier; }
    public String    getSupplierAddress(){ return supplierAddress; }
    public String   getSupplierFirstName()  { return supplierFirstName; }
    public String   getSupplierLastName()   { return supplierLastName; }
    public String   getSupplierContactNum() { return supplierContactNum; }
}
