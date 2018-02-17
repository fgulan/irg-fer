package hr.fer.zemris.irg.interfaces;

import com.jogamp.opengl.GL2;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Filip Gulan
 */
public interface ISceneModel {
    void pointerMoved(Point point);
    void addPoint(Point point);
    void keyPressed(KeyEvent key);
    void drawElements(GL2 gl2, int width, int height);
}
