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
import javafx.scene.layout.*;
import se233.project.Launcher;
import se233.project.controller.viewcontrollers.CropPaneController;
import se233.project.model.AreaSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CropPane extends BorderPane {

    public static ImageView mainImageView;
    public static boolean isAreaSelected = false;
    public static List<WritableImage> wiList = new ArrayList<>();
    public static int imageIndex = 0;
    public static ObservableList<ImageView> previewImgList = FXCollections.observableArrayList();
    public static List<File> croppedFilesList = new ArrayList<>();
    public static ImageView previewImgView;
    public static CroppedListView CroppedimageListView = new CroppedListView();
    public static DoubleProperty displayWidth = new SimpleDoubleProperty();
    public static DoubleProperty displayHeight = new SimpleDoubleProperty();
    public static BorderPane secondLevelpane;
    public static ScrollPane scrollPane;
    public static Group selectionGroup;
    public static Image blankImage;
    public static CropBoxConfigPane cropBoxConfigPane;
    private Image mainImage;
    public static AreaSelection areaSelection;


    public CropPane() {



        secondLevelpane = new BorderPane();
        secondLevelpane.setPadding(new Insets(20, 20, 20, 20));
        blankImage = new Image(Objects.requireNonNull(Launcher.class.getResource("blank.png")).toString());


        /*Control Bar Group*/
        HBox controlBar = new HBox(20);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.setPadding(new Insets(20, 20, 20, 20));

        Button selectionBtn = new Button(("Start Selection"));
        Button clearSelectionBtn = new Button("Clear Selection");
        Button nextImageBtn = new Button("Next Image");
        Button previousImageBtn = new Button("Previous Image");
        Button cropBtn = new Button("Crop");
        Button zoomInBtn = new Button("Zoom In");
        Button zoomOutBtn = new Button("Zoom Out");
        zoomInBtn.setVisible(false);
        zoomOutBtn.setVisible(false);
        Label zoomPercentLbl = new Label(100 + "%");


        /*Image Instantiate*/
        mainImageView = new ImageView();
        try {
            mainImage = CropPaneController.convertFileToImage(Launcher.imageFiles.getFirst());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainImageView.setImage(mainImage);
        mainImageView.setFitWidth(800);
        mainImageView.setFitHeight(400);
        mainImageView.setPreserveRatio(false);
        CropPaneController.changeImageViewSize(mainImage, mainImageView);


        /*Crop Box Start*/
        areaSelection = new AreaSelection(mainImageView, mainImage);
        selectionGroup = new Group();
        ComboBox<String> cropOptionBox = cropBox(areaSelection, selectionGroup);
        final MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("File");
        final MenuItem backToBtn = new MenuItem("Back");
        menu1.getItems().add(backToBtn);
        backToBtn.setOnAction(event -> {
            Launcher.primaryStage.setScene(new Scene(new InputPane("Crop")));
            Launcher.primaryStage.setMaximized(false);
            Launcher.primaryStage.setWidth(585);
            Launcher.primaryStage.setHeight(500);
            CropPane.wiList.clear();
            CropPane.CroppedimageListView.getItems().clear();
            CropPane.croppedFilesList.clear();
            CropPaneController.centerStage(Launcher.primaryStage);
        });


        selectionBtn.setOnAction(e -> {
            areaSelection.selectArea(selectionGroup);
            cropBoxConfigPane.refresh(areaSelection.getSelectionRectangle());
        });

        clearSelectionBtn.setOnAction(event -> {
            CropPaneController.clearSelection(selectionGroup);
            cropBoxConfigPane.refresh(null);
        });


        cropBtn.setOnAction(e -> {
            if (isAreaSelected) {
                CropPaneController.cropImage(areaSelection.getSelectionRectangle().getBoundsInParent(), mainImageView);
            }
        });

        nextImageBtn.setOnAction(e -> {
            CropPaneController.nextImageBtn(mainImageView, mainImage);
            zoomPercentLbl.setText(100 + "%");
        });

        previousImageBtn.setOnAction(e -> {
            CropPaneController.previousImageBtn(mainImageView, mainImage);
            zoomPercentLbl.setText(100 + "%");
        });

        zoomInBtn.setOnAction(event -> CropPaneController.zoomIn(scrollPane, zoomPercentLbl,selectionGroup));

        zoomOutBtn.setOnAction(event -> CropPaneController.zoomOut(scrollPane, zoomPercentLbl,selectionGroup,cropBoxConfigPane ));

        selectionGroup.getChildren().add(mainImageView);
        cropBoxConfigPane = new CropBoxConfigPane(areaSelection.getSelectionRectangle(), cropOptionBox,selectionGroup);
        StackPane containerForImageView = new StackPane();
        containerForImageView.getChildren().addAll(selectionGroup);
        scrollPane = changeScrollPane(containerForImageView);
        secondLevelpane.setTop(controlBar);
        secondLevelpane.setLeft(cropBoxConfigPane);
        secondLevelpane.setCenter(scrollPane);
        secondLevelpane.setRight(previewPane());
/*selectionBtn, clearSelectionBtn,cropBtn removed*/
        controlBar.getChildren().addAll( previousImageBtn, zoomInBtn,zoomPercentLbl, zoomOutBtn,nextImageBtn );
        this.setCenter(secondLevelpane);
        menuBar.getMenus().addAll(menu1);
        this.setTop(menuBar);
        this.setBottom(new HBox());
        mode();
        zoomInBtn.setVisible(true);
        zoomOutBtn.setVisible(true);
        CropPaneController.viewFullSize(mainImage);
        CropPaneController.centerStage(Launcher.primaryStage);
        setObPropertyForImageView(mainImageView);

    }

    public static void refreshPreview(Image image) {
        previewImgView.setImage(image);
        System.out.println("called" + image);


    }

    public static ScrollPane changeScrollPane(StackPane selectionGroup) {
        ScrollPane scrollPane = new ScrollPane(selectionGroup);
        scrollPane.setPrefViewportWidth(800);
        scrollPane.setPrefViewportHeight(400);
        scrollPane.setPannable(false);
        selectionGroup.setMinWidth(scrollPane.getPrefViewportWidth());
        selectionGroup.setMinHeight(scrollPane.getPrefViewportHeight());
        return scrollPane;
    }

    public VBox previewPane() {
        VBox containerPane = new VBox();
        containerPane.setPrefWidth(400);
        containerPane.setAlignment(Pos.CENTER);

        VBox previewImagePane= new VBox();
        previewImagePane.setPrefWidth(380);
        previewImagePane.setPrefHeight(260);
        previewImagePane.setAlignment(Pos.CENTER);
        previewImagePane.setSpacing(10);
        previewImagePane.setPadding(new Insets(10,10,10,10));
        previewImagePane.setStyle("-fx-border-color: black;-fx-border-width: 1");
        Label pLabel = new Label("Preview Image");
        previewImgView = new ImageView();
        previewImgView.setFitWidth(300);
        previewImgView.setFitHeight(225);
        previewImgView.setPreserveRatio(true);
        previewImagePane.getChildren().add(previewImgView);
        previewImagePane.getChildren().add(pLabel);
        containerPane.setSpacing(10);
        containerPane.setPadding(new Insets(10, 10, 10, 10));
        containerPane.getChildren().add(previewImagePane);
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

    public void mode() {
        this.setStyle("-fx-background-color: #F5F5F5;");
        secondLevelpane.getLeft().setStyle("-fx-border-color: black;-fx-border-width: 1");
        scrollPane.setStyle("-fx-background-color:#0b0707;");
    }

    public static void setObPropertyForImageView(ImageView imageView) {
        displayWidth.set(imageView.getBoundsInLocal().getWidth());
        displayHeight.set(imageView.getBoundsInLocal().getHeight());
        imageView.getParent().layoutBoundsProperty().addListener((obs, old, newBound) -> {
            displayWidth.set(imageView.getBoundsInLocal().getWidth());
            displayHeight.set(imageView.getBoundsInLocal().getHeight());
        });
        System.out.println("Image bound:"+imageView.getBoundsInLocal().getWidth());
        System.out.println("Ob width:" + displayWidth);
    }



    public ComboBox<String> cropBox(AreaSelection selectedArea, Group group) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Crop Box Options");
        comboBox.getItems().add("Free");
        comboBox.getItems().add("16:9");
        comboBox.getItems().add("4:3");
        comboBox.getItems().add("1:1");
        comboBox.getItems().add("3:2");
        comboBox.getItems().add("5:4");

        comboBox.setOnAction(event -> CropPaneController.cropOptionOnAction(comboBox.getSelectionModel().getSelectedIndex(), selectedArea, group,cropBoxConfigPane));
        return comboBox;
    }


}
