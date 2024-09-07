package se233.project.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import se233.project.Launcher;

public class MainMenu extends VBox {
    private Button edgeDetectBtn = new Button("Edge Detector");
    private Button cropBtn = new Button("Crop");

    public MainMenu() {
        super(25);
        edgeDetectBtn.setPrefSize(100, 30);
        cropBtn.setPrefSize(100, 30);
        this.getChildren().addAll(edgeDetectBtn, cropBtn);
        this.setPadding(new Insets(100, 200, 100, 200));

        edgeDetectBtn.setOnAction(e -> {
            Launcher.primaryStage.setScene(new Scene(new InputPane("EdgeDetection")));
        });

        cropBtn.setOnAction(e -> {
            Launcher.primaryStage.setScene(new Scene(new InputPane("Crop")));
        });
    }
}
