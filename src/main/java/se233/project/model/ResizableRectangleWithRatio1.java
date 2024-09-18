package se233.project.model;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import se233.project.view.CropPane;


public class ResizableRectangleWithRatio1 extends ResizableRectangle {

    private double rectangleStartX;
    private double rectangleStartY;
    private static final double RESIZER_SQUARE_SIDE = 8;
    public final double aspect_ratio;

    public ResizableRectangleWithRatio1(double x, double y, double width, Group group,double aspect_ratio) {
          super(x,y,width,aspect_ratio,0,group);
        makeCWResizerSquare(group);
        makeSCResizerSquare(group);
        makeCEResizerSquare(group);
        makeNCResizerSquare(group);
        this.aspect_ratio=aspect_ratio;
    }
    //Left Side
    private void makeCWResizerSquare(Group group) {
        Rectangle squareCW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareCW.xProperty().bind(super.xProperty().subtract(squareCW.widthProperty().divide(2.0)));
        squareCW.yProperty().bind(super.yProperty().add(super.heightProperty().divide(2.0).subtract(
                squareCW.heightProperty().divide(2.0))));
        group.getChildren().add(squareCW);

        squareCW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareCW.getParent().setCursor(Cursor.W_RESIZE));

        prepareResizerSquare(squareCW);

        squareCW.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY=-1*offsetX/aspect_ratio;
            double newX = super.getX() + offsetX;
            double newY=super.getY()+offsetY;

            if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5 && super.getY()+super.getHeight()<=group.getChildren().get(0).getBoundsInLocal().getHeight()) {
                super.setX(newX);
                super.setWidth(super.getWidth() - offsetX);
                if ( super.getY()+super.getHeight()<=group.getChildren().get(0).getBoundsInLocal().getHeight()) {
                    super.setHeight(super.getHeight()+offsetY);
                }


            }
            else if(super.getY()+super.getHeight()>group.getChildren().get(0).getBoundsInLocal().getHeight()){
                if(offsetY<=0)
                 super.setHeight(super.getHeight()+offsetY);
            }
            CropPane.cropBoxConfigPane.refresh(this);




            System.out.println("OffsetY:"+offsetY);
            System.out.println("Rectangle Height:"+super.getHeight());

        });

    }

//Bottom Side
    private void makeSCResizerSquare(Group group) {
        Rectangle squareSC = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareSC.xProperty().bind(super.xProperty().add(super.widthProperty().divide(2.0).subtract(
                squareSC.widthProperty().divide(2.0))));
        squareSC.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSC.heightProperty().divide(2.0))));
        group.getChildren().add(squareSC);

        squareSC.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSC.getParent().setCursor(Cursor.S_RESIZE));

        prepareResizerSquare(squareSC);

        squareSC.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartY = super.getY();
            rectangleStartX=super.getX();
            double offsetY = event.getY() - rectangleStartY;
            double offsetX=(offsetY-super.getHeight())*aspect_ratio;
            double width=super.getWidth();
            if (offsetY >= 0 && offsetY <= super.getY() + super.getHeight() - 5 && event.getY()<= CropPane.mainImageView.getBoundsInParent().getHeight() && super.getX()+super.getWidth()<=group.getChildren().get(0).getBoundsInLocal().getWidth()) {
                super.setHeight(offsetY);
                if ( super.getX()+super.getWidth()<=group.getChildren().get(0).getBoundsInLocal().getWidth()) {
                    super.setWidth(super.getWidth()+offsetX);
                }

            }
            else if(offsetX<=0){
                super.setWidth(super.getWidth()+offsetX);
            }
            CropPane.cropBoxConfigPane.refresh(this);

            System.out.println("\nFor Bottom Side:");
            System.out.println("Image View Bounds:"+CropPane.mainImageView.getBoundsInParent().getWidth());
            System.out.println("Rectangle X:"+rectangleStartX);
            System.out.println("Width"+super.getWidth());


        });
    }

//Right Side
    private void makeCEResizerSquare(Group group) {
        Rectangle squareCE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareCE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareCE.widthProperty().divide(2.0)));
        squareCE.yProperty().bind(super.yProperty().add(super.heightProperty().divide(2.0).subtract(
                squareCE.heightProperty().divide(2.0))));
        group.getChildren().add(squareCE);

        squareCE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareCE.getParent().setCursor(Cursor.E_RESIZE));

        prepareResizerSquare(squareCE);

        squareCE.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY=(offsetX-super.getWidth())/aspect_ratio;
            if (offsetX >= 0 && offsetX <= super.getX() + super.getWidth() - 5 && event.getX()<= CropPane.mainImageView.getBoundsInParent().getWidth() && super.getY()+super.getHeight()<=group.getChildren().get(0).getBoundsInLocal().getHeight()) {
                super.setWidth(offsetX);
                if ( super.getY()+super.getHeight()<=group.getChildren().get(0).getBoundsInLocal().getHeight()) {
                    super.setHeight(super.getHeight()+offsetY);
                }

            }
            else if(offsetY<=0){
                super.setHeight(super.getHeight()+offsetY);
            }
            CropPane.cropBoxConfigPane.refresh(this);

            System.out.println("\nFor Right Side:");
            System.out.println("Image View Bounds:"+CropPane.mainImageView.getBoundsInParent().getWidth());
            System.out.println("Rectangle Bounds:"+(rectangleStartX+this.getWidth()));




        });
    }


//Top Side
    private void makeNCResizerSquare(Group group){
        Rectangle squareNC = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNC.xProperty().bind(super.xProperty().add(super.widthProperty().divide(2.0).subtract(
                squareNC.widthProperty().divide(2.0))));
        squareNC.yProperty().bind(super.yProperty().subtract(
                squareNC.heightProperty().divide(2.0)));
        group.getChildren().add(squareNC);

        squareNC.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNC.getParent().setCursor(Cursor.N_RESIZE));

        prepareResizerSquare(squareNC);

        squareNC.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartY = super.getY();
            double offsetY = event.getY() - rectangleStartY;
            double offsetX= (-1)*offsetY*aspect_ratio;
            double newY = super.getY() + offsetY ;

            if (newY >= 0 && newY <= super.getY() + super.getHeight() - 5 && super.getX()+super.getWidth()<=group.getChildren().get(0).getBoundsInLocal().getWidth()) {
                super.setY(newY);
                super.setHeight(super.getHeight() - offsetY);
                if ( super.getX()+super.getWidth()<=group.getChildren().get(0).getBoundsInLocal().getWidth()) {
                    super.setWidth(super.getWidth()+offsetX);
                }


            }
            else if(super.getX()+super.getWidth()>group.getChildren().get(0).getBoundsInLocal().getWidth()){
                if(offsetX<=0)
                    super.setWidth(super.getWidth()+offsetX);
            }
            CropPane.cropBoxConfigPane.refresh(this);

        });
    }



}

