package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.CurrentPageOptions;
import chooser.model.InventoryItem;
import chooser.model.MenuItem;
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
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static chooser.utils.ExcelExporter.exportTable;
import static chooser.utils.TableViewUtils.retrieveColumnTitles;

public class TableViewViewModel {

    private TableView currentTableView;
    private List<?> entireDataCollection;
    private Map<Integer, List<?>> splitPageRowData = new HashMap<>();

    private ObservableList<?> currTableData;

    private String storedColumnClickableName;
    private int maxRecordCount;
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

    public TableViewViewModel() {
        initialSetRowDisplay = 12;
        currentTableViewPage.set(1);
        selectedRowID = "";
        collectionName = "";
        entireDataCollection = null;

        titleMapping.put("View Accounts", "Accounts");
        titleMapping.put("View Menu Items", "Menu");
        titleMapping.put("View Inventory", "Inventory");
        titleMapping.put("Inventory Delivery Report", "Deliveries");
    }

    public void setCurrTableView(TableView tableView) {
        currentTableView = tableView;
    }

    public BooleanProperty getLeftArrowVisible() {
        return leftArrowVisible;
    }

    public BooleanProperty getRightArrowVisible() {
        return rightArrowVisible;
    }

    public BooleanProperty getLeftArrowBtnDisable() {
        return leftArrowBtnDisable;
    }

    public BooleanProperty getRightArrowBtnDisable() {
        return rightArrowBtnDisable;
    }

    public StringProperty getRecordsAmountLbl() {
        return recordsAmountLbl;
    }

    public StringProperty getMaxTableViewPageLbl() {
        return maxTableViewPageLbl;
    }

    public IntegerProperty getCurrentTableViewPage() {
        return currentTableViewPage;
    }

    public Map<String, String> getTitleMapping() {
        return titleMapping;
    }

    private void handleDeleteDocument() {
        if (selectedRowID != null && collectionName != null) {
            boolean checkHasDeleted = FirestoreUtils.deleteDoc(collectionName, selectedRowID);
            if (checkHasDeleted) {
                SystemMessageUtils.setCurrSystemText(selectedRowID + " has been successfully delete.");
                SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                SystemMessageUtils.messageAnimation();
                setUp();
            } else {
                System.out.println("DOCUMENT DELETION FAILED. SOMETHING WENT WRONG!!");
                SystemMessageUtils.setCurrSystemText("An error occurred when deleting account for user: " + selectedRowID);
                SystemMessageUtils.setCurrPropertyColor("ERROR");
                SystemMessageUtils.messageAnimation();
            }
        }
    }

