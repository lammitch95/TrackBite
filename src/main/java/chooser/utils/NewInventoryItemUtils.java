package chooser.utils;

import chooser.model.CurrentPageOptions;
import chooser.model.ItemStock;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NewInventoryItemUtils {

    private static VBox storeInventoryItemSelectView;
    private static AnchorPane storeRoot;
    private static ArrayList<String> categoryList = new ArrayList<>();


    static{
        storeInventoryItemSelectView = null;
        storeRoot = null;

        categoryList.add("Select Category");
        categoryList.add("Meat");
        categoryList.add("Poultry");
        categoryList.add("Seafood");
        categoryList.add("Vegetables");
        categoryList.add("Fruits");
        categoryList.add("Grains & Cereals");
        categoryList.add("Dairy");
        categoryList.add("Eggs");
        categoryList.add("Nuts & Seeds");
        categoryList.add("Legumes");
        categoryList.add("Herbs & Spices");
        categoryList.add("Oils & Fats");
        categoryList.add("Sweeteners");
        categoryList.add("Baked goods");
        categoryList.add("Baking Ingredients");
        categoryList.add("Beverages");
        categoryList.add("Condiments & Sauces");
        categoryList.add("Preserved Foods");
        categoryList.add("Frozen Foods");
        categoryList.add("Processed Foods");
        categoryList.add("Miscellaneous");
    }

    public static boolean isValidDetails(String name) {
        if (name == null) return false;
        return name.trim().length() >= 3;
    }

    public static boolean isValidQuantity(String quantityStr) {
        if (quantityStr == null) return false;

        try {
            int quantity = Integer.parseInt(quantityStr.trim());
            return quantity >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidExpDateFormat(String dob) {
        //format Example is MM/DD/YYYY
        String regex = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        return dob != null && Pattern.matches(regex, dob);
    }

    public static String calculateTotalStockQuantity(String status,String currentTotal, ItemStock newlyAdded){
        int convertCurrTotal = Integer.parseInt(currentTotal);
        int newTotal = 0;
        switch(status){
            case "ADD":
                newTotal = (int) (convertCurrTotal + newlyAdded.getItemAmount());
                break;
            case "DELETE":
                newTotal = (int) (convertCurrTotal - newlyAdded.getItemAmount());
                break;

        }

        return String.valueOf(newTotal);
    }

    public static String calculateStockStatus(String minStock, String currentTotal){
        double convMinSotck = Double.parseDouble(minStock);
        double convCurrTotal = Double.parseDouble(currentTotal);
        String newStatus = "";
        if(convCurrTotal > convMinSotck){
            newStatus = "In Stock";
        }else if(convCurrTotal <= convMinSotck && convCurrTotal > 0.0){
            newStatus = "Low Stock";
        }else{
            newStatus = "Out of Stock";
        }
        return newStatus;
    }


    public static void selectInventoryItem(String status,AnchorPane menuFormRootPane){
        switch(status){
            case "SHOW":
                try {
                    storeRoot = menuFormRootPane;

                    String fxmlPathFull = "/chooser/trackbite/InventoryItemSelect.fxml";
                    FXMLLoader loader = new FXMLLoader(NewInventoryItemUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storeInventoryItemSelectView = view;
                    menuFormRootPane.getChildren().add(view);

                    AnchorPane.setTopAnchor(view, 0.0);
                    AnchorPane.setRightAnchor(view, 0.0);
                    AnchorPane.setBottomAnchor(view, 0.0);
                    AnchorPane.setLeftAnchor(view, 0.0);

                    view.setVisible(true);
                    view.setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "HIDE":
                if(storeInventoryItemSelectView != null && storeRoot!=null){
                    storeRoot.getChildren().remove(storeInventoryItemSelectView);
                    storeInventoryItemSelectView = null;
                    /*
                    storeInventoryItemSelectView.setVisible(false);
                    storeInventoryItemSelectView.setDisable(true);
                     */
                }
                break;
        }



    }


    public static ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public static void setCategoryList(ArrayList<String> categoryList) {
        NewInventoryItemUtils.categoryList = categoryList;
    }
}
