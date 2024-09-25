package se233.project.view;

import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import se233.project.controller.CropSaveTask;

public class CroppedListView extends ListView<HBox> {

    public CroppedListView() {
        this.setPrefHeight(300);

    }

    public void modifyListCell(int i, CropSaveTask task) {
        ProgressIndicator progressIndicator1 = (ProgressIndicator) getItems().get(i).getChildren().get(1);
        progressIndicator1.progressProperty().bind(task.progressProperty());
        progressIndicator1.setVisible(true);

    }


}
