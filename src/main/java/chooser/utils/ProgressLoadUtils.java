package chooser.utils;

import chooser.view.ProgressLoadController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ProgressLoadUtils {

    private static AnchorPane rootPane;
    private static Node progressLoadNode;

    private static StringProperty currProgressLbl = new SimpleStringProperty("");


    static{
        currProgressLbl.set("");
    }
    public static void setUpProgressLoad(AnchorPane root) {

        if (progressLoadNode != null && rootPane != null) {
            rootPane.getChildren().remove(progressLoadNode);
            progressLoadNode = null;
        }

        if (progressLoadNode == null) {
            try {

                String fxmlPathFull = "/chooser/trackbite/ProgressLoad.fxml";
                FXMLLoader loader = new FXMLLoader(ProgressLoadUtils.class.getResource(fxmlPathFull));
                progressLoadNode = loader.load();

                ProgressLoadController controller = loader.getController();
                rootPane = root;

                AnchorPane.setTopAnchor(progressLoadNode, 0.0);
                AnchorPane.setBottomAnchor(progressLoadNode, 0.0);
                AnchorPane.setLeftAnchor(progressLoadNode, 0.0);
                AnchorPane.setRightAnchor(progressLoadNode, 0.0);

                root.getChildren().add(progressLoadNode);

                progressLoadNode.toFront();

                progressLoadNode.setVisible(false);
                progressLoadNode.setDisable(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void toggleProgressLoadVisibility(boolean visible) {
        if (progressLoadNode != null) {
            progressLoadNode.setVisible(visible);
            progressLoadNode.setDisable(!visible);
        }
    }

    public static void resetProgessLoadNode(){
        progressLoadNode = null;
    }

    public static void setProgressText(String text) {
        currProgressLbl.set(text);
    }

    public static StringProperty currProgrresLblProperty(){
        return currProgressLbl;
    }

    public static void hideProgressLoad() {
        toggleProgressLoadVisibility(false);
    }

    public static void showProgressLoad() {
        toggleProgressLoadVisibility(true);
    }


}
