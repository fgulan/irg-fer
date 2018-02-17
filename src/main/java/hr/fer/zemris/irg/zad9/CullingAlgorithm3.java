package hr.fer.zemris.irg.zad9;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;
import hr.fer.zemris.irg.utility.linear.IRG;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class CullingAlgorithm3 extends AbstractCullingWithShading {

    @Override
    public void renderScene(GL2 gl2, ObjectModel model, IMatrix matrix, IVector eye, Shading shading) {
        List<Vertex3D> vertices = model.getVertices();
        gl2.glColor3f(1, 0, 0);
        for (Face3D face3D : model.getFaces()) {
            List<VectorTuple> tPoints = new ArrayList<>(3);

            if (shading == Shading.CONSTANT) {
                shade(gl2, face3D.getPlane().getNormal(), face3D.getCenter(vertices), eye);
            }

            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                IVector v = new Vector(vertex.x, vertex.y,  vertex.z, 1.0);
                IVector tv = v.toRowMatrix(false).nMultiply(matrix).toVector(false).nFromHomogeneus();
                VectorTuple tp = new VectorTuple(tv, vertex.getNormal().normalize());
                tp.vertex = vertex;
                tPoints.add(tp);
            }

            boolean isAntiClockwise = IRG.isAntiClockwise(tPoints.get(0).vector, tPoints.get(1).vector, tPoints.get(2).vector);
            if (!isAntiClockwise) {
                continue;
            }
            gl2.glBegin(GL2.GL_POLYGON);
            for (VectorTuple tuple : tPoints) {
                if (shading == Shading.GOURARD) {
                    shade(gl2, tuple.vertex.getNormal(), tuple.vertex.toVector(), eye);
                }
                gl2.glVertex3f((float) tuple.vector.get(0), (float) tuple.vector.get(1), (float) tuple.vector.get(2));
            }
            gl2.glEnd();
        }
    }

    private class VectorTuple {
        public IVector vector;
        public IVector normal;
        public Vertex3D vertex;

        public VectorTuple(IVector vector, IVector normal) {
            this.vector = vector;
            this.normal = normal;
        }
    }
}
