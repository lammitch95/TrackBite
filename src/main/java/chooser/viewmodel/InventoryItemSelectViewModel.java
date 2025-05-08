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
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class InventoryItemSelectViewModel {

    private TableView currentTableView;
    private List<?> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();

    private String storedColumnClickableName;
    private  int maxRecordCount;
    private int maxPageCount;

    private int initialSetRowDisplay;
    private String collectionName;


    private final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    private final StringProperty selectedRowID = new SimpleStringProperty("");
    private final StringProperty recordsAmountLbl = new SimpleStringProperty("");
    private final StringProperty maxTableViewPageLbl = new SimpleStringProperty("");
    private final IntegerProperty currentTableViewPage = new SimpleIntegerProperty(1);
    private final BooleanProperty leftArrowBtnDisable = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowBtnDisable = new SimpleBooleanProperty(true);

    private final BooleanProperty leftArrowVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowVisible = new SimpleBooleanProperty(true);
    private final BooleanProperty isValidItemSelect = new SimpleBooleanProperty(false);


    public InventoryItemSelectViewModel(){
        initialSetRowDisplay = 12;
        currentTableViewPage.set(1);
        isValidItemSelect.set(false);
        selectedRowID.set("No item selected.");
        collectionName = "";
        entireDataCollection = null;

        selectedRowID.addListener((obs, oldVal, newVal) -> {
            isValidItemSelect.set(!newVal.equals("No item selected."));
            if(!newVal.equals("No item selected.")){
                NewMenuItemUtils.linkInventoryId().set(newVal);
            }

        });
    }

    public BooleanProperty validItemSelectProp(){return  isValidItemSelect;}
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

    public StringProperty getSelectedRowId(){return selectedRowID;}

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

                String currPageOption = CurrentPageOptions.getCurrPageOption();

                if(currPageOption.equals("New Purchase Order") && PurchaseOrderUtils.trackUsageProp().get().equals("Suppliers")) {
                    collectionName = "SuppliersV2";
                    QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                    if (snapshot != null) {
                        entireDataCollection = TableViewUtils.prepareTableViewData(SupplierInfo.class, collectionName, snapshot);
                        storedColumnClickableName = "supplierId";
                    }
                }else if(currPageOption.equals("New Received Items") && PurchaseOrderUtils.trackUsageProp().get().equals("PurchaseOrder")){
                        collectionName = "PurchaseOrders";
                        QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                        if (snapshot != null) {
                            entireDataCollection = TableViewUtils.prepareTableViewData(PurchaseOrder.class, collectionName, snapshot);
                            storedColumnClickableName = "id";
                        }
                }else{
                    collectionName = "InventoryV2";
                    QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                    if (snapshot != null) {
                        entireDataCollection = TableViewUtils.prepareTableViewData(InventoryItem.class, collectionName, snapshot);
                        storedColumnClickableName = "inventoryId";
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

    public void storeRowSelectedID(Object value){
        if (value instanceof InventoryItem selectedInventoryItem) {
            String inventoryItemID = selectedInventoryItem.getInventoryId();
            System.out.println("Selected Inventory Item ID on selected Row: " + inventoryItemID);
            selectedRowID.set(inventoryItemID);

            String checkOption = CurrentPageOptions.getCurrPageOption();
            switch(checkOption){
                case "New Purchase Order":
                    PurchaseOrderUtils.itemInventoryIdProp().set(selectedInventoryItem.getInventoryId());
                    PurchaseOrderUtils.itemNameProp().set(selectedInventoryItem.getInventoryName());
                    PurchaseOrderUtils.itemUOMProp().set(selectedInventoryItem.getQuantityUOM());
                    break;
                case "New Received Items":
                    PurchaseOrderUtils.itemInventoryIdProp().set(selectedInventoryItem.getInventoryId());
                    PurchaseOrderUtils.itemNameProp().set(selectedInventoryItem.getInventoryName());
                    PurchaseOrderUtils.itemUOMProp().set(selectedInventoryItem.getQuantityUOM());
                    break;
            }
        } else {
            System.out.println("The selected value is not a Inventory Item object.");
        }

        String checkOption = CurrentPageOptions.getCurrPageOption();

        if (value instanceof PurchaseOrder selectedItem) {

            String retrieveId = selectedItem.getId();
            selectedRowID.set(retrieveId);


            switch(checkOption){
                case "New Received Items":

                    PurchaseOrderUtils.setStoreRequestItemList(selectedItem.getRequestItemList());
                    PurchaseOrderUtils.triggerPopulateTableProp().set(!PurchaseOrderUtils.triggerPopulateTableProp().get());

                    PurchaseOrderUtils.purchaseOrderIdProp().set(retrieveId);

                    PurchaseOrderUtils.supplerIdProp().set(selectedItem.getSupplierId());
                    PurchaseOrderUtils.supplierNameProp().set(selectedItem.getSupplierName());

                    PrimaryContactInfo contact = selectedItem.getPrimaryContact();

                    PurchaseOrderUtils.prmcFirstNameProp().set(contact.getFirstName());
                    PurchaseOrderUtils.prmcLastNameProp().set(contact.getLastName());

                    PurchaseOrderUtils.prmcPhoneNumberProp().set(contact.getPhoneNum());
                    PurchaseOrderUtils.prmcEmailProp().set(contact.getEmail());

                    String checkWebsite = contact.getWebsite().isEmpty() ? "N/A" : contact.getWebsite();
                    PurchaseOrderUtils.prmcWebsiteProp().set(checkWebsite);

                    AddressInfo address = selectedItem.getAddressInfo();

                    PurchaseOrderUtils.warAddressLineProp().set(address.getAddressLine());
                    PurchaseOrderUtils.warCityProp().set(address.getCity());
                    PurchaseOrderUtils.warStateProp().set(address.getState());
                    PurchaseOrderUtils.warPostalCodeProp().set(address.getPostalCode());
                    PurchaseOrderUtils.warCountryProp().set(address.getCountry());

                    break;
            }


        }

        if (value instanceof SupplierInfo selectedItem) {
            String retrieveId = selectedItem.getSupplierId();
            selectedRowID.set(retrieveId);

            switch(checkOption){
                case "New Purchase Order":
                    PurchaseOrderUtils.supplerIdProp().set(retrieveId);
                    PurchaseOrderUtils.supplierNameProp().set(selectedItem.getSupplierName());

                    PurchaseOrderUtils.prmcFirstNameProp().set(selectedItem.getContactInfo().getFirstName());
                    PurchaseOrderUtils.prmcLastNameProp().set(selectedItem.getContactInfo().getLastName());

                    PurchaseOrderUtils.prmcPhoneNumberProp().set(selectedItem.getContactInfo().getPhoneNum());
                    PurchaseOrderUtils.prmcEmailProp().set(selectedItem.getContactInfo().getEmail());

                    String checkWebsite = selectedItem.getContactInfo().getWebsite().isEmpty() ? "N/A" : selectedItem.getContactInfo().getWebsite();
                    PurchaseOrderUtils.prmcWebsiteProp().set(checkWebsite);

                    PurchaseOrderUtils.warAddressLineProp().set(selectedItem.getWarehouseAddressInfo().getAddressLine());
                    PurchaseOrderUtils.warCityProp().set(selectedItem.getWarehouseAddressInfo().getCity());
                    PurchaseOrderUtils.warStateProp().set(selectedItem.getWarehouseAddressInfo().getState());
                    PurchaseOrderUtils.warPostalCodeProp().set(selectedItem.getWarehouseAddressInfo().getPostalCode());
                    PurchaseOrderUtils.warCountryProp().set(selectedItem.getWarehouseAddressInfo().getCountry());

                    break;
            }
        } else {
            System.out.println("The selected value is not a Inventory Item object.");
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
}
