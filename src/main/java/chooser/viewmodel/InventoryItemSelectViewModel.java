package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.CurrentPageOptions;
import chooser.model.InventoryItem;
import chooser.model.MenuItem;
import chooser.model.User;
import chooser.utils.NewInventoryItemUtils;
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

                collectionName = "InventoryV2";

                currentTableViewPage.set(1);
                leftArrowVisible.set(false);
                leftArrowBtnDisable.set(true);

                rightArrowVisible.set(true);
                rightArrowBtnDisable.set(false);

                QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                if (snapshot != null) {
                    entireDataCollection = TableViewUtils.prepareTableViewData(InventoryItem.class, collectionName, snapshot);
                    storedColumnClickableName = "inventoryId";
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
