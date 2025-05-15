package chooser.utils;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderUtils {

    private static VBox storeInventoryItemSelectView;
    private static AnchorPane storeRoot;

    private static VBox storedViewPurchaseOrder;
    private static PurchaseOrder selectedPurchaseOrder;

    private static ReceiveItems selectedReceivedItem;
    private static AnchorPane storeRootPurchaseOrder;

    public static void setstoreRootPurchaseOrder(AnchorPane value) {
        storeRootPurchaseOrder = value;
    }

    public static void setSelectedPurchaseOrder(PurchaseOrder value) {
        selectedPurchaseOrder = value;
    }

    public static PurchaseOrder getSelectedPurchaseOrder() {
        return selectedPurchaseOrder;
    }

    public static void setSelectedReceivedItem(ReceiveItems value) {
        selectedReceivedItem = value;
    }

    public static ReceiveItems getSelectedReceivedItem() {
        return selectedReceivedItem;
    }

    private final static StringProperty purcahseOrderId = new SimpleStringProperty("");

    private final static StringProperty trackUsage = new SimpleStringProperty("");
    private final static StringProperty supplerId = new SimpleStringProperty("");
    private final static BooleanProperty validSupplierId = new SimpleBooleanProperty(false);

    private final static StringProperty itemInventoryId = new SimpleStringProperty("");
    private final static StringProperty itemName = new SimpleStringProperty("");
    private final static StringProperty itemUOM = new SimpleStringProperty("");
    private final static BooleanProperty validInventoryId = new SimpleBooleanProperty(false);


    private final static StringProperty supplierName = new SimpleStringProperty("");
    private final static StringProperty prmcFirstName = new SimpleStringProperty("");
    private final static StringProperty prmcLastName = new SimpleStringProperty("");

    private final static StringProperty prmcPhoneNumber = new SimpleStringProperty("");
    private final static StringProperty prmcEmail = new SimpleStringProperty("");
    private final static StringProperty prmcWebsite = new SimpleStringProperty("");

    private final static StringProperty warAddressLine = new SimpleStringProperty("");
    private final static StringProperty warCity = new SimpleStringProperty("");
    private final static StringProperty warState = new SimpleStringProperty("");
    private final static StringProperty warPostalCode = new SimpleStringProperty("");
    private final static StringProperty warCountry = new SimpleStringProperty("");


    public static StringProperty purchaseOrderIdProp() {
        return purcahseOrderId;
    }

    public static StringProperty trackUsageProp() {
        return trackUsage;
    }

    public static StringProperty supplerIdProp() {
        return supplerId;
    }

    public static BooleanProperty validSupplierIdProp() {
        return validSupplierId;
    }

    public static StringProperty itemInventoryIdProp() {
        return itemInventoryId;
    }

    public static StringProperty itemNameProp() {
        return itemName;
    }

    public static StringProperty itemUOMProp() {
        return itemUOM;
    }

    public static BooleanProperty validInventoryIdProp() {
        return validInventoryId;
    }


    public static StringProperty supplierNameProp() {
        return supplierName;
    }

    public static StringProperty prmcFirstNameProp() {
        return prmcFirstName;
    }

    public static StringProperty prmcLastNameProp() {
        return prmcLastName;
    }

    public static StringProperty prmcPhoneNumberProp() {
        return prmcPhoneNumber;
    }

    public static StringProperty prmcEmailProp() {
        return prmcEmail;
    }

    public static StringProperty prmcWebsiteProp() {
        return prmcWebsite;
    }

    public static StringProperty warAddressLineProp() {
        return warAddressLine;
    }

    public static StringProperty warCityProp() {
        return warCity;
    }

    public static StringProperty warStateProp() {
        return warState;
    }

    public static StringProperty warPostalCodeProp() {
        return warPostalCode;
    }

    public static StringProperty warCountryProp() {
        return warCountry;
    }


    //For Received Items For
    private static final BooleanProperty triggerPopulateTable = new SimpleBooleanProperty(false);
    private static List<RequestItem> storeRequestItemsList;

    public static BooleanProperty triggerPopulateTableProp() {
        return triggerPopulateTable;
    }

    public static List<RequestItem> getStoreRequestItemList() {
        return storeRequestItemsList;
    }

    public static void setStoreRequestItemList(List<RequestItem> value) {
        storeRequestItemsList = value;
    }


    static {
        storeRequestItemsList = null;

        supplerId.addListener((obs, oldVal, newVal) -> {
            validSupplierId.set(!newVal.equals("Search Supplier"));
        });

        itemInventoryId.addListener((obs, oldVal, newVal) -> {
            validInventoryId.set(!newVal.equals("Search Inventory"));
        });

    }

    public static boolean isValidTime(String time) {
        return time != null && time.matches("^(0[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)?$|^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }


    public static void searchSystemData(String status, String dataStatus, AnchorPane formRoot) {
        switch (status) {
            case "SHOW":
                try {
                    storeRoot = formRoot;
                    trackUsage.set(dataStatus);

                    String fxmlPathFull = "/chooser/trackbite/InventoryItemSelect.fxml";
                    FXMLLoader loader = new FXMLLoader(PurchaseOrderUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storeInventoryItemSelectView = view;
                    formRoot.getChildren().add(view);

                    AnchorPane.setTopAnchor(view, 0.0);
                    AnchorPane.setRightAnchor(view, 0.0);
                    AnchorPane.setBottomAnchor(view, 0.0);
                    AnchorPane.setLeftAnchor(view, 0.0);

                    view.setVisible(true);
                    view.setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "HIDE":
                if (storeInventoryItemSelectView != null && storeRoot != null) {
                    storeRoot.getChildren().remove(storeInventoryItemSelectView);
                    storeInventoryItemSelectView = null;
                }
                break;
        }

    }


    public static void displayChangeViewPurchaseOrder(String status) {
        switch (status) {
            case "SHOW":
                try {
                    String fxmlPathFull = "/chooser/trackbite/ViewPurchaseOrder.fxml";
                    FXMLLoader loader = new FXMLLoader(PurchaseOrderUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storedViewPurchaseOrder = view;
                    storeRootPurchaseOrder.getChildren().add(view);

                    AnchorPane.setTopAnchor(view, 0.0);
                    AnchorPane.setRightAnchor(view, 0.0);
                    AnchorPane.setBottomAnchor(view, 0.0);
                    AnchorPane.setLeftAnchor(view, 0.0);

                    view.setVisible(true);
                    view.setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "HIDE":
                if (storedViewPurchaseOrder != null && storeRootPurchaseOrder != null) {
                    storeRootPurchaseOrder.getChildren().remove(storedViewPurchaseOrder);
                    storedViewPurchaseOrder = null;
                }
                break;
        }
    }

    public static void displayChangeViewReceivedItems(String status) {
        switch (status) {
            case "SHOW":
                try {
                    String fxmlPathFull = "/chooser/trackbite/ViewReceiveItems.fxml";
                    FXMLLoader loader = new FXMLLoader(PurchaseOrderUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storedViewPurchaseOrder = view;
                    storeRootPurchaseOrder.getChildren().add(view);

                    AnchorPane.setTopAnchor(view, 0.0);
                    AnchorPane.setRightAnchor(view, 0.0);
                    AnchorPane.setBottomAnchor(view, 0.0);
                    AnchorPane.setLeftAnchor(view, 0.0);

                    view.setVisible(true);
                    view.setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "HIDE":
                if (storedViewPurchaseOrder != null && storeRootPurchaseOrder != null) {
                    storeRootPurchaseOrder.getChildren().remove(storedViewPurchaseOrder);
                    storedViewPurchaseOrder = null;
                }
                break;
        }
    }

    public static void updatePurchaseOrderStatus(String selectedId){
            String collectionName = "PurchaseOrders";
             QuerySnapshot snapshot = FirestoreUtils.getAllDocuments(collectionName);
              if (snapshot != null) {
                  Map<String,PurchaseOrder> purchaseOrderListMap = new HashMap<>();
                  for (QueryDocumentSnapshot document : snapshot) {
                         PurchaseOrder formatData = FirestoreUtils.createPurchaseOrderFromDocument(document);
                         purchaseOrderListMap.put(formatData.getId(),formatData);
                  }

                  if(purchaseOrderListMap.containsKey(selectedId)){
                      PurchaseOrder po = purchaseOrderListMap.get(selectedId);
                      po.setOrderStatus("Delivered");

                      Map<String,Object> formatData = setupFirebaseData(po);
                      FirestoreUtils.writeDoc("PurchaseOrders", po.getId(), formatData);
                  }


              }
    }

    public static void handleUpdateInventoryStock(List<RequestItem> itemList) {
        Map<String, InventoryItem> inventoryItemMap = MenuItemSelectUtils.setUpInventoryListMap();
        if (inventoryItemMap != null && !inventoryItemMap.isEmpty()) {
            for (RequestItem item : itemList) {
                String invId = item.getInventoryId();
                if (inventoryItemMap.containsKey(invId)) {
                    InventoryItem invItem = inventoryItemMap.get(invId);
                    List<ItemStock> itemStockList = invItem.getItemStockList();
                    int counter = invItem.getInventoryItemCount() + 1;
                    String createItemStockId = getFirstThreeChars(invItem.getInventoryName())+counter;
                    itemStockList.add(new ItemStock(
                            createItemStockId,
                            Integer.parseInt(item.getQuantity()),
                            getTwoWeeksFromNow()
                    ));

                    int newTotal = 0;
                    for(ItemStock itemStock: itemStockList){
                        newTotal += itemStock.getItemAmount();
                    }

                    invItem.setTotalQuantity(newTotal);
                    invItem.setStockStatus(NewInventoryItemUtils.calculateStockStatus(
                            String.valueOf(invItem.getMinStock()),
                            String.valueOf(newTotal)
                    ));
                    invItem.setInventoryItemCount(counter);
                    invItem.setItemStockList(itemStockList);

                    inventoryItemMap.put(invId, invItem);

                    MenuItemSelectUtils.formatAndUpdateInventoryItems(invItem);
                }
            }
        }
    }

    public static String getTwoWeeksFromNow() {
        LocalDate twoWeeksLater = LocalDate.now().plusWeeks(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return twoWeeksLater.format(formatter);
    }

    public static String getFirstThreeChars(String input) {
        return input != null && input.length() >= 3
                ? input.substring(0, 3).toLowerCase()
                : input != null ? input.toLowerCase() : null;
    }

    public static Map<String, Object> setupFirebaseData(PurchaseOrder purchaseOrder) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", purchaseOrder.getId());
        data.put("orderStatus", purchaseOrder.getOrderStatus());
        data.put("estDelivery", purchaseOrder.getEstDelivery());
        data.put("orderDate", purchaseOrder.getOrderDate());
        data.put("signedBy", purchaseOrder.getSignedBy());
        data.put("supplierId", purchaseOrder.getSupplierId());
        data.put("supplierName", purchaseOrder.getSupplierName());

        data.put("primaryContactFirstName", purchaseOrder.getPrimaryContact().getFirstName());
        data.put("primaryContactLastName", purchaseOrder.getPrimaryContact().getLastName());
        data.put("primaryContactPhoneNum", purchaseOrder.getPrimaryContact().getPhoneNum());
        data.put("primaryContactEmail", purchaseOrder.getPrimaryContact().getEmail());
        data.put("primaryContactWebsite", purchaseOrder.getPrimaryContact().getWebsite());

        data.put("warAddressLine", purchaseOrder.getAddressInfo().getAddressLine());
        data.put("warAddressCity", purchaseOrder.getAddressInfo().getCity());
        data.put("warAddressState", purchaseOrder.getAddressInfo().getState());
        data.put("warAddressPostalCode", purchaseOrder.getAddressInfo().getPostalCode());
        data.put("warAddressCountry", purchaseOrder.getAddressInfo().getCountry());

        List<Map<String, Object>> requestItemForFirestore = new ArrayList<>();
        for(RequestItem item : purchaseOrder.getRequestItemList()) {
            Map<String, Object> itemMap = new HashMap<>();

            itemMap.put("inventoryId", item.getInventoryId());
            itemMap.put("inventoryName", item.getInventoryName());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("uom", item.getUom());
            itemMap.put("notes", item.getNotes());

            requestItemForFirestore.add(itemMap);
        }

        data.put("requestItemList", requestItemForFirestore);

        return data;
    }
}