package se233.project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se233.project.model.ResizableRectangle;
import se233.project.model.ResizableRectangleWithRatio1;

public class CropBoxConfigPane extends VBox {
    ResizableRectangle rectangle;
    TextField widthField;
    TextField heightField;
    ComboBox cropOptionBox;

    public CropBoxConfigPane(ResizableRectangle rectangle, ComboBox cropOption) {
        super(30);
        this.rectangle = rectangle;
        setAlignment(Pos.CENTER);
        setWidth(200);
        widthField = new TextField();
        heightField = new TextField();
        Label titleLbl=new Label("Crop Settings");
        this.cropOptionBox=cropOption;
        widthField.setPrefWidth(50);
        heightField.setPrefWidth(50);
        HBox dimensionBox = new HBox(10);
        dimensionBox.setAlignment(Pos.CENTER);
        dimensionBox.getChildren().addAll(widthField, heightField);
        HBox lblBox = new HBox(15);
        lblBox.setAlignment(Pos.CENTER);
        Label widthLbl = new Label("Width");
        Label heightLbl = new Label("Height");
        lblBox.getChildren().addAll(widthLbl, heightLbl);
        Button applyBtn = new Button("Apply");
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
                alert.setContentText("Enter numbers only!");
                alert.showAndWait();
                return;

            }
            if (width + this.rectangle.getX() > CropPane.mainImageView.getBoundsInParent().getWidth() | height + this.rectangle.getY() > CropPane.mainImageView.getBoundsInParent().getHeight()) {

               /* width = CropPane.mainImageView.getBoundsInParent().getWidth() - this.rectangle.getX();*/
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Crop Size Cannot Be Bigger Than Image Size.");
                alert.showAndWait();
                return;

            }
            else if(width<=0 | height<=0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Crop Size Cannot Be 0 or Negative.");
                alert.showAndWait();
                return;

            }

          /*  if (height + this.rectangle.getY() > CropPane.mainImageView.getBoundsInParent().getHeight()) {
                height = CropPane.mainImageView.getBoundsInParent().getHeight() - this.rectangle.getY();
            }
            else if(height<=0){
                return;
            }*/

            this.rectangle.setWidth(width);
            this.rectangle.setHeight(height);
            widthField.setText(String.format("%.2f", width));
            heightField.setText(String.format("%.2f", height));
        });

        this.getChildren().addAll(titleLbl,cropOptionBox,lblBox, dimensionBox, applyBtn);
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setPrefHeight(400);

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
}
