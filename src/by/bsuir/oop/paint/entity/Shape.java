package by.bsuir.oop.paint.entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "shape")
@XmlAccessorType(XmlAccessType.FIELD)
public  class Shape {

    Shape(){}

    protected MyPoint firstPoint, secondPoint;

    private String color;

    @XmlAttribute
    private String type;

    public Shape (MyPoint point1, MyPoint point2){
        firstPoint = point1;
        secondPoint = point2;
        type = this.getClass().getSimpleName();
    }

    public final MyPoint getFirstPoint(){
        return firstPoint;
    }

    public void setFirstPoint(MyPoint firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void setSecondPoint(MyPoint secondPoint) {
        this.secondPoint = secondPoint;
    }

    public final MyPoint getSecondPoint(){
        return secondPoint;
    }

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color.toString();
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void draw(Canvas canvas){}
}
