package chooser.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import chooser.model.MenuItem;
import chooser.database.FirestoreUtils;

//import com.google.firebase.database.*;
import javafx.application.Platform;

public class MenuPageController {

    private MenuItem selectedMenuItem; // Stores the currently selected item
    private VBox selectedMenuItemVBox; // Stores the corresponding VBox UI component

    @FXML
    private Button editItemButton, deleteItemButton;

    @FXML
    private VBox itemContainer; // The menu item container

    // List to store all menu items
    private List<VBox> allItems = new ArrayList<>();

    //private DatabaseReference databaseRef;


    @FXML
    private void initialize() {
        itemContainer.getChildren().clear(); // Clear existing items
    }

    @FXML
    private void openAddItemForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooser/trackbite/NewMenuItem.fxml"));
            Parent root = loader.load();

            // Get the controller and link it to this page
            MenuForumController controller = loader.getController();
            controller.setMenuPageController(this);

            // Open the form as a new window
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Menu Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading NewMenuItem.fxml");
        }
    }

    @FXML
    private void openEditItemForm() {
        if (selectedMenuItem == null) {
            showAlert("No item selected", "Please select an item to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooser/trackbite/NewMenuItem.fxml"));
            Parent root = loader.load();

            // Get the controller and pass data
            MenuForumController controller = loader.getController();
            controller.loadExistingMenuItem(selectedMenuItem);

            // Open edit form in a new window
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Menu Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshMenuList(); // Reload menu items after editing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditMenuItem(MenuItem item, VBox itemBox) {
        if (item == null) {
            showAlert("No item selected", "Please select an item to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooser/trackbite/NewMenuItem.fxml"));
            Parent root = loader.load();

            // Get controller and pass data
            MenuForumController controller = loader.getController();
            controller.loadExistingMenuItem(item); // Now works correctly!

            // Open edit form in a new window
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Menu Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshMenuList(); // Reload menu items after editing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteItem() {
        if (selectedMenuItem == null) {
            showAlert("No item selected", "Please select an item to delete.");
            return;
        }

        // Confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            FirestoreUtils.deleteDoc("NewMenuItems", selectedMenuItem.getMenuId()); // Delete from Firestore
            itemContainer.getChildren().remove(selectedMenuItemVBox); // Remove from UI
            showAlert("Success", "Item deleted successfully.");
        }
    }




    @FXML
    private void handleDeleteMenuItem(MenuItem item, VBox itemBox) {
        if (item == null) {
            showAlert("No item selected", "Please select an item to delete.");
            return;
        }

        // Confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            FirestoreUtils.deleteDoc("NewMenuItems", item.getMenuId()); // Delete from database
            itemContainer.getChildren().remove(itemBox); // Remove from UI
            showAlert("Success", "Item deleted successfully.");
        }
    }


    // Refresh the menu list by reloading all items
    private void refreshMenuList() {
        itemContainer.getChildren().clear(); // Clear current menu items

        // Fetch updated items from Firestore (assuming FirestoreUtils has a readAllDocs method)
        List<MenuItem> updatedMenuItems = FirestoreUtils.readAllDocs("NewMenuItems");

        for (MenuItem item : updatedMenuItems) {
            VBox menuItemBox = createMenuItemBox(item); // Create updated UI components
            itemContainer.getChildren().add(menuItemBox);
        }
    }



    private VBox createMenuItemBox(MenuItem menuItem) { // Now uses chooser.model.MenuItem
        VBox itemBox = new VBox(10);
        itemBox.setAlignment(Pos.TOP_CENTER);
        itemBox.setStyle("-fx-border-color: black; -fx-padding: 20; -fx-background-color: #f9f9f9;");
        itemBox.setPadding(new Insets(20));
        itemBox.setPrefSize(900, 250);
        itemBox.setUserData(menuItem); // Store menu item as user data

        // Image Section
        ImageView itemImage = new ImageView();
        if (menuItem.getImageUrl() != null) {
            itemImage.setImage(new Image(menuItem.getImageUrl()));
        }
        itemImage.setFitWidth(320);
        itemImage.setFitHeight(180);

        // Details Section
        Label nameLabel = new Label(menuItem.getItemName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label priceLabel = new Label("Price: " + menuItem.getPrice());

        Label descriptionLabel = new Label("Description: " + menuItem.getDescription());

        // Buttons for Edit and Delete
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> handleEditMenuItem(menuItem, itemBox));

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> handleDeleteMenuItem(menuItem, itemBox));

        HBox buttonBox = new HBox(10, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        itemBox.getChildren().addAll(itemImage, nameLabel, priceLabel, descriptionLabel, buttonBox);

        // Handle selection click
        itemBox.setOnMouseClicked(event -> {
            selectedMenuItem = menuItem; // Now correctly assigns chooser.model.MenuItem
            selectedMenuItemVBox = itemBox;
        });

        return itemBox;
    }



    // Category Filtering Methods
    @FXML
    private void showAppetizers() {
        filterItemsByCategory("Appetizers");
    }

    @FXML
    private void showLunch() {
        filterItemsByCategory("Lunch");
    }

    @FXML
    private void showDinner() {
        filterItemsByCategory("Dinner");
    }

    @FXML
    private void showDesserts() {
        filterItemsByCategory("Desserts");
    }

    @FXML
    private void showSpecials() {
        filterItemsByCategory("Specials");
    }

    @FXML
    private void showDrinks() {
        filterItemsByCategory("Drinks");
    }

    private void filterItemsByCategory(String category) {
        itemContainer.getChildren().clear();
        for (VBox item : allItems) {
            if (item.getUserData().equals(category)) {
                itemContainer.getChildren().add(item);
            }
        }
    }
    // Helper method to display alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
