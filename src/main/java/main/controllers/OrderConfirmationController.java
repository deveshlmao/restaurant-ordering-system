package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.utils.SceneSwitcher;

public class OrderConfirmationController {
    @FXML private Button returnButton;

    public void initialize() {
        returnButton.setOnAction(e -> SceneSwitcher.switchScene("menu-view.fxml"));
    }
}
