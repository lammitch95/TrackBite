package chooser.view;

import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.PurchaseOrderUtils;
import chooser.viewmodel.InventoryItemSelectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InventoryItemSelectController {

    @FXML
    private VBox categoryMenu;

    @FXML
    private TextField currentPageText;

    @FXML
    private Label exitBtn;

    @FXML
    private Label itemIdLabel;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private Label pageCountLbl;

    @FXML
    private Label recordsCountLbl;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private Button selectBtn;

    @FXML
    private TableView<?> tableViewMain;

    private InventoryItemSelectViewModel invItemSelectVM;

    @FXML
    public void initialize(){

        invItemSelectVM = new InventoryItemSelectViewModel();
        invItemSelectVM.setCurrTableView(tableViewMain);
        invItemSelectVM.setUp();

        itemIdLabel.textProperty().bind(invItemSelectVM.getSelectedRowId());

        exitBtn.setOnMouseClicked(event->{
            NewMenuItemUtils.linkInventoryId().set("Select Inventory");
            NewInventoryItemUtils.selectInventoryItem("HIDE",null);

            PurchaseOrderUtils.itemInventoryIdProp().set("Search Inventory");
            PurchaseOrderUtils.searchSystemData("HIDE", null, null);
        });

        selectBtn.setOnAction(actionEvent -> {
            NewInventoryItemUtils.selectInventoryItem("HIDE",null);
            PurchaseOrderUtils.searchSystemData("HIDE", null, null);
        });

        selectBtn.disableProperty().bind(invItemSelectVM.validItemSelectProp().not());


        recordsCountLbl.textProperty().bind(invItemSelectVM.getRecordsAmountLbl());
        pageCountLbl.textProperty().bind(invItemSelectVM.getMaxTableViewPageLbl());

        leftArrowBtn.disableProperty().bind(invItemSelectVM.getLeftArrowBtnDisable());
        rightArrowBtn.disableProperty().bind(invItemSelectVM.getRightArrowBtnDisable());

        leftArrowImage.visibleProperty().bind(invItemSelectVM.getLeftArrowVisible());
        rightArrowImage.visibleProperty().bind(invItemSelectVM.getRightArrowVisible());

        leftArrowBtn.setOnMouseClicked(event -> {
            invItemSelectVM.adjustPageDirection("left");
            currentPageText.setText(String.valueOf(invItemSelectVM.getCurrentTableViewPage().get()));
        });
        rightArrowBtn.setOnMouseClicked(event -> {
            invItemSelectVM.adjustPageDirection("right");
            currentPageText.setText(String.valueOf(invItemSelectVM.getCurrentTableViewPage().get()));
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }


        currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                invItemSelectVM.userPageNum(Integer.parseInt(newValue));
            } else {
                System.out.println("Invalid input: Not a number");
            }
        });

        tableViewMain.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                invItemSelectVM.storeRowSelectedID(newSelection);

            }
        });
    }
}
