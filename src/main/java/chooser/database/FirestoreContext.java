package chooser.database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreContext {

    private static Firestore firestore;

    public static Firestore getFirestore() throws IOException {
        if (firestore == null) {
            initializeFirestore();
        }
        return firestore;
    }

    public static void initializeFirestore() {
        try {
            FileInputStream serviceAccount = new FileInputStream("key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
            System.out.println("✅ Firebase Firestore initialized.");

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}
