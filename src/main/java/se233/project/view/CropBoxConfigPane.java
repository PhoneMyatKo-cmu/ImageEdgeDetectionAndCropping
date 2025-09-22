package se233.project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se233.project.controller.viewcontrollers.CropPaneController;
import se233.project.model.ResizableRectangle;
import se233.project.model.ResizableRectangleWithRatio1;

public class CropBoxConfigPane extends VBox {
    ResizableRectangle rectangle;
   public static TextField widthField;
   public static TextField heightField;
    ComboBox<String> cropOptionBox;
    public static ToggleButton croppingOnOff;
    public Button cropButton;

    public CropBoxConfigPane(ResizableRectangle rectangle, ComboBox<String> cropOption, Group group) {
        super(30);
        this.rectangle = rectangle;
        setAlignment(Pos.CENTER);
        setWidth(200);
        widthField = new TextField();
        heightField = new TextField();
        croppingOnOff=new ToggleButton("Cropping Off");
        cropButton = new Button("Crop");


        Label titleLbl=new Label("Crop Settings");
        this.cropOptionBox=cropOption;
        HBox dimensionBox = new HBox(10);
        dimensionBox.setAlignment(Pos.CENTER);
        dimensionBox.getChildren().addAll(widthField, heightField);
        HBox lblBox = new HBox(15);
        lblBox.setAlignment(Pos.CENTER);
        Label widthLbl = new Label("Width");
        Label heightLbl = new Label("Height");
        lblBox.getChildren().addAll(widthLbl, heightLbl);
        Button applyBtn = new Button("Apply");


        this.getChildren().addAll(titleLbl,croppingOnOff,cropOptionBox,cropButton,lblBox, dimensionBox, applyBtn);
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setPrefHeight(400);
        configToggle(group,applyBtn);
        initialize(applyBtn);

    }

    public void refresh(ResizableRectangle rectangle) {
        if (rectangle == null) {
            widthField.setText("" + 0);
            heightField.setText("" + 0);
            return;
        } else heightField.setEditable(!(rectangle instanceof ResizableRectangleWithRatio1));
        if(rectangle instanceof ResizableRectangleWithRatio1)
            heightField.setStyle("-fx-background-color: darkgray");
        else
            heightField.setStyle(null);
        this.rectangle = rectangle;
        widthField.setText(String.format("%.2f", rectangle.getWidth()));
        heightField.setText(String.format("%.2f", rectangle.getHeight()));
    }

    public void configToggle(Group group,Button applyBtn) {
      croppingOnOff.selectedProperty().addListener((observable, oldValue, newValue) -> {
          if(croppingOnOff.isSelected()){
              croppingOnOff.setText("Cropping On");
              CropPane.areaSelection.selectArea(group);
              refresh(CropPane.areaSelection.getSelectionRectangle());

          }
          else{
              croppingOnOff.setText("Cropping Off");
              CropPaneController.clearSelection(group);
              refresh(null);
          }
          cropButton.setDisable(!croppingOnOff.isSelected());
          cropOptionBox.setDisable(!croppingOnOff.isSelected());
          widthField.setDisable(!croppingOnOff.isSelected());
          heightField.setDisable(!croppingOnOff.isSelected());
          applyBtn.setDisable(!croppingOnOff.isSelected());


      });
    }

    public void initialize(Button applyBtn){
        cropButton.setDisable(true);
        cropOptionBox.setDisable(true);
        widthField.setDisable(true);
        heightField.setDisable(true);
        applyBtn.setDisable(true);
        widthField.setPrefWidth(50);
        heightField.setPrefWidth(50);

        cropButton.setOnAction(actionEvent -> {
            if (CropPane.isAreaSelected) {
                CropPaneController.cropImage(CropPane.areaSelection.getSelectionRectangle().getBoundsInParent(), CropPane.mainImageView);
            }
        });

        applyBtn.setOnAction(actionEvent -> {
            double width;
            double height;
            try {
                width = Double.parseDouble(widthField.getText());
                if (this.rectangle instanceof ResizableRectangleWithRatio1) {
                    height = width / ((ResizableRectangleWithRatio1) this.rectangle).aspect_ratio;
                } else {
                    height = Double.parseDouble(heightField.getText());
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Enter numbers only!");
                alert.showAndWait();
                return;

            }
            if (width + this.rectangle.getX() > CropPane.mainImageView.getBoundsInParent().getWidth() | height + this.rectangle.getY() > CropPane.mainImageView.getBoundsInParent().getHeight()) {

                /* width = CropPane.mainImageView.getBoundsInParent().getWidth() - this.rectangle.getX();*/
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Crop Size Cannot Be Bigger Than Image Size.");
                alert.showAndWait();
                return;

            }
            else if(width<=0 | height<=0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Crop Size Cannot Be 0 or Negative.");
                alert.showAndWait();
                return;

            }

            this.rectangle.setWidth(width);
            this.rectangle.setHeight(height);
            widthField.setText(String.format("%.2f", width));
            heightField.setText(String.format("%.2f", height));
        });

    }
}
