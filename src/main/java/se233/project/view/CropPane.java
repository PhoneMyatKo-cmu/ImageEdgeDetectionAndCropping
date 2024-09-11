package se233.project.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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


    public CropPane() {

        final ScrollPane rootPane = new ScrollPane();
        BorderPane secondLevelpane=new BorderPane();
        secondLevelpane.setPadding(new Insets(20,20,20,20));

        rootPane.setPannable(true);
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
        mainImageView.setFitHeight(600);
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
        final MenuItem clearSelectionItem = new MenuItem("Clear Selection");

        menu2.getItems().add(select);
        menu2.getItems().add(crop);
        menu2.getItems().add(clearSelectionItem);
        menu2.getItems().add(save);

        select.setOnAction(event -> {areaSelection.selectArea(selectionGroup);
            rootPane.setPannable(false);});
        selectionBtn.setOnAction(e->{areaSelection.selectArea(selectionGroup);rootPane.setPannable(false);});
        clearSelectionBtn.setOnAction(e->CropPaneController.clearSelection(selectionGroup));
        cropBtn.setOnAction(e->{
            if(isAreaSelected){
                CropPaneController.cropImage(areaSelection.selectArea(selectionGroup).getBoundsInParent(),mainImageView);
            }
        });
        nextImageBtn.setOnAction(e->{

            if (imageIndex >= Launcher.imageFiles.size() - 1) {
                imageIndex = -1;
            } if(imageIndex<Launcher.imageFiles.size()) {
                imageIndex++;
                mainImageView.setImage(CropPaneController.convertFileToImage(Launcher.imageFiles.get(imageIndex)));
            }

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

        clearSelectionItem.setOnAction(event -> CropPaneController.clearSelection(selectionGroup));

        selectionGroup.getChildren().add(mainImageView);
        secondLevelpane.setTop(controlBar);
        secondLevelpane.setLeft(rootPane);
        secondLevelpane.setRight(previewPane());

        rootPane.setContent(selectionGroup);
        this.setCenter(secondLevelpane);
        menuBar.getMenus().addAll(menu1, menu2);
        this.setTop(menuBar);
        this.setBottom(new HBox());

    }

   public VBox previewPane(){
        VBox containerPane=new VBox();
        containerPane.setAlignment(Pos.CENTER);
        Label pLabel=new Label("Preview Image");
         previewImgView=new ImageView();
         previewImgView.setFitWidth(300);
         previewImgView.setFitHeight(300);
         containerPane.setSpacing(10);
         containerPane.setPadding(new Insets(10,10,10,10));
         containerPane.getChildren().add(previewImgView);
         containerPane.getChildren().add(pLabel);
         Image pImage=CropPaneController.convertFileToImage(Launcher.imageFiles.get(0));
         previewImgView.setImage(pImage);
      /*  ListView<ImageView> imageListView=new ListView<>();
        imageListView.setItems(previewImgList);*/

        return containerPane ;

   }

   public static void refreshPreview(Image image){
        previewImgView.setImage(image);
       System.out.println("called"+image);

   }




}
