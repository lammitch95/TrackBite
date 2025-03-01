package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.SessionManager;
import chooser.model.User;
import chooser.utils.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel {
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");;
    private StringProperty loginStatusMessage = new SimpleStringProperty("");;

    public void checkLogin() {
        String enteredUsername = username.get();
        String enteredPassword = password.get();

        User userInfo = FirestoreUtils.authenticateUser(enteredUsername, enteredPassword);
        if (userInfo != null) {
            SessionManager.setLoggedInUser(userInfo);
            SceneNavigator.switchScene(
                    "Homepage",
                    "TrackBite/Homepage",
                    -1,
                    -1,
                    true
            );
        } else {
            System.out.println("Login failed. Check credentials.");
            loginStatusMessage.set("Invalid credentials. Please try again.");
        }
    }

    public StringProperty getUsername() { return username; }
    public StringProperty getPassword() { return password; }
    public StringProperty getLoginStatusMessage() { return loginStatusMessage; }

}
