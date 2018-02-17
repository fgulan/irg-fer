package hr.fer.zemris.irg.zad9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.models.Face3D;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.Vertex3D;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;
import hr.fer.zemris.irg.zad6.AngleController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zadatak9_1 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private ObjectModel model;
    private AngleController controller;
    private boolean depthTest = true;
    private boolean useGourard = true;

    public Zadatak9_1(ObjectModel model) {
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
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (depthTest) {
            gl2.glEnable(GL.GL_DEPTH_TEST);
        } else {
            gl2.glDisable(GL.GL_DEPTH_TEST);
        }

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        GLU glu = GLU.createGLU(gl2);
        glu.gluLookAt(controller.getX(), controller.getY(), controller.getZ(), 0, 0, 0, 0, 1, 0);

        gl2.glEnable(GLLightingFunc.GL_LIGHTING);
        gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0, 0, 0, 1}, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {4, 5, 3, 1}, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.2f, 0.2f, 0.2f, 1}, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.8f, 0.8f, 0, 1}, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {0, 0, 0, 1}, 0);
        gl2.glEnable(GL2.GL_LIGHT0);


        gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
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
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    controller.increment();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    controller.decrement();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.reset();
                } else if (e.getKeyCode() == KeyEvent.VK_U) {
                    controller.incrementY();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    controller.decrementY();
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    useGourard = false;
                } else if (e.getKeyCode() == KeyEvent.VK_G) {
                    useGourard = true;
                } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                    depthTest = !depthTest;
                    if (depthTest) {
                        System.out.println("Koristim z-spremnik");
                    } else {
                        System.out.println("Gasim z-spremnik");
                    }
                } else {
                    return;
                }
                glCanvas.display();
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
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[] {1, 1, 1, 1}, 0);
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {1, 1, 1, 1}, 0);
        gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {0.01f, 0.01f, 0.01f, 1}, 0);
        gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);
        gl2.glShadeModel(useGourard ? GL2.GL_SMOOTH : GL2.GL_FLAT);

        List<Vertex3D> vertices = model.getVertices();
        for (Face3D face3D : model.getFaces()) {
            gl2.glBegin(GL2.GL_POLYGON);
            for (int elem : face3D.elems) {
                Vertex3D vertex = vertices.get(elem);
                IVector normal;
                if (useGourard) {
                    normal = vertex.getNormal().normalize();
                } else {
                    normal = face3D.getPlane().getNormal().normalize();
                }
                gl2.glNormal3dv(normal.toArray(), 0);
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
            final JFrame frame = new Zadatak9_1(model);
            frame.setVisible(true);
        });
    }
}
