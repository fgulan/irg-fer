package hr.fer.zemris.irg.interfaces;

import com.jogamp.opengl.GL2;

/**
 * @author Filip Gulan
 */
public interface IDrawable {

    void drawElement(GL2 gl2);
    void drawFilledElement(GL2 gl2);

}
