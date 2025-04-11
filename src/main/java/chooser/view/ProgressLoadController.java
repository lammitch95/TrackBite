package chooser.view;

import chooser.utils.ProgressLoadUtils;
import com.gluonhq.charm.glisten.control.ProgressIndicator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProgressLoadController {

    @FXML
    private ProgressIndicator progressCircle;

    @FXML
    private Label progressLbl;

    @FXML
    private VBox progressMain;


    public void initialize(){
       progressLbl.textProperty().bind(ProgressLoadUtils.currProgrresLblProperty());
    }

}
