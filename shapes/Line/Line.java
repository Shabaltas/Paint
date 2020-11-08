package by.bsuir.oop.paint.modules.shapes.Line;

import by.bsuir.oop.paint.entity.*;
import javafx.scene.canvas.Canvas;

public class Line extends Shape {

    public Line(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.getGraphicsContext2D().strokeLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY());
    }
}