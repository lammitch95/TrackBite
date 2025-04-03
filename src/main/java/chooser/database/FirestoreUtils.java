package chooser.database;

import chooser.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreUtils {

    private static HashMap<String, HashMap<String, Map<String, Object>>> collectionMap = new HashMap<>();



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

    public static boolean deleteDoc(String collectionName, String documentId) {
        try {
            Firestore db = FirestoreContext.getFirestore();

            DocumentReference docRef = db.collection(collectionName).document(documentId);

            ApiFuture<WriteResult> result = docRef.delete();

            WriteResult writeResult = result.get();

            if (writeResult.getUpdateTime() != null) {
                System.out.println("Document deleted at " + writeResult.getUpdateTime());
                return true;
            }

        } catch (InterruptedException | ExecutionException | IOException e) {

            System.err.println("Error deleting data: " + e.getMessage());
        }

        return false;
    }

    public static QuerySnapshot getAllDocuments(String collectionName) {
        List<Map<String, Object>> documentsList = new ArrayList<>();

        try {

            Firestore db = FirestoreContext.getFirestore();
            CollectionReference collectionRef = db.collection(collectionName);

            ApiFuture<QuerySnapshot> future = collectionRef.get();
            QuerySnapshot querySnapshot = future.get();

            return querySnapshot;

        } catch (InterruptedException | ExecutionException | IOException e) {
            System.err.println("Error fetching documents: " + e.getMessage());
        }

        return null;
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

    public static User createUserFromDocument(DocumentSnapshot document) {
        String userId = document.getId();
        String username = document.getString("username");
        String firstName = document.getString("firstName");
        String lastName = document.getString("lastName");
        String dob = document.getString("dob");
        String phoneNum = document.getString("phoneNum");
        String role = document.getString("role");
        String password = document.getString("password");
        return new User(userId, username, firstName, lastName, dob, phoneNum, role, password);
    }

    public static List<Map<String, Object>> readCollection(String suppliers) {
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            // Get Firestore instance
            Firestore db = FirestoreClient.getFirestore();

            // Create a reference to the "Suppliers" collection specifically
            ApiFuture<QuerySnapshot> query = db.collection("Suppliers").get();

            // Get all documents from the Suppliers collection
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            // Convert each document to a Map
            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> data = document.getData();
                results.add(data);
            }

            return results;

        } catch (InterruptedException e) {
            System.err.println("Thread interrupted while fetching suppliers: " + e.getMessage());
            Thread.currentThread().interrupt();
            return results;
        } catch (ExecutionException e) {
            System.err.println("Error executing Firestore query for suppliers: " + e.getMessage());
            return results;
        } catch (Exception e) {
            System.err.println("Unexpected error reading suppliers: " + e.getMessage());
            return results;
        }
    }
}



