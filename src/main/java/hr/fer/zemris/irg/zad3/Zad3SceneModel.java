package hr.fer.zemris.irg.zad3;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.models.InterceptionBounds;
import hr.fer.zemris.irg.models.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zad3SceneModel {

    private Point currentPoint;
    private List<IDrawable> sceneElements;
    private InterceptionBounds bounds;

    private boolean control;
    private boolean interception;

    public Zad3SceneModel() {
        this.sceneElements = new ArrayList<>();
        this.bounds = new InterceptionBounds();
    }

    public void addPoint(Point newPoint) {
        if (currentPoint != null) {
            Line line = new Line(Color.BLACK, currentPoint, newPoint);
            sceneElements.add(line);
            currentPoint = null;
        } else {
            currentPoint = newPoint;
        }
    }

    public void drawElements(GL2 gl2, int width, int height) {
        for (IDrawable element : sceneElements) {
            if (interception) {
                element.drawElementWithInterception(gl2, control, width, height);
            } else {
                element.drawElement(gl2, control);
            }
        }

    }

    public void toggleControl() {
        control = !control;
    }

    public void toggleInterception() {
        interception = !interception;
        if (interception) {
            sceneElements.add(0, bounds);
        } else {
            sceneElements.remove(bounds);
        }
    }
}
