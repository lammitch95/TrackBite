package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.utils.ProgressLoadUtils;
import chooser.utils.SystemMessageUtils;
import chooser.utils.TableViewUtils;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chooser.utils.ExcelExporter.exportTable;
import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class DeliveryTableViewModel {

    private TableView currentTableView;
    private List<?> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();

    private String storedColumnClickableName;
    private int maxRecordCount;
    private int maxPageCount;

    private int initialSetRowDisplay;
    private String collectionName;

    private final Map<String, String> titleMapping = new HashMap<>();

    private final StringProperty recordsAmountLbl = new SimpleStringProperty("");
    private final StringProperty maxTableViewPageLbl = new SimpleStringProperty("");
    private final IntegerProperty currentTableViewPage = new SimpleIntegerProperty(1);

    public DeliveryTableViewModel(){
        initialSetRowDisplay = 12;
        currentTableViewPage.set(1);
        collectionName = "Deliveries";
        entireDataCollection = null;
        titleMapping.put("View Deliveries", "Deliveries");
    }

    public void setCurrTableView(TableView tableView){ currentTableView = tableView; }
    public StringProperty getRecordsAmountLbl() { return recordsAmountLbl; }
    public StringProperty getMaxTableViewPageLbl(){ return maxTableViewPageLbl; }
    public IntegerProperty getCurrentTableViewPage(){ return currentTableViewPage; }
    public Map<String,String> getTitleMapping() { return titleMapping; }

    public void headerMultiOptions(String optionName){
        switch(optionName){
            case "REFRESH":
                setUp();
                break;
            case "EXPORT EXCEL":
                handleExportExcelTableView();
                break;
            default:
                System.out.println("Option does not exist: " + optionName);
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
            boolean success = exportTable(currentTableView);
            if(success){
                SystemMessageUtils.setCurrSystemText("Excel file created successfully.");
                SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                SystemMessageUtils.messageAnimation();
            } else {
                SystemMessageUtils.setCurrSystemText("Error creating excel file.");
                SystemMessageUtils.setCurrPropertyColor("ERROR");
                SystemMessageUtils.messageAnimation();
            }
            setUp();
        } catch(IOException ex){
            System.out.println("Export to excel failed.");
            SystemMessageUtils.setCurrSystemText("Error occurred creating excel file.");
            SystemMessageUtils.setCurrPropertyColor("ERROR");
            SystemMessageUtils.messageAnimation();
            throw new RuntimeException(ex);
        }
    }

    public void setUp(){
        ProgressLoadUtils.showProgressLoad();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                collectionName = "Deliveries";
                TableViewUtils.setStoreCollectionName(collectionName);
                currentTableViewPage.set(1);
                QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
                if(snapshot != null){
                    // Using a custom DeliveryRecord wrapper class
                    entireDataCollection = TableViewUtils.prepareTableViewData(DeliveryRecord.class, collectionName, snapshot);
                    storedColumnClickableName = "deliveryID";
                }
                return null;
            }

            @Override
            protected void succeeded(){
                Platform.runLater(() -> {
                    if(entireDataCollection != null){
                        maxRecordCount = entireDataCollection.size();
                        maxPageCount = (maxRecordCount + initialSetRowDisplay - 1) / initialSetRowDisplay;
                        splitPageRowData = splitDataToPages(entireDataCollection);
                        tableViewData();
                    }
                    ProgressLoadUtils.hideProgressLoad();
                });
            }

            @Override
            protected void failed(){
                Platform.runLater(() -> {
                    System.out.println("Failed to load delivery data.");
                    ProgressLoadUtils.hideProgressLoad();
                });
            }
        };
        new Thread(task).start();
    }

    public Map<Integer, List<?>> splitDataToPages(List<?> data){
        Map<Integer, List<?>> pages = new HashMap<>();
        int pageNumber = 1;
        for (int i = 0; i < data.size(); i += initialSetRowDisplay){
            pages.put(pageNumber++, data.subList(i, Math.min(i + initialSetRowDisplay, data.size())));
        }
        return pages;
    }

    public void tableViewData(){
        if(currentTableView != null && splitPageRowData != null && collectionName != null){
            TableViewUtils.populateTableView(
                    "DEFAULT",
                    currentTableView,
                    splitPageRowData.get(currentTableViewPage.get()),
                    retrieveColumnTitles(collectionName),
                    storedColumnClickableName,
                    null
            );
        }
    }

    // A DeliveryRecord wrapper class for table view binding
    public static class DeliveryRecord {
        private final StringProperty deliveryID;
        private final StringProperty deliveryDate;
        private final StringProperty supplier;
        private final StringProperty deliveryAddress;

        public DeliveryRecord(String deliveryID, String deliveryDate, String supplier, String deliveryAddress){
            this.deliveryID = new SimpleStringProperty(deliveryID);
            this.deliveryDate = new SimpleStringProperty(deliveryDate);
            this.supplier = new SimpleStringProperty(supplier);
            this.deliveryAddress = new SimpleStringProperty(deliveryAddress);
        }

        public StringProperty deliveryIDProperty(){ return deliveryID; }
        public StringProperty deliveryDateProperty(){ return deliveryDate; }
        public StringProperty supplierProperty(){ return supplier; }
        public StringProperty deliveryAddressProperty(){ return deliveryAddress; }
    }
}
