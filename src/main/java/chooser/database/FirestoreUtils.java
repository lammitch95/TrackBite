package chooser.database;

import chooser.model.InventoryItem;
import chooser.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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
            System.out.println(documentId);
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

    public static  List<InventoryItem>  readCollection(String collectionName) {
        try {
            Firestore db = FirestoreContext.getFirestore();

            ApiFuture<QuerySnapshot> snapshot = db.collection(collectionName).get();
            List<QueryDocumentSnapshot> documents = snapshot.get().getDocuments();

            List<InventoryItem> inventoryItems = new ArrayList<>();

            for (QueryDocumentSnapshot document: documents) {
                System.out.println(document.getData());
                Object test1DocValue = document.getData().get("quantity");
                long testElse = (long)0;
                String testOptionalFix = Optional.ofNullable(String.valueOf(test1DocValue )).orElse("0");
                float testFinal = Float.parseFloat(testOptionalFix);
                inventoryItems.add(new InventoryItem((String) document.getData().get("InventoryItemID"), document.getString("itemName"), document.getString("unit"), document.getString("category"), (String.valueOf(document.getData().get("quantity"))), testFinal,document.getString("supplier")));
                System.out.println(document.getString("InventoryItemID"));
                System.out.println(document.getData());
                System.out.println(document.getData().get("InventoryItemID"));



            }

            return inventoryItems;
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

    private static InventoryItem createInvItemFromDocument(DocumentSnapshot document) {
        String itemId = document.getId();
        String itemName = document.getString("username");
        String unit = document.getString("password");
        String category = document.getString("firstName");
        String quantity = document.getString("lastName");
        float pricePerUnit = Float.parseFloat((String) document.getData().get("pricePerUnit"));;
        String supplier = document.getString("supplier");

        return new InventoryItem(itemId, itemName, unit, category, quantity, pricePerUnit, supplier);
    }
}



