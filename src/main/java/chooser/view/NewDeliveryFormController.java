package chooser.view;

import chooser.model.Suppliers;
import chooser.viewmodel.NewDeliveryViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NewDeliveryFormController {

    // All FXML injections remain exactly the same (keeping all your original UI components)
    @FXML public Label DeliveryInfoLabel;
    @FXML public BorderPane DeliveryFormPane;
    @FXML public VBox DeliveryDetailsVbox;
    @FXML public VBox DeliveryListVbox;
    @FXML public VBox SupplierDetailsVbox;
    @FXML public HBox saveBtn1;
    @FXML public HBox addNewBtn1;
    @FXML public ImageView saveBtnImageView1;
    @FXML public HBox deleteBtn1;
    @FXML public HBox SupplierHBox;
    @FXML public ComboBox<Suppliers> supplierComboBox;
    @FXML public HBox SupplierNameHBox;
    @FXML public TextField supplierFirstName;
    @FXML public TextField SupplierLastName;
    @FXML public HBox SupplierNumberHBox;
    @FXML public TextField supplierContactNum;
    @FXML public HBox SupplierAddressHBox;
    @FXML public TextField supplierAddress;
    @FXML private TextField DeliveryID;
    @FXML private TextField OrderNumber;
    @FXML private DatePicker DeliveryDate;
    @FXML private TextField DeliveryTime;
    @FXML private TextField DeliveryAddress;
    @FXML private TextField ItemName;
    @FXML private TextField ItemQuantity;
    @FXML public ComboBox<String> DeliveryTimeUnit;
    @FXML private ComboBox<String> Unit;
    @FXML private ComboBox<String> Category;
    @FXML private ComboBox<String> Priority;
    @FXML private TextArea Notes;
    @FXML private Button submitButton;
    @FXML private Label feedbackLabel;
    @FXML public HBox DeliveryIDHbox;
    @FXML public HBox OrderNumHbox;
    @FXML public HBox DeliveryDateHbox;
    @FXML public HBox DeliveryTimeHbox;
    @FXML public HBox DeliveryAddressHbox;
    @FXML public HBox ItemQualityHbox;
    @FXML public HBox ItemNameHbox;
    @FXML public HBox CategoryHbox;
    @FXML public HBox PriorityHbox;
    @FXML public HBox NotesHbox;

    private final NewDeliveryViewModel viewModel = new NewDeliveryViewModel();
    private Firestore db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean updatingSupplierFields = false;
    private static int deliveryCounter = 1;
    private static int orderCounter = 1;
    private final Random random = new Random();

    @FXML
    public void initialize() {
        db = FirestoreClient.getFirestore();

        // Initialize with auto-generated IDs
        viewModel.deliveryIdProperty().set(generateShortDeliveryId());
        viewModel.orderNumberProperty().set(generateShortOrderNumber());

        // Bind all form fields
        DeliveryID.textProperty().bindBidirectional(viewModel.deliveryIdProperty());
        OrderNumber.textProperty().bindBidirectional(viewModel.orderNumberProperty());
        DeliveryDate.valueProperty().bindBidirectional(viewModel.deliveryDateProperty());
        DeliveryTime.textProperty().bindBidirectional(viewModel.deliveryTimeProperty());
        DeliveryAddress.textProperty().bindBidirectional(viewModel.deliveryAddressProperty());
        ItemName.textProperty().bindBidirectional(viewModel.itemNameProperty());

        // Quantity needs conversion between String <-> Integer
        Bindings.bindBidirectional(
                ItemQuantity.textProperty(),
                viewModel.itemQuantityProperty(),
                new NumberStringConverter()
        );

        // Bind ComboBox values
        Unit.valueProperty().bindBidirectional(viewModel.unitProperty());
        Category.valueProperty().bindBidirectional(viewModel.categoryProperty());
        Priority.valueProperty().bindBidirectional(viewModel.priorityProperty());
        Notes.textProperty().bindBidirectional(viewModel.notesProperty());

        // Bind supplier fields
        supplierFirstName.textProperty().bindBidirectional(viewModel.supplierFirstNameProperty());
        SupplierLastName.textProperty().bindBidirectional(viewModel.supplierLastNameProperty());
        supplierContactNum.textProperty().bindBidirectional(viewModel.supplierContactNumProperty());
        supplierAddress.textProperty().bindBidirectional(viewModel.supplierAddressProperty());

        // Button state
        submitButton.disableProperty().bind(viewModel.allowSaveProperty().not());

        // Initialize ComboBox items
        Unit.getItems().addAll(
                "Teaspoon (tsp)", "Tablespoon (tbsp)", "Fluid ounce (fl oz)",
                "Cup (c)", "Pint (pt)", "Quart (qt)", "Gallon (gal)", "Liter (L)",
                "Milliliter (mL)", "Ounce (oz)", "Pound (lb)", "Gram (g)",
                "Kilogram (kg)", "Whole", "Slice", "Clove", "Stick",
                "Dash, Pinch, Smidgen"
        );

        Category.getItems().addAll(
                "Appetizers", "Entrées", "Sides", "Beverages",
                "Desserts", "Kids Menu", "Specials"
        );

        Priority.getItems().addAll("Low", "Medium", "High");
        DeliveryTimeUnit.getItems().addAll("AM", "PM");

        // Setup supplier ComboBox with fixed selection handling
        setupSupplierComboBox();
    }

    private String generateShortDeliveryId() {
        return "DEL-" + String.format("%03d", deliveryCounter++);
    }

    private String generateShortOrderNumber() {
        return "ORD-" + String.format("%03d", orderCounter++);
    }

    private void setupSupplierComboBox() {
        ObservableList<Suppliers> suppliers = FXCollections.observableArrayList();
        supplierComboBox.setItems(suppliers);
        supplierComboBox.setEditable(true);

        // Fetch suppliers from Firestore
        executor.submit(() -> {
            try {
                List<Suppliers> supplierList = db.collection("Suppliers")
                        .get()
                        .get()
                        .getDocuments()
                        .stream()
                        .map(this::documentToSuppliers)
                        .collect(Collectors.toList());
                Platform.runLater(() -> suppliers.setAll(supplierList));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    feedbackLabel.setText("Error fetching suppliers: " + e.getMessage());
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                });
            }
        });

        // Configure ComboBox display
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

        // Handle selection changes
        supplierComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSupplier, newSupplier) -> {
            if (!updatingSupplierFields) {
                if (newSupplier != null) {
                    updateSupplierFields(newSupplier);
                } else if (oldSupplier != null && supplierComboBox.getEditor().getText().isEmpty()) {
                    clearSupplierFields();
                }
            }
        });

        // Configure filtering
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

        // Handle Enter key
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
    }

    private void updateSupplierFields(Suppliers supplier) {
        try {
            updatingSupplierFields = true;

            String contactPerson = supplier.getContactPerson();
            String[] names = contactPerson != null && !contactPerson.trim().isEmpty() ?
                    contactPerson.trim().split("\\s+", 2) : new String[]{"", ""};

            viewModel.supplierFirstNameProperty().set(names[0]);
            viewModel.supplierLastNameProperty().set(names.length > 1 ? names[1] : "");
            viewModel.supplierContactNumProperty().set(supplier.getPhoneNumber() != null ?
                    supplier.getPhoneNumber() : "");
            viewModel.supplierAddressProperty().set(supplier.getWarehouseAddress() != null ?
                    supplier.getWarehouseAddress() : "");

            Platform.runLater(() -> {
                supplierComboBox.getEditor().setText(supplier.getSupplierName());
            });
        } finally {
            updatingSupplierFields = false;
        }
    }

    private void clearSupplierFields() {
        viewModel.supplierFirstNameProperty().set("");
        viewModel.supplierLastNameProperty().set("");
        viewModel.supplierContactNumProperty().set("");
        viewModel.supplierAddressProperty().set("");
    }

    @FXML
    public void handleSubmitButtonAction() {
        try {
            // Create delivery data map
            Map<String, Object> deliveryData = new HashMap<>();
            deliveryData.put("deliveryId", viewModel.deliveryIdProperty().get());
            deliveryData.put("orderNumber", viewModel.orderNumberProperty().get());
            deliveryData.put("deliveryDate", viewModel.deliveryDateProperty().get() != null ?
                    viewModel.deliveryDateProperty().get().toString() : "");
            deliveryData.put("deliveryTime", viewModel.deliveryTimeProperty().get());
            deliveryData.put("deliveryAddress", viewModel.deliveryAddressProperty().get());
            deliveryData.put("itemName", viewModel.itemNameProperty().get());
            deliveryData.put("itemQuantity", viewModel.itemQuantityProperty().get());
            deliveryData.put("unit", viewModel.unitProperty().get());
            deliveryData.put("category", viewModel.categoryProperty().get());
            deliveryData.put("priority", viewModel.priorityProperty().get());
            deliveryData.put("notes", viewModel.notesProperty().get());
            deliveryData.put("supplierFirstName", viewModel.supplierFirstNameProperty().get());
            deliveryData.put("supplierLastName", viewModel.supplierLastNameProperty().get());
            deliveryData.put("supplierContactNum", viewModel.supplierContactNumProperty().get());
            deliveryData.put("supplierAddress", viewModel.supplierAddressProperty().get());
            deliveryData.put("timestamp", System.currentTimeMillis());

            // Write to Firestore
            DocumentReference docRef = db.collection("Deliveries").document(viewModel.deliveryIdProperty().get());
            ApiFuture<WriteResult> result = docRef.set(deliveryData);

            result.get(); // Wait for completion

            // Clear form and show success
            Platform.runLater(() -> {
                clearForm();
                feedbackLabel.setText("Delivery saved successfully!");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                feedbackLabel.setText("Error saving delivery: " + e.getMessage());
                feedbackLabel.setStyle("-fx-text-fill: red;");
            });
        }
    }

    private void clearForm() {
        // Generate new IDs
        viewModel.deliveryIdProperty().set(generateShortDeliveryId());
        viewModel.orderNumberProperty().set(generateShortOrderNumber());

        // Clear all other fields
        viewModel.deliveryDateProperty().set(null);
        viewModel.deliveryTimeProperty().set("");
        viewModel.deliveryAddressProperty().set("");
        viewModel.itemNameProperty().set("");
        viewModel.itemQuantityProperty().set(0);
        viewModel.unitProperty().set("");
        viewModel.categoryProperty().set("");
        viewModel.priorityProperty().set("");
        viewModel.notesProperty().set("");
        viewModel.supplierFirstNameProperty().set("");
        viewModel.supplierLastNameProperty().set("");
        viewModel.supplierContactNumProperty().set("");
        viewModel.supplierAddressProperty().set("");

        // Reset supplier selection
        supplierComboBox.getSelectionModel().clearSelection();
        supplierComboBox.getEditor().clear();

        // Reset focus
        DeliveryDate.requestFocus();
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

    public void shutdown() {
        executor.shutdown();
    }
}