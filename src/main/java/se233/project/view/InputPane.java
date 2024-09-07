package se233.project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    ListView<String> imageList = new ListView<String>();
    Label dropLabel = new Label("Drop Image Here");
    VBox dropLabelContainer = new VBox(dropLabel);
    AnchorPane dropPane = new AnchorPane(new VBox(dropLabelContainer));
    Button continueBtn = new Button("Continue");
    Button backBtn = new Button("Back");
    String mode;

    public InputPane(String mode) {
        super(20);
        this.mode = mode;
        this.setPadding(new Insets(10, 20, 50, 20));
        dropLabelContainer.setPrefSize(400, 300);
        dropLabelContainer.setAlignment(Pos.CENTER);
        dropPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        inputBtn.setPrefSize(100, 30);
        outputBtn.setPrefSize(300, 30);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(inputBtn, dropPane);
        this.getChildren().addAll(inputBox, outputBtn, new HBox(20, backBtn, continueBtn));

        inputBtn.setOnAction(e -> InputPaneController.setOnInputBtn(inputFileChooser));

        outputBtn.setOnAction(e -> InputPaneController.setOnOutputBtn(outputDirectoryChooser));

        continueBtn.setOnAction(e -> InputPaneController.setOnContinueBtn(mode));

        backBtn.setOnAction(e -> InputPaneController.setOnBackBtn());

        dropPane.setOnDragOver(e -> InputPaneController.setOnDragOver(e));

        dropPane.setOnDragDropped(e -> {
            InputPaneController.setOnDragDropped(e, imageList);
            dropPane.getChildren().removeAll();
            dropPane.getChildren().add(imageList);
        });
    }
}
