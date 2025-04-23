package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import main.utils.SceneSwitcher;

public class PaymentController {
    @FXML private TextField nameField, cardField, expiryField, cvvField;
    @FXML private Button payButton;

    public void initialize() {
        payButton.setOnAction(e -> processPayment());
    }

    private void processPayment() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> SceneSwitcher.switchScene("order-confirmation-view.fxml"));
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
