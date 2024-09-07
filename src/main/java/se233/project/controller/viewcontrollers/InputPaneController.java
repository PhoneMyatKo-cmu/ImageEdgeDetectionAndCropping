package se233.project.controller.viewcontrollers;

import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import se233.project.Launcher;
import se233.project.view.EdgeDetectionPane;
import se233.project.view.InputPane;
import se233.project.view.MainMenu;

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
}
