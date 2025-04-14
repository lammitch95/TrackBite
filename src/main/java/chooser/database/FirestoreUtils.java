package chooser.database;

import chooser.model.InventoryDelivery;
import chooser.model.InventoryItem;
import chooser.model.IngredientItem;
import chooser.model.MenuItem;
import chooser.model.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Blob;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreUtils {

    private static HashMap<String, HashMap<String, Map<String, Object>>> collectionMap = new HashMap<>();


    public static void writeDoc(String collectionName, String documentId, Map<String, Object> data) {
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

    public static List<InventoryItem> readCollection(String collectionName) {
        try {
            Firestore db = FirestoreContext.getFirestore();

            ApiFuture<QuerySnapshot> snapshot = db.collection(collectionName).get();
            List<QueryDocumentSnapshot> documents = snapshot.get().getDocuments();

            List<InventoryItem> inventoryItems = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {
                System.out.println(document.getData());
                Object test1DocValue = document.getData().get("quantity");
                long testElse = (long) 0;
                String testOptionalFix = Optional.ofNullable(String.valueOf(test1DocValue)).orElse("0");
                float testFinal = Float.parseFloat(testOptionalFix);
                inventoryItems.add(new InventoryItem((String) document.getData().get("InventoryItemID"), document.getString("itemName"), document.getString("unit"), document.getString("category"), (String.valueOf(document.getData().get("quantity"))), testFinal, document.getString("supplier")));
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


    public static User authenticateUser(String username, String password) {
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

    public static MenuItem createMenuItemFromDocument(DocumentSnapshot document) {
        String menuItemID = document.getString("id");
        String name = document.getString("name");
        String description = document.getString("description");
        String category = document.getString("category");
        String price = document.getString("price");
        String uom = document.getString("uom");
        String itemImage = document.getString("itemImage");
        List<Map<String, Object>> ingredientsData = (List<Map<String, Object>>) document.get("ingredientsList");

        List<IngredientItem> ingredientsList = new ArrayList<>();
        for (Map<String, Object> ingredientMap : ingredientsData) {
            String ingredientName = (String) ingredientMap.get("name");
            String ingredientQuantity = (String) ingredientMap.get("quantity");
            String ingredientUOM = (String) ingredientMap.get("uom");
            String prepDetails = (String) ingredientMap.get("prepDetails");

            IngredientItem ingredientItem = new IngredientItem(ingredientName, ingredientQuantity, ingredientUOM, prepDetails);
            ingredientsList.add(ingredientItem);
        }
        return new MenuItem(menuItemID, name, description, category, price, uom, itemImage, ingredientsList);
    }

    public static InventoryItem createInvItemFromDocument(DocumentSnapshot document) {

        String itemId = document.contains("InventoryItemID")
                ? document.getString("InventoryItemID")
                : document.getId();  // Use doc ID as fallback

        String itemName = document.getString("itemName");
        String unit = document.getString("unit");
        String category = document.getString("category");
        String quantity = document.get("quantity") != null
                ? String.valueOf(document.get("quantity"))
                : "0";

        float pricePerUnit;
        try {
            pricePerUnit = document.get("pricePerUnit") != null
                    ? Float.parseFloat(String.valueOf(document.get("pricePerUnit")))
                    : 0f;
        } catch (Exception e) {
            pricePerUnit = 0f;
        }

        // ✅ Supplier fallback for missing field
        String supplier = document.contains("supplier") && document.getString("supplier") != null
                ? document.getString("supplier")
                : "N/A";

        return new InventoryItem(itemId, itemName, unit, category, quantity, pricePerUnit, supplier);
    }

    public static List<InventoryDelivery> readDeliveriesCollection() {
        List<InventoryDelivery> deliveries = new ArrayList<>();
        try {
            Firestore db = FirestoreContext.getFirestore();
            List<QueryDocumentSnapshot> docs = db.collection("inventoryDeliveries").get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                InventoryDelivery d = new InventoryDelivery(
                        doc.getString("itemId"),
                        doc.getString("itemName"),
                        Float.parseFloat(String.valueOf(doc.get("quantity"))),
                        LocalDate.parse(doc.getString("deliveryDate")),
                        LocalDate.parse(doc.getString("expirationDate")),
                        Float.parseFloat(String.valueOf(doc.get("pricePerUnit"))),
                        doc.getString("supplier")
                );
                deliveries.add(d);
            }
        } catch (Exception e) {
            System.err.println("Error reading deliveries: " + e.getMessage());
        }
        return deliveries;
    }
}





