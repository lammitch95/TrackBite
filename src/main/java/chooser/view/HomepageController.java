package chooser.view;

import chooser.database.FirestoreUtils;
import chooser.model.InventoryItem;
import chooser.model.NavOptions;
import chooser.model.SessionManager;
import chooser.model.User;
import chooser.utils.SceneNavigator;
import chooser.viewmodel.HomepageViewModel;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;

public class HomepageController {

    @FXML
    private HBox accountsBtn;

    @FXML
    private HBox changeLanguageBtn;

    @FXML
    private Label currUserNameLabel;

    @FXML
    private Label currUserTitleLabel;

    @FXML
    private HBox darkLightBtn;

    @FXML
    private HBox inventoryBtn;

    @FXML
    private ImageView lightDarkIcon;

    @FXML
    private VBox mainContentPane;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private ScrollPane mainOrderScroll;

    @FXML
    private HBox menuBtn;

    @FXML
    private VBox pageMenuHeader;

    @FXML
    private Label pageName;

    @FXML
    private HBox pageOptionOneBtn;

    @FXML
    private HBox pageOptionTwoBtn;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private HBox settingBtn;

    @FXML
    private VBox settingNavbar;

    @FXML
    private VBox sideNavBar;

    @FXML
    private HBox signOutBtn;

    @FXML
    private Button EnterNewItemButton;

    @FXML
    private Button EnterDeliveryButton;

    @FXML
    private TableColumn<InventoryItem, String> itemIdCol;

    @FXML
    private TableColumn<InventoryItem, String> itemNameCol;

    @FXML
    private TableColumn<InventoryItem, String> unitCol;

    @FXML
    private TableColumn<InventoryItem, String> categoryCol;

    @FXML
    private TableColumn<InventoryItem, String> quantityCol;

    @FXML
    private TableColumn<InventoryItem, String> stockCol;

    @FXML
    private TableColumn<InventoryItem, String> actionCol;

    @FXML
    private TableView itemTable;




    private HomepageViewModel homepageViewModel;
    private HBox[] pageOptionBtnList;

