package chooser.utils;

import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SystemMessageUtils {

    private static StringProperty currSystemColor = new SimpleStringProperty("");
    private static VBox systemMessageBox;
    private static Label systemMessageLabel;
    public static void setSystemMessageBox(VBox box) {
        systemMessageBox = box;
        systemMessageBox.setOpacity(0);
        systemMessageBox.setDisable(true);

    }

    public static void setSysteMessageLabel(Label label){
        systemMessageLabel = label;
    }


    public static void setCurrPropertyColor(String colorCode){
        switch (colorCode){
            case "ERROR":
                currSystemColor.set("red");
                break;
            case "SUCCESS":
                currSystemColor.set("#11e302");
                break;
            default:
                System.out.println("System Message Color code doesnt exist: " + colorCode);

        }

        systemMessageBox.setStyle("-fx-background-color: " + currSystemColor.get() + ";");
    }

    public static void setCurrSystemText(String value){
        systemMessageLabel.setText("");
        systemMessageLabel.setText(value);
    }

    public static void messageAnimation(){

        if (systemMessageBox == null) {
            System.out.println("SystemMessageBox is not set!");
            return;
        }

        System.out.println("Label opacity before animation: " + systemMessageLabel.getOpacity());
        System.out.println("Label Text before animation: " + systemMessageLabel.getText());


        systemMessageBox.setDisable(false);
        systemMessageBox.setOpacity(1);

        systemMessageLabel.setVisible(true);
        systemMessageLabel.setOpacity(1);
        systemMessageLabel.setDisable(false);

        double initialY = systemMessageBox.getTranslateY();
        systemMessageBox.setTranslateY(initialY - 20);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), systemMessageBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition moveDown = new TranslateTransition(Duration.millis(500), systemMessageBox);
        moveDown.setToY(initialY);

        PauseTransition holdDuration = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), systemMessageBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(500), systemMessageBox);
        moveUp.setToY(initialY - 20);

        ParallelTransition firstPart = new ParallelTransition(fadeIn, moveDown);
        ParallelTransition secondPart = new ParallelTransition(fadeOut, moveUp);

        SequentialTransition sequence = new SequentialTransition(
                firstPart, holdDuration, secondPart
        );

        sequence.setOnFinished(event -> {
            systemMessageBox.setTranslateY(initialY);
            systemMessageBox.setDisable(true);
            systemMessageBox.setOpacity(0);
        });
        sequence.play();


    }

}
