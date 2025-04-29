package chooser.view;

import chooser.utils.SceneNavigator;
import chooser.viewmodel.OrderMenuViewViewModel;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderMenuViewController {

    @FXML
    private VBox ItemGridPane;

    @FXML
    private VBox categoryMenu;

    @FXML
    private HBox chooseCategoryBtn;

    @FXML
    private Label chooseCategoryLabel;

    @FXML
    private Label exitBtn;

    @FXML
    private HBox logOrderBtn;

    @FXML
    private VBox mainContentPane;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private Label notItemsLabel;

    @FXML
    private Button option1;

    @FXML
    private Button option10;

    @FXML
    private Button option2;

    @FXML
    private Button option3;

    @FXML
    private Button option4;

    @FXML
    private Button option5;

    @FXML
    private Button option6;

    @FXML
    private Button option7;

    @FXML
    private Button option8;

    @FXML
    private Button option9;

    @FXML
    private VBox pageMenuHeader;

    @FXML
    private Label pageName;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollGridPane;

    @FXML
    private HBox viewOrderHistBtn;

    private Button[] categoryBtnsArr;

    private OrderMenuViewViewModel omvVM;

    private HBox[] hboxList;
    @FXML
    private void initialize(){
        omvVM = new OrderMenuViewViewModel();
        omvVM.setStoreMainLayout(rootPane);


        hboxList = new HBox[]{
            chooseCategoryBtn,
                logOrderBtn,
                viewOrderHistBtn
        };

        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("hover-effect");
        }

        categoryBtnsArr = new Button[]{
                option1,
                option2,
                option3,
                option4,
                option5,
                option6,
                option7,
                option8,
                option9,
                option10
        };

        omvVM.setUpCategoryBtns(categoryBtnsArr,ItemGridPane);
        omvVM.toggleCategoryMenuProp().set(false);

        categoryMenu.disableProperty().bind(omvVM.toggleCategoryMenuProp().not());
        categoryMenu.visibleProperty().bind(omvVM.toggleCategoryMenuProp());

        chooseCategoryBtn.setOnMouseClicked(event ->{
            omvVM.toggleCategoryMenuProp().set(true);
        });

        exitBtn.setOnMouseClicked(event ->{
            omvVM.toggleCategoryMenuProp().set(false);
        });

        chooseCategoryLabel.textProperty().bind(omvVM.trackSelectedCategoryProp());

        omvVM.isNoItemsProp().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                notItemsLabel.setPrefHeight(26);
                notItemsLabel.setVisible(true);
            } else {
                notItemsLabel.setPrefHeight(0);
                notItemsLabel.setVisible(false);
            }
        });

        logOrderBtn.setOnMouseClicked(mouseEvent -> {
            SceneNavigator.loadView("Log Order");
        });

        viewOrderHistBtn.setOnMouseClicked(mouseEvent -> {
            SceneNavigator.loadView("View Order History");
        });

    }


}



