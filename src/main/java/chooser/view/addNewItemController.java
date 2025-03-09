



package chooser.view;

import chooser.utils.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class addNewItemController {

    @FXML
    private Button AddInvButton;

    @FXML
    private Button EnterBackToMainMenu;

    @FXML
    private TextField ItemName;

    @FXML
    private ComboBox UnitDropDown;

    @FXML
    private ComboBox CategoryDropDown;


    @FXML
    private void initialize() {

        UnitDropDown.getItems().addAll("Lbs", "Oz", "Gal", "Pieces");
        CategoryDropDown.getItems().addAll("Meat", "Dairy", "Bread", "Produce", "Condiments");

        }

    @FXML
    void ItemNameEntered(ActionEvent event) {

    }
    @FXML
    void UnitSelection(ActionEvent event) {

    }
    @FXML
    void AddInvClick(ActionEvent event) {

    }
    @FXML
    void CategorySelection(ActionEvent event) {

    }
    @FXML
    void BacktoMainClick(ActionEvent event) {
        SceneNavigator.switchScene(
                "Homepage",
                "TrackBite/Homepage",
                -1,
                -1,
                true);

    }
    }
