package se233.project.controller.viewcontrollers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import java.util.concurrent.TimeUnit;

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
        HBox croppedHbox=new HBox(20);
        croppedHbox.setOnMouseClicked(event -> {

                int selectedIndex= CropPane.CroppedimageListView.getSelectionModel().getSelectedIndex();
                CropPane.previewImgView.setImage(CropPane.wiList.get(selectedIndex));
                System.out.println("Click on list cell index:"+selectedIndex);
        });
        ProgressIndicator progressIndicator=new ProgressIndicator();
        progressIndicator.setVisible(false);
        croppedHbox.getChildren().addAll(new Label(getFileName(CropPane.croppedFilesList.size()-1)),progressIndicator);
        CropPane.CroppedimageListView.getItems().add(croppedHbox);

        //showCroppedImageNewStage(wi, croppedImage);
      //  System.out.println("Crop called.");
       // System.out.println(CropPane.wiList);

    }

    public   static void saveCroppedImage(Stage stage) {
        if(CropPane.wiList.isEmpty() || CropPane.wiList==null){
            System.out.println("No Cropped Image..");
            return;
        }

        for(int i=0;i<CropPane.wiList.size();i++){
            String fileExtension=getFileExtension(CropPane.croppedFilesList.get(i));
            String fileFullName=CropPane.croppedFilesList.get(i).getName();
            String fileName=fileFullName.substring(0,fileFullName.length()-4)+"Cropped";
            File imgFile=new File(Launcher.outputPath +"\\"+fileName+i+"."+fileExtension );


            final File imgFileForuse=imgFile;
            int finalI = i;
            Task saveTask=new Task() {
                @Override
                protected Object call() throws Exception {

                    BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(CropPane.wiList.get(finalI), null);
                    BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(),
                            bufImageARGB.getHeight(), BufferedImage.OPAQUE);

                    Graphics2D graphics = bufImageRGB.createGraphics();
                    graphics.drawImage(bufImageARGB, 0, 0, null);

                   for(int i=0;i<=100;i++){
                       updateProgress(i,100);
                       TimeUnit.MILLISECONDS.sleep(10);
                       System.out.println("Save Progress:"+i);

                   }

                    try {
                        ImageIO.write(bufImageRGB, fileExtension, imgFileForuse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        graphics.dispose();
                        System.gc();
                    }
                    System.out.println("Save Task called.");

                    return null;
                }
            };
            saveTask.setOnSucceeded(event -> Platform.runLater(()-> CropPane.CroppedimageListView.getItems().get(finalI).getChildren().add(new Label("Saved."))));
            CropPane.CroppedimageListView.modifyListCell(finalI,saveTask);

            Thread thread = new Thread(saveTask);
            thread.start();

        }


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
         //   imageView.setFitHeight(imageView.getFitHeight());
         //   imageView.s;
        }

        System.out.println("Image size:"+image.getWidth()+","+image.getHeight());

        System.out.println("After Changing Image View Size:"+imageView.getFitWidth()+","+imageView.getFitHeight());
        System.out.println("After changing inbound parentS:"+imageView.getBoundsInParent().getWidth()+","+imageView.getBoundsInParent().getHeight());
        System.out.println("After chaniging, bound in local:"+imageView.getBoundsInLocal().getWidth()+","+imageView.getBoundsInLocal().getHeight());
    }

    public static String getFileName(int i){
        String fileExtension=getFileExtension(CropPane.croppedFilesList.get(i));
        String fileFullName=CropPane.croppedFilesList.get(i).getName();
        String fileName=fileFullName.substring(0,fileFullName.length()-4)+"Cropped";
        File imgFile=new File(Launcher.outputPath +"\\"+fileName+i+"."+fileExtension );
        return imgFile.getName();
    }

    public static void clearList(){
        if(CropPane.croppedFilesList!=null && CropPane.wiList!=null && CropPane.CroppedimageListView!=null ){
            CropPane.croppedFilesList.clear();
            CropPane.wiList.clear();
            CropPane.CroppedimageListView.getItems().clear();
        }
    }



}
