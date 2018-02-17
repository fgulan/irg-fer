package hr.fer.zemris.irg.zad9;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public interface ICullingWithShading {
    void renderScene(GL2 gl2, ObjectModel model, IMatrix matrix, IVector eye, Shading shading);

}
