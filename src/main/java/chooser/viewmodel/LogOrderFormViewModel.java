package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import chooser.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BinaryOperator;

public class LogOrderFormViewModel {

    private ObservableList<CustomerOrder> customerOrderList;
    private final StringProperty customerOrderId = new SimpleStringProperty("");
    private final StringProperty tableNum = new SimpleStringProperty("");
    private final StringProperty quantity = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty notes = new SimpleStringProperty("");

    private final StringProperty tbvPageNumDisplay = new SimpleStringProperty("");

    private final BooleanProperty isValidCustomerOrderId = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidTableNum = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidQuantity = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidName = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidNotes = new SimpleBooleanProperty(false);


    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty orderAddValid = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoadedOrder= new SimpleBooleanProperty(false);
    private final IntegerProperty menutItemsAddedCounter = new SimpleIntegerProperty(0);


    private CustomerOrder currSelectedOrder;
    private List<BooleanProperty> orderAddValidInputList;
    private List<StringProperty> orderAddInputList;

    private Map<Integer, List<CustomerOrder>> customerOrderMap;
    private int trackTableViewPage;
    private int maxPages;


    public void setCurrSelectedOrder(CustomerOrder currSelectedOrder) {this.currSelectedOrder = currSelectedOrder;}
    public CustomerOrder getCurrSelectedOrder(){
        return  this.currSelectedOrder;
    }
    public StringProperty tbvPageNumDisplayProperty(){return tbvPageNumDisplay;}
    public StringProperty customerIDProperty(){return customerOrderId;}
    public StringProperty tableNumProperty(){return  tableNum;}

    public StringProperty quantityProperty(){return quantity;}
    public StringProperty nameProperty(){return name;}
    public StringProperty notesProperty(){return notes;}


    public BooleanProperty customerOrderIDValidProperty(){return isValidCustomerOrderId;}
    public BooleanProperty tableNumValidProperty(){return isValidTableNum;}

    public BooleanProperty nameValidProperty(){return isValidName;}
    public BooleanProperty quantityValidProperty(){return isValidQuantity;}
    public BooleanProperty notesValidProperty(){return isValidNotes;}


    public void setIsNewRedcordBoolean(boolean value){
        isNewRecord.set(value);
    }
    public BooleanProperty formValidProperty() {
        return formValid;
    }
    public BooleanProperty allowSaveProperty(){return allowSave;}
    public BooleanProperty orderAddValidProperty(){return orderAddValid;}


