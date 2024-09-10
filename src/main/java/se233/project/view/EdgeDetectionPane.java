package se233.project.view;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import se233.project.Launcher;

public class EdgeDetectionPane extends BorderPane {
    private EDSettingArea settingArea;
    private EDImageDisplayArea imageDisplayArea;

    public EdgeDetectionPane() {
        imageDisplayArea = new EDImageDisplayArea();
        this.setCenter(imageDisplayArea);
        settingArea = new EDSettingArea(imageDisplayArea);
        this.setLeft(settingArea);
        Launcher.primaryStage.setMaximized(true);
        imageDisplayArea.loadInputImages();
    }
}
