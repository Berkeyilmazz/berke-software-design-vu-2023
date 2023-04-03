package softwaredesign;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CookieMain extends Application {
    public static void main (String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CookieClickerGUI.fxml")));

        // Check if we are able to fetch the required fxml file
        if(root == null){
            System.out.println("Couldn't fetch CookieClickerGUI.fxml");
            return;
        }

        // Set title
        primaryStage.setTitle("Cookie Clicker 2.0");
        // Force 1024p
        primaryStage.setScene(new Scene(root, 512, 512));
        primaryStage.show();

    }
}