    public LogOrderFormViewModel(){

        customerOrderList = FXCollections.observableArrayList();
        menutItemsAddedCounter.set(0);
        isLoadedOrder.set(false);
        trackTableViewPage = 1;
        maxPages = 1;
        customerOrderMap = new HashMap<>();
        customerOrderMap.put(maxPages, new ArrayList<>());
        setTBVPageDisplay(null);


        isNewRecord.set(false);
        customerOrderId.set("New Menu Item");
        orderAddValid.set(false);
        MenuItemSelectUtils.menuItemIDProperty().set("Select Menu Item");

        orderAddValidInputList = List.of(
                isValidTableNum,
                MenuItemSelectUtils.menuItemIDValidProperty(),
                isValidQuantity
        );

        orderAddInputList = List.of(
                tableNum,
                MenuItemSelectUtils.menuItemIDProperty(),
                quantity,
                name,
                notes
        );

        customerOrderId.addListener((obs, oldVal, newVal) -> {
            isValidCustomerOrderId.set(!newVal.equals("New Customer Order"));
            hasChanged.set(true);
        });

        tableNum.addListener((obs, oldVal, newVal) -> {
            System.out.println("Checking Table Num Value: "+newVal);
            isValidTableNum.set(NewInventoryItemUtils.isValidQuantity(newVal));
            hasChanged.set(true);
        });


        quantity.addListener((obs, oldVal, newVal) -> {
            isValidQuantity.set(MenuItemSelectUtils.checkValidQuantityServing(newVal));
            hasChanged.set(true);
        });

        name.addListener((obs, oldVal, newVal) -> {
            isValidName.set(NewMenuItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        notes.addListener((obs, oldVal, newVal) -> {
            isValidNotes.set(NewMenuItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        orderAddValid.bind(isValidQuantity.and(isValidTableNum)
                .and(MenuItemSelectUtils.menuItemIDValidProperty())
        );

        allowSave.bind(formValid.and(hasChanged));

        tableNum.set("");
    }

    public void populateAddOrderInputs(){

        System.out.println("populateAddOrderInputs Called: ");
        System.out.println("currSelectedOrder Called: "+currSelectedOrder.getMenuItemId());

        isLoadedOrder.set(true);

        tableNum.set(currSelectedOrder.getTableNum());
        quantity.set(currSelectedOrder.getQuantity());
        name.set(currSelectedOrder.getName());
        notes.set(currSelectedOrder.getNotes());

        String displayText = (currSelectedOrder.getMenuItemId() == null || currSelectedOrder.getMenuItemId().isEmpty())
                ? "Select Menu Item"
                : currSelectedOrder.getMenuItemId();

        MenuItemSelectUtils.menuItemIDProperty().set(displayText);

    }

    public void onSelectOrderItem(String status, AnchorPane menuFormRootPane){
        MenuItemSelectUtils.showMenuItemSelect(status, menuFormRootPane);
    }

    public String generateCustomerOrderID() {
        String featureInitials = "ORD";
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return now.format(timeFormatter);
    }

    public boolean onSubmitNewLoggedOrder(){
        String customerOrderGenerated = generateCustomerOrderID();

        if(customerOrderList.isEmpty()){
            return false;
        }

        ProgressLoadUtils.showProgressLoad();


        boolean checkDeductions = MenuItemSelectUtils.changeInventoryItems("DEDUCT", customerOrderMap);
        if (!checkDeductions) {
            return false;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", customerOrderGenerated);
        data.put("loggedDate", getCurrentDate());
        data.put("loggedTime", getCurrentTime());

        User currLoggedUser = SessionManager.getLoggedInUser();

        data.put("signedBy", currLoggedUser.getId());

        List<CustomerOrder> orderList = new ArrayList<>();
        for (Map.Entry<Integer, List<CustomerOrder>> entry : customerOrderMap.entrySet()) {
            orderList.addAll(entry.getValue());
        }

        List<Map<String, Object>> customerOrderForFirestore = new ArrayList<>();
        for (CustomerOrder order : orderList) {
            Map<String, Object> newOrderMap = new HashMap<>();

            newOrderMap.put("tableNum", order.getTableNum());
            newOrderMap.put("menuItemId", order.getMenuItemId());
            newOrderMap.put("menuItemName", order.getMenuItemName());
            newOrderMap.put("quantity", order.getQuantity());
            newOrderMap.put("name", order.getName());
            newOrderMap.put("notes", order.getNotes());

            customerOrderForFirestore.add(newOrderMap);
        }

        data.put("orderList", customerOrderForFirestore);

        Task<Boolean> task = new Task<>() {

            @Override
            protected Boolean call() {
                try {
                    FirestoreUtils.writeDoc("CustomerOrderHistory", customerOrderGenerated, data);
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
                        SystemMessageUtils.setCurrSystemText("New Customer Order record has been saved: " + customerOrderGenerated);
                        SystemMessageUtils.setCurrPropertyColor("SUCCESS");
                    } else {
                        SystemMessageUtils.setCurrSystemText("Failed to log Customer order (inventory deduction failed).");
                        SystemMessageUtils.setCurrPropertyColor("ERROR");
                    }
                    SystemMessageUtils.messageAnimation();
                    ProgressLoadUtils.hideProgressLoad();
                });
            }

            protected void failed() {
                Platform.runLater(() -> {

                    SystemMessageUtils.setCurrSystemText("Failed to log Customer order (inventory deduction failed).");
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

        clearAddOrderInputs();
        handleMenuItemsLoggedCOunter("RESET");
        MenuItemSelectUtils.resetChangeItemSelectedStatus();
    }

    public void clearAddOrderInputs(){
        for(StringProperty userInput: orderAddInputList){
            userInput.set("");
        }

        tableNum.set("");
        isLoadedOrder.set(false);
        MenuItemSelectUtils.menuItemIDProperty().set("Select Menu Item");
        System.out.println("Checking table num data: "+tableNum.get());
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

    private void setTBVPageDisplay(TableView<CustomerOrder> customerOrderTableView){
        tbvPageNumDisplay.set(trackTableViewPage+" of "+maxPages+" pages");

        List<CustomerOrder> latestOrderList = customerOrderMap.get(trackTableViewPage);

        if(latestOrderList != null && customerOrderTableView!=null){
            customerOrderList = FXCollections.observableArrayList(latestOrderList);
            customerOrderTableView.setItems(customerOrderList);
        }


    }

    public void handleTableViewNav(String status, TableView<CustomerOrder> customerOrderTableView){
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

    public void onAddCustomerOrder(TableView<CustomerOrder> customerOrderTableView){
        if(isLoadedOrder.get()){
            onDeleteCustomerOrder(customerOrderTableView);
        }

        for (BooleanProperty validInput : orderAddValidInputList) {
            if (!validInput.get()) {
                return;
            }
        }

        if (notes.get().isEmpty()) {
            notes.set("--");
        }

        if (name.get().isEmpty()) {
            name.set("--");
        }

        CustomerOrder newCustomerOrder = new CustomerOrder(
                tableNum.get(),
                MenuItemSelectUtils.menuItemIDProperty().get(),
                MenuItemSelectUtils.menutItemNameProperty().get(),
                quantity.get(),
                name.get(),
                notes.get(),
                MenuItemSelectUtils.getStoredIngredientItem()
        );


        List<CustomerOrder> latestOrderList = customerOrderMap.get(maxPages);
        if (latestOrderList == null) {
            customerOrderMap.put(maxPages, new ArrayList<>());
        }

        latestOrderList = customerOrderMap.get(maxPages);
        if (latestOrderList.size() == 7) {
            maxPages++;
            customerOrderMap.put(maxPages, new ArrayList<>());
        }
        customerOrderMap.get(maxPages).add(newCustomerOrder);

        handleMenuItemsLoggedCOunter("ADD");
        MenuItemSelectUtils.changeItemSelectedStatus(MenuItemSelectUtils.menuItemIDProperty().get(), true);

        setTBVPageDisplay(customerOrderTableView);
        clearAddOrderInputs();
    }

    public void onDeleteCustomerOrder(TableView<CustomerOrder> customerOrderTableView) {
        System.out.println("onDeleteCustomerOrder Called");

        if (currSelectedOrder != null) {

            List<CustomerOrder> allOrders = new ArrayList<>();
            for (List<CustomerOrder> pageItems : customerOrderMap.values()) {
                allOrders.addAll(pageItems);
            }


            boolean removed = allOrders.removeIf(order ->
                            order.getName().equals(currSelectedOrder.getName()) &&
                            order.getQuantity().equals(currSelectedOrder.getQuantity()) &&
                            order.getMenuItemId().equals(currSelectedOrder.getMenuItemId()) &&
                            order.getTableNum().equals(currSelectedOrder.getTableNum()) &&
                                    order.getNotes().equals(currSelectedOrder.getNotes())
            );

            if (removed) {
                System.out.println("Logged Menu Item has been removed: " + currSelectedOrder.getMenuItemId());

                handleMenuItemsLoggedCOunter("DELETE");
                MenuItemSelectUtils.changeItemSelectedStatus(currSelectedOrder.getMenuItemId(), false);
                customerOrderMap.clear();
                int maxPages = 1;
                customerOrderMap.put(maxPages, new ArrayList<>());

                for (int i = 0; i < allOrders.size(); ++i) {
                    List<CustomerOrder> currentPage = customerOrderMap.get(maxPages);
                    if (currentPage.size() == 7) {
                        maxPages++;
                        customerOrderMap.put(maxPages, new ArrayList<>());
                    }
                    customerOrderMap.get(maxPages).add(allOrders.get(i));
                }

                setTBVPageDisplay(customerOrderTableView);
            }
        }
    }

    public void handleIngredientsTableView(String status, TableView<CustomerOrder> customerOrderTableView){
        switch (status){
            case "RESET":
                if (customerOrderTableView.getItems() != null && !customerOrderTableView.getItems().isEmpty()) {
                    customerOrderTableView.getItems().clear();
                    customerOrderMap.clear();
                }
                break;
            case "INITIAL":
                customerOrderTableView.setItems(customerOrderList);
                TableColumn<CustomerOrder, String> columnOne = new TableColumn<CustomerOrder,String>("Menu Item Id");
                columnOne.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("menuItemId"));

                TableColumn<CustomerOrder, String> columnTwo = new TableColumn<CustomerOrder,String>("Menu Item Name");
                columnTwo.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("menuItemName"));

                TableColumn<CustomerOrder, String> columnThree = new TableColumn<CustomerOrder,String>("Table Number");
                columnThree.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("tableNum"));

                TableColumn<CustomerOrder, String> columnFour= new TableColumn<CustomerOrder,String>("Serving Amount");
                columnFour.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("quantity"));

                TableColumn<CustomerOrder, String> columnFive= new TableColumn<CustomerOrder,String>("Customer Name");
                columnFive.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("name"));

                TableColumn<CustomerOrder, String> columnSix = new TableColumn<CustomerOrder,String>("Notes");
                columnSix.setCellValueFactory(new PropertyValueFactory<CustomerOrder,String>("notes"));

                customerOrderTableView.getColumns().add(columnOne);
                customerOrderTableView.getColumns().add(columnTwo);
                customerOrderTableView.getColumns().add(columnThree);
                customerOrderTableView.getColumns().add(columnFour);
                customerOrderTableView.getColumns().add(columnFive);
                customerOrderTableView.getColumns().add(columnSix);

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
