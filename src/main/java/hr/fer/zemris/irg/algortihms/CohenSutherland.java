package hr.fer.zemris.irg.algortihms;

import hr.fer.zemris.irg.models.Line;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class CohenSutherland implements ILineClipper {

    private static final int INSIDE = 0x0;
    private static final int LEFT   = 0x1;
    private static final int RIGHT  = 0x2;
    private static final int BOTTOM = 0x4;
    private static final int TOP    = 0x8;

    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    public CohenSutherland(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public List<Point> calculateInterceptionPoints(Line line) {
        Point start = new Point(line.getStartPoint());
        Point end = new Point(line.getEndPoint());
        int startCode = getInterceptionCode(start);
        int endCode = getInterceptionCode(end);

        boolean found = false;
        while (true) {
            if ((startCode & endCode) != 0) {
                break;
            } else if ((startCode | endCode) == 0) {
                found = true;
                break;
            } else {
                Point point = new Point();
                int code = (startCode != 0) ? startCode : endCode;

                if ((code & TOP) != 0) {
                    point.x = start.x + (end.x - start.x) * (yMax - start.y) / (end.y - start.y);
                    point.y = yMax;
                } else if ((code & RIGHT) != 0) {
                    point.y = start.y + (end.y - start.y) * (xMax - start.x) / (end.x - start.x);
                    point.x = xMax;
                } else if ((code & BOTTOM) != 0) {
                    point.x = start.x + (end.x - start.x) * (yMin - start.y) / (end.y - start.y);
                    point.y = yMin;
                } else if ((code & LEFT) != 0) {
                    point.y = start.y + (end.y - start.y) * (xMin - start.x) / (end.x - start.x);
                    point.x = xMin;
                }


                if (code == startCode) {
                    start = point;
                    startCode = getInterceptionCode(start);
                } else {
                    end = point;
                    endCode = getInterceptionCode(end);
                }
            }
        }
        List<Point> points = new ArrayList<>();
        if (found) {
            points.add(start);
            points.add(end);
        }
        return points;
    }

    private int getInterceptionCode(Point point) {
        int code = 0;

        if (point.x < xMin) {
            code |= LEFT;
        } else if (point.x > xMax) {
            code |= RIGHT;
        }

        if (point.y < yMin) {
            code |= BOTTOM;
        } else if (point.y > yMax) {
            code |= TOP;
        }

        return code;
    }
}
