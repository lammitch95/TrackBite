package chooser.view;

import chooser.viewmodel.NewDeliveryViewModel;
//import com.google.cloud.firestore.DocumentSnapshot;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.QuerySnapshot;
//import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public class NewDeliveryFormController {

    //pane
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

    //Buttons
    @FXML
    public HBox saveBtn1;
    @FXML
    public HBox addNewBtn1;
    @FXML
    public ImageView saveBtnImageView1;
    @FXML
    public HBox deleteBtn1;

    //Supplier
    @FXML
    public HBox SupplierHBox;
    @FXML
    public TextField supplierSearch;
    @FXML
    public Button SupplierSearchButton;
    @FXML
    public HBox SiupplierNameHBox;
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


    //Delivery text
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

    //Delivery Box
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

    @FXML
    public void initialize() {
        db = FirestoreClient.getFirestore();


// for later
//        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn1,deleteBtn1};
//        for (HBox hbox : hboxList) {
//            hbox.getStyleClass().add("tableview-hover-effect");
//        }
//
//        categoryComboBox.getItems().clear();
//        categoryComboBox.getSelectionModel().clearSelection();
//        categoryComboBox.setValue(null);
//
//        currencyComboBox.getItems().clear();
//        currencyComboBox.getSelectionModel().clearSelection();
//        currencyComboBox.setValue(null);

        // --- bind the form fields ---
        DeliveryID.textProperty().bindBidirectional(viewModel.deliveryIdProperty());
        OrderNumber.textProperty().bindBidirectional(viewModel.orderNumberProperty());
        DeliveryDate.valueProperty().bindBidirectional(viewModel.deliveryDateProperty());
        DeliveryTime.textProperty().bindBidirectional(viewModel.deliveryTimeProperty());
        DeliveryAddress.textProperty().bindBidirectional(viewModel.deliveryAddressProperty());
        ItemName.textProperty().bindBidirectional(viewModel.itemNameProperty());

        // quantity needs conversion between String <-> Integer
        Bindings.bindBidirectional(
                ItemQuantity.textProperty(),
                viewModel.itemQuantityProperty(),
                new NumberStringConverter()
        );
        Unit.valueProperty().bindBidirectional(viewModel.unitProperty());
        Category.valueProperty().bindBidirectional(viewModel.categoryProperty());
        Priority.valueProperty().bindBidirectional(viewModel.priorityProperty());
        Notes.textProperty().bindBidirectional(viewModel.notesProperty());

        // supplier fields
        supplierSearch.textProperty().bindBidirectional(viewModel.supplierSearchProperty());
        supplierFirstName.textProperty().bindBidirectional(viewModel.supplierFirstNameProperty());
        SupplierLastName.textProperty().bindBidirectional(viewModel.supplierLastNameProperty());
        supplierContactNum.textProperty().bindBidirectional(viewModel.supplierContactNumProperty());
        supplierAddress.textProperty().bindBidirectional(viewModel.supplierAddressProperty());

//        // === Button State ===
//        // Disable submit until form is valid and changed
       submitButton.disableProperty().bind(viewModel.allowSaveProperty().not());

        // Optionally, initialize any ComboBox items
        Unit.getItems().addAll( "Teaspoon (tsp)", "Tablespoon (tbsp)","Fluid ounce (fl oz)", "Cup (c)", "Pint (pt)", "Quart (qt)", "Gallon (gal)", "Liter (L)",
                                     "Milliliter (mL)", "Ounce (oz)", "Pound (lb)", "Gram (g)", "Kilogram (kg)", "Whole", "Slice", "Clove", "Stick", "Dash, Pinch, Smidgen");
        Category.getItems().addAll("Appetizers", "Entrées", "Sides", "Beverages", "Desserts", "Kids Menu", "Specials");
        Priority.getItems().addAll("Low", "Medium", "High");
        DeliveryTimeUnit.getItems().addAll("AM", "PM");
    }

    @FXML
    public void handleSubmitButtonAction() {
        boolean ok = viewModel.onSubmit();
        if (ok) {
            feedbackLabel.setText("Delivery saved successfully!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Validation failed – please check your inputs.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
