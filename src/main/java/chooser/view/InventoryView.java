package chooser.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InventoryView {

    @FXML
    private TableView<InventoryItem> tableView;

    @FXML
    private TableColumn<InventoryItem, String> itemIdColumn;

    @FXML
    private TableColumn<InventoryItem, String> itemNameColumn;

    @FXML
    private TableColumn<InventoryItem, String> unitColumn;

    @FXML
    private TableColumn<InventoryItem, String> categoryColumn;

    @FXML
    private TableColumn<InventoryItem, Float> quantityColumn;

    @FXML
    private TableColumn<InventoryItem, String> stockStatusColumn;

    @FXML
    private Button backButton;

    private Stage primaryStage;

    private static ObservableValue<String> call(TableColumn.CellDataFeatures<InventoryItem, Float> cellData) {
        return cellData.getValue().unitProperty();
    }

    @FXML
    public void initialize() {
        // Set cell value factories to bind to properties of InventoryItem
        itemIdColumn.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty());
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());



        // Set the stock status column based on the quantity
        stockStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getStockStatus(cellData.getValue())));

        // Populate the table with inventory items
        tableView.getItems().setAll(InventoryMenu.getAllInventoryItems());

        // Setup the back button action
        backButton.setOnAction(e -> showMainMenu());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Return stock status based on the item's quantity
    private String getStockStatus(InventoryItem item) {
        float quantity = item.getQuantityTotal();
        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity < 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

    private void showMainMenu() {
        InventoryMenu mainMenuController = new InventoryMenu();
        mainMenuController.setPrimaryStage(primaryStage);
    }

    public void showInventoryView(Stage primaryStage) {
    }
}