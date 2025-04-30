package chooser.utils;



import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.model.InventoryItem;
import chooser.model.MenuItem;
import chooser.model.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class TableViewUtils {

    private static final Map<String, HashMap<String, String>> entireColumnNames = new HashMap<>();
    private static final Map<String, HashMap<String, ?>> entireDatabaseCollection = new HashMap<>();
    private static String selectedRowID;
    private static String storeCollectionName;

    static {
        selectedRowID = null;

        entireColumnNames.put("Employees", new HashMap<>());
        entireColumnNames.get("Employees").put("role", "Role");
        entireColumnNames.get("Employees").put("username", "Employee ID");
        entireColumnNames.get("Employees").put("fName", "First Name");
        entireColumnNames.get("Employees").put("lName", "Last Name");
        entireColumnNames.get("Employees").put("dob", "DOB (MM/DD/YYYY)");
        entireColumnNames.get("Employees").put("phoneNum", "Phone Number (XXX-XXX-XXXX)");
        entireColumnNames.get("Employees").put("password", "Password");

        entireColumnNames.put("Menu", new HashMap<>());
        entireColumnNames.get("Menu").put("id", "Menu Item ID");
        entireColumnNames.get("Menu").put("name", "Name");
        entireColumnNames.get("Menu").put("description", "Description");
        entireColumnNames.get("Menu").put("category", "Category");
        entireColumnNames.get("Menu").put("price", "Price");
        entireColumnNames.get("Menu").put("uom", "UOM");
        entireColumnNames.get("Menu").put("itemImage", "Item Image");
        entireColumnNames.get("Menu").put("ingredientsList", "Ingredients");

        entireColumnNames.put("Inventory", new HashMap<>());
        entireColumnNames.get("Inventory").put("itemId", "Item ID");
        entireColumnNames.get("Inventory").put("itemName", "Item Name");
        entireColumnNames.get("Inventory").put("unit", "Unit");
        entireColumnNames.get("Inventory").put("category", "Category");
        entireColumnNames.get("Inventory").put("quantity", "Quantity");
        entireColumnNames.get("Inventory").put("pricePerUnit", "Price per Unit");
        entireColumnNames.get("Inventory").put("supplier", "Supplier");
        entireColumnNames.get("Inventory").put("stockStatus", "Stock Status");
        entireColumnNames.get("Inventory").put("status", "Expiration Status");

        entireColumnNames.put("inventoryDeliveries", new HashMap<>());
        entireColumnNames.get("inventoryDeliveries").put("supplier", "Supplier");
        entireColumnNames.get("inventoryDeliveries").put("deliveryDate", "Delivery Date");
        entireColumnNames.get("inventoryDeliveries").put("itemId", "Item ID");
        entireColumnNames.get("inventoryDeliveries").put("itemName", "Item Name");
        entireColumnNames.get("inventoryDeliveries").put("quantity", "Quantity");
        entireColumnNames.get("inventoryDeliveries").put("pricePerUnit", "Price Per Unit");
        entireColumnNames.get("inventoryDeliveries").put("expirationDate", "Expiration Date");
    }

    public static void setStoreCollectionName(String value) { storeCollectionName = value; }
    public static void setSelectedRowID(String value) { selectedRowID = value; }
    public static String getSelectedRowID() { return selectedRowID; }
    public static String getStoredCollectionName() { return storeCollectionName; }

    public static void handleNewDocument() {
        System.out.println("handleNewDocument() called!");
        switch (storeCollectionName) {
            case "Employees" -> SceneNavigator.loadView("New User");
            case "Menu" -> SceneNavigator.loadView("New Menu Item");
            case "Inventory" -> SceneNavigator.loadView("Add Inventory Item");
            default -> System.out.println("Collection doesn't exist for ADD NEW operations");
        }
    }

    public static void addCollectionToMap(String collectionName, List<?> data) {
        HashMap<String, Object> newMap = new HashMap<>();
        for (Object obj : data) {
            String retrieveId = null;
            if (obj instanceof User) retrieveId = ((User) obj).getUsername();
            else if (obj instanceof MenuItem) retrieveId = ((MenuItem) obj).getId();
            else if (obj instanceof InventoryItem) retrieveId = ((InventoryItem) obj).getItemId();
            else System.out.println("Unknown object type: " + obj.getClass().getName());

            if (retrieveId != null) newMap.put(retrieveId, obj);
        }
        entireDatabaseCollection.put(collectionName, newMap);
        System.out.println("Checking Size of entireDatabaseCollection: " + entireDatabaseCollection.size());
    }

    public static Object retrieveDocumentData(String collectionName, String documentID) {
        return entireDatabaseCollection.get(collectionName).get(documentID);
    }

    public static HashMap<String, String> retrieveColumnTitles(String collectionName) {
        return entireColumnNames.get(collectionName);
    }

    public static <T> List<T> prepareTableViewData(Class<T> clazz, String collectionName, QuerySnapshot snapshot) {
        List<T> tableData = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
            if (collectionName.equals("Employees") && clazz == User.class)
                tableData.add(clazz.cast(FirestoreUtils.createUserFromDocument(document)));
            else if (collectionName.equals("Menu") && clazz == MenuItem.class)
                tableData.add(clazz.cast(FirestoreUtils.createMenuItemFromDocument(document)));
            else if (collectionName.equals("Inventory") && clazz == InventoryItem.class)
                tableData.add(clazz.cast(FirestoreUtils.createInvItemFromDocument(document)));
            else if (collectionName.equals("inventoryDeliveries") && clazz == InventoryDelivery.class)
                tableData.add(clazz.cast(FirestoreUtils.createDeliveryFromDocument(document)));
        }
        return tableData;
    }

    public static <T> void populateTableView(String status, TableView<T> tableView, List<T> data, Map<String, String> columnNameMap, String clickableField, Consumer<T> onClickAction) {
        tableView.getColumns().clear();
        tableView.getItems().clear();
        if (data == null || data.isEmpty()) return;

        T firstItem = data.get(0);
        List<InventoryDelivery> allDeliveries = FirestoreUtils.readDeliveriesCollection();

        Map<String, LocalDate> latestExpirations = new HashMap<>();
        for (InventoryDelivery delivery : allDeliveries) {
            latestExpirations.merge(delivery.getItemId(), delivery.getExpDate(), (oldDate, newDate) -> oldDate.isAfter(newDate) ? oldDate : newDate);
        }

        if (columnNameMap.containsKey("stockStatus")) {
            TableColumn<T, String> stockStatusCol = new TableColumn<>(columnNameMap.get("stockStatus"));
            stockStatusCol.setCellValueFactory(cellData -> {
                try {
                    Method method = cellData.getValue().getClass().getMethod("getStockStatus");
                    return new SimpleStringProperty(method.invoke(cellData.getValue()).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("--");
                }
            });
            stockStatusCol.setCellFactory(col -> new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else {
                        setText(item);
                        setStyle(switch (item.toLowerCase()) {
                            case "out of stock" -> "-fx-text-fill: red; -fx-font-weight: bold;";
                            case "low stock" -> "-fx-text-fill: orange; -fx-font-weight: bold;";
                            default -> "-fx-text-fill: black;";
                        });
                    }
                }
            });
            tableView.getColumns().add(stockStatusCol);
        }

        if (columnNameMap.containsKey("status")) {
            TableColumn<T, String> expStatusCol = new TableColumn<>(columnNameMap.get("status"));
            expStatusCol.setCellValueFactory(cellData -> {
                try {
                    Method method = cellData.getValue().getClass().getMethod("getItemId");
                    String itemId = (String) method.invoke(cellData.getValue());
                    if (itemId != null && latestExpirations.containsKey(itemId)) {
                        LocalDate expDate = latestExpirations.get(itemId);
                        LocalDate today = LocalDate.now();
                        if (expDate.isBefore(today) || expDate.equals(today)) return new SimpleStringProperty("Expired, Discard");
                        else if (today.until(expDate).getDays() <= 4) return new SimpleStringProperty("Expiration Approaching, Run Special");
                        else return new SimpleStringProperty("Good");
                    }
                    return new SimpleStringProperty("--");
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("--");
                }
            });
            expStatusCol.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        switch (item.toLowerCase()) {
                            case "expired, discard" -> setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            case "expiration approaching, run special" -> setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            default -> setStyle("-fx-text-fill: black;");
                        }
                    }
                }
            });

            tableView.getColumns().add(expStatusCol);

        }

        for (var field : firstItem.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            if (!columnNameMap.containsKey(fieldName)) continue;
            String columnTitle = columnNameMap.get(fieldName);
            if (fieldName.equals(clickableField) && status.equals("DEFAULT")) {
                TableColumn<T, Hyperlink> hyperlinkCol = new TableColumn<>(columnTitle);
                hyperlinkCol.setCellValueFactory(cellData -> {
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(cellData.getValue());
                        if (fieldValue != null) {
                            Hyperlink hyperlink = new Hyperlink(fieldValue.toString());
                            hyperlink.setOnAction(event -> {
                                setSelectedRowID(hyperlink.getText());
                                SceneNavigator.setSelectedItemId(hyperlink.getText());
                                if ("Inventory".equals(storeCollectionName)) SceneNavigator.loadView("Edit Item");
                                else handleNewDocument();
                            });
                            return new SimpleObjectProperty<>(hyperlink);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new SimpleObjectProperty<>(new Hyperlink("--"));
                });
                tableView.getColumns().add(hyperlinkCol);
            } else {
                TableColumn<T, String> column = new TableColumn<>(columnTitle);
                column.setCellValueFactory(cellData -> {
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(cellData.getValue());
                        return new SimpleStringProperty(fieldValue != null ? fieldValue.toString() : "--");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return new SimpleStringProperty("");
                    }
                });
                tableView.getColumns().add(column);
            }
        }

        ObservableList<T> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
