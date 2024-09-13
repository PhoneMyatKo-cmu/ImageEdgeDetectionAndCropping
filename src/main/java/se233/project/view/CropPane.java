package se233.project.view;


import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import se233.project.Launcher;
import se233.project.controller.viewcontrollers.CropPaneController;
import se233.project.model.AreaSelection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CropPane extends BorderPane {

    public static ImageView mainImageView;
    private Image mainImage;
    public static boolean isAreaSelected = false;
    public static List<WritableImage> wiList = new ArrayList<>();
    public static int imageIndex = 0;
    public static ObservableList<ImageView> previewImgList= FXCollections.observableArrayList();
    public static List<File> croppedFilesList=new ArrayList<>();
    public static ImageView previewImgView;
    public static CroppedListView CroppedimageListView=new CroppedListView();
    public static DoubleProperty displayWidth=new SimpleDoubleProperty();
    public static DoubleProperty displayHeight=new SimpleDoubleProperty();


    public CropPane() {



        final BorderPane rootPane = new BorderPane();
        //  rootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        BorderPane secondLevelpane=new BorderPane();
        secondLevelpane.setPadding(new Insets(20,20,20,20));

     //   rootPane.setPannable(true);
       /* rootPane.setPrefViewportHeight(600);
        rootPane.setPrefViewportWidth(800);*/

        HBox controlBar=new HBox(20);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.setPadding(new Insets(20,20,20,20));
       /* ComboBox comboBox=new ComboBox();
        comboBox.setPromptText("Choose ratio");*/
        //comboBox.getItems().add("4:3");
        Button selectionBtn=new Button(("Start Selection"));
        Button clearSelectionBtn=new Button("Clear Selection");
        Button nextImageBtn=new Button("Next Image");
        Button cropBtn=new Button("Crop");
        controlBar.getChildren().addAll(selectionBtn,clearSelectionBtn,nextImageBtn,cropBtn);
        //this.setLeft(controlBar);






        mainImageView = new ImageView();
        mainImage = CropPaneController.convertFileToImage(Launcher.imageFiles.get(0));
        mainImageView.setImage(mainImage);
        mainImageView.setFitWidth(800);
        mainImageView.setFitHeight(400);
        mainImageView.setPreserveRatio(true);
        CropPaneController.changeImageViewSize(mainImage,mainImageView);


        final AreaSelection areaSelection = new AreaSelection(mainImageView, mainImage);
        final Group selectionGroup = new Group();
        final MenuBar menuBar = new MenuBar();


        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Options");

        final MenuItem open = new MenuItem("Open");
        final MenuItem clear = new MenuItem("Clear");



        menu1.getItems().addAll(open, clear);

        clear.setOnAction(event -> {
            CropPaneController.clearSelection(selectionGroup);
            mainImageView.setImage(null);
            System.gc();
        });

        MenuItem save = new Menu("Save");
        final MenuItem select = new MenuItem("Select Area");
        final MenuItem crop = new MenuItem("Crop");
        final MenuItem backToBtn = new MenuItem("Back");


        menu2.getItems().add(select);
        menu2.getItems().add(crop);
        menu2.getItems().add(backToBtn);
        menu2.getItems().add(save);

        select.setOnAction(event -> {areaSelection.selectArea(selectionGroup);
           /* rootPane.setPannable(false);*/});

        selectionBtn.setOnAction(e->{areaSelection.selectArea(selectionGroup);});

        clearSelectionBtn.setOnAction(event-> CropPaneController.clearSelection(selectionGroup));



        cropBtn.setOnAction(e->{
            if(isAreaSelected){
                CropPaneController.cropImage(areaSelection.selectArea(selectionGroup).getBoundsInParent(),mainImageView);
            }
        });

        nextImageBtn.setOnAction(e->{

            mainImage=CropPaneController.convertFileToImage(Launcher.imageFiles.get(imageIndex));

            if (imageIndex >= Launcher.imageFiles.size() - 1) {
                imageIndex = -1;
            } if(imageIndex<Launcher.imageFiles.size()) {
                imageIndex++;
                mainImageView.setImage(mainImage);
            }
            CropPaneController.changeImageViewSize(mainImage,mainImageView);

           Launcher.primaryStage.setMinWidth(mainImageView.getBoundsInParent().getWidth()+CroppedimageListView.getWidth()+50);
            System.out.println("MainImageView FitWidth after resize:"+mainImageView.getFitWidth());
            System.out.println("Next Image Index:"+imageIndex);
        });



        crop.setOnAction(event -> {
            if (isAreaSelected)
                CropPaneController.cropImage(areaSelection.getSelectionRectangle().getBoundsInParent(), mainImageView);
        });

        open.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image file");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg"));
            File selectedFile = fileChooser.showOpenDialog(Launcher.primaryStage);
            if (selectedFile != null) {
                CropPaneController.clearSelection(selectionGroup);
                mainImage = CropPaneController.convertFileToImage(selectedFile);
                mainImageView.setImage(mainImage);
                CropPaneController.changeStageSizeImageDimensions(Launcher.primaryStage, mainImage);
            }
        });



        save.setOnAction(e -> CropPaneController.saveCroppedImage(Launcher.primaryStage));

        backToBtn.setOnAction(event ->   {
            Launcher.primaryStage.setScene(new Scene(new InputPane("Crop")));
            System.out.println("BackBtn pressed.");
        });

        selectionGroup.getChildren().add(mainImageView);
        secondLevelpane.setTop(controlBar);
        secondLevelpane.setLeft(rootPane);
        secondLevelpane.setRight(previewPane());

        rootPane.setCenter(selectionGroup);
        this.setCenter(secondLevelpane);
        menuBar.getMenus().addAll(menu1, menu2);
        this.setTop(menuBar);
        this.setBottom(new HBox());
        mode();
        Platform.runLater(()-> setObPropertyForImageView(mainImageView));
        System.out.println("MainImageView FitWidth"+mainImageView.getFitWidth());

    }

   public VBox previewPane(){
        VBox containerPane=new VBox();
        containerPane.setAlignment(Pos.CENTER);
        Label pLabel=new Label("Preview Image");
         previewImgView=new ImageView();
         previewImgView.setFitWidth(300);
         previewImgView.setFitHeight(225);
         previewImgView.setPreserveRatio(true);
         containerPane.setSpacing(10);
         containerPane.setPadding(new Insets(10,10,10,10));
         containerPane.getChildren().add(previewImgView);
         containerPane.getChildren().add(pLabel);
         Image pImage=CropPaneController.convertFileToImage(Launcher.imageFiles.get(0));
         previewImgView.setImage(pImage);
         containerPane.getChildren().add(CroppedimageListView);
         HBox btnBox=new HBox(30);
         btnBox.setAlignment(Pos.CENTER);
         Button saveBtn=new Button("Save");
         Button clearBtn=new Button("Clear List");

         saveBtn.setOnAction(actionEvent -> CropPaneController.saveCroppedImage(Launcher.primaryStage));
         clearBtn.setOnAction(actionEvent -> CropPaneController.clearList());

         btnBox.getChildren().addAll(saveBtn,clearBtn);
         containerPane.getChildren().add(btnBox);
      /*  ListView<ImageView> imageListView=new ListView<>();
        imageListView.setItems(previewImgList);*/

        return containerPane ;

   }

   public static void refreshPreview(Image image){
        previewImgView.setImage(image);
       System.out.println("called"+image);


   }

   public  void mode(){
        this.setStyle("-fx-background-color: #F5F5F5;");

    //   Launcher.primaryStage.setMinWidth(mainImageView.getFitWidth()+previewImgView.getFitWidth()+CroppedimageListView.getWidth());


   }

   public void setObPropertyForImageView(ImageView imageView){
        imageView.getParent().layoutBoundsProperty().addListener((obs,old,newBound)->{
            displayWidth.set(imageView.getBoundsInParent().getWidth());
            displayHeight.set(imageView.getBoundsInParent().getHeight());
        });
       System.out.println("Ob width:"+displayWidth);
   }




}
