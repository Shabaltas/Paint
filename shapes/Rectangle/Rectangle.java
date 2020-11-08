package by.bsuir.oop.paint.modules.shapes.Rectangle;

import by.bsuir.oop.paint.entity.*;
import javafx.scene.canvas.Canvas;

public class Rectangle extends Shape {

    public Rectangle(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){
        double width = Math.abs(firstPoint.getX() - secondPoint.getX());
        double height = Math.abs(firstPoint.getY() - secondPoint.getY());
        canvas.getGraphicsContext2D().strokeRect(Math.min(firstPoint.getX(), secondPoint.getX()),Math.min(firstPoint.getY(), secondPoint.getY()), width, height);
    }
}
