package chooser.view;

import chooser.utils.NewMenuItemUtils;
import chooser.utils.SupplierInfoUtils;
import chooser.utils.TableViewUtils;
import chooser.viewmodel.SupplierInfoViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class SupplierInfoController {

    @FXML
    private HBox addNewBtn;

    @FXML
    private TextField ba_addressLineText;

    @FXML
    private HBox ba_adressLineHbox;

    @FXML
    private HBox ba_cityHbox;

    @FXML
    private TextField ba_cityText;

    @FXML
    private ComboBox<String> ba_countryComboxBox;

    @FXML
    private HBox ba_countryHbox;

    @FXML
    private HBox ba_postalCodeHbox;

    @FXML
    private TextField ba_postalCodeText;

    @FXML
    private HBox ba_stateHbox;

    @FXML
    private TextField ba_statsText;

    @FXML
    private HBox deleteBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private HBox descriptionHbox;

    @FXML
    private TextField descriptionText;

    @FXML
    private HBox emailHbox;

    @FXML
    private TextField emailText;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private HBox nameHbox;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private HBox phoneNumHbox;

    @FXML
    private TextField phoneNumText;

    @FXML
    private CheckBox sameBACheckBox;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private HBox stockStatusHbox;

    @FXML
    private HBox supplierNameHbox;

    @FXML
    private TextField supplierNameText;

    @FXML
    private HBox totalStockHbox;

    @FXML
    private TextField w_addressLineText;

    @FXML
    private HBox w_adressLibeHbox;

    @FXML
    private HBox w_cityHbox;

    @FXML
    private TextField w_cityText;

    @FXML
    private ComboBox<String> w_countryComboBox;

    @FXML
    private HBox w_countryHbox;

    @FXML
    private HBox w_postalCodeHbox;

    @FXML
    private TextField w_postalCodeText;

    @FXML
    private HBox w_stateHbox;

    @FXML
    private TextField w_stateText;

    @FXML
    private HBox websiteHbox;

    @FXML
    private TextField websiteText;

    private SupplierInfoViewModel supInfoVM;
    private List<HBox> inputLabelHbox;

    @FXML
    private void initialize(){


        supInfoVM = new SupplierInfoViewModel();

        saveBtn.disableProperty().bind(supInfoVM.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(supInfoVM.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(
                supplierNameHbox,
                descriptionHbox,
                nameHbox,
                phoneNumHbox,
                emailHbox,
                websiteHbox,
                ba_adressLineHbox,
                ba_cityHbox,
                ba_stateHbox,
                ba_postalCodeHbox,
                ba_countryHbox,
                w_adressLibeHbox,
                w_cityHbox,
                w_stateHbox,
                w_postalCodeHbox,
                w_countryHbox
        );

        supplierNameText.textProperty().bindBidirectional(supInfoVM.supplierNameProp());
        descriptionText.textProperty().bindBidirectional(supInfoVM.descriptionProp());

        firstNameText.textProperty().bindBidirectional(supInfoVM.primContactFirstNameProp());
        lastnameText.textProperty().bindBidirectional(supInfoVM.primContactLastNameProp());
        phoneNumText.textProperty().bindBidirectional(supInfoVM.primContactPhoneNumProp());
        emailText.textProperty().bindBidirectional(supInfoVM.primContactEmailProp());
        websiteText.textProperty().bindBidirectional(supInfoVM.primContactWebsiteProp());

        ba_addressLineText.textProperty().bindBidirectional(supInfoVM.ba_addressLineProp());
        ba_cityText.textProperty().bindBidirectional(supInfoVM.ba_cityProp());
        ba_statsText.textProperty().bindBidirectional(supInfoVM.ba_stateProp());
        ba_postalCodeText.textProperty().bindBidirectional(supInfoVM.ba_postalCodeProp());

        ba_countryComboxBox.setOnAction(e -> {
            String selectedItem = ba_countryComboxBox.getValue();
            supInfoVM.ba_countryProp().set(selectedItem);
        });

        w_addressLineText.textProperty().bindBidirectional(supInfoVM.w_addressLineProp());
        w_cityText.textProperty().bindBidirectional(supInfoVM.w_cityProp());
        w_stateText.textProperty().bindBidirectional(supInfoVM.w_stateProp());
        w_postalCodeText.textProperty().bindBidirectional(supInfoVM.w_postalCodeProp());

        w_countryComboBox.setOnAction(e -> {
            String selectedItem = w_countryComboBox.getValue();
            supInfoVM.w_countryProp().set(selectedItem);
        });


        supInfoVM.ba_countryProp().addListener((obs, oldVal, newVal) -> {
            if (ba_countryComboxBox.getItems().contains(newVal)) {
                ba_countryComboxBox.getSelectionModel().select(newVal);
            }
        });

        supInfoVM.w_countryProp().addListener((obs, oldVal, newVal) -> {
            if (w_countryComboBox.getItems().contains(newVal)) {
                w_countryComboBox.getSelectionModel().select(newVal);
            }
        });

        addValidationStyle(supplierNameText, supInfoVM.validSupplierNameProp());
        addValidationStyle(firstNameText, supInfoVM.validPrimContactFirstNameProp());
        addValidationStyle(lastnameText, supInfoVM.validPrimContactLastNameProp());
        addValidationStyle(phoneNumText, supInfoVM.validPrimContactPhoneNumProp());
        addValidationStyle(emailText, supInfoVM.validPrimContactEmailProp());

        addValidationStyle(ba_addressLineText, supInfoVM.valid_ba_addressLineProp());
        addValidationStyle(ba_cityText, supInfoVM.valid_ba_cityProp());
        addValidationStyle(ba_statsText, supInfoVM.valid_ba_stateProp());
        addValidationStyle(ba_postalCodeText, supInfoVM.valid_ba_postalCodeProp());
        addValidationStyle(ba_countryComboxBox, supInfoVM.valid_ba_countryProp());

        addValidationStyle(w_addressLineText, supInfoVM.valid_w_addressLineProp());
        addValidationStyle(w_cityText, supInfoVM.valid_w_cityProp());
        addValidationStyle(w_stateText, supInfoVM.valid_w_stateProp());
        addValidationStyle(w_postalCodeText, supInfoVM.valid_w_postalCodeProp());
        addValidationStyle(w_countryComboBox, supInfoVM.valid_w_countryProp());

        supInfoVM.supplierIdProp().addListener((obs, oldVal, newVal) -> {
            employeeIDLabel.setText(newVal);
        });

        supInfoVM.validSupplierNameProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(supplierNameHbox, supInfoVM.validSupplierNameProp());
        });

        supInfoVM.validSupplierNameProp().set(false);

        supInfoVM.validPrimContactFirstNameProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(nameHbox, supInfoVM.validPrimContactFirstNameProp(), supInfoVM.validPrimContactLastNameProp());
        });

        supInfoVM.validPrimContactLastNameProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(nameHbox, supInfoVM.validPrimContactLastNameProp(), supInfoVM.validPrimContactFirstNameProp());
        });

        supInfoVM.validPrimContactPhoneNumProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(phoneNumHbox, supInfoVM.validPrimContactPhoneNumProp());
        });

        supInfoVM.validPrimContactPhoneNumProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(phoneNumHbox, supInfoVM.validPrimContactPhoneNumProp());
        });

        supInfoVM.validPrimContactEmailProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(emailHbox, supInfoVM.validPrimContactEmailProp());
        });

        supInfoVM.valid_ba_addressLineProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(ba_adressLineHbox, supInfoVM.valid_ba_addressLineProp());
        });
        supInfoVM.valid_ba_cityProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(ba_cityHbox, supInfoVM.valid_ba_cityProp());
        });
        supInfoVM.valid_ba_stateProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(ba_stateHbox, supInfoVM.valid_ba_stateProp());
        });
        supInfoVM.valid_ba_postalCodeProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(ba_postalCodeHbox, supInfoVM.valid_ba_postalCodeProp());
        });
        supInfoVM.valid_ba_countryProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(ba_countryHbox, supInfoVM.valid_ba_countryProp());
        });

        supInfoVM.valid_w_addressLineProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(w_adressLibeHbox, supInfoVM.valid_w_addressLineProp());
        });
        supInfoVM.valid_w_cityProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(w_cityHbox, supInfoVM.valid_w_cityProp());
        });
        supInfoVM.valid_w_stateProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(w_stateHbox, supInfoVM.valid_w_stateProp());
        });
        supInfoVM.valid_w_postalCodeProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(w_postalCodeHbox, supInfoVM.valid_w_postalCodeProp());
        });
        supInfoVM.valid_w_countryProp().addListener((obs, oldVal, newVal) -> {
            supInfoVM.updateValidImageViews(w_countryHbox, supInfoVM.valid_w_countryProp());
        });


        resetForm();

        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());

        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = supInfoVM.onSubmit();
            if(validSubmission){
                System.out.println("Submission Sucessful. New Supplier Created.");
            }else{
                System.out.println("Error occured creating new supplier.");
            }
        });

        String selectedRowID = TableViewUtils.getSelectedRowID();
        String collectionName = TableViewUtils.getStoredCollectionName();
        if(selectedRowID!=null && collectionName.equals("SuppliersV2")){
            supInfoVM.setIsNewRedcordBoolean(false);
            supInfoVM.populateTextFields();
        }

    }




    private void resetForm(){
        supInfoVM.setIsNewRedcordBoolean(true);
        supInfoVM.clearInputs();

        ba_countryComboxBox.getItems().clear();
        ba_countryComboxBox.setItems(FXCollections.observableArrayList(SupplierInfoUtils.retrieveCountriesList()));
        ba_countryComboxBox.getSelectionModel().selectFirst();
        supInfoVM.ba_countryProp().set(ba_countryComboxBox.getValue());

        w_countryComboBox.getItems().clear();
        w_countryComboBox.setItems(FXCollections.observableArrayList(SupplierInfoUtils.retrieveCountriesList()));
        w_countryComboBox.getSelectionModel().selectFirst();
        supInfoVM.w_countryProp().set(w_countryComboBox.getValue());

        supInfoVM.updateValidImageViews(supplierNameHbox, supInfoVM.validSupplierNameProp());
        supInfoVM.updateValidImageViews(nameHbox, supInfoVM.validPrimContactFirstNameProp(), supInfoVM.validPrimContactLastNameProp());
        supInfoVM.updateValidImageViews(phoneNumHbox, supInfoVM.validPrimContactPhoneNumProp());
        supInfoVM.updateValidImageViews(emailHbox, supInfoVM.validPrimContactEmailProp());

        supInfoVM.updateValidImageViews(ba_adressLineHbox, supInfoVM.valid_ba_addressLineProp());
        supInfoVM.updateValidImageViews(ba_cityHbox, supInfoVM.valid_ba_cityProp());
        supInfoVM.updateValidImageViews(ba_stateHbox, supInfoVM.valid_ba_stateProp());
        supInfoVM.updateValidImageViews(ba_postalCodeHbox, supInfoVM.valid_ba_postalCodeProp());
        supInfoVM.updateValidImageViews(ba_countryHbox, supInfoVM.valid_ba_countryProp());

        supInfoVM.updateValidImageViews(w_adressLibeHbox, supInfoVM.valid_w_addressLineProp());
        supInfoVM.updateValidImageViews(w_cityHbox, supInfoVM.valid_w_cityProp());
        supInfoVM.updateValidImageViews(w_stateHbox, supInfoVM.valid_w_stateProp());
        supInfoVM.updateValidImageViews(w_postalCodeHbox, supInfoVM.valid_w_postalCodeProp());
        supInfoVM.updateValidImageViews(w_countryHbox, supInfoVM.valid_w_countryProp());

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
