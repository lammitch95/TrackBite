package chooser.utils;

import chooser.model.LoggedOrder;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ViewLoggedOrderUtils {
    private static VBox storeView;
    private static String selectedLoggedOrder;
    private static AnchorPane storeRoot;


    public static void setStoreRoot(AnchorPane value){
        storeRoot = value;
    }
    public static void setSelectedLoggedUser(String value){
        selectedLoggedOrder = value;
    }
    public static String getSelectedLoggedUser(){
        return selectedLoggedOrder;
    }

    static{
        selectedLoggedOrder = null;
        storeView = null;
    }
    public static void displayChangeViewLoggedOrder(String status){
        switch(status){
            case "SHOW":
                try {
                    String fxmlPathFull = "/chooser/trackbite/ViewLoggedOrder.fxml";
                    FXMLLoader loader = new FXMLLoader(NewInventoryItemUtils.class.getResource(fxmlPathFull));
                    VBox view = loader.load();
                    storeView = view;
                    storeRoot.getChildren().add(view);

                    AnchorPane.setTopAnchor(view, 0.0);
                    AnchorPane.setRightAnchor(view, 0.0);
                    AnchorPane.setBottomAnchor(view, 0.0);
                    AnchorPane.setLeftAnchor(view, 0.0);

                    view.setVisible(true);
                    view.setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "HIDE":
                if(storeView != null && storeRoot!=null){
                    storeRoot.getChildren().remove(storeView);
                    storeView = null;
                }
                break;
        }
    }
}
