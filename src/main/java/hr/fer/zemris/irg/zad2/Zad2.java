package hr.fer.zemris.irg.zad2;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.models.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Filip Gulan
 */
public class Zad2 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private static final int RECTANGLE_SIZE = 10;
    private Zad2SceneModel scene;
    private int height;

    public Zad2() {
        this.scene = new Zad2SceneModel();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
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
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();
        gl2.glClearColor(1,1,1,1);
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
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glViewport(0, 0, width, height);
    }

    private void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    e.consume();
                    scene.getNextColor();
                    glCanvas.display();
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    e.consume();
                    scene.getPreviousColor();
                    glCanvas.display();
                }
            }
        });
    }

    private void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = new Point(e.getX(), -(e.getY() - height));
                scene.addPoint(point);
                glCanvas.display();
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                scene.setMousePointer(new Point(e.getX(), -(e.getY() - height)));
                glCanvas.display();
            }
        });
    }

    private void renderScene(GL2 gl2, int width, int height) {
        Color color = scene.getSelectedColor();
        gl2.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex2f(width - RECTANGLE_SIZE, height);
        gl2.glVertex2f(width - RECTANGLE_SIZE, height - RECTANGLE_SIZE);
        gl2.glVertex2f(width, height - RECTANGLE_SIZE);
        gl2.glVertex2f(width, height);
        gl2.glEnd();

        for (Triangle triangle : scene.getTriangles()) {
            color = triangle.getColor();
            gl2.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
            gl2.glBegin(GL.GL_TRIANGLES);
            gl2.glVertex2f(triangle.getFirstPoint().x, triangle.getFirstPoint().y);
            gl2.glVertex2f(triangle.getSecondPoint().x, triangle.getSecondPoint().y);
            gl2.glVertex2f(triangle.getThirdPoint().x, triangle.getThirdPoint().y);
            gl2.glEnd();
        }

        color = scene.getSelectedColor();
        gl2.glColor3f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);

        if (scene.getPoints().size() == 1 && scene.getMousePointer() != null) {
            gl2.glBegin(GL.GL_LINES);
            Point point = scene.getPoints().get(0);
            gl2.glVertex2f(point.x, point.y);
            gl2.glVertex2f(scene.getMousePointer().x, scene.getMousePointer().y);
            gl2.glEnd();
        } else if (scene.getPoints().size() == 2 && scene.getMousePointer() != null) {
            gl2.glBegin(GL.GL_TRIANGLES);
            Point firstPoint = scene.getPoints().get(0);
            Point secondPoint = scene.getPoints().get(1);
            gl2.glVertex2f(firstPoint.x, firstPoint.y);
            gl2.glVertex2f(secondPoint.x, secondPoint.y);
            gl2.glVertex2f(scene.getMousePointer().x, scene.getMousePointer().y);
            gl2.glEnd();

            gl2.glBegin(GL.GL_LINES);
            gl2.glVertex2f(firstPoint.x, firstPoint.y);
            gl2.glVertex2f(secondPoint.x, secondPoint.y);
            gl2.glEnd();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zad2();
            frame.setVisible(true);
        });
    }

}
