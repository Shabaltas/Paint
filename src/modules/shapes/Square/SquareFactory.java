package modules.shapes.Square;

import entity.MyPoint;
import entity.Shape;
import entity.ShapeFactory;

public class SquareFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Square(point1, point2);
    }
}
