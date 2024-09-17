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

    private Group group;

    private ResizableRectangle selectionRectangle = null;
    private double rectangleStartX;
    private double rectangleStartY;
    private final Paint darkAreaColor = Color.color(0,0,0,0.5);
    private javafx.scene.image.ImageView mainImageView;
    private Image mainImage;
    private String mode;

    public AreaSelection(ImageView imageView, Image image){
        this.mainImageView=imageView;
        this.mainImage=image;
        this.mode="Free";
    }

    public void resizeRectangleByRatio(){
        this.selectionRectangle.setWidth(100);
        this.selectionRectangle.setHeight(100.0/(9/16));
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

    public ResizableRectangle selectArea(Group group) {

        this.group = group;
        if(group.getChildren().size()>=2)
        {System.out.println("Rectangle already added.");
            return null;}
      /*  this.group.prefWidth(mainImage.getWidth());
        this.group.prefHeight(mainImage.getHeight())*///fix a little check on it later


        // group.getChildren().get(0) == mainImageView. We assume image view as base container layer
        if (mainImageView != null && mainImage != null ) {
            this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
         // if(selectionRectangle==null){
              CropPaneController.clearSelection(group);
              rectangleStartX =30;
              rectangleStartY = 30;
              if(mode=="Free")
              selectionRectangle=new ResizableRectangle(rectangleStartX,rectangleStartY,100,100,this.group);
              else if(mode=="16/9")
              selectionRectangle=new ResizableRectangleWithRatio1(rectangleStartX,rectangleStartY,100,this.group,16.0/9) ;
              else if(mode=="4/3")
                  selectionRectangle=new ResizableRectangleWithRatio1(rectangleStartX,rectangleStartY,100,this.group,4.0/3);
              else if (mode=="1/1") {
                  selectionRectangle=new ResizableRectangleWithRatio1(rectangleStartX,rectangleStartY,100,this.group,1.0/1);
              }
              else if (mode=="3/2") {
                  selectionRectangle=new ResizableRectangleWithRatio1(rectangleStartX,rectangleStartY,100,this.group,3.0/2);
              }
              else  {
                  selectionRectangle=new ResizableRectangleWithRatio1(rectangleStartX,rectangleStartY,100,this.group,5.0/4);
              }


            // darkenOutsideRectangle(selectionRectangle);
         // }

       /*    else {
              selectionRectangle.setWidth(100);
              selectionRectangle.setHeight(100);
              selectionRectangle.setX(30);
              selectionRectangle.setY(30);
              group.getChildren().add(selectionRectangle);
          }*/
            CropPane.isAreaSelected=true;

        }


        return selectionRectangle;
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        CropPaneController.clearSelection(group);
        if (event.isSecondaryButtonDown()){

            return;}



        rectangleStartX = event.getX();
        rectangleStartY = event.getY();


        selectionRectangle=null;

      //  selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, group);
        System.out.println("On Mouse Press:"+selectionRectangle.getWidth()+","+selectionRectangle.getHeight());

     //  darkenOutsideRectangle(selectionRectangle);

    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        if (event.isSecondaryButtonDown())
            return;

        double offsetX = event.getX() - rectangleStartX;
        double offsetY = event.getY() - rectangleStartY;
        System.out.println("On Mouse Drag:"+offsetX+","+offsetY);

        if (offsetX > 0) {
            if (event.getX() > mainImageView.getBoundsInParent().getWidth()){
                selectionRectangle.setWidth(mainImageView.getBoundsInParent().getWidth() - rectangleStartX);
            System.out.println("Drag stuck at x position:" + event.getX() + " image width+" + mainImage.getWidth());
        }
            else {
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

    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
        if (selectionRectangle != null)
            CropPane.isAreaSelected = true;
    };


    public void darkenOutsideRectangle(Rectangle rectangle) {
        Rectangle darkAreaTop = new Rectangle(0,0,darkAreaColor);
        Rectangle darkAreaLeft = new Rectangle(0,0,darkAreaColor);
        Rectangle darkAreaRight = new Rectangle(0,0,darkAreaColor);
        Rectangle darkAreaBottom = new Rectangle(0,0,darkAreaColor);

        darkAreaTop.widthProperty().bind(CropPane.displayWidth);
        darkAreaTop.heightProperty().bind(rectangle.yProperty());

        darkAreaLeft.yProperty().bind(rectangle.yProperty());
        darkAreaLeft.widthProperty().bind(rectangle.xProperty());
        darkAreaLeft.heightProperty().bind(rectangle.heightProperty());

        darkAreaRight.xProperty().bind(rectangle.xProperty().add(rectangle.widthProperty()));
        darkAreaRight.yProperty().bind(rectangle.yProperty());
        darkAreaRight.widthProperty().bind(CropPane.displayWidth.subtract(
                rectangle.xProperty().add(rectangle.widthProperty())));
        darkAreaRight.heightProperty().bind(rectangle.heightProperty());

        darkAreaBottom.yProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
        darkAreaBottom.widthProperty().bind(CropPane.displayWidth);
        darkAreaBottom.heightProperty().bind(CropPane.displayHeight.subtract(
                rectangle.yProperty().add(rectangle.heightProperty())));

        // adding dark area rectangles before the selectionRectangle. So it can't overlap rectangle
        group.getChildren().add(1,darkAreaTop);
        group.getChildren().add(1,darkAreaLeft);
        group.getChildren().add(1,darkAreaBottom);
        group.getChildren().add(1,darkAreaRight);

        // make dark area container layer as well
        darkAreaTop.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
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
        darkAreaBottom.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }
}
