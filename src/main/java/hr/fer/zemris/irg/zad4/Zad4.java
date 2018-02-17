package hr.fer.zemris.irg.zad4;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.interfaces.ISceneModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Filip Gulan
 */
public class Zad4 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private ISceneModel scene;
    private int height;

    public Zad4() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocation(100, 100);
        scene = new Zad4SceneModel();
        initGUI();
    }

    public void initGUI() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);
        setupMouseListenersForCanvas(glCanvas);
        setupKeyboardListenersForCanvas(glCanvas);
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
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();
        gl2.glClearColor(1, 1, 1, 1);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl2.glLoadIdentity();
        renderScene(gl2, width, height);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        this.height = height;

        GLU gLu = new GLU();
        gLu.gluOrtho2D(0, width, 0, height);
        gl2.glViewport(0, 0, width, height);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
    }

    private void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                scene.keyPressed(e);
                glCanvas.display();
            }
        });
    }

    private void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scene.addPoint(new Point(e.getX(), height - e.getY()));
                glCanvas.display();
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                scene.pointerMoved(new Point(e.getX(), height - e.getY()));
                glCanvas.display();
            }
        });
    }

    private void renderScene(GL2 gl2, int width, int height) {
        scene.drawElements(gl2, width, height);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zad4();
            frame.setVisible(true);
        });
    }
}
