package by.bsuir.oop.paint.modules.shapes.Ellipse;

import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.Shape;
import by.bsuir.oop.paint.entity.ShapeFactory;

public class EllipseFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Ellipse(point1, point2);
    }
}
