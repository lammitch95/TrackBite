package chooser.database;

import chooser.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;
import chooser.model.MenuItem;



public class FirestoreUtils {


    public static void writeDoc(String collectionName, String documentId, Map<String, Object> data){
        try {

            Firestore db = FirestoreContext.getFirestore();
            DocumentReference docRef = db.collection(collectionName).document(documentId);

            ApiFuture<WriteResult> result = docRef.set(data);

            System.out.println("Data written at " + result.get().getUpdateTime());

        } catch (InterruptedException | ExecutionException | IOException e) {

            System.err.println("Error writing data: " + e.getMessage());
        }
    }

    public static Map<String, Object> readDoc(String collectionName, String documentId) {
        try {
            Firestore db = FirestoreContext.getFirestore();

            DocumentReference docRef = db.collection(collectionName).document(documentId);

            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                return document.getData();
            } else {
                System.out.println("No such document!");
                return null;
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            System.err.println("Error reading data: " + e.getMessage());
            return null;
        }
    }

    public static void deleteDoc(String collectionName, String documentId) {
        try {
            Firestore db = FirestoreContext.getFirestore();

            DocumentReference docRef = db.collection(collectionName).document(documentId);

            ApiFuture<WriteResult> result = docRef.delete();

            System.out.println("Document deleted at " + result.get().getUpdateTime());

        } catch (InterruptedException | ExecutionException | IOException e) {

            System.err.println("Error deleting data: " + e.getMessage());
        }
    }


    public static User authenticateUser(String username, String password){
        try {
            Query query = FirestoreContext.getFirestore().collection("Employees")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password);

            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot document = querySnapshot.getDocuments().getFirst(); // Get first document if found
                return createUserFromDocument(document);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static User createUserFromDocument(DocumentSnapshot document) {
        String userId = document.getId();
        String username = document.getString("username");
        String password = document.getString("password");
        String firstName = document.getString("firstName");
        String lastName = document.getString("lastName");
        String dob = document.getString("dob");
        String phoneNum = document.getString("phoneNum");
        String role = document.getString("role");
        return new User(userId, username, password, firstName, lastName, dob, phoneNum, role);
    }

    public static void updateDoc(String collectionName, String documentId, Map<String, Object> updatedData) {
        try {
            Firestore db = FirestoreContext.getFirestore();
            DocumentReference docRef = db.collection(collectionName).document(documentId);

            ApiFuture<WriteResult> result = docRef.update(updatedData);

            System.out.println("Document updated at: " + result.get().getUpdateTime());

        } catch (InterruptedException | ExecutionException | IOException e) {
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public static List<MenuItem> readAllDocs(String collectionName) {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            Firestore db = FirestoreContext.getFirestore();
            ApiFuture<QuerySnapshot> query = db.collection(collectionName).get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                MenuItem item = new MenuItem(
                        document.getId(),
                        document.getString("itemName"),
                        document.getString("description"),
                        document.getString("price"),
                        document.getString("category"),
                        (List<String>) document.get("ingredients"),
                        document.getString("imageUrl")
                );
                menuItems.add(item);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving menu items: " + e.getMessage());
        }
        return menuItems;
    }




}



