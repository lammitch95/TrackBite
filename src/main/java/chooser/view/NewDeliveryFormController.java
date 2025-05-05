package chooser.view;

import chooser.model.Suppliers;
import chooser.viewmodel.NewDeliveryViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NewDeliveryFormController {

    // FXML injections
    @FXML private BorderPane DeliveryFormPane;
    @FXML private Label DeliveryInfoLabel;
    @FXML private HBox saveBtn1;
    @FXML private ImageView saveBtnImageView1;
    @FXML private HBox addNewBtn1;
    @FXML private HBox deleteBtn1;
    @FXML private VBox DeliveryDetailsVbox;
    @FXML private HBox DeliveryIDHbox;
    @FXML private TextField DeliveryID;
    @FXML private HBox OrderNumHbox;
    @FXML private TextField OrderNumber;
    @FXML private HBox DeliveryDateHbox;
    @FXML private DatePicker DeliveryDate;
    @FXML private HBox DeliveryTimeHbox;
    @FXML private TextField DeliveryTime;
    @FXML private ComboBox<String> DeliveryTimeUnit;
    @FXML private HBox DeliveryAddressHbox;
    @FXML private TextField DeliveryAddress;
    @FXML private VBox DeliveryListVbox;
    @FXML private HBox ItemNameHbox;
    @FXML private TextField ItemName;
    @FXML private HBox ItemQualityHbox;
    @FXML private TextField ItemQuantity;
    @FXML private ComboBox<String> Unit;
    @FXML private HBox PriorityHbox;
    @FXML private ComboBox<String> Priority;
    @FXML private HBox NotesHbox;
    @FXML private TextArea Notes;
    @FXML private Label feedbackLabel;
    @FXML private Button ItemAddBtn;
    @FXML private Button ItemClearBtn;
    @FXML private TableView<DeliveryItem> DeliveryTableView;
    @FXML private HBox leftArrowBtn;
    @FXML private Label pageCountLbl;
    @FXML private HBox rightArrowBtn;
    @FXML private ImageView leftArrowImage;
    @FXML private ImageView rightArrowImage;
    @FXML private HBox deleteIngredientBtn;
    @FXML private VBox SupplierDetailsVbox;
    @FXML private HBox SupplierHBox;
    @FXML private ComboBox<Suppliers> supplierComboBox;
    @FXML private HBox SupplierNameHBox;
    @FXML private TextField supplierFirstName;
    @FXML private TextField SupplierLastName;
    @FXML private HBox SupplierNumberHBox;
    @FXML private TextField supplierContactNum;
    @FXML private HBox SupplierAddressHBox;
    @FXML private TextField supplierAddress;
    @FXML private Button submitButton;

    private final NewDeliveryViewModel viewModel = new NewDeliveryViewModel();
    private Firestore db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean updatingSupplierFields = false;
    private int currentPage = 1;
    private final int itemsPerPage = 5;
    private static int deliveryCounter = 1;
    private static int orderCounter = 1;

    // Inner class for DeliveryItem
    public static class DeliveryItem {
        private final SimpleStringProperty itemName;
        private final SimpleIntegerProperty itemQuantity;
        private final SimpleStringProperty unit;
        private final SimpleStringProperty priority;
        private final SimpleStringProperty notes;

        public DeliveryItem(String itemName, int itemQuantity, String unit, String priority, String notes) {
            this.itemName = new SimpleStringProperty(itemName);
            this.itemQuantity = new SimpleIntegerProperty(itemQuantity);
            this.unit = new SimpleStringProperty(unit);
            this.priority = new SimpleStringProperty(priority);
            this.notes = new SimpleStringProperty(notes);
        }

        public String getItemName() { return itemName.get(); }
        public SimpleStringProperty itemNameProperty() { return itemName; }
        public int getItemQuantity() { return itemQuantity.get(); }
        public SimpleIntegerProperty itemQuantityProperty() { return itemQuantity; }
        public String getUnit() { return unit.get(); }
        public SimpleStringProperty unitProperty() { return unit; }
        public String getPriority() { return priority.get(); }
        public SimpleStringProperty priorityProperty() { return priority; }
        public String getNotes() { return notes.get(); }
        public SimpleStringProperty notesProperty() { return notes; }
    }

    @FXML
    public void initialize() {
        System.out.println("START: Initializing NewDeliveryFormController");
        try {
            db = FirestoreClient.getFirestore();
            System.out.println("Firestore initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize Firestore: " + e.getMessage());
            Platform.runLater(() -> {
                feedbackLabel.setText("Firestore init failed. Buttons still functional.");
                feedbackLabel.setStyle("-fx-text-fill: orange;");
            });
            db = null;
        }

        try {
            viewModel.deliveryIdProperty().set(generateShortDeliveryId());
            viewModel.orderNumberProperty().set(generateShortOrderNumber());
            System.out.println("DeliveryID and OrderNumber set");
        } catch (Exception e) {
            System.err.println("Error setting IDs: " + e.getMessage());
        }

        try {
            DeliveryID.textProperty().bindBidirectional(viewModel.deliveryIdProperty());
            OrderNumber.textProperty().bindBidirectional(viewModel.orderNumberProperty());
            DeliveryDate.valueProperty().bindBidirectional(viewModel.deliveryDateProperty());
            DeliveryTime.textProperty().bindBidirectional(viewModel.deliveryTimeProperty());
            DeliveryAddress.textProperty().bindBidirectional(viewModel.deliveryAddressProperty());
            DeliveryTimeUnit.valueProperty().bindBidirectional(viewModel.deliveryTimeUnitProperty());
            supplierFirstName.textProperty().bindBidirectional(viewModel.supplierFirstNameProperty());
            SupplierLastName.textProperty().bindBidirectional(viewModel.supplierLastNameProperty());
            supplierContactNum.textProperty().bindBidirectional(viewModel.supplierContactNumProperty());
            supplierAddress.textProperty().bindBidirectional(viewModel.supplierAddressProperty());
            System.out.println("ViewModel bindings complete");
        } catch (Exception e) {
            System.err.println("Error setting bindings: " + e.getMessage());
        }

        try {
            DeliveryTimeUnit.getItems().addAll("AM", "PM");
            Unit.getItems().addAll(
                    "Teaspoon (tsp)", "Tablespoon (tbsp)", "Fluid ounce (fl oz)", "Cup (c)",
                    "Cases(CS)" , "Pieces(PC)",
                    "Pint (pt)", "Quart (qt)", "Gallon (gal)", "Liter (L)", "Milliliter (mL)",
                    "Ounce (oz)", "Pound (lb)", "Gram (g)", "Kilogram (kg)", "Whole",
                    "Slice", "Clove", "Stick", "Dash, Pinch, Smidgen,"
            );
            Priority.getItems().addAll("Low", "Medium", "High");
            System.out.println("ComboBoxes initialized");
        } catch (Exception e) {
            System.err.println("Error initializing ComboBoxes: " + e.getMessage());
        }

        try {
            submitButton.disableProperty().bind(
                    Bindings.createBooleanBinding(() -> viewModel.getDeliveryItems().isEmpty(), viewModel.getDeliveryItems())
            );
            System.out.println("Submit button bound to deliveryItems isEmpty binding");
        } catch (Exception e) {
            System.err.println("Error binding submitButton: " + e.getMessage());
        }

        try {
            setupTableView();
            System.out.println("TableView setup complete");
        } catch (Exception e) {
            System.err.println("Error setting up TableView: " + e.getMessage());
        }

        try {
            setupSupplierComboBox();
            System.out.println("Supplier ComboBox setup complete");
        } catch (Exception e) {
            System.err.println("Error setting up supplier ComboBox: " + e.getMessage());
        }

        try {
            saveBtn1.setOnMouseClicked(this::handleSaveBtn);
            addNewBtn1.setOnMouseClicked(this::handleAddNewBtn);
            leftArrowBtn.setOnMouseClicked(this::handleLeftArrowBtn);
            rightArrowBtn.setOnMouseClicked(this::handleRightArrowBtn);
            deleteIngredientBtn.setOnMouseClicked(this::handleDeleteIngredientBtn);
            System.out.println("Event handlers set");
        } catch (Exception e) {
            System.err.println("Error setting event handlers: " + e.getMessage());
        }

        try {
            viewModel.getDeliveryItems().addListener((ListChangeListener<DeliveryItem>) change -> {
                System.out.println("deliveryItems changed");
                while (change.next()) {
                    if (change.wasAdded()) {
                        System.out.println("Items added: " + change.getAddedSubList());
                    } else if (change.wasRemoved()) {
                        System.out.println("Items removed: " + change.getRemoved());
                    }
                    System.out.println("deliveryItems size: " + viewModel.getDeliveryItems().size());
                }
            });
            System.out.println("ListChangeListener added");
        } catch (Exception e) {
            System.err.println("Error adding ListChangeListener: " + e.getMessage());
        }

        try {
            updateTableViewPage();
            System.out.println("Initial pagination complete");
        } catch (Exception e) {
            System.err.println("Error initializing pagination: " + e.getMessage());
        }

        System.out.println("END: Initialization complete");
    }

    private String generateShortDeliveryId() {
        String id = "DEL-" + String.format("%03d", deliveryCounter++);
        System.out.println("Generated DeliveryID: " + id);
        return id;
    }

    private String generateShortOrderNumber() {
        String id = "ORD-" + String.format("%03d", orderCounter++);
        System.out.println("Generated OrderNumber: " + id);
        return id;
    }

    private void setupTableView() {
        System.out.println("START: Setting up TableView");
        TableColumn<DeliveryItem, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        TableColumn<DeliveryItem, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().itemQuantityProperty());

        TableColumn<DeliveryItem, String> unitColumn = new TableColumn<>("Unit");
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());

        TableColumn<DeliveryItem, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());

        TableColumn<DeliveryItem, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        DeliveryTableView.getColumns().addAll(nameColumn, quantityColumn, unitColumn, priorityColumn, notesColumn);
        DeliveryTableView.setItems(viewModel.getDeliveryItems());

        nameColumn.prefWidthProperty().bind(DeliveryTableView.widthProperty().multiply(0.3));
        quantityColumn.prefWidthProperty().bind(DeliveryTableView.widthProperty().multiply(0.15));
        unitColumn.prefWidthProperty().bind(DeliveryTableView.widthProperty().multiply(0.2));
        priorityColumn.prefWidthProperty().bind(DeliveryTableView.widthProperty().multiply(0.15));
        notesColumn.prefWidthProperty().bind(DeliveryTableView.widthProperty().multiply(0.2));
        System.out.println("END: TableView setup complete");
    }

    private void setupSupplierComboBox() {
        System.out.println("START: Setting up supplier ComboBox");
        ObservableList<Suppliers> suppliers = FXCollections.observableArrayList();
        supplierComboBox.setItems(suppliers);
        supplierComboBox.setEditable(true);
        supplierComboBox.setPromptText("Type to search supplier");

        if (db != null) {
            executor.submit(() -> {
                try {
                    ApiFuture<QuerySnapshot> future = db.collection("Suppliers").get();
                    List<Suppliers> supplierList = future.get().getDocuments().stream()
                            .map(this::documentToSuppliers)
                            .collect(Collectors.toList());
                    Platform.runLater(() -> {
                        suppliers.setAll(supplierList);
                        System.out.println("Loaded " + supplierList.size() + " suppliers");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        feedbackLabel.setText("Error fetching suppliers: " + e.getMessage());
                        feedbackLabel.setStyle("-fx-text-fill: red;");
                    });
                    System.err.println("Error fetching suppliers: " + e.getMessage());
                }
            });
        } else {
            System.out.println("Skipping supplier fetch: Firestore not initialized");
        }

        supplierComboBox.setConverter(new StringConverter<Suppliers>() {
            @Override
            public String toString(Suppliers supplier) {
                return supplier != null ? supplier.getSupplierName() : "";
            }

            @Override
            public Suppliers fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;
                return supplierComboBox.getItems().stream()
                        .filter(s -> s.getSupplierName().equalsIgnoreCase(string.trim()))
                        .findFirst()
                        .orElse(null);
            }
        });

        supplierComboBox.setCellFactory(lv -> new ListCell<Suppliers>() {
            @Override
            protected void updateItem(Suppliers supplier, boolean empty) {
                super.updateItem(supplier, empty);
                setText(empty || supplier == null ? "" : supplier.getSupplierName());
            }
        });

        supplierComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            if (!updatingSupplierFields) {
                if (newSupplier != null) {
                    updateSupplierFields(newSupplier);
                } else if (oldSupplier != null && supplierComboBox.getEditor().getText().isEmpty()) {
                    clearSupplierFields();
                }
            }
        });

        supplierComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!updatingSupplierFields && supplierComboBox.isFocused()) {
                String filter = newValue != null ? newValue.trim().toLowerCase() : "";
                ObservableList<Suppliers> filtered = suppliers.filtered(supplier ->
                        supplier.getSupplierName() != null &&
                                supplier.getSupplierName().toLowerCase().contains(filter)
                );
                supplierComboBox.setItems(filtered);
                if (!filter.isEmpty() && !supplierComboBox.isShowing()) {
                    supplierComboBox.show();
                }
            }
        });

        supplierComboBox.getEditor().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Suppliers selected = supplierComboBox.getConverter().fromString(
                        supplierComboBox.getEditor().getText()
                );
                if (selected != null) {
                    supplierComboBox.getSelectionModel().select(selected);
                }
                event.consume();
            }
        });
        System.out.println("END: Supplier ComboBox setup complete");
    }

    private void updateSupplierFields(Suppliers supplier) {
        try {
            updatingSupplierFields = true;
            String contactPerson = supplier.getContactPerson() != null ? supplier.getContactPerson() : "";
            String[] nameParts = contactPerson.trim().split("\\s+", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            supplierFirstName.setText(firstName);
            SupplierLastName.setText(lastName);
            supplierContactNum.setText(supplier.getPhoneNumber() != null ? supplier.getPhoneNumber() : "");
            supplierAddress.setText(supplier.getBusinessAddress() != null ? supplier.getBusinessAddress() : "");

            Platform.runLater(() -> supplierComboBox.getEditor().setText(supplier.getSupplierName()));
        } finally {
            updatingSupplierFields = false;
        }
    }

    private void clearSupplierFields() {
        supplierFirstName.setText("");
        SupplierLastName.setText("");
        supplierContactNum.setText("");
        supplierAddress.setText("");
    }

    private Suppliers documentToSuppliers(QueryDocumentSnapshot document) {
        return new Suppliers(
                document.getString("supplierId"),
                document.getString("supplierName"),
                document.getString("contactPerson"),
                document.getString("phoneNumber"),
                document.getString("emailAddress"),
                document.getString("websiteLink"),
                document.getString("businessAddress"),
                document.getString("warehouseAddress"),
                document.getString("deliveryArea")
        );
    }

    @FXML
    private void handleItemAddBtn(ActionEvent event) {
        System.out.println("START: ItemAddBtn clicked");
        try {
            String itemName = ItemName.getText() != null ? ItemName.getText().trim() : "";
            if (itemName.isEmpty()) {
                feedbackLabel.setText("Item name is required!");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                System.out.println("Validation failed: Item name is empty");
                return;
            }

            int quantity = 1;
            try {
                if (ItemQuantity.getText() != null && !ItemQuantity.getText().trim().isEmpty()) {
                    quantity = Integer.parseInt(ItemQuantity.getText().trim());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity, defaulting to 1");
            }

            String unit = Unit.getValue() != null ? Unit.getValue() : "Whole";
            String priority = Priority.getValue() != null ? Priority.getValue() : "Low";
            String notes = Notes.getText() != null ? Notes.getText().trim() : "";

            DeliveryItem item = new DeliveryItem(itemName, quantity, unit, priority, notes);
            viewModel.getDeliveryItems().add(item);
            System.out.println("Added item: " + item.getItemName() + ", Quantity: " + item.getItemQuantity());

            ItemName.clear();
            ItemQuantity.clear();
            Unit.getSelectionModel().clearSelection();
            Priority.getSelectionModel().clearSelection();
            Notes.clear();

            updateTableViewPage();
            feedbackLabel.setText("Item added successfully!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            System.out.println("END: ItemAddBtn completed, deliveryItems size = " + viewModel.getDeliveryItems().size());
        } catch (Exception e) {
            feedbackLabel.setText("Error adding item: " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            System.err.println("Error in handleItemAddBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleItemClearBtn(ActionEvent event) {
        System.out.println("START: ItemClearBtn clicked");
        try {
            viewModel.getDeliveryItems().clear();
            ItemName.clear();
            ItemQuantity.clear();
            Unit.getSelectionModel().clearSelection();
            Priority.getSelectionModel().clearSelection();
            Notes.clear();
            currentPage = 1;
            updateTableViewPage();
            feedbackLabel.setText("Items cleared successfully!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            System.out.println("END: ItemClearBtn completed, deliveryItems size = " + viewModel.getDeliveryItems().size());
        } catch (Exception e) {
            feedbackLabel.setText("Error clearing items: " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            System.err.println("Error in handleItemClearBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteIngredientBtn(MouseEvent event) {
        System.out.println("START: DeleteIngredientBtn clicked");
        try {
            DeliveryItem selected = DeliveryTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.getDeliveryItems().remove(selected);
                updateTableViewPage();
                feedbackLabel.setText("Item deleted!");
                feedbackLabel.setStyle("-fx-text-fill: green;");
                System.out.println("Deleted item: " + selected.getItemName());
            } else {
                feedbackLabel.setText("Select an item to delete!");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                System.out.println("No item selected for deletion");
            }
            System.out.println("END: DeleteIngredientBtn completed");
        } catch (Exception e) {
            feedbackLabel.setText("Error deleting item: " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            System.err.println("Error in handleDeleteIngredientBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleLeftArrowBtn(MouseEvent event) {
        System.out.println("START: LeftArrowBtn clicked");
        try {
            if (currentPage > 1) {
                currentPage--;
                updateTableViewPage();
                System.out.println("Navigated to page: " + currentPage);
            }
            System.out.println("END: LeftArrowBtn completed");
        } catch (Exception e) {
            System.err.println("Error in handleLeftArrowBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleRightArrowBtn(MouseEvent event) {
        System.out.println("START: RightArrowBtn clicked");
        try {
            int totalPages = (int) Math.ceil((double) viewModel.getDeliveryItems().size() / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                updateTableViewPage();
                System.out.println("Navigated to page: " + currentPage);
            }
            System.out.println("END: RightArrowBtn completed");
        } catch (Exception e) {
            System.err.println("Error in handleRightArrowBtn: " + e.getMessage());
        }
    }

    private void updateTableViewPage() {
        System.out.println("START: Updating TableView page");
        try {
            int totalItems = viewModel.getDeliveryItems().size();
            int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / itemsPerPage));
            currentPage = Math.min(currentPage, totalPages);

            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

            ObservableList<DeliveryItem> pageItems = FXCollections.observableArrayList();
            if (startIndex < totalItems) {
                pageItems.addAll(viewModel.getDeliveryItems().subList(startIndex, endIndex));
            }

            DeliveryTableView.setItems(pageItems);
            pageCountLbl.setText(String.format("%d of %d pages", currentPage, totalPages));
            System.out.println("END: TableView updated, Page " + currentPage + ", Items displayed: " + pageItems.size());
        } catch (Exception e) {
            System.err.println("Error updating TableView: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveBtn(MouseEvent event) {
        System.out.println("START: SaveBtn clicked");
        try {
            handleSubmitButtonAction(null);
            System.out.println("END: SaveBtn completed");
        } catch (Exception e) {
            System.err.println("Error in handleSaveBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddNewBtn(MouseEvent event) {
        System.out.println("START: AddNewBtn clicked");
        try {
            clearForm();
            feedbackLabel.setText("New delivery form ready!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            System.out.println("END: Form cleared for new delivery");
        } catch (Exception e) {
            feedbackLabel.setText("Error clearing form: " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            System.err.println("Error in handleAddNewBtn: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        System.out.println("START: SubmitButton clicked");
        try {
            if (viewModel.getDeliveryItems().isEmpty()) {
                feedbackLabel.setText("Add at least one item!");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                System.out.println("Validation failed: No items in delivery");
                return;
            }

            if (db == null) {
                feedbackLabel.setText("Cannot save: Firestore not initialized!");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                System.out.println("Firestore not available");
                clearForm();
                return;
            }

            executor.submit(() -> {
                System.out.println("Starting Firestore write");
                int retries = 3;
                while (retries > 0) {
                    try {
                        Map<String, Object> deliveryData = new HashMap<>();
                        String deliveryId = viewModel.deliveryIdProperty().get() != null ? viewModel.deliveryIdProperty().get() : "";
                        deliveryData.put("deliveryId", deliveryId);
                        deliveryData.put("orderNumber", viewModel.orderNumberProperty().get() != null ? viewModel.orderNumberProperty().get() : "");
                        deliveryData.put("deliveryDate", viewModel.deliveryDateProperty().get() != null ? viewModel.deliveryDateProperty().get().toString() : "");
                        deliveryData.put("deliveryTime", viewModel.deliveryTimeProperty().get() != null ? viewModel.deliveryTimeProperty().get() + " " +
                                (viewModel.deliveryTimeUnitProperty().get() != null ? viewModel.deliveryTimeUnitProperty().get() : "") : "");
                        deliveryData.put("deliveryAddress", viewModel.deliveryAddressProperty().get() != null ? viewModel.deliveryAddressProperty().get() : "");

                        Map<String, Object> supplierData = new HashMap<>();
                        Suppliers selectedSupplier = supplierComboBox.getSelectionModel().getSelectedItem();
                        String contactPerson = viewModel.supplierFirstNameProperty().get() != null && !viewModel.supplierFirstNameProperty().get().isEmpty() ?
                                viewModel.supplierFirstNameProperty().get() : "Unknown";
                        if (viewModel.supplierLastNameProperty().get() != null && !viewModel.supplierLastNameProperty().get().isEmpty()) {
                            contactPerson += " " + viewModel.supplierLastNameProperty().get();
                        }
                        if (selectedSupplier != null && selectedSupplier.getSupplierId() != null) {
                            supplierData.put("supplierId", selectedSupplier.getSupplierId());
                            supplierData.put("supplierName", selectedSupplier.getSupplierName() != null ? selectedSupplier.getSupplierName() : "");
                        } else {
                            supplierData.put("supplierId", "CUSTOM-" + System.currentTimeMillis());
                            supplierData.put("supplierName", contactPerson);
                        }
                        supplierData.put("contactPerson", contactPerson);
                        supplierData.put("phoneNumber", viewModel.supplierContactNumProperty().get() != null ? viewModel.supplierContactNumProperty().get() : "");
                        supplierData.put("businessAddress", viewModel.supplierAddressProperty().get() != null ? viewModel.supplierAddressProperty().get() : "");
                        deliveryData.put("supplier", supplierData);

                        deliveryData.put("timestamp", System.currentTimeMillis());
                        deliveryData.put("items", viewModel.getDeliveryItems().stream().map(item -> {
                            Map<String, Object> itemData = new HashMap<>();
                            itemData.put("name", item.getItemName() != null ? item.getItemName() : "");
                            itemData.put("quantity", item.getItemQuantity());
                            itemData.put("unit", item.getUnit() != null ? item.getUnit() : "");
                            itemData.put("priority", item.getPriority() != null ? item.getPriority() : "");
                            itemData.put("notes", item.getNotes() != null ? item.getNotes() : "");
                            return itemData;
                        }).collect(Collectors.toList()));

                        System.out.println("Writing to Firestore document ID: " + deliveryId);
                        System.out.println("Delivery data: " + deliveryData);
                        DocumentReference docRef = db.collection("Deliveries").document(deliveryId);
                        ApiFuture<WriteResult> result = docRef.set(deliveryData);
                        WriteResult writeResult = result.get();
                        System.out.println("Firestore write successful at: " + writeResult.getUpdateTime());

                        Platform.runLater(() -> {
                            clearForm();
                            feedbackLabel.setText("Delivery saved successfully!");
                            feedbackLabel.setStyle("-fx-text-fill: green;");
                            System.out.println("END: SubmitButton completed, form cleared");
                        });
                        return;
                    } catch (Exception e) {
                        System.err.println("Firestore write failed (retries left: " + retries + "): " + e.getMessage());
                        retries--;
                        if (retries == 0) {
                            Platform.runLater(() -> {
                                feedbackLabel.setText("Error saving delivery: " + e.getMessage());
                                feedbackLabel.setStyle("-fx-text-fill: red;");
                            });
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        } catch (Exception e) {
            feedbackLabel.setText("Error submitting delivery: " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
            System.err.println("Error in handleSubmitButtonAction: " + e.getMessage());
        }
    }

    private void clearForm() {
        System.out.println("START: Clearing form");
        try {
            viewModel.reset();
            viewModel.deliveryIdProperty().set(generateShortDeliveryId());
            viewModel.orderNumberProperty().set(generateShortOrderNumber());
            supplierComboBox.getSelectionModel().clearSelection();
            supplierComboBox.getEditor().clear();
            ItemName.clear();
            ItemQuantity.clear();
            Unit.getSelectionModel().clearSelection();
            Priority.getSelectionModel().clearSelection();
            Notes.clear();
            DeliveryDate.setValue(null);
            DeliveryTime.clear();
            DeliveryAddress.clear();
            supplierFirstName.clear();
            SupplierLastName.clear();
            supplierContactNum.clear();
            supplierAddress.clear();
            currentPage = 1;
            updateTableViewPage();
            feedbackLabel.setText("");
            System.out.println("END: Form cleared");
        } catch (Exception e) {
            System.err.println("Error clearing form: " + e.getMessage());
        }
    }

    public void shutdown() {
        System.out.println("Shutting down executor");
        executor.shutdown();
    }
}