package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.models.Order;
import main.utils.DatabaseHelper;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.utils.SceneSwitcher;

import java.lang.constant.Constable;
import java.util.List;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Integer> userIdColumn;
    @FXML
    private TableColumn<Order, String> itemsColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    @FXML
    private Button updateOrderStatusButton;
    @FXML
    private Button deleteOrderButton;
    @FXML
    private Button manageMenuButton;
    @FXML
    private Button viewUsersButton;

    private ObservableList<Order> ordersList;

    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("items"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));



        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = DatabaseHelper.getAllOrders();
        ordersList = FXCollections.observableArrayList(orders);
        ordersTable.setItems(ordersList);
    }

    @FXML
    private void handleUpdateOrderStatus() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Error", "No order selected.");
            return;
        }

        ChoiceDialog<? extends Constable> dialog = new ChoiceDialog<>(selectedOrder.getOrderStatus(), "Pending", "Processing", "Completed", "Cancelled");
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Select new order status");
        dialog.setContentText("Choose status:");

        Optional<String> result = (Optional<String>) dialog.showAndWait();
        result.ifPresent(newStatus -> {
            if (DatabaseHelper.updateOrderStatus(selectedOrder.getId(), newStatus)) {
                showAlert("Success", "Order status updated to " + newStatus + ".");
                loadOrders();
            } else {
                showAlert("Error", "Failed to update order status.");
            }
        });
    }

    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Error", "No order selected.");
            return;
        }

        if (DatabaseHelper.deleteOrder(selectedOrder.getId())) {
            showAlert("Success", "Order deleted.");
            loadOrders();
        } else {
            showAlert("Error", "Failed to delete order.");
        }
    }

    @FXML
    private void handleManageMenu() {
        SceneSwitcher.switchScene("manage-menu.fxml");
    }

    @FXML
    private void handleViewUsers() {
        SceneSwitcher.switchScene("user-management.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLogout(ActionEvent actionEvent) {
        SceneSwitcher.switchScene("login-view.fxml");
    }
}
