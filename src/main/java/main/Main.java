package main;

import main.utils.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.utils.SceneSwitcher;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseInitializer.initializeDatabase();
        SceneSwitcher.setStage(primaryStage);
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/login-view.fxml"))));
        primaryStage.setTitle("Restaurant Ordering System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
