package hr.fer.zemris.irg.zad7;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;
import hr.fer.zemris.irg.zad6.AngleController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zadatak7_1 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private ObjectModel model;
    private AngleController controller;

    public Zadatak7_1(ObjectModel model) {
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
        GLU glu = GLU.createGLU(gl2);
        glu.gluLookAt(controller.getX(), 4, controller.getZ(), 0, 0, 0, 0, 1, 0);

        gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        gl2.glEnable(GL2.GL_CULL_FACE);
        gl2.glCullFace(GL2.GL_BACK);
        renderScene(gl2, width, height);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1, 100);

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

    private void renderScene(GL2 gl2, int width, int height) {
        List<Vertex3D> vertices = model.getVertices();
        gl2.glColor3f(1, 0, 0);
        for (Face3D face3D : model.getFaces()) {
            gl2.glBegin(GL2.GL_POLYGON);
            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                gl2.glVertex3f((float) vertex.x, (float) vertex.y, (float) vertex.z);
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
            final JFrame frame = new Zadatak7_1(model);
            frame.setVisible(true);
        });
    }
}
