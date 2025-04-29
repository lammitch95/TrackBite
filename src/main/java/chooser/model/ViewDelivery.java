package chooser.model;

import java.time.LocalDate;

public class ViewDelivery {
    private final String DeliveryID;
    private final String OrderNumber;
    private final LocalDate DeliveryDate;
    private final String DeliveryTime;
    private final String DeliveryAddress;
    private final String Supplier;
    private final String SupplierAddress;

    public ViewDelivery(String DeliveryID, String OrderNumber, LocalDate DeliveryDate, String DeliveryTime, String DeliveryAddress, String Supplier, String SupplierAddress) {
        this.DeliveryID       = DeliveryID;
        this.OrderNumber      = OrderNumber;
        this.DeliveryDate     = DeliveryDate;
        this.DeliveryTime     = DeliveryTime;
        this.DeliveryAddress  = DeliveryAddress;
        this.Supplier         = Supplier;
        this.SupplierAddress  = SupplierAddress;
    }

    public String   getDeliveryID()      { return DeliveryID; }
    public String   getOrderNumber()     { return OrderNumber; }
    public LocalDate getDeliveryDate()   { return DeliveryDate; }
    public String   getDeliveryTime()    { return DeliveryTime; }
    public String   getDeliveryAddress() { return DeliveryAddress; }
    public String   getSupplier()        { return Supplier; }
    public String   getSupplierAddress() { return SupplierAddress; }
}
