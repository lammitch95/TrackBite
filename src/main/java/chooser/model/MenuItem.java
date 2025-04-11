package chooser.model;

import java.util.List;

public class MenuItem {

    private String id;
    private String name;
    private String description;
    private String category;

    private String price;
    private String uom;

    private String itemImage;

    private List<IngredientItem> ingredientsList;

    public MenuItem(String id, String name, String description, String category, String price, String uom, String itemImage, List<IngredientItem> ingredientsList){
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.uom = uom;
        this.itemImage = itemImage;
        this.ingredientsList = ingredientsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public List<IngredientItem> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<IngredientItem> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }
}
