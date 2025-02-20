package chooser;

import chooser.database.FirestoreContext;
import chooser.database.FirestoreUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TrackBiteApp extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        FirestoreContext.getFirestore();

        /**This part is example usage of the read/write/delete functions******/

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Mitchell Lam");
        data.put("email", "1234Mitch@gmail.com");

        FirestoreUtils.writeDoc("Employees", "mlam1234", data);

        Map<String, Object> userData = FirestoreUtils.readDoc("Employees", "mlam1234");
        if (userData != null) {
            System.out.println("User Data: " + userData);
        }

        FirestoreUtils.deleteDoc("Employees", "mlam1234");

        /******** read/write/delete functions example ends here*****/

        FXMLLoader fxmlLoader = new FXMLLoader(TrackBiteApp.class.getResource("trackbite/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}