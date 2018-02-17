package hr.fer.zemris.irg.zad3;

import com.jogamp.opengl.GL2;

/**
 * @author Filip Gulan
 */
public interface IDrawable {
    public void drawElement(GL2 gl2, boolean control);
    public void drawElementWithInterception(GL2 gl2, boolean control, int width, int height);
}
