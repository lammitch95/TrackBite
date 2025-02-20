module chooser.trackbite {
    requires javafx.controls;
    requires javafx.fxml;

    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires java.logging;
    requires com.google.api.apicommon;
    requires com.google.auth;
    requires google.cloud.core;

    opens chooser to javafx.fxml, javafx.graphics;

    exports chooser.view;
    opens chooser.view to javafx.fxml;
}