package chooser.view;

import chooser.model.RequestItem;
import chooser.utils.PurchaseOrderUtils;
import chooser.viewmodel.PurchaseOrderViewModel;
import chooser.viewmodel.ReceivedItemsViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class ReceivedItemsController {

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
    private HBox deliveredDateHbox;

    @FXML
    private TextField deliveredDateText;

    @FXML
    private HBox deliveredTimeHbox;

    @FXML
    private TextField deliveredTimeText;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private Label employeeIDLabel;

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
    private HBox receivedByHbox;

    @FXML
    private Label receivedByLabel;

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
    private Button searchPOBtn;

    @FXML
    private HBox searchPOHbox;

    @FXML
    private Label suplierNameLabel;

    ReceivedItemsViewModel receivedItemsViewModel;
    private List<HBox> inputLabelHbox;

    @FXML
    private void initialize(){

        receivedItemsViewModel = new ReceivedItemsViewModel();

        PurchaseOrderUtils.triggerPopulateTableProp().addListener((obs, oldValue, newValue) -> {
            receivedItemsViewModel.initialPopulateTable(IngredientTableView);
        });

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn,deleteBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }

        receivedItemsViewModel.handleIngredientsTableView("INITIAL", IngredientTableView);

        receivedItemsViewModel.requestItemValidProperty().addListener((obs, oldValue, newValue) -> {
            IngredientAddBtn.setDisable(!newValue);
        });

        IngredientTableView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex != null && newIndex.intValue() >= 0) {
                RequestItem selected = IngredientTableView.getItems().get(newIndex.intValue());
                receivedItemsViewModel.setCurrRequestItem(selected);
                receivedItemsViewModel.populateAddOrderInputs();
            }
        });

        deleteIngredientBtn.setOnMouseClicked(mouseEvent -> {
            receivedItemsViewModel.handleIngredientsTableView("DELETE", IngredientTableView);
        });


        saveBtn.disableProperty().bind(receivedItemsViewModel.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(receivedItemsViewModel.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(
                deliveredDateHbox,
                deliveredTimeHbox,
                itemQuantityHbox,
                inventoryIdHbox
        );

        receivedItemsViewModel.validReceivedItemsIdProp().addListener((obs, oldVal, newVal) -> {
            employeeIDLabel.setText(receivedItemsViewModel.receiveItemsIdProp().get());
        });

        deliveredDateText.textProperty().bindBidirectional(receivedItemsViewModel.deliveredDateProp());
        deliveredTimeText.textProperty().bindBidirectional(receivedItemsViewModel.deliveredTimeProp());
        receivedByLabel.textProperty().bindBidirectional(receivedItemsViewModel.receivedByProp());

        searchPOBtn.textProperty().bindBidirectional(PurchaseOrderUtils.purchaseOrderIdProp());

        suplierNameLabel.textProperty().bind(Bindings.concat(PurchaseOrderUtils.supplerIdProp()," | ",PurchaseOrderUtils.supplierNameProp()));


        pmcNameLabel.textProperty().bind(Bindings.concat("Name: ", PurchaseOrderUtils.prmcFirstNameProp(), " ", PurchaseOrderUtils.prmcLastNameProp()));
        pmcPhoneLabel.textProperty().bind(Bindings.concat("Phone Number: ", PurchaseOrderUtils.prmcPhoneNumberProp()));
        pmcEmailLabel.textProperty().bind(Bindings.concat("Email: ", PurchaseOrderUtils.prmcEmailProp()));
        pmcWebsiteLabel.textProperty().bind(Bindings.concat("Website: ", PurchaseOrderUtils.prmcWebsiteProp()));

        addressLine1Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warAddressLineProp()));
        addressLine2Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warCityProp()," ", PurchaseOrderUtils.warStateProp(),", ",PurchaseOrderUtils.warPostalCodeProp()));
        addressLine3Label.textProperty().bind(Bindings.concat(PurchaseOrderUtils.warCountryProp()));

        itemQuantityText.textProperty().bindBidirectional(receivedItemsViewModel.itemQuantityProp());
        searchInventoryBtn.textProperty().bindBidirectional(PurchaseOrderUtils.itemInventoryIdProp());

        itemNameLabel.textProperty().bind(Bindings.concat("Name: ", PurchaseOrderUtils.itemNameProp()));
        itemUOMLabel.textProperty().bind(Bindings.concat("UOM: ", PurchaseOrderUtils.itemUOMProp()));

        notesText.textProperty().bindBidirectional(receivedItemsViewModel.itemNotesProp());

        addValidationStyle(deliveredDateText, receivedItemsViewModel.validDeliveredDateProp());
        addValidationStyle(deliveredTimeText, receivedItemsViewModel.validDeliveredTimeProp());
        addValidationStyle(itemQuantityText, receivedItemsViewModel.validItemQuantityProp());
        addValidationStyle(searchInventoryBtn,PurchaseOrderUtils.validInventoryIdProp());

        receivedItemsViewModel.validDeliveredDateProp().addListener((obs, oldVal, newVal) -> {
            receivedItemsViewModel.updateValidImageViews(deliveredDateHbox, receivedItemsViewModel.validDeliveredDateProp());
        });
        receivedItemsViewModel.validDeliveredTimeProp().addListener((obs, oldVal, newVal) -> {
            receivedItemsViewModel.updateValidImageViews(deliveredTimeHbox, receivedItemsViewModel.validDeliveredTimeProp());
        });
        PurchaseOrderUtils.validInventoryIdProp().addListener((obs, oldVal, newVal) -> {
            receivedItemsViewModel.updateValidImageViews(inventoryIdHbox, PurchaseOrderUtils.validInventoryIdProp());
        });
        receivedItemsViewModel.validItemQuantityProp().addListener((obs, oldVal, newVal) -> {
            receivedItemsViewModel.updateValidImageViews(itemQuantityHbox, receivedItemsViewModel.validItemQuantityProp());
        });

        searchInventoryBtn.setOnAction(event ->{
            receivedItemsViewModel.onSearchSystem("SHOW","Inventory",menuFormRootPane);
        });

        searchPOBtn.setOnAction(event ->{
            receivedItemsViewModel.onSearchSystem("SHOW","PurchaseOrder",menuFormRootPane);
        });

        IngredientAddBtn.setOnAction(e ->{
            receivedItemsViewModel.handleIngredientsTableView("ADD", IngredientTableView);

        });

        ingredientClearBtn.setOnAction(e -> {
            receivedItemsViewModel.clearAddOrderInputs();

        });

        pageCountLbl.textProperty().bindBidirectional(receivedItemsViewModel.tbvPageNumDisplayProperty());

        leftArrowBtn.setOnMouseClicked(e ->{
            receivedItemsViewModel.handleTableViewNav("LEFT", IngredientTableView);
        });

        rightArrowBtn.setOnMouseClicked(e ->{
            receivedItemsViewModel.handleTableViewNav("RIGHT", IngredientTableView);
        });

        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = receivedItemsViewModel.onSubmit();
            if(validSubmission){
                System.out.println("Submission Sucessful. New Received Items Created.");
                resetForm();
            }else{
                System.out.println("Error occured creating new Received Items.");
            }
        });

    }

    private void resetForm(){
        IngredientAddBtn.setDisable(true);
        receivedItemsViewModel.setIsNewRedcordBoolean(true);
        receivedItemsViewModel.clearInputs();

        receivedItemsViewModel.handleIngredientsTableView("RESET", IngredientTableView);

        receivedItemsViewModel.updateValidImageViews(deliveredDateHbox, receivedItemsViewModel.validDeliveredDateProp());
        receivedItemsViewModel.updateValidImageViews(deliveredTimeHbox, receivedItemsViewModel.validDeliveredTimeProp());
        receivedItemsViewModel.updateValidImageViews(inventoryIdHbox, PurchaseOrderUtils.validInventoryIdProp());
        receivedItemsViewModel.updateValidImageViews(itemQuantityHbox, receivedItemsViewModel.validItemQuantityProp());
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
