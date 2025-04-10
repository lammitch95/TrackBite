package chooser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class NewMenuItemUtils {

    private static ArrayList<String> itemsList = new ArrayList<>();
    private static ArrayList<String> currencyList = new ArrayList<>();

    private static ArrayList<String> ingredientUOMList = new ArrayList<>();

    static{

        ingredientUOMList.add("Select UOM");
        ingredientUOMList.add("Teaspoon (tsp)");
        ingredientUOMList.add("Tablespoon (tbsp)");
        ingredientUOMList.add("Fluid ounce (fl oz)");
        ingredientUOMList.add("Cup (c)");
        ingredientUOMList.add("Pint (pt)");
        ingredientUOMList.add("Quart (qt)");
        ingredientUOMList.add("Gallon (gal)");
        ingredientUOMList.add("Liter (L)");
        ingredientUOMList.add("Milliliter (mL)");
        ingredientUOMList.add("Ounce (oz)");
        ingredientUOMList.add("Pound (lb)");
        ingredientUOMList.add("Gram (g)");
        ingredientUOMList.add("Kilogram (kg)");
        ingredientUOMList.add("Whole");
        ingredientUOMList.add("Slice");
        ingredientUOMList.add("Clove");
        ingredientUOMList.add("Stick");
        ingredientUOMList.add("Dash, Pinch, Smidgen");


        itemsList.add("Select Category");
        itemsList.add("Appetizers");
        itemsList.add("Entrées");
        itemsList.add("Sides");
        itemsList.add("Beverages");
        itemsList.add("Desserts");
        itemsList.add("Kids Menu");
        itemsList.add("Specials");

        currencyList.add("Select Currency");
        currencyList.add("USD – US Dollar");
        currencyList.add("EUR – Euro");
        currencyList.add("GBP – British Pound");
        currencyList.add("JPY – Japanese Yen");
        currencyList.add("INR – Indian Rupee");
        currencyList.add("CAD – Canadian Dollar");
        currencyList.add("AUD – Australian Dollar");
        currencyList.add("CNY – Chinese Yuan");
        currencyList.add("KRW – South Korean Won");
        currencyList.add("BRL – Brazilian Real");
    }

    public static ArrayList<String> retrieveIngredientUOMListList(){return ingredientUOMList;}
    public static ArrayList<String> retrieveMenuCategoryList(){return itemsList;}

    public static ArrayList<String> retrieveMoneyCurrencyList(){return currencyList;}

    public static boolean isValidDetails(String name) {
        if (name == null) return false;
        return name.trim().length() >= 3;
    }

    public static boolean isValidPrice(String price) {
        try {
            double value = Double.parseDouble(price);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static String saveImagesLocally(String imagePath, String fileName){
        try {
            Path directory = Paths.get("menuItemImages");
            Files.createDirectories(directory);

            Path destinationPath = Paths.get(directory.toString(), fileName);

            File inputFile = new File(imagePath);
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte[] buffer = new byte[(int) inputFile.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath.toFile());
            fileOutputStream.write(buffer);
            fileOutputStream.close();

            System.out.println("Image saved locally: " + destinationPath.toString());

            return destinationPath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
