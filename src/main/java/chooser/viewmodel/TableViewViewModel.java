package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.CurrentPageOptions;
import chooser.model.User;
import chooser.utils.ProgressLoadUtils;
import chooser.utils.SceneNavigator;
import chooser.utils.SystemMessageUtils;
import chooser.utils.TableViewUtils;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
        selectedRowID = "";
        collectionName = "";
        entireDataCollection = null;

        titleMapping.put("View Accounts","Accounts");

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
            int currentPageSize = splitPageRowData.get(currentTableViewPage.get()).size();
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

        System.out.println("INITIALIZED TABLE VIEW SETUP....");

        ProgressLoadUtils.showProgressLoad();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String pageOption = CurrentPageOptions.getCurrPageOption();

                switch(pageOption){
                    case "View Accounts":
                        collectionName = "Employees";
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

                            case "Inventory":
                                break;

                            case "Menu":
                                break;
                            default:
                                System.out.println("Collection Doesnt Exist. Table View Failed..");
                        }

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
