package se233.project.controller.viewcontrollers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import se233.project.Launcher;
import se233.project.controller.AlertDialog;
import se233.project.controller.EdgeDetectionTask;
import se233.project.controller.customexceptions.InvalidThresholdException;
import se233.project.model.*;
import se233.project.view.EDImageDisplayArea;
import se233.project.view.InputPane;
import se233.project.view.ProgressView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static se233.project.model.EdgeDetectionAlgorithms.Canny;

public class EDSettingAreaController {
    public static void setOnBack() {
        Launcher.imageFiles.clear();
        Launcher.primaryStage.setScene(new Scene(new InputPane("EdgeDetection")));
        Launcher.primaryStage.setMaximized(false);
    }

    public static void setOnPreview(EdgeDetectionAlgorithms algo, String kernelSize, String cannyType, boolean defaultThreshold, int weakThreshold, int strongThreshold, EDImageDisplayArea imageDisplayArea, ProgressView progressView) {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            if (algo.equals(Canny) && weakThreshold > strongThreshold) {
                throw new InvalidThresholdException(weakThreshold, strongThreshold);
            }
            for (int i = 0; i < Launcher.imageFiles.size(); i++) {
                File imgFile = Launcher.imageFiles.get(i);
                ProgressIndicator pi = progressView.get(i);
                EdgeDetectionTask task = new EdgeDetectionTask(algo, kernelSize, cannyType, defaultThreshold, weakThreshold, strongThreshold, imgFile, imageDisplayArea, i, pi);
                Platform.runLater(() -> {
                    pi.progressProperty().bind(task.progressProperty());
                    pi.setVisible(true);
                });
                executorService.submit(task);
            }
            executorService.shutdown();
        } catch (Exception e) {
            AlertDialog.showDialog(e);
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
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
                BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics g = convertedImage.createGraphics();
                g.drawImage(bufferedImage, 0, 0, null);
                g.dispose();
                String fileName = Launcher.imageFiles.get(i).getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                ImageIO.write(convertedImage, fileExtension, new File(output.getPath() + File.separator + "EdgeDetected_" +  fileName));
            }
            AlertDialog.showDialog("Saved", "Successfully saved EdgeDetection images to " + output.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOnAlgorithmChange(EdgeDetectionAlgorithms newAlgo, RadioButton defaultButton, RadioButton weakStrongButton, HBox kernelBox, VBox weakThresholdBox, CheckBox thresholdCheckBox) {
        switch (newAlgo) {
            case Canny -> {
                kernelBox.setVisible(false);
                weakStrongButton.setVisible(true);
                weakThresholdBox.setVisible(true);
                thresholdCheckBox.setVisible(false);
                thresholdCheckBox.setSelected(false);
            }
            case Laplacian, Sobel -> {
                kernelBox.setVisible(true);
                weakStrongButton.setVisible(false);
                weakThresholdBox.setVisible(false);
                thresholdCheckBox.setVisible(true);
                thresholdCheckBox.setSelected(true);
                defaultButton.setSelected(true);
            }
            case Prewitt, RobertsCross -> {
                kernelBox.setVisible(false);
                weakStrongButton.setVisible(false);
                weakThresholdBox.setVisible(false);
                thresholdCheckBox.setVisible(true);
                thresholdCheckBox.setSelected(true);
                defaultButton.setSelected(true);
            }
        }
    }

    public static void setOnDefaultThresholdChange(EdgeDetectionAlgorithms algo, boolean defaultThreshold, VBox weakThresholdBox, VBox thresholdBox) {
        thresholdBox.setVisible(!defaultThreshold);
    }

    public static void setOnThresholdTextInputChange(TextField thresholdInputField, Slider thresholdSlider, String newValue) {
        try {
            if (newValue.isEmpty()) {
                thresholdInputField.setText("0");
            } else if (!newValue.matches("\\d*")) {
                thresholdInputField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 3) {
                thresholdInputField.setText(newValue.substring(1, 4));
            } else if (Integer.parseInt(newValue) > 255) {
                thresholdInputField.setText("255");
                thresholdSlider.setValue(255);
                throw new InvalidThresholdException(Integer.parseInt(newValue));
            }
            thresholdInputField.setText(Integer.parseInt(thresholdInputField.getText()) + "");
            thresholdSlider.setValue(Integer.parseInt(thresholdInputField.getText()));
        } catch (Exception e) {
            AlertDialog.showDialog(e);
        }
    }

    public static void setOnThresholdSliderInput(TextField thresholdTextField, int newValue) {
        thresholdTextField.setText(String.format("%d", newValue));
    }

}
