package modules.shapes.RoundRectangle;

import entity.MyPoint;
import entity.ShapeFactory;
import entity.Shape;

public class RoundRectangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new RoundRectangle(point1, point2);
    }
}
