package modules.shapes.Ellipse;

import entity.MyPoint;
import entity.Shape;
import entity.ShapeFactory;

public class EllipseFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Ellipse(point1, point2);
    }
}
