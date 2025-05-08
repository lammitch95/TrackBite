package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.IngredientItem;
import chooser.model.InventoryItem;
import chooser.model.ItemStock;
import chooser.model.MenuItem;
import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.SystemMessageUtils;
import chooser.utils.TableViewUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.*;

public class NewInventoryItemViewModel {

    private final StringProperty inventoryId = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty category = new SimpleStringProperty("");
    private final StringProperty itemLimit = new SimpleStringProperty("");
    private final StringProperty minStock = new SimpleStringProperty("");
    private final StringProperty stockStatus = new SimpleStringProperty("");
    private final StringProperty quantityUOM = new SimpleStringProperty("");
    private final StringProperty price = new SimpleStringProperty("");
    private final StringProperty priceUOM = new SimpleStringProperty("");
    private final StringProperty totalQuantity = new SimpleStringProperty("");
    private final StringProperty inventoryItemCount = new SimpleStringProperty("");


    //String property for Item stock of inventory
    private final StringProperty itemStockId = new SimpleStringProperty("");
    private final StringProperty quantity = new SimpleStringProperty("");
    private final StringProperty expireDate = new SimpleStringProperty("");

    //others
    private final StringProperty tbvPageNumDisplay = new SimpleStringProperty("");
    private final StringProperty stockQuantityDisplay = new SimpleStringProperty("");
    private final StringProperty stockStatusDisplay = new SimpleStringProperty("");


    //Validity of String Properties
    private final BooleanProperty isValidInventoryId = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidName= new SimpleBooleanProperty(false);
    private final BooleanProperty isValidItemLimit = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidCategory = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidMinStock = new SimpleBooleanProperty(false);

    private final BooleanProperty isValidQuantityUOM = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidPrice = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidPriceUOM = new SimpleBooleanProperty(false);


    private final BooleanProperty isValidItemStockId = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidQuantity = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidExpireDate = new SimpleBooleanProperty(false);


    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty validItemStockForm = new SimpleBooleanProperty(false);

    private List<BooleanProperty> validInputList;
    private List<StringProperty> inventoryInputList;
    private List<BooleanProperty> validItemStockList;
    private List<StringProperty> itemStockInputList;

    private int trackTableViewPage;
    private int maxPages;

    private ItemStock currSelectedItemStock;

    private Map<Integer, List<ItemStock>> itemStockListMap;

    private ObservableList<ItemStock> itemStockList;

    //retrieve/getters
    public void setCurrSelectedItemStock(ItemStock item) {this.currSelectedItemStock = item;}
    public ItemStock getCurrSelectedItemStock(){
        return  this.currSelectedItemStock;
    }

    public StringProperty tbvPageNumDisplayProperty(){return tbvPageNumDisplay;}
    public StringProperty inventoryIdProp(){return inventoryId;}
    public StringProperty nameProp(){return name;}
    public StringProperty itemLimitProp(){return itemLimit;}
    public StringProperty minStockProp(){return minStock;}

    public StringProperty quantityUOMProp(){return quantityUOM;}
    public StringProperty categoryProp(){return category;}
    public StringProperty priceProp(){return price;}
    public StringProperty priceUOMProp(){return  priceUOM;}


    public StringProperty quantityProp(){return quantity;}
    public StringProperty expireDateProp(){return expireDate;}

    public StringProperty stockQuantityDisplayProp(){return stockQuantityDisplay;}
    public StringProperty stockStatusDisplayProp(){return stockStatusDisplay;}


    public BooleanProperty validInventoryIdProp(){return isValidInventoryId;}
    public BooleanProperty validNameProp(){return isValidName;}
    public BooleanProperty validItemLimitProp(){return isValidItemLimit;}
    public BooleanProperty validMinStockProp(){return isValidMinStock;}
    public BooleanProperty validQuantityUOM(){return isValidQuantityUOM;}
    public BooleanProperty validCategoryProp(){return isValidCategory;}
    public BooleanProperty validPriceProp(){return isValidPrice;}
    public BooleanProperty validPriceUOMProp(){return isValidPriceUOM;}