    private void handleExportExcelTableView() {
        try {
            TableViewUtils.populateTableView(
                    "EXPORT",
                    currentTableView,
                    entireDataCollection,
                    TableViewUtils.retrieveColumnTitles(collectionName),
                    storedColumnClickableName,
                    null
            );
            boolean hasExportSuccess = exportTable(currentTableView);
            if (hasExportSuccess) {
                System.out.println("Excel file saved successfully.");
                SystemMessageUtils.setCurrSystemText("Excel file created successfully.");
                SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                SystemMessageUtils.messageAnimation();
            } else {
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

    public void headerMultiOptions(String optionName) {
        switch (optionName) {
            case "DELETE" -> handleDeleteDocument();
            case "ADD NEW" -> TableViewUtils.handleNewDocument();
            case "REFRESH" -> setUp();
            case "EXPORT EXCEL" -> handleExportExcelTableView();
            default -> {
                System.out.println("This option doesnt exist in table view: " + optionName);
                if (optionName.equals("ADD NEW")) {
                    if ("Inventory".equals(CurrentPageOptions.getCurrPageOption())) {
                        SceneNavigator.switchScene("AddNewItemPage", "TrackBite/AddNewItemPage", -1, -1, true);
                    } else {
                        System.out.println("ADD NEW is not supported for: " + CurrentPageOptions.getCurrPageOption());
                    }
                }
            }
        }
    }

    public void storeRowSelectedID(Object value) {
        if (!collectionName.isEmpty()) {
            switch (collectionName) {
                case "Employees" -> {
                    if (value instanceof User selectedUser) {
                        selectedRowID = selectedUser.getUsername();
                    }
                }
                case "Menu" -> {
                    if (value instanceof MenuItem selectedMenuItem) {
                        selectedRowID = selectedMenuItem.getId();
                    }
                }
                case "Inventory" -> {
                    if (value instanceof InventoryItem selectedUser) {
                        selectedRowID = selectedUser.getItemId();
                    }
                }
                default -> System.out.println("Collection doesn't exist to store row ID");
            }
        }
    }

    public void checkArrowDisable() {
        leftArrowBtnDisable.set(currentTableViewPage.get() <= 1);
        leftArrowVisible.set(currentTableViewPage.get() > 1);

        rightArrowBtnDisable.set(currentTableViewPage.get() >= maxPageCount);
        rightArrowVisible.set(currentTableViewPage.get() < maxPageCount);
    }

    public void adjustPageDirection(String direction) {
        switch (direction) {
            case "left" -> {
                if (!leftArrowBtnDisable.get()) {
                    currentTableViewPage.set(currentTableViewPage.get() - 1);
                    tableViewData();
                }
            }
            case "right" -> {
                if (!rightArrowBtnDisable.get()) {
                    currentTableViewPage.set(currentTableViewPage.get() + 1);
                    tableViewData();
                }
            }
            default -> System.out.println("Page Direction doesn't exist for table view: " + direction);
        }
        checkArrowDisable();
    }

    public <T> Map<Integer, List<?>> splitDataToPages(List<T> data) {
        Map<Integer, List<?>> pages = new HashMap<>();
        int pageNumber = 1;
        for (int i = 0; i < data.size(); i += initialSetRowDisplay) {
            pages.put(pageNumber++, data.subList(i, Math.min(i + initialSetRowDisplay, data.size())));
        }
        return pages;
    }

    public void tableViewData() {
        if (currentTableView != null && splitPageRowData != null && storedColumnClickableName != null && collectionName != null) {
            TableViewUtils.populateTableView(
                    "DEFAULT",
                    currentTableView,
                    splitPageRowData.get(currentTableViewPage.get()),
                    TableViewUtils.retrieveColumnTitles(collectionName),
                    storedColumnClickableName,
                    data -> System.out.println("Clicked on: " + data.getClass().getName())
            );

            int minRecordNumDisplayed = (currentTableViewPage.get() - 1) * initialSetRowDisplay + 1;
            int currentPageSize = splitPageRowData.get(currentTableViewPage.get()).size();
            int maxRecordNumDisplayed = Math.min(minRecordNumDisplayed + currentPageSize - 1, maxRecordCount);

            recordsAmountLbl.set(minRecordNumDisplayed + "-" + maxRecordNumDisplayed + " of " + maxRecordCount + " records");
            maxTableViewPageLbl.set("of " + splitPageRowData.size() + " pages");
        }
    }

    public void userPageNum(int value) {
        if (value >= maxPageCount) currentTableViewPage.set(maxPageCount);
        else if (value <= 1) currentTableViewPage.set(1);
        else currentTableViewPage.set(value);

        tableViewData();
        checkArrowDisable();
    }

    public void setUp() {
        ProgressLoadUtils.showProgressLoad();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String pageOption = CurrentPageOptions.getCurrPageOption();
                switch (pageOption) {
                    case "View Accounts" -> collectionName = "Employees";
                    case "View Menu Items" -> collectionName = "Menu";
                    case "View Inventory" -> collectionName = "Inventory";
                    case "Inventory Delivery Report" -> collectionName = "inventoryDeliveries";
                    default -> System.out.println("Collection Name doesn't exist for Table View");
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
                        switch (collectionName) {
                            case "Employees" -> {
                                entireDataCollection = TableViewUtils.prepareTableViewData(User.class, collectionName, snapshot);
                                storedColumnClickableName = "username";
                            }
                            case "Inventory" -> {
                                entireDataCollection = TableViewUtils.prepareTableViewData(InventoryItem.class, collectionName, snapshot);
                                storedColumnClickableName = "itemId";
                            }
                            case "Menu" -> {
                                entireDataCollection = TableViewUtils.prepareTableViewData(MenuItem.class, collectionName, snapshot);
                                storedColumnClickableName = "id";
                            }
                            default -> System.out.println("Collection Doesn't Exist. Table View Failed.");
                        }
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    if (entireDataCollection != null) {
                        maxRecordCount = entireDataCollection.size();
                        maxPageCount = (maxRecordCount + initialSetRowDisplay - 1) / initialSetRowDisplay;
                        splitPageRowData = splitDataToPages(entireDataCollection);
                        tableViewData();
                        currTableData = FXCollections.observableArrayList(entireDataCollection);
                        currentTableView.setItems(currTableData);
                        checkArrowDisable();
                    }
                    TableViewUtils.addCollectionToMap(collectionName, entireDataCollection);
                    ProgressLoadUtils.hideProgressLoad();
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    System.out.println("Failed to load data.");
                    ProgressLoadUtils.hideProgressLoad();
                });
            }
        };
        new Thread(task).start();
    }

    public void attachSearchBar(TextField searchField) {
        if (entireDataCollection == null || entireDataCollection.isEmpty()) return;

        if (entireDataCollection.get(0) instanceof InventoryItem) {
            ObservableList<InventoryItem> observableData = FXCollections.observableArrayList(
                    entireDataCollection.stream().map(obj -> (InventoryItem) obj).toList()
            );

            FilteredList<InventoryItem> filtered = new FilteredList<>(observableData, p -> true);

            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                String filter = newVal.toLowerCase();
                filtered.setPredicate(item ->
                        item.getItemId().toLowerCase().contains(filter) ||
                                item.getItemName().toLowerCase().contains(filter) ||
                                item.getCategory().toLowerCase().contains(filter)
                );
            });

            currentTableView.setItems(filtered);
        }
    }
}
