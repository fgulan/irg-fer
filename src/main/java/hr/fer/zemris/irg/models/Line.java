package hr.fer.zemris.irg.models;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.algortihms.CohenSutherland;
import hr.fer.zemris.irg.algortihms.ILineClipper;
import hr.fer.zemris.irg.zad3.IDrawable;
import lombok.Getter;

import java.awt.Point;
import java.awt.Color;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Line implements IDrawable {

    private final int CONTROL_LINE_DISTANCE = 5;
    @Getter
    private Color color;
    @Getter
    private Point startPoint;
    @Getter
    private Point endPoint;

    private double angle;

    public Line(Color color, Point startPoint, Point endPoint) {
        this.color = color;
        this.endPoint = endPoint;
        this.startPoint = startPoint;
    }

    @Override
    public void drawElement(GL2 gl2, boolean control) {
        drawLine(gl2, startPoint, endPoint);
        if (control) {
            drawControlLine(gl2);
        }
    }

    @Override
    public void drawElementWithInterception(GL2 gl2, boolean control, int width, int height) {
        int xOffset = width / 4;
        int yOffset = height / 4;
        Point center = new Point(xOffset * 2, yOffset * 2);
        ILineClipper lineClipper = new CohenSutherland(center.x - xOffset, center.x + xOffset, center.y - yOffset, center.y + yOffset);
        List<Point> points = lineClipper.calculateInterceptionPoints(this);

        if (points.size() == 2) {
            drawLine(gl2, points.get(0), points.get(1));
        }
        if (control) {
            drawControlLine(gl2);
        }
    }

    private void drawLine(GL2 gl2, Point startPoint, Point endPoint) {
        gl2.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);

        if (startPoint.x <= endPoint.x) {
            if (startPoint.y <= endPoint.y) {
                drawWithBresenhamNormal(gl2, startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            } else {
                drawWithBresenhamInverse(gl2, startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            }
        } else {
            if (startPoint.y >= endPoint.y) {
                drawWithBresenhamNormal(gl2, endPoint.x, endPoint.y, startPoint.x, startPoint.y);
            } else {
                drawWithBresenhamInverse(gl2, endPoint.x, endPoint.y, startPoint.x, startPoint.y);
            }
        }
    }

    private void drawControlLine(GL2 gl2) {
        int deltaX = endPoint.x - startPoint.x;
        int deltaY = endPoint.y - startPoint.y;
        angle = Math.atan(deltaY/(double)deltaX);

        int xOffset = (int)(CONTROL_LINE_DISTANCE * Math.sin(angle));
        int yOffset = (int)(CONTROL_LINE_DISTANCE * Math.cos(angle));

        gl2.glColor3f(Color.RED.getRed()/255f, Color.RED.getGreen()/255f, Color.RED.getBlue()/255f);
        gl2.glBegin(GL.GL_LINES);

        // Biramo s koje strane crtamo kontrolnu liniju
        if (startPoint.x <= endPoint.x) {
            gl2.glVertex2f(startPoint.x + xOffset, startPoint.y - yOffset);
            gl2.glVertex2f(endPoint.x + xOffset, endPoint.y - yOffset);
        } else {
            gl2.glVertex2f(startPoint.x - xOffset, startPoint.y + yOffset);
            gl2.glVertex2f(endPoint.x - xOffset, endPoint.y + yOffset);
        }
        gl2.glEnd();
    }

    private void drawWithBresenhamNormal(GL2 gl2, int xS, int yS, int xE, int yE) {
        gl2.glBegin(GL.GL_POINTS);
        int yC, correction, a ,yF;
        int deltaX = xE - xS;
        int deltaY = yE - yS;

        if (deltaY <= deltaX) {
            a = 2*deltaY;
            yC = yS;
            yF = -deltaX;
            correction = -2*deltaX;

            for (int x = xS; x <= xE; x++) {
                gl2.glVertex2f(x, yC);
                yF += a;
                if (yF >= 0) {
                    yF += correction;
                    yC++;
                }
            }
        } else {
            int temp;
            temp = xE; xE = yE; yE = temp;
            temp = xS; xS = yS; yS = temp;
            a = 2*(yE - yS);
            yC = yS;
            yF = - (xE - xS);
            correction = 2*yF;
            for (int x = xS; x <= xE; x++) {
                gl2.glVertex2f(yC, x);
                yF += a;
                if (yF >= 0) {
                    yF += correction;
                    yC++;
                }
            }
        }
        gl2.glEnd();
    }

    private void drawWithBresenhamInverse(GL2 gl2, int xS, int yS, int xE, int yE) {
        gl2.glBegin(GL.GL_POINTS);
        int yC, correction, a ,yF;
        int deltaX = xE - xS;
        int deltaY = yE - yS;

        if (-deltaY <= deltaX) {
            a = 2*deltaY;
            yC = yS;
            yF = deltaX;
            correction = 2*deltaX;

            for (int x = xS; x <= xE; x++) {
                gl2.glVertex2f(x, yC);
                yF += a;
                if (yF <= 0) {
                    yF += correction;
                    yC--;
                }
            }
        } else {
            int temp;
            temp = xE; xE = yS; yS = temp;
            temp = xS; xS = yE; yE = temp;
            a = 2*(yE - yS);
            yC = yS;
            yF = xE - xS;
            correction = 2*yF;
            for (int x = xS; x <= xE; x++) {
                gl2.glVertex2f(yC, x);
                yF += a;
                if (yF <= 0) {
                    yF += correction;
                    yC--;
                }
            }
        }
        gl2.glEnd();
    }
}
