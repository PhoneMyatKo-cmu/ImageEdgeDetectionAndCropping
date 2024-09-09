package se233.project.controller.viewcontrollers;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se233.project.Launcher;
import se233.project.view.CropPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CropPaneController {
    public static void cropImage(Bounds bounds, ImageView imageView){
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        Image croppedImage = imageView.snapshot(parameters, wi);
        CropPane.wiList.add(wi);

        showCroppedImageNewStage(wi, croppedImage);
        System.out.println("Crop called.");
        System.out.println(CropPane.wiList);

    }
    private static void showCroppedImageNewStage(WritableImage wi, Image croppedImage) {
        final Stage croppedImageStage = new Stage();
        croppedImageStage.setResizable(true);
        croppedImageStage.setTitle("Cropped Image");
    //    changeStageSizeImageDimensions(croppedImageStage,croppedImage);
        final BorderPane borderPane = new BorderPane();
        final MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("File");
        final MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> saveCroppedImage(croppedImageStage));
        menu1.getItems().add(save);
        menuBar.getMenus().add(menu1);
        borderPane.setTop(menuBar);
        borderPane.setCenter(new ImageView(croppedImage));
        final Scene scene = new Scene(borderPane);
        croppedImageStage.setScene(scene);
        croppedImageStage.show();
        System.out.println("ShowCrop called");
    }
    public   static void saveCroppedImage(Stage stage) {
      /*  DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Save Image");
        //fileChooser.setInitialFileName("cats.png");

        File file = fileChooser.showDialog(stage);*/
        for(int i=0;i<CropPane.wiList.size();i++){
            File imgFile=new File(Launcher.outputPath +"\\crop"+i+".png");
            int finalI = i;
            Runnable runnable = () -> {
                BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(CropPane.wiList.get(finalI), null);
                BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(),
                        bufImageARGB.getHeight(), BufferedImage.BITMASK);

                Graphics2D graphics = bufImageRGB.createGraphics();
                graphics.drawImage(bufImageARGB, 0, 0, null);

                try {
                    ImageIO.write(bufImageRGB, "png", imgFile);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    graphics.dispose();
                    System.gc();
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();

        }

        /*if (file == null)
            return;*/


       // stage.close();
        System.out.println("Save called.");
    }
}
