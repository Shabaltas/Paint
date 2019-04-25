package modules.shapes.Triangle;

import entity.MyPoint;
import entity.Shape;
import entity.ShapeFactory;

public class TriangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Triangle(point1, point2);
    }
}


