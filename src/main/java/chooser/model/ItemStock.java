package chooser.model;

public class ItemStock {

    private String itemId;
    private int itemAmount;
    private String expireDate;

    public ItemStock(){
        this.itemAmount = 0;
        this.expireDate = "MM/DD/YYYY";
    }

    public ItemStock(String itemId, int itemAmount, String expireDate){
        this.itemId = itemId;
        this.itemAmount = itemAmount;
        this.expireDate = expireDate;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return itemAmount + "-" + expireDate + ", ";
    }
}
