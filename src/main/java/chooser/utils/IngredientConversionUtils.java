package chooser.utils;

import chooser.model.OrderMenuDisplay;

import java.util.List;
import java.util.Map;

public class IngredientConversionUtils {

    private static final Map<String, Double> volumeToML = Map.ofEntries(
            Map.entry("Teaspoon (tsp)", 4.93),
            Map.entry("Tablespoon (tbsp)", 14.79),
            Map.entry("Fluid ounce (fl oz)", 29.57),
            Map.entry("Cup (c)", 240.0),
            Map.entry("Pint (pt)", 473.0),
            Map.entry("Quart (qt)", 946.0),
            Map.entry("Gallon (gal)", 3785.0),
            Map.entry("Liter (L)", 1000.0),
            Map.entry("Milliliter (mL)", 1.0)
    );
    private static final Map<String, Double> weightToGrams = Map.ofEntries(
            Map.entry("Ounce (oz)", 28.35),
            Map.entry("Pound (lb)", 453.59),
            Map.entry("Gram (g)", 1.0),
            Map.entry("Kilogram (kg)", 1000.0)
    );

    private static final double DEFAULT_GRAMS_PER_CUP = 160.0;

    public static double calculateIngredientUses(double requiredAmount, String requiredUOM, double inventoryAmount, String inventoryUOM) {

        boolean requiredIsVolume = volumeToML.containsKey(requiredUOM);
        boolean requiredIsWeight = weightToGrams.containsKey(requiredUOM);
        boolean inventoryIsVolume = volumeToML.containsKey(inventoryUOM);
        boolean inventoryIsWeight = weightToGrams.containsKey(inventoryUOM);

        double requiredGrams;
        double inventoryGrams;

        if (requiredIsWeight) {
            requiredGrams = requiredAmount * weightToGrams.get(requiredUOM);
        } else if (requiredIsVolume) {
            double requiredML = requiredAmount * volumeToML.get(requiredUOM);
            requiredGrams = requiredML * (DEFAULT_GRAMS_PER_CUP / 240.0);
        } else {
            System.out.println("Unsupported required UOM: " + requiredUOM);
            return -1;
        }

        if (inventoryIsWeight) {
            inventoryGrams = inventoryAmount * weightToGrams.get(inventoryUOM);
        } else if (inventoryIsVolume) {
            double inventoryML = inventoryAmount * volumeToML.get(inventoryUOM);
            inventoryGrams = inventoryML * (DEFAULT_GRAMS_PER_CUP / 240.0);
        } else {
            System.out.println("Unsupported inventory UOM: " + inventoryUOM);
            return -1;
        }

        return inventoryGrams / requiredGrams;
    }


    public static int  calculateMaxServings(List<OrderMenuDisplay> ingredientList) {
        System.out.println("calculateMaxServings called");
        if (ingredientList == null || ingredientList.isEmpty()) return 0;

        int minServings = Integer.MAX_VALUE;

        for (OrderMenuDisplay ingredient : ingredientList) {
            int uses = ingredient.getRemainingUses();
            minServings = Math.min(minServings, uses);
        }

        return minServings;
    }

}
