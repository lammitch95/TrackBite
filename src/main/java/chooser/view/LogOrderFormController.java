package chooser.view;

import chooser.model.CustomerOrder;
import chooser.utils.MenuItemSelectUtils;
import chooser.viewmodel.LogOrderFormViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class LogOrderFormController {

    @FXML
    private Button IngredientAddBtn;

    @FXML
    private TableView<CustomerOrder> IngredientTableView;

    @FXML
    private HBox addNewBtn;

    @FXML
    private HBox customerNameHbox;

    @FXML
    private TextField customerNameText;

    @FXML
    private HBox deleteBtn;

    @FXML
    private HBox deleteIngredientBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private TextField ingredQuantityText;

    @FXML
    private Button ingredientClearBtn;

    @FXML
    private HBox ingredientQuantityHbox;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private AnchorPane menuFormRootPane;

    @FXML
    private HBox menuItemHbox;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private HBox notesHbox;

    @FXML
    private TextField notesText;

    @FXML
    private Label pageCountLbl;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private Button selectMenuItemBtn;

    @FXML
    private HBox tableNumHbox;

    @FXML
    private TextField tableNumText;

    private LogOrderFormViewModel logOrderVM;
    private List<HBox> inputLabelHbox;

    @FXML
    public void initialize(){

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn,deleteBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }

        logOrderVM = new LogOrderFormViewModel();


        logOrderVM.handleIngredientsTableView("INITIAL", IngredientTableView);
        logOrderVM.orderAddValidProperty().addListener((obs, oldValue, newValue) -> {
            IngredientAddBtn.setDisable(!newValue);
        });

        IngredientTableView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex != null && newIndex.intValue() >= 0) {
                CustomerOrder selected = IngredientTableView.getItems().get(newIndex.intValue());
                logOrderVM.setCurrSelectedOrder(selected);
                logOrderVM.populateAddOrderInputs();
            }
        });

        deleteIngredientBtn.setOnMouseClicked(mouseEvent -> {
            logOrderVM.handleIngredientsTableView("DELETE", IngredientTableView);
        });

        logOrderVM.customerIDProperty().set("New Customer Order");
        saveBtn.disableProperty().bind(logOrderVM.allowSaveProperty().not());

        inputLabelHbox = List.of(
                customerNameHbox,
                menuItemHbox,
                ingredientQuantityHbox
        );

        tableNumText.textProperty().bindBidirectional(logOrderVM.tableNumProperty());
        ingredQuantityText.textProperty().bindBidirectional(logOrderVM.quantityProperty());
        customerNameText.textProperty().bindBidirectional(logOrderVM.nameProperty());
        notesText.textProperty().bindBidirectional(logOrderVM.notesProperty());

        addValidationStyle(tableNumText, logOrderVM.tableNumValidProperty());
        addValidationStyle(selectMenuItemBtn, MenuItemSelectUtils.menuItemIDValidProperty());
        addValidationStyle(ingredQuantityText, logOrderVM.quantityValidProperty());



        MenuItemSelectUtils.menuItemIDValidProperty().addListener((obs, oldVal, newVal) -> {
            logOrderVM.updateValidImageViews(menuItemHbox, MenuItemSelectUtils.menuItemIDValidProperty());
        });

        logOrderVM.quantityValidProperty().addListener((obs, oldVal, newVal) -> {
            logOrderVM.updateValidImageViews(ingredientQuantityHbox, logOrderVM.quantityValidProperty());
        });

        logOrderVM.tableNumValidProperty().addListener((obs, oldVal, newVal) -> {
            logOrderVM.updateValidImageViews(tableNumHbox, logOrderVM.tableNumValidProperty());
        });



        selectMenuItemBtn.textProperty().bind(MenuItemSelectUtils.menuItemIDProperty());

        selectMenuItemBtn.setOnAction(event ->{
            logOrderVM.onSelectOrderItem("SHOW",menuFormRootPane);
        });

        IngredientAddBtn.setOnAction(e ->{
            logOrderVM.handleIngredientsTableView("ADD", IngredientTableView);
        });

        ingredientClearBtn.setOnAction(e -> {
            logOrderVM.clearAddOrderInputs();
        });

        pageCountLbl.textProperty().bindBidirectional(logOrderVM.tbvPageNumDisplayProperty());

        leftArrowBtn.setOnMouseClicked(e ->{
            logOrderVM.handleTableViewNav("LEFT", IngredientTableView);
        });

        rightArrowBtn.setOnMouseClicked(e ->{
            logOrderVM.handleTableViewNav("RIGHT", IngredientTableView);
        });


        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.disableProperty().bind(logOrderVM.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(logOrderVM.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = logOrderVM.onSubmitNewLoggedOrder();
            if(validSubmission){
                resetForm();
                System.out.println("Submission Sucessful. New Menu Item Created.");
            }else{
                System.out.println("Error occured creating new menu item.");
            }
        });

    }


    private void resetForm(){

        IngredientAddBtn.setDisable(true);
        logOrderVM.setIsNewRedcordBoolean(true);
        logOrderVM.clearInputs();

        for(HBox labelHbox: inputLabelHbox){
            ImageView imageView = (ImageView) labelHbox.lookup(".image-view");
            if (imageView != null) {
                imageView.setVisible(false);
            }
        }

        logOrderVM.customerIDProperty().set("New Customer Order");
        logOrderVM.handleIngredientsTableView("RESET", IngredientTableView);

    }

    private void addValidationStyle(Control field, BooleanProperty validProperty) {
        validProperty.addListener((obs, wasValid, isValid) -> {

            System.out.println("Field: " + field.getId() + " Valid: " + isValid);
            field.getStyleClass().removeAll("valid");

            if (isValid) {
                field.getStyleClass().add("valid");
            }
        });
    }

}
