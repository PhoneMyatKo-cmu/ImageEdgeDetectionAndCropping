package se233.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.project.controller.AlertDialog;
import se233.project.view.MainMenu;

import java.io.File;
import java.util.ArrayList;

public class Launcher extends Application {
    public static Stage primaryStage;
    public static ArrayList<File> imageFiles = new ArrayList<>();
    public static String outputPath;

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            AlertDialog.showDialog((Exception) throwable);
        });
        primaryStage = stage;
        Scene mainScene = new Scene(new MainMenu());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Edge Detect & Crop");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
