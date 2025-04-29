package chooser.view;

import chooser.viewmodel.DeliveryTableViewModel;
import chooser.viewmodel.DeliveryTableViewModel.DeliveryRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DeliveryTableViewController {

    @FXML
    private TableView<DeliveryRecord> deliveryTable;
    @FXML
    private TableColumn<DeliveryRecord, String> deliveryIDColumn;
    @FXML
    private TableColumn<DeliveryRecord, String> deliveryDateColumn;
    @FXML
    private TableColumn<DeliveryRecord, String> supplierColumn;
    @FXML
    private TableColumn<DeliveryRecord, String> deliveryAddressColumn;
    @FXML
    private Button exportExcelButton;
    @FXML
    private Button refreshButton;

    private DeliveryTableViewModel viewModel;

    @FXML
    public void initialize(){
        viewModel = new DeliveryTableViewModel();
        viewModel.setCurrTableView(deliveryTable);

        // Set up table columns based on DeliveryRecord properties:
        deliveryIDColumn.setCellValueFactory(cellData -> cellData.getValue().deliveryIDProperty());
        deliveryDateColumn.setCellValueFactory(cellData -> cellData.getValue().deliveryDateProperty());
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        deliveryAddressColumn.setCellValueFactory(cellData -> cellData.getValue().deliveryAddressProperty());

        // Load table data
        viewModel.setUp();
    }

    @FXML
    private void onExportExcel(){
        viewModel.headerMultiOptions("EXPORT EXCEL");
    }

    @FXML
    private void onRefresh(){
        viewModel.headerMultiOptions("REFRESH");
    }
}
