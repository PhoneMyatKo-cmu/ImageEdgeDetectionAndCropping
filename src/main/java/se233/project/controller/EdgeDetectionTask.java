package se233.project.controller;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import se233.project.controller.grayscale.Grayscale;
import se233.project.controller.util.Threshold;
import se233.project.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class EdgeDetectionTask extends Task<Void> {
    EdgeDetectionAlgorithms algo;
    File imgFile;
    List<Image> outputImages;

    public EdgeDetectionTask(EdgeDetectionAlgorithms algo, File imgFile, List<Image> outputImages) {
        this.algo = algo;
        this.imgFile = imgFile;
        this.outputImages = outputImages;
    }

    @Override
    protected Void call() {
        BufferedImage edgeImg = detectEdges(algo, imgFile);
        if (edgeImg != null) {
            WritableImage img = SwingFXUtils.toFXImage(edgeImg, null);
            outputImages.add(img);
        }
        return null;
    }

    private BufferedImage detectEdges(EdgeDetectionAlgorithms algo, File imgFile) {
        try {
            BufferedImage img = ImageIO.read(imgFile);
            int[][] pixels = Grayscale.imgToGrayPixels(img);
            switch (algo) {
                case Canny -> {
                    CannyEdgeDetector canny = new CannyEdgeDetector.Builder(pixels)
                            .minEdgeSize(10)
                            .thresholds(20, 35)
                            .L1norm(false)
                            .build();
                    boolean[][] edges = canny.getEdges();
                    BufferedImage cannyImg = Threshold.applyThresholdReversed(edges);
                    BufferedImage weakStrongImg = Threshold.applyThresholdWeakStrongCanny(canny.getWeakEdges(), canny.getStrongEdges());
                    BufferedImage originalImg = Threshold.applyThresholdOriginal(edges, img);
                    return cannyImg;
                }
                case Laplacian -> {
                    LaplacianEdgeDetector laplacian = new LaplacianEdgeDetector(pixels);
                    boolean[][] edges = laplacian.getEdges();
                    return Threshold.applyThresholdReversed(edges);
                }
                case Prewitt -> {
                    PrewittEdgeDetector prewitt = new PrewittEdgeDetector(pixels);
                    boolean[][] edges = prewitt.getEdges();
                    return Threshold.applyThresholdReversed(edges);
                }
                case RobertsCross -> {
                    RobertsCrossEdgeDetector robertsCross = new RobertsCrossEdgeDetector(pixels);
                    boolean[][] edges = robertsCross.getEdges();
                    return Threshold.applyThresholdReversed(edges);
                }
                case Sobel -> {
                    SobelEdgeDetector sobel = new SobelEdgeDetector(pixels);
                    boolean[][] edges = sobel.getEdges();
                    return Threshold.applyThresholdReversed(edges);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
