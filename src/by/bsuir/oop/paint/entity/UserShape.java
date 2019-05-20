package by.bsuir.oop.paint.entity;

import javafx.scene.canvas.Canvas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "UserShape")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserShape extends Shape {

    @XmlElement(name = "innerShape")
    private final List<Shape> shapes = new ArrayList<>();
    private MyPoint initFirstPoint, initSecondPoint;
    private final List<MyPoint[]> mapa = new ArrayList<>();

    public UserShape(){
    }

    public UserShape(MyPoint point1, MyPoint point2){
       super(point1, point2);
    }

    public void draw(Canvas canvas){
        for (Shape shape : shapes) {
            shape.draw(canvas);
        }
    }

    private void calcCoord(){
        double coefX = (secondPoint.getX() - firstPoint.getX())/(initSecondPoint.getX() - initFirstPoint.getX());
        double coefY = (secondPoint.getY() - firstPoint.getY())/(initSecondPoint.getY() - initFirstPoint.getY());
        int i = 0;
        while (i < shapes.size()){
            Shape shape = shapes.get(i);
            MyPoint point1 = mapa.get(i)[0],
                    point2 = mapa.get(i)[1];
            double x1, y1, x2, y2;
            x1 = firstPoint.getX() + (point1.getX() - initFirstPoint.getX())*coefX;
            y1 = firstPoint.getY() + (point1.getY() - initFirstPoint.getY())*coefY;
            x2 = x1 + (point2.getX() - point1.getX())*coefX;
            y2 = y1 + (point2.getY() - point1.getY())*coefY;
            shape.setFirstPoint(new MyPoint(x1, y1));
            shape.setSecondPoint(new MyPoint(x2, y2));
            i++;
        }
    }

    public void setShapes(List<Shape> shapes){
        this.shapes.addAll(shapes);
        setInitPoints();
    }

    public List<Shape> getShapes(){
        return this.shapes;
    }

    private void setInitPoints(){
        MyPoint point1 = shapes.get(0).firstPoint,
                point2 = shapes.get(0).secondPoint;
        double minX = point1.getX(),
               minY = point1.getY(),
               maxX = point2.getX(),
               maxY = point2.getY(),
               curX, curY;
        int i = 0;
        while (i < shapes.size()){
            point1 = shapes.get(i).firstPoint;
            point2 = shapes.get(i).secondPoint;
            curX = Math.min(point1.getX(), point2.getX());
            curY = Math.min(point1.getY(), point2.getY());
            if (curX < minX) minX = curX;
            if (curY < minY) minY = curY;
            curX = Math.max(point1.getX(), point2.getX());
            curY = Math.max(point1.getY(), point2.getY());
            if (curX > maxX) maxX = curX;
            if (curY > maxY) maxY = curY;
            i++;
        }
        this.initFirstPoint = new MyPoint(minX, minY);
        this.initSecondPoint = new MyPoint(maxX, maxY);
    }

    public void setInitFirstPoint(MyPoint initFirstPoint) {
        this.initFirstPoint = initFirstPoint;
    }

    public void setInitSecondPoint(MyPoint initSecondPoint) {
        this.initSecondPoint = initSecondPoint;
    }

    public MyPoint getInitFirstPoint() {
        return initFirstPoint;
    }

    public MyPoint getInitSecondPoint() {
        return initSecondPoint;
    }
    @Override
    public void setSecondPoint(MyPoint point){
        this.secondPoint = point;
        calcCoord();
    }

    public List<MyPoint[]> getMapa() {
        return mapa;
    }

}
