package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.utils.SceneSwitcher;
import main.utils.DatabaseHelper;
import main.utils.SessionManager;
import main.models.User;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        User user = DatabaseHelper.authenticateUser(username, password);

        if (user != null) {
            SessionManager.setLoggedInUser(user.getId(), user.getUsername(), user.isAdmin());

            if (user.isAdmin()) {
                showAlert("Login Successful", "Welcome, Admin " + user.getUsername() + "!");
                SceneSwitcher.switchScene("admin-dashboard.fxml");
            } else {
                showAlert("Login Successful", "Welcome, " + user.getUsername() + "!");
                SceneSwitcher.switchScene("menu-view.fxml");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToRegister () {
        SceneSwitcher.switchScene("register-view.fxml");
    }
}
