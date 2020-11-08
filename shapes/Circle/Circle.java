package by.bsuir.oop.paint.modules.shapes.Circle;

import by.bsuir.oop.paint.entity.*;
import javafx.scene.canvas.Canvas;

public class Circle extends Shape {

    public Circle(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){
        double side = Math.min(Math.abs(firstPoint.getY() - secondPoint.getY()), Math.abs(firstPoint.getX() - secondPoint.getX())),
                x = firstPoint.getX(), y = firstPoint.getY();
        if (firstPoint.getX() > secondPoint.getX()) x -= side;
        if (firstPoint.getY() > secondPoint.getY()) y -= side;
        canvas.getGraphicsContext2D().strokeOval(x, y, side, side);
    }
}
