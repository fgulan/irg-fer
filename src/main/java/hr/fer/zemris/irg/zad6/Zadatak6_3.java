package hr.fer.zemris.irg.zad6;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;
import hr.fer.zemris.irg.utility.linear.IRG;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zadatak6_3 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private ObjectModel model;
    private AngleController controller;

    public Zadatak6_3(ObjectModel model) {
        this.model = model;
        this.controller = new AngleController(3, 4, 1, 10);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocation(100, 100);
        initGUI();
    }

    public void initGUI() {
        // Setup canvas
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);

        // Setup listeners
        setupMouseListenersForCanvas(glCanvas);
        setupKeyboardListenersForCanvas(glCanvas);
        glCanvas.addGLEventListener(this);

        // Add canvas to frame
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
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        gl2.glClearColor(0, 1, 0, 0);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        IVector eye = new Vector(controller.getX(), 4, controller.getZ());
        IVector center = new Vector(0, 0, 0);
        IVector viewUp = new Vector(0, 1, 0);
        IMatrix lookAtMatrix = IRG.lookAtMatrix(eye, center, viewUp);
        IMatrix frustumMatrix = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);

        renderScene(gl2, lookAtMatrix.nMultiply(frustumMatrix));
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

    private void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_L){
                    controller.increment();
                    glCanvas.display();
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    controller.decrement();
                    glCanvas.display();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.reset();
                    glCanvas.display();
                }
            }
        });
    }

    private void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private void renderScene(GL2 gl2, IMatrix transform) {
        List<Vertex3D> vertices = model.getVertices();
        gl2.glColor3f(1, 0, 0);
        for (Face3D face3D : model.getFaces()) {
            gl2.glBegin(GL2.GL_LINE_LOOP);
            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                IVector v = new Vector(vertex.x, vertex.y,  vertex.z, 1.0);
                IVector tv = v.toRowMatrix(false).nMultiply(transform).toVector(false).nFromHomogeneus();
                //System.out.println(tv);
                gl2.glVertex2f((float) tv.get(0), (float) tv.get(1));
            }
            gl2.glEnd();

        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide path to .obj file! Closing...");
            System.exit(1);
        }

        ObjectModel model = ObjectModel.readOBJFromFile(args[0]).normalize();

        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zadatak6_3(model);
            frame.setVisible(true);
        });
    }
}