    @FXML
    private void initialize() {

        pageOptionBtnList = new HBox[]{pageOptionOneBtn, pageOptionTwoBtn};

        homepageViewModel = new HomepageViewModel();

        settingNavbar.setVisible(false);
        settingNavbar.setDisable(true);

        homepageViewModel.setIsSettingOpen(true);

        homepageViewModel.currentPageProperty().addListener((obs, oldPage, newPage) -> {
            System.out.println("Navigated to: " + newPage);
            pageName.setText(newPage);
        });


        User currLoggedUser = SessionManager.getLoggedInUser();
        if (currLoggedUser != null) {
            currUserNameLabel.setText(currLoggedUser.getFirstName() != null ? currLoggedUser.getFirstName() : currLoggedUser.getUsername());
            currUserTitleLabel.setText(currLoggedUser.getRole() != null ? currLoggedUser.getRole() : "Role Not Available");

            if (currLoggedUser.getRole().equals("Admin")) {
                accountsBtn.setDisable(false);
                accountsBtn.setVisible(true);
            } else {
                accountsBtn.setDisable(true);
                accountsBtn.setVisible(false);
            }
        }

        HBox[] hboxList = new HBox[]{inventoryBtn, menuBtn, accountsBtn, settingBtn, pageOptionOneBtn, pageOptionTwoBtn, darkLightBtn, signOutBtn, changeLanguageBtn};
        for (HBox hbox : hboxList) {
            hbox.getStyleClass().add("hover-effect");
        }

        homepageViewModel.setCurrentPage("Inventory");
        changeUIPageOptions("Inventory");

        homepageViewModel.currentViewProperty().addListener((obs, oldView, newView) -> {
            if (oldView != null) {
                mainContentPane.getChildren().remove(oldView);
            }
            if (newView != null) {
                mainContentPane.getChildren().add(newView);
                pageMenuHeader.setPrefHeight(0);
                pageMenuHeader.setVisible(false);
                pageMenuHeader.setDisable(true);
            }
        });

        for (HBox pageOption : pageOptionBtnList) {
            Label label = (Label) pageOption.getChildren().get(1);

            pageOption.setOnMouseClicked(event -> {
                if (label != null) {
                    String buttonText = label.getText();
                    homepageViewModel.loadView(buttonText);
                }
            });
        }



        itemIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().itemIdProperty()));
        itemNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().itemNameProperty()));
        unitCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().unitProperty()));
        categoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().categoryProperty()));
        quantityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().quantityProperty()));

        stockCol.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue().getStockStatus()); // get the stock status
        });


        actionCol.setCellFactory(column -> new TableCell<InventoryItem, String>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

            {

                        deleteButton.setOnAction(event -> {
                            InventoryItem item = getTableRow().getItem();
                            if (item != null) {
                                handleDelete(item);
                            }
                        });


                        editButton.setOnAction(event -> {
                             InventoryItem item = getTableRow().getItem();
                             if (item != null) {
                                 System.out.println("Edit button clicked for item: " + item.getItemId());
                                 SceneNavigator.setSelectedItemId(item.getItemId());
                                 SceneNavigator.switchScene(
                                         "editItemForm",
                                         "TrackBite/editItemForm",

                                         -1,
                                         -1,

                                         true
                                 );
                                 System.out.println("Switch scene called.");

                             } else {
                                 System.out.println("No item selected.");
                             }
                        });
                     }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonBox = new HBox(10);  // Create a container for the buttons
                            buttonBox.getChildren().addAll(editButton, deleteButton);  // Add both buttons
                            setGraphic(buttonBox);  // Set the buttons as the graphic of the ce

                        }
                    }
                });


        List<InventoryItem> documents=FirestoreUtils.readCollection("Inventory");
        System.out.println(documents);
        itemTable.setItems (FXCollections.observableList(documents));

    }

    private void handleDelete(InventoryItem item) {
        if (item != null) {
            itemTable.getItems().remove(item);
            System.out.println("Deleting item with ID: " + item.getItemId());
            FirestoreUtils.deleteDoc("Inventory",  item.getItemId());
            itemTable.getItems().remove(item);
            itemTable.refresh();
            showAlert("Item Deleted", "The item has been successfully deleted.");
        }
        }

    void changeUIPageOptions(String selectChoice) {
        List<NavOptions> checkOptionsExist = homepageViewModel.getNavMap().get(selectChoice);

        pageMenuHeader.setPrefHeight(130);
        pageMenuHeader.setVisible(true);
        pageMenuHeader.setDisable(false);

        homepageViewModel.removePreviousView();
        System.out.println("Page Options Current: " + pageOptionBtnList.length);

        for (int i = 0; i < pageOptionBtnList.length; i++) {
            HBox pageOption = pageOptionBtnList[i];
            pageOption.setVisible(false);
            pageOption.setDisable(true);

            if (checkOptionsExist != null) {
                int optionSize = checkOptionsExist.size();
                if (i < optionSize) {

                    NavOptions option = checkOptionsExist.get(i);

                    Node firstNode = pageOption.getChildren().get(0);
                    Node secondNode = pageOption.getChildren().get(1);

                    ImageView imageView = (firstNode instanceof ImageView) ? (ImageView) firstNode : null;
                    Label label = (secondNode instanceof Label) ? (Label) secondNode : null;

                    if (imageView != null) {
                        String imagePath = "/chooser/trackbite/" + option.getImagePath();
                        URL imageUrl = getClass().getResource(imagePath);

                        if (imageUrl != null) {
                            imageView.setImage(new Image(imageUrl.toExternalForm()));
                        } else {
                            System.err.println("Error: Image not found at " + imagePath);
                        }
                    }
                    if (label != null) {
                        label.setText(option.getName());
                    }

                    pageOption.setVisible(true);
                    pageOption.setDisable(false);
                }

            }
        }

    }

    @FXML
    void onAccountsClick(MouseEvent event) {
        homepageViewModel.setCurrentPage("Accounts");
        changeUIPageOptions("Accounts");
    }

    @FXML
    void onInventoryClick(MouseEvent event) {
        homepageViewModel.setCurrentPage("Inventory");
        changeUIPageOptions("Inventory");
    }

    @FXML
    void onMenuClick(MouseEvent event) {
        homepageViewModel.setCurrentPage("Menu");
        changeUIPageOptions("Menu");
    }

    @FXML
    void onChangeLanguageClick(ActionEvent event) {

    }

    @FXML
    void onDarkLightClick(ActionEvent event) {

    }


    @FXML
    void onNewUserClick(MouseEvent event) {

    }

    @FXML
    void onSettingsClick(MouseEvent event) {
        settingNavbar.setVisible(homepageViewModel.getIsSettingOpen());
        settingNavbar.setDisable(!homepageViewModel.getIsSettingOpen());
        homepageViewModel.setIsSettingOpen(!homepageViewModel.getIsSettingOpen());
    }

    @FXML
    void onSignOutClick(MouseEvent event) {
        System.out.println("Sign-out clicked");
        homepageViewModel.userLogOut();

    }

    @FXML
    void EnterNewItemPressed(ActionEvent event) {
        System.out.println("test");

        SceneNavigator.switchScene(
                "AddNewItemPage",
                "TrackBite/AddNewItemPage",
                -1,
                -1,
                true
        );



    }

    @FXML
    void EnterDeliveryPressed(ActionEvent event) {
        System.out.println("test");
        SceneNavigator.switchScene(
                "addDeliveryPage",
                "TrackBite/addDeliveryPage",
                -1,
                -1,
                true
        );


    }

    private void refreshTable() {
        List<InventoryItem> updatedDocuments = FirestoreUtils.readCollection("Inventory");
        itemTable.setItems(FXCollections.observableList(updatedDocuments));
        itemTable.refresh();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
