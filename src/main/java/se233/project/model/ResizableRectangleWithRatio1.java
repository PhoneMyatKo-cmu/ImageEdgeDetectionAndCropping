package se233.project.model;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import se233.project.view.CropPane;


public class ResizableRectangleWithRatio1 extends ResizableRectangle {

    private double rectangleStartX;
    private double rectangleStartY;
    private double mouseClickPozX;
    private double mouseClickPozY;
    private static final double RESIZER_SQUARE_SIDE = 8;
    private Paint resizerSquareColor = Color.WHITE;
    private Paint rectangleStrokeColor = Color.BLUE;
    private final Paint darkAreaColor = Color.color(0,0,0,0.2);
    private double aspect_ratio;

    public ResizableRectangleWithRatio1(double x, double y, double width, Group group,double aspect_ratio) {
          super(x,y,width,aspect_ratio,0,group);
        makeCWResizerSquare(group);
        //  makeSWResizerSquare(group);
        makeSCResizerSquare(group);
        //  makeSEResizerSquare(group);
        makeCEResizerSquare(group);
        //  makeNEResizerSquare(group);
        makeNCResizerSquare(group);
        this.aspect_ratio=aspect_ratio;

    }
    //TopLeft Corner
    private void makeNWResizerSquare(Group group) {
        Rectangle squareNW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNW.xProperty().bind(super.xProperty().subtract(squareNW.widthProperty().divide(2.0)));
        squareNW.yProperty().bind(super.yProperty().subtract(squareNW.heightProperty().divide(2.0)));
        group.getChildren().add(squareNW);

        squareNW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNW.getParent().setCursor(Cursor.NW_RESIZE));

        prepareResizerSquare(squareNW);

        squareNW.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newX = super.getX() + offsetX ;
            double newY = super.getY() + offsetY ;

            if (newX >= 0 && newX <= super.getX() + super.getWidth() ) {
                super.setX(newX);
                super.setWidth(super.getWidth() - offsetX);
            }

            if (newY >= 0 && newY <= super.getY() + super.getHeight() ) {
                super.setY(newY);
                super.setHeight(super.getHeight() - offsetY);
            }

        });
    }

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




            System.out.println("OffsetY:"+offsetY);
            System.out.println("Rectangle Height:"+super.getHeight());

        });

    }

    private void makeSWResizerSquare(Group group) {
        Rectangle squareSW = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareSW.xProperty().bind(super.xProperty().subtract(squareSW.widthProperty().divide(2.0)));
        squareSW.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSW.heightProperty().divide(2.0))));
        group.getChildren().add(squareSW);

        squareSW.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSW.getParent().setCursor(Cursor.SW_RESIZE));

        prepareResizerSquare(squareSW);

        squareSW.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newX = super.getX() + offsetX;

            if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
                super.setX(newX);
                super.setWidth(super.getWidth() - offsetX);
            }
            if(event.getY()>=group.getChildren().get(0).getBoundsInLocal().getHeight()){

                return;
            }

            if (offsetY >= 0 && offsetY <= super.getY() + super.getHeight() - 5 && event.getY()<=CropPane.mainImageView.getBoundsInParent().getHeight()) {
                super.setHeight(offsetY);
            }
        });
    }

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
            double offsetY = event.getY() - rectangleStartY;
            double offsetX=(offsetY-super.getHeight())*aspect_ratio;

            /*Test*/       if (offsetY >= 0 && event.getY()<= CropPane.mainImageView.getBoundsInParent().getHeight() && super.getWidth()+rectangleStartX+offsetX<=group.getChildren().get(0).getBoundsInLocal().getWidth()) {
                super.setHeight(offsetY);
                if(super.getWidth()+rectangleStartX+offsetX<=group.getChildren().get(0).getBoundsInLocal().getWidth()){
                    super.setWidth(super.getWidth()+offsetX);
                }
            } else if (offsetX<=0) {
                super.setWidth(super.getWidth()+offsetX);

            }


        });
    }

    private void makeSEResizerSquare(Group group) {
        Rectangle squareSE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);
        squareSE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareSE.widthProperty().divide(2.0)));
        squareSE.yProperty().bind(super.yProperty().add(super.heightProperty().subtract(
                squareSE.heightProperty().divide(2.0))));
        group.getChildren().add(squareSE);

        squareSE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareSE.getParent().setCursor(Cursor.SE_RESIZE));

        prepareResizerSquare(squareSE);

        squareSE.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;

            /*Test*/   if (offsetX >= 0  && event.getX()<=CropPane.mainImageView.getBoundsInParent().getWidth()) {
                super.setWidth(offsetX);
            }

            /*Test*/     if (offsetY >= 0  && event.getY()<=CropPane.mainImageView.getBoundsInParent().getHeight()) {
                super.setHeight(offsetY);
            }
        });
    }

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




        });
    }

    private void makeNEResizerSquare(Group group){
        Rectangle squareNE = new Rectangle(RESIZER_SQUARE_SIDE,RESIZER_SQUARE_SIDE);

        squareNE.xProperty().bind(super.xProperty().add(super.widthProperty()).subtract(
                squareNE.widthProperty().divide(2.0)));
        squareNE.yProperty().bind(super.yProperty().subtract(squareNE.heightProperty().divide(2.0)));
        group.getChildren().add(squareNE);

        squareNE.addEventHandler(MouseEvent.MOUSE_ENTERED,event ->
                squareNE.getParent().setCursor(Cursor.NE_RESIZE));

        prepareResizerSquare(squareNE);

        squareNE.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            rectangleStartX = super.getX();
            rectangleStartY = super.getY();
            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;
            double newY = super.getY() + offsetY ;

            if (offsetX >= 0 && offsetX <= super.getX() + super.getWidth() - 5 && event.getX()<=CropPane.mainImageView.getBoundsInParent().getWidth()) {
                super.setWidth(offsetX);
            }

            if (newY >= 0 && newY <= super.getY() + super.getHeight() - 5) {
                super.setY(newY);
                super.setHeight(super.getHeight() - offsetY);
            }

        });
    }

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
            double newY = super.getY() + offsetY ;

            if (newY >= 0 && newY <= super.getY() + super.getHeight()) {
                super.setY(newY);
                super.setHeight(super.getHeight() - offsetY);
            }

        });
    }

    private void prepareResizerSquare(Rectangle rect) {
        rect.setFill(resizerSquareColor);

        rect.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
                rect.getParent().setCursor(Cursor.DEFAULT));
    }



}

