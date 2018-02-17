package hr.fer.zemris.irg.zad11;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

/**
 * @author Filip Gulan
 */
public class Zadatak11_2 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private int width;
    private int height;

    public Zadatak11_2() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(100, 100);
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
        width = drawable.getSurfaceWidth();
        height = drawable.getSurfaceHeight();
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

        GLU gLu = new GLU();
        gLu.gluOrtho2D(0, width - 1, 0, height - 1);
        gl2.glViewport(0, 0, width, height);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
    }

    private void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
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

    private int round(double d) {
        if (d >= 0) return (int) (d + 0.5);
        return (int) (d - 0.5);
    }

    private void renderScene(GL2 gl2, int width, int height) {
        IFSFractal fractal = IFSFractal.readFromFile(Paths.get("ifs2.txt"));
        int limit = fractal.getLimit();
        int numOfPoints = fractal.getPointsNumber();
        gl2.glPointSize(1);
        gl2.glColor3f(0, 0.7f, 0.3f);
        gl2.glBegin(GL2.GL_POINTS);
        double x0, y0;
        for (int counter = 0; counter < numOfPoints; counter++) {
            x0 = 0;
            y0 = 0;

            for (int iter = 0; iter < limit; iter++) {
                double x, y;
                double p = Math.random();
                x = fractal.getX(x0, y0, p);
                y = fractal.getY(x0, y0, p);
                x0 = x;
                y0 = y;
            }
            gl2.glVertex2i(round(x0 * fractal.getEta1() + fractal.getEta2()), round(y0 * fractal.getEta3() + fractal.getEta4()));
        }
        gl2.glEnd();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zadatak11_2();
            frame.setVisible(true);
        });
    }
}
