package hr.fer.zemris.irg.models;

import java.awt.*;

/**
 * @author Filip Gulan
 */
public class Triangle {

    private Color color;
    private Point firstPoint;
    private Point secondPoint;
    private Point thirdPoint;

    public Triangle(Color color, Point firstPoint, Point secondPoint, Point thirdPoint) {
        this.color = color;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public Point getThirdPoint() {
        return thirdPoint;
    }

    public void setThirdPoint(Point thirdPoint) {
        this.thirdPoint = thirdPoint;
    }
}
