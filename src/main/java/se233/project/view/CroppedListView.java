package se233.project.view;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CroppedListView extends ListView<HBox> {
    HBox hBox;
    Label imageName;
    ProgressIndicator progressIndicator;
    public CroppedListView(){
        this.setPrefHeight(300);

    }

    public void modifyListCell(int i,Task task){
        ProgressIndicator progressIndicator1=(ProgressIndicator) getItems().get(i).getChildren().get(1);
        progressIndicator1.progressProperty().bind(task.progressProperty());
        progressIndicator1.setVisible(true);

       // onClick();

        //getItems().add(hBox);
    }

    public void onClick(){
        for(int i=0;i<getItems().size();i++){
            getItems().get(i).setOnMouseClicked(event -> {
                int selectedIndex= getSelectionModel().getSelectedIndex();
                CropPane.previewImgView.setImage(CropPane.wiList.get(selectedIndex));
                System.out.println("Click on list cell index:"+selectedIndex);

            });
        }


    }
    class CustomListCell extends ListCell<HBox> {
        HBox hBox = new HBox();
        Label label =new Label();
        ProgressIndicator progressIndicator=new ProgressIndicator() ;

        public CustomListCell() {
            // Spacing between Label and ProgressIndicator

            // Add event handler for cell click
            setOnMouseClicked(event -> {
                if (!isEmpty()) {
                    HBox item = getItem();
                   int selectedIndex= this.getListView().getSelectionModel().getSelectedIndex();
                   CropPane.previewImgView.setImage(CropPane.wiList.get(selectedIndex));
                    System.out.println("Click on list cell index:"+selectedIndex);
                }
            });
        }

        @Override
        protected void updateItem(HBox item, boolean empty) {
            super.updateItem(item, empty);


        }
    }

    public void refreshList(){
        if(getItems().isEmpty()){

        }
    }

}
