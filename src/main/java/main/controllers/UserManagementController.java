package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.models.User;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;

import java.util.List;
import java.util.Optional;

public class UserManagementController {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button editUserButton;
    @FXML
    private Button backButton;

    private ObservableList<User> usersList;

    @FXML
    public void initialize() {
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());


        loadUsers();
    }

    private void loadUsers() {
        List<User> users = DatabaseHelper.getAllUsers();
        usersList = FXCollections.observableArrayList(users);
        usersTable.setItems(usersList);
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "No user selected.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Are you sure you want to delete this user?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseHelper.deleteUser(selectedUser.getId())) {
                showAlert("Success", "User deleted.");
                loadUsers();
            } else {
                showAlert("Error", "Failed to delete user.");
            }
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "No user selected.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedUser.getUsername());
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit username for " + selectedUser.getUsername());
        dialog.setContentText("Enter new username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newUsername -> {
            if (!newUsername.trim().isEmpty()) {
                if (DatabaseHelper.updateUsername(selectedUser.getId(), newUsername)) {
                    showAlert("Success", "Username updated.");
                    loadUsers();
                } else {
                    showAlert("Error", "Failed to update username.");
                }
            }
        });
    }

    @FXML
    private void handleBack() {
        SceneSwitcher.switchScene("admin-dashboard.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
