package hr.fer.zemris.irg.zad4;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.interfaces.ISceneModel;
import hr.fer.zemris.irg.models.PointPosition;
import hr.fer.zemris.irg.models.Polygon;

import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 * @author Filip Gulan
 */
public class Zad4SceneModel implements ISceneModel {

    private Polygon polygon;
    private boolean drawingState;
    private boolean convexFlag;
    private boolean fillFlag;

    public Zad4SceneModel() {
        this.polygon = new Polygon();
        this.drawingState = true;
        this.convexFlag = false;
        this.fillFlag = false;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        key.consume();
        if (key.getKeyCode() == KeyEvent.VK_K) {
            setConvexFlag(!convexFlag);
        } else if (key.getKeyCode() == KeyEvent.VK_P) {
            setFillFlag(!fillFlag);
        } else if (key.getKeyCode() == KeyEvent.VK_N) {
            setDrawingState(!drawingState);
        }
    }

    @Override
    public void pointerMoved(Point point) {
        if (drawingState) {
            polygon.setCurrentPoint(point);
        } else {
            polygon.setCurrentPoint(null);
        }
    }

    @Override
    public void addPoint(Point point) {
        if (drawingState) {
            polygon.addNewPoint(point, convexFlag);
        } else {
            PointPosition position = polygon.checkPointPosition(point);
            switch (position) {
                case INSIDE:
                    System.out.println("(" + point.x + ", " + point.y + ")" +" je unutar poligona.");
                    break;
                case OUTSIDE:
                    System.out.println("(" + point.x + ", " + point.y + ")" +" je izvan poligona.");
                    break;
                case ON:
                    System.out.println("(" + point.x + ", " + point.y + ")" +"je na obodu poligona.");
                    break;
            }
        }
    }

    @Override
    public void drawElements(GL2 gl2, int width, int height) {
        if (convexFlag) {
            gl2.glClearColor(0, 1, 0, 1);
        } else {
            gl2.glClearColor(1, 1, 1, 1);
        }
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        if (fillFlag) {
            polygon.drawFilledElement(gl2);
        } else {
            polygon.drawElement(gl2);
        }
    }

    private void setFillFlag(boolean fillFlag) {
        if (drawingState) {
            this.fillFlag = fillFlag;
        } else {
            System.out.println("Trenunto niste u stanju iscrtavanja. Ignoriram postavljanje zastavice 'ispuna'.");
        }
    }

    private void setConvexFlag(boolean convexFlag) {
        if (!drawingState) {
            System.out.println("Trenunto niste u stanju iscrtavanja. Ignoriram postavljanje zastavice 'konveksan'.");
        } else if (convexFlag && polygon.isPolygonConvex()) {
            this.convexFlag = convexFlag;
        } else if (convexFlag) {
            System.out.println("Trenunti poligon nije konveksan. Ignoriram postavljanje zastavice 'konveksan'");
        } else {
            this.convexFlag = convexFlag;
        }
    }

    private void setDrawingState(boolean drawingState) {
        this.drawingState = drawingState;
        if (drawingState) {
            resetState();
        } else {
            convexFlag = false;
            fillFlag = false;
            polygon.setCurrentPoint(null);
        }
    }

    private void resetState() {
        polygon = new Polygon();
        convexFlag = false;
        fillFlag = false;
    }
}
