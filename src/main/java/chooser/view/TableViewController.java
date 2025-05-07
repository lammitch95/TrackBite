package chooser.view;

import chooser.model.CurrentPageOptions;
import chooser.model.InventoryItem;
import chooser.model.TableViewColumnData;
import chooser.utils.SystemMessageUtils;
import chooser.utils.TableViewUtils;
import chooser.viewmodel.TableViewViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableViewController {

    @FXML
    private HBox addNewBtn;

    @FXML
    private TextField currentPageText;

    @FXML
    private HBox deleteBtn;

    @FXML
    private HBox exportExcelBtn;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private Label pageCountLbl;

    @FXML
    private Label recordsCountLbl;

    @FXML
    private HBox refreshBtn;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private TableView<?> tableViewMain;

    @FXML
    private TextField searchBarTextField;


    @FXML
    private Label viewTitleLbl;


    private TableViewViewModel tableViewVM;

    @FXML
    private HBox generatePOBtn;

    @FXML
    private void initialize() {


        tableViewVM = new TableViewViewModel();
        tableViewVM.setCurrTableView(tableViewMain);

        String currPageOption = CurrentPageOptions.getCurrPageOption();
        if (currPageOption != null && !currPageOption.equals("")) {
            System.out.println("Current Page Options in tableview controller intialize: " + currPageOption);
            viewTitleLbl.setText(tableViewVM.getTitleMapping().get(currPageOption));
            tableViewVM.setUp();

            if ("View Inventory".equals(currPageOption)) {
                tableViewVM.attachSearchBar(searchBarTextField);

            }

            recordsCountLbl.textProperty().bind(tableViewVM.getRecordsAmountLbl());
            //currentPageText.textProperty().bind(tableViewVM.getCurrentTableViewPage().asString());
            pageCountLbl.textProperty().bind(tableViewVM.getMaxTableViewPageLbl());

            leftArrowBtn.disableProperty().bind(tableViewVM.getLeftArrowBtnDisable());
            rightArrowBtn.disableProperty().bind(tableViewVM.getRightArrowBtnDisable());

            leftArrowImage.visibleProperty().bind(tableViewVM.getLeftArrowVisible());
            rightArrowImage.visibleProperty().bind(tableViewVM.getRightArrowVisible());

            leftArrowBtn.setOnMouseClicked(event -> {
                tableViewVM.adjustPageDirection("left");
                currentPageText.setText(String.valueOf(tableViewVM.getCurrentTableViewPage().get()));
            });
            rightArrowBtn.setOnMouseClicked(event -> {
                tableViewVM.adjustPageDirection("right");
                currentPageText.setText(String.valueOf(tableViewVM.getCurrentTableViewPage().get()));
            });

            HBox[] hboxList = new HBox[]{leftArrowBtn, rightArrowBtn, refreshBtn, addNewBtn, deleteBtn, exportExcelBtn};
            for (HBox hbox : hboxList) {
                hbox.getStyleClass().add("tableview-hover-effect");
            }

            refreshBtn.setOnMouseClicked(event -> tableViewVM.headerMultiOptions("REFRESH"));
            addNewBtn.setOnMouseClicked(event -> tableViewVM.headerMultiOptions("ADD NEW"));
            deleteBtn.setOnMouseClicked(event -> tableViewVM.headerMultiOptions("DELETE"));
            exportExcelBtn.setOnMouseClicked(event -> tableViewVM.headerMultiOptions("EXPORT EXCEL"));

            currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("\\d*")) {
                    tableViewVM.userPageNum(Integer.parseInt(newValue));
                } else {
                    System.out.println("Invalid input: Not a number");
                }
            });

            tableViewMain.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    tableViewVM.storeRowSelectedID(newSelection);
                }
            });

        }
    }
}



