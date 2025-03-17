package chooser.view;


import chooser.Inventory;
import chooser.utils.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;



public class addDeliveryController {
    @FXML
    private Button addDeliveryButton;

    @FXML
    private Button backToMainSelected;

    @FXML
    private TextField itemID;

    @FXML
    private TextField itemName;

    @FXML
    private TextField pricePerUnit;

    @FXML
    private TextField quantity;

    @FXML
    private DatePicker deliveryDate;

    @FXML
    private DatePicker expirationDate;


    @FXML
    void itemIdEntered(ActionEvent event) {

    }

    @FXML
    void itemNameEntered(ActionEvent event) {

    }

    @FXML
    void quantityEntered(ActionEvent event) {

    }

    @FXML
    void deliveryDateSelected(ActionEvent event) {

    }

    @FXML
    void expirationDateSelected(ActionEvent event) {

    }

    @FXML
    void pricePerUnitEntered(ActionEvent event) {

    }


    @FXML
    void backToMainSelected
            (ActionEvent event) {
        SceneNavigator.switchScene(
                "Homepage",
                "TrackBite/Homepage",
                -1,
                -1,
                true);

    }

    @FXML
    void addDeliverySelected(ActionEvent event) {
    }}





        




