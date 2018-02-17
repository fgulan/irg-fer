package hr.fer.zemris.irg.models;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.zad3.IDrawable;

import java.awt.*;

/**
 * @author Filip Gulan
 */
public class InterceptionBounds implements IDrawable {

    @Override
    public void drawElement(GL2 gl2, boolean control) {

    }

    @Override
    public void drawElementWithInterception(GL2 gl2, boolean control, int width, int height) {
        int newWidth = width / 2;
        int newHeight = height / 2;

        int xOffset = newWidth / 2 + 1;
        int yOffset = newHeight / 2 + 1;

        Point center = new Point(newWidth, newHeight);
        Point upperLeft = new Point(center.x - xOffset, center.y + yOffset);
        Point lowerLeft = new Point(center.x - xOffset, center.y - yOffset);
        Point lowerRight = new Point(center.x + xOffset, center.y - yOffset);
        Point upperRight = new Point(center.x + xOffset, center.y + yOffset);

        gl2.glColor3f(0, 1, 0);
        gl2.glBegin(GL.GL_LINE_LOOP);
        gl2.glVertex2f(upperLeft.x, upperLeft.y);
        gl2.glVertex2f(lowerLeft.x, lowerLeft.y);
        gl2.glVertex2f(lowerRight.x, lowerRight.y);
        gl2.glVertex2f(upperRight.x, upperRight.y);
        gl2.glEnd();
    }
}
