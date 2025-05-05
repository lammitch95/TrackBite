package chooser.model;

public class DeliveryItemTable {
    //private String inventoryId;
    private String itemName;
    private String quantity;
    private String uom;
    //private double price;
    private String priority;

    public DeliveryItemTable(String inventoryId, String itemName, String quantity, String uom) {
        //this.inventoryId = inventoryId;
        this.itemName    = itemName;
        this.quantity    = quantity;
        this.uom         = uom;
        //this.price       = price;
        this.priority    = String.valueOf(priority);
    }

//    public String getInventoryId() { return inventoryId; }
//    public void setInventoryId(String inventoryId) { this.inventoryId = inventoryId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

//    public double getPrice() { return price; }
//    public void setPrice(double price) { this.price = price; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    @Override
    public String toString() {
        return itemName + " (" + quantity + " " + uom + ") priority: " + priority ; //inventoryId + " - " +  /*+ " @ $" + price*/
    }
}