package chooser.model;

import java.util.List;

public class MenuItem {
    private String menuId;
    private String itemName;
    private String description;
    private String price;
    private String category;
    private List<String> ingredients;
    private String imageUrl;

    public MenuItem(String menuId, String itemName, String description, String price, String category, List<String> ingredients, String imageUrl) {
        this.menuId = menuId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getMenuId() { return menuId; }
    public String getItemName() { return itemName; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getCategory() { return category; }
    public List<String> getIngredients() { return ingredients; }
    public String getImageUrl() { return imageUrl; }
}
