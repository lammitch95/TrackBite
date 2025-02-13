module chooser.trackbite {
    requires javafx.controls;
    requires javafx.fxml;


    opens chooser.trackbite to javafx.fxml;
    exports chooser.trackbite;
}