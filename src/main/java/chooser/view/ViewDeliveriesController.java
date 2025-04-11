package chooser.view;

import chooser.database.FirestoreUtils;
import chooser.model.InventoryDelivery;
import chooser.utils.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ViewDeliveriesController {
    @FXML private TableView<InventoryDelivery> deliveriesTable;
    @FXML
    private TableColumn<InventoryDelivery, String> supplierCol;
    @FXML private TableColumn<InventoryDelivery, String> deliveryDateCol;
    @FXML private TableColumn<InventoryDelivery, String> itemIdCol;
    @FXML private TableColumn<InventoryDelivery, String> itemNameCol;
    @FXML private TableColumn<InventoryDelivery, String> quantityCol;
    @FXML private TableColumn<InventoryDelivery, String> pricePerUnitCol;
    @FXML private TableColumn<InventoryDelivery, String> totalPriceCol;
    @FXML private TableColumn<InventoryDelivery, String> expirationDateCol;

    @FXML
    private void initialize() {
        supplierCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
        deliveryDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDeliveryDate().toString()));
        itemIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemId()));
        itemNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemName()));
        quantityCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        pricePerUnitCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPricePerUnit())));
        totalPriceCol.setCellValueFactory(data -> {
            float total = data.getValue().getQuantity() * data.getValue().getPricePerUnit();
            return new SimpleStringProperty(String.format("%.2f", total));
        });
        expirationDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpDate().toString()));

        List<InventoryDelivery> deliveries = FirestoreUtils.readDeliveriesCollection(); // ← new helper method needed
        deliveriesTable.setItems(FXCollections.observableList(deliveries));
    }

    @FXML
    private void backToMainSelected(ActionEvent event) {
        SceneNavigator.switchScene(
                "Homepage",
                "TrackBite/Homepage",
                -1,
                -1,
                true
        );
    }
}

