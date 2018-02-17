package hr.fer.zemris.irg.models;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.interfaces.IDrawable;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Polygon implements IDrawable {

    private List<PolygonElement> elems;
    private PolygonElement currentPoint;
    private PolygonStatus status;
    private boolean updated;

    public Polygon() {
        this.currentPoint = null;
        this.elems = new ArrayList<>();
    }

    @Override
    public void drawElement(GL2 gl2) {
        gl2.glColor3f(0, 0, 0);
        gl2.glBegin(GL.GL_LINE_LOOP);
        for (PolygonElement elem : this.elems) {
            gl2.glVertex2f(elem.point.x, elem.point.y);
        }
        if (currentPoint != null) {
            gl2.glVertex2f(currentPoint.getX(), currentPoint.getY());
        }
        updatePolygonState(elems);
        gl2.glEnd();
    }

    @Override
    public void drawFilledElement(GL2 gl2) {
        List<PolygonElement> tempElems = elems;
        if (currentPoint != null) {
            tempElems = new ArrayList<>(elems);
            tempElems.add(currentPoint);
        }
        updatePolygonState(tempElems);
        fillPolygon(gl2, tempElems);
    }

    private void fillPolygon(GL2 gl2, List<PolygonElement> elems) {
        int size = elems.size();
        if (size < 1) {
            return;
        }

        gl2.glColor3f(0, 0, 0);
        gl2.glBegin(GL.GL_LINES);

        if (size == 2) {
            gl2.glVertex2f(elems.get(0).getX(), elems.get(0).getY());
            gl2.glVertex2f(elems.get(1).getX(), elems.get(1).getY());
            gl2.glEnd();
            return;
        }

        int xmin, xmax, ymin, ymax;
        xmin = xmax = elems.get(0).getX();
        ymin = ymax = elems.get(0).getY();

        for (int i = 1; i < size; i++) {
            PolygonElement elem = elems.get(i);
            if (xmin > elem.getX()) xmin = elem.getX();
            if (xmax < elem.getX()) xmax = elem.getX();
            if (ymin > elem.getY()) ymin = elem.getY();
            if (ymax < elem.getY()) ymax = elem.getY();
        }

        for (int y = ymin; y <= ymax; y++) {
            int left = xmin, right = xmax;
            int count = size - 1;

            for (int i = 0; i < size; count = i++) {
                if (elems.get(count).edge.a == 0) {
                    if (elems.get(count).getY() == y) {
                        if (elems.get(count).getX() < elems.get(i).getX()) {
                            left = elems.get(count).getX();
                            right = elems.get(i).getX();
                        } else {
                            right = elems.get(count).getX();
                            left = elems.get(i).getX();
                        }
                        break;
                    }
                } else {
                    double x = (-elems.get(count).edge.b * y - elems.get(count).edge.c) / (double) elems.get(count).edge.a;
                    if (elems.get(count).left) {
                        if (left < x) left = (int) x;
                    } else {
                        if (right > x) right = (int) x;
                    }
                }
            }
            if (left > right) continue;
            gl2.glVertex2f(left, y);
            gl2.glVertex2f(right, y);
        }
        gl2.glEnd();
    }

    private void updatePolygonState(List<PolygonElement> elems) {
        calculateEdgeCoef(elems);
        this.status = checkPolygonConvex(elems);
    }

    private void calculateEdgeCoef(List<PolygonElement> elems) {
        int size = elems.size();
        int idy = size - 1;
        for (int idx = 0; idx < size; idy = idx++) {
            PolygonElement elemY = elems.get(idy);
            PolygonElement elemX = elems.get(idx);
            IVector v1 = elemY.getPoint3DVector();
            IVector v2 = elemX.getPoint3DVector();

            Edge2D edge = new Edge2D(v1.nVectorProduct(v2));
            elemY.edge = edge;
            elemY.left = elemY.getY() < elemX.getY();
        }
    }

    private PolygonStatus checkPolygonConvex(List<PolygonElement> elems) {
        int above = 0, below = 0;
        int size = elems.size();
        if (size < 2) return null;
        int idy = size - 2;

        for (int idx = 0; idx < size; idx++, idy++) {
            if (idy >= size) idy = 0;
            IVector pointVector = elems.get(idx).getPoint3DVector();
            IVector edgeVector = elems.get(idy).getEdge3DVector();
            int r = (int) edgeVector.scalarProduct(pointVector);
            if (r > 0) above++;
            else if (r < 0) below++;
        }
        boolean convex = false;
        Orientation orientation = Orientation.UNKNOWN;

        if (below == 0) {
            elems.stream().forEach(elem -> elem.left = !elem.left);
            convex = true;
            orientation = Orientation.COUNTERCLOCKWISE;
        } else if (above == 0) {
            convex = true;
            orientation = Orientation.CLOCKWISE;
        }
        return new PolygonStatus(convex, orientation);
    }

    public boolean isPolygonConvex() {
        List<PolygonElement> tempElems = elems;
        if (currentPoint != null) {
            tempElems = new ArrayList<>(elems);
            tempElems.add(currentPoint);
        }
        calculateEdgeCoef(tempElems);
        PolygonStatus status = checkPolygonConvex(tempElems);
        return status != null ? status.convex : false;
    }

    public void setCurrentPoint(Point currentPoint) {
        updated = false;
        if (currentPoint != null) {
            this.currentPoint = new PolygonElement(currentPoint);
        } else {
            this.currentPoint = null;
        }
    }

    public void addNewPoint(Point point, boolean convex) {
        updated = false;
        if (!convex) {
            this.elems.add(new PolygonElement(point));
        } else if (isPolygonConvex()) {
            this.elems.add(new PolygonElement(point));
        } else {
            System.out.println("Dodavanjem danog vrha poligon ne bi bio konveksan");
        }
    }

    public PointPosition checkPointPosition(Point point) {
        if (!updated) {
            updatePolygonState(elems);
        }
        int above = 0, below = 0, on = 0;
        int size = elems.size();
        if (size < 2) return PointPosition.OUTSIDE;

        IVector pointVector = new Vector(point.x, point.y, 1);
        for (int idx = 0; idx < size; idx++) {
            IVector edgeVector = elems.get(idx).getEdge3DVector();
            int r = (int) edgeVector.scalarProduct(pointVector);
            if (r == 0) on++;
            else if (r > 0) above++;
            else below++;
        }

        if (on > 0) return PointPosition.ON;
        else if (above == 0 || below == 0) return PointPosition.INSIDE;
        else return PointPosition.OUTSIDE;
    }
}