package se233.project.controller.viewcontrollers;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import se233.project.Launcher;
import se233.project.controller.AlertDialog;
import se233.project.controller.CropSaveTask;
import se233.project.model.AreaSelection;
import se233.project.model.ResizableRectangle;
import se233.project.model.ResizableRectangleWithRatio1;
import se233.project.view.CropBoxConfigPane;
import se233.project.view.CropPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class CropPaneController {
    public static double zoomFactor = 1;
    private static  ExecutorService service;
    private static CountDownLatch countDownLatch ;


    public static void cropImage(Bounds bounds, ImageView imageView) {
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));
        WritableImage wi = new WritableImage(width, height);
        Image croppedImage = imageView.snapshot(parameters, wi);
        CropPane.wiList.add(wi);
        CropPane.previewImgList.add(new ImageView(croppedImage));
        CropPane.croppedFilesList.add(Launcher.imageFiles.get(CropPane.imageIndex));
        CropPane.refreshPreview(croppedImage);
        addToCroppedList();
    }

    public static void addToCroppedList(){
        HBox croppedHbox = new HBox(20);
        croppedHbox.setOnMouseClicked(event -> {
            int selectedIndex = CropPane.CroppedimageListView.getSelectionModel().getSelectedIndex();
            CropPane.previewImgView.setImage(CropPane.wiList.get(selectedIndex));
           /* System.out.println("Click on list cell index:" + selectedIndex);*/
        });
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        Button removeBtn = new Button("remove");
        removeBtn.setOnAction(actionEvent ->
        {
            int index = CropPane.CroppedimageListView.getItems().indexOf(removeBtn.getParent());
            if(CropPane.previewImgView.getImage().equals(CropPane.wiList.get(index))){
                CropPane.previewImgView.setImage(CropPane.blankImage);
            }
            CropPane.CroppedimageListView.getItems().remove(removeBtn.getParent());
         /*   System.out.println("Remove index:" + index + " Size of wiList:" + CropPane.wiList.size() + " Size of CrList:" + CropPane.croppedFilesList.size());*/
            CropPane.wiList.remove(index);
            CropPane.croppedFilesList.remove(index);

        });
        croppedHbox.getChildren().addAll(new Label(getFileName(CropPane.croppedFilesList.size() - 1)), progressIndicator, removeBtn);
        CropPane.CroppedimageListView.getItems().add(croppedHbox);

    }

    public static void saveCroppedImage() {
        if (CropPane.wiList.isEmpty()) {
           /* System.out.println("No Cropped Image..");*/
            return;
        }
        service=Executors.newFixedThreadPool(15);
        countDownLatch=new CountDownLatch(CropPane.wiList.size());


        for (int i = 0; i < CropPane.wiList.size(); i++) {
            String fileExtension = getFileExtension(CropPane.croppedFilesList.get(i));
            String fileFullName = CropPane.croppedFilesList.get(i).getName();
            String fileName = fileFullName.substring(0, fileFullName.length() - 4) + "Cropped";
            final File imgFileForesee = new File(Launcher.outputPath + "\\" + fileName + i + "." + fileExtension);
            CropSaveTask cropSaveTask = new CropSaveTask(i, fileExtension, imgFileForesee,countDownLatch);
            CropPane.CroppedimageListView.modifyListCell(i, cropSaveTask);
            service.submit(cropSaveTask);

        }
        service.submit(()->{
            boolean flag=new File(Launcher.outputPath).exists();
            try {
                countDownLatch.await();
                TimeUnit.MILLISECONDS.sleep(100);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->{

                if(!flag){

                    AlertDialog.showDialog(new Exception("Invalid Path."));
                }
                else {
                    AlertDialog.showDialog("Save", "Save successful");
                }
            });



        });

        service.shutdown();

 /*       System.out.println("Save called.");*/
    }

    public static Image convertFileToImage(File imageFile) throws IOException {
        Image image;
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            image = new Image(fileInputStream);
        } catch (IOException e) {
            throw e;
        }
        return image;
    }

    public static void clearSelection(Group group) {
        //deletes everything except for base container layer
        CropPane.isAreaSelected = false;
        group.getChildren().remove(1, group.getChildren().size());

    }

    public static void changeStageSizeImageDimensions(Stage stage, Image image) {
        if (image != null) {
            stage.setMinHeight(250);
            stage.setMinWidth(250);
            stage.setWidth(image.getWidth() + 4);
            stage.setHeight(image.getHeight() + 56);
        }
        stage.show();
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();

        // Find the last occurrence of '.'
        int dotIndex = fileName.lastIndexOf('.');

        // If the dot is found, and it's not the first character, extract the extension
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase(); // Return the extension in lowercase
        } else {
            return ""; // No extension found
        }
    }

    public static void changeImageViewSize(Image image, ImageView imageView) {
        imageView.setPreserveRatio(true);
        if (image.getWidth() <= 800 && image.getHeight() <= 600) {
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
        } else {
            imageView.setFitWidth(800);
        }

        if (image.getWidth() < 300 || image.getHeight() < 300) {
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);

        }

    /*    System.out.println("Image size:" + image.getWidth() + "," + image.getHeight());
        System.out.println("After Changing Image View Size:" + imageView.getFitWidth() + "," + imageView.getFitHeight());
        System.out.println("After changing inbound parentS:" + imageView.getBoundsInParent().getWidth() + "," + imageView.getBoundsInParent().getHeight());
        System.out.println("After changing, bound in local:" + imageView.getBoundsInLocal().getWidth() + "," + imageView.getBoundsInLocal().getHeight());*/
    }

    public static String getFileName(int i) {
        String fileExtension = getFileExtension(CropPane.croppedFilesList.get(i));
        String fileFullName = CropPane.croppedFilesList.get(i).getName();
        String fileName = fileFullName.substring(0, fileFullName.length() - 4) + "Cropped";
        File imgFile = new File(Launcher.outputPath + "\\" + fileName + i + "." + fileExtension);
        return imgFile.getName();
    }

    public static void clearList() {
        if (CropPane.croppedFilesList != null && CropPane.wiList != null && CropPane.CroppedimageListView != null) {
            CropPane.croppedFilesList.clear();
            CropPane.wiList.clear();
            CropPane.CroppedimageListView.getItems().clear();
            CropPane.previewImgView.setImage(CropPane.blankImage);
        }
    }

    public static void viewFullSize(Image mainImage) {
        if(mainImage==null){
            throw new NullPointerException("mainImage is null");
        }
        CropPane.mainImageView.setPreserveRatio(false);
        CropPane.mainImageView.setFitHeight(mainImage.getHeight());
        CropPane.mainImageView.setFitWidth(mainImage.getWidth());
        Launcher.primaryStage.setWidth(CropPane.scrollPane.getPrefViewportWidth() + 600 + 70);
        Launcher.primaryStage.setHeight(CropPane.scrollPane.getPrefViewportHeight() + 400);
       // Launcher.primaryStage.sizeToScene();
      Launcher.primaryStage.setMaximized(false);
//        Launcher.primaryStage.setFullScreen(true);*/
       // Launcher.primaryStage.setMaximized(true);
        centerStage(Launcher.primaryStage);
    }


    public static void zoomIn(ScrollPane scrollPane, Label zoomPercentLbl,Group group) {
        if(zoomFactor>3)
            return;
        double zoomIncrement = 1.1;
        zoomFactor = zoomFactor * zoomIncrement;
        zoom(scrollPane, zoomPercentLbl,group );

    }


    public static void zoomOut(ScrollPane scrollPane, Label zoomPercentLbl, Group group, CropBoxConfigPane configPane) {
        ImageView imageView=(ImageView) (group.getChildren().getFirst());
        if(zoomFactor<0.5 && imageView.getImage().getWidth()<1800 && imageView.getImage().getHeight()<1000)
            return;
        double zoomIncrement = 1.1;
        zoomFactor = zoomFactor / zoomIncrement;
        zoom(scrollPane, zoomPercentLbl, group);
        CropPane.setObPropertyForImageView(CropPane.mainImageView);
        ResizableRectangle rectangle=CropPane.areaSelection.getSelectionRectangle();

        while (isOutOfBound(rectangle,imageView) )
        {
           if(rectangle.getWidth()<=0 || rectangle.getHeight()<=0){
               //rectangle.setX(imageView.getBoundsInLocal().getWidth()-41);
               CropBoxConfigPane.croppingOnOff.setSelected(false);
               break;
           }
            rectangle.setWidth(rectangle.getWidth()-1);
            if(rectangle instanceof ResizableRectangleWithRatio1){
                rectangle.setHeight(rectangle.getHeight()-(1/((ResizableRectangleWithRatio1) rectangle).aspect_ratio));
            }
            else{
                double aspect_ratio=rectangle.getWidth()/rectangle.getHeight();
            rectangle.setHeight(rectangle.getHeight()-(1/aspect_ratio));}
        }
        configPane.refresh(rectangle);


    }

    private static void zoom(ScrollPane scrollPane, Label zoomPercentLbl, Group group) {
        var x = scrollPane.getHvalue();
        var y = scrollPane.getVvalue();
        CropPane.mainImageView.setFitWidth(CropPane.mainImageView.getImage().getWidth() * zoomFactor);
        CropPane.mainImageView.setFitHeight(CropPane.mainImageView.getImage().getHeight() * zoomFactor);
        scrollPane.setHvalue(x);
        scrollPane.setVvalue(y);
        zoomPercentLbl.setText((int) (CropPane.mainImageView.getFitWidth() / CropPane.mainImageView.getImage().getWidth() * 100) + "%");
    }

    public static boolean isOutOfBound(ResizableRectangle rectangle,ImageView imageView) {
        if(rectangle==null)
            return false;
        if(rectangle.getWidth()+rectangle.getX()>=imageView.getBoundsInParent().getWidth()) {
            return true;
        }
        if(rectangle.getHeight()+rectangle.getY()>=imageView.getBoundsInParent().getHeight()) {
            return true;
        }
        return false;
    }

    public static void nextImageBtn(ImageView mainImageView, Image mainImage,Label zoomLbl) {
        if(Launcher.imageFiles.size()==1){
            return;
        }
        zoomFactor = 1;
        zoomLbl.setText("100%");

        if (CropPane.imageIndex >= Launcher.imageFiles.size() - 1) {
            CropPane.imageIndex = -1;

        }
        CropPane.imageIndex++;
        try {
            mainImage = CropPaneController.convertFileToImage(Launcher.imageFiles.get(CropPane.imageIndex));
            mainImageView.setImage(mainImage);
            viewFullSize(mainImage);
        }
        catch (IOException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
       CropBoxConfigPane.croppingOnOff.setSelected(false);


    }

    public static void previousImageBtn(ImageView mainImageView, Image mainImage,Label zoomLbl) {
        if(Launcher.imageFiles.size()==1){
            return;
        }
            zoomFactor = 1;
            zoomLbl.setText("100%");
            if (CropPane.imageIndex <=0) {
                CropPane.imageIndex = Launcher.imageFiles.size() ;

            }
        CropPane.imageIndex--;
        try{
            mainImage = CropPaneController.convertFileToImage(Launcher.imageFiles.get(CropPane.imageIndex));
            mainImageView.setImage(mainImage);
            viewFullSize(mainImage);
        }
        catch (IOException | NullPointerException e){
           System.err.println(e.getMessage());
        }
        CropBoxConfigPane.croppingOnOff.setSelected(false);



    }


    public static void cropOptionOnAction(int selectedIndex, AreaSelection selectedArea, Group group, CropBoxConfigPane cropBoxConfigPane) {
        if (selectedIndex == 0) {
            selectedArea.setMode("Free");
        } else if (selectedIndex == 1) {
            selectedArea.setMode("16/9");
        } else if (selectedIndex == 2) {
            selectedArea.setMode("4/3");
        } else if (selectedIndex == 3) {
            selectedArea.setMode("1/1");

        } else if (selectedIndex == 4) {
            selectedArea.setMode("3/2");
        } else {
            selectedArea.setMode("5/4");
        }
        clearSelection(group);
        selectedArea.selectArea(group);
        cropBoxConfigPane.refresh(selectedArea.getSelectionRectangle());


    }

    public static Alert errorBox() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText("Output Path Not Specify!");
        return alert;

    }

    public static void centerStage(Stage stage) {
        // Get the primary screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Calculate the new position of the stage
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Set the stage to the center of the screen and ensure it's within the screen bounds
        stage.setX(Math.max((screenBounds.getWidth() - stageWidth) / 2, screenBounds.getMinX()));
      /*  stage.setY(Math.max((screenBounds.getHeight() - stageHeight) / 2, screenBounds.getMinY()));*/
        stage.setY(10);
    }
}
