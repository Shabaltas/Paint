package by.bsuir.oop.paint.modules.shapes.RoundRectangle;

import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.ShapeFactory;
import by.bsuir.oop.paint.entity.Shape;

public class RoundRectangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new RoundRectangle(point1, point2);
    }
}
