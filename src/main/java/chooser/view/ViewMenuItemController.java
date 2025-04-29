package chooser.view;

import chooser.utils.NewMenuItemUtils;
import chooser.viewmodel.ViewMenuItemViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class ViewMenuItemController {

    @FXML
    private VBox categoryMenu;

    @FXML
    private TextField currentPageText;

    @FXML
    private Label exitBtn;

    @FXML
    private Label itemIdLabel;

    @FXML
    private ImageView itemImageView;

    @FXML
    private Label itemNameLabel;

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
    private Label servingAmountLabel;

    @FXML
    private TableView<?> tableViewMain;

    private ViewMenuItemViewModel menuItemVM;

    @FXML
    public void initialize(){
        menuItemVM = new ViewMenuItemViewModel();

        exitBtn.setOnMouseClicked(event->{
            NewMenuItemUtils.selectOrderMenuItemView("HIDE",null);
        });

        itemNameLabel.textProperty().bind(menuItemVM.displayItemNameProp());
        menuItemVM.displayItemImgFileNameProp().addListener((observable, oldValue, newValue) -> {
            String imagePath;
            if (newValue != null && !newValue.equals("DEFAULT")) {
                String tempImageFile = "menuItemImages/" + newValue;
                imagePath = new File(tempImageFile).toURI().toString();
            } else {
                imagePath = String.valueOf(ViewMenuItemController.class.getResource("/chooser/trackbite/defaultNoImageFound.png"));
            }
            try {
                
                Image image = new Image(imagePath);
                itemImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error Dsiplay Menu Item Image");
            }
        });

        menuItemVM.servingLeftAmountProp().addListener((observable, oldValue, newValue) -> {
            int servingsLeft = newValue.intValue();
            System.out.println("Checking serving amount left: "+servingsLeft);
            if (servingsLeft > 0) {
                servingAmountLabel.setStyle("-fx-background-color: #00f52d;");
            } else {
                servingAmountLabel.setStyle("-fx-background-color: #ff0000;");
            }
            servingAmountLabel.setText("Servings Left: " + servingsLeft);
        });

        menuItemVM.setCurrTableView(tableViewMain);
        menuItemVM.setUp();

        recordsCountLbl.textProperty().bind(menuItemVM.getRecordsAmountLbl());
        pageCountLbl.textProperty().bind(menuItemVM.getMaxTableViewPageLbl());

        leftArrowBtn.disableProperty().bind(menuItemVM.getLeftArrowBtnDisable());
        rightArrowBtn.disableProperty().bind(menuItemVM.getRightArrowBtnDisable());

        leftArrowImage.visibleProperty().bind(menuItemVM.getLeftArrowVisible());
        rightArrowImage.visibleProperty().bind(menuItemVM.getRightArrowVisible());

        leftArrowBtn.setOnMouseClicked(event -> {
            menuItemVM.adjustPageDirection("left");
            currentPageText.setText(String.valueOf(menuItemVM.getCurrentTableViewPage().get()));
        });
        rightArrowBtn.setOnMouseClicked(event -> {
            menuItemVM.adjustPageDirection("right");
            currentPageText.setText(String.valueOf(menuItemVM.getCurrentTableViewPage().get()));
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }
        currentPageText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                menuItemVM.userPageNum(Integer.parseInt(newValue));
            } else {
                System.out.println("Invalid input: Not a number");
            }
        });

    }

}
