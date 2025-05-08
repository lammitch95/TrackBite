package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import chooser.utils.*;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static chooser.utils.ExcelExporter.exportTable;
import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class TableViewViewModel {

    private TableView currentTableView;
    private List<?> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();

    private String storedColumnClickableName;
    private  int maxRecordCount;
    private int maxPageCount;

    private int initialSetRowDisplay;
    private String selectedRowID;
    private String collectionName;


    private final Map<String, String> titleMapping = new HashMap<>();
    private final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    private final StringProperty recordsAmountLbl = new SimpleStringProperty("");
    private final StringProperty maxTableViewPageLbl = new SimpleStringProperty("");
    private final IntegerProperty currentTableViewPage = new SimpleIntegerProperty(1);
    private final BooleanProperty leftArrowBtnDisable = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowBtnDisable = new SimpleBooleanProperty(true);

    private final BooleanProperty leftArrowVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowVisible = new SimpleBooleanProperty(true);


    public TableViewViewModel(){
        initialSetRowDisplay = 12;
        currentTableViewPage.set(1);
        selectedRowID = "";
        collectionName = "";
        entireDataCollection = null;

        titleMapping.put("View Accounts","Accounts");
        titleMapping.put("View Menu Items","Menu");
        titleMapping.put("View Inventory", "Inventory");
        titleMapping.put("View Order History", "Customer Order History");
        titleMapping.put("View Suppliers", "Suppliers");
        titleMapping.put("View Purchase Orders", "Purchase Orders");
        titleMapping.put("View Received Items", "Received Items");

    }

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

    public Map<String,String> getTitleMapping() {return titleMapping;}

    private void handleDeleteDocument(){
        if(selectedRowID!=null && collectionName!=null){
            boolean checkHasDeleted = FirestoreUtils.deleteDoc(collectionName, selectedRowID);
            if(checkHasDeleted){
                SystemMessageUtils.setCurrSystemText(selectedRowID+" has been successfully delete.");
                SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                SystemMessageUtils.messageAnimation();
                setUp();
            }else{
                System.out.println("DOCUMENT DELETION FAILED. SOMETHING WENT WRONG!!");
                SystemMessageUtils.setCurrSystemText("An error occurred when deleting account for user: "+selectedRowID);
                SystemMessageUtils.setCurrPropertyColor("ERROR");
                SystemMessageUtils.messageAnimation();
            }

        }

    }



    private void handleExportExcelTableView(){
        try {

            TableViewUtils.populateTableView(
                    "EXPORT",
                    currentTableView,
                    entireDataCollection,
                    retrieveColumnTitles(collectionName),
                    storedColumnClickableName,
                    null
            );
            boolean hasExportSucess = exportTable(currentTableView);
            if(hasExportSucess){
                System.out.println("Excel file saved successfully.");
                SystemMessageUtils.setCurrSystemText("Excel file created successfully.");
                SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                SystemMessageUtils.messageAnimation();
            }else{
                System.err.println("Error exporting to Excel.");
                SystemMessageUtils.setCurrSystemText("Error occurred creating excel file.");
                SystemMessageUtils.setCurrPropertyColor("ERROR");
                SystemMessageUtils.messageAnimation();
            }

            setUp();
        } catch (IOException ex) {

            System.out.println("Couldn't export table data to excel.");
            SystemMessageUtils.setCurrSystemText("Error occurred creating excel file.");
            SystemMessageUtils.setCurrPropertyColor("ERROR");
            SystemMessageUtils.messageAnimation(); 
            throw new RuntimeException(ex);

        }
    }
    public void headerMultiOptions(String optionName){
        switch (optionName){
            case "DELETE":
                handleDeleteDocument();
                break;
            case "ADD NEW":
                TableViewUtils.handleNewDocument();
                break;
            case "REFRESH":
                setUp();
                break;
            case "EXPORT EXCEL":
                handleExportExcelTableView();
                break;
            default:
                System.out.println("This option doesnt exist in table view: "+optionName);
        }
    }


    public void storeRowSelectedID(Object value){
        if(!collectionName.isEmpty()){
            switch (collectionName){
                case "Employees":
                    if (value instanceof User selectedUser) {
                        String retrieveEmployeeID = selectedUser.getUsername();
                        System.out.println("Selected User employee ID on selected Row: " + retrieveEmployeeID);
                        selectedRowID = retrieveEmployeeID;
                    } else {
                        System.out.println("The selected value is not a User object.");
                    }
                    break;

                case "Menu":
                    if (value instanceof MenuItem selectedMenuItem) {
                        String menuItemID = selectedMenuItem.getId();
                        System.out.println("Selected Menu Item ID on selected Row: " + menuItemID);
                        selectedRowID = menuItemID;
                    } else {
                        System.out.println("The selected value is not a Menu Item object.");
                    }
                    break;
                case "InventoryV2":
                    if (value instanceof InventoryItem selectedInventoryItem) {
                        String inventoryItemID = selectedInventoryItem.getInventoryId();
                        System.out.println("Selected Inventory Item ID on selected Row: " + inventoryItemID);
                        selectedRowID = inventoryItemID;
                    } else {
                        System.out.println("The selected value is not a Inventory Item object.");
                    }
                    break;
                case "SuppliersV2":
                    if (value instanceof SupplierInfo selectedSupplier) {
                        String supplierID = selectedSupplier.getSupplierId();
                        System.out.println("Selected Supplier ID on selected Row: " + supplierID);
                        selectedRowID = supplierID;
                    } else {
                        System.out.println("The selected value is not a Inventory Item object.");
                    }
                    break;
                case "PurchaseOrders":
                    if (value instanceof PurchaseOrder selectedPO) {
                        String poId = selectedPO.getId();
                        System.out.println("Selected Purcahse Order ID on selected Row: " + poId);
                        selectedRowID = poId;
                    } else {
                        System.out.println("The selected value is not a Inventory Item object.");
                    }
                    break;
                case "ReceivedItems":
                    if (value instanceof ReceiveItems selectedRecv) {
                        String poId = selectedRecv.getId();
                        System.out.println("Selected ReceiveItems ID on selected Row: " + poId);
                        selectedRowID = poId;
                    } else {
                        System.out.println("The selected value is not a ReceiveItems object.");
                    }
                    break;
                case "CustomerOrderHistory":
                    if (value instanceof LoggedOrder selectedLoggedOrderItem) {
                        String itemID = selectedLoggedOrderItem.getId();
                        System.out.println("Selected Logged Order ID on selected Row: " + itemID);
                        selectedRowID = itemID;
                    } else {
                        System.out.println("The selected value is not a Inventory Item object.");
                    }
                    break;


                default:
                    System.out.println("Collection doesnt exist to store row ID");
            }
        }

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

        if(currentTableView!=null && splitPageRowData!=null && storedColumnClickableName!=null && collectionName!=null){

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


    public void setUp(){

        ProgressLoadUtils.showProgressLoad();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String pageOption = CurrentPageOptions.getCurrPageOption();

                switch(pageOption){
                    case "View Accounts":
                        collectionName = "Employees";
                        break;
                    case "View Menu Items":
                        collectionName = "Menu";
                        break;
                    case "View Inventory":
                        collectionName = "InventoryV2";
                        break;
                    case "View Suppliers":
                        collectionName = "SuppliersV2";
                        break;
                    case "View Purchase Orders":
                        collectionName = "PurchaseOrders";
                        break;
                    case "View Received Items":
                        collectionName = "ReceivedItems";
                        break;
                    case "View Order History":
                        collectionName = "CustomerOrderHistory";
                        break;

                    default:
                        System.out.println("Collection Name doesnt exist for Table View");
                }

                TableViewUtils.setStoreCollectionName(collectionName);


                if (collectionName != null && !collectionName.isEmpty()) {

                    currentTableViewPage.set(1);
                    leftArrowVisible.set(false);
                    leftArrowBtnDisable.set(true);

                    rightArrowVisible.set(true);
                    rightArrowBtnDisable.set(false);

                    QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                    if (snapshot != null) {

                        switch (collectionName){
                            case "Employees":
                                entireDataCollection = TableViewUtils.prepareTableViewData(User.class, collectionName, snapshot);
                                storedColumnClickableName = "username";
                                break;

                            case "Menu":
                                entireDataCollection = TableViewUtils.prepareTableViewData(MenuItem.class, collectionName, snapshot);
                                storedColumnClickableName = "id";
                                break;

                            case "InventoryV2":
                                entireDataCollection = TableViewUtils.prepareTableViewData(InventoryItem.class, collectionName, snapshot);
                                storedColumnClickableName = "inventoryId";
                                break;
                            case "SuppliersV2":
                                entireDataCollection = TableViewUtils.prepareTableViewData(SupplierInfo.class, collectionName, snapshot);
                                storedColumnClickableName = "supplierId";
                                break;
                            case "PurchaseOrders":
                                entireDataCollection = TableViewUtils.prepareTableViewData(PurchaseOrder.class, collectionName, snapshot);
                                storedColumnClickableName = "id";
                                break;
                            case "ReceivedItems":
                                entireDataCollection = TableViewUtils.prepareTableViewData(ReceiveItems.class, collectionName, snapshot);
                                storedColumnClickableName = "id";
                                break;
                            case "CustomerOrderHistory":
                                entireDataCollection = TableViewUtils.prepareTableViewData(LoggedOrder.class, collectionName, snapshot);
                                storedColumnClickableName = "id";
                                break;

                            default:
                                System.out.println("Collection Doesnt Exist. Table View Failed..");
                        }

                        System.out.println("Checking entireDataCollection Size: "+entireDataCollection.size());

                    }
                }

                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {

                    if(entireDataCollection!=null){
                        maxRecordCount = entireDataCollection.size();
                        maxPageCount = (maxRecordCount + initialSetRowDisplay - 1) / initialSetRowDisplay;
                        splitPageRowData = splitDataToPages(entireDataCollection);
                        tableViewData();
                        checkArrowDisable();
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
}
