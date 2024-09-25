package se233.project.controller.viewcontrollers;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import se233.project.Launcher;
import se233.project.controller.AlertDialog;
import se233.project.controller.Unzip;
import se233.project.controller.customexceptions.EmptyFieldException;
import se233.project.view.CropPane;
import se233.project.view.EdgeDetectionPane;
import se233.project.view.MainMenu;

import java.io.File;
import java.util.List;

public class InputPaneController {
    public static void setOnInputBtn(FileChooser inputFileChooser, ListView<String> imageList) {
        List<File> files = inputFileChooser.showOpenMultipleDialog(Launcher.primaryStage);
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".zip")) {
                    List<File> zipFiles = Unzip.unzip(file);
                    for (File zipFile : zipFiles) {
                        imageList.getItems().add(zipFile.getName());
                        Launcher.imageFiles.add(zipFile);
                    }
                } else {
                    imageList.getItems().add(file.getName());
                    Launcher.imageFiles.add(file);
                }
            }
        }
    }

    public static void setOnOutputBtn(DirectoryChooser outputDirectoryChooser, TextField outputField) {
        File file = outputDirectoryChooser.showDialog(Launcher.primaryStage);
        if (file != null) {
            outputField.setText(file.getAbsolutePath());
        }
    }

    public static void setOnContinueBtn(String mode, TextField outputField) {
        try {
            if (Launcher.imageFiles.isEmpty()) {
                throw new EmptyFieldException(" Input Images");
            }
            if (outputField.getText().isEmpty() || outputField.getText() == null) {
                throw new EmptyFieldException("Output Path");
            }
            if (mode.equals("EdgeDetection")) {
                Launcher.primaryStage.setScene(new Scene(new EdgeDetectionPane()));
            } else if (mode.equals("Crop")) {
                Launcher.primaryStage.setScene(new Scene(new CropPane()));
                Launcher.primaryStage.setMaximized(false);
            }
            Launcher.outputPath = outputField.getText();
        } catch (Exception e) {
            AlertDialog.showDialog(e);
        }
    }

    public static void setOnBackBtn() {
        Launcher.primaryStage.setScene(new Scene(new MainMenu()));
    }

    public static void setOnDragOver(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        final boolean isAccepted = db.getFiles().get(0).getName().endsWith(".jpg") || db.getFiles().get(0).getName().endsWith(".png") || db.getFiles().get(0).getName().endsWith(".zip");
        if (db.hasFiles() && isAccepted) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        } else {
            dragEvent.consume();
        }
    }

    public static void setOnDragDropped(DragEvent dragEvent, ListView<String> imageList) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String fileName;
            int totalFiles = db.getFiles().size();
            for (int i = 0; i < totalFiles; i++) {
                File file = db.getFiles().get(i);
                fileName = file.getName();
                imageList.getItems().add(fileName);
                Launcher.imageFiles.add(file);
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    public static void updateImageListView(ListView<String> imageList, ScrollPane dropPane) {
        dropPane.setContent(imageList);
    }
}
