package chooser.view;

import chooser.viewmodel.MenuFormViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import chooser.model.MenuItem;

public class MenuForumController {

    private String menuId; // Store the auto-generated ID
    private static int idCounter = 0; // Static counter for unique IDs

    @FXML
    private Label menuIdLabel;
    @FXML
    private TextField menuItemNameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Spinner<Integer> ingredientAmountSpinner;
    @FXML
    private TextField ingredientInputField;
    @FXML
    private ComboBox<String> unitSuggestionComboBox;
    @FXML
    private ListView<String> ingredientListView;
    @FXML
    private Button addIngredientButton;
    @FXML
    private Button uploadImageButton;
    @FXML
    private ImageView menuItemImageView;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private ObservableList<String> ingredientsList;
    private MenuFormViewModel menuforumvm;
    private MenuPageController menuPageController;

    @FXML
    public void initialize() {
        menuforumvm = new MenuFormViewModel();
        menuItemNameField.textProperty().bindBidirectional(menuforumvm.itemNameProperty());
        priceField.textProperty().bindBidirectional(menuforumvm.priceProperty());
        //ingredientInputField.textProperty().bindBidirectional(menuforumvm.ingredientInputProperty());

        descriptionField.textProperty().bindBidirectional(menuforumvm.descriptionProperty());
        menuforumvm.categoryProperty().bindBidirectional(categoryComboBox.valueProperty());



        ingredientsList = FXCollections.observableArrayList();
        ingredientListView.setItems(ingredientsList);

        // Set default value for unitSuggestionComboBox
        unitSuggestionComboBox.setValue("Pieces");

        // Initialize the Spinner with a value factory if not set in FXML
        if (ingredientAmountSpinner.getValueFactory() == null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
            ingredientAmountSpinner.setValueFactory(valueFactory);
        }

        // Generate and display the initial menu ID
        generateMenuId();
    }


    private void generateMenuId() {
        idCounter++;
        menuId = "MENU-" + String.format("%04d", idCounter); // e.g., MENU-0001
        menuIdLabel.setText(menuId); // Update the Label with the new ID
    }

    @FXML
    private void handleAddIngredient() {
        // Get values from input fields
        int amount = ingredientAmountSpinner.getValue();
        String ingredient = ingredientInputField.getText().trim();
        String unit = unitSuggestionComboBox.getValue();

        // Validate input
        if (ingredient.isEmpty() || unit == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an ingredient and select a unit.");
            alert.showAndWait();
            return;
        }

        // Combine amount, ingredient, and unit into a single string
        String ingredientEntry = amount + " " + unit + " of " + ingredient;

        // Add to the ListView via the ObservableList
        ingredientsList.add(ingredientEntry);


        ingredientInputField.clear();
        ingredientAmountSpinner.getValueFactory().setValue(1);
        unitSuggestionComboBox.setValue("Pieces");
    }

    public void setMenuPageController(MenuPageController controller) {
        this.menuPageController = controller;
    }

    public void loadExistingMenuItem(MenuItem item) {
        if (item == null) {
            return;
        }

        menuIdLabel.setText(item.getMenuId());
        menuItemNameField.setText(item.getItemName());
        descriptionField.setText(item.getDescription());
        priceField.setText(item.getPrice());
        categoryComboBox.setValue(item.getCategory());
        ingredientsList.setAll(item.getIngredients());

        // Load image if available
        if (item.getImageUrl() != null) {
            menuItemImageView.setImage(new Image(item.getImageUrl()));
        }
    }



    @FXML
    private void handleSubmit() {
        // Validate required fields
        if (menuItemNameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submission Error");
            alert.setHeaderText(null);
            alert.setContentText("Item Name and Price are required fields.");
            alert.showAndWait();
            return;
        }

        // Log the submitted data (replace with your actual save logic)

        // Optionally show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Menu item submitted successfully with ID: " + menuId);
        alert.showAndWait();

        // Generate a new ID for the next entry and update the label
        generateMenuId();
        boolean isiteamcreated  = menuforumvm.onSubmit();
    }

    @FXML
    private void handleCancel() {
        // Reset all fields
        menuItemNameField.clear();
        descriptionField.clear();
        priceField.clear();
        categoryComboBox.setValue(null);
        ingredientsList.clear();
        ingredientInputField.clear();
        ingredientAmountSpinner.getValueFactory().setValue(1);
        unitSuggestionComboBox.setValue("Pieces");
        menuItemImageView.setImage(null);

        // Generate a new menu ID for the next entry and update the label
        generateMenuId();
    }

    @FXML
    private void handleImageUpload() {
        // Create a FileChooser for selecting an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Menu Item Image");

        // Restrict to image file types
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(menuItemImageView.getScene().getWindow());

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Convert the file to an Image object
                String fileUri = selectedFile.toURI().toString();
                Image image = new Image(fileUri);

                // Check if the image loaded successfully
                if (image.isError()) {
                    throw new Exception("Failed to load the image.");
                }

                // Set the image in the ImageView
                menuItemImageView.setImage(image);

                // Ensure the image fits within the defined bounds (150x150) while preserving aspect ratio
                menuItemImageView.setFitWidth(150);
                menuItemImageView.setFitHeight(150);
                menuItemImageView.setPreserveRatio(true);

            } catch (Exception e) {
                // Show an error message if something goes wrong
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Image Upload Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to upload the image: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}