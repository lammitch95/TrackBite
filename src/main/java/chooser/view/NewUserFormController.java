package chooser.view;

import chooser.viewmodel.NewUserFormViewModel;
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
    private Button clearBtn;

    @FXML
    private HBox confirmPassHbox;

    @FXML
    private TextField confirmPassText;

    @FXML
    private HBox createPassHbox;

    @FXML
    private TextField createPassText;

    @FXML
    private Label deliveryFormLabel;

    @FXML
    private HBox dobHbox;

    @FXML
    private TextField dobText;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private Label errMessLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private Label formDescriplbl;

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
    private Label patientInfoLbl;

    @FXML
    private HBox phoneNumHbox;

    @FXML
    private TextField phoneNumText;

    @FXML
    private HBox roleHbox;

    @FXML
    private Button submitBtn;

    @FXML
    private RadioButton waitStaffRadio;

    @FXML
    private ToggleGroup roleToggleGroup;

    private final NewUserFormViewModel newUserformViewModel = new NewUserFormViewModel();
    private List<HBox> inputLabelHbox;

    @FXML
    public void initialize(){

        submitBtn.disableProperty().bind(newUserformViewModel.formValidProperty().not());

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
            employeeIDLabel.setText("Employee ID: "+newUserformViewModel.usernameProperty().get());
        });

        roleToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedRadio = (RadioButton) newToggle;
                newUserformViewModel.roleProperty().set(selectedRadio.getText());
            }
        });

        resetForm();


    }

    @FXML
    void onClearText(ActionEvent event) {
        resetForm();
    }

    @FXML
    void onSubmitUserForm(ActionEvent event) {
        boolean validSubmission = newUserformViewModel.onSubmit();
        if(validSubmission){
            System.out.println("Submission Sucessful. New User Created.");
            resetForm();
            errMessLabel.setText("Submission Successful. Account Created.");
        }else{
            System.out.println("Error occured creating new user.");
        }
    }


    private void resetForm(){
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

        errMessLabel.setText("");
        newUserformViewModel.usernameProperty().set("Not Available");
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
