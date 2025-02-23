package chooser.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPage {

    @FXML
    private TextField usernameField; // reference username input

    @FXML
    private PasswordField passwordField;  //reference Password input

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // authentication check
        if ("admin".equals(username) && "password".equals(password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials");
        }
    }
}