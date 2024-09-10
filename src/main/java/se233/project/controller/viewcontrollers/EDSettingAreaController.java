package se233.project.controller.viewcontrollers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import se233.project.Launcher;
import se233.project.controller.EdgeDetectionTask;
import se233.project.model.*;
import se233.project.view.EDImageDisplayArea;
import se233.project.view.InputPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class EDSettingAreaController {
    public static void setOnBack() {
        Launcher.imageFiles.clear();
        Launcher.primaryStage.setScene(new Scene(new InputPane("EdgeDetection")));
        Launcher.primaryStage.setMaximized(false);
    }

    public static void setOnPreview(EdgeDetectionAlgorithms algo, String kernelSize, String cannyType, boolean defaultThreshold, int weakThreshold, int strongThreshold, EDImageDisplayArea imageDisplayArea) {
        for (int i = 0; i < Launcher.imageFiles.size(); i++) {
            //imageDisplayArea.removeOutputFromGrid(i);
            File imgFile = Launcher.imageFiles.get(i);
            EdgeDetectionTask task = new EdgeDetectionTask(algo, kernelSize, cannyType, defaultThreshold, weakThreshold, strongThreshold, imgFile, imageDisplayArea, i);
            new Thread(task).start();
        }
    }

    public static void setOnSave(EDImageDisplayArea imageDisplayArea) {
        Map<File, Image> outputImages = imageDisplayArea.getOutputImages();
        File output = new File(Launcher.outputPath);
        if (!output.exists()) {
            output.mkdirs();
        }
        try {
            for (int i = 0; i < Launcher.imageFiles.size(); i++) {
                Image img = outputImages.get(Launcher.imageFiles.get(i));
                String fileName = Launcher.imageFiles.get(i).getName();
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", new File(Launcher.outputPath + File.separator + fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOnAlgorithmChange(EdgeDetectionAlgorithms newAlgo, RadioButton weakStrongButton, HBox kernelBox, HBox weakThresholdBox, boolean defaultThreshold) {
        switch (newAlgo) {
            case Canny -> {
                kernelBox.setVisible(false);
                weakStrongButton.setVisible(true);
                weakThresholdBox.setVisible(!defaultThreshold);
            }
            case Laplacian, Sobel -> {
                kernelBox.setVisible(true);
                weakStrongButton.setVisible(false);
                weakThresholdBox.setVisible(false);
            }
            case Prewitt, RobertsCross -> {
                kernelBox.setVisible(false);
                weakStrongButton.setVisible(false);
                weakThresholdBox.setVisible(false);
            }
        }
    }

    public static void setOnDefaultThresholdChange(EdgeDetectionAlgorithms algo, boolean defaultThreshold, HBox weakThresholdBox, HBox thresholdBox) {
        switch (algo) {
            case Canny -> {
                weakThresholdBox.setVisible(!defaultThreshold);
                thresholdBox.setVisible(!defaultThreshold);
            }
            case Laplacian, Prewitt, RobertsCross, Sobel -> {
                thresholdBox.setVisible(!defaultThreshold);
            }
        }
    }
}
