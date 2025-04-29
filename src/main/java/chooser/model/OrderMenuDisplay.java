package chooser.model;

public class OrderMenuDisplay {

    private String ingredientName;
    private String ingredientQuantity;
    private String ingredientUOM;
    private int remainingUses;
    private String linkInventoryId;
    private String stockStatus;
    private int stockQuantity;
    private String stockUOM;

    public OrderMenuDisplay(
            String ingredientName,
            String ingredientQuantity,
            String ingredientUOM,
            int remainingUses,
            String linkInventoryId,
            String stockStatus,
            int stockQuantity,
            String stockUOM
    ){

        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUOM = ingredientUOM;
        this.remainingUses = remainingUses;
        this.linkInventoryId = linkInventoryId;
        this.stockStatus = stockStatus;
        this.stockQuantity = stockQuantity;
        this.stockUOM = stockUOM;

    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(String ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientUOM() {
        return ingredientUOM;
    }

    public void setIngredientUOM(String ingredientUOM) {
        this.ingredientUOM = ingredientUOM;
    }

    public int getRemainingUses() {
        return remainingUses;
    }

    public void setRemainingUses(int remainingUses) {
        this.remainingUses = remainingUses;
    }

    public String getLinkInventoryId() {
        return linkInventoryId;
    }

    public void setLinkInventoryId(String linkInventoryId) {
        this.linkInventoryId = linkInventoryId;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStockUOM() {
        return stockUOM;
    }

    public void setStockUOM(String stockUOM) {
        this.stockUOM = stockUOM;
    }
}
