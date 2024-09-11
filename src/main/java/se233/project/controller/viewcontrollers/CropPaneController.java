package se233.project.controller.viewcontrollers;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
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
import javafx.stage.Stage;
import se233.project.Launcher;
import se233.project.view.CropPane;
import se233.project.view.InputPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class CropPaneController {
    public static void cropImage(Bounds bounds, ImageView imageView){
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        System.out.println("Crop Method: Bound::"+width+" ,"+height);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        Image croppedImage = imageView.snapshot(parameters, wi);
      //  String fileType=getFileExtension(new File(imageView.getImage().getUrl()));
      //  System.out.println(imageView.getImage().);
        CropPane.wiList.add(wi);
        CropPane.previewImgList.add(new ImageView(croppedImage));
        CropPane.croppedFilesList.add(Launcher.imageFiles.get(CropPane.imageIndex));
        CropPane.refreshPreview(croppedImage);

        //showCroppedImageNewStage(wi, croppedImage);
      //  System.out.println("Crop called.");
       // System.out.println(CropPane.wiList);

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

        for(int i=0;i<CropPane.wiList.size();i++){
            String fileExtension=getFileExtension(CropPane.croppedFilesList.get(i));
            String fileFullName=CropPane.croppedFilesList.get(i).getName();
            String fileName=fileFullName.substring(0,fileFullName.length()-4)+"Cropped";
            File imgFile=new File(Launcher.outputPath +"\\"+fileName+i+"."+fileExtension );


            final File imgFileForuse=imgFile;
            int finalI = i;
            Runnable runnable = () -> {
                BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(CropPane.wiList.get(finalI), null);
                BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(),
                        bufImageARGB.getHeight(), BufferedImage.OPAQUE);

                Graphics2D graphics = bufImageRGB.createGraphics();
                graphics.drawImage(bufImageARGB, 0, 0, null);

                try {
                    ImageIO.write(bufImageRGB, fileExtension, imgFileForuse);
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

    public static Image convertFileToImage(File imageFile) {
        Image image = null;
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)){
            image = new Image(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void clearSelection(Group group) {
        //deletes everything except for base container layer
        CropPane.isAreaSelected = false;
        group.getChildren().remove(1,group.getChildren().size());

    }

    public static void changeStageSizeImageDimensions(Stage stage, Image image) {
        if (image != null) {
            stage.setMinHeight(250);
            stage.setMinWidth(250);
            stage.setWidth(image.getWidth()+4);
            stage.setHeight(image.getHeight()+56);
        }
        stage.show();
    }
    public  static String getFileExtension(File file) {
        String fileName = file.getName();

        // Find the last occurrence of '.'
        int dotIndex = fileName.lastIndexOf('.');

        // If the dot is found and it's not the first character, extract the extension
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase(); // Return the extension in lowercase
        } else {
            return ""; // No extension found
        }
    }

    public static void changeImageViewSize(Image image,ImageView imageView){
        if(image.getWidth()<=800 && image.getHeight()<=600){
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
        }
        else {
            imageView.setFitWidth(800);
            imageView.setFitHeight(600);
        }
    }



}
