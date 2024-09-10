package se233.project.view;

import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import se233.project.Launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDImageDisplayArea extends ScrollPane {
    private GridPane imageViewGrid;
    private Map<File, Image> outputImages;
    private Image blank;
    static final int IMG_WIDTH = 600;

    public EDImageDisplayArea() {
        imageViewGrid = new GridPane();
        imageViewGrid.setHgap(10);
        imageViewGrid.setVgap(10);
        imageViewGrid.setPadding(new Insets(10, 10, 10, 10));
        imageViewGrid.setPrefWidth(IMG_WIDTH * 2 + 10);
        this.setContent(imageViewGrid);
        this.setMaxHeight(925);

        outputImages = new HashMap<>();

        blank = new Image(Launcher.class.getResourceAsStream("blank.png"));

        loadInputImages();
    }

    public void loadInputImages() {
        for (int i = 0; i < Launcher.imageFiles.size(); i++) {
            File imgFile = Launcher.imageFiles.get(i);
            Image image = new Image(imgFile.toURI().toString());
            ImageView imageView = resize(image);
            this.imageViewGrid.add(imageView, 0, i);
            this.imageViewGrid.add(resize(blank, imageView.getFitWidth(), imageView.getFitHeight()), 2, i);
        }
    }

    public void loadOutputImage(ImageView imgView, int row) {
        this.imageViewGrid.add(imgView, 2, row);
    }

//    public void addProgressIndicator(ProgressIndicator pi, int row) {
//        //this.imageViewGrid.add(pi, 4, row);
//    }

    public void addOutputImage(Image img, int row) {
        outputImages.put(Launcher.imageFiles.get(row), img);
    }

//    public void removeOutputFromGrid(int row) {
//        this.imageViewGrid.getChildren().remove(2 * row + 1 - row);
//    }

    public static ImageView resize(Image img) {
        ImageView imgView = new ImageView(img);
        double aspectRatio = img.getHeight() / img.getWidth();
        imgView.setFitWidth(IMG_WIDTH);
        imgView.setFitHeight(IMG_WIDTH * aspectRatio);

        return imgView;
    }
    private static ImageView resize(Image img, double w, double h) {
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(w);
        imgView.setFitHeight(h);
        return imgView;
    }

    public Map<File, Image> getOutputImages() {
        return outputImages;
    }
}
