package chooser.view;

import chooser.model.RequestItem;
import chooser.utils.PurchaseOrderUtils;
import chooser.viewmodel.ViewPurchaseOrderViewModel;
import chooser.viewmodel.ViewReceiveItemsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewReceiveItemsController {

    @FXML
    private Label addressLine1Label;

    @FXML
    private Label addressLine2Label;

    @FXML
    private Label addressLine3Label;

    @FXML
    private VBox categoryMenu;

    @FXML
    private TextField currentPageText;

    @FXML
    private Label exitBtn;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private Label pageCountLbl;

    @FXML
    private Label pmcEmailLabel;

    @FXML
    private Label pmcNameLabel;

    @FXML
    private Label pmcPhoneLabel;

    @FXML
    private Label pmcWebsiteLabel;

    @FXML
    private Label primary;

    @FXML
    private Label purchaseOrderIdLabel;

    @FXML
    private Label receivedByLabel;

    @FXML
    private Label receivedDateLabel;

    @FXML
    private Label receivedItemIdLabel;

    @FXML
    private Label receivedTimeLabel;

    @FXML
    private Label recordsCountLbl;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private Label supplierIdNameLabel;

    @FXML
    private TableView<RequestItem> tableViewMain;

    private ViewReceiveItemsViewModel vriViewModel;

    @FXML
    private void initialize(){

        vriViewModel = new ViewReceiveItemsViewModel();

        vriViewModel.setCurrTableView(tableViewMain);

        receivedItemIdLabel.textProperty().bind(vriViewModel.receviedItemIdProp());
        receivedDateLabel.textProperty().bind(vriViewModel.deliveredDateProp());
        receivedTimeLabel.textProperty().bind(vriViewModel.deliveredTimeProp());
        receivedByLabel.textProperty().bind(vriViewModel.receivedByProp());
        purchaseOrderIdLabel.textProperty().bind(vriViewModel.purchaseOrderIdProp());

        supplierIdNameLabel.textProperty().bind(vriViewModel.supplierIdAndNameProp());
        pmcNameLabel.textProperty().bind(vriViewModel.prmcNameProp());
        pmcPhoneLabel.textProperty().bind(vriViewModel.prmcPhoneNumberProp());
        pmcEmailLabel.textProperty().bind(vriViewModel.prmcEmailProp());
        pmcWebsiteLabel.textProperty().bind(vriViewModel.prmcWebsiteProp());

        addressLine1Label.textProperty().bind(vriViewModel.addressLineOneProp());
        addressLine2Label.textProperty().bind(vriViewModel.addressLineTwoProp());
        addressLine3Label.textProperty().bind(vriViewModel.addressLineThreeProp());

        exitBtn.setOnMouseClicked(mouseEvent -> {
            PurchaseOrderUtils.displayChangeViewReceivedItems("HIDE");
        });

        vriViewModel.setUp();

        recordsCountLbl.textProperty().bind(vriViewModel.getRecordsAmountLbl());
        pageCountLbl.textProperty().bind(vriViewModel.getMaxTableViewPageLbl());

        leftArrowBtn.disableProperty().bind(vriViewModel.getLeftArrowBtnDisable());
        rightArrowBtn.disableProperty().bind(vriViewModel.getRightArrowBtnDisable());

        leftArrowImage.visibleProperty().bind(vriViewModel.getLeftArrowVisible());
        rightArrowImage.visibleProperty().bind(vriViewModel.getRightArrowVisible());

        leftArrowBtn.setOnMouseClicked(event -> {
            vriViewModel.adjustPageDirection("left");
            currentPageText.setText(String.valueOf(vriViewModel.getCurrentTableViewPage().get()));
        });
        rightArrowBtn.setOnMouseClicked(event -> {
            vriViewModel.adjustPageDirection("right");
            currentPageText.setText(String.valueOf(vriViewModel.getCurrentTableViewPage().get()));
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }
        currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                vriViewModel.userPageNum(Integer.parseInt(newValue));
            } else {
                System.out.println("Invalid input: Not a number");
            }
        });
    }

}
