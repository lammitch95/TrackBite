package chooser.model;

import java.time.LocalDate;



public class InventoryDelivery {

        private final String itemId;
        private final String itemName;
        private final float quantity;
        private final LocalDate deliveryDate;
        private final LocalDate expDate;
        private final float pricePerUnit;

        public InventoryDelivery(String itemId, String itemName, float quantity, LocalDate deliveryDate, LocalDate expDate, float pricePerUnit) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.deliveryDate = deliveryDate;
            this.expDate = expDate;
            this.pricePerUnit = pricePerUnit;
        }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public float getQuantity() {
        return quantity;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }
}


