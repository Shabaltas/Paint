package modules.shapes.Rectangle;

import entity.MyPoint;
import entity.ShapeFactory;
import entity.Shape;

public class RectangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Rectangle(point1, point2);
    }
}
