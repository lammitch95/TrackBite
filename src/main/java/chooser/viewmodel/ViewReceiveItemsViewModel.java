package chooser.viewmodel;

import chooser.model.*;
import chooser.utils.PurchaseOrderUtils;
import chooser.utils.TableViewUtils;
import javafx.beans.property.*;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class ViewReceiveItemsViewModel {

    private TableView currentTableView;
    private List<?> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();
    private  int maxRecordCount;
    private int maxPageCount;
    private int initialSetRowDisplay;
    private String collectionName;

    private LoggedOrder findCurrLoggedOrderData;

    private final StringProperty receviedItemId = new SimpleStringProperty("");
    private final StringProperty purchaseOrderId = new SimpleStringProperty("");
    private final StringProperty deliveredDate = new SimpleStringProperty("");
    private final StringProperty deliveredTime = new SimpleStringProperty("");
    private final StringProperty receivedBy = new SimpleStringProperty("");

    private final StringProperty supplierIdAndName = new SimpleStringProperty("");
    private final StringProperty prmcName = new SimpleStringProperty("");
    private final StringProperty prmcPhoneNumber = new SimpleStringProperty("");
    private final StringProperty prmcEmail = new SimpleStringProperty("");
    private final StringProperty prmcWebsite = new SimpleStringProperty("");

    private final StringProperty addressLineOne = new SimpleStringProperty("");
    private final StringProperty addressLineTwo = new SimpleStringProperty("");
    private final StringProperty addressLineThree = new SimpleStringProperty("");


    private final StringProperty recordsAmountLbl = new SimpleStringProperty("");
    private final StringProperty maxTableViewPageLbl = new SimpleStringProperty("");
    private final IntegerProperty currentTableViewPage = new SimpleIntegerProperty(1);
    private final BooleanProperty leftArrowBtnDisable = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowBtnDisable = new SimpleBooleanProperty(true);

    private final BooleanProperty leftArrowVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty rightArrowVisible = new SimpleBooleanProperty(true);
    private final BooleanProperty isValidItemSelect = new SimpleBooleanProperty(false);

    public ViewReceiveItemsViewModel(){
        findCurrLoggedOrderData = null;
        initialSetRowDisplay = 12;
        currentTableViewPage.set(1);
        isValidItemSelect.set(false);
        entireDataCollection = null;
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


    public StringProperty receviedItemIdProp() { return receviedItemId; }
    public StringProperty purchaseOrderIdProp() { return purchaseOrderId; }
    public StringProperty deliveredDateProp() { return deliveredDate; }
    public StringProperty deliveredTimeProp() { return deliveredTime; }
    public StringProperty receivedByProp() { return receivedBy; }
    public StringProperty supplierIdAndNameProp() { return supplierIdAndName; }
    public StringProperty prmcNameProp() { return prmcName; }
    public StringProperty prmcPhoneNumberProp() { return prmcPhoneNumber; }
    public StringProperty prmcEmailProp() { return prmcEmail; }
    public StringProperty prmcWebsiteProp() { return prmcWebsite; }
    public StringProperty addressLineOneProp() { return addressLineOne; }
    public StringProperty addressLineTwoProp() { return addressLineTwo; }
    public StringProperty addressLineThreeProp() { return addressLineThree; }


    public void setUp(){

        collectionName = "RequestItem";

        currentTableViewPage.set(1);
        leftArrowVisible.set(false);
        leftArrowBtnDisable.set(true);

        rightArrowVisible.set(true);
        rightArrowBtnDisable.set(false);

        ReceiveItems selectedReceiveItems = PurchaseOrderUtils.getSelectedReceivedItem();
        if (selectedReceiveItems!=null) {

            receviedItemId.set(selectedReceiveItems.getId());
            purchaseOrderId.set("PO ID: "+selectedReceiveItems.getPurchaseOrderId());
            deliveredDate.set("Received Date: "+selectedReceiveItems.getDeliveredDate());
            deliveredTime.set("Received Time: "+selectedReceiveItems.getDeliveredTime());
            receivedBy.set("Received By: "+selectedReceiveItems.getReceivedBy());

            purchaseOrderId.set("PO ID: "+selectedReceiveItems.getPurchaseOrderId());

            supplierIdAndName.set(selectedReceiveItems.getSupplierId()+" | "+selectedReceiveItems.getSupplierName());

            PrimaryContactInfo prmcData = selectedReceiveItems.getPrimaryContact();
            prmcName.set("Name: "+prmcData.getFirstName() + " " + prmcData.getLastName());
            prmcPhoneNumber.set("Phone Number: "+prmcData.getPhoneNum());
            prmcEmail.set("Email: "+prmcData.getEmail());
            prmcWebsite.set("Website: "+prmcData.getWebsite());

            AddressInfo adressData = selectedReceiveItems.getAddressInfo();

            addressLineOne.set(adressData.getAddressLine());
            addressLineTwo.set(adressData.getCity()+" "+adressData.getState()+", "+adressData.getPostalCode());
            addressLineThree.set(adressData.getCountry());


            List<RequestItem> requestItemList = selectedReceiveItems.getRequestItemList();

            if(requestItemList != null && !requestItemList.isEmpty()){
                entireDataCollection = requestItemList;

                maxRecordCount = entireDataCollection.size();
                maxPageCount = (maxRecordCount + initialSetRowDisplay - 1) / initialSetRowDisplay;
                splitPageRowData = splitDataToPages(entireDataCollection);
                tableViewData();
                checkArrowDisable();

                TableViewUtils.addCollectionToMap(collectionName, entireDataCollection);

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

        if(currentTableView!=null && splitPageRowData!=null && collectionName!=null){

            TableViewUtils.populateTableView(
                    "No Selected",
                    currentTableView,
                    splitPageRowData.get(currentTableViewPage.get()),
                    retrieveColumnTitles(collectionName),
                    "inventoryId",
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
