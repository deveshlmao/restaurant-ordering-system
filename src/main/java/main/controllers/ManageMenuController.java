package main.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.models.MenuItem;
import main.utils.DatabaseHelper;
import main.utils.SceneSwitcher;

import java.util.List;

public class ManageMenuController {

    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, Integer> itemIdColumn;
    @FXML
    private TableColumn<MenuItem, String> nameColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> categoryColumn;
    @FXML
    private Button addItemButton;
    @FXML
    private Button editItemButton;
    @FXML
    private Button deleteItemButton;

    private ObservableList<MenuItem> menuList;

    @FXML
    public void initialize() {
        System.out.println("Initializing ManageMenuController...");

        if (menuTable == null) {
            System.out.println("menuTable is NULL!");
            return;
        }

        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadMenuItems();
    }

    private void loadMenuItems() {
        List<MenuItem> menuItems = DatabaseHelper.getAllMenuItems();
        menuList = FXCollections.observableArrayList(menuItems);
        menuTable.setItems(menuList);
        menuTable.refresh();
    }

    @FXML
    private void handleAddItem() {
        SceneSwitcher.switchScene("add-menu-item.fxml");
    }

    @FXML
    private void handleEditItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "No menu item selected.");
            return;
        }

        SceneSwitcher.switchSceneWithData("edit-menu-item.fxml", controller -> {
            if (controller instanceof EditMenuItemController) {
                ((EditMenuItemController) controller).setMenuItem(selectedItem);
            }
        });
    }

    @FXML
    private void handleDeleteItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "No menu item selected.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this menu item?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText(null);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (DatabaseHelper.deleteMenuItem(selectedItem.getId())) {
                    showAlert("Success", "Menu item deleted.");
                    loadMenuItems();
                } else {
                    showAlert("Error", "Failed to delete menu item.");
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToDashboard() {
        SceneSwitcher.switchScene("admin-dashboard.fxml");
    }
}
