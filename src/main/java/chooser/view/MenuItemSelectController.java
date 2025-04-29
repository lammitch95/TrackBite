package chooser.view;

import chooser.utils.MenuItemSelectUtils;
import chooser.utils.NewInventoryItemUtils;
import chooser.utils.NewMenuItemUtils;
import chooser.viewmodel.MenuItemSelectViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MenuItemSelectController {

    @FXML
    private VBox ItemGridPane;

    @FXML
    private ComboBox<String> categoryCombobox;

    @FXML
    private VBox categoryMenu;

    @FXML
    private Label exitBtn;

    @FXML
    private Label itemIdLabel;

    @FXML
    private Label notItemsLabel;

    @FXML
    private ScrollPane scrollGridPane;

    @FXML
    private TextField searchBarText;

    @FXML
    private Button selectBtn;


    private MenuItemSelectViewModel menuItemSelectVM;

    @FXML
    public void initialize(){
        menuItemSelectVM = new MenuItemSelectViewModel();

        itemIdLabel.textProperty().bind(menuItemSelectVM.trackSelectedItemProp());

        exitBtn.setOnMouseClicked(event->{
            MenuItemSelectUtils.menuItemIDProperty().set("Select Menu Item");
            MenuItemSelectUtils.showMenuItemSelect("HIDE",null);
        });

        selectBtn.setOnAction(actionEvent -> {
            MenuItemSelectUtils.menuItemIDProperty().set(
                    !menuItemSelectVM.trackSelectedItemProp().get().equals("No item selected.") ?
                            menuItemSelectVM.trackSelectedItemProp().get() :
                            "Select Menu Item"
            );
            MenuItemSelectUtils.showMenuItemSelect("HIDE",null);
        });

        categoryCombobox.getItems().clear();
        categoryCombobox.setItems(FXCollections.observableArrayList(NewMenuItemUtils.retrieveMenuCategoryList()));
        categoryCombobox.getSelectionModel().selectFirst();
        menuItemSelectVM.trackSelectedCategoryProp().set(categoryCombobox.getValue());

        menuItemSelectVM.trackSelectedCategoryProp().addListener((obs, oldVal, newVal) -> {
            if (categoryCombobox.getItems().contains(newVal)) {
                categoryCombobox.getSelectionModel().select(newVal);
            }
        });

        categoryCombobox.setOnAction(e -> {
            String selectedItem = categoryCombobox.getValue();
            menuItemSelectVM.trackSelectedCategoryProp().set(selectedItem);
            menuItemSelectVM.displayMenuItemGrid(ItemGridPane);
        });

        menuItemSelectVM.isNoItemsProp().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                notItemsLabel.setPrefHeight(26);
                notItemsLabel.setVisible(true);
            } else {
                notItemsLabel.setPrefHeight(0);
                notItemsLabel.setVisible(false);
            }
        });



    }

}
