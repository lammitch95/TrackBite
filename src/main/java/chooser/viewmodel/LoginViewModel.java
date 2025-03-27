package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.SessionManager;
import chooser.model.User;
import chooser.utils.ProgressLoadUtils;
import chooser.utils.SceneNavigator;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class LoginViewModel {
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");;
    private StringProperty loginStatusMessage = new SimpleStringProperty("");;

    private BooleanProperty isVisiblePassword = new SimpleBooleanProperty(false);


    public  LoginViewModel(){
        isVisiblePassword.set(false);
    }
    public void togglePasswordVisibleButton(){
        isVisiblePassword.set(!isVisiblePassword.get());
    }

    public void checkLogin() {

        ProgressLoadUtils.showProgressLoad();
        Task<Void> authTask = new Task<>() {
            @Override
            protected Void call() {
                String enteredUsername = username.get();
                String enteredPassword = password.get();

                System.out.println("Checking credentials username: "+enteredUsername+" ,password: "+enteredPassword);

                User userInfo = FirestoreUtils.authenticateUser(enteredUsername, enteredPassword);
                Platform.runLater(() -> {
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

                    ProgressLoadUtils.hideProgressLoad();

                });

                return null;
            }

        };

        Thread authThread = new Thread(authTask);
        authThread.setDaemon(true);
        authThread.start();

    }

    public StringProperty getUsername() { return username; }
    public StringProperty getPassword() { return password; }
    public StringProperty getLoginStatusMessage() { return loginStatusMessage; }

    public BooleanProperty getIsVisiblePasswordProperty(){return isVisiblePassword;}

}
