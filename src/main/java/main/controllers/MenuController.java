package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Main;
import main.models.MenuItem;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;


import java.util.List;

public class MenuController {

    @FXML
    private GridPane menuGrid;
    @FXML
    private ToggleButton allButton, mealsButton, drinksButton, dessertsButton;
    private final ToggleGroup categoryToggleGroup = new ToggleGroup();

    private static final String DEFAULT_IMAGE = "/images/salad.png";

    @FXML
    public void initialize() {
        setupCategoryButtons();
        loadMenuItems("All");
    }

    private void setupCategoryButtons() {
        allButton.setToggleGroup(categoryToggleGroup);
        mealsButton.setToggleGroup(categoryToggleGroup);
        drinksButton.setToggleGroup(categoryToggleGroup);
        dessertsButton.setToggleGroup(categoryToggleGroup);

        allButton.setOnAction(e -> loadMenuItems("All"));
        mealsButton.setOnAction(e -> loadMenuItems("Meals"));
        drinksButton.setOnAction(e -> loadMenuItems("Drinks"));
        dessertsButton.setOnAction(e -> loadMenuItems("Desserts"));

        allButton.setSelected(true);
    }

    private void loadMenuItems(String category) {
        menuGrid.getChildren().clear();

        List<MenuItem> menuItems = category.equals("All") ?
                DatabaseHelper.getAllMenuItems() :
                DatabaseHelper.getMenuItemsByCategory(category);

        System.out.println("🔍 Loading " + menuItems.size() + " menu items for category: " + category);

        populateMenu(menuItems);
    }


    private void populateMenu(List<MenuItem> menuItems) {
        int row = 0, col = 0;

        for (MenuItem item : menuItems) {
            System.out.println("🛒 Adding item to menu: " + item.getName());

            VBox itemBox = new VBox();
            ImageView imageView = new ImageView();

            setImageForItem(imageView, item.getImagePath());

            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            Label nameLabel = new Label(item.getName());
            Label priceLabel = new Label("$" + String.format("%.2f", item.getPrice()));

            Button addButton = new Button("Add to Cart");
            addButton.setOnAction(event -> addToCart(item));

            itemBox.getChildren().addAll(imageView, nameLabel, priceLabel, addButton);
            menuGrid.add(itemBox, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }




    private void setImageForItem(ImageView imageView, String imagePath) {
        try {
            System.out.println("🔍 Searching for image: " + imagePath);

            if (imagePath == null || getClass().getResource("/" + imagePath) == null) {
                throw new Exception("Image not found");
            }

            Image image = new Image(getClass().getResource("/" + imagePath).toExternalForm());
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("❌ Image not found: " + imagePath);
            imageView.setImage(new Image(getClass().getResource("/images/salad.png").toExternalForm()));
        }
    }


















    private void addToCart(MenuItem item) {
        CartController.addItemToCart(item);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Added");
        alert.setHeaderText(null);
        alert.setContentText(item.getName() + " has been added to your cart.");
        alert.showAndWait();
    }

    @FXML
    private void handleCheckout() {
        if (CartController.getCartItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Checkout Warning");
            alert.setHeaderText(null);
            alert.setContentText("Cannot proceed to checkout unless the cart has items.");
            alert.showAndWait();
        } else {
            SceneSwitcher.switchScene("cart-view.fxml");
        }
    }


    @FXML
    private void handleLogout(ActionEvent event) {
        SceneSwitcher.switchScene("login-view.fxml");
    }

}
