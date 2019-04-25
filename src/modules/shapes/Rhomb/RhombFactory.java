package modules.shapes.Rhomb;

import entity.MyPoint;
import entity.Shape;
import entity.ShapeFactory;

public class RhombFactory extends ShapeFactory {
    @Override
    public Shape newShape(MyPoint point1, MyPoint point2) {
        return new Rhomb(point1, point2);
    }
}
