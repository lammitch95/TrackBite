package chooser.viewmodel;

import chooser.database.FirestoreUtils;
import chooser.model.*;
import chooser.utils.*;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMenuViewViewModel {

    private final StringProperty trackSelectedCategory = new SimpleStringProperty("");
    private final BooleanProperty toggleCategoryMenu = new SimpleBooleanProperty(false);
    private final BooleanProperty isNoItems = new SimpleBooleanProperty(true);
    private List<MenuItem> menuItemsData;
    private HashMap<String,List<MenuItem>> sortedMenuItemsMap;
    private int numSquarePerRow;

    private AnchorPane storeMainLayout;

    public void setStoreMainLayout(AnchorPane pane){storeMainLayout = pane;}

    public StringProperty trackSelectedCategoryProp(){return trackSelectedCategory;}
    public BooleanProperty toggleCategoryMenuProp(){return toggleCategoryMenu;}
    public BooleanProperty isNoItemsProp(){return isNoItems;}



    public OrderMenuViewViewModel(){
        storeMainLayout = null;
        isNoItems.set(true);
        sortedMenuItemsMap = new HashMap<>();
        numSquarePerRow = 7;
        setUpMenuItemsData();
        trackSelectedCategory.set("Select Category");
    }

    public void setUpCategoryBtns(Button[] buttonArr, VBox itemGridPane){

        List<String> retrieveCategoryList = NewMenuItemUtils.retrieveMenuCategoryList();

        for(int i = 0; i < buttonArr.length; ++i){
            if(i < retrieveCategoryList.size()){
                String findCategoryLabel = retrieveCategoryList.get(i);
                if(findCategoryLabel!= null){
                    buttonArr[i].setVisible(true);
                    buttonArr[i].setDisable(false);
                    buttonArr[i].setText(findCategoryLabel);
                    buttonArr[i].setOnAction(actionEvent -> {
                        toggleCategoryMenu.set(false);
                        trackSelectedCategory.set(findCategoryLabel);
                        displayMenuItemGrid(itemGridPane);
                    });
                }
            }else{
                buttonArr[i].setVisible(false);
                buttonArr[i].setDisable(true);
            }


            buttonArr[i].getStyleClass().add("hover-effect");

        }

    }

    private void setUpMenuItemsData(){

        System.out.println("setUpMenuItemsData called");

        List<MenuItem> tableData = new ArrayList<>();
        QuerySnapshot snapshot = FirestoreUtils.getAllDocuments("Menu");

        assert snapshot != null;
        for (QueryDocumentSnapshot document : snapshot) {
            MenuItem formatMenuData = FirestoreUtils.createMenuItemFromDocument(document);
            tableData.add(formatMenuData);
        }

        menuItemsData = tableData;

        for(int i = 0; i < menuItemsData.size(); ++i){
            String categoryData = menuItemsData.get(i).getCategory();
            if (!sortedMenuItemsMap.containsKey(categoryData)) {
                sortedMenuItemsMap.put(categoryData, new ArrayList<>());
            }
            sortedMenuItemsMap.get(categoryData).add(menuItemsData.get(i));
        }


    }

    public void displayMenuItemGrid(VBox itemGridPane){

        System.out.println("displayMenuItemGrid called");

        itemGridPane.getChildren().clear();

        List<MenuItem> chosenItemsToDisplay = sortedMenuItemsMap.get(trackSelectedCategory.get());
        if(chosenItemsToDisplay!=null && !chosenItemsToDisplay.isEmpty()){
            isNoItems.set(false);
            setUpSquares(itemGridPane, chosenItemsToDisplay);
        }else{
            isNoItems.set(true);
        }
    }




    public void setUpSquares(VBox itemGridPane, List<MenuItem> data){

        System.out.println("setUpSquares called");

        Map<Integer,List<MenuItem>> splitDataRowMap = new HashMap<>();
        int pageCounter = 1;
        int itemCounter = 0;
        splitDataRowMap.put(pageCounter, new ArrayList<>());
        for(int i = 0; i < data.size(); i++){
            if(itemCounter < numSquarePerRow){
                splitDataRowMap.get(pageCounter).add(data.get(i));
                itemCounter++;
            }else{
                pageCounter++;
                itemCounter = 1;
                List<MenuItem> newRow = new ArrayList<>();
                newRow.add(data.get(i));
                splitDataRowMap.put(pageCounter, newRow);
            }
        }

        List<InventoryItem> retrieveInventoryList = MenuItemSelectUtils.setUpInventoryData();


        for (List<MenuItem> rowItems : splitDataRowMap.values()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooser/trackbite/ItemSquare.fxml"));
                HBox rowBox = loader.load();

                for (int i = 0; i < numSquarePerRow; i++) {
                    Node square = rowBox.getChildren().get(i);//should be a Hbox
                    VBox innerVBox = (VBox) ((HBox) square).getChildren().get(0);

                    innerVBox.getStyleClass().add("hover-effect");

                    ImageView itemImageView = (ImageView) innerVBox.getChildren().get(0);
                    Label itemNameLabel = (Label) innerVBox.getChildren().get(1);
                    Label itemServingsCountLabel  = (Label) innerVBox.getChildren().get(2);

                    if (i < rowItems.size()) {
                        MenuItem menuItem = rowItems.get(i);

                        if(!menuItem.getItemImage().equals("DEFAULT")){
                            String imagePath = new File("menuItemImages/" + menuItem.getItemImage()).toURI().toString();
                            Image image = new Image(imagePath);
                            itemImageView.setImage(image);
                        }

                        itemNameLabel.setText(menuItem.getName());

                        int servingsLeft = MenuItemSelectUtils.remainingServings(menuItem,retrieveInventoryList);
                        if (servingsLeft > 0) {
                            itemServingsCountLabel.setStyle("-fx-background-color: #00f52d;");
                        } else {
                            itemServingsCountLabel.setStyle("-fx-background-color: #ff0000;");
                        }
                        itemServingsCountLabel.setText("Servings Left: " + servingsLeft);

                        innerVBox.setOnMouseClicked(mouseEvent -> {
                            System.out.println("Order Menu Item Clicked: "+menuItem.getName());
                            if(storeMainLayout!=null){
                                NewMenuItemUtils.selectedOrderMenuItemProp().set(menuItem.getId());
                                NewMenuItemUtils.selectOrderMenuItemView("SHOW", storeMainLayout);

                            }
                        });

                    } else {

                        square.setVisible(false);
                        square.setManaged(false);
                    }
                }

                itemGridPane.getChildren().add(rowBox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
