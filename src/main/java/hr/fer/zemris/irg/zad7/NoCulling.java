package hr.fer.zemris.irg.zad7;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.util.List;

/**
 * @author Filip Gulan
 */
public class NoCulling implements ICulling {

    @Override
    public void renderScene(GL2 gl2, ObjectModel model, IMatrix matrix, IVector eye) {
        List<Vertex3D> vertices = model.getVertices();
        gl2.glColor3f(1, 0, 0);
        for (Face3D face3D : model.getFaces()) {
            gl2.glBegin(GL2.GL_LINE_LOOP);
            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                IVector v = new Vector(vertex.x, vertex.y,  vertex.z, 1.0);
                IVector tv = v.toRowMatrix(false).nMultiply(matrix).toVector(false).nFromHomogeneus();
                gl2.glVertex2f((float) tv.get(0), (float) tv.get(1));
            }
            gl2.glEnd();
        }
    }
}
