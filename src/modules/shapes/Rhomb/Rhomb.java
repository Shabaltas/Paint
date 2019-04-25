package modules.shapes.Rhomb;

import entity.*;
import javafx.scene.canvas.Canvas;

public class Rhomb extends Shape {

    public Rhomb(MyPoint point1, MyPoint point2){
        super(point1, point2);
    }

    @Override
    public void draw(Canvas canvas){
        double x1 = firstPoint.getX(), y1 = firstPoint.getY(),
                x2 = secondPoint.getX(), y2 = secondPoint.getY(),
                y3 = 2*y2 - y1, x4 = 2*x1 - x2;
        canvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
        canvas.getGraphicsContext2D().strokeLine(x2, y2, x1, y3);
        canvas.getGraphicsContext2D().strokeLine(x1, y3, x4, y2);
        canvas.getGraphicsContext2D().strokeLine(x4, y2, x1, y1);
    }
}
