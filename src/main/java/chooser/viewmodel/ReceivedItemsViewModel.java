package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.PurchaseOrder;
import chooser.model.RequestItem;
import chooser.model.SessionManager;
import chooser.model.User;
import chooser.utils.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReceivedItemsViewModel {

    private List<BooleanProperty> validInputList;
    private List<StringProperty> formInputList;
    private List<BooleanProperty> validRequestItemInputList;
    private List<StringProperty> requestItemInputList;

    private int trackTableViewPage;
    private int maxPages;

    private RequestItem currentSelectedItem;
    private Map<Integer, List<RequestItem>> requestItemMap;
    private ObservableList<RequestItem> requestItemList;
    private final StringProperty tbvPageNumDisplay = new SimpleStringProperty("");
    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty requestItemFormValid = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoadedOrder= new SimpleBooleanProperty(false);
    private final IntegerProperty menutItemsAddedCounter = new SimpleIntegerProperty(0);


    public void setCurrRequestItem(RequestItem value) {
        this.currentSelectedItem = value;
    }

    public StringProperty tbvPageNumDisplayProperty(){return tbvPageNumDisplay;}
    public void setIsNewRedcordBoolean(boolean value){
        isNewRecord.set(value);
    }
    public BooleanProperty formValidProperty() {
        return formValid;
    }
    public BooleanProperty allowSaveProperty(){return allowSave;}
    public BooleanProperty requestItemValidProperty(){return requestItemFormValid;}

    private final StringProperty receiveItemsId = new SimpleStringProperty("");
    private final StringProperty deliveredDate = new SimpleStringProperty("");
    private final StringProperty deliveredTime = new SimpleStringProperty("");
    private final StringProperty receivedBy = new SimpleStringProperty("");


    private final StringProperty itemQuantity = new SimpleStringProperty("");
    private final StringProperty itemNotes = new SimpleStringProperty("");


    public StringProperty receiveItemsIdProp() { return receiveItemsId; }
    public StringProperty deliveredDateProp() { return deliveredDate; }
    public StringProperty deliveredTimeProp() { return deliveredTime; }
    public StringProperty receivedByProp() { return receivedBy; }
    public StringProperty itemQuantityProp() { return itemQuantity; }
    public StringProperty itemNotesProp() { return itemNotes; }



    private final BooleanProperty validDeliveredDate = new SimpleBooleanProperty(false);
    private final BooleanProperty validDeliveredTime = new SimpleBooleanProperty(false);
    private final BooleanProperty validItemQuantity = new SimpleBooleanProperty(false);
    private final BooleanProperty validReceivedItemsId = new SimpleBooleanProperty(false);


    public BooleanProperty validDeliveredDateProp() { return validDeliveredDate; }
    public BooleanProperty validDeliveredTimeProp() { return validDeliveredTime; }
    public BooleanProperty validItemQuantityProp() { return validItemQuantity; }
    public BooleanProperty validReceivedItemsIdProp() { return validReceivedItemsId; }

    public ReceivedItemsViewModel(){

        trackTableViewPage = 1;
        maxPages = 1;
        requestItemMap = new HashMap<>();
        requestItemMap.put(maxPages, new ArrayList<>());
        setTBVPageDisplay(null);


        isNewRecord.set(false);
        receiveItemsId.set("New Received Items");

        requestItemFormValid.set(false);

        PurchaseOrderUtils.itemInventoryIdProp().set("Search Inventory");

        validRequestItemInputList = List.of(
                validItemQuantity,
                PurchaseOrderUtils.validInventoryIdProp()
        );

        requestItemInputList = List.of(
                itemQuantity,
                PurchaseOrderUtils.itemInventoryIdProp(),
                itemNotes
        );

        validInputList = List.of(
                validDeliveredDate,
                validDeliveredTime
        );

        formInputList = List.of(
                deliveredDate,
                deliveredTime
        );

        requestItemFormValid.bind(validItemQuantity.and(PurchaseOrderUtils.validInventoryIdProp()));


        receiveItemsId.addListener((obs, oldVal, newVal) -> {
            validReceivedItemsId.set(!newVal.equals("New Received Items"));
            hasChanged.set(true);
        });

        deliveredDate.addListener((obs, oldVal, newVal) -> {
            validDeliveredDate.set(NewUserUtils.isValidDateOfBirth(newVal));
            hasChanged.set(true);
        });

        deliveredTime.addListener((obs, oldVal, newVal) -> {
            validDeliveredTime.set(PurchaseOrderUtils.isValidTime(newVal));
            hasChanged.set(true);
        });

        itemQuantity.addListener((obs, oldVal, newVal) -> {
            validItemQuantity.set(NewInventoryItemUtils.isValidQuantity(newVal));
            hasChanged.set(true);
        });

        allowSave.bind(formValid.and(hasChanged)
                .and(validDeliveredDate)
                .and(validDeliveredTime)
        );

    }
    public void onSearchSystem(String status, String dataStatus, AnchorPane formRoot){
        PurchaseOrderUtils.searchSystemData(status, dataStatus, formRoot);
    }

    public void populateAddOrderInputs(){

        isLoadedOrder.set(true);

        itemQuantity.set(currentSelectedItem.getQuantity());
        itemNotes.set(currentSelectedItem.getNotes());
        PurchaseOrderUtils.itemNameProp().set("Name: "+currentSelectedItem.getInventoryName());
        PurchaseOrderUtils.itemUOMProp().set("UOM: "+currentSelectedItem.getUom());
        PurchaseOrderUtils.itemInventoryIdProp().set(currentSelectedItem.getInventoryId());

    }
    public String generateReceivedItemsIdID() {
        String featureInitials = "RCVITEMS";
        LocalDateTime now = LocalDateTime.now();
        String todaysDate =  now.format(DateTimeFormatter.ofPattern("MMddyyyy"));//format: MMDDYYYY
        String currentTime =  now.format(DateTimeFormatter.ofPattern("HHmmss"));//format: HHMMSS

        return featureInitials + "_" + todaysDate + "_" + currentTime;
    }

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return now.format(dateFormatter);
    }

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return now.format(timeFormatter);
    }

    public boolean onSubmit(){
        String generatedId = generateReceivedItemsIdID();

        if(requestItemList.isEmpty()){
            return false;
        }

        ProgressLoadUtils.showProgressLoad();

        Map<String, Object> data = new HashMap<>();
        data.put("id", generatedId);
        data.put("deliveredDate", deliveredDate.get());
        data.put("deliveredTime", deliveredTime.get());


        data.put("receivedBy", receivedBy.get());

        data.put("purchaseOrderId", PurchaseOrderUtils.purchaseOrderIdProp().get());

        data.put("supplierId", PurchaseOrderUtils.supplerIdProp().get());
        data.put("supplierName", PurchaseOrderUtils.supplierNameProp().get());

        data.put("primaryContactFirstName", PurchaseOrderUtils.prmcFirstNameProp().get());
        data.put("primaryContactLastName", PurchaseOrderUtils.prmcLastNameProp().get());
        data.put("primaryContactPhoneNum", PurchaseOrderUtils.prmcPhoneNumberProp().get());
        data.put("primaryContactEmail", PurchaseOrderUtils.prmcEmailProp().get());
        data.put("primaryContactWebsite", PurchaseOrderUtils.prmcWebsiteProp().get());

        data.put("warAddressLine", PurchaseOrderUtils.warAddressLineProp().get());
        data.put("warAddressCity", PurchaseOrderUtils.warCityProp().get());
        data.put("warAddressState", PurchaseOrderUtils.warStateProp().get());
        data.put("warAddressPostalCode", PurchaseOrderUtils.warPostalCodeProp().get());
        data.put("warAddressCountry", PurchaseOrderUtils.warCountryProp().get());


        List<RequestItem> itemList = new ArrayList<>();
        for (Map.Entry<Integer, List<RequestItem>> entry : requestItemMap.entrySet()) {
            itemList.addAll(entry.getValue());
        }

        List<Map<String, Object>> requestItemForFirestore = new ArrayList<>();
        for (RequestItem order : itemList) {
            Map<String, Object> newOrderMap = new HashMap<>();

            newOrderMap.put("inventoryId", order.getInventoryId());
            newOrderMap.put("inventoryName", order.getInventoryName());
            newOrderMap.put("quantity", order.getQuantity());
            newOrderMap.put("uom", order.getUom());
            newOrderMap.put("notes", order.getNotes());

            requestItemForFirestore.add(newOrderMap);
        }

        data.put("requestItemList", requestItemForFirestore);

        PurchaseOrderUtils.handleUpdateInventoryStock(requestItemList);
        PurchaseOrderUtils.updatePurchaseOrderStatus(PurchaseOrderUtils.purchaseOrderIdProp().get());

        Task<Boolean> task = new Task<>() {

            @Override
            protected Boolean call() {
                try {
                    FirestoreUtils.writeDoc("ReceivedItems", generatedId, data);
                    hasChanged.set(false);
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    boolean result = getValue();

                    if (result) {
                        SystemMessageUtils.setCurrSystemText("New Received Items record has been saved: " + generatedId);
                        SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                    } else {
                        SystemMessageUtils.setCurrSystemText("Failed to log Received Items.");
                        SystemMessageUtils.setCurrPropertyColor("ERROR");
                    }
                    SystemMessageUtils.messageAnimation();
                    ProgressLoadUtils.hideProgressLoad();
                });
            }

            protected void failed() {
                Platform.runLater(() -> {

                    SystemMessageUtils.setCurrSystemText("Failed to log Received Items.");
                    SystemMessageUtils.setCurrPropertyColor("ERROR");

                    SystemMessageUtils.messageAnimation();
                    ProgressLoadUtils.hideProgressLoad();
                });
            }

        };

        new Thread(task).start();


        return true;

    }

    public void clearInputs(){

        for(StringProperty input: formInputList){
            input.set("");
        }

        clearAddOrderInputs();

        receiveItemsId.set("New Received Items");

        deliveredDate.set(getCurrentDate());
        deliveredTime.set(getCurrentTime());
        User currLoggedUser = SessionManager.getLoggedInUser();
        receivedBy.set(currLoggedUser.getUsername());

        PurchaseOrderUtils.purchaseOrderIdProp().set("Search PO");

        PurchaseOrderUtils.prmcFirstNameProp().set("N/A");
        PurchaseOrderUtils.prmcLastNameProp().set("");

        PurchaseOrderUtils.prmcPhoneNumberProp().set("N/A");
        PurchaseOrderUtils.prmcEmailProp().set("N/A");
        PurchaseOrderUtils.prmcWebsiteProp().set("N/A");

        PurchaseOrderUtils.warAddressLineProp().set("N/A");
        PurchaseOrderUtils.warCityProp().set("");
        PurchaseOrderUtils.warStateProp().set("");
        PurchaseOrderUtils.warPostalCodeProp().set("");
        PurchaseOrderUtils.warCountryProp().set("");


        PurchaseOrderUtils.supplerIdProp().set("N/A");
        PurchaseOrderUtils.supplierNameProp().set("");

    }

    public void clearAddOrderInputs(){
        for(StringProperty userInput: requestItemInputList){
            userInput.set("");
        }

        isLoadedOrder.set(false);
        PurchaseOrderUtils.itemInventoryIdProp().set("Search Inventory");

        PurchaseOrderUtils.itemNameProp().set("N/A");
        PurchaseOrderUtils.itemUOMProp().set("N/A");

    }

    public void updateValidImageViews(HBox hBox, BooleanProperty... properties) {

        Image requiredIconImg = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/requiredIcon.png")).toExternalForm());
        Image validIconImage = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/valid.png")).toExternalForm());

        ImageView imageView = (ImageView) hBox.lookup(".image-view");
        Label label = (Label) hBox.lookup(".label");

        if (imageView != null && requiredIconImg!=null && validIconImage != null) {

            imageView.setVisible(true);
            imageView.setImage(requiredIconImg);

            boolean allTrue = false;
            for (BooleanProperty property : properties) {
                if (property.get()) {
                    allTrue = true;
                    break;
                }
            }

            if (allTrue) {

                imageView.setImage(validIconImage);
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }

    private void setTBVPageDisplay(TableView<RequestItem> customerOrderTableView){
        tbvPageNumDisplay.set(trackTableViewPage+" of "+maxPages+" pages");

        List<RequestItem> latestOrderList = requestItemMap.get(trackTableViewPage);

        if(latestOrderList != null && customerOrderTableView!=null){
            requestItemList = FXCollections.observableArrayList(latestOrderList);
            customerOrderTableView.setItems(requestItemList);
        }


    }

    public void handleTableViewNav(String status, TableView<RequestItem> customerOrderTableView){
        switch (status){
            case "LEFT":
                if(trackTableViewPage > 1){
                    trackTableViewPage--;
                }
                break;
            case "RIGHT":
                if(trackTableViewPage < maxPages){
                    trackTableViewPage++;
                }
                break;
        }
        setTBVPageDisplay(customerOrderTableView);
    }

    public void handleMenuItemsLoggedCOunter(String status){
        switch (status){
            case "RESET":
                menutItemsAddedCounter.set(0);
                break;
            case "ADD":
                menutItemsAddedCounter.set(menutItemsAddedCounter.get() + 1);
                break;
            case "DELETE":
                menutItemsAddedCounter.set(menutItemsAddedCounter.get() -1);
                break;
        }


        formValid.set(menutItemsAddedCounter.get() > 0);
    }

    public void initialPopulateTable(TableView<RequestItem> customerOrderTableView){
        List<RequestItem> checkList = PurchaseOrderUtils.getStoreRequestItemList();
        handleMenuItemsLoggedCOunter("RESET");
        if (checkList != null && !checkList.isEmpty()) {

            requestItemMap.clear();
            maxPages = 1;

            for (RequestItem item : checkList) {
                List<RequestItem> latestOrderList = requestItemMap.get(maxPages);
                if (latestOrderList == null) {
                    requestItemMap.put(maxPages, new ArrayList<>());
                    latestOrderList = requestItemMap.get(maxPages);
                }

                if (latestOrderList.size() == 7) {
                    maxPages++;
                    requestItemMap.put(maxPages, new ArrayList<>());
                    latestOrderList = requestItemMap.get(maxPages);
                }

                latestOrderList.add(item);
                handleMenuItemsLoggedCOunter("ADD");
            }


            setTBVPageDisplay(customerOrderTableView);
        }
    };

    public void onAddCustomerOrder(TableView<RequestItem> customerOrderTableView){
        if(isLoadedOrder.get()){
            onDeleteCustomerOrder(customerOrderTableView);
        }

        for (BooleanProperty validInput : validRequestItemInputList) {
            if (!validInput.get()) {
                return;
            }
        }

        if (itemNotes.get().isEmpty()) {
            itemNotes.set("--");
        }


        RequestItem newRequestItem = new RequestItem(
                PurchaseOrderUtils.itemInventoryIdProp().get(),
                PurchaseOrderUtils.itemNameProp().get(),
                itemQuantity.get(),
                PurchaseOrderUtils.itemUOMProp().get(),
                itemNotes.get()
        );


        List<RequestItem> latestOrderList = requestItemMap.get(maxPages);
        if (latestOrderList == null) {
            requestItemMap.put(maxPages, new ArrayList<>());
        }

        latestOrderList = requestItemMap.get(maxPages);
        if (latestOrderList.size() == 7) {
            maxPages++;
            requestItemMap.put(maxPages, new ArrayList<>());
        }
        requestItemMap.get(maxPages).add(newRequestItem);

        handleMenuItemsLoggedCOunter("ADD");
        //MenuItemSelectUtils.changeItemSelectedStatus(MenuItemSelectUtils.menuItemIDProperty().get(), true);

        setTBVPageDisplay(customerOrderTableView);
        clearAddOrderInputs();
    }

    public void onDeleteCustomerOrder(TableView<RequestItem> customerOrderTableView) {

        if (currentSelectedItem != null) {

            List<RequestItem> allOrders = new ArrayList<>();
            for (List<RequestItem> pageItems : requestItemMap.values()) {
                allOrders.addAll(pageItems);
            }

            boolean removed = allOrders.removeIf(order ->
                    order.getInventoryName().equals(currentSelectedItem.getInventoryName()) &&
                            order.getInventoryId().equals(currentSelectedItem.getInventoryId()) &&
                            order.getQuantity().equals(currentSelectedItem.getQuantity()) &&
                            order.getUom().equals(currentSelectedItem.getUom()) &&
                            order.getNotes().equals(currentSelectedItem.getNotes())
            );

            if (removed) {

                handleMenuItemsLoggedCOunter("DELETE");
                //MenuItemSelectUtils.changeItemSelectedStatus(currentSelectedItem.getMenuItemId(), false);

                requestItemMap.clear();
                int maxPages = 1;
                requestItemMap.put(maxPages, new ArrayList<>());

                for (int i = 0; i < allOrders.size(); ++i) {
                    List<RequestItem> currentPage = requestItemMap.get(maxPages);
                    if (currentPage.size() == 7) {
                        maxPages++;
                        requestItemMap.put(maxPages, new ArrayList<>());
                    }
                    requestItemMap.get(maxPages).add(allOrders.get(i));
                }

                setTBVPageDisplay(customerOrderTableView);
            }
        }
    }

    public void handleIngredientsTableView(String status, TableView<RequestItem> customerOrderTableView){
        switch (status){
            case "RESET":
                if (customerOrderTableView.getItems() != null && !customerOrderTableView.getItems().isEmpty()) {
                    customerOrderTableView.getItems().clear();
                    requestItemMap.clear();
                }
                break;
            case "INITIAL":
                customerOrderTableView.setItems(requestItemList);
                TableColumn<RequestItem, String> columnOne = new TableColumn<RequestItem,String>("Item Id");
                columnOne.setCellValueFactory(new PropertyValueFactory<RequestItem,String>("inventoryId"));

                TableColumn<RequestItem, String> columnTwo = new TableColumn<RequestItem,String>("Item Name");
                columnTwo.setCellValueFactory(new PropertyValueFactory<RequestItem,String>("inventoryName"));

                TableColumn<RequestItem, String> columnThree = new TableColumn<RequestItem,String>("Quantity");
                columnThree.setCellValueFactory(new PropertyValueFactory<RequestItem,String>("quantity"));

                TableColumn<RequestItem, String> columnFour= new TableColumn<RequestItem,String>("UOM");
                columnFour.setCellValueFactory(new PropertyValueFactory<RequestItem,String>("uom"));

                TableColumn<RequestItem, String> columnFive = new TableColumn<RequestItem,String>("Notes");
                columnFive.setCellValueFactory(new PropertyValueFactory<RequestItem,String>("notes"));

                customerOrderTableView.getColumns().add(columnOne);
                customerOrderTableView.getColumns().add(columnTwo);
                customerOrderTableView.getColumns().add(columnThree);
                customerOrderTableView.getColumns().add(columnFour);
                customerOrderTableView.getColumns().add(columnFive);

                customerOrderTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                break;
            case "ADD":
                onAddCustomerOrder(customerOrderTableView);
                hasChanged.set(true);
                break;

            case "DELETE":
                onDeleteCustomerOrder(customerOrderTableView);
                hasChanged.set(true);
                clearAddOrderInputs();
                break;

        }
    }
}
