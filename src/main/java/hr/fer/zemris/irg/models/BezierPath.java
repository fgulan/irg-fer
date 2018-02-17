package hr.fer.zemris.irg.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class BezierPath {

    private static final int RADIUS = 20;

    private int selectedIndex = -1;
    private List<Point> points;

    public BezierPath() {
        this.points = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public void clearPoints() {
        points.clear();
    }

    public boolean containsPoint(Point point) {
        return points.contains(point);
    }

    public boolean removePoint(Point point) {
        return points.remove(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void selectPoint(int x, int y) {
        for (int i = 0, size = points.size(); i < size; i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < RADIUS && Math.abs(point.y - y) < RADIUS) {
                selectedIndex = i;
                return;
            }
        }
        selectedIndex = -1;
    }

    public void moveSelectedPoint(int x, int y) {
        if (selectedIndex < 0 || selectedIndex >= points.size()) {
            return;
        }

        points.set(selectedIndex, new Point(x, y));
    }

    public void deselectPoint() {
        selectedIndex = -1;
    }
}
