package chooser.viewmodel;

import chooser.model.CurrentPageOptions;
import chooser.model.NavOptions;
import chooser.model.SessionManager;
import chooser.utils.SceneNavigator;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomepageViewModel {

    private final Map<String, List<NavOptions>> navMap = new HashMap<>();
    private final StringProperty currentPage = new SimpleStringProperty("Inventory");
    private final BooleanProperty isSettingOpen = new SimpleBooleanProperty(false);

    private Parent previousView;



    public HomepageViewModel(){
        navMap.put("Accounts", List.of(
                new NavOptions("New User", "newUserBtn", "newUser.png"),
                new NavOptions("View Accounts", "viewAccountsBtn", "viewAccounts.png")

        ));
        navMap.put("Menu", List.of(
                new NavOptions("New Menu Item", "newMenuItemBtn", "newMenuItem.png"),
                new NavOptions("View Menu Items", "viewMenuBtn", "viewMenuItemsIcon.png")
        ));


    }


    public Map<String, List<NavOptions>> getNavMap() {
        return navMap;
    }
    public boolean getIsSettingOpen(){return  isSettingOpen.get();}
    public void setIsSettingOpen(boolean value){isSettingOpen.set(value);}
    public BooleanProperty currentIsSettingOpenProp(){return  isSettingOpen;}

    public StringProperty currentPageProperty() {return currentPage;}

    public String getCurrentPage() {return currentPage.get();}

    public void setCurrentPage(String page) {currentPage.set(page);}


    public void loadNewUserForm(){

    }

    public void userLogOut(){
        SessionManager.setLoggedInUser(null);
        SceneNavigator.switchScene(
                "LoginPage",
                "TrackBite/Login",
                -1,
                -1,
                true
        );
    }
}
