package se233.project;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.project.view.MainMenu;

import java.io.File;
import java.util.ArrayList;

public class Launcher extends Application {
    public static Stage primaryStage;
    public static ArrayList<File> imageFiles = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        //testing merge github
        primaryStage = stage;
        Scene mainScene = new Scene(new MainMenu());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Edge Detect & Crop");
        primaryStage.show();
        //Test comment for merge conflict
    }

    public static void main(String[] args) {
        launch(args);
    }
}
