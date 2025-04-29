package chooser.model;

import java.util.List;

public class CustomerOrder {

    private String menuItemId;
    private String menuItemName;
    private String tableNum;
    private String quantity;
    private String name;
    private String notes;
    private List<IngredientItem> ingredientList;

    public CustomerOrder(String tableNum, String menuItemId, String menuItemName, String quantity, String name,String notes, List<IngredientItem> ingredientList){
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.tableNum = tableNum;
        this.quantity = quantity;
        this.name = name;
        this.notes = notes;
        this.ingredientList = ingredientList;
    }


    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<IngredientItem> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<IngredientItem> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }


    @Override
    public String toString() {
        return "{" +menuItemId + '\'' +menuItemName + '\'' +tableNum + '\'' +quantity + '\'' +name + '\'' +notes + '}';
    }
}
