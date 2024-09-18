package se233.project.view;


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
import javafx.scene.layout.StackPane;
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
    public static ObservableList<ImageView> previewImgList = FXCollections.observableArrayList();
    public static List<File> croppedFilesList = new ArrayList<>();
    public static ImageView previewImgView;
    public static CroppedListView CroppedimageListView = new CroppedListView();
    public static DoubleProperty displayWidth = new SimpleDoubleProperty();
    public static DoubleProperty displayHeight = new SimpleDoubleProperty();
    /*Test as ScrollPane*/
    public static BorderPane secondLevelpane;
    //  public static BorderPane rootPane;
    public static ScrollPane scrollPane;
    public static Group selectionGroup;
    public static Image blankImage;
    public static CropBoxConfigPane cropBoxConfigPane;


    public CropPane() {


        // rootPane = new BorderPane();
        secondLevelpane = new BorderPane();
        secondLevelpane.setPadding(new Insets(20, 20, 20, 20));
        blankImage=new Image(Launcher.class.getResource("blank.png").toString());
        // Launcher.primaryStage.setMaximized(true);

        /*Control Bar Group*/
        HBox controlBar = new HBox(20);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.setPadding(new Insets(20, 20, 20, 20));

        Button selectionBtn = new Button(("Start Selection"));
        Button clearSelectionBtn = new Button("Clear Selection");
        Button nextImageBtn = new Button("Next Image");
        Button cropBtn = new Button("Crop");
        Button zoomInBtn = new Button("Zoom In");
        Button zoomOutBtn = new Button("Zoom Out");
        zoomInBtn.setVisible(false);
        zoomOutBtn.setVisible(false);
        Label zoomPercentLbl=new Label(100+"%");


        /*Image Instatiate*/
        mainImageView = new ImageView();
        mainImage = CropPaneController.convertFileToImage(Launcher.imageFiles.get(0));

        mainImageView.setImage(mainImage);
        mainImageView.setFitWidth(800);
        mainImageView.setFitHeight(400);
        mainImageView.setPreserveRatio(false);
        CropPaneController.changeImageViewSize(mainImage, mainImageView);


        /*Crop Box Start*/
        final AreaSelection areaSelection = new AreaSelection(mainImageView, mainImage);
        selectionGroup = new Group();
        ComboBox cropOptionBox=cropBox(areaSelection,selectionGroup);
        //controlBar.getChildren().add(cropOptionBox);
        /*Menu Bar*/
        final MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Options");

        final MenuItem open = new MenuItem("Open");
        final MenuItem clear = new MenuItem("Clear");
        final MenuItem viewFull = new Menu("View Full Size");
        final MenuItem viewFixed = new Menu("View Fixed Size");

        menu1.getItems().addAll(open, clear, viewFull, viewFixed);
        clear.setOnAction(event -> {
            CropPaneController.clearSelection(selectionGroup);
            mainImageView.setImage(null);
            System.gc();
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



        final MenuItem backToBtn = new MenuItem("Back");
        menu2.getItems().add(backToBtn);
        backToBtn.setOnAction(event -> {
            Launcher.primaryStage.setScene(new Scene(new InputPane("Crop")));
            CropPane.wiList.clear();
            CropPane.CroppedimageListView.getItems().clear();
            CropPane.croppedFilesList.clear();
            System.out.println("BackBtn pressed.");
        });


        selectionBtn.setOnAction(e -> {areaSelection.selectArea(selectionGroup);cropBoxConfigPane.refresh(areaSelection.getSelectionRectangle());});

        clearSelectionBtn.setOnAction(event -> {CropPaneController.clearSelection(selectionGroup);cropBoxConfigPane.refresh(null);});


        cropBtn.setOnAction(e -> {
            if (isAreaSelected) {
                CropPaneController.cropImage(areaSelection.getSelectionRectangle().getBoundsInParent(), mainImageView);
            }
        });

        nextImageBtn.setOnAction(e -> {
            CropPaneController.nextImageBtn(mainImageView, mainImage);
            zoomPercentLbl.setText(100+"%");
            //CropPaneController.viewFullSize(mainImage);
        });

        zoomInBtn.setOnAction(event -> CropPaneController.zoomIn(scrollPane,zoomPercentLbl));
        zoomOutBtn.setOnAction(event -> CropPaneController.zoomOut(scrollPane,zoomPercentLbl));
        cropBoxConfigPane=new CropBoxConfigPane(areaSelection.getSelectionRectangle());
        selectionGroup.getChildren().add(mainImageView);
        StackPane containerForImageView=new StackPane(selectionGroup);
        scrollPane = changeScrollPane(containerForImageView);
        //CropPaneController.viewFullSize(mainImage);
        secondLevelpane.setTop(controlBar);
        secondLevelpane.setLeft(cropBoxConfigPane);
        secondLevelpane.setCenter(scrollPane);
        secondLevelpane.setRight(previewPane());
        controlBar.getChildren().addAll(cropOptionBox,selectionBtn, clearSelectionBtn, nextImageBtn, cropBtn, zoomInBtn, zoomOutBtn,zoomPercentLbl);

        /*Test as scroll pane*/
        // rootPane.setCenter(selectionGroup);

        this.setCenter(secondLevelpane);
        menuBar.getMenus().addAll(menu1, menu2);
        this.setTop(menuBar);
        this.setBottom(new HBox());
        mode();
        zoomInBtn.setVisible(true);
        zoomOutBtn.setVisible(true);
        CropPaneController.viewFullSize(mainImage);

    }

    public VBox previewPane() {
        VBox containerPane = new VBox();
        containerPane.setPrefWidth(400);
        containerPane.setAlignment(Pos.CENTER);
        Label pLabel = new Label("Preview Image");
        previewImgView = new ImageView();
        previewImgView.setFitWidth(300);
        previewImgView.setFitHeight(225);
        previewImgView.setPreserveRatio(true);
        containerPane.setSpacing(10);
        containerPane.setPadding(new Insets(10, 10, 10, 10));
        containerPane.getChildren().add(previewImgView);
        containerPane.getChildren().add(pLabel);
        Image pImage = CropPaneController.convertFileToImage(Launcher.imageFiles.get(0));
        previewImgView.setImage(blankImage);
        containerPane.getChildren().add(CroppedimageListView);
        HBox btnBox = new HBox(30);
        btnBox.setAlignment(Pos.CENTER);
        Button saveBtn = new Button("Save");
        Button clearBtn = new Button("Clear List");

        saveBtn.setOnAction(actionEvent -> CropPaneController.saveCroppedImage());
        clearBtn.setOnAction(actionEvent -> CropPaneController.clearList());
        
        btnBox.getChildren().addAll(saveBtn, clearBtn);
        containerPane.getChildren().add(btnBox);
        return containerPane;
    }

    public static void refreshPreview(Image image) {
        previewImgView.setImage(image);
        System.out.println("called" + image);


    }

    public void mode() {
        this.setStyle("-fx-background-color: #F5F5F5;");
        secondLevelpane.getLeft().setStyle("-fx-border-color: black;-fx-border-width: 1");
        // rootPane.setPadding(new Insets(20,20,20,20));


        //   Launcher.primaryStage.setMinWidth(mainImageView.getFitWidth()+previewImgView.getFitWidth()+CroppedimageListView.getWidth());


    }

    public void setObPropertyForImageView(ImageView imageView) {
        imageView.getParent().layoutBoundsProperty().addListener((obs, old, newBound) -> {
            displayWidth.set(imageView.getBoundsInParent().getWidth());
            displayHeight.set(imageView.getBoundsInParent().getHeight());
        });
        System.out.println("Ob width:" + displayWidth);
    }


    public static ScrollPane changeScrollPane(StackPane selectionGroup) {
        ScrollPane scrollPane = new ScrollPane(selectionGroup);
        scrollPane.setPrefViewportWidth(800);
        scrollPane.setPrefViewportHeight(400);
        scrollPane.setPannable(false);
        selectionGroup.setMinWidth(scrollPane.getPrefViewportWidth());
        selectionGroup.setMinHeight(scrollPane.getPrefViewportHeight());
        //CropPaneController.viewFullSize(mainImage);

        return scrollPane;
    }

    public ComboBox cropBox(AreaSelection selectedArea,Group group){
        ComboBox comboBox=new ComboBox();
        comboBox.setPromptText("Crop Box Options");
        comboBox.getItems().add("Free");
        comboBox.getItems().add("16:9");
        comboBox.getItems().add("4:3");
        comboBox.getItems().add("1:1");
        comboBox.getItems().add("3:2");
        comboBox.getItems().add("5:4");

        comboBox.setOnAction(event->{
            CropPaneController.cropOptionOnAction(comboBox.getSelectionModel().getSelectedIndex(),selectedArea,group);
        });
        return comboBox;
    }


}
