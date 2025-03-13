



package chooser.view;

import chooser.Inventory;
import chooser.database.FirestoreUtils;
import chooser.utils.SceneNavigator;
import chooser.viewmodel.newInventoryItemViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Random;


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

    private newInventoryItemViewModel itemViewModel;


    @FXML
    private void initialize() {

        UnitDropDown.getItems().addAll("Lbs", "Oz", "Gal", "Pieces");
        CategoryDropDown.getItems().addAll("Meat", "Dairy", "Bread", "Produce", "Condiments");
        itemViewModel = new newInventoryItemViewModel();
        ItemName.textProperty().bindBidirectional(itemViewModel.itemNameProperty());
        UnitDropDown.valueProperty().bindBidirectional(itemViewModel.unit);
        CategoryDropDown.valueProperty().bindBidirectional(itemViewModel.category);



    }

    @FXML
    void ItemNameEntered(ActionEvent event) {

    }

    @FXML
    void UnitSelection(ActionEvent event) {

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

    @FXML
    void AddInvClick(ActionEvent event) {
        itemViewModel.onSubmit();


        }

       

    }



































