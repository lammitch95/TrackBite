package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.utils.NewUserUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NewUserFormViewModel {

    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty dateOfBirth = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");
    private final StringProperty role = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty confirmPassword = new SimpleStringProperty("");


    private final BooleanProperty usernameValid = new SimpleBooleanProperty(false);
    private final BooleanProperty firstNameValid = new SimpleBooleanProperty(false);
    private final BooleanProperty lastNameValid = new SimpleBooleanProperty(false);
    private final BooleanProperty dobValid = new SimpleBooleanProperty(false);
    private final BooleanProperty phoneValid = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordValid = new SimpleBooleanProperty(false);
    private final BooleanProperty confirmPasswordValid = new SimpleBooleanProperty(false);

    private final BooleanProperty roleValid = new SimpleBooleanProperty(true);

    private final BooleanProperty formValid = new SimpleBooleanProperty(false);

    private List<BooleanProperty> validInputList;
    private List<StringProperty> userInputList;

    public NewUserFormViewModel() {

        username.addListener((obs, oldVal, newVal) -> usernameValid.set(!username.get().equals("Not Available")));
        firstName.addListener((obs, oldVal, newVal) -> firstNameValid.set(NewUserUtils.isValidName(newVal)));
        lastName.addListener((obs, oldVal, newVal) -> lastNameValid.set(NewUserUtils.isValidName(newVal)));
        dateOfBirth.addListener((obs, oldVal, newVal) -> dobValid.set(NewUserUtils.isValidDateOfBirth(newVal)));
        phoneNumber.addListener((obs, oldVal, newVal) -> phoneValid.set(NewUserUtils.isValidPhoneNum(newVal)));
        password.addListener((obs, oldVal, newVal) -> passwordValid.set(NewUserUtils.isValidPassword(newVal)));
        confirmPassword.addListener((obs, oldVal, newVal) -> confirmPasswordValid.set(NewUserUtils.isValidConfirmPassword(password.get(), newVal)));
        role.addListener((obs, oldVal, newVal) -> roleValid.set(!role.get().isEmpty()));

        validInputList = List.of(firstNameValid,lastNameValid,roleValid,dobValid,phoneValid, passwordValid,confirmPasswordValid);
        userInputList = List.of(username,firstName,lastName,role,dateOfBirth,phoneNumber,password,confirmPassword);

        formValid.bind(firstNameValidProperty()
                        .and(lastNameValidProperty())
                        .and(dobValidProperty())
                        .and(phoneValidProperty())
                        .and(passwordValidProperty())
                        .and(confirmPasswordValidProperty())
                        .and(roleValidProperty())
        );
    }

    public StringProperty usernameProperty(){return username;}
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty roleProperty() { return role; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty confirmPasswordProperty() { return confirmPassword; }

    public BooleanProperty usernameValidProperty(){return usernameValid;}
    public BooleanProperty firstNameValidProperty() { return firstNameValid; }
    public BooleanProperty lastNameValidProperty() { return lastNameValid; }
    public BooleanProperty dobValidProperty() { return dobValid; }
    public BooleanProperty phoneValidProperty() { return phoneValid; }
    public BooleanProperty passwordValidProperty() { return passwordValid; }
    public BooleanProperty confirmPasswordValidProperty() { return confirmPasswordValid; }

    public BooleanProperty roleValidProperty(){return roleValid;}

    public BooleanProperty formValidProperty() {
        return formValid;
    }


    public String generateEmployeeID() {
        String firstInitial = firstName.get().substring(0, 1).toLowerCase();
        String lastNameStr = lastName.get().toLowerCase();
        String randomDigits = String.format("%04d", new Random().nextInt(10000));

        return firstInitial + lastNameStr + randomDigits;
    }



    public boolean onSubmit(){

        for(BooleanProperty validInput: validInputList){
            if(!validInput.get()){
                return false;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username.get());
        data.put("password", password.get());
        data.put("role", role.get());
        data.put("firstName", firstName.get());
        data.put("lastName", lastName.get());
        data.put("dob", dateOfBirth.get());
        data.put("phoneNum", phoneNumber.get());

        FirestoreUtils.writeDoc("Employees", username.get(), data);

        return true;
    }

    public void clearInputs(){
        for(StringProperty userInput: userInputList){
            userInput.set("");
        }
    }

    public void updateValidImageViews(HBox hBox, BooleanProperty... properties) {

        ImageView imageView = (ImageView) hBox.lookup(".image-view");
        Label label = (Label) hBox.lookup(".label");

        if (imageView != null) {
            boolean allTrue = true;
            for (BooleanProperty property : properties) {
                if (!property.get()) {
                    allTrue = false;
                    break;
                }
            }

            if (allTrue) {
                imageView.setVisible(true);
                Image validImage = new Image(getClass().getResource("/chooser/trackbite/valid.png").toExternalForm());
                imageView.setImage(validImage);

                if(label.getText().equals("Name")){
                    String newEmployeeId = generateEmployeeID();
                    username.set(newEmployeeId);
                }

            } else {
                imageView.setVisible(false);

                if(label.getText().equals("Name")){
                    username.set("Not Available");
                }
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }


}
