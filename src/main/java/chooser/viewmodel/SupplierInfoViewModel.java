package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.AddressInfo;
import chooser.model.PrimaryContactInfo;
import chooser.model.SupplierInfo;
import chooser.model.User;
import chooser.utils.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.*;

public class SupplierInfoViewModel {

    private final StringProperty supplierId = new SimpleStringProperty("");
    private final StringProperty supplierName = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty primContactFirstName = new SimpleStringProperty("");
    private final StringProperty primContactLastName = new SimpleStringProperty("");
    private final StringProperty primContactPhoneNum = new SimpleStringProperty("");
    private final StringProperty primContactEmail = new SimpleStringProperty("");
    private final StringProperty primContactWebsite = new SimpleStringProperty("");

    private final StringProperty ba_addressLine = new SimpleStringProperty("");
    private final StringProperty ba_city = new SimpleStringProperty("");
    private final StringProperty ba_state = new SimpleStringProperty("");
    private final StringProperty ba_postalCode = new SimpleStringProperty("");
    private final StringProperty ba_country= new SimpleStringProperty("");

    private final StringProperty w_addressLine = new SimpleStringProperty("");
    private final StringProperty w_city = new SimpleStringProperty("");
    private final StringProperty w_state = new SimpleStringProperty("");
    private final StringProperty w_postalCode = new SimpleStringProperty("");
    private final StringProperty w_country= new SimpleStringProperty("");



    private final BooleanProperty validSupplierId= new SimpleBooleanProperty(false);
    private final BooleanProperty validSupplierName= new SimpleBooleanProperty(false);
    private final BooleanProperty validPrimContactFirstName= new SimpleBooleanProperty(false);
    private final BooleanProperty validPrimContactLastName= new SimpleBooleanProperty(false);
    private final BooleanProperty validPrimContactPhoneNum = new SimpleBooleanProperty(false);
    private final BooleanProperty validPrimContactEmail = new SimpleBooleanProperty(false);


    private final BooleanProperty valid_ba_addressLine = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_ba_city= new SimpleBooleanProperty(false);
    private final BooleanProperty valid_ba_state = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_ba_postalCode = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_ba_country  = new SimpleBooleanProperty(false);

    private final BooleanProperty valid_w_addressLine = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_w_city= new SimpleBooleanProperty(false);
    private final BooleanProperty valid_w_state = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_w_postalCode = new SimpleBooleanProperty(false);
    private final BooleanProperty valid_w_country  = new SimpleBooleanProperty(false);

    private final BooleanProperty allowSave = new SimpleBooleanProperty(false);
    private final BooleanProperty hasChanged = new SimpleBooleanProperty(false);
    private final BooleanProperty isNewRecord = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty ingredientFormValid = new SimpleBooleanProperty(false);


    private List<BooleanProperty> validInputList;
    private List<StringProperty> formInputList;;

    public void setIsNewRedcordBoolean(boolean value){
        isNewRecord.set(value);
    }
    public BooleanProperty formValidProperty() {
        return formValid;
    }
    public BooleanProperty allowSaveProperty(){return allowSave;}
    public BooleanProperty ingredientItemValidProperty(){return ingredientFormValid;}

