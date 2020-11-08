package by.bsuir.oop.paint.modules.shapes.RoundRectangle;

import by.bsuir.oop.paint.entity.*;
import javafx.scene.canvas.Canvas;

public class RoundRectangle extends Shape {

    public RoundRectangle(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){
        double width = Math.abs(firstPoint.getX() - secondPoint.getX());
        double height = Math.abs(firstPoint.getY() - secondPoint.getY());
        canvas.getGraphicsContext2D().strokeRoundRect(Math.min(firstPoint.getX(), secondPoint.getX()),Math.min(firstPoint.getY(), secondPoint.getY()), width, height, 50, 50);
    }
}
