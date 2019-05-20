package by.bsuir.oop.paint.entity;

import java.util.HashMap;
import java.util.List;

public class UserShapeFactory extends ShapeFactory {

    private UserShape userShape;

    private HashMap<String, ShapeFactory> factoryHashMap;

    public UserShapeFactory(UserShape shape, HashMap<String, ShapeFactory> factoryHashMap){
        this.userShape = shape;
        this.factoryHashMap = factoryHashMap;
    }

    @Override
    public Shape newShape(MyPoint point1, MyPoint point2){
        UserShape uShape = new UserShape(point1, point2);
        List<Shape> shapes = userShape.getShapes();
        for (Shape shape : shapes){
            uShape.getShapes().add(factoryHashMap.get(shape.getType()).newShape(shape.getFirstPoint(), shape.getSecondPoint()));
            uShape.getMapa().add(new MyPoint[]{shape.getFirstPoint(), shape.getSecondPoint()});
        }
        uShape.setInitFirstPoint(userShape.getInitFirstPoint());
        uShape.setInitSecondPoint(userShape.getInitSecondPoint());
        return uShape;
    }


}
