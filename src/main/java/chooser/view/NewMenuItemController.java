package chooser.view;

import chooser.model.IngredientItem;
import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.TableViewUtils;
import chooser.viewmodel.NewMenuItemViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class NewMenuItemController {

    @FXML
    private Button IngredientAddBtn;

    @FXML
    private TextField IngredientNameText;

    @FXML
    private TableView<IngredientItem> IngredientTableView;

    @FXML
    private ImageView ItemImageDisplay;

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
    private HBox descriptionHbox;

    @FXML
    private TextField descriptionText;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField ingredPrepText;

    @FXML
    private TextField ingredQuantityText;

    @FXML
    private ComboBox<String> ingredUOMCombobox;

    @FXML
    private Button ingredientClearBtn;

    @FXML
    private HBox ingredientNameHbox;

    @FXML
    private HBox ingredientQuantityHbox;

    @FXML
    private HBox ingredientUOMHbox;

    @FXML
    private HBox itemImageHbox;

    @FXML
    private HBox leftArrowBtn;

    @FXML
    private ImageView leftArrowImage;

    @FXML
    private Button linkInventoryBtn;

    @FXML
    private HBox linkInventoryHbox;
    @FXML

    private AnchorPane menuFormRootPane;

    @FXML
    private HBox nameHbox;

    @FXML
    private HBox nameHbox1111;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private Label pageCountLbl;

    @FXML
    private HBox priceHbox;

    @FXML
    private TextField priceText;

    @FXML
    private HBox rightArrowBtn;

    @FXML
    private ImageView rightArrowImage;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private Button uploadImageBtn;


    private NewMenuItemViewModel newMenuItemVM;
    private List<HBox> inputLabelHbox;

    @FXML
    public void initialize(){

        System.out.println("NewMenuItemController Initialize");

        HBox[] hboxList = new HBox[]{leftArrowBtn,rightArrowBtn,addNewBtn,deleteBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("tableview-hover-effect");
        }

        categoryComboBox.getItems().clear();
        categoryComboBox.getSelectionModel().clearSelection();
        categoryComboBox.setValue(null);

        currencyComboBox.getItems().clear();
        currencyComboBox.getSelectionModel().clearSelection();
        currencyComboBox.setValue(null);

        newMenuItemVM = new NewMenuItemViewModel();

        newMenuItemVM.handleIngredientsTableView("INITIAL", IngredientTableView);

        newMenuItemVM.ingredientItemValidProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Checking IngredientAddBtn Disable Property: "+newValue);
            IngredientAddBtn.setDisable(!newValue);
        });

        IngredientTableView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex != null && newIndex.intValue() >= 0) {
                IngredientItem selected = IngredientTableView.getItems().get(newIndex.intValue());
                newMenuItemVM.setCurrentSelectedIngredient(selected);
                newMenuItemVM.populateIngredientsInputs();
            }
        });


        deleteIngredientBtn.setOnMouseClicked(mouseEvent -> {
            System.out.println("DELETE BUTTON CLICKED FOR INGREDIENT TABLE VIEW");
            newMenuItemVM.handleIngredientsTableView("DELETE", IngredientTableView);
        });


        newMenuItemVM.menuItemIDProperty().set("New Menu Item");
        saveBtn.disableProperty().bind(newMenuItemVM.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(newMenuItemVM.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(
                nameHbox,
                descriptionHbox,
                categoryHbox,
                priceHbox,
                itemImageHbox,
                ingredientNameHbox,
                ingredientQuantityHbox,
                ingredientUOMHbox
        );

        newMenuItemVM.categoryProperty().addListener((obs, oldVal, newVal) -> {
            if (categoryComboBox.getItems().contains(newVal)) {
                categoryComboBox.getSelectionModel().select(newVal);
            }
        });

        newMenuItemVM.priceUOMProperty().addListener((obs, oldVal, newVal) -> {
            if (currencyComboBox.getItems().contains(newVal)) {
                currencyComboBox.getSelectionModel().select(newVal);
            }
        });

        newMenuItemVM.ingredientUOMProperty().addListener((obs, oldVal, newVal) -> {
            if (ingredUOMCombobox.getItems().contains(newVal)) {
                ingredUOMCombobox.getSelectionModel().select(newVal);
            }
        });

        newMenuItemVM.imageFileNameProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    String imagePath = new File("menuItemImages/" + newVal).toURI().toString();
                    Image image = new Image(imagePath);
                    ItemImageDisplay.setImage(image);

                } catch (Exception e) {
                    System.out.println("Error Dsiplay Menu Item Image");
                }

        });

        newMenuItemVM.menuItemIDValidProperty().addListener((obs, oldVal, newVal) -> {
            employeeIDLabel.setText(newMenuItemVM.menuItemIDProperty().get());
        });

        firstnameText.textProperty().bindBidirectional(newMenuItemVM.nameProperty());
        descriptionText.textProperty().bindBidirectional(newMenuItemVM.descriptionProperty());


        categoryComboBox.setOnAction(e -> {
            String selectedItem = categoryComboBox.getValue();
            newMenuItemVM.categoryProperty().set(selectedItem);
        });

        priceText.textProperty().bindBidirectional(newMenuItemVM.priceProperty());

        currencyComboBox.setOnAction(e -> {
            String selectedItem = currencyComboBox.getValue();
            newMenuItemVM.priceUOMProperty().set(selectedItem);
        });



        uploadImageBtn.setOnAction(e ->{
            newMenuItemVM.uploadItemImage(ItemImageDisplay, "ADD");
        });


        addValidationStyle(firstnameText, newMenuItemVM.nameValidProperty());
        addValidationStyle(descriptionText, newMenuItemVM.descriptionValidProperty());
        addValidationStyle(priceText, newMenuItemVM.priceValidProperty());

        addValidationStyle(categoryComboBox,newMenuItemVM.categoryValidProperty());
        addValidationStyle(currencyComboBox,newMenuItemVM.priceUOMValidProperty());

        addValidationStyle(IngredientNameText, newMenuItemVM.ingredientNameValidProperty());
        addValidationStyle(ingredQuantityText, newMenuItemVM.ingredientQuantityValidProperty());
        addValidationStyle(ingredUOMCombobox,newMenuItemVM.ingredientUOMValidProperty());
        addValidationStyle(linkInventoryBtn,NewMenuItemUtils.linkInventoryIdValidProp());

        newMenuItemVM.nameValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(nameHbox, newMenuItemVM.nameValidProperty());
        });

        newMenuItemVM.descriptionValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(descriptionHbox, newMenuItemVM.descriptionValidProperty());
        });

        newMenuItemVM.categoryValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(categoryHbox, newMenuItemVM.categoryValidProperty());
        });

        newMenuItemVM.priceValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(priceHbox, newMenuItemVM.priceValidProperty(), newMenuItemVM.priceUOMValidProperty());
        });

        newMenuItemVM.itemImageValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(itemImageHbox, newMenuItemVM.itemImageValidProperty());
        });


        newMenuItemVM.ingredientNameValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(ingredientNameHbox, newMenuItemVM.ingredientNameValidProperty());
        });

        newMenuItemVM.ingredientQuantityValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(ingredientQuantityHbox, newMenuItemVM.ingredientQuantityValidProperty());
        });

        newMenuItemVM.ingredientUOMValidProperty().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(ingredientUOMHbox, newMenuItemVM.ingredientUOMValidProperty());
        });

        NewMenuItemUtils.linkInventoryIdValidProp().addListener((obs, oldVal, newVal) -> {
            newMenuItemVM.updateValidImageViews(linkInventoryHbox, NewMenuItemUtils.linkInventoryIdValidProp());
        });

        IngredientNameText.textProperty().bindBidirectional(newMenuItemVM.ingredientNameProperty());
        ingredQuantityText.textProperty().bindBidirectional(newMenuItemVM.ingredientQuantityProperty());
        ingredPrepText.textProperty().bindBidirectional(newMenuItemVM.ingredientPrepDetails());
        linkInventoryBtn.textProperty().bind(NewMenuItemUtils.linkInventoryId());

        linkInventoryBtn.setOnAction(event ->{
            newMenuItemVM.onSelectInventoryItem("SHOW",menuFormRootPane);
        });


        ingredUOMCombobox.setOnAction(e -> {
            String selectedItem = ingredUOMCombobox.getValue();
            newMenuItemVM.ingredientUOMProperty().set(selectedItem);
        });

        IngredientAddBtn.setOnAction(e ->{
            newMenuItemVM.handleIngredientsTableView("ADD", IngredientTableView);
            ingredUOMCombobox.getSelectionModel().selectFirst();
        });

        ingredientClearBtn.setOnAction(e -> {
            newMenuItemVM.clearIngredientInputs();
            ingredUOMCombobox.getSelectionModel().selectFirst();

        });

        pageCountLbl.textProperty().bindBidirectional(newMenuItemVM.tbvPageNumDisplayProperty());

        leftArrowBtn.setOnMouseClicked(e ->{
            newMenuItemVM.handleTableViewNav("LEFT", IngredientTableView);
        });

        rightArrowBtn.setOnMouseClicked(e ->{
            newMenuItemVM.handleTableViewNav("RIGHT", IngredientTableView);
        });


        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = newMenuItemVM.onSubmitNewMenuItem();
            if(validSubmission){
                System.out.println("Submission Sucessful. New Menu Item Created.");
                //resetForm();
            }else{
                System.out.println("Error occured creating new menu item.");
            }
        });

        String selectedRowID = TableViewUtils.getSelectedRowID();
        String collectionName = TableViewUtils.getStoredCollectionName();
        if(selectedRowID!=null && collectionName.equals("Menu")){
            newMenuItemVM.setIsNewRedcordBoolean(false);
            newMenuItemVM.populateTextFields(IngredientTableView);
        }

    }

    private void resetForm(){

        IngredientAddBtn.setDisable(true);
        newMenuItemVM.setIsNewRedcordBoolean(true);
        newMenuItemVM.clearInputs();

        for(HBox labelHbox: inputLabelHbox){
            ImageView imageView = (ImageView) labelHbox.lookup(".image-view");
            if (imageView != null) {
                imageView.setVisible(false);
            }
        }

        categoryComboBox.getItems().clear();
        categoryComboBox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveMenuCategoryList()));
        categoryComboBox.getSelectionModel().selectFirst();
        newMenuItemVM.categoryProperty().set(categoryComboBox.getValue());

        currencyComboBox.getItems().clear();
        currencyComboBox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveMoneyCurrencyList()));
        currencyComboBox.getSelectionModel().selectFirst();
        newMenuItemVM.priceUOMProperty().set(currencyComboBox.getValue());

        ingredUOMCombobox.getItems().clear();
        ingredUOMCombobox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveIngredientUOMListList()));
        ingredUOMCombobox.getSelectionModel().selectFirst();
        newMenuItemVM.ingredientUOMProperty().set(ingredUOMCombobox.getValue());



        //Later replace with universal message system
        //errMessLabel.setText("");

        newMenuItemVM.menuItemIDProperty().set("New Menu Item");
        newMenuItemVM.uploadItemImage(ItemImageDisplay, "DEFAULT");
        newMenuItemVM.handleIngredientsTableView("RESET", IngredientTableView);

        newMenuItemVM.updateValidImageViews(nameHbox, newMenuItemVM.nameValidProperty());
        newMenuItemVM.updateValidImageViews(descriptionHbox, newMenuItemVM.descriptionValidProperty());
        newMenuItemVM.updateValidImageViews(categoryHbox, newMenuItemVM.categoryValidProperty());
        newMenuItemVM.updateValidImageViews(priceHbox, newMenuItemVM.priceValidProperty(), newMenuItemVM.priceUOMValidProperty());
        //newMenuItemVM.updateValidImageViews(itemImageHbox, newMenuItemVM.itemImageValidProperty());
        newMenuItemVM.updateValidImageViews(ingredientNameHbox, newMenuItemVM.ingredientNameValidProperty());
        newMenuItemVM.updateValidImageViews(ingredientQuantityHbox, newMenuItemVM.ingredientQuantityValidProperty());
        newMenuItemVM.updateValidImageViews(ingredientUOMHbox, newMenuItemVM.ingredientUOMValidProperty());
        newMenuItemVM.updateValidImageViews(linkInventoryHbox, NewMenuItemUtils.linkInventoryIdValidProp());


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
