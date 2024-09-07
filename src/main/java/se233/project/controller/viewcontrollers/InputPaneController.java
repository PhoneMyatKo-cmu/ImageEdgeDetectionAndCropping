package se233.project.controller.viewcontrollers;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import se233.project.Launcher;
import se233.project.view.EdgeDetectionPane;
import se233.project.view.InputPane;
import se233.project.view.MainMenu;

import java.io.File;

public class InputPaneController {
    public static void setOnInputBtn(FileChooser inputFileChooser) {
        inputFileChooser.showOpenDialog(Launcher.primaryStage);
    }

    public static void setOnOutputBtn(DirectoryChooser outputDirectoryChooser) {
        outputDirectoryChooser.showDialog(Launcher.primaryStage);
    }

    public static void setOnContinueBtn(String mode) {
        if (mode.equals("EdgeDetection")) {
            Launcher.primaryStage.setScene(new Scene(new EdgeDetectionPane()));
        } else if (mode.equals("Crop")) {

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
}
