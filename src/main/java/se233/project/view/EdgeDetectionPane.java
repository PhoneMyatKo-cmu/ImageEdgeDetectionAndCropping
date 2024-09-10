package se233.project.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se233.project.Launcher;
import se233.project.controller.viewcontrollers.EdgeDetectionPaneController;
import se233.project.model.EdgeDetectionAlgorithms;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static se233.project.model.EdgeDetectionAlgorithms.*;

public class EdgeDetectionPane extends BorderPane {
    private List<Image> outputImages = new ArrayList<Image>();
    private VBox settingArea = new VBox(25);
    private GridPane imageViewGrid = new GridPane();
    private ScrollPane imageViewScroll = new ScrollPane(imageViewGrid);
    private ComboBox<EdgeDetectionAlgorithms> edgeDetectionAlgorithm = new ComboBox<>();
    private Button previewButton = new Button("Preview");
    private Button saveButton = new Button("Save");
    private Button backButton = new Button("Back");
    private Group algorithmSettingGp = new Group();

    public EdgeDetectionPane() {
        this.setLeft(settingArea);
        this.setCenter(imageViewScroll);
        Launcher.primaryStage.setMaximized(true);
        drawSettingArea();
        refresh();
    }

    public void refresh() {
        drawImageViewArea();
    }

    private void drawImageViewArea() {
        loadImages(imageViewGrid);
        this.setCenter(imageViewScroll);
        imageViewGrid.setPrefWidth(1300);
        GridPane.setHalignment(imageViewGrid, HPos.CENTER);
        GridPane.setValignment(imageViewGrid, VPos.CENTER);
        imageViewGrid.setHgap(10);
        imageViewGrid.setVgap(10);
        imageViewScroll.setMaxHeight(910);
    }
    private void drawSettingArea() {
        edgeDetectionAlgorithm.getItems().addAll(Canny, Laplacian, Prewitt, RobertsCross, Sobel);
        edgeDetectionAlgorithm.setValue(Canny);
        settingArea.setPadding(new Insets(15));
        settingArea.getChildren().addAll(edgeDetectionAlgorithm, new HBox(30, backButton, new VBox(10, previewButton, saveButton)), algorithmSettingGp);
        EdgeDetectionPaneController.setOnAlgorithmChange(Canny, algorithmSettingGp);

        backButton.setOnAction(e -> EdgeDetectionPaneController.setOnBack(this));
        previewButton.setOnAction(e -> { EdgeDetectionPaneController.setOnPreview(edgeDetectionAlgorithm.getValue(), imageViewGrid, outputImages); refresh();});
        edgeDetectionAlgorithm.valueProperty().addListener((observableValue, oldV, newV) -> EdgeDetectionPaneController.setOnAlgorithmChange(newV, algorithmSettingGp));
    }

    public void loadImages(GridPane imageViewGrid) {
        imageViewGrid.getChildren().clear();
        Image blank = new Image(Launcher.class.getResourceAsStream("blank.png"));
        try {
            for (int i = 0; i < Launcher.imageFiles.size(); i++) {
                File imgFile = Launcher.imageFiles.get(i);
                Image img = new Image(imgFile.toURI().toURL().toString());
                ImageView imageView = resize(img);
                imageViewGrid.add(imageView, 0, i);
                if (outputImages.isEmpty()) {
                    imageViewGrid.add(resize(blank, imageView.getFitWidth(), imageView.getFitHeight()), 1, i);
                } else {
                    imageViewGrid.add(resize(outputImages.get(i)), 1, i);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (Launcher.imageFiles.isEmpty()) {
            imageViewGrid.addRow(0, resize(blank), resize(blank));
        }

    }

    public static ImageView resize(Image img) {
        ImageView imgView = new ImageView(img);
        if (img.getWidth() > 0) {
            double aspectRatio = img.getHeight() / img.getWidth();
            imgView.setFitWidth(600);
            imgView.setFitHeight(600 * aspectRatio);
        }
        return imgView;
    }
    private static ImageView resize(Image img, double w, double h) {
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(w);
        imgView.setFitHeight(h);
        return imgView;
    }

    public void setOutputImages(List<Image> outputImages) {
        this.outputImages = outputImages;
    }
}
