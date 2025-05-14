package chooser.utils;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItemSelectUtils {
    private static VBox storeMenuItemSelectView;
    private static AnchorPane storeRoot;
    private static final StringProperty menuItemId = new SimpleStringProperty("");
    private static final StringProperty menuItemName = new SimpleStringProperty("");
    private static final IntegerProperty maxServingOfItem = new SimpleIntegerProperty(-1);

    private static List<IngredientItem> storedIngredientItem;
    private static Map<String, Boolean> menutItemSelectedMap = new HashMap<>();


    private static final BooleanProperty isValidMenuItemId = new SimpleBooleanProperty(false);

    public static StringProperty menuItemIDProperty(){return menuItemId;}
    public static StringProperty menutItemNameProperty(){return menuItemName;}
    public static IntegerProperty maxServingOfItemProp(){return maxServingOfItem;}
    public static BooleanProperty menuItemIDValidProperty(){return isValidMenuItemId;}



    static{
        storeMenuItemSelectView = null;
        storeRoot = null;

        menuItemId.set("Select Menu Item");
        menuItemId.addListener((obs, oldVal, newVal) -> {
            isValidMenuItemId.set(!newVal.equals("Select Menu Item"));
        });
    }

    public static void setStoredIngredientItem(List<IngredientItem> value){storedIngredientItem = value;}
    public static List<IngredientItem> getStoredIngredientItem(){return storedIngredientItem;}
    public static Map<String,Boolean> getMenutItemSelectedMap(){return menutItemSelectedMap;}

    public static void changeItemSelectedStatus(String key, boolean status){
        menutItemSelectedMap.put(key,status);
    }

    public static void resetChangeItemSelectedStatus() {
        for (Map.Entry<String, Boolean> entry : menutItemSelectedMap.entrySet()) {
            entry.setValue(false);
        }
    }

    public static List<InventoryItem> setUpInventoryData(){

        String inventoryCollection = "InventoryV2";
        QuerySnapshot snapshotInventory = FirestoreUtils.getAllDocuments(inventoryCollection);
        if (snapshotInventory != null) {
            List<InventoryItem> inventoryItemList = TableViewUtils.prepareTableViewData(
                    InventoryItem.class,
                    "InventoryV2",
                    snapshotInventory
            );
            return  inventoryItemList;
        }
        return null;
    }


    public static Map<String,InventoryItem> setUpInventoryListMap(){

        String inventoryCollection = "InventoryV2";
        QuerySnapshot snapshotInventory = FirestoreUtils.getAllDocuments(inventoryCollection);
        if (snapshotInventory != null) {
            Map<String,InventoryItem> inventoryItemListMap = new HashMap<>();

            for (QueryDocumentSnapshot document : snapshotInventory) {
                InventoryItem formatInventoryData = FirestoreUtils.createInventoryItemFromDocument(document);
                inventoryItemListMap.put(formatInventoryData.getInventoryId(),formatInventoryData);

            }
            return  inventoryItemListMap;
        }
        return null;
    }



    public static boolean checkValidQuantityServing(String amount){
        try {
            int convertAmount = Integer.parseInt(amount);
            return convertAmount > 0 && convertAmount <= maxServingOfItem.get();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int calculateMaxServings(List<Integer> usesList) {

        if (usesList == null || usesList.isEmpty()) return 0;

        int minServings = Integer.MAX_VALUE;

        for (Integer uses : usesList) {
            minServings = Math.min(minServings, uses);
        }

        return minServings;
    }

    public static int remainingServings(MenuItem foundMenuItem, List<InventoryItem> inventoryItemList){
        List<Integer> remainingUsesList = new ArrayList<>();

        List<IngredientItem> retrieveIngredientList = foundMenuItem.getIngredientsList();
        for(int i = 0; i < retrieveIngredientList.size(); ++i){

            IngredientItem ingItem = retrieveIngredientList.get(i);
            String retrieveLinkInvId = ingItem.getLinkInventoryId();

            if(ingItem.getLinkInventoryId().equals("**Link Error**")){
                return 0;
            }

            for(int j = 0; j < inventoryItemList.size(); ++j){

                InventoryItem invItem = inventoryItemList.get(j);

                if(invItem.getInventoryId().equals(retrieveLinkInvId)){

                    int calculateRemainUses = (int) IngredientConversionUtils.calculateIngredientUses(
                            Double.parseDouble(ingItem.getQuantity()),
                            ingItem.getUom(),
                            Double.parseDouble(String.valueOf(invItem.getTotalQuantity())),
                            invItem.getQuantityUOM()
                    );

                    System.out.println("Remaining Uses for ingredient: "+ingItem.getName()+"/"+calculateRemainUses);
                    remainingUsesList.add(calculateRemainUses);

                    break;
                }

            }


        }

        return calculateMaxServings(remainingUsesList);

    }



    public static void showMenuItemSelect(String status, AnchorPane rootPane){
        switch(status){
            case "SHOW":
                try {
                    storeRoot = rootPane;

                    String fxmlPathFull = "/chooser/trackbite/MenuItemSelect.fxml";
                    FXMLLoader loader = new FXMLLoader(NewInventoryItemUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storeMenuItemSelectView = view;
                    rootPane.getChildren().add(view);

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
                if(storeMenuItemSelectView != null && storeRoot!=null){
                    storeRoot.getChildren().remove(storeMenuItemSelectView);
                    storeMenuItemSelectView = null;
                    /*
                    storeInventoryItemSelectView.setVisible(false);
                    storeInventoryItemSelectView.setDisable(true);
                     */
                }
                break;
        }



    }

    public static void formatAndUpdateInventoryItems(InventoryItem invItem){

        Map<String, Object> data = new HashMap<>();
        data.put("inventoryId", invItem.getInventoryId());
        data.put("inventoryName", invItem.getInventoryName());
        data.put("category", invItem.getCategory());
        data.put("stockStatus", invItem.getStockStatus());
        data.put("totalQuantity",invItem.getTotalQuantity());
        data.put("minStock", invItem.getMinStock());
        data.put("quantityUOM", invItem.getQuantityUOM());
        data.put("itemLimit", invItem.getItemLimit());
        data.put("itemPrice", invItem.getItemPrice());
        data.put("priceUOM", invItem.getPriceUOM());
        data.put("inventoryItemCount", invItem.getInventoryItemCount());

        List<Map<String, Object>> itemStockForFirestore = new ArrayList<>();

        for (ItemStock itemStock : invItem.getItemStockList()) {
            Map<String, Object> itemStockMap = new HashMap<>();
            itemStockMap.put("itemId", itemStock.getItemId());
            itemStockMap.put("quantity", String.valueOf(itemStock.getItemAmount()));
            itemStockMap.put("expireDate", itemStock.getExpireDate());
            itemStockForFirestore.add(itemStockMap);
        }

        data.put("itemStockList",itemStockForFirestore);

        FirestoreUtils.writeDoc("InventoryV2", invItem.getInventoryId(), data);

    }

    private static InventoryItem deductionsOnItemStock(InventoryItem invItem, int deductServeQuantity, IngredientItem ingItem) {
        if (invItem == null || ingItem == null) {
            return invItem;
        }

        String invItemUOM = invItem.getQuantityUOM();
        List<ItemStock> invItemStockList = invItem.getItemStockList();

        String ingUOM = ingItem.getUom();
        String ingQuantityStr = ingItem.getQuantity();

        if (ingQuantityStr == null || ingQuantityStr.isEmpty()) {
            return invItem;
        }

        double ingQuantity;
        try {
            ingQuantity = Double.parseDouble(ingQuantityStr);
        } catch (NumberFormatException e) {
            return invItem;
        }

        double totalRequiredIngredientUOM = deductServeQuantity * ingQuantity;

        double totalRequiredInventoryUOM = totalRequiredIngredientUOM;
        if (!invItemUOM.equals(ingUOM)) {

            double uses = IngredientConversionUtils.calculateIngredientUses(1.0, ingUOM, 1.0, invItemUOM);
            if (uses <= 0) {
                System.out.println("Cannot convert between UOMs: " + ingUOM + " and " + invItemUOM);
                return invItem;
            }
            totalRequiredInventoryUOM = totalRequiredIngredientUOM / uses;
        }

        double remainingRequired = totalRequiredInventoryUOM;

        for (ItemStock stock : invItemStockList) {
            if (remainingRequired <= 0) break;

            double stockQuantity = stock.getItemAmount();

            if (stockQuantity <= 0) continue;

            if (stockQuantity >= remainingRequired) {
                stock.setItemAmount((int) (stockQuantity - remainingRequired));

                remainingRequired = 0;
            } else {
                stock.setItemAmount(0);
                remainingRequired -= stockQuantity;
            }
        }


        int updatedTotalQuantity = invItemStockList.stream()
                .mapToInt(stock -> (int) stock.getItemAmount())
                .sum();
        invItem.setTotalQuantity(updatedTotalQuantity);
        invItem.setStockStatus(
                NewInventoryItemUtils.calculateStockStatus(
                        String.valueOf(invItem.getMinStock()),
                        String.valueOf(updatedTotalQuantity)
                )
        );

        invItem.setItemStockList(invItemStockList);
        return invItem;
    }


    public static boolean changeInventoryItems(String changeStatus, Map<Integer,List<CustomerOrder>> customerOrderMap){

        if(changeStatus.equals("DEDUCT")){
            Map<String,InventoryItem> inventoryItemListMap =  setUpInventoryListMap();

            if(inventoryItemListMap != null && customerOrderMap!=null && !customerOrderMap.isEmpty()){

                Map<String,InventoryItem> changedInventoryItems = new HashMap<>();

                List<CustomerOrder> currOrderList = new ArrayList<>();
                for (Map.Entry<Integer, List<CustomerOrder>> entry : customerOrderMap.entrySet()) {
                    currOrderList.addAll(entry.getValue());
                }

                for(int i = 0; i < currOrderList.size(); ++i){

                    String logMenuItemQuantity = currOrderList.get(i).getQuantity();
                    List<IngredientItem> logMenuItemIngredList = currOrderList.get(i).getIngredientList();

                    for(int j = 0; j < logMenuItemIngredList.size(); ++j){

                        IngredientItem tempIngredientItem = logMenuItemIngredList.get(j);
                        String linkInvId = tempIngredientItem.getLinkInventoryId();


                        if(!changedInventoryItems.containsKey(linkInvId)){
                            changedInventoryItems.put(linkInvId, inventoryItemListMap.get(linkInvId));
                        }

                        InventoryItem hasNewInvItem = deductionsOnItemStock(
                                changedInventoryItems.get(linkInvId),
                                Integer.parseInt(logMenuItemQuantity),
                                tempIngredientItem

                        );

                        changedInventoryItems.put(hasNewInvItem.getInventoryId(), hasNewInvItem);

                    }
                }

                for (Map.Entry<String, InventoryItem> entry : changedInventoryItems.entrySet()) {
                    InventoryItem value = entry.getValue();
                    if (value != null) {
                        formatAndUpdateInventoryItems(value);
                        System.out.println(value.getInventoryName()+" stock item changes has been saved!!");
                    } else{
                        return false;
                    }
                }

                return true;
            }
        }

        return false;

    }


    public static List<MenuItem> setUpMenuItemList(){

        String inventoryCollection = "Menu";
        QuerySnapshot snapshotInventory = FirestoreUtils.getAllDocuments(inventoryCollection);
        if (snapshotInventory != null) {
            List<MenuItem> menuItemList= new ArrayList<>();

            for (QueryDocumentSnapshot document : snapshotInventory) {
                MenuItem formatMeuItemData = FirestoreUtils.createMenuItemFromDocument(document);
                menuItemList.add(formatMeuItemData);

            }
            return  menuItemList;
        }
        return null;
    }

    public static void updateFireStoreMenuItem(MenuItem menuItemData){
        Map<String, Object> data = new HashMap<>();
        data.put("id", menuItemData.getId());
        data.put("name", menuItemData.getName());
        data.put("description", menuItemData.getDescription());
        data.put("category", menuItemData.getCategory());
        data.put("price", menuItemData.getPrice());
        data.put("uom", menuItemData.getUom());
        data.put("itemImage", menuItemData.getItemImage());


        List<IngredientItem> entireIngredientsList = menuItemData.getIngredientsList();

        List<Map<String, Object>> ingredientsForFirestore = new ArrayList<>();
        for (IngredientItem ingredient : entireIngredientsList) {
            Map<String, Object> ingredientMap = new HashMap<>();

            ingredientMap.put("name", ingredient.getName());
            ingredientMap.put("quantity", ingredient.getQuantity());
            ingredientMap.put("uom", ingredient.getUom());
            ingredientMap.put("prepDetails", ingredient.getPrepDetails());
            ingredientMap.put("linkInventoryId", ingredient.getLinkInventoryId());
            ingredientMap.put("remainingUses", ingredient.getRemainingUses());

            ingredientsForFirestore.add(ingredientMap);
        }

        data.put("ingredientsList",ingredientsForFirestore);

        FirestoreUtils.writeDoc("Menu", menuItemData.getId(), data);

    }


    public static void updateMenuItemInventoryLinkError(String inventoryId){
        List<MenuItem> menuItemList = setUpMenuItemList();

        for (MenuItem item : menuItemList) {
            boolean shouldUpdate = false;

            List<IngredientItem> ingredList = item.getIngredientsList();
            for (IngredientItem ingredItem : ingredList) {
                if (ingredItem.getLinkInventoryId().equals(inventoryId)) {
                    ingredItem.setLinkInventoryId("**Link Error**");
                    shouldUpdate = true;
                }
            }

            if (shouldUpdate) {
                updateFireStoreMenuItem(item);
            }
        }
    }


}