    public BooleanProperty validQuantityProp(){return isValidQuantity;}
    public BooleanProperty validExpireDateProp(){return isValidExpireDate;}

    public void setIsNewRedcordBoolean(boolean value){
        isNewRecord.set(value);
    }

    public BooleanProperty validFormProp() {
        return formValid;
    }

    public BooleanProperty allowSaveProperty(){return allowSave;}

    public BooleanProperty validItemStockFormProp(){return validItemStockForm;}

    public NewInventoryItemViewModel(){

        minStock.set("0.0");
        itemLimit.set("0.0");

        stockStatus.set("Out of Stock");
        inventoryItemCount.set("0");
        totalQuantity.set("0");

        displayQuantityAndStockStatus();
        trackTableViewPage = 1;
        maxPages = 1;
        itemStockListMap = new HashMap<>();
        itemStockListMap.put(maxPages, new ArrayList<>());
        setTBVPageDisplay(null);
        isNewRecord.set(false);

        inventoryId.set("New Inventory Item");
        validItemStockForm.set(false);

        priceUOM.set("Select Currency");
        isValidPriceUOM.set(false);

        quantityUOM.set("Select UOM");
        isValidQuantityUOM.set(false);

        validItemStockList = List.of(
                isValidQuantity,
                isValidExpireDate
        );

        itemStockInputList = List.of(
                quantity,
                expireDate
        );

        validInputList = List.of(
                isValidName,
                isValidItemLimit,
                isValidMinStock,
                isValidCategory,
                isValidQuantityUOM
        );

        inventoryInputList = List.of(
                name,
                itemLimit,
                minStock,
                category,
                quantityUOM,
                price,
                priceUOM
        );

        inventoryId.addListener((obs, oldVal, newVal) -> {
            isValidInventoryId.set(!newVal.equals("New Inventory Item"));
            hasChanged.set(true);
        });

        name.addListener((obs, oldVal, newVal) -> {
            isValidName.set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        itemLimit.addListener((obs, oldVal, newVal) -> {
            isValidItemLimit.set(NewInventoryItemUtils.isValidQuantity(newVal));
            hasChanged.set(true);
        });

        minStock.addListener((obs, oldVal, newVal) -> {
            isValidMinStock.set(NewInventoryItemUtils.isValidQuantity(newVal));
            hasChanged.set(true);
        });

        category.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidCategory.set(!newVal.equals("Select Category"));
                hasChanged.set(true);
            }
        });

