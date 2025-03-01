package chooser.view;

import chooser.utils.SceneNavigator;
import chooser.viewmodel.LoginViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoginPageController {

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button loginBtn;

    @FXML
    private Pane loginLogoImage;

    @FXML
    private HBox loginPane;

    @FXML
    private VBox loginVBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final LoginViewModel loginViewModel;

    public LoginPageController(){
        loginViewModel = new LoginViewModel();
    }

    public void initialize(){
        usernameField.textProperty().bindBidirectional(loginViewModel.getUsername());
        passwordField.textProperty().bindBidirectional(loginViewModel.getPassword());
        errorMessageLabel.textProperty().bindBidirectional(loginViewModel.getLoginStatusMessage());
    }
    @FXML
    void onHandleLogin(ActionEvent event) {
        loginViewModel.checkLogin();
    }

}
