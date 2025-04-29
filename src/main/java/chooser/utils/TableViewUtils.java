package chooser.utils;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import chooser.model.MenuItem;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TableViewUtils {

    private static final Map<String,HashMap<String,String>> entireColumnNames = new HashMap<>();
    private static final Map<String,HashMap<String,?>> entireDatabaseCollection = new HashMap<>();
    private static String selectedRowID;

    private static String storeCollectionName;

    static{
        selectedRowID = null;
        entireColumnNames.put("Employees",new HashMap<>());
        entireColumnNames.get("Employees").put("role", "Role");
        entireColumnNames.get("Employees").put("username", "Employee ID");
        entireColumnNames.get("Employees").put("fName", "First Name");
        entireColumnNames.get("Employees").put("lName", "Last Name");
        entireColumnNames.get("Employees").put("dob", "DOB (MM/DD/YYYY)");
        entireColumnNames.get("Employees").put("phoneNum", "Phone Number (XXX-XXX-XXXX)");
        entireColumnNames.get("Employees").put("password", "Password");


        entireColumnNames.put("Menu",new HashMap<>());
        entireColumnNames.get("Menu").put("id", "Menu Item ID");
        entireColumnNames.get("Menu").put("name", "Name");
        entireColumnNames.get("Menu").put("description", "Description");
        entireColumnNames.get("Menu").put("category", "Category");
        entireColumnNames.get("Menu").put("price", "Price");
        entireColumnNames.get("Menu").put("uom", "UOM");
        entireColumnNames.get("Menu").put("itemImage", "Item Image");
        entireColumnNames.get("Menu").put("ingredientsList", "Ingredients");

        entireColumnNames.put("InventoryV2",new HashMap<>());
        entireColumnNames.get("InventoryV2").put("inventoryId", "Inventory Id");
        entireColumnNames.get("InventoryV2").put("inventoryName", "Name");
        entireColumnNames.get("InventoryV2").put("category", "Category");
        entireColumnNames.get("InventoryV2").put("stockStatus", "Stock Status");
        entireColumnNames.get("InventoryV2").put("totalQuantity", "Total Quantity");
        entireColumnNames.get("InventoryV2").put("quantityUOM", "UOM");
        entireColumnNames.get("InventoryV2").put("minStock", "Min. Stock");
        entireColumnNames.get("InventoryV2").put("itemLimit", "Item Limit");
        //entireColumnNames.get("InventoryV2").put("itemPrice", "Price");
        //entireColumnNames.get("InventoryV2").put("priceUOM", "Currency UOM");
        entireColumnNames.get("InventoryV2").put("itemStockList", "Stock Items");

        entireColumnNames.put("OrderMenuDisplay",new HashMap<>());
        entireColumnNames.get("OrderMenuDisplay").put("ingredientName", "Name");
        entireColumnNames.get("OrderMenuDisplay").put("ingredientQuantity", "Quantity");
        entireColumnNames.get("OrderMenuDisplay").put("ingredientUOM", "UOM");
        entireColumnNames.get("OrderMenuDisplay").put("remainingUses", "Uses Left");
        entireColumnNames.get("OrderMenuDisplay").put("linkInventoryId", "Link Inv. Id");
        entireColumnNames.get("OrderMenuDisplay").put("stockStatus", "Stock Status");
        entireColumnNames.get("OrderMenuDisplay").put("stockQuantity", "Inv. Quantity");
        entireColumnNames.get("OrderMenuDisplay").put("stockUOM", "Inv. UOM");


        entireColumnNames.put("CustomerOrderHistory",new HashMap<>());
        entireColumnNames.get("CustomerOrderHistory").put("id", "Customer Order Id");
        entireColumnNames.get("CustomerOrderHistory").put("loggedDate", "Logged Date");
        entireColumnNames.get("CustomerOrderHistory").put("loggedTime", "Logged Time");
        entireColumnNames.get("CustomerOrderHistory").put("signedBy", "Signed by");
        entireColumnNames.get("CustomerOrderHistory").put("orderList", "Order List");

        entireColumnNames.put("OrderList",new HashMap<>());
        entireColumnNames.get("OrderList").put("menuItemId", "Menu Item Id");
        entireColumnNames.get("OrderList").put("menuItemName", "Menu Item Name");
        entireColumnNames.get("OrderList").put("tableNum", "Table Number");
        entireColumnNames.get("OrderList").put("quantity", "Serving Amount");
        entireColumnNames.get("OrderList").put("name", "Customer Name");
        entireColumnNames.get("OrderList").put("notes", "**Notes**");

    }

    public static void setStoreCollectionName(String value){storeCollectionName = value;}
    public static void setSelectedRowID(String value){selectedRowID = value;}

    public static String getSelectedRowID(){
        return selectedRowID;
    }

    public static String getStoredCollectionName(){
        return storeCollectionName;
    }

    public static void handleNewDocument(){
        System.out.println("handleNewDocument() called!");
        switch (storeCollectionName){
            case "Employees":
                SceneNavigator.loadView("New User");
                break;
            case "Menu":
                SceneNavigator.loadView("New Menu Item");
                break;
            case "InventoryV2":
                SceneNavigator.loadView("New Inventory");
                break;
            case "CustomerOrderHistory":
                SceneNavigator.loadView("Log Order");
                break;

                default:
                System.out.println("Collection doesnt exist for ADD NEW operations");
        }

    }

    public static void addCollectionToMap(String collectionName, List<?> data){

        HashMap<String, Object> newMap = new HashMap<>();
        for (Object obj : data) {
                String retrieveId = null;

                if (obj instanceof User) {
                    retrieveId = ((User) obj).getUsername();
                } else if(obj instanceof MenuItem) {
                    retrieveId = ((MenuItem) obj).getId();
                }else if(obj instanceof InventoryItem) {
                    retrieveId = ((InventoryItem) obj).getInventoryId();
                }else if(obj instanceof LoggedOrder) {
                    retrieveId = ((LoggedOrder) obj).getId();
                }else if(obj instanceof CustomerOrder) {
                    retrieveId = ((CustomerOrder) obj).getMenuItemId();
                }else{
                    System.out.println("Unknown object type: " + obj.getClass().getName());
                    continue;
                }

                if (retrieveId != null) {
                    newMap.put(retrieveId, obj);
                }
        }
        entireDatabaseCollection.put(collectionName, newMap);
    }

    public static Object retrieveDocumentData(String collectionName, String documentID){
        return entireDatabaseCollection.get(collectionName).get(documentID);
    }

    public static HashMap<String, String> retrieveColumnTitles(String collectionName){
        return entireColumnNames.get(collectionName);
    }




    public static <T> List<T> prepareTableViewData(Class<T> clazz, String collectionName, QuerySnapshot snapshot) {

        System.out.println("prepareTableViewData called");

        List<QueryDocumentSnapshot> documents = snapshot.getDocuments();
        List<T> tableData = new ArrayList<>();

        if (collectionName.equals("Employees") && clazz == User.class) {
            for (QueryDocumentSnapshot document : documents) {
                User formatUserData = FirestoreUtils.createUserFromDocument(document);
                tableData.add(clazz.cast(formatUserData));
            }
        }

        if (collectionName.equals("Menu") && clazz == MenuItem.class) {
            for (QueryDocumentSnapshot document : documents) {
                MenuItem formatMenuData = FirestoreUtils.createMenuItemFromDocument(document);
                tableData.add(clazz.cast(formatMenuData));
            }
        }

        if (collectionName.equals("InventoryV2") && clazz == InventoryItem.class) {
            System.out.println("Checking InventoryV2 if statement: ");
            for (QueryDocumentSnapshot document : documents) {
                InventoryItem formatInventoryData = FirestoreUtils.createInventoryItemFromDocument(document);
                //System.out.println("Checking formatInventoryData: "+formatInventoryData);
                tableData.add(clazz.cast(formatInventoryData));
            }
        }

        if (collectionName.equals("CustomerOrderHistory") && clazz == LoggedOrder.class) {
            for (QueryDocumentSnapshot document : documents) {
                LoggedOrder formatLoggedOrderData = FirestoreUtils.createLoggedOrderFromDocument(document);
                tableData.add(clazz.cast(formatLoggedOrderData));
            }
        }


        System.out.println("Checking tableData Size: "+tableData.size());

        return tableData;
    }

    public static <T> void populateTableView(String status,TableView<T> tableView, List<T> data, Map<String, String> columnNameMap, String clickableField, Consumer<T> onClickAction ) {

        System.out.println("populateTableView called");

        tableView.getColumns().clear();
        tableView.getItems().clear();

        if (data == null || data.isEmpty()) {
            return;
        }

        T firstItem = data.get(0);

        for (var field : firstItem.getClass().getDeclaredFields()) {
            String fieldName = field.getName();

            if (!columnNameMap.containsKey(fieldName)) {
                //System.out.println("Skipping column for field: " + fieldName);
                continue;
            }

            String columnTitle = columnNameMap.get(fieldName);


            if (fieldName.equals(clickableField) && status.equals("DEFAULT") && !CurrentPageOptions.getCurrPageOption().equals("New Menu Item")) {

                TableColumn<T, Hyperlink> hyperlinkColumn = new TableColumn<>(columnTitle);

                hyperlinkColumn.setCellValueFactory(cellData -> {
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(cellData.getValue());
                        if (fieldValue != null) {
                            Hyperlink hyperlink = new Hyperlink(fieldValue.toString());
                            hyperlink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                            hyperlink.setVisited(false);
                            hyperlink.setOnAction(event -> {
                                String hyperlinkValue = hyperlink.getText();
                                System.out.println("Clicked: " + hyperlink.getText());
                                TableViewUtils.setSelectedRowID(hyperlinkValue);

                                if(storeCollectionName.equals("CustomerOrderHistory")){
                                    ViewLoggedOrderUtils.setSelectedLoggedUser(hyperlink.getText());
                                    ViewLoggedOrderUtils.displayChangeViewLoggedOrder("SHOW");
                                }else{
                                    handleNewDocument();
                                }

                                hyperlink.setVisited(false);
                            });
                            return new SimpleObjectProperty<>(hyperlink);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return new SimpleObjectProperty<>(new Hyperlink("--"));
                });


                tableView.getColumns().add(hyperlinkColumn);

            }else{
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

                if (fieldName.equals("stockStatus")) {
                    column.setCellFactory(col -> new TableCell<>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                                setStyle("");
                            } else {
                                setText(item);
                                switch (item) {
                                    case "Out of Stock":
                                        setStyle("-fx-background-color: #FFCDD2;"); // red
                                        break;
                                    case "Low Stock":
                                        setStyle("-fx-background-color: #FFF9C4;"); // yellow
                                        break;
                                    case "In Stock":
                                        setStyle("-fx-background-color: #C8E6C9;"); // green
                                        break;
                                    default:
                                        setStyle("");
                                        break;
                                }
                            }
                        }
                    });
                }

                tableView.getColumns().add(column);
            }


        }

        ObservableList<T> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
}
