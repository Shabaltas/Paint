package by.bsuir.oop.paint.modules.shapes.Line;

import by.bsuir.oop.paint.entity.ShapeFactory;
import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.Shape;

public class LineFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Line(point1, point2);
    }
}
