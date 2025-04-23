package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;

public class RegisterController {

    @FXML
    private TextField fullNameField, usernameField, emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Registration Failed", "All fields must be filled.");
            return;
        }

        boolean success = DatabaseHelper.registerUser(fullName, username, email, password);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You can now log in.");
            SceneSwitcher.switchScene("login-view.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username or email already exists.");
        }
    }

    @FXML
    private void goToLogin() {
        SceneSwitcher.switchScene("login-view.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
