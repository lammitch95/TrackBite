package chooser.view;

import chooser.utils.TableViewUtils;
import chooser.viewmodel.NewUserFormViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class NewUserFormController {

    @FXML
    private HBox addNewBtn;

    @FXML
    private HBox confirmPassHbox;

    @FXML
    private TextField confirmPassText;

    @FXML
    private HBox createPassHbox;

    @FXML
    private TextField createPassText;

    @FXML
    private HBox deleteBtn;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private HBox dobHbox;

    @FXML
    private TextField dobText;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private RadioButton managerRadio;

    @FXML
    private HBox nameHbox;

    @FXML
    private BorderPane newUserFormPane;

    @FXML
    private TextArea passRulesText;

    @FXML
    private HBox phoneNumHbox;

    @FXML
    private TextField phoneNumText;

    @FXML
    private HBox roleHbox;

    @FXML
    private HBox roleHbox1;

    @FXML
    private ToggleGroup roleToggleGroup;

    @FXML
    private HBox saveBtn;

    @FXML
    private ImageView saveBtnImageView;

    @FXML
    private RadioButton waitStaffRadio;

    private NewUserFormViewModel newUserformViewModel;
    private List<HBox> inputLabelHbox;

    @FXML
    public void initialize(){
        newUserformViewModel = new NewUserFormViewModel();

        saveBtn.disableProperty().bind(newUserformViewModel.allowSaveProperty().not());

        saveBtnImageView.opacityProperty().bind(
                Bindings.when(newUserformViewModel.allowSaveProperty())
                        .then(1.0)
                        .otherwise(0.25)
        );

        inputLabelHbox = List.of(nameHbox,phoneNumHbox,dobHbox,confirmPassHbox,createPassHbox,roleHbox);

        firstnameText.textProperty().bindBidirectional(newUserformViewModel.firstNameProperty());
        lastnameText.textProperty().bindBidirectional(newUserformViewModel.lastNameProperty());
        dobText.textProperty().bindBidirectional(newUserformViewModel.dateOfBirthProperty());
        phoneNumText.textProperty().bindBidirectional(newUserformViewModel.phoneNumberProperty());
        createPassText.textProperty().bindBidirectional(newUserformViewModel.passwordProperty());
        confirmPassText.textProperty().bindBidirectional(newUserformViewModel.confirmPasswordProperty());

        addValidationStyle(firstnameText, newUserformViewModel.firstNameValidProperty());
        addValidationStyle(lastnameText, newUserformViewModel.lastNameValidProperty());
        addValidationStyle(dobText, newUserformViewModel.dobValidProperty());
        addValidationStyle(phoneNumText, newUserformViewModel.phoneValidProperty());
        addValidationStyle(createPassText, newUserformViewModel.passwordValidProperty());
        addValidationStyle(confirmPassText, newUserformViewModel.confirmPasswordValidProperty());

        newUserformViewModel.firstNameValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(nameHbox, newUserformViewModel.firstNameValidProperty(), newUserformViewModel.lastNameValidProperty());
        });

        newUserformViewModel.lastNameValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(nameHbox, newUserformViewModel.firstNameValidProperty(), newUserformViewModel.lastNameValidProperty());
        });

        newUserformViewModel.dobValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(dobHbox, newUserformViewModel.dobValidProperty());
        });

        newUserformViewModel.phoneValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(phoneNumHbox, newUserformViewModel.phoneValidProperty());
        });

        newUserformViewModel.passwordValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(createPassHbox, newUserformViewModel.passwordValidProperty());
        });

        newUserformViewModel.confirmPasswordValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(confirmPassHbox, newUserformViewModel.confirmPasswordValidProperty());
        });

        newUserformViewModel.roleValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(roleHbox, newUserformViewModel.roleValidProperty());
        });


        newUserformViewModel.roleValidProperty().addListener((obs, oldVal, newVal) -> {
            newUserformViewModel.updateValidImageViews(roleHbox, newUserformViewModel.roleValidProperty());
        });

        newUserformViewModel.usernameValidProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println();
            employeeIDLabel.setText(newUserformViewModel.usernameProperty().get());
        });

        roleToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedRadio = (RadioButton) newToggle;
                newUserformViewModel.roleProperty().set(selectedRadio.getText());
            }
        });

        newUserformViewModel.roleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                for (Toggle toggle : roleToggleGroup.getToggles()) {
                    RadioButton radioButton = (RadioButton) toggle;
                    if (radioButton.getText().equals(newValue)) {
                        roleToggleGroup.selectToggle(radioButton);
                        break;
                    }
                }
            }
        });

        resetForm();


        addNewBtn.setOnMouseClicked(mouseEvent -> resetForm());


        saveBtn.setOnMouseClicked(mouseEvent -> {
            boolean validSubmission = newUserformViewModel.onSubmit();
            if(validSubmission){
                System.out.println("Submission Sucessful. New User Created.");
                //resetForm();
            }else{
                System.out.println("Error occured creating new user.");
            }
        });

        String selectedRowID = TableViewUtils.getSelectedRowID();
        String collectionName = TableViewUtils.getStoredCollectionName();
        if(selectedRowID!=null && collectionName.equals("Employees")){
            newUserformViewModel.setIsNewRedcordBoolean(false);
            newUserformViewModel.populateTextFields();
        }


    }

    private void resetForm(){
        newUserformViewModel.setIsNewRedcordBoolean(true);
        newUserformViewModel.clearInputs();

        for(HBox labelHbox: inputLabelHbox){
            ImageView imageView = (ImageView) labelHbox.lookup(".image-view");
            if (imageView != null) {
                imageView.setVisible(false);
            }
        }

        roleToggleGroup.selectToggle(waitStaffRadio);
        newUserformViewModel.roleProperty().set("Wait Staff");
        newUserformViewModel.updateValidImageViews(roleHbox, newUserformViewModel.roleValidProperty());

        //Later replace with universal message system
        //errMessLabel.setText("");

        newUserformViewModel.usernameProperty().set("New User");

    }
    private void addValidationStyle(TextField field, BooleanProperty validProperty) {
        validProperty.addListener((obs, wasValid, isValid) -> {

            System.out.println("Field: " + field.getId() + " Valid: " + isValid);
            field.getStyleClass().removeAll("valid");

            if (isValid) {
                field.getStyleClass().add("valid");
            }
        });
    }

}
