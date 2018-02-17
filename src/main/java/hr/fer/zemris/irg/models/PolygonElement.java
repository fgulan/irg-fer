package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.awt.*;

/**
 * @author Filip Gulan
 */
public class PolygonElement {

    public Point point;
    public Edge2D edge;
    public boolean left;

    public PolygonElement(Point point) {
        this.point = point;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PolygonElement that = (PolygonElement) o;

        if (left != that.left) return false;
        if (!point.equals(that.point)) return false;
        return edge != null ? edge.equals(that.edge) : that.edge == null;
    }

    @Override
    public int hashCode() {
        int result = point.hashCode();
        result = 31 * result + (edge != null ? edge.hashCode() : 0);
        result = 31 * result + (left ? 1 : 0);
        return result;
    }

    public IVector getPoint3DVector() {
        return new Vector(point.x, point.y, 1);
    }

    public IVector getEdge3DVector() {
        return edge.vector3D();
    }
}
