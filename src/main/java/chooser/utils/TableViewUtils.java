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

        entireColumnNames.put("SuppliersV2",new HashMap<>());
        entireColumnNames.get("SuppliersV2").put("supplierId", "Supplier Id");
        entireColumnNames.get("SuppliersV2").put("supplierName", "Supplier Name");
        entireColumnNames.get("SuppliersV2").put("description", "Description");
        entireColumnNames.get("SuppliersV2").put("contactInfo", "Primary Contact Info");
        entireColumnNames.get("SuppliersV2").put("businessAddressInfo", "Business Address");
        entireColumnNames.get("SuppliersV2").put("warehouseAddressInfo", "Warehouse Address");

        entireColumnNames.put("PurchaseOrders",new HashMap<>());
        entireColumnNames.get("PurchaseOrders").put("id", "Purchase Order Id");
        entireColumnNames.get("PurchaseOrders").put("orderStatus", "Order Status");
        entireColumnNames.get("PurchaseOrders").put("estDelivery", "Est. Delivery");
        entireColumnNames.get("PurchaseOrders").put("orderDate", "Order Date");
        entireColumnNames.get("PurchaseOrders").put("signedBy", "Signed By");
        entireColumnNames.get("PurchaseOrders").put("supplierId", "Supplier Id");
        entireColumnNames.get("PurchaseOrders").put("supplierName", "Supplier Name");
        entireColumnNames.get("PurchaseOrders").put("primaryContact", "Primary Contact");
        entireColumnNames.get("PurchaseOrders").put("addressInfo", "Warehouse Address");
        entireColumnNames.get("PurchaseOrders").put("requestItemList", "Requested Items");

        entireColumnNames.put("ReceivedItems",new HashMap<>());
        entireColumnNames.get("ReceivedItems").put("id", "Received Items Id");
        entireColumnNames.get("ReceivedItems").put("deliveredDate", "Received Date");
        entireColumnNames.get("ReceivedItems").put("deliveredTime", "Received Time");
        entireColumnNames.get("ReceivedItems").put("receivedBy", "Received By");
        entireColumnNames.get("ReceivedItems").put("purchaseOrderId", "Purchase Order Id");
        entireColumnNames.get("ReceivedItems").put("supplierId", "Supplier Id");
        entireColumnNames.get("ReceivedItems").put("supplierName", "Supplier Name");
        entireColumnNames.get("ReceivedItems").put("primaryContact", "Primary Contact");
        entireColumnNames.get("ReceivedItems").put("addressInfo", "Warehouse Address");
        entireColumnNames.get("ReceivedItems").put("requestItemList", "Requested Items");

        entireColumnNames.put("RequestItem",new HashMap<>());
        entireColumnNames.get("RequestItem").put("inventoryId", "Item Id");
        entireColumnNames.get("RequestItem").put("inventoryName", "Item Name");
        entireColumnNames.get("RequestItem").put("quantity", "Quantity");
        entireColumnNames.get("RequestItem").put("uom", "UOM");
        entireColumnNames.get("RequestItem").put("notes", "**Notes**");

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
            case "SuppliersV2":
                SceneNavigator.loadView("New Supplier");
                break;
            case "PurchaseOrders":
                SceneNavigator.loadView("New Purchase Order");
                break;
            case "CustomerOrderHistory":
                SceneNavigator.loadView("Log Order");
                break;
            case "ReceivedItems":
                SceneNavigator.loadView("New Received Items");
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
                }else if(obj instanceof SupplierInfo) {
                    retrieveId = ((SupplierInfo) obj).getSupplierId();
                }else if(obj instanceof PurchaseOrder) {
                    retrieveId = ((PurchaseOrder) obj).getId();
                }else if(obj instanceof RequestItem) {
                    retrieveId = ((RequestItem) obj).getInventoryId();
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
                if(!formatUserData.getUsername().equals("admin0")){
                    tableData.add(clazz.cast(formatUserData));
                }
            }
        }

        if (collectionName.equals("Menu") && clazz == MenuItem.class) {
            for (QueryDocumentSnapshot document : documents) {
                MenuItem formatMenuData = FirestoreUtils.createMenuItemFromDocument(document);
                tableData.add(clazz.cast(formatMenuData));
            }
        }

        if (collectionName.equals("InventoryV2") && clazz == InventoryItem.class) {
            for (QueryDocumentSnapshot document : documents) {
                InventoryItem formatInventoryData = FirestoreUtils.createInventoryItemFromDocument(document);
                if(CurrentPageOptions.getCurrPageOption().equals("New Purchase Order")){
                    if(formatInventoryData.getStockStatus().equals("In Stock")){
                        continue;
                    }
                }
                tableData.add(clazz.cast(formatInventoryData));
            }
        }

        if (collectionName.equals("SuppliersV2") && clazz == SupplierInfo.class) {
            for (QueryDocumentSnapshot document : documents) {
                SupplierInfo formatSupplierData = FirestoreUtils.createSupplierFromDocument(document);
                tableData.add(clazz.cast(formatSupplierData));
            }
        }

        if (collectionName.equals("PurchaseOrders") && clazz == PurchaseOrder.class) {
            for (QueryDocumentSnapshot document : documents) {
                PurchaseOrder formatPOData = FirestoreUtils.createPurchaseOrderFromDocument(document);
                if(CurrentPageOptions.getCurrPageOption().equals("New Received Items")){
                    if(formatPOData.getOrderStatus().equals("Delivered")){
                        continue;
                    }
                }
                tableData.add(clazz.cast(formatPOData));
            }
        }
        if (collectionName.equals("ReceivedItems") && clazz == ReceiveItems.class) {
            for (QueryDocumentSnapshot document : documents) {
                ReceiveItems formatPOData = FirestoreUtils.createReceivedItemsFromDocument(document);
                tableData.add(clazz.cast(formatPOData));
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


            if (fieldName.equals(clickableField) && status.equals("DEFAULT")
                    && !CurrentPageOptions.getCurrPageOption().equals("New Menu Item")
                    && !CurrentPageOptions.getCurrPageOption().equals("New Purchase Order")
                    && !CurrentPageOptions.getCurrPageOption().equals("New Received Items")) {

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

                                T findObject = null;
                                for(T item: data){
                                    if(item instanceof PurchaseOrder poItem){
                                        if(poItem.getId().equals(hyperlink.getText())){
                                            findObject = item;
                                            break;
                                        }
                                    }

                                    if(item instanceof ReceiveItems poItem){
                                        if(poItem.getId().equals(hyperlink.getText())){
                                            findObject = item;
                                            break;
                                        }
                                    }
                                }


                                if(storeCollectionName.equals("CustomerOrderHistory")){
                                    ViewLoggedOrderUtils.setSelectedLoggedUser(hyperlink.getText());
                                    ViewLoggedOrderUtils.displayChangeViewLoggedOrder("SHOW");
                                } else if (storeCollectionName.equals("PurchaseOrders") && findObject!=null) {
                                    PurchaseOrderUtils.setSelectedPurchaseOrder((PurchaseOrder) findObject);
                                    PurchaseOrderUtils.displayChangeViewPurchaseOrder("SHOW");
                                } else if (storeCollectionName.equals("ReceivedItems") && findObject!=null) {
                                    PurchaseOrderUtils.setSelectedReceivedItem((ReceiveItems) findObject);
                                    PurchaseOrderUtils.displayChangeViewReceivedItems("SHOW");
                                } else{
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

                if (fieldName.equals("orderStatus")) {
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
                                    case "In Progress":
                                        setStyle("-fx-background-color: #FFF9C4;"); // yellow
                                        break;
                                    case "Delivered":
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
