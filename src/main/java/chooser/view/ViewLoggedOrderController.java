package chooser.view;

import chooser.utils.ViewLoggedOrderUtils;
import chooser.viewmodel.ViewLoggedOrderViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewLoggedOrderController {

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
    private Label loggedDateLbl;

    @FXML
    private Label loggedOrdeerIdLbl;

    @FXML
    private Label loggedTimeLbl;

    @FXML
    private Label pageCountLbl;

    @FXML
    private Label recordsCountLbl;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private Label signByLbl;

    @FXML
    private TableView<?> tableViewMain;

    private ViewLoggedOrderViewModel viewLoggedOrderVM;

    @FXML
    public void initialize(){


        viewLoggedOrderVM = new ViewLoggedOrderViewModel();

        viewLoggedOrderVM.setCurrTableView(tableViewMain);

        loggedOrdeerIdLbl.textProperty().bind(viewLoggedOrderVM.displayLoggedOrderIdProp());
        loggedDateLbl.textProperty().bind(viewLoggedOrderVM.displayDateProp());
        loggedTimeLbl.textProperty().bind(viewLoggedOrderVM.displayTimeProp());
        signByLbl.textProperty().bind(viewLoggedOrderVM.displaySignByProp());

        exitBtn.setOnMouseClicked(mouseEvent -> {
            ViewLoggedOrderUtils.displayChangeViewLoggedOrder("HIDE");
        });

        viewLoggedOrderVM.setUp();

        recordsCountLbl.textProperty().bind(viewLoggedOrderVM.getRecordsAmountLbl());
        pageCountLbl.textProperty().bind(viewLoggedOrderVM.getMaxTableViewPageLbl());

        leftArrowBtn.disableProperty().bind(viewLoggedOrderVM.getLeftArrowBtnDisable());
        rightArrowBtn.disableProperty().bind(viewLoggedOrderVM.getRightArrowBtnDisable());

        leftArrowImage.visibleProperty().bind(viewLoggedOrderVM.getLeftArrowVisible());
        rightArrowImage.visibleProperty().bind(viewLoggedOrderVM.getRightArrowVisible());

        leftArrowBtn.setOnMouseClicked(event -> {
            viewLoggedOrderVM.adjustPageDirection("left");
            currentPageText.setText(String.valueOf(viewLoggedOrderVM.getCurrentTableViewPage().get()));
        });
        rightArrowBtn.setOnMouseClicked(event -> {
            viewLoggedOrderVM.adjustPageDirection("right");
            currentPageText.setText(String.valueOf(viewLoggedOrderVM.getCurrentTableViewPage().get()));
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }
        currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                viewLoggedOrderVM.userPageNum(Integer.parseInt(newValue));
            } else {
                System.out.println("Invalid input: Not a number");
            }
        });

    }


}
