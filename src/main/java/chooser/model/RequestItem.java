package chooser.model;

public class RequestItem {
    private String inventoryId;
    private String inventoryName;
    private String quantity;
    private String uom;
    private String notes;

    public RequestItem(String inventoryId, String inventoryName, String quantity, String uom, String notes){
        this.inventoryId = inventoryId;
        this.inventoryName = inventoryName;
        this.quantity = quantity;
        this.uom = uom;
        this.notes = notes;

    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return inventoryName + "," + quantity + "," + uom + "," + notes + " |";
    }
}
