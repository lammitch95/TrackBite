package chooser.view;

import chooser.utils.TableViewUtils;
import chooser.viewmodel.SupplierTableViewModel;
import chooser.viewmodel.SupplierViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SupplierTableController {

    @FXML
    private TableView<SupplierTableViewModel.SupplierData> supplierTable;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> supplierIdColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> supplierNameColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> contactPersonColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> phoneNumberColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> emailAddressColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> websiteLinkColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> businessAddressColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> warehouseAddressColumn;
    @FXML
    private TableColumn<SupplierTableViewModel.SupplierData, String> deliveryAreaColumn;
    @FXML
    private Label statusLabel;

    private SupplierTableViewModel viewModel;

    public SupplierTableController() {
        this.viewModel = new SupplierTableViewModel();
    }

    @FXML
    public void initialize() {
        // Bind columns to SupplierData properties
        supplierIdColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty());
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        contactPersonColumn.setCellValueFactory(cellData -> cellData.getValue().contactPersonProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        emailAddressColumn.setCellValueFactory(cellData -> cellData.getValue().emailAddressProperty());
        websiteLinkColumn.setCellValueFactory(cellData -> cellData.getValue().websiteLinkProperty());
        businessAddressColumn.setCellValueFactory(cellData -> cellData.getValue().businessAddressProperty());
        warehouseAddressColumn.setCellValueFactory(cellData -> cellData.getValue().warehouseAddressProperty());
        deliveryAreaColumn.setCellValueFactory(cellData -> cellData.getValue().deliveryAreaProperty());

        // Bind table data to view model
        supplierTable.setItems(viewModel.getSupplierList());

        // Fetch supplier data on initialization
        fetchSupplierData();

    }

    private void fetchSupplierData() {
        viewModel.fetchSuppliers();
        if (!viewModel.getSupplierList().isEmpty()) {
            statusLabel.setText("Data loaded successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("No data found or error occurred.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
