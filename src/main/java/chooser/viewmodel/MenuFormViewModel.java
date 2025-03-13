package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuFormViewModel {


    private final StringProperty menuId = new SimpleStringProperty("");
    private final StringProperty itemName = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty price = new SimpleStringProperty("");
    private final StringProperty currency = new SimpleStringProperty("$");
    private final StringProperty category = new SimpleStringProperty("");
    private final ListProperty<String> ingredients = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Image> itemImage = new SimpleObjectProperty<>(null);
    private final StringProperty ingredientInput = new SimpleStringProperty("");
    private final IntegerProperty ingredientAmount = new SimpleIntegerProperty(1);
    private final StringProperty ingredientUnit = new SimpleStringProperty("");


    private final BooleanProperty menuIdValid = new SimpleBooleanProperty(false);
    private final BooleanProperty itemNameValid = new SimpleBooleanProperty(false);
    private final BooleanProperty descriptionValid = new SimpleBooleanProperty(false);
    private final BooleanProperty priceValid = new SimpleBooleanProperty(false);
    private final BooleanProperty currencyValid = new SimpleBooleanProperty(true);
    private final BooleanProperty categoryValid = new SimpleBooleanProperty(false);
    private final BooleanProperty ingredientsValid = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);


    private final List<BooleanProperty> validInputList;
    private final List<Property<?>> userInputList;

    public MenuFormViewModel() {

        menuId.addListener((obs, oldVal, newVal) -> menuIdValid.set(newVal != null && !newVal.isEmpty()));
        itemName.addListener((obs, oldVal, newVal) -> itemNameValid.set(newVal != null && !newVal.trim().isEmpty()));
        description.addListener((obs, oldVal, newVal) -> descriptionValid.set(newVal != null && !newVal.trim().isEmpty()));
        price.addListener((obs, oldVal, newVal) -> price.set(String.valueOf(newVal != null && !newVal.trim().isEmpty())));
        currency.addListener((obs, oldVal, newVal) -> currencyValid.set(newVal != null && !newVal.isEmpty()));
        category.addListener((obs, oldVal, newVal) -> categoryValid.set(newVal != null && !newVal.trim().isEmpty()));
        ingredients.addListener((obs, oldVal, newVal) -> ingredientsValid.set(newVal != null && !newVal.isEmpty()));


        validInputList = List.of(menuIdValid, itemNameValid, descriptionValid, priceValid, currencyValid, categoryValid, ingredientsValid);
        userInputList = List.of(menuId, itemName, description, price, currency, category, ingredients, itemImage, ingredientInput, ingredientAmount, ingredientUnit);



        menuId.set(generateMenuId());
    }


    public StringProperty menuIdProperty() { return menuId; }
    public StringProperty itemNameProperty() { return itemName; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty priceProperty() { return price; }
    public StringProperty currencyProperty() { return currency; }
    public StringProperty categoryProperty() { return category; }
    public ListProperty<String> ingredientsProperty() { return ingredients; }
    public ObjectProperty<Image> itemImageProperty() { return itemImage; }
    public StringProperty ingredientInputProperty() { return ingredientInput; }
    public IntegerProperty ingredientAmountProperty() { return ingredientAmount; }
    public StringProperty ingredientUnitProperty() { return ingredientUnit; }





    private String generateMenuId() {
        Random random = new Random();
        return String.format("MENU-%04d", random.nextInt(10000));
    }

    public void addIngredient() {
        if (!ingredientInput.get().isEmpty()) {
            String ingredient = String.format("%d %s %s", ingredientAmount.get(), ingredientUnit.get(), ingredientInput.get());
            ingredients.add(ingredient);
            ingredientInput.set("");
            ingredientAmount.set(1);
            ingredientUnit.set("");
        }
    }


    public boolean onSubmit() {
        System.out.println("On Submit method Called");
        System.out.println("Menu ID: " + menuId.get());
        System.out.println("Menu Item Name: " + itemName.get());
        System.out.println("Description: " + description.get());
        System.out.println("Price: " + price.get());
        System.out.println("Category: " + category.get());
        System.out.println("Ingredients: " + ingredients);
        // System.out.println("Image: " + (menuItemImage.getImage() != null ? "Image uploaded" : "No image"));

       /* for (BooleanProperty validInput : validInputList) {
            if (!validInput.get()) {
                return false;
            }
        }*/

        Map<String, Object> data = new HashMap<>();
        data.put("menuId", menuId.get());
        data.put("itemName", itemName.get());
        data.put("description", description.get());
        data.put("price", price.get());
        data.put("currency", currency.get());
        data.put("category", category.get());
        data.put("ingredients", ingredients.get());
        /*if (itemImage.get() != null) {
            data.put("imageUrl", "placeholder");
        }*/

        FirestoreUtils.writeDoc("NewMenuItems", menuId.get(), data);
        System.out.println("created new menu item");
        return true;
    }

    public void clearInputs() {
        for (Property<?> userInput : userInputList) {
            if (userInput instanceof StringProperty) {
                ((StringProperty) userInput).set("");
            } else if (userInput instanceof IntegerProperty) {
                ((IntegerProperty) userInput).set(1);
            } else if (userInput instanceof ListProperty) {
                ((ListProperty<?>) userInput).clear();
            } else if (userInput instanceof ObjectProperty) {
                ((ObjectProperty<?>) userInput).set(null);
            }
        }
        menuId.set(generateMenuId());
        currency.set("$");
    }


    public void updateValidImageViews(HBox hBox, BooleanProperty... properties) {
        ImageView imageView = (ImageView) hBox.lookup(".image-view");
        Label label = (Label) hBox.lookup(".label");

        if (imageView != null) {
            boolean allTrue = true;
            for (BooleanProperty property : properties) {
                if (!property.get()) {
                    allTrue = false;
                    break;
                }
            }

            if (allTrue) {
                imageView.setVisible(true);
                Image validImage = new Image(getClass().getResource("/chooser/trackbite/valid.png").toExternalForm());
                imageView.setImage(validImage);
            } else {
                imageView.setVisible(false);
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }
}