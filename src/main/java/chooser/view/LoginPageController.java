package chooser.view;

import chooser.utils.ProgressLoadUtils;
import chooser.utils.SceneNavigator;
import chooser.viewmodel.LoginViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Objects;

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
    private AnchorPane rootPane;

    @FXML
    private HBox showPasswordBtn;

    @FXML
    private ImageView showPasswordImageView;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField visiblePasswordText;

    private LoginViewModel loginViewModel;

    public void initialize(){

        loginViewModel = new LoginViewModel();
        usernameField.textProperty().bindBidirectional(loginViewModel.getUsername());

        passwordField.textProperty().bindBidirectional(loginViewModel.getPassword());
        visiblePasswordText.textProperty().bindBidirectional(loginViewModel.getPassword());

        passwordField.visibleProperty().bind(loginViewModel.getIsVisiblePasswordProperty().not());
        passwordField.disableProperty().bind(loginViewModel.getIsVisiblePasswordProperty());

        visiblePasswordText.visibleProperty().bind(loginViewModel.getIsVisiblePasswordProperty());
        visiblePasswordText.disableProperty().bind(loginViewModel.getIsVisiblePasswordProperty().not());


        errorMessageLabel.textProperty().bindBidirectional(loginViewModel.getLoginStatusMessage());

        showPasswordBtn.setOnMouseClicked(mouseEvent -> loginViewModel.togglePasswordVisibleButton());

        Image showPasswordImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/chooser/trackbite/grayShowPassword.png")));
        Image hidePasswordImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/chooser/trackbite/grayHidePassword.png")));

        showPasswordImageView.imageProperty().bind(Bindings.when(loginViewModel.getIsVisiblePasswordProperty())
                .then(showPasswordImage)
                .otherwise(hidePasswordImage)
        );

    }
    @FXML
    void onHandleLogin(ActionEvent event) {loginViewModel.checkLogin();}



}