        quantityUOM.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidQuantityUOM.set(!newVal.equals("Select UOM"));
                hasChanged.set(true);
            }
        });

        price.addListener((obs, oldVal, newVal) -> {
            isValidPrice.set(NewMenuItemUtils.isValidPrice(newVal) && isValidPriceUOM.get());
            hasChanged.set(true);
        });

        priceUOM.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidPriceUOM.set(!newVal.equals("Select Currency"));
                isValidPrice.set(NewMenuItemUtils.isValidPrice(price.get()) && isValidPriceUOM.get());
                hasChanged.set(true);
            }
        });

        formValid.bind(validInventoryIdProp()
                .and(validNameProp())
                .and(validCategoryProp())
                .and(validItemLimitProp())
                .and(validMinStockProp())
                .and(validQuantityUOM())
        );

        allowSave.bind(formValid.and(hasChanged));

        validItemStockForm.bind(validQuantityProp()
                .and(validExpireDateProp())
        );


        quantity.addListener((obs, oldVal, newVal) -> {
            isValidQuantity.set(NewInventoryItemUtils.isValidQuantity(newVal));
        });

        expireDate.addListener((obs, oldVal, newVal) -> {
            isValidExpireDate.set(NewInventoryItemUtils.isValidExpDateFormat(newVal));
        });


    }

    public String generateInvItemID() {
        String featureInitials = "inv";
        String itemNameShort = name.get().substring(0, 3).toLowerCase();
        String randomDigits = String.format("%04d", new Random().nextInt(10000));

        return featureInitials + itemNameShort + randomDigits;
    }

    public String generateItemStockID(){
        String itemNameShort = name.get().substring(0, 3).toLowerCase();
        String newIdGenerated = itemNameShort + inventoryItemCount.get();
        int checkNum = Integer.parseInt(inventoryItemCount.get()) + 1;
        inventoryItemCount.set(String.valueOf(checkNum));
        return newIdGenerated;
    }

    public boolean onSubmit(){

        for(BooleanProperty validInput: validInputList){
            if(!validInput.get()){
                return false;
            }
        }

        SystemMessageUtils.setCurrSystemText("New inventory item record has been saved: "+inventoryId.get());
        SystemMessageUtils.setCurrPropertyColor("SUCCESS");
        SystemMessageUtils.messageAnimation();

        if(!validPriceProp().get()){
            price.set("--");
            priceUOM.set("--");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("inventoryId", inventoryId.get());
        data.put("inventoryName", name.get());
        data.put("category", category.get());
        data.put("stockStatus", stockStatus.get());
        data.put("totalQuantity",totalQuantity.get());
        data.put("minStock", minStock.get());
        data.put("quantityUOM", quantityUOM.get());
        data.put("itemLimit", itemLimit.get());
        data.put("itemPrice", price.get());
        data.put("priceUOM", priceUOM.get());
        data.put("inventoryItemCount", inventoryItemCount.get());


        List<ItemStock> entireItemStockList = new ArrayList<>();
        for (Map.Entry<Integer, List<ItemStock>> entry : itemStockListMap.entrySet()) {
            entireItemStockList.addAll(entry.getValue());
        }

        List<Map<String, Object>> itemStockForFirestore = new ArrayList<>();
        for (ItemStock itemStock : entireItemStockList) {
            Map<String, Object> itemStockMap = new HashMap<>();
            itemStockMap.put("itemId", itemStock.getItemId());
            itemStockMap.put("quantity", String.valueOf(itemStock.getItemAmount()));
            itemStockMap.put("expireDate", itemStock.getExpireDate());
            itemStockForFirestore.add(itemStockMap);
        }

        data.put("itemStockList",itemStockForFirestore);

        FirestoreUtils.writeDoc("InventoryV2", inventoryId.get(), data);
        hasChanged.set(false);

        return true;
    }

    public void clearInputs(){
        for(StringProperty userInput: inventoryInputList){
            userInput.set("");
        }

        for(StringProperty userInput: itemStockInputList){
            userInput.set("");
        }

        priceUOM.set("Select Currency");
        category.set("Select Category");
        quantityUOM.set("Select UOM");

        stockStatus.set("Out of Stock");
        inventoryItemCount.set("0");
        totalQuantity.set("0");
    }


    public void clearStockItemInputs(){
        for(StringProperty userInput: itemStockInputList){
            userInput.set("");
        }
    }

    public void updateValidImageViews(HBox hBox, BooleanProperty... properties) {

        Image requiredIconImg = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/requiredIcon.png")).toExternalForm());
        Image validIconImage = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/valid.png")).toExternalForm());

        ImageView imageView = (ImageView) hBox.lookup(".image-view");
        Label label = (Label) hBox.lookup(".label");

        if (imageView != null && requiredIconImg!=null && validIconImage != null) {

            imageView.setVisible(true);
            imageView.setImage(requiredIconImg);

            boolean allTrue = false;
            for (BooleanProperty property : properties) {
                if (property.get()) {
                    allTrue = true;
                    break;
                }
            }

            if (allTrue) {

                imageView.setImage(validIconImage);

                if(label.getText().equals("Name:") && isNewRecord.get()){
                    String newEmployeeId = generateInvItemID();
                    inventoryId.set(newEmployeeId);
                    System.out.println("CHECKING NEW GENERATED MENU ITEM ID: "+newEmployeeId);
                }

            } else {

                if(label.getText().equals("Name:") && isNewRecord.get()){
                    inventoryId.set("New Inventory Item");
                }
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }

    public void populateTextFields(TableView<ItemStock> itemStockTV){

        Object selectedData = TableViewUtils.retrieveDocumentData(TableViewUtils.getStoredCollectionName(),TableViewUtils.getSelectedRowID());

        if(selectedData instanceof InventoryItem obj){

            System.out.println("Inventory Item obj exists");
            inventoryId.set(obj.getInventoryId());
            name.set(obj.getInventoryName());
            category.set(obj.getCategory());
            itemLimit.set(String.valueOf(obj.getItemLimit()));
            minStock.set(String.valueOf(obj.getMinStock()));
            quantityUOM.set(obj.getQuantityUOM());
            category.set(obj.getCategory());
            price.set(obj.getItemPrice());
            priceUOM.set(obj.getPriceUOM());

            stockStatus.set(obj.getStockStatus());
            totalQuantity.set(String.valueOf(obj.getTotalQuantity()));
            inventoryItemCount.set(String.valueOf(obj.getInventoryItemCount()));

            displayQuantityAndStockStatus();

            List<ItemStock> retrieveItemStockList = obj.getItemStockList();

            int checkTotalQuantity = 0;

            if(retrieveItemStockList != null){
                for(int i = 0; i < retrieveItemStockList.size(); ++i){
                    System.out.println("retrieveItemStockList item: "+retrieveItemStockList.get(i).toString());
                    List<ItemStock> latestItemStockList = itemStockListMap.get(maxPages);
                    if(latestItemStockList != null){
                        if(latestItemStockList.size() == 7){
                            maxPages++;
                            itemStockListMap.put(maxPages, new ArrayList<>());
                        }
                        itemStockListMap.get(maxPages).add(retrieveItemStockList.get(i));
                        checkTotalQuantity += retrieveItemStockList.get(i).getItemAmount();
                    }
                }

                setTBVPageDisplay(itemStockTV);
            }

            totalQuantity.set(String.valueOf(checkTotalQuantity));
            stockStatus.set(NewInventoryItemUtils.calculateStockStatus(minStock.get(),totalQuantity.get()));



            hasChanged.set(false);

            TableViewUtils.setStoreCollectionName("");
            TableViewUtils.setSelectedRowID(null);
        }
    }

    private void setTBVPageDisplay(TableView<ItemStock> itemStockTV){
        tbvPageNumDisplay.set(trackTableViewPage+" of "+maxPages+" pages");

        List<ItemStock> latestIngredientList = itemStockListMap.get(trackTableViewPage);

        if(latestIngredientList != null && itemStockTV!=null){
            itemStockList = FXCollections.observableArrayList(latestIngredientList);
            itemStockTV.setItems(itemStockList);
        }
    }

    public void handleTableViewNav(String status, TableView<ItemStock> itemStockTV){
        switch (status){
            case "LEFT":
                if(trackTableViewPage > 1){
                    trackTableViewPage--;
                }
                break;
            case "RIGHT":
                if(trackTableViewPage < maxPages){
                    trackTableViewPage++;
                }
                break;
        }


        setTBVPageDisplay(itemStockTV);
    }

    private void displayQuantityAndStockStatus(){
        stockStatusDisplay.set("Stock Status: "+stockStatus.get());
        stockQuantityDisplay.set("Total Quantity: "+totalQuantity.get());
    }

    public void onAddItemStock(TableView<ItemStock> itemStockTV){
        for(BooleanProperty validInput: validItemStockList){
            if(!validInput.get()){
                return;
            }
        }

        itemStockId.set(generateItemStockID());

        ItemStock newItemStock = new ItemStock(itemStockId.get(), Integer.parseInt(quantity.get()), expireDate.get());

        totalQuantity.set(NewInventoryItemUtils.calculateTotalStockQuantity("ADD",totalQuantity.get(), newItemStock));
        stockStatus.set(NewInventoryItemUtils.calculateStockStatus(minStock.get(),totalQuantity.get()));
        displayQuantityAndStockStatus();

        List<ItemStock> latestItemStockList = itemStockListMap.get(maxPages);
        if(latestItemStockList == null){
            itemStockListMap.put(maxPages, new ArrayList<>());
        }

        latestItemStockList = itemStockListMap.get(maxPages);
        if(latestItemStockList.size() == 7){
            maxPages++;
            itemStockListMap.put(maxPages, new ArrayList<>());
        }
        itemStockListMap.get(maxPages).add(newItemStock);

        setTBVPageDisplay(itemStockTV);
        clearStockItemInputs();
    }

    public void onDeleteItemStock(TableView<ItemStock> itemStockTV){
        System.out.println("onDeleteItemStock Called");

        if(currSelectedItemStock!=null){
            boolean hasFoundElement = false;
            for (Map.Entry<Integer, List<ItemStock>> entry : itemStockListMap.entrySet()) {
                List<ItemStock> entireItemStockList = entry.getValue();
                for (ItemStock item : entireItemStockList) {

                    boolean idCheck = currSelectedItemStock.getItemId().equals(item.getItemId());
                    boolean quantityCheck = (currSelectedItemStock.getItemAmount() == item.getItemAmount());
                    boolean expDateCheck = currSelectedItemStock.getExpireDate().equals(item.getExpireDate());

                    if(idCheck && quantityCheck && expDateCheck){

                        totalQuantity.set(NewInventoryItemUtils.calculateTotalStockQuantity("DELETE",totalQuantity.get(), item));
                        stockStatus.set(NewInventoryItemUtils.calculateStockStatus(minStock.get(),totalQuantity.get()));
                        displayQuantityAndStockStatus();

                        System.out.println("ItemStock has been removed: "+item.getItemId());
                        entry.getValue().remove(item);
                        hasFoundElement = true;
                        break;
                    }

                    if(hasFoundElement){break;}

                }
            }
            if(hasFoundElement){setTBVPageDisplay(itemStockTV);}

        }

    }

    public void handleItemStockTableView(String status, TableView<ItemStock> itemStockTV){
        switch (status){
            case "RESET":
                if (itemStockTV.getItems() != null && !itemStockTV.getItems().isEmpty()) {
                    itemStockTV.getItems().clear();
                    itemStockListMap.clear();
                }
                break;
            case "INITIAL":
                itemStockTV.setItems(itemStockList);

                TableColumn<ItemStock, String> columnOne = new TableColumn<ItemStock,String>("Item ID");
                columnOne.setCellValueFactory(new PropertyValueFactory<ItemStock,String>("itemId"));

                TableColumn<ItemStock, String> columnTwo = new TableColumn<ItemStock,String>("Quantity");
                columnTwo.setCellValueFactory(new PropertyValueFactory<ItemStock,String>("itemAmount"));

                TableColumn<ItemStock, String> columnThree= new TableColumn<ItemStock,String>("Exp. Date (MM/DD/YYYY)");
                columnThree.setCellValueFactory(new PropertyValueFactory<ItemStock,String>("expireDate"));

                itemStockTV.getColumns().add(columnOne);
                itemStockTV.getColumns().add(columnTwo);
                itemStockTV.getColumns().add(columnThree);

                itemStockTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                break;
            case "ADD":
                onAddItemStock(itemStockTV);
                hasChanged.set(true);
                break;

            case "DELETE":
                onDeleteItemStock(itemStockTV);
                hasChanged.set(true);
                break;

        }
    }

}
