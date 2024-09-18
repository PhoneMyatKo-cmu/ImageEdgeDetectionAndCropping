package se233.project.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import se233.project.controller.viewcontrollers.EDSettingAreaController;
import se233.project.model.EdgeDetectionAlgorithms;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

import static se233.project.model.EdgeDetectionAlgorithms.*;

public class EDSettingArea extends VBox {
    private EDImageDisplayArea imageDisplayArea;
    private String kernelSize, cannyType;
    private ComboBox<EdgeDetectionAlgorithms> algorithmsComboBox;
    private ToggleGroup kernelToggleGroup, cannyToggleGroup;
    private RadioButton cannyWeakStrong;
    private HBox weakThresholdBox, thresholdBox, kernelBox, cannyBox;
    private CheckBox thresholdCheckBox;
    private ProgressView progressListView;

    public EDSettingArea(EDImageDisplayArea imageDisplayArea) {
        this.imageDisplayArea = imageDisplayArea;
        // choose algorithm
        algorithmsComboBox = new ComboBox<>();
        algorithmsComboBox.getItems().addAll(EdgeDetectionAlgorithms.values());
        // choose kernel size
        kernelBox = new HBox(5);
        kernelToggleGroup = new ToggleGroup();
        RadioButton kernel3x3 = new RadioButton("3x3");
        RadioButton kernel5x5 = new RadioButton("5x5");
        kernelToggleGroup.getToggles().addAll(kernel3x3, kernel5x5);
        kernelBox.getChildren().addAll(kernel3x3, kernel5x5);
        // choose canny output image type
        cannyBox = new HBox(5);
        cannyToggleGroup = new ToggleGroup();
        RadioButton cannyDefault = new RadioButton("Default");
        cannyWeakStrong = new RadioButton("Weak Strong");
        RadioButton cannyOriginalColor = new RadioButton("Original Color");
        cannyToggleGroup.getToggles().addAll(cannyDefault, cannyWeakStrong, cannyOriginalColor);
        cannyBox.getChildren().addAll(cannyDefault, cannyWeakStrong, cannyOriginalColor);
        // default or custom threshold
        thresholdCheckBox = new CheckBox("Default Threshold");
        // change weak threshold for canny
        weakThresholdBox = new HBox(5);
        TextField weakThresholdTextField = new TextField();
        weakThresholdTextField.setPrefWidth(40);
        Slider weakThresholdSlider = new Slider(0, 255, 10);
        weakThresholdSlider.setShowTickLabels(true);
        weakThresholdSlider.setShowTickMarks(true);
        weakThresholdSlider.setBlockIncrement(1);
        weakThresholdSlider.setMajorTickUnit(25);
        weakThresholdSlider.setMinorTickCount(25);
        weakThresholdTextField.textProperty().bindBidirectional(weakThresholdSlider.valueProperty(), new DecimalFormat("###"));
        weakThresholdBox.getChildren().addAll(weakThresholdTextField, weakThresholdSlider, new Label("255"));
        // change threshold (strong for canny)
        thresholdBox = new HBox(5);
        TextField thresholdTextField = new TextField();
        thresholdTextField.setPrefWidth(40);
        Slider thresholdSlider = new Slider(0, 255, 25);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setShowTickMarks(true);
        thresholdSlider.setMajorTickUnit(25);
        thresholdSlider.setBlockIncrement(1);
        thresholdSlider.setMinorTickCount(25);
        thresholdTextField.textProperty().bindBidirectional(thresholdSlider.valueProperty(), new DecimalFormat("###"));
        thresholdBox.getChildren().addAll(thresholdTextField, thresholdSlider, new Label("255"));
        // buttons
        HBox buttonBox = new HBox(30);
        Button backButton = new Button("Back");
        Button previewButton = new Button("Preview");
        Button saveButton = new Button("Save");
        buttonBox.getChildren().addAll(backButton, new VBox(10, previewButton, saveButton));

        // padding & spacing
        algorithmsComboBox.setPadding(new Insets(10));
        kernelBox.setPadding(new Insets(10));
        cannyBox.setPadding(new Insets(10));
        thresholdCheckBox.setPadding(new Insets(10));
        weakThresholdBox.setPadding(new Insets(10));
        thresholdBox.setPadding(new Insets(10));
        buttonBox.setPadding(new Insets(20, 10, 10, 10));

        backButton.setPrefSize(100, 30);
        previewButton.setPrefSize(100, 30);
        saveButton.setPrefSize(100, 30);

        // progress list view
        progressListView = new ProgressView();


        // group by vbox
        this.getChildren().addAll(algorithmsComboBox, kernelBox, cannyBox, thresholdCheckBox, weakThresholdBox, thresholdBox, buttonBox, progressListView);

        // handlers
        kernelToggleGroup.selectedToggleProperty().addListener(((observableValue, oldV, newV) -> setKernelSize(((RadioButton) newV).getText())));
        cannyToggleGroup.selectedToggleProperty().addListener(((observableValue, toggle, t1) -> setCannyType(((RadioButton) t1).getText())));
        algorithmsComboBox.valueProperty().addListener(((observableValue, edgeDetectionAlgorithms, t1) -> EDSettingAreaController.setOnAlgorithmChange(t1, cannyDefault, cannyWeakStrong, kernelBox, weakThresholdBox, thresholdCheckBox)));
        thresholdCheckBox.selectedProperty().addListener(((observableValue, aBoolean, t1) -> EDSettingAreaController.setOnDefaultThresholdChange(algorithmsComboBox.getValue(), t1, weakThresholdBox, thresholdBox)));
        thresholdCheckBox.setSelected(true);
        kernelToggleGroup.selectToggle(kernel3x3);
        cannyToggleGroup.selectToggle(cannyDefault);
        algorithmsComboBox.setValue(Canny);
        previewButton.setOnAction(e -> EDSettingAreaController.setOnPreview(algorithmsComboBox.getValue(), kernelSize, cannyType, thresholdCheckBox.isSelected(), Integer.parseInt(weakThresholdTextField.getText()), Integer.parseInt(thresholdTextField.getText()), imageDisplayArea, progressListView));
        backButton.setOnAction(e -> EDSettingAreaController.setOnBack());
        saveButton.setOnAction(e -> EDSettingAreaController.setOnSave(imageDisplayArea));
    }

    public void setKernelSize(String kernelSize) {
        this.kernelSize = kernelSize;
    }

    public void setCannyType(String cannyType) {
        this.cannyType = cannyType;
    }


}
