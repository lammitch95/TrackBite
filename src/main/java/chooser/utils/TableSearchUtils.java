package chooser.utils;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class TableSearchUtils {

    public static <T> void enableSearch(TextField searchField, FilteredList<T> filteredList, Predicate<T> predicateTemplate) {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filterText = newVal == null ? "" : newVal.toLowerCase();
            filteredList.setPredicate(item -> predicateTemplate.test(item) && (
                    filterText.isEmpty() || item.toString().toLowerCase().contains(filterText)
            ));
        });
    }
}
