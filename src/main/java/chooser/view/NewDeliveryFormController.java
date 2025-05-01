package chooser.view;

import chooser.viewmodel.NewDeliveryViewModel;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import javafx.util.converter.NumberStringConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewDeliveryFormController {

    // Pane
    @FXML
    public Label DeliveryInfoLabel;
    @FXML
    public BorderPane DeliveryFormPane;
    @FXML
    public VBox DeliveryDetailsVbox;
    @FXML
    public VBox DeliveryListVbox;
    @FXML
    public VBox SupplierDetailsVbox;

    // Buttons
    @FXML
    public HBox saveBtn1;
    @FXML
    public HBox addNewBtn1;
    @FXML
    public ImageView saveBtnImageView1;
    @FXML
    public HBox deleteBtn1;

    // Supplier
    @FXML
    public HBox SupplierHBox;
    @FXML
    public TextField supplierSearch;
    @FXML
    public HBox SupplierNameHBox;
    @FXML
    public TextField supplierFirstName;
    @FXML
    public TextField SupplierLastName;
    @FXML
    public HBox SupplierNumberHBox;
    @FXML
    public TextField supplierContactNum;
    @FXML
    public HBox SupplierAddressHBox;
    @FXML
    public TextField supplierAddress;

    // Delivery text
    @FXML
    private TextField DeliveryID;
    @FXML
    private TextField OrderNumber;
    @FXML
    private DatePicker DeliveryDate;
    @FXML
    private TextField DeliveryTime;
    @FXML
    private TextField DeliveryAddress;
    @FXML
    private TextField ItemName;
    @FXML
    private TextField ItemQuantity;
    @FXML
    public ComboBox<String> DeliveryTimeUnit;
    @FXML
    private ComboBox<String> Unit;
    @FXML
    private ComboBox<String> Category;
    @FXML
    private ComboBox<String> Priority;
    @FXML
    private TextArea Notes;
    @FXML
    private Button submitButton;
    @FXML
    private Label feedbackLabel;

    // Delivery Box
    @FXML
    public HBox DeliveryIDHbox;
    @FXML
    public HBox OrderNumHbox;
    @FXML
    public HBox DeliveryDateHbox;
    @FXML
    public HBox DeliveryTimeHbox;
    @FXML
    public HBox DeliveryAddressHbox;
    @FXML
    public HBox ItemQualityHbox;
    @FXML
    public HBox ItemNameHbox;
    @FXML
    public HBox CategoryHbox;
    @FXML
    public HBox PriorityHbox;
    @FXML
    public HBox NotesHbox;

    private final NewDeliveryViewModel viewModel = new NewDeliveryViewModel();
    private Firestore db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    public void initialize() {
        db = FirestoreClient.getFirestore();

        // Bind the form fields
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
        Unit.valueProperty().bindBidirectional(viewModel.unitProperty());
        Category.valueProperty().bindBidirectional(viewModel.categoryProperty());
        Priority.valueProperty().bindBidirectional(viewModel.priorityProperty());
        Notes.textProperty().bindBidirectional(viewModel.notesProperty());

        // Supplier fields
        supplierSearch.textProperty().bindBidirectional(viewModel.supplierSearchProperty());
        supplierFirstName.textProperty().bindBidirectional(viewModel.supplierFirstNameProperty());
        SupplierLastName.textProperty().bindBidirectional(viewModel.supplierLastNameProperty());
        supplierContactNum.textProperty().bindBidirectional(viewModel.supplierContactNumProperty());
        supplierAddress.textProperty().bindBidirectional(viewModel.supplierAddressProperty());

        // Button state
        submitButton.disableProperty().bind(viewModel.allowSaveProperty().not());

        // Initialize ComboBox items
        Unit.getItems().addAll("Teaspoon (tsp)", "Tablespoon (tbsp)", "Fluid ounce (fl oz)", "Cup (c)", "Pint (pt)", "Quart (qt)", "Gallon (gal)", "Liter (L)",
                "Milliliter (mL)", "Ounce (oz)", "Pound (lb)", "Gram (g)", "Kilogram (kg)", "Whole", "Slice", "Clove", "Stick", "Dash, Pinch, Smidgen");
        Category.getItems().addAll("Appetizers", "Entrées", "Sides", "Beverages", "Desserts", "Kids Menu", "Specials");
        Priority.getItems().addAll("Low", "Medium", "High");
        DeliveryTimeUnit.getItems().addAll("AM", "PM");

        // Add listener for supplier search
        supplierSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                searchSupplier(newValue.trim());
            } else {
                clearSupplierFields();
            }
        });
    }

    private void searchSupplier(String supplierName) {
        executor.submit(() -> {
            try {
                // Query Firestore for supplier by name
                QueryDocumentSnapshot doc = db.collection("Suppliers")
                        .whereEqualTo("supplierName", supplierName)
                        .limit(1)
                        .get()
                        .get()
                        .getDocuments()
                        .stream()
                        .findFirst()
                        .orElse(null);

                Platform.runLater(() -> {
                    if (doc != null) {
                        // Assuming Firestore document has these fields
                        String contactPerson = doc.getString("contactPerson");
                        String[] names = contactPerson != null ? contactPerson.split(" ", 2) : new String[]{"", ""};
                        viewModel.supplierFirstNameProperty().set(names[0]);
                        viewModel.supplierLastNameProperty().set(names.length > 1 ? names[1] : "");
                        viewModel.supplierContactNumProperty().set(doc.getString("phoneNumber"));
                        viewModel.supplierAddressProperty().set(doc.getString("warehouseAddress")); // Changed from deliveryArea
                    } else {
                        clearSupplierFields();
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    feedbackLabel.setText("Error searching supplier: " + e.getMessage());
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                });
            }
        });
    }

    private void clearSupplierFields() {
        viewModel.supplierFirstNameProperty().set("");
        viewModel.supplierLastNameProperty().set("");
        viewModel.supplierContactNumProperty().set("");
        viewModel.supplierAddressProperty().set("");
    }

    @FXML
    public void handleSubmitButtonAction() {
        boolean ok = viewModel.onSubmit();
        if (ok) {
            feedbackLabel.setText("Delivery saved successfully to Firestore!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Failed to save delivery to Firestore. Check inputs or connection.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}