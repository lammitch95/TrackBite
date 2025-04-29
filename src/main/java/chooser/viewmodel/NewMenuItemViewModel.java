package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.IngredientItem;
import chooser.model.MenuItem;
import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.utils.SystemMessageUtils;
import chooser.utils.TableViewUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;

public class NewMenuItemViewModel {

    private final StringProperty menuItemID = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty category = new SimpleStringProperty("");
    private final StringProperty price = new SimpleStringProperty("");
    private final StringProperty priceUOM = new SimpleStringProperty("");
    private final StringProperty itemImage = new SimpleStringProperty("");


    //Ingredient Info Property Section
    private final StringProperty ingredientName = new SimpleStringProperty("");
    private final StringProperty ingredientQuantity = new SimpleStringProperty("");
    private final StringProperty ingredientUOM = new SimpleStringProperty("");
    private final StringProperty prepDetails = new SimpleStringProperty("");

    private final StringProperty remainingUses = new SimpleStringProperty("");
    private final StringProperty tbvPageNumDisplay = new SimpleStringProperty("");
    private final BooleanProperty isLoadedIngredient = new SimpleBooleanProperty(false);


    //Validity String Properties
    private final BooleanProperty isValidMenuItemID = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidName = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidDescription = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidCategory = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidPrice = new SimpleBooleanProperty(false);

    private final BooleanProperty isValidPriceUOM = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidItemImage = new SimpleBooleanProperty(false);



    private final BooleanProperty isValidIngredientName = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidIngredientQuantity = new SimpleBooleanProperty(false);
    private final BooleanProperty isValidIngredientUOM = new SimpleBooleanProperty(false);
    private final BooleanProperty ingredientItemValid = new SimpleBooleanProperty(false);


    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty ingredientFormValid = new SimpleBooleanProperty(false);


    private List<BooleanProperty> validInputList;
    private List<StringProperty> menuItemInputList;
    private List<BooleanProperty> ingredientValidInputList;
    private List<StringProperty> ingredientInputList;

    private int trackTableViewPage;
    private int maxPages;

    private IngredientItem currentSelectedIngredient;

    private Map<Integer, List<IngredientItem>> ingredientListMap;

    private ObservableList<IngredientItem> ingredientList;

    private final StringProperty imageFileName = new SimpleStringProperty("");

    private final BooleanProperty isNewImage = new SimpleBooleanProperty(false);


    public void setCurrentSelectedIngredient(IngredientItem currentSelectedIngredient) {
        this.currentSelectedIngredient = currentSelectedIngredient;
    }

    public IngredientItem getCurrentSelectedIngredient(){
        return  this.currentSelectedIngredient;
    }

    public StringProperty tbvPageNumDisplayProperty(){return tbvPageNumDisplay;}
    public StringProperty menuItemIDProperty(){return menuItemID;}
    public StringProperty nameProperty(){return name;}
    public StringProperty descriptionProperty(){return description;}
    public StringProperty categoryProperty(){return category;}
    public StringProperty priceProperty(){return price;}
    public StringProperty priceUOMProperty(){return  priceUOM;}
    public StringProperty itemImageProperty(){return itemImage;}

    public StringProperty imageFileNameProperty(){return imageFileName;}


    public StringProperty ingredientNameProperty(){return ingredientName;}
    public StringProperty ingredientQuantityProperty(){return ingredientQuantity;}
    public StringProperty ingredientUOMProperty(){return ingredientUOM;}
    public StringProperty ingredientPrepDetails(){return prepDetails;}



    public BooleanProperty menuItemIDValidProperty(){return isValidMenuItemID;}
    public BooleanProperty nameValidProperty(){return isValidName;}
    public BooleanProperty descriptionValidProperty(){return isValidDescription;}
    public BooleanProperty categoryValidProperty(){return isValidCategory;}
    public BooleanProperty priceValidProperty(){return isValidPrice;}
    public BooleanProperty priceUOMValidProperty(){return isValidPriceUOM;}
    public BooleanProperty itemImageValidProperty(){return isValidItemImage;}

