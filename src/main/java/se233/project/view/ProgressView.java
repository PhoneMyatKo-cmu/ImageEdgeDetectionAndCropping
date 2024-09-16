package se233.project.view;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import se233.project.Launcher;

import java.io.File;

public class ProgressView extends ScrollPane {
    private GridPane containerPane = new GridPane();
    private int size = 0;

    public ProgressView() {
        this.setContent(containerPane);
        for (File file : Launcher.imageFiles) {
            this.add(file);
        }
    }

    public void add(File file) {
        Label fileName = new Label(file.getName());
        ProgressIndicator pi = new ProgressIndicator();
        pi.setVisible(false);
        containerPane.add(fileName, 0, size);
        containerPane.add(pi, 1, size);
        size++;
    }

    public ProgressIndicator get(int i) {
        return (ProgressIndicator) containerPane.getChildren().get(2 * i + 1);
    }
}
