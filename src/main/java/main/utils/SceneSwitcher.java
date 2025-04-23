package main.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneSwitcher {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } else {
                System.err.println("Error: Primary stage is not set.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchScene(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchScene(String fxmlFile, Object data, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof DataReceiver) {
                ((DataReceiver) controller).receiveData(data);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchSceneWithData(String fxmlFile, Consumer<Object> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            controllerSetup.accept(controller);

            Stage stage = primaryStage;
            if (stage == null) {
                stage = (Stage) root.getScene().getWindow();
            }
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
