package by.bsuir.oop.paint.modules.shapes.Rectangle;

import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.ShapeFactory;
import by.bsuir.oop.paint.entity.Shape;

public class RectangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Rectangle(point1, point2);
    }
}
