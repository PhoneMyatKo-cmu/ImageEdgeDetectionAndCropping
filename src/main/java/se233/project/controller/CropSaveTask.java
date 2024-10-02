package se233.project.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import se233.project.Launcher;
import se233.project.view.CropPane;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CropSaveTask extends Task<Void> {
    private final int finalI;
    private final String fileExtension;
    private final File imgFileForesee;
    private CountDownLatch countDownLatch;
    private static Random rand = new Random();

    public CropSaveTask(int finalI, String fileExtension, File imgFileForesee,CountDownLatch countDownLatch) {
        this.finalI = finalI;
        this.imgFileForesee = imgFileForesee;
        this.fileExtension = fileExtension;
        this.countDownLatch = countDownLatch;
    }


    @Override
    protected Void call() throws Exception {

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(CropPane.wiList.get(finalI), null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(),
                bufImageARGB.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        for (int i = 0; i <= 100; i++) {
            updateProgress(i, 100);

            TimeUnit.MILLISECONDS.sleep(rand.nextInt(10,50));

        }

        try {
            if (!new File(Launcher.outputPath).exists()){
                throw new FileNotFoundException();
            }

            ImageIO.write(bufImageRGB, fileExtension, imgFileForesee);

        } catch (IOException e ) {
            cancel();
        }
        finally {
            countDownLatch.countDown();
            graphics.dispose();
            System.gc();
        }
        return null;
    }


    @Override
    protected void succeeded() {
        Platform.runLater(() -> {
                    if (CropPane.CroppedimageListView.getItems().get(finalI).getChildren().size() > 2) {
                        CropPane.CroppedimageListView.getItems().get(finalI).getChildren().remove(2);
                    }
                    CropPane.CroppedimageListView.getItems().get(finalI).getChildren().add(new javafx.scene.control.Label("Saved."));

                }
        );
    }

    protected void cancelled() {
        Platform.runLater(() -> {

            if (CropPane.CroppedimageListView.getItems().get(finalI).getChildren().size() > 3) {
                CropPane.CroppedimageListView.getItems().get(finalI).getChildren().remove(2);
            }
            ProgressIndicator progressIndicator = (ProgressIndicator) (CropPane.CroppedimageListView.getItems().get(finalI).getChildren().get(1));
            progressIndicator.progressProperty().unbind();
            progressIndicator.setProgress(-1);

            CropPane.CroppedimageListView.getItems().get(finalI).getChildren().add(2, new Label("Save Failed!"));


        });


    }

}
