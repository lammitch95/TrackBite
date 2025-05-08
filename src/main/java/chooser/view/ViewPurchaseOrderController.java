package chooser.view;

import chooser.utils.PurchaseOrderUtils;
import chooser.utils.ViewLoggedOrderUtils;
import chooser.viewmodel.ViewPurchaseOrderViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewPurchaseOrderController {

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
    private Label estDeliveryLabel;

    @FXML
    private Label exitBtn;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private Label orderDateLabel;

    @FXML
    private Label orderStatusLabel;

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
    private Label recordsCountLbl;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private Label signByLabel;

    @FXML
    private Label supplierIdNameLabel;

    @FXML
    private TableView<?> tableViewMain;

    private ViewPurchaseOrderViewModel vpoViewModel;
    @FXML
    private void initialize(){
        vpoViewModel = new ViewPurchaseOrderViewModel();

        vpoViewModel.setCurrTableView(tableViewMain);

        purchaseOrderIdLabel.textProperty().bind(vpoViewModel.purchaseOrderIdProp());
        estDeliveryLabel.textProperty().bind(vpoViewModel.estDeliveryProp());
        orderDateLabel.textProperty().bind(vpoViewModel.orderDateProp());
        orderStatusLabel.textProperty().bind(vpoViewModel.orderStatusProp());
        signByLabel.textProperty().bind(vpoViewModel.signedByProp());

        supplierIdNameLabel.textProperty().bind(vpoViewModel.supplierIdAndNameProp());
        pmcNameLabel.textProperty().bind(vpoViewModel.prmcNameProp());
        pmcPhoneLabel.textProperty().bind(vpoViewModel.prmcPhoneNumberProp());
        pmcEmailLabel.textProperty().bind(vpoViewModel.prmcEmailProp());
        pmcWebsiteLabel.textProperty().bind(vpoViewModel.prmcWebsiteProp());

        addressLine1Label.textProperty().bind(vpoViewModel.addressLineOneProp());
        addressLine2Label.textProperty().bind(vpoViewModel.addressLineTwoProp());
        addressLine3Label.textProperty().bind(vpoViewModel.addressLineThreeProp());

        exitBtn.setOnMouseClicked(mouseEvent -> {
            PurchaseOrderUtils.displayChangeViewPurchaseOrder("HIDE");
        });

        vpoViewModel.setUp();

        recordsCountLbl.textProperty().bind(vpoViewModel.getRecordsAmountLbl());
        pageCountLbl.textProperty().bind(vpoViewModel.getMaxTableViewPageLbl());

        leftArrowBtn.disableProperty().bind(vpoViewModel.getLeftArrowBtnDisable());
        rightArrowBtn.disableProperty().bind(vpoViewModel.getRightArrowBtnDisable());

        leftArrowImage.visibleProperty().bind(vpoViewModel.getLeftArrowVisible());
        rightArrowImage.visibleProperty().bind(vpoViewModel.getRightArrowVisible());

        leftArrowBtn.setOnMouseClicked(event -> {
            vpoViewModel.adjustPageDirection("left");
            currentPageText.setText(String.valueOf(vpoViewModel.getCurrentTableViewPage().get()));
        });
        rightArrowBtn.setOnMouseClicked(event -> {
            vpoViewModel.adjustPageDirection("right");
            currentPageText.setText(String.valueOf(vpoViewModel.getCurrentTableViewPage().get()));
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }
        currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                vpoViewModel.userPageNum(Integer.parseInt(newValue));
            } else {
                System.out.println("Invalid input: Not a number");
            }
        });
    }

}
