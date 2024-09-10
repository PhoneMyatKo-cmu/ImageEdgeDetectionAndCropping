package se233.project.controller.viewcontrollers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import se233.project.Launcher;
import se233.project.controller.EdgeDetectionTask;
import se233.project.model.*;
import se233.project.view.EdgeDetectionPane;
import se233.project.view.InputPane;

import java.io.File;
import java.util.List;

import static se233.project.model.EdgeDetectionAlgorithms.*;

public class EdgeDetectionPaneController {
    public static void setOnBack(Pane pane) {
        Launcher.primaryStage.setScene(new Scene(new InputPane("EdgeDetection")));
        Launcher.primaryStage.setMaximized(false);
    }

    public static void setOnPreview(EdgeDetectionAlgorithms algo, GridPane imageViewGrid, List<Image> outputImages) {
        outputImages.clear();
        for (int i = 0; i < Launcher.imageFiles.size(); i++) {
            File imgFile = Launcher.imageFiles.get(i);
            EdgeDetectionTask task = new EdgeDetectionTask(algo, imgFile, outputImages);
            task.run();
        }
    }

    public static void setOnAlgorithmChange(EdgeDetectionAlgorithms newAlgo, Group algorithmSettingGp) {
        algorithmSettingGp.getChildren().clear();
        switch (newAlgo) {
            case Canny -> {
                ButtonBar imgType = new ButtonBar("Normal");
                algorithmSettingGp.getChildren().add(imgType);
            }
        }
        Slider thresholdSlider = new Slider(0, 100, 20);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setShowTickMarks(true);
        thresholdSlider.setMajorTickUnit(10);
        thresholdSlider.setMinorTickCount(0);
        thresholdSlider.setBlockIncrement(1);
        Label thresholdLbl = new Label();
        thresholdLbl.textProperty().bind(thresholdSlider.valueProperty().asString("%.0f"));
        algorithmSettingGp.getChildren().add(new HBox(5, thresholdLbl, thresholdSlider));
    }

}
