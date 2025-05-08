package chooser.view;

import chooser.model.IngredientItem;
import chooser.model.PurchaseOrder;
import chooser.model.RequestItem;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.PurchaseOrderUtils;
import chooser.utils.TableViewUtils;
import chooser.viewmodel.PurchaseOrderViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class PurchaseOrderController {

    @FXML
    private Button IngredientAddBtn;

    @FXML
    private TableView<RequestItem> IngredientTableView;

    @FXML
    private HBox addNewBtn;

    @FXML
    private Label addressLine1Label;

    @FXML
    private Label addressLine2Label;

    @FXML
    private Label addressLine3Label;

    @FXML
    private HBox deleteBtn;

    @FXML
    private HBox deleteIngredientBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private HBox estDeliveryHbox;

    @FXML
    private TextField estDeliveryText;

    @FXML
    private Button ingredientClearBtn;

    @FXML
    private HBox inventoryIdHbox;

    @FXML
    private Label itemNameLabel;

    @FXML
    private HBox itemQuantityHbox;

    @FXML
    private TextField itemQuantityText;

    @FXML
    private Label itemUOMLabel;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private AnchorPane menuFormRootPane;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private HBox notesHbox;

    @FXML
    private TextField notesText;

    @FXML
    private HBox orderDateHbox;

    @FXML
    private Label orderDateLabel;

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
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private Button searchInventoryBtn;

    @FXML
    private Button searchSupplierBtn;

    @FXML
    private Label suplierNameLabel;

    @FXML
    private HBox supplierSelectHbox;

    PurchaseOrderViewModel purOrderVM;
    private List<HBox> inputLabelHbox;


    @FXML
    private void initialize(){
        purOrderVM = new PurchaseOrderViewModel();

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn,deleteBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }

        purOrderVM.handleIngredientsTableView("INITIAL", IngredientTableView);

        purOrderVM.requestItemValidProperty().addListener((obs, oldValue, newValue) -> {
            IngredientAddBtn.setDisable(!newValue);
        });

        IngredientTableView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex != null && newIndex.intValue() >= 0) {
                RequestItem selected = IngredientTableView.getItems().get(newIndex.intValue());
                purOrderVM.setCurrRequestItem(selected);
                purOrderVM.populateAddOrderInputs();
            }
        });

        deleteIngredientBtn.setOnMouseClicked(mouseEvent -> {
            System.out.println("DELETE BUTTON CLICKED FOR INGREDIENT TABLE VIEW");
            purOrderVM.handleIngredientsTableView("DELETE", IngredientTableView);
        });


        purOrderVM.purcahseOrderIdProp().set("New Purchase Order");
        saveBtn.disableProperty().bind(purOrderVM.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(purOrderVM.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(
                estDeliveryHbox,
                supplierSelectHbox,
                itemQuantityHbox,
                inventoryIdHbox
        );

        purOrderVM.validPOIdProp().addListener((obs, oldVal, newVal) -> {
            employeeIDLabel.setText(purOrderVM.purcahseOrderIdProp().get());
        });

        estDeliveryText.textProperty().bindBidirectional(purOrderVM.estDeliveryProp());
        orderDateLabel.textProperty().bindBidirectional(purOrderVM.orderDateProp());

        searchSupplierBtn.textProperty().bindBidirectional(PurchaseOrderUtils.supplerIdProp());
        suplierNameLabel.textProperty().bind(Bindings.concat("Supplier Name: ", PurchaseOrderUtils.supplierNameProp()));


        pmcNameLabel.textProperty().bind(Bindings.concat("Name: ", PurchaseOrderUtils.prmcFirstNameProp(), " ", PurchaseOrderUtils.prmcLastNameProp()));
        pmcPhoneLabel.textProperty().bind(Bindings.concat("Phone Number: ", PurchaseOrderUtils.prmcPhoneNumberProp()));
        pmcEmailLabel.textProperty().bind(Bindings.concat("Email: ", PurchaseOrderUtils.prmcEmailProp()));
        pmcWebsiteLabel.textProperty().bind(Bindings.concat("Website: ", PurchaseOrderUtils.prmcWebsiteProp()));

        addressLine1Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warAddressLineProp()));
        addressLine2Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warCityProp()," ", PurchaseOrderUtils.warStateProp(),", ",PurchaseOrderUtils.warPostalCodeProp()));
        addressLine3Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warCountryProp()));

        itemQuantityText.textProperty().bindBidirectional(purOrderVM.itemQuantityProp());
        searchInventoryBtn.textProperty().bindBidirectional(PurchaseOrderUtils.itemInventoryIdProp());

        itemNameLabel.textProperty().bind(Bindings.concat("Name: ", PurchaseOrderUtils.itemNameProp()));
        itemUOMLabel.textProperty().bind(Bindings.concat("UOM: ", PurchaseOrderUtils.itemUOMProp()));

        notesText.textProperty().bindBidirectional(purOrderVM.itemNotesProp());

        addValidationStyle(estDeliveryText, purOrderVM.validEstDeliveryProp());
        addValidationStyle(searchSupplierBtn, PurchaseOrderUtils.validSupplierIdProp());
        addValidationStyle(itemQuantityText, purOrderVM.validItemQuantityProp());
        addValidationStyle(searchInventoryBtn,PurchaseOrderUtils.validInventoryIdProp());

        purOrderVM.validEstDeliveryProp().addListener((obs, oldVal, newVal) -> {
            purOrderVM.updateValidImageViews(estDeliveryHbox, purOrderVM.validEstDeliveryProp());
        });
        PurchaseOrderUtils.validInventoryIdProp().addListener((obs, oldVal, newVal) -> {
            purOrderVM.updateValidImageViews(inventoryIdHbox, PurchaseOrderUtils.validInventoryIdProp());
        });
        PurchaseOrderUtils.validSupplierIdProp().addListener((obs, oldVal, newVal) -> {
            purOrderVM.updateValidImageViews(supplierSelectHbox,  PurchaseOrderUtils.validSupplierIdProp());
        });
        purOrderVM.validItemQuantityProp().addListener((obs, oldVal, newVal) -> {
            purOrderVM.updateValidImageViews(itemQuantityHbox, purOrderVM.validItemQuantityProp());
        });

        searchInventoryBtn.setOnAction(event ->{
            purOrderVM.onSearchSystem("SHOW","Inventory",menuFormRootPane);
        });

        searchSupplierBtn.setOnAction(event ->{
            purOrderVM.onSearchSystem("SHOW","Suppliers",menuFormRootPane);
        });

        IngredientAddBtn.setOnAction(e ->{
            purOrderVM.handleIngredientsTableView("ADD", IngredientTableView);

        });

        ingredientClearBtn.setOnAction(e -> {
            purOrderVM.clearAddOrderInputs();

        });

        pageCountLbl.textProperty().bindBidirectional(purOrderVM.tbvPageNumDisplayProperty());

        leftArrowBtn.setOnMouseClicked(e ->{
            purOrderVM.handleTableViewNav("LEFT", IngredientTableView);
        });

        rightArrowBtn.setOnMouseClicked(e ->{
            purOrderVM.handleTableViewNav("RIGHT", IngredientTableView);
        });

        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = purOrderVM.onSubmit();
            if(validSubmission){
                System.out.println("Submission Sucessful. New Purchase Order Created.");
                resetForm();
            }else{
                System.out.println("Error occured creating new Purchase Order.");
            }
        });

    }

    private void resetForm(){

        IngredientAddBtn.setDisable(true);
        purOrderVM.setIsNewRedcordBoolean(true);
        purOrderVM.clearInputs();

        purOrderVM.purcahseOrderIdProp().set("New Purchase Order");
        purOrderVM.handleIngredientsTableView("RESET", IngredientTableView);


        purOrderVM.updateValidImageViews(estDeliveryHbox, purOrderVM.validEstDeliveryProp());
        purOrderVM.updateValidImageViews(inventoryIdHbox, PurchaseOrderUtils.validInventoryIdProp());
        purOrderVM.updateValidImageViews(supplierSelectHbox,  PurchaseOrderUtils.validSupplierIdProp());
        purOrderVM.updateValidImageViews(itemQuantityHbox, purOrderVM.validItemQuantityProp());


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
