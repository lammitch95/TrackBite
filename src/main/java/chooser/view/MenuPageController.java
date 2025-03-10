package chooser.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuPageController {

    @FXML
    private GridPane appetizerGrid, lunchGrid, dinnerGrid, dessertGrid, specialsGrid;

    private int appetizerCount = 0, lunchCount = 0, dinnerCount = 0, dessertCount = 0, specialsCount = 0;

    @FXML
    private void initialize() {
        addItemToGrid(appetizerGrid, "Spring Rolls", "/chooser/trackbite/SpringRolls.jpg", 0);
        addItemToGrid(appetizerGrid, "Mozzarella Sticks", "/chooser/trackbite/menu.png", 1);
        addItemToGrid(appetizerGrid, "Garlic Bread", "/chooser/trackbite/menu.png", 2);
        addItemToGrid(appetizerGrid, "Stuffed Mushrooms", "/chooser/trackbite/menu.png", 3);
    }


    @FXML
    private void addAppetizer() {
        openAddItemForm();
    }

    @FXML
    private void addLunch() {
        openAddItemForm();
    }

    @FXML
    private void addDinner() {
        openAddItemForm();
    }

    @FXML
    private void addDessert() {
        openAddItemForm();
    }

    @FXML
    private void addSpecials() {
        openAddItemForm();
    }

    @FXML
    private void openAddItemForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooser/trackbite/AddItemForum.fxml")); // Updated name
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Menu Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AddItemForum.fxml");
        }
    }


    private void addItemToGrid(GridPane grid, String itemName, String imagePath, int index) {
        VBox itemBox = createMenuItemBox(itemName, imagePath, index);

        grid.setHgap(50); // Increased spacing between columns
        grid.setVgap(30); // Increased spacing between rows

        int column = index % 2; // Arrange items in 2 columns
        int row = index / 2; // Expand dynamically into new rows
        grid.add(itemBox, column, row);
    }


    private VBox createMenuItemBox(String itemName, String imagePath, int index) {
        VBox itemBox = new VBox(10);
        itemBox.setAlignment(Pos.TOP_CENTER);
        itemBox.setStyle("-fx-border-color: black; -fx-padding: 20; -fx-background-color: #f9f9f9;");
        itemBox.setPadding(new Insets(20));
        itemBox.setPrefSize(350, 420); // Item box size

        // Image Section (Top Half)
        ImageView itemImage;
        try {
            itemImage = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            System.err.println("Image not found, using placeholder.");
            itemImage = new ImageView(new Image("https://via.placeholder.com/300x150")); // Fallback image
        }
        itemImage.setFitWidth(320);
        itemImage.setFitHeight(180);

        // Details Section (Bottom Half)
        VBox detailsBox = new VBox(8);
        detailsBox.setAlignment(Pos.CENTER);

        // Name Label
        Label nameLabel = new Label(itemName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Editable Price TextField
        TextField priceField = new TextField();
        priceField.setPromptText("Enter Price");
        priceField.setPrefWidth(150);
        priceField.setPrefHeight(30);

        // Editable Description TextField
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter Description");
        descriptionField.setPrefWidth(250);
        descriptionField.setPrefHeight(50);

        // Edit Button (Allows Editing)
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            System.out.println("Editing item: " + itemName);
            priceField.setEditable(true);
            descriptionField.setEditable(true);
        });

        // Delete Button (Removes Item)
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteItem(index));

        // Buttons Section
        HBox buttonBox = new HBox(10, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Add elements to layout
        detailsBox.getChildren().addAll(nameLabel, priceField, descriptionField, buttonBox);
        itemBox.getChildren().addAll(itemImage, detailsBox);

        return itemBox;
    }



    private void deleteItem(int index) {
        System.out.println("Removing item at index: " + index);
        appetizerGrid.getChildren().remove(index);
    }
}
