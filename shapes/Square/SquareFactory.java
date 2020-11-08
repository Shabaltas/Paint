package by.bsuir.oop.paint.modules.shapes.Square;

import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.Shape;
import by.bsuir.oop.paint.entity.ShapeFactory;

public class SquareFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Square(point1, point2);
    }
}
