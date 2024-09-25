package se233.project.model;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import se233.project.controller.viewcontrollers.CropPaneController;
import se233.project.view.CropPane;

public class AreaSelection {

    private final Paint darkAreaColor = Color.color(0, 0, 0, 0.5);
    private Group group;
    private ResizableRectangle selectionRectangle = null;
    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
        if (selectionRectangle != null)
            CropPane.isAreaSelected = true;
    };
    private double rectangleStartX;
    private double rectangleStartY;
    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        CropPaneController.clearSelection(group);
        if (event.isSecondaryButtonDown()) {

            return;
        }

        rectangleStartX = event.getX();
        rectangleStartY = event.getY();
        selectionRectangle = null;



    };
    private javafx.scene.image.ImageView mainImageView;
    private Image mainImage;
    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        if (event.isSecondaryButtonDown())
            return;

        double offsetX = event.getX() - rectangleStartX;
        double offsetY = event.getY() - rectangleStartY;
        System.out.println("On Mouse Drag:" + offsetX + "," + offsetY);

        if (offsetX > 0) {
            if (event.getX() > mainImageView.getBoundsInParent().getWidth()) {
                selectionRectangle.setWidth(mainImageView.getBoundsInParent().getWidth() - rectangleStartX);
                System.out.println("Drag stuck at x position:" + event.getX() + " image width+" + mainImage.getWidth());
            } else {
                selectionRectangle.setWidth(offsetX);
            }

        } else {
            if (event.getX() < 0)
                selectionRectangle.setX(0);
            else
                selectionRectangle.setX(event.getX());
            selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
        }

        if (offsetY > 0) {
            if (event.getY() > mainImageView.getBoundsInParent().getHeight())
                selectionRectangle.setHeight(mainImageView.getBoundsInParent().getHeight() - rectangleStartY);
            else
                selectionRectangle.setHeight(offsetY);
        } else {
            if (event.getY() < 0)
                selectionRectangle.setY(0);
            else
                selectionRectangle.setY(event.getY());
            selectionRectangle.setHeight(rectangleStartY - selectionRectangle.getY());
        }

    };
    private String mode;

    public AreaSelection(ImageView imageView, Image image) {
        this.mainImageView = imageView;
        this.mainImage = image;
        this.mode = "Free";
    }

    public ResizableRectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public void setSelectionRectangle(ResizableRectangle selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void selectArea(Group group) {

        this.group = group;
        if (group.getChildren().size() >= 2) {
            System.out.println("Rectangle already added.");
            return;
        }

        // group.getChildren().get(0) == mainImageView. We assume image view as base container layer
        if (mainImageView != null && mainImage != null) {
            CropPaneController.clearSelection(group);
            double rectangleWidth = 100;
            double rectangleHeight=100;
            if(mainImageView.getBoundsInParent().getWidth()<=100){
                rectangleWidth = mainImageView.getBoundsInParent().getWidth()-10;
            }
            if(mainImageView.getBoundsInParent().getHeight()<=100){
                rectangleHeight = mainImageView.getBoundsInParent().getHeight()-10;
            }
            rectangleStartX = 0;
            rectangleStartY = 0;
            switch (mode) {
                case "Free" ->
                        selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, rectangleWidth, rectangleHeight, this.group);
                case "16/9" ->
                        selectionRectangle = new ResizableRectangleWithRatio1(rectangleStartX, rectangleStartY, rectangleWidth, this.group, 16.0 / 9);
                case "4/3" ->
                        selectionRectangle = new ResizableRectangleWithRatio1(rectangleStartX, rectangleStartY, rectangleWidth, this.group, 4.0 / 3);
                case "1/1" ->
                        selectionRectangle = new ResizableRectangleWithRatio1(rectangleStartX, rectangleStartY, rectangleWidth, this.group, 1.0);
                case "3/2" ->
                        selectionRectangle = new ResizableRectangleWithRatio1(rectangleStartX, rectangleStartY, rectangleWidth, this.group, 3.0 / 2);
                default ->
                        selectionRectangle = new ResizableRectangleWithRatio1(rectangleStartX, rectangleStartY, rectangleWidth, this.group, 5.0 / 4);
            }

            CropPane.isAreaSelected = true;
            CropPane.setObPropertyForImageView(mainImageView);
            darkenOutsideRectangle(selectionRectangle);

        }


    }

    public void darkenOutsideRectangle(Rectangle rectangle) {
        Rectangle darkAreaTop = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaLeft = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaRight = new Rectangle(0, 0, darkAreaColor);
        Rectangle darkAreaBottom = new Rectangle(0, 0, darkAreaColor);
       // darkAreaTop.widthProperty().bind(CropPane.scrollPane.getWidth());
        darkAreaTop.widthProperty().bind(CropPane.displayWidth);
        darkAreaTop.heightProperty().bind(rectangle.yProperty());

        darkAreaLeft.yProperty().bind(rectangle.yProperty());
        darkAreaLeft.widthProperty().bind(rectangle.xProperty());
        darkAreaLeft.heightProperty().bind(rectangle.heightProperty());

        darkAreaRight.xProperty().bind(rectangle.xProperty().add(rectangle.widthProperty()));
        darkAreaRight.yProperty().bind(rectangle.yProperty());
  /*      darkAreaRight.widthProperty().bind(CropPane.displayWidth.subtract(
                rectangle.xProperty().add(rectangle.widthProperty())));*/
        darkAreaRight.widthProperty().bind(CropPane.displayWidth.subtract(rectangle.widthProperty().add(rectangle.xProperty())));
        darkAreaRight.heightProperty().bind(rectangle.heightProperty());

        darkAreaBottom.yProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        darkAreaBottom.widthProperty().bind(CropPane.displayWidth);

        darkAreaBottom.heightProperty().bind(CropPane.displayHeight.subtract(
                rectangle.yProperty().add(rectangle.heightProperty())));


        // adding dark area rectangles before the selectionRectangle. So it can't overlap rectangle
        group.getChildren().add(1, darkAreaTop);
        group.getChildren().add(1, darkAreaLeft);
        group.getChildren().add(1, darkAreaBottom);
        group.getChildren().add(1, darkAreaRight);

        // make dark area container layer as well
      /*  darkAreaTop.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaLeft.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaRight.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaRight.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaRight.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);*/
    }
}
