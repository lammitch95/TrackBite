package chooser.view;

import chooser.model.ItemStock;
import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.TableViewUtils;
import chooser.viewmodel.NewInventoryItemViewModel;
import chooser.viewmodel.NewMenuItemViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class NewInventoryItemController {

    @FXML
    private Button IngredientAddBtn;

    @FXML
    private HBox addNewBtn;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private HBox categoryHbox;

    @FXML
    private ComboBox<String> currencyComboBox;

    @FXML
    private HBox deleteBtn;

    @FXML
    private HBox deleteIngredientBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private HBox expireDateHbox;

    @FXML
    private TextField expireDateText;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField ingredQuantityText;

    @FXML
    private Button ingredientClearBtn;

    @FXML
    private HBox ingredientQuantityHbox;

    @FXML
    private HBox itemLimitHbox;

    @FXML
    private TextField itemLimitText;

    @FXML
    private TableView<ItemStock> itemStockTableView;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private HBox minStockHbox;

    @FXML
    private TextField minStockText;

    @FXML
    private HBox nameHbox;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private Label pageCountLbl;

    @FXML
    private HBox priceHbox;

    @FXML
    private TextField priceText;

    @FXML
    private ComboBox<String> quantityUOMComboBox;

    @FXML
    private HBox quantityUOMHbox;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private HBox stockStatusHbox;

    @FXML
    private Label stockStatusLabel;

    @FXML
    private HBox totalStockHbox;

    @FXML
    private Label totalStockLabel;

    private NewInventoryItemViewModel newInvItemVM;
    private List<HBox> inputLabelHbox;

    @FXML
    public void initialize(){
        newInvItemVM = new NewInventoryItemViewModel();

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn,deleteBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }

        totalStockLabel.textProperty().bind(newInvItemVM.stockQuantityDisplayProp());
        stockStatusLabel.textProperty().bind(newInvItemVM.stockStatusDisplayProp());

        categoryComboBox.getItems().clear();
        categoryComboBox.getSelectionModel().clearSelection();
        categoryComboBox.setValue(null);

        quantityUOMComboBox.getItems().clear();
        quantityUOMComboBox.getSelectionModel().clearSelection();
        quantityUOMComboBox.setValue(null);

        currencyComboBox.getItems().clear();
        currencyComboBox.getSelectionModel().clearSelection();
        currencyComboBox.setValue(null);

        newInvItemVM.handleItemStockTableView("INITIAL", itemStockTableView);

        newInvItemVM.validItemStockFormProp().addListener((obs, oldValue, newValue) -> {
            System.out.println("Checking ItemStockAddBtn Disable Property: "+newValue);
            IngredientAddBtn.setDisable(!newValue);
        });

        itemStockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                newInvItemVM.setCurrSelectedItemStock(newSelection);
            }
        });


        deleteIngredientBtn.setOnMouseClicked(mouseEvent -> {
            System.out.println("DELETE BUTTON CLICKED FOR ITEM STOCK TABLE VIEW");
            newInvItemVM.handleItemStockTableView("DELETE", itemStockTableView);
        });

        newInvItemVM.inventoryIdProp().set("New Inventory Item");
        saveBtn.disableProperty().bind(newInvItemVM.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(newInvItemVM.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(
                nameHbox,
                categoryHbox,
                itemLimitHbox,
                minStockHbox,
                ingredientQuantityHbox,
                expireDateHbox
        );

        newInvItemVM.categoryProp().addListener((obs, oldVal, newVal) -> {
            if (categoryComboBox.getItems().contains(newVal)) {
                categoryComboBox.getSelectionModel().select(newVal);
            }
        });

        newInvItemVM.quantityUOMProp().addListener((obs, oldVal, newVal) -> {
            if (quantityUOMComboBox.getItems().contains(newVal)) {
                quantityUOMComboBox.getSelectionModel().select(newVal);
            }
        });

        newInvItemVM.priceUOMProp().addListener((obs, oldVal, newVal) -> {
            if (currencyComboBox.getItems().contains(newVal)) {
                currencyComboBox.getSelectionModel().select(newVal);
            }
        });

        newInvItemVM.validInventoryIdProp().addListener((obs, oldVal, newVal) -> {
            employeeIDLabel.setText(newInvItemVM.inventoryIdProp().get());
        });

        firstnameText.textProperty().bindBidirectional(newInvItemVM.nameProp());
        itemLimitText.textProperty().bindBidirectional(newInvItemVM.itemLimitProp());
        minStockText.textProperty().bindBidirectional(newInvItemVM.minStockProp());

        categoryComboBox.setOnAction(e -> {
            String selectedItem = categoryComboBox.getValue();
            newInvItemVM.categoryProp().set(selectedItem);
        });

        quantityUOMComboBox.setOnAction(e -> {
            String selectedItem = quantityUOMComboBox.getValue();
            newInvItemVM.quantityUOMProp().set(selectedItem);
        });

        priceText.textProperty().bindBidirectional(newInvItemVM.priceProp());

        currencyComboBox.setOnAction(e -> {
            String selectedItem = currencyComboBox.getValue();
            newInvItemVM.priceUOMProp().set(selectedItem);
        });

        addValidationStyle(firstnameText, newInvItemVM.validNameProp());
        addValidationStyle(itemLimitText, newInvItemVM.validItemLimitProp());
        addValidationStyle(minStockText, newInvItemVM.validMinStockProp());
        addValidationStyle(priceText, newInvItemVM.validPriceProp());

        addValidationStyle(categoryComboBox,newInvItemVM.validCategoryProp());
        addValidationStyle(quantityUOMComboBox,newInvItemVM.validQuantityUOM());
        addValidationStyle(currencyComboBox,newInvItemVM.validPriceUOMProp());

        addValidationStyle(ingredQuantityText, newInvItemVM.validQuantityProp());
        addValidationStyle(expireDateText, newInvItemVM.validExpireDateProp());


        newInvItemVM.validNameProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(nameHbox, newInvItemVM.validNameProp());
        });

        newInvItemVM.validItemLimitProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(itemLimitHbox, newInvItemVM.validItemLimitProp());
        });

        newInvItemVM.validMinStockProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(minStockHbox, newInvItemVM.validMinStockProp());
        });

        newInvItemVM.validCategoryProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(categoryHbox, newInvItemVM.validCategoryProp());
        });

        newInvItemVM.validQuantityUOM().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(quantityUOMHbox, newInvItemVM.validQuantityUOM());
        });

        newInvItemVM.validPriceProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(priceHbox, newInvItemVM.validPriceProp(), newInvItemVM.validPriceUOMProp());
        });

        newInvItemVM.validQuantityProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(ingredientQuantityHbox, newInvItemVM.validQuantityProp());
        });

        newInvItemVM.validExpireDateProp().addListener((obs, oldVal, newVal) -> {
            newInvItemVM.updateValidImageViews(expireDateHbox, newInvItemVM.validExpireDateProp());
        });

        ingredQuantityText.textProperty().bindBidirectional(newInvItemVM.quantityProp());
        expireDateText.textProperty().bindBidirectional(newInvItemVM.expireDateProp());

        IngredientAddBtn.setOnAction(e ->{
            newInvItemVM.handleItemStockTableView("ADD", itemStockTableView);
        });

        ingredientClearBtn.setOnAction(e -> {
            newInvItemVM.clearStockItemInputs();
        });

        pageCountLbl.textProperty().bindBidirectional(newInvItemVM.tbvPageNumDisplayProperty());

        leftArrowBtn.setOnMouseClicked(e ->{
            newInvItemVM.handleTableViewNav("LEFT", itemStockTableView);
        });

        rightArrowBtn.setOnMouseClicked(e ->{
            newInvItemVM.handleTableViewNav("RIGHT", itemStockTableView);
        });


        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = newInvItemVM.onSubmit();
            if(validSubmission){
                System.out.println("Submission Sucessful. New Inventory Item Created.");
                //resetForm();
            }else{
                System.out.println("Error occured creating new inventory item.");
            }
        });

        String selectedRowID = TableViewUtils.getSelectedRowID();
        String collectionName = TableViewUtils.getStoredCollectionName();
        if(selectedRowID!=null && collectionName.equals("InventoryV2")){
            newInvItemVM.setIsNewRedcordBoolean(false);
            newInvItemVM.populateTextFields(itemStockTableView);
        }

    }

    private void resetForm(){

        IngredientAddBtn.setDisable(true);
        newInvItemVM.setIsNewRedcordBoolean(true);
        newInvItemVM.clearInputs();

        for(HBox labelHbox: inputLabelHbox){
            ImageView imageView = (ImageView) labelHbox.lookup(".image-view");
            if (imageView != null) {
                imageView.setVisible(false);
            }
        }

        categoryComboBox.getItems().clear();
        categoryComboBox.setItems(FXCollections.observableArrayList(NewInventoryItemUtils.getCategoryList()));
        categoryComboBox.getSelectionModel().selectFirst();
        newInvItemVM.categoryProp().set(categoryComboBox.getValue());

        quantityUOMComboBox.getItems().clear();
        quantityUOMComboBox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveIngredientUOMListList()));
        quantityUOMComboBox.getSelectionModel().selectFirst();
        newInvItemVM.quantityUOMProp().set(quantityUOMComboBox.getValue());

        currencyComboBox.getItems().clear();
        currencyComboBox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveMoneyCurrencyList()));
        currencyComboBox.getSelectionModel().selectFirst();
        newInvItemVM.priceUOMProp().set(currencyComboBox.getValue());

        //Later replace with universal message system
        //errMessLabel.setText("");

        newInvItemVM.inventoryIdProp().set("New Inventory Item");
        newInvItemVM.handleItemStockTableView("RESET", itemStockTableView);

        newInvItemVM.updateValidImageViews(nameHbox, newInvItemVM.validNameProp());
        newInvItemVM.updateValidImageViews(itemLimitHbox, newInvItemVM.validItemLimitProp());
        newInvItemVM.updateValidImageViews(minStockHbox, newInvItemVM.validMinStockProp());
        newInvItemVM.updateValidImageViews(categoryHbox, newInvItemVM.validCategoryProp());
        newInvItemVM.updateValidImageViews(quantityUOMHbox, newInvItemVM.validQuantityUOM());
        //newInvItemVM.updateValidImageViews(priceHbox, newInvItemVM.validPriceProp(), newInvItemVM.validPriceUOMProp());
        newInvItemVM.updateValidImageViews(ingredientQuantityHbox, newInvItemVM.validQuantityProp());
        newInvItemVM.updateValidImageViews(expireDateHbox, newInvItemVM.validExpireDateProp());


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
