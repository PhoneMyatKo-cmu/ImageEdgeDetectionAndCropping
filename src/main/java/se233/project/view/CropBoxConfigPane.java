package se233.project.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se233.project.model.ResizableRectangle;
import se233.project.model.ResizableRectangleWithRatio1;

public class CropBoxConfigPane extends VBox {
    ResizableRectangle rectangle;
    TextField widthField;
    TextField heightField;

    public CropBoxConfigPane(ResizableRectangle rectangle) {
        super(30);
        this.rectangle = rectangle;
        setAlignment(Pos.CENTER);
        setWidth(200);
        widthField = new TextField();
        heightField = new TextField();
        widthField.setPrefWidth(50);
        heightField.setPrefWidth(50);
        HBox dimensionBox = new HBox(10);
        dimensionBox.getChildren().addAll(widthField, heightField);
        HBox lblBox = new HBox(10);
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
            if (width + this.rectangle.getX() > CropPane.mainImageView.getBoundsInParent().getWidth()) {

                width = CropPane.mainImageView.getBoundsInParent().getWidth() - this.rectangle.getX();

            }
            if (height + this.rectangle.getY() > CropPane.mainImageView.getBoundsInParent().getHeight()) {
                height = CropPane.mainImageView.getBoundsInParent().getHeight() - this.rectangle.getY();
            }

            this.rectangle.setWidth(width);
            this.rectangle.setHeight(height);
            widthField.setText(String.format("%.2f", width));
            heightField.setText(String.format("%.2f", height));
        });

        this.getChildren().addAll(lblBox, dimensionBox, applyBtn);
        this.setPadding(new Insets(20, 20, 20, 20));

    }

    public void refresh(ResizableRectangle rectangle) {
        if (rectangle == null) {
            widthField.setText("" + 0);
            heightField.setText("" + 0);
            return;
        } else heightField.setEditable(!(rectangle instanceof ResizableRectangleWithRatio1));
        this.rectangle = rectangle;
        widthField.setText(String.format("%.2f", rectangle.getWidth()));
        heightField.setText(String.format("%.2f", rectangle.getHeight()));
    }
}