    public BooleanProperty ingredientNameValidProperty(){return isValidIngredientName;}
    public BooleanProperty ingredientQuantityValidProperty(){return isValidIngredientQuantity;}
    public BooleanProperty ingredientUOMValidProperty(){return isValidIngredientUOM;}

    public void setIsNewRedcordBoolean(boolean value){
        isNewRecord.set(value);
    }

    public BooleanProperty formValidProperty() {
        return formValid;
    }

    public BooleanProperty allowSaveProperty(){return allowSave;}

    public BooleanProperty ingredientItemValidProperty(){return ingredientFormValid;}



    public NewMenuItemViewModel(){
        isLoadedIngredient.set(false);
        isNewImage.set(false);
        itemImage.set("DEFAULT");
        imageFileName.set("DEFAULT");
        trackTableViewPage = 1;
        maxPages = 1;
        ingredientListMap = new HashMap<>();
        ingredientListMap.put(maxPages, new ArrayList<>());
        setTBVPageDisplay(null);


        isNewRecord.set(false);
        menuItemID.set("New Menu Item");
        ingredientFormValid.set(false);
        priceUOM.set("Select Currency");
        isValidPriceUOM.set(false);



        ingredientValidInputList = List.of(
                isValidIngredientName,
                isValidIngredientQuantity,
                isValidIngredientUOM,
                NewMenuItemUtils.linkInventoryIdValidProp()
        );

        ingredientInputList = List.of(
                ingredientName,
                ingredientQuantity,
                ingredientUOM,
                prepDetails,
                NewMenuItemUtils.linkInventoryId()
        );

        validInputList = List.of(
                isValidName,
                isValidDescription,
                isValidCategory,
                isValidPrice,
                isValidPriceUOM,
                isValidItemImage
        );

        menuItemInputList = List.of(
                name,
                description,
                category,
                price,
                priceUOM,
                itemImage
        );

        menuItemID.addListener((obs, oldVal, newVal) -> {
            isValidMenuItemID.set(!newVal.equals("New Menu Item"));
            hasChanged.set(true);
        });

        name.addListener((obs, oldVal, newVal) -> {
            isValidName.set(NewMenuItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        description.addListener((obs, oldVal, newVal) -> {
            isValidDescription.set(NewMenuItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        category.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidCategory.set(!newVal.equals("Select Category"));
                hasChanged.set(true);
            }
        });

        price.addListener((obs, oldVal, newVal) -> {
            isValidPrice.set(NewMenuItemUtils.isValidPrice(newVal) && isValidPriceUOM.get());
            hasChanged.set(true);
        });

        priceUOM.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidPriceUOM.set(!newVal.equals("Select Currency"));
                isValidPrice.set(NewMenuItemUtils.isValidPrice(price.get()) && isValidPriceUOM.get());
                hasChanged.set(true);
            }
        });

        itemImage.addListener((obs, oldVal, newVal) -> {
            isValidItemImage.set(!newVal.isEmpty());
            hasChanged.set(true);
        });

        formValid.bind(menuItemIDValidProperty()
                .and(nameValidProperty())
                .and(descriptionValidProperty())
                .and(categoryValidProperty())
                .and(priceValidProperty())
                .and(priceUOMValidProperty())
        );

        allowSave.bind(formValid.and(hasChanged));

        ingredientItemValid.bind(ingredientNameValidProperty()
                .and(ingredientQuantityValidProperty())
                .and(ingredientUOMValidProperty())
        );


        ingredientName.addListener((obs, oldVal, newVal) -> {
            isValidIngredientName.set(NewMenuItemUtils.isValidDetails(newVal));
        });

        ingredientQuantity.addListener((obs, oldVal, newVal) -> {
            isValidIngredientQuantity.set(NewMenuItemUtils.isValidPrice(newVal));
        });

        ingredientUOM.addListener((obs, oldVal, newVal) -> {
            if(newVal!=null){
                isValidIngredientUOM.set(!newVal.equals("Select UOM"));
            }

        });

        ingredientFormValid.bind(ingredientNameValidProperty()
                .and(ingredientQuantityValidProperty())
                .and(ingredientUOMValidProperty())
                .and(NewMenuItemUtils.linkInventoryIdValidProp())
        );


    }

