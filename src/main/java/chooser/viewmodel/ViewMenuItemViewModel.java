package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.IngredientItem;
import chooser.model.InventoryItem;
import chooser.model.MenuItem;
import chooser.model.OrderMenuDisplay;
import chooser.utils.IngredientConversionUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.ProgressLoadUtils;
import chooser.utils.TableViewUtils;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class ViewMenuItemViewModel {

    private final BooleanProperty isDisplayMenu = new SimpleBooleanProperty(false);
    private final StringProperty displayItemName = new SimpleStringProperty("");
    private final StringProperty displayItemImgFileName = new SimpleStringProperty("");
    private final IntegerProperty servingLeftAmount = new SimpleIntegerProperty(-1);

    private TableView currentTableView;
    private List<OrderMenuDisplay> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();
    private String storedColumnClickableName;
    private  int maxRecordCount;
    private int maxPageCount;
    private int initialSetRowDisplay;

    private String collectionName;
    private  MenuItem foundMenuItem;

    private final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    private final StringProperty recordsAmountLbl = new SimpleStringProperty("");
    private final StringProperty maxTableViewPageLbl = new SimpleStringProperty("");
    private final IntegerProperty currentTableViewPage = new SimpleIntegerProperty(1);
    private final BooleanProperty leftArrowBtnDisable = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowBtnDisable = new SimpleBooleanProperty(true);

    private final BooleanProperty leftArrowVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowVisible = new SimpleBooleanProperty(true);

    public  ViewMenuItemViewModel(){
        servingLeftAmount.set(-1);
        foundMenuItem = null;
        displayItemImgFileName.set(null);
        displayItemName.set("");

        isDisplayMenu.set(false);
        isDisplayMenu.bind(NewMenuItemUtils.checkDisplayMenuItemProp());
        isDisplayMenu.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setUp();
            }
        });

        initialSetRowDisplay = 10;
        currentTableViewPage.set(1);

        entireDataCollection = null;
    }
    public BooleanProperty isDisplayMenuProp(){return isDisplayMenu;}
    public StringProperty displayItemNameProp(){return  displayItemName;}
    public StringProperty displayItemImgFileNameProp(){return  displayItemImgFileName;}
    public IntegerProperty servingLeftAmountProp(){return  servingLeftAmount;}

    public void setCurrTableView(TableView tableView){currentTableView = tableView;}
    public  BooleanProperty getLeftArrowVisible(){return leftArrowVisible;}
    public  BooleanProperty getRightArrowVisible(){return rightArrowVisible;}
    public  BooleanProperty getLeftArrowBtnDisable(){return leftArrowBtnDisable;}
    public  BooleanProperty getRightArrowBtnDisable(){return rightArrowBtnDisable;}

    public StringProperty getRecordsAmountLbl() {
        return recordsAmountLbl;
    }
    public StringProperty getMaxTableViewPageLbl(){return maxTableViewPageLbl;}
    public IntegerProperty getCurrentTableViewPage(){return currentTableViewPage;}


    public MenuItem findMenuItem(List<MenuItem> menuItemList){
        for(int i = 0; i < menuItemList.size(); ++i){
            if(menuItemList.get(i).getId().equals(NewMenuItemUtils.selectedOrderMenuItemProp().get())){
                return menuItemList.get(i);
            }
        }
        return null;
    }
    public void setUp(){
        ProgressLoadUtils.showProgressLoad();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                currentTableViewPage.set(1);
                leftArrowVisible.set(false);
                leftArrowBtnDisable.set(true);

                rightArrowVisible.set(true);
                rightArrowBtnDisable.set(false);

                String inventoryCollection = "InventoryV2";
                String menuCollection = "Menu";
                collectionName = "OrderMenuDisplay";

                QuerySnapshot snapshotInventory = FirestoreUtils.getAllDocuments(inventoryCollection);
                QuerySnapshot snapshotMenu = FirestoreUtils.getAllDocuments(menuCollection);

                if (snapshotInventory != null && snapshotMenu != null) {

                    List<MenuItem> menuItemsList = TableViewUtils.prepareTableViewData(
                            MenuItem.class,
                            "Menu",
                            snapshotMenu
                    );
                    List<InventoryItem> inventoryItemList = TableViewUtils.prepareTableViewData(
                            InventoryItem.class,
                            "InventoryV2",
                            snapshotInventory
                    );

                    foundMenuItem = findMenuItem(menuItemsList);

                    System.out.println("foundMenuItem: "+foundMenuItem.getName());
                    System.out.println("menuItemsList size: "+menuItemsList.size());
                    System.out.println("inventoryItemList size: "+inventoryItemList.size());
                    if(foundMenuItem != null){

                        List<OrderMenuDisplay> newDisplayData = new ArrayList<>();

                        List<IngredientItem> retrieveIngredientList = foundMenuItem.getIngredientsList();
                        for(int i = 0; i < retrieveIngredientList.size(); ++i){

                            IngredientItem ingItem = retrieveIngredientList.get(i);
                            String retrieveLinkInvId = ingItem.getLinkInventoryId();

                            for(int j = 0; j < inventoryItemList.size(); ++j){

                                InventoryItem invItem = inventoryItemList.get(j);

                                if(invItem.getInventoryId().equals(retrieveLinkInvId)){

                                    int calculateRemainUses = (int)IngredientConversionUtils.calculateIngredientUses(
                                            Double.parseDouble(ingItem.getQuantity()),
                                            ingItem.getUom(),
                                            Double.parseDouble(String.valueOf(invItem.getTotalQuantity())),
                                            invItem.getQuantityUOM()
                                    );

                                    System.out.println("Remaining Uses for ingredient: "+ingItem.getName()+"/"+calculateRemainUses);
                                    newDisplayData.add(new OrderMenuDisplay(
                                            ingItem.getName(),
                                            ingItem.getQuantity(),
                                            ingItem.getUom(),
                                            calculateRemainUses,
                                            retrieveLinkInvId,
                                            invItem.getStockStatus(),
                                            invItem.getTotalQuantity(),
                                            invItem.getQuantityUOM()
                                    ));
                                    break;
                                }
                            }
                        }

                        System.out.println("newDisplayData size: "+newDisplayData.size());
                        entireDataCollection = newDisplayData;
                        //storedColumnClickableName = "inventoryId";
                    }

                }

                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {

                    if(entireDataCollection!=null){
                        int retrieveServingsLeft = IngredientConversionUtils.calculateMaxServings(entireDataCollection);
                        System.out.println("retrieveServingsLeft: "+retrieveServingsLeft);
                        servingLeftAmount.set(retrieveServingsLeft);
                        maxRecordCount = entireDataCollection.size();
                        maxPageCount = (maxRecordCount + initialSetRowDisplay - 1) / initialSetRowDisplay;
                        splitPageRowData = splitDataToPages(entireDataCollection);
                        tableViewData();
                        checkArrowDisable();
                    }

                    if(foundMenuItem!=null){
                        displayItemName.set(foundMenuItem.getName());
                        displayItemImgFileName.set(foundMenuItem.getItemImage());
                    }

                    TableViewUtils.addCollectionToMap(collectionName, entireDataCollection);

                    ProgressLoadUtils.hideProgressLoad();
                });
            }

            protected void failed() {
                Platform.runLater(() -> {
                    System.out.println("Failed to load data.");
                    ProgressLoadUtils.hideProgressLoad();
                });
            }
        };

        new Thread(task).start();

    }

    public void checkArrowDisable(){
        leftArrowBtnDisable.set(currentTableViewPage.get() <= 1);
        leftArrowVisible.set(currentTableViewPage.get() > 1);

        rightArrowBtnDisable.set(currentTableViewPage.get() >= maxPageCount);
        rightArrowVisible.set(currentTableViewPage.get() < maxPageCount);
    }

    public void adjustPageDirection(String direction){
        switch (direction){
            case "left":
                if(!leftArrowBtnDisable.get()){
                    currentTableViewPage.set(currentTableViewPage.get()-1);
                    tableViewData();
                }

                break;

            case "right":
                if(!rightArrowBtnDisable.get()){
                    currentTableViewPage.set(currentTableViewPage.get()+1);
                    tableViewData();
                }
                break;
            default:
                System.out.println("Page Direction doesnt exist for table view: "+direction);
        }


        checkArrowDisable();



        System.out.println("User Page Direction: "+direction);
        System.out.println("Current Table View:: "+currentTableViewPage.get());

    }

    public <T> Map<Integer, List<?>> splitDataToPages(List<T> data){

        Map<Integer, List<?>> pages = new HashMap<>();
        int pageNumber = 1;

        for (int i = 0; i < data.size(); i += initialSetRowDisplay) {
            pages.put(pageNumber++, data.subList(i, Math.min(i + initialSetRowDisplay, data.size())));
        }

        return pages;

    }

    public void tableViewData(){

        if(currentTableView!=null && splitPageRowData!=null && collectionName!=null){

            TableViewUtils.populateTableView(
                    "DEFAULT",
                    currentTableView,
                    splitPageRowData.get(currentTableViewPage.get()),
                    retrieveColumnTitles(collectionName),
                    storedColumnClickableName,
                    data ->{
                        String dataID = data.getClass().getName();
                        System.out.println("Clicked on: " + dataID);
                    });

            int minRecordNumDisplayed = (currentTableViewPage.get()-1) * initialSetRowDisplay + 1;
            List<?> pageData = splitPageRowData.get(currentTableViewPage.get());
            int currentPageSize = (pageData != null) ? pageData.size() : 1;
            //int currentPageSize = splitPageRowData.get(currentTableViewPage.get()).size();
            int maxRecordNumDisplayed = Math.min(minRecordNumDisplayed + currentPageSize - 1, maxRecordCount);

            recordsAmountLbl.set(minRecordNumDisplayed+"-"+maxRecordNumDisplayed+" of "+maxRecordCount+" records");

            maxTableViewPageLbl.set("of "+splitPageRowData.size()+" pages");


        }
    }

    public void userPageNum(int value){
        if(value >= maxPageCount){
            currentTableViewPage.set(maxPageCount);
        }else if(value <= 1) {
            currentTableViewPage.set(1);
        }else{
            currentTableViewPage.set(value);
        }

        tableViewData();
        checkArrowDisable();
    }


}
