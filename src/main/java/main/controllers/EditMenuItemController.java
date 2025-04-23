package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.models.MenuItem;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;

public class EditMenuItemController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField categoryField;
    @FXML
    private Button handleSave;
    @FXML
    private Button handleCancel;

    private MenuItem selectedItem;

    public void setMenuItem(MenuItem item) {
        this.selectedItem = item;
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        categoryField.setText(item.getCategory());
    }



    @FXML
    private void handleSave() {
        selectedItem.setName(nameField.getText());
        selectedItem.setPrice(Double.parseDouble(priceField.getText()));
        selectedItem.setCategory(categoryField.getText());


        if (DatabaseHelper.updateMenuItem(selectedItem)) {
            SceneSwitcher.switchScene("manage-menu.fxml");
        } else {
            showAlert("Error", "Failed to update menu item.");
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
