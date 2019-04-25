package modules.shapes.Line;

import entity.ShapeFactory;
import entity.MyPoint;
import entity.Shape;

public class LineFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Line(point1, point2);
    }
}
