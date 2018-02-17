package hr.fer.zemris.irg.zad9;

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
public class CullingAlgorithm1 extends AbstractCullingWithShading {

    @Override
    public void renderScene(GL2 gl2, ObjectModel model, IMatrix matrix, IVector eye, Shading shading) {
        model.determineFaceVisibilities1(eye);

        List<Vertex3D> vertices = model.getVertices();
        for (Face3D face3D : model.getFaces()) {
            if (!face3D.isVisible()) {
                continue;
            }

            if (shading == Shading.CONSTANT) {
                shade(gl2, face3D.getPlane().getNormal(), face3D.getCenter(vertices), eye);
            }

            gl2.glBegin(GL2.GL_POLYGON);
            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                IVector v = new Vector(vertex.x, vertex.y,  vertex.z, 1.0);
                IVector tv = v.toRowMatrix(false).nMultiply(matrix).toVector(false).nFromHomogeneus();

                if (shading == Shading.GOURARD) {
                    shade(gl2, vertex.getNormal(), vertex.toVector(), eye);
                }
                gl2.glVertex3f((float) tv.get(0), (float) tv.get(1), (float) tv.get(2));
            }
            gl2.glEnd();
        }
    }
}
