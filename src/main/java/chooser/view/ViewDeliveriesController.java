package chooser.view;

import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.utils.SceneNavigator;
import chooser.viewmodel.TableViewViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ViewDeliveriesController {

    @FXML private TableView<InventoryDelivery> deliveriesTable;
    @FXML private TableColumn<InventoryDelivery, String> supplierCol;
    @FXML private TableColumn<InventoryDelivery, String> deliveryDateCol;
    @FXML private TableColumn<InventoryDelivery, String> itemIdCol;
    @FXML private TableColumn<InventoryDelivery, String> itemNameCol;
    @FXML private TableColumn<InventoryDelivery, String> quantityCol;
    @FXML private TableColumn<InventoryDelivery, String> pricePerUnitCol;
    @FXML private TableColumn<InventoryDelivery, String> expirationDateCol;
    @FXML private TableColumn<InventoryDelivery, String> totalPriceCol;


    @FXML
    private void initialize() {
        // Set up each column properly
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));
        pricePerUnitCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPricePerUnit())));
        totalPriceCol.setCellValueFactory(data -> {
            float total = data.getValue().getQuantity() * data.getValue().getPricePerUnit();
            return new SimpleStringProperty(String.format("%.2f", total));
        });

        expirationDateCol.setCellValueFactory(new PropertyValueFactory<>("expDate"));

        // Load deliveries from Firestore
        List<InventoryDelivery> deliveries = FirestoreUtils.readDeliveriesCollection();
        if (deliveries != null && !deliveries.isEmpty()) {
            deliveriesTable.setItems(FXCollections.observableList(deliveries));
        } else {
            System.out.println("No deliveries found or failed to load deliveries.");
        }
    }

    @FXML
    private void backToMainSelected(ActionEvent event) {
        SceneNavigator.switchScene("Homepage", "TrackBite/Homepage", -1, -1, true);
    }
}