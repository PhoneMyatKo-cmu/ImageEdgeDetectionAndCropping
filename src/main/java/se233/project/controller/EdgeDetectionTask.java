package se233.project.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import se233.project.controller.grayscale.Grayscale;
import se233.project.controller.util.Threshold;
import se233.project.model.*;
import se233.project.view.EDImageDisplayArea;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class EdgeDetectionTask extends Task<Image> {
    EdgeDetectionAlgorithms algo;
    File imgFile;
    EDImageDisplayArea imageDisplayArea;
    String kernelSize, cannyType;
    int weakThreshold, strongThreshold, row;
    boolean defaultThreshold;
    ProgressIndicator pi;

    public EdgeDetectionTask(EdgeDetectionAlgorithms algo, String kernelSize, String cannyType, boolean defaultThreshold, int weakThreshold, int strongThreshold, File imgFile, EDImageDisplayArea imageDisplayArea, int row, ProgressIndicator pi) {
        this.algo = algo;
        this.imgFile = imgFile;
        this.imageDisplayArea = imageDisplayArea;
        this.kernelSize = kernelSize;
        this.cannyType = cannyType;
        this.weakThreshold = weakThreshold;
        this.strongThreshold = strongThreshold;
        this.row = row;
        this.defaultThreshold = defaultThreshold;
        this.pi = pi;
    }

    @Override
    protected Image call() {
        Platform.runLater(() -> {
            pi.setVisible(true);
            pi.progressProperty().bind(this.progressProperty());
        });

        BufferedImage edgeImg = detectEdges(algo, kernelSize, cannyType, defaultThreshold, weakThreshold, strongThreshold, imgFile);
        for (int i = 0; i < 75; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            updateProgress(i, 100);
        }
        updateProgress(100, 100);
        if (edgeImg != null) {
            WritableImage img = SwingFXUtils.toFXImage(edgeImg, null);
            return img;
        }
        return null;
    }

    @Override
    protected void succeeded() {
        Image img = getValue();
        imageDisplayArea.addOutputImage(img, row);
//        pi.setVisible(false);
        Platform.runLater(() -> imageDisplayArea.loadOutputImage(imageDisplayArea.resize(img), row));
    }

    private BufferedImage detectEdges(EdgeDetectionAlgorithms algo, String kernelSize, String cannyType, boolean defaultThreshold, int weakThreshold, int strongThreshold, File imgFile) {
        EdgeDetector edgeDetector;
        try {
            BufferedImage img = ImageIO.read(imgFile);
            int[][] pixels = Grayscale.imgToGrayPixels(img);
            switch (algo) {
                case Canny -> {
                    edgeDetector = new CannyEdgeDetector.Builder(pixels)
                            .minEdgeSize(10)
                            .thresholds(weakThreshold, strongThreshold)
                            .L1norm(true)
                            .build();
                }
                case Laplacian -> {
                    edgeDetector = new LaplacianEdgeDetector(pixels, kernelSize, defaultThreshold, strongThreshold);
                }
                case Prewitt -> {
                    edgeDetector = new PrewittEdgeDetector(pixels, defaultThreshold, strongThreshold);
                }
                case RobertsCross -> {
                    edgeDetector = new RobertsCrossEdgeDetector(pixels, defaultThreshold, strongThreshold);
                }
                case Sobel -> {
                    edgeDetector = new SobelEdgeDetector(pixels, kernelSize, defaultThreshold, strongThreshold);
                }
                default -> {
                    edgeDetector = null;
                }
            }
            boolean[][] edges = edgeDetector.getEdges();

            if (cannyType.equals("Weak Strong")) {
                CannyEdgeDetector canny = (CannyEdgeDetector) edgeDetector;
                return Threshold.applyThresholdWeakStrongCanny(canny.getWeakEdges(), canny.getStrongEdges());
            } else if (cannyType.equals("Original Color")) {
                return Threshold.applyThresholdOriginal(edges, img);
            } else {
                return Threshold.applyThresholdReversed(edges);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
