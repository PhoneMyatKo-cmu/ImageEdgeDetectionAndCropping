package se233.project.model;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import se233.project.view.CropPane;


public class ResizableRectangle extends Rectangle {

    private double rectangleStartX;
    private double rectangleStartY;
    private double mouseClickPozX;
    private double mouseClickPozY;
    private static final double RESIZER_SQUARE_SIDE = 8;
    private Paint resizerSquareColor = Color.WHITE;
    private Paint rectangleStrokeColor = Color.BLUE;
    private final Paint darkAreaColor = Color.color(0,0,0,0.2);

    public ResizableRectangle(double x, double y, double width, double height, Group group) {

        super(x,y,width,height);
        helper(group);
        makeNWResizerSquare(group);
        makeCWResizerSquare(group);
        makeSWResizerSquare(group);
        makeSCResizerSquare(group);
        makeSEResizerSquare(group);
        makeCEResizerSquare(group);
        makeNEResizerSquare(group);
        makeNCResizerSquare(group);


    }

    public ResizableRectangle(double x, double y, double width,double aspectRatio,int dummy, Group group) {

        super(x,y,width,width/aspectRatio);
        helper(group);
        makeCWResizerSquare(group);
        makeSCResizerSquare(group);
        makeCEResizerSquare(group);
        makeNCResizerSquare(group);
    }

    public void helper(Group group){
        group.getChildren().add(this);
        super.setStroke(rectangleStrokeColor);
        super.setStrokeWidth(1);
        super.setFill(Color.color(1, 1, 1, 0));
        Rectangle moveRect = new Rectangle(0,0,0,0);
        moveRect.setFill(Color.color(1,1,1,0));
        moveRect.xProperty().bind(super.xProperty());
        moveRect.yProperty().bind(super.yProperty());
        moveRect.widthProperty().bind(super.widthProperty());
        moveRect.heightProperty().bind(super.heightProperty());
        moveRect.setFill(darkAreaColor);

        group.getChildren().add(moveRect);

        moveRect.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
                moveRect.getParent().setCursor(Cursor.HAND));

        moveRect.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            moveRect.getParent().setCursor(Cursor.MOVE);
            mouseClickPozX = event.getX();
            mouseClickPozY = event.getY();

        });

        moveRect.addEventHandler(MouseEvent.MOUSE_RELEASED, event ->
                moveRect.getParent().setCursor(Cursor.HAND));

        moveRect.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
                moveRect.getParent().setCursor(Cursor.DEFAULT));

        moveRect.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {

            double offsetX = event.getX() - mouseClickPozX;
            double offsetY = event.getY() - mouseClickPozY;
            double newX = super.getX() + offsetX ;
            double newY = super.getY() + offsetY ;
            System.out.println("Resizable Rectangle Move at Y:"+event.getY()+" Image View Width:"+ CropPane.mainImageView.getFitHeight());

            if (newX >= 0 && newX + super.getWidth() <= CropPane.mainImageView.getBoundsInParent().getWidth() ) {
                super.setX(newX);
            }
            if (newY >= 0 && newY + super.getHeight() <= CropPane.mainImageView.getBoundsInParent().getHeight()) {
                super.setY(newY);
            }
            mouseClickPozX = event.getX();
            mouseClickPozY = event.getY();

        });

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

            CropPane.cropBoxConfigPane.refresh(this);

        });
    }
/*Left Side*/
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
            double newX = super.getX() + offsetX;

            if (newX >= 0 && newX <= super.getX() + super.getWidth() - 5) {
                super.setX(newX);
                super.setWidth(super.getWidth() - offsetX);
            }
            CropPane.cropBoxConfigPane.refresh(this);

        });

    }
/*Bottom left Corner*/
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

            CropPane.cropBoxConfigPane.refresh(this);
        });
    }
/*Bottom Side*/
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

     /*Test*/       if (offsetY >= 0 && event.getY()<= CropPane.mainImageView.getBoundsInParent().getHeight()) {
                super.setHeight(offsetY);
            }
            CropPane.cropBoxConfigPane.refresh(this);

        });
    }
/*Bottom Right Corner*/
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
            CropPane.cropBoxConfigPane.refresh(this);
        });
    }
/*Right Side*/
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
            if (offsetX >= 0 && offsetX <= super.getX() + super.getWidth() - 5 && event.getX()<= CropPane.mainImageView.getBoundsInParent().getWidth()) {
                super.setWidth(offsetX);
            }

            CropPane.cropBoxConfigPane.refresh(this);



        });
    }
/*Top Right Corner*/
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
            CropPane.cropBoxConfigPane.refresh(this);

        });
    }
/*Top Side*/
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
            CropPane.cropBoxConfigPane.refresh(this);

        });
    }

    public void prepareResizerSquare(Rectangle rect) {
        rect.setFill(resizerSquareColor);

        rect.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
                rect.getParent().setCursor(Cursor.DEFAULT));
    }



}
