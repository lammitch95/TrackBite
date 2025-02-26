package chooser.model;

public class SessionManager {

    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isUserLoggedIn() {
        return loggedInUser != null;
    }
}