    public void populateIngredientsInputs(){

        System.out.println("populateIngredientsInputs Called: ");
        System.out.println("currentSelectedIngredient Called: "+currentSelectedIngredient);
        isLoadedIngredient.set(true);

        ingredientName.set(currentSelectedIngredient.getName());
        ingredientQuantity.set(currentSelectedIngredient.getQuantity());
        ingredientUOM.set(currentSelectedIngredient.getUom());
        prepDetails.set(currentSelectedIngredient.getPrepDetails());

        String displayText = (currentSelectedIngredient.getLinkInventoryId() == null || currentSelectedIngredient.getLinkInventoryId().isEmpty())
                ? "Select Inventory"
                : currentSelectedIngredient.getLinkInventoryId();
        NewMenuItemUtils.linkInventoryId().set(displayText);

    }

    public void onSelectInventoryItem(String status,AnchorPane menuFormRootPane){
        NewInventoryItemUtils.selectInventoryItem(status, menuFormRootPane);
    }

    public String generateMenuItemID() {
        String featureInitials = "mi";
        String itemNameShort = name.get().substring(0, 3).toLowerCase();
        String randomDigits = String.format("%04d", new Random().nextInt(10000));

        return featureInitials + itemNameShort + randomDigits;

    }

    public void uploadItemImage(ImageView itemImageDisplay, String status){
        switch(status){
            case "DEFAULT":
                Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/chooser/trackbite/defaultNoImageFound.png")));
                itemImageDisplay.setImage(defaultImage);
                break;

            case "ADD":
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose an Image");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
                );
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    try {

                        Random random = new Random();
                        int randomFourDigitNumber = 1000 + random.nextInt(9000);

                        String newImagePath = selectedFile.getAbsolutePath();

                        String newImageFileName = randomFourDigitNumber+selectedFile.getName();
                        NewMenuItemUtils.saveImagesLocally(newImagePath,newImageFileName);

                        isNewImage.set(true);

                        itemImage.set(newImagePath);
                        imageFileName.set(newImageFileName);

                        System.out.println("NEW MENU ITEM IMAGE PATH: "+ itemImage.get());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public boolean onSubmitNewMenuItem(){

        for(BooleanProperty validInput: validInputList){
            if(!validInput.get()){
                return false;
            }
        }

        SystemMessageUtils.setCurrSystemText("New menu item record has been saved: "+menuItemID.get());
        SystemMessageUtils.setCurrPropertyColor("SUCCESS");
        SystemMessageUtils.messageAnimation();


        Map<String, Object> data = new HashMap<>();
        data.put("id", menuItemID.get());
        data.put("name", name.get());
        data.put("description", description.get());
        data.put("category", category.get());
        data.put("price", price.get());
        data.put("uom", priceUOM.get());
        data.put("itemImage", imageFileName.get());


        List<IngredientItem> entireIngredientsList = new ArrayList<>();
        for (Map.Entry<Integer, List<IngredientItem>> entry : ingredientListMap.entrySet()) {
            entireIngredientsList.addAll(entry.getValue());
        }

        List<Map<String, Object>> ingredientsForFirestore = new ArrayList<>();
        for (IngredientItem ingredient : entireIngredientsList) {
            Map<String, Object> ingredientMap = new HashMap<>();

            ingredientMap.put("name", ingredient.getName());
            ingredientMap.put("quantity", ingredient.getQuantity());
            ingredientMap.put("uom", ingredient.getUom());
            ingredientMap.put("prepDetails", ingredient.getPrepDetails());
            ingredientMap.put("linkInventoryId", ingredient.getLinkInventoryId());
            ingredientMap.put("remainingUses", ingredient.getRemainingUses());

            ingredientsForFirestore.add(ingredientMap);
        }

        data.put("ingredientsList",ingredientsForFirestore);

        FirestoreUtils.writeDoc("Menu", menuItemID.get(), data);
        hasChanged.set(false);
        isNewImage.set(false);
        return true;
    }

    public void clearInputs(){
        for(StringProperty userInput: menuItemInputList){
            userInput.set("");
        }

        priceUOM.set("Select Currency");
        category.set("Select Category");
        NewMenuItemUtils.linkInventoryId().set("Select Inventory");
        itemImage.set("DEFAULT");
        imageFileName.set("DEFAULT");
        isNewImage.set(false);

        clearIngredientInputs();
    }


    public void clearIngredientInputs(){
        for(StringProperty userInput: ingredientInputList){
            userInput.set("");
        }

        ingredientUOM.set(("Select UOM"));
        isLoadedIngredient.set(false);
        NewMenuItemUtils.linkInventoryId().set("Select Inventory");
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

                if(label.getText().equals("Name:") && isNewRecord.get()){
                    String newEmployeeId = generateMenuItemID();
                    menuItemID.set(newEmployeeId);
                    System.out.println("CHECKING NEW GENERATED MENU ITEM ID: "+newEmployeeId);
                }

            } else {
                imageView.setVisible(false);

                if(label.getText().equals("Name:") && isNewRecord.get()){
                    menuItemID.set("New Menu Item");
                }
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }
    public void populateTextFields(TableView<IngredientItem> ingredientTV){

        Object selectedData = TableViewUtils.retrieveDocumentData(TableViewUtils.getStoredCollectionName(),TableViewUtils.getSelectedRowID());

        if(selectedData instanceof MenuItem obj){

            System.out.println("MenuItem obj exists");
            menuItemID.set(obj.getId());
            name.set(obj.getName());
            description.set(obj.getDescription());
            category.set(obj.getCategory());
            price.set(obj.getPrice());
            priceUOM.set(obj.getUom());
            imageFileName.set(obj.getItemImage());

            List<IngredientItem> retrieveIngredientList = obj.getIngredientsList();

            if(retrieveIngredientList != null){
                for(int i = 0; i < retrieveIngredientList.size(); ++i){
                    List<IngredientItem> latestIngredientList = ingredientListMap.get(maxPages);
                    if(latestIngredientList != null){
                        if(latestIngredientList.size() == 7){
                            maxPages++;
                            ingredientListMap.put(maxPages, new ArrayList<>());
                        }
                        ingredientListMap.get(maxPages).add(retrieveIngredientList.get(i));

                    }
                }

                setTBVPageDisplay(ingredientTV);
            }



            hasChanged.set(false);
            isNewImage.set(false);

            TableViewUtils.setStoreCollectionName("");
            TableViewUtils.setSelectedRowID(null);
        }
    }

    private void setTBVPageDisplay(TableView<IngredientItem> IngredientTableView){
        tbvPageNumDisplay.set(trackTableViewPage+" of "+maxPages+" pages");

        List<IngredientItem> latestIngredientList = ingredientListMap.get(trackTableViewPage);

        if(latestIngredientList != null && IngredientTableView!=null){
            ingredientList = FXCollections.observableArrayList(latestIngredientList);
            IngredientTableView.setItems(ingredientList);
        }
    }

    public void handleTableViewNav(String status, TableView<IngredientItem> IngredientTableView){
        switch (status){
            case "LEFT":
                if(trackTableViewPage > 1){
                    trackTableViewPage--;
                }
                break;
            case "RIGHT":
                if(trackTableViewPage < maxPages){
                    trackTableViewPage++;
                }
                break;
        }


        setTBVPageDisplay(IngredientTableView);
    }

    public void onAddIngredient(TableView<IngredientItem> ingredientTV){
            if(isLoadedIngredient.get()){
                onDeleteIngredient(ingredientTV);
            }

            for (BooleanProperty validInput : ingredientValidInputList) {
                if (!validInput.get()) {
                    return;
                }
            }

            if (prepDetails.get().isEmpty()) {
                prepDetails.set("--");
            }

            IngredientItem newIngredient = new IngredientItem(
                    ingredientName.get(),
                    ingredientQuantity.get(),
                    ingredientUOM.get(),
                    prepDetails.get(),
                    NewMenuItemUtils.linkInventoryId().get(),
                    remainingUses.get()
            );


            List<IngredientItem> latestIngredientList = ingredientListMap.get(maxPages);
            if (latestIngredientList == null) {
                ingredientListMap.put(maxPages, new ArrayList<>());
            }

            latestIngredientList = ingredientListMap.get(maxPages);
            if (latestIngredientList.size() == 7) {
                maxPages++;
                ingredientListMap.put(maxPages, new ArrayList<>());
            }
            ingredientListMap.get(maxPages).add(newIngredient);

            setTBVPageDisplay(ingredientTV);
            clearIngredientInputs();
    }

    public void onDeleteIngredient(TableView<IngredientItem> ingredientTV) {
        System.out.println("onDeleteIngredient Called");

        if (currentSelectedIngredient != null) {

            List<IngredientItem> allIngredients = new ArrayList<>();
            for (List<IngredientItem> pageItems : ingredientListMap.values()) {
                allIngredients.addAll(pageItems);
            }


            boolean removed = allIngredients.removeIf(ingredient ->
                    ingredient.getName().equals(currentSelectedIngredient.getName()) &&
                            ingredient.getQuantity().equals(currentSelectedIngredient.getQuantity()) &&
                            ingredient.getUom().equals(currentSelectedIngredient.getUom()) &&
                            ingredient.getPrepDetails().equals(currentSelectedIngredient.getPrepDetails())
            );

            if (removed) {
                System.out.println("Ingredient Item has been removed: " + currentSelectedIngredient.getName());


                ingredientListMap.clear();
                int maxPages = 1;
                ingredientListMap.put(maxPages, new ArrayList<>());

                for (int i = 0; i < allIngredients.size(); ++i) {
                    List<IngredientItem> currentPage = ingredientListMap.get(maxPages);
                    if (currentPage.size() == 7) {
                        maxPages++;
                        ingredientListMap.put(maxPages, new ArrayList<>());
                    }
                    ingredientListMap.get(maxPages).add(allIngredients.get(i));
                }


                setTBVPageDisplay(ingredientTV);
            }
        }
    }

    public void handleIngredientsTableView(String status, TableView<IngredientItem> ingredientTV){
        switch (status){
            case "RESET":
                if (ingredientTV.getItems() != null && !ingredientTV.getItems().isEmpty()) {
                    ingredientTV.getItems().clear();
                    ingredientListMap.clear();
                }
                break;
            case "INITIAL":
                ingredientTV.setItems(ingredientList);
                TableColumn<IngredientItem, String> columnOne = new TableColumn<IngredientItem,String>("Name");
                columnOne.setCellValueFactory(new PropertyValueFactory<IngredientItem,String>("name"));

                TableColumn<IngredientItem, String> columnTwo = new TableColumn<IngredientItem,String>("Quantity");
                columnTwo.setCellValueFactory(new PropertyValueFactory<IngredientItem,String>("quantity"));

                TableColumn<IngredientItem, String> columnThree= new TableColumn<IngredientItem,String>("UOM");
                columnThree.setCellValueFactory(new PropertyValueFactory<IngredientItem,String>("uom"));

                TableColumn<IngredientItem, String> columnFour= new TableColumn<IngredientItem,String>("Prep Details");
                columnFour.setCellValueFactory(new PropertyValueFactory<IngredientItem,String>("prepDetails"));

                TableColumn<IngredientItem, String> columnFive = new TableColumn<IngredientItem,String>("Link Item Id");
                columnFive.setCellValueFactory(new PropertyValueFactory<IngredientItem,String>("linkInventoryId"));

                ingredientTV.getColumns().add(columnOne);
                ingredientTV.getColumns().add(columnTwo);
                ingredientTV.getColumns().add(columnThree);
                ingredientTV.getColumns().add(columnFour);
                ingredientTV.getColumns().add(columnFive);

                ingredientTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                break;
            case "ADD":
                onAddIngredient(ingredientTV);
                hasChanged.set(true);
                break;

            case "DELETE":
                onDeleteIngredient(ingredientTV);
                hasChanged.set(true);
                clearIngredientInputs();
                break;

        }
    }
}
