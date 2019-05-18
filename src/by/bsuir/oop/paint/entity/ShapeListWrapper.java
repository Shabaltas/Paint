package by.bsuir.oop.paint.entity;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name = "Shapes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShapeListWrapper {

    @XmlElement(name = "shape")
    private ArrayList<Shape> shapeList;

    public ShapeListWrapper(){

    }

    public ArrayList<Shape> getShapeList() {
        return shapeList;
    }

    public void setShapeList(ArrayList<Shape> shapeList) {
        this.shapeList = shapeList;
    }
}
