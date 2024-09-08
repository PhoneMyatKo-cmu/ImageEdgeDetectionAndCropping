package se233.project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import se233.project.controller.viewcontrollers.InputPaneController;

public class InputPane extends VBox {

    HBox inputBox = new HBox(25);
    FileChooser inputFileChooser = new FileChooser();
    DirectoryChooser outputDirectoryChooser = new DirectoryChooser();
    Button inputBtn = new Button("Choose Image");
    Button outputBtn = new Button("Choose Output Directory");
    TextField outputField = new TextField();
    ListView<String> imageList = new ListView<>();
    Label dropLabel = new Label("Drop Image Here");
    VBox dropLabelContainer = new VBox(dropLabel);
    ScrollPane dropPane = new ScrollPane(new VBox(dropLabelContainer));
    Button continueBtn = new Button("Continue");
    Button backBtn = new Button("Back");
    String mode;

    public InputPane(String mode) {
        super(20);
        this.mode = mode;
        this.setPadding(new Insets(30, 20, 30, 20));
        dropLabelContainer.setPrefSize(400, 300);
        dropLabelContainer.setAlignment(Pos.CENTER);
        dropPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        dropPane.setPrefHeight(300);
        dropPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dropPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        inputBtn.setPrefSize(100, 30);
        inputFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.zip"));
        outputBtn.setPrefSize(200, 30);
        outputField.setPrefSize(300, 30);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(inputBtn, dropPane);
        this.getChildren().addAll(inputBox, new HBox(outputBtn, outputField), new HBox(20, backBtn, continueBtn));

        inputBtn.setOnAction(e -> {
            InputPaneController.setOnInputBtn(inputFileChooser, imageList);
            InputPaneController.updateImageListView(imageList, dropPane);
        });

        outputBtn.setOnAction(e -> InputPaneController.setOnOutputBtn(outputDirectoryChooser, outputField));

        continueBtn.setOnAction(e -> InputPaneController.setOnContinueBtn(mode, outputField));

        backBtn.setOnAction(e -> InputPaneController.setOnBackBtn());

        dropPane.setOnDragOver(InputPaneController::setOnDragOver);

        dropPane.setOnDragDropped(e -> {
            InputPaneController.setOnDragDropped(e, imageList);
            InputPaneController.updateImageListView(imageList, dropPane);
        });
    }
}
