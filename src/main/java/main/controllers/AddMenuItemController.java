package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.models.MenuItem;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;

public class AddMenuItemController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField availabilityField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        String category = categoryField.getText();
        String availability = availabilityField.getText();

        MenuItem newItem = new MenuItem(0, name, price, "default_image.png", category, availability.equalsIgnoreCase("Available"));
        if (DatabaseHelper.addMenuItem(newItem)) {
            SceneSwitcher.switchScene("manage-menu.fxml");
        } else {
            showAlert("Error", "Failed to add menu item.");
        }
    }

    @FXML
    private void handleCancel() {
        SceneSwitcher.switchScene("manage-menu.fxml");
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
