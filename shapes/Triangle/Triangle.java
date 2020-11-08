package by.bsuir.oop.paint.modules.shapes.Triangle;

import by.bsuir.oop.paint.entity.*;
import javafx.scene.canvas.Canvas;

public class Triangle extends Shape {

    public Triangle(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){

        double x1 = firstPoint.getX(), y1 = firstPoint.getY(),
                x2 = secondPoint.getX(), y2 = secondPoint.getY(),
                x3 = 2*x1 - x2;
        canvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
        canvas.getGraphicsContext2D().strokeLine(x2, y2, x3, y2);
        canvas.getGraphicsContext2D().strokeLine(x3, y2, x1, y1);
    }
}

