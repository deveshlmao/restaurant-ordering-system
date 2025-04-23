package main.controllers;

import main.models.MenuItem;
import main.utils.SceneSwitcher;
import main.controllers.OrderController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;

public class CartController {

    public Button backButton;
    @FXML
    private VBox cartContainer;
    @FXML
    private Label totalPriceLabel;

    private static final Map<MenuItem, Integer> cartItems = new HashMap<>();

    @FXML
    public void initialize() {
        displayCartItems();
    }

    public static void addItemToCart(MenuItem item) {
        cartItems.put(item, cartItems.getOrDefault(item, 0) + 1);
    }

    public static Map<MenuItem, Integer> getCartItems() {
        return cartItems;
    }

    private void displayCartItems() {
        cartContainer.getChildren().clear();
        double total = 0;

        if (cartItems.isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty.");
            cartContainer.getChildren().add(emptyLabel);
        } else {
            for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                double itemTotal = item.getPrice() * quantity;
                total += itemTotal;

                HBox itemBox = new HBox(10);
                Label itemLabel = new Label(item.getName() + " - $" + item.getPrice() + " x" + quantity);
                Button decreaseButton = new Button("-");
                Button increaseButton = new Button("+");

                increaseButton.setOnAction(e -> {
                    cartItems.put(item, cartItems.get(item) + 1);
                    displayCartItems();
                });

                decreaseButton.setOnAction(e -> {
                    if (cartItems.get(item) > 1) {
                        cartItems.put(item, cartItems.get(item) - 1);
                    } else {
                        cartItems.remove(item);
                    }
                    displayCartItems();
                });

                itemBox.getChildren().addAll(itemLabel, increaseButton, decreaseButton);
                cartContainer.getChildren().add(itemBox);
            }
        }

        totalPriceLabel.setText("Total: $" + String.format("%.2f", total));
    }


    @FXML
    private void payNow() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Checkout Warning");
            alert.setHeaderText(null);
            alert.setContentText("Cannot proceed to checkout unless the cart has items.");
            alert.showAndWait();
            return;
        }

        boolean orderPlaced = OrderController.placeOrder(cartItems);
        if (orderPlaced) {
            cartItems.clear();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Order Successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Redirecting to Payment page!");
            successAlert.showAndWait();
            SceneSwitcher.switchScene("payment-view.fxml");
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Order Failed");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("There was an issue placing your order. Please try again.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void backToMain() {
        SceneSwitcher.switchScene("menu-view.fxml");
    }
}
