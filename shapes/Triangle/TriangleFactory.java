package by.bsuir.oop.paint.modules.shapes.Triangle;

import by.bsuir.oop.paint.entity.MyPoint;
import by.bsuir.oop.paint.entity.Shape;
import by.bsuir.oop.paint.entity.ShapeFactory;

public class TriangleFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Triangle(point1, point2);
    }
}