    public SupplierInfoViewModel(){
        isNewRecord.set(false);
        supplierId.set("New Supplier");
        ingredientFormValid.set(false);
        ba_country.set("Select Country");
        w_country.set("Select Country");


        validInputList = List.of(
                validSupplierName,
                validPrimContactFirstName,
                validPrimContactLastName,
                validPrimContactPhoneNum,
                validPrimContactEmail,
                valid_ba_addressLine,
                valid_ba_city,
                valid_ba_state,
                valid_ba_postalCode,
                valid_ba_country,
                valid_w_addressLine,
                valid_w_city,
                valid_w_state,
                valid_w_postalCode,
                valid_w_country
        );

        formValid.bind(validSupplierIdProp()
                .and(validSupplierNameProp())
                .and(validPrimContactFirstNameProp())
                .and(validPrimContactLastNameProp())
                .and(validPrimContactPhoneNumProp())
                .and(validPrimContactEmailProp())
                .and(valid_ba_addressLineProp())
                .and(valid_ba_cityProp())
                .and(valid_ba_stateProp())
                .and(valid_ba_postalCodeProp())
                .and(valid_ba_countryProp())
                .and(valid_w_addressLineProp())
                .and(valid_w_cityProp())
                .and(valid_w_stateProp())
                .and(valid_w_postalCodeProp())
                .and(valid_w_countryProp())
        );

        allowSave.bind(formValid.and(hasChanged));

        formInputList = List.of(
                supplierName,
                description,
                primContactFirstName,
                primContactLastName,
                primContactPhoneNum,
                primContactEmail,
                primContactWebsite,
                ba_addressLine,
                ba_city,
                ba_state,
                ba_postalCode,
                ba_country,
                w_addressLine,
                w_city,
                w_state,
                w_postalCode,
                w_country
        );

        supplierId.addListener((obs, oldVal, newVal) -> {
            validSupplierId.set(!newVal.equals("New Supplier"));
            hasChanged.set(true);
        });

        supplierName.addListener((obs, oldVal, newVal) -> {
            validSupplierName.set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        primContactFirstName.addListener((obs, oldVal, newVal) -> {
            validPrimContactFirstName.set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        primContactLastName.addListener((obs, oldVal, newVal) -> {
            validPrimContactLastNameProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        primContactPhoneNum.addListener((obs, oldVal, newVal) -> {
            validPrimContactPhoneNumProp().set(NewUserUtils.isValidPhoneNum(newVal));
            hasChanged.set(true);
        });
        primContactEmail.addListener((obs, oldVal, newVal) -> {
            validPrimContactEmailProp().set(SupplierInfoUtils.isValidEmail(newVal));
            hasChanged.set(true);
        });

        ba_addressLine.addListener((obs, oldVal, newVal) -> {
            valid_ba_addressLineProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        ba_city.addListener((obs, oldVal, newVal) -> {
            valid_ba_cityProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        ba_state.addListener((obs, oldVal, newVal) -> {
            valid_ba_stateProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        ba_postalCode.addListener((obs, oldVal, newVal) -> {
            valid_ba_postalCodeProp().set(SupplierInfoUtils.isValidPostalCode(newVal));
            hasChanged.set(true);
        });

        ba_country.addListener((obs, oldVal, newVal) -> {
            valid_ba_countryProp().set(!newVal.equals("Select Country"));
            hasChanged.set(true);
        });

        w_addressLine.addListener((obs, oldVal, newVal) -> {
            valid_w_addressLineProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        w_city.addListener((obs, oldVal, newVal) -> {
            valid_w_cityProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });
        w_state.addListener((obs, oldVal, newVal) -> {
            valid_w_stateProp().set(NewInventoryItemUtils.isValidDetails(newVal));
            hasChanged.set(true);
        });

        w_postalCode.addListener((obs, oldVal, newVal) -> {
            valid_w_postalCodeProp().set(SupplierInfoUtils.isValidPostalCode(newVal));
            hasChanged.set(true);
        });

        w_country.addListener((obs, oldVal, newVal) -> {
            valid_w_countryProp().set(!newVal.equals("Select Country"));
            hasChanged.set(true);
        });

    }

    public StringProperty supplierIdProp() {return supplierId;}
    public StringProperty supplierNameProp() {return supplierName;}

    public StringProperty descriptionProp() {return description;}
    public StringProperty primContactFirstNameProp() {return primContactFirstName;}
    public StringProperty primContactLastNameProp() {return primContactLastName;}
    public StringProperty primContactPhoneNumProp() {return primContactPhoneNum;}
    public StringProperty primContactEmailProp() {return primContactEmail;}

    public StringProperty primContactWebsiteProp() {return primContactWebsite;}
    public StringProperty ba_addressLineProp() {return ba_addressLine;}
    public StringProperty ba_cityProp() {return ba_city;}
    public StringProperty ba_stateProp() {return ba_state;}

    public StringProperty ba_postalCodeProp() {return ba_postalCode;}
    public StringProperty ba_countryProp() {return ba_country;}
    public StringProperty w_addressLineProp() {return w_addressLine;}
    public StringProperty w_cityProp() {return w_city;}
    public StringProperty w_stateProp() {return w_state;}
    public StringProperty w_postalCodeProp() {return w_postalCode;}
    public StringProperty w_countryProp() {return w_country;}


    public BooleanProperty validSupplierIdProp() { return validSupplierId; }
    public BooleanProperty validSupplierNameProp() { return validSupplierName; }
    public BooleanProperty validPrimContactFirstNameProp() { return validPrimContactFirstName; }
    public BooleanProperty validPrimContactLastNameProp() { return validPrimContactLastName; }
    public BooleanProperty validPrimContactPhoneNumProp() { return validPrimContactPhoneNum; }
    public BooleanProperty validPrimContactEmailProp() { return validPrimContactEmail; }
    public BooleanProperty valid_ba_addressLineProp() { return valid_ba_addressLine; }
    public BooleanProperty valid_ba_cityProp() { return valid_ba_city; }
    public BooleanProperty valid_ba_stateProp() { return valid_ba_state; }
    public BooleanProperty valid_ba_postalCodeProp() { return valid_ba_postalCode; }
    public BooleanProperty valid_ba_countryProp() { return valid_ba_country; }

    public BooleanProperty valid_w_addressLineProp() { return valid_w_addressLine; }
    public BooleanProperty valid_w_cityProp() { return valid_w_city; }
    public BooleanProperty valid_w_stateProp() { return valid_w_state; }
    public BooleanProperty valid_w_postalCodeProp() { return valid_w_postalCode; }
    public BooleanProperty valid_w_countryProp() { return valid_w_country; }


    public String generateSupplierID() {
        String firstCode= supplierName.get().length() >= 3 ? supplierName.get().substring(0, 3).toUpperCase() : supplierName.get().toUpperCase();
        int randomNumber = new Random().nextInt(10000);
        return firstCode + String.format("%04d", randomNumber);
    }

    public boolean onSubmit(){

        for(BooleanProperty validInput: validInputList){
            if(!validInput.get()){
                return false;
            }
        }

        SystemMessageUtils.setCurrSystemText("New supplier record has been saved: "+supplierId.get());
        SystemMessageUtils.setCurrPropertyColor("SUCCESS");
        SystemMessageUtils.messageAnimation();

        Map<String, Object> data = new HashMap<>();
        data.put("supplierId", supplierId.get());
        data.put("supplierName", supplierName.get());
        data.put("description", description.get());
        data.put("primaryContactFirstName", primContactFirstName.get());
        data.put("primaryContactLastName", primContactLastName.get());
        data.put("primaryContactPhoneNum", primContactPhoneNum.get());
        data.put("primaryContactEmail", primContactEmail.get());
        data.put("primaryContactWebsite", primContactWebsite.get());

        data.put("busAddressLine", ba_addressLine.get());
        data.put("busAddressCity", ba_city.get());
        data.put("busAddressState", ba_state.get());
        data.put("busAddressPostalCode", ba_postalCode.get());
        data.put("busAddressCountry", ba_country.get());

        data.put("warAddressLine", w_addressLine.get());
        data.put("warAddressCity", w_city.get());
        data.put("warAddressState", w_state.get());
        data.put("warAddressPostalCode", w_postalCode.get());
        data.put("warAddressCountry", w_country.get());

        FirestoreUtils.writeDoc("SuppliersV2", supplierId.get(), data);
        hasChanged.set(false);
        return true;
    }

    public void clearInputs(){
        for(StringProperty userInput: formInputList){
            userInput.set("");
        }

        for(BooleanProperty booleanInput: validInputList){
            booleanInput.set(false);
        }


        ba_country.set("Select Country");
        w_country.set("Select Country");
        supplierId.set("New Supplier");
    }

    public void updateValidImageViews(HBox hBox, BooleanProperty... properties) {

        Image requiredIconImg = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/requiredIcon.png")).toExternalForm());
        Image validIconImage = new Image(Objects.requireNonNull(getClass().getResource("/chooser/trackbite/valid.png")).toExternalForm());

        ImageView imageView = (ImageView) hBox.lookup(".image-view");
        Label label = (Label) hBox.lookup(".label");

        if (imageView != null && requiredIconImg!=null && validIconImage != null) {

            imageView.setVisible(true);
            imageView.setImage(requiredIconImg);

            boolean allTrue = false;
            for (BooleanProperty property : properties) {
                if (property.get()) {
                    allTrue = true;
                    break;
                }
            }

            if (allTrue) {

                imageView.setImage(validIconImage);

                if(label.getText().equals("Supplier Name:") && isNewRecord.get()){
                    String newSupplierId = generateSupplierID();
                    supplierId.set(newSupplierId);
                    System.out.println("CHECKING NEW GENERATED SupplierId: "+newSupplierId);
                }

            } else {

                if(label.getText().equals("Supplier Name:") && isNewRecord.get()){
                    supplierId.set("New Supplier");
                }
            }
        } else {
            System.out.println("ImageView not found inside the HBox.");
        }
    }


    public void populateTextFields(){

        System.out.println("Populating text fields called:");

        Object selectedData = TableViewUtils.retrieveDocumentData(TableViewUtils.getStoredCollectionName(),TableViewUtils.getSelectedRowID());
        if(selectedData instanceof SupplierInfo obj){

            supplierId.set(obj.getSupplierId());
            supplierName.set(obj.getSupplierName());
            description.set(obj.getDescription());

            PrimaryContactInfo primContactData = obj.getContactInfo();

            primContactFirstName.set(primContactData.getFirstName());
            primContactLastName.set(primContactData.getLastName());
            primContactPhoneNum.set(primContactData.getPhoneNum());
            primContactEmail.set(primContactData.getEmail());
            primContactWebsite.set(primContactData.getWebsite());

            AddressInfo busAddressData = obj.getBusinessAddressInfo();

            ba_addressLine.set(busAddressData.getAddressLine());
            ba_city.set(busAddressData.getCity());
            ba_state.set(busAddressData.getState());
            ba_postalCode.set(busAddressData.getPostalCode());
            ba_country.set(busAddressData.getCountry());

            AddressInfo warehouseAddressData = obj.getWarehouseAddressInfo();

            w_addressLine.set(warehouseAddressData.getAddressLine());
            w_city.set(warehouseAddressData.getCity());
            w_state.set(warehouseAddressData.getState());
            w_postalCode.set(warehouseAddressData.getPostalCode());
            w_country.set(warehouseAddressData.getCountry());

            hasChanged.set(false);

            TableViewUtils.setStoreCollectionName("");
            TableViewUtils.setSelectedRowID(null);
        }
    }



}
