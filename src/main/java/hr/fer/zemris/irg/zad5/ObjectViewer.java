package hr.fer.zemris.irg.zad5;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * @author Filip Gulan
 */
public class ObjectViewer extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private int height;
    private int width;

    private ObjectModel object;

    public ObjectViewer(ObjectModel object) {
        this.object = object.normalize();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocation(100, 100);
        initGUI();
    }

    public void initGUI() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);

        glCanvas.addGLEventListener(this);

        getContentPane().add(glCanvas, BorderLayout.CENTER);
        glCanvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClearColor(1,1,1,0);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glColor3f(1, 0, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl2.glBegin(GL.GL_TRIANGLES);

        List<Vertex3D> vertices = object.getVertices();
        for (Face3D face : object.getFaces()) {
            Vertex3D first = vertices.get(face.elems[0]);
            Vertex3D second = vertices.get(face.elems[1]);
            Vertex3D third = vertices.get(face.elems[2]);

            gl2.glVertex3d(first.x, first.y, first.z);
            gl2.glVertex3d(second.x, second.y, second.z);
            gl2.glVertex3d(third.x, third.y, third.z);
        }

        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        this.height = height;
        this.width = width;
        gl2.glViewport(0, 0, width, height);
    }
}
