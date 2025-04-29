package chooser.model;

import java.util.List;

public class InventoryItem {

    private String inventoryId;
    private String inventoryName;
    private String category;
    private String stockStatus;
    private int totalQuantity;
    private String quantityUOM;
    private int itemLimit;
    private int minStock;
    private String itemPrice;
    private String priceUOM;
    private List<ItemStock> itemStockList;
    private int inventoryItemCount;


    public InventoryItem(){
        this.inventoryItemCount = 0;
    }

    public InventoryItem(String inventoryId,
                         String inventoryName,
                         String category,
                         String stockStatus,
                         int totalQuantity,
                         String quantityUOM,
                         int itemLimit,
                         int minStock,
                         String itemPrice,
                         String priceUOM,
                         int inventoryItemCount,
                         List<ItemStock> itemStockList){

        this.inventoryId = inventoryId;
        this.inventoryName = inventoryName;
        this.category = category;
        this.stockStatus = stockStatus;
        this.totalQuantity = totalQuantity;
        this.itemLimit = itemLimit;
        this.minStock = minStock;
        this.quantityUOM = quantityUOM;
        this.itemPrice = itemPrice;
        this.priceUOM = priceUOM;
        this.inventoryItemCount = inventoryItemCount;
        this.itemStockList = itemStockList;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getItemLimit() {
        return itemLimit;
    }

    public void setItemLimit(int itemLimit) {
        this.itemLimit = itemLimit;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public List<ItemStock> getItemStockList() {
        return itemStockList;
    }

    public void setItemStockList(List<ItemStock> itemStockList) {
        this.itemStockList = itemStockList;
    }

    public int getInventoryItemCount() {
        return inventoryItemCount;
    }

    public void setInventoryItemCount(int inventoryItemCount) {
        this.inventoryItemCount = inventoryItemCount;
    }

    public String getPriceUOM() {
        return priceUOM;
    }

    public void setPriceUOM(String priceUOM) {
        this.priceUOM = priceUOM;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getQuantityUOM() {
        return quantityUOM;
    }

    public void setQuantityUOM(String quantityUOM) {
        this.quantityUOM = quantityUOM;
    }
}
