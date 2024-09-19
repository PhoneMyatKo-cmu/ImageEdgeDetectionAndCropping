package se233.project.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertDialog {
    public static void showDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showDialog(Exception e) {
        Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK);
        alert.setTitle(e.getClass().getSimpleName());
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
