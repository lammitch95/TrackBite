package chooser.view;

import chooser.viewmodel.SupplierViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SupplierController {

    @FXML
    private TextField supplierID;
    @FXML
    private TextField supplierName;
    @FXML
    private TextField personFirstName;
    @FXML
    private TextField personLastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField websiteLink;
    @FXML
    private TextField businessAddress;
    @FXML
    private TextField warehouseAddress;
    @FXML
    private TextField deliveryArea;
    @FXML
    private Button submitButton;
    @FXML
    private Label feedbackLabel;
    @FXML
    private HBox saveBtn;    // Added for the Save icon
    @FXML
    private HBox addNewBtn;  // Added for the Add New icon
    @FXML
    private HBox deleteBtn;  // Added for the Delete icon (disabled by default)

    private SupplierViewModel viewModel;

    public SupplierController() {
        this.viewModel = new SupplierViewModel();
    }

    @FXML
    public void initialize() {
        // Bind TextField properties to ViewModel properties
        supplierID.textProperty().bindBidirectional(viewModel.supplierIdProperty());
        supplierName.textProperty().bindBidirectional(viewModel.supplierNameProperty());
        personFirstName.textProperty().bindBidirectional(viewModel.personFirstNameProperty());
        personLastName.textProperty().bindBidirectional(viewModel.personLastNameProperty());
        phoneNumber.textProperty().bindBidirectional(viewModel.phoneNumberProperty());
        emailAddress.textProperty().bindBidirectional(viewModel.emailAddressProperty());
        websiteLink.textProperty().bindBidirectional(viewModel.websiteLinkProperty());
        businessAddress.textProperty().bindBidirectional(viewModel.businessAddressProperty());
        warehouseAddress.textProperty().bindBidirectional(viewModel.warehouseAddressProperty());
        deliveryArea.textProperty().bindBidirectional(viewModel.deliveryAreaProperty());

        // Set up button actions for the icons
        saveBtn.setOnMouseClicked(event -> handleSubmitButtonAction()); // Reuse submit logic
        addNewBtn.setOnMouseClicked(event -> handleAddNewAction());
        deleteBtn.setOnMouseClicked(event -> handleDeleteAction());
    }

    @FXML
    public void handleSubmitButtonAction() {
        boolean success = viewModel.onSubmit();
        if (success) {
            feedbackLabel.setText("Supplier data saved successfully!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Failed to save supplier data.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void handleAddNewAction() {
        // Clear all fields for a new supplier entry
        viewModel.clearFields(); // Assumes this method exists in ViewModel
        feedbackLabel.setText("Enter new supplier details.");
        feedbackLabel.setStyle("-fx-text-fill: blue;");
        supplierName.requestFocus();
    }

    private void handleDeleteAction() {
        // Delete the current supplier (assumes method exists in ViewModel)
        boolean success = viewModel.deleteSupplier();
        if (success) {
            feedbackLabel.setText("Supplier deleted successfully!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            viewModel.clearFields();
        } else {
            feedbackLabel.setText("Failed to delete supplier.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }
}