package hr.fer.zemris.irg.zad9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.utility.linear.IRG;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;
import hr.fer.zemris.irg.zad6.AngleController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Filip Gulan
 */
public class Zadatak9_2 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private ICullingWithShading culling;
    private ObjectModel model;
    private AngleController controller;

    private boolean depthTest = true;
    private boolean useGourard = true;

    public Zadatak9_2(ObjectModel model) {
        this.model = model;
        this.controller = new AngleController(3, 4, 1, 5);
        this.culling = new NoCulling();
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


        gl2.glClearColor(0, 1, 0, 0);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (depthTest) {
            gl2.glEnable(GL.GL_DEPTH_TEST);
        } else {
            gl2.glDisable(GL.GL_DEPTH_TEST);
        }

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        IVector eye = new Vector(controller.getX(), controller.getY(), controller.getZ());
        IVector center = new Vector(0, 0, 0);
        IVector viewUp = new Vector(0, 1, 0);
        IMatrix lookAtMatrix = IRG.lookAtMatrix(eye, center, viewUp);
        IMatrix frustumMatrix = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);

        gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl2.glDisable(GL2.GL_CULL_FACE);
        gl2.glDisable(GL2.GL_LIGHTING);
        renderScene(gl2, lookAtMatrix.nMultiply(frustumMatrix), eye);
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
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    controller.decrement();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.reset();
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    culling = new NoCulling();
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    culling = new CullingAlgorithm1();
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    culling = new CullingAlgorithm2();
                } else if (e.getKeyCode() == KeyEvent.VK_4) {
                    culling = new CullingAlgorithm3();
                } else if (e.getKeyCode() == KeyEvent.VK_U) {
                    controller.incrementY();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    controller.decrementY();
                }  else if (e.getKeyCode() == KeyEvent.VK_K) {
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

    private void renderScene(GL2 gl2, IMatrix matrix, IVector eye) {
        culling.renderScene(gl2, model, matrix, eye, useGourard ? Shading.GOURARD : Shading.CONSTANT);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide path to .obj file! Closing...");
            System.exit(1);
        }

        ObjectModel model = ObjectModel.readOBJFromFile(args[0]).normalize();

        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zadatak9_2(model);
            frame.setVisible(true);
        });
    }
}
