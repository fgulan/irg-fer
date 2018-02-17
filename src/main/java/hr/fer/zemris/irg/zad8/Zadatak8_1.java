package hr.fer.zemris.irg.zad8;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.models.BezierPath;
import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zadatak8_1 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private static final int DIVS = 200;
    private BezierPath bezier;

    public Zadatak8_1() {
        bezier = new BezierPath();
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
        gl2.glLoadIdentity();

        renderScene(gl2, width, height);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, height, 0.0f);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

    private void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    bezier.clearPoints();
                    glCanvas.display();
                }
            }
        });
    }

    private void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    bezier.addPoint(e.getX(), e.getY());
                    glCanvas.display();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    bezier.selectPoint(e.getX(), e.getY());
                    glCanvas.display();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    bezier.deselectPoint();
                    glCanvas.display();

                }
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                bezier.moveSelectedPoint(e.getX(), e.getY());
                glCanvas.display();
            }
        });

    }

    private void renderScene(GL2 gl2, int width, int height) {
        drawPolygon(gl2, bezier.getPoints());
        drawPath(gl2, bezier.getPoints());

        List<Point> interpolatedPoints = interpolate(bezier.getPoints());
        drawPath(gl2, interpolatedPoints);
    }

    private void drawPath(GL2 gl2, List<Point> points) {
        if (points == null || points.isEmpty()) {
            return;
        }
        gl2.glColor3f(0, 0, 1);

        int n = points.size() - 1;
        List<Integer> factors = computeFactors(n);

        gl2.glBegin(GL2.GL_LINE_STRIP);
        double t, b;
        for (int i = 0; i <= DIVS; i++) {
            t = 1.0 / DIVS * i;
            Point point = new Point();

            for (int j = 0; j <= n; j++) {
                if (j == 0) {
                    b = factors.get(j) * Math.pow(1 - t, n);
                } else if (j == n) {
                    b = factors.get(j) * Math.pow(t, n);
                } else {
                    b = factors.get(j) * Math.pow(1 - t, n - j) * Math.pow(t, j);
                }
                point.x += b * points.get(j).x;
                point.y += b * points.get(j).y;
            }
            gl2.glVertex2f(point.x, point.y);
        }
        gl2.glEnd();
    }

    private List<Integer> computeFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        int a = 1;
        for (int i = 1; i <= n + 1 ; i++) {
            factors.add(a);
            a = a * (n - i + 1) / i;
        }
        return factors;
    }

    private void drawPolygon(GL2 gl2, List<Point> points) {
        gl2.glColor3f(1, 0, 0);
        gl2.glBegin(GL2.GL_LINE_STRIP);
        for (Point point : points) {
            gl2.glVertex2f(point.x, point.y);
        }
        gl2.glEnd();
    }

    private List<Point> interpolate(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }
        int n = points.size() - 1;

        double[][] p = new double[points.size()][2];
        for (int i = 0, size = n + 1; i < size; i++) {
            p[i][0] = points.get(i).x;
            p[i][1] = points.get(i).y;
        }
        IMatrix P = new Matrix(n + 1, 2, p, true);
        List<Integer> factors = computeFactors(n);

        double[][] b = new double[n + 1][n + 1];
        double t;
        for (int i = 0; i <= n; i++) {
            t = 1.0 / n * i;
            for (int j = 0; j <= n; j++) {
                if (j == 0) {
                    b[i][j] = factors.get(j) * Math.pow(1 - t, n);
                } else if (j == n) {
                    b[i][j] = factors.get(j) * Math.pow(t, n);
                } else {
                    b[i][j] = factors.get(j) * Math.pow(1 - t, n - j) * Math.pow(t, j);
                }
            }
        }

        IMatrix B = new Matrix(n + 1, n + 1, b, true);
        IMatrix R = B.nInvert().nMultiply(P);

        List<Point> polygonPoints = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            polygonPoints.add(new Point((int) R.get(i, 0), (int) R.get(i, 1)));
        }
        return polygonPoints;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zadatak8_1();
            frame.setVisible(true);
        });
    }
}
