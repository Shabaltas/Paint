package modules.shapes.Circle;

import entity.MyPoint;
import entity.Shape;
import entity.ShapeFactory;

public class CircleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Circle(point1, point2);
    }
}
