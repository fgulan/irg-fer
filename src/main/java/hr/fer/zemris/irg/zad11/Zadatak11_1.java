package hr.fer.zemris.irg.zad11;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

/**
 * @author Filip Gulan
 */
public class Zadatak11_1 extends JFrame implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    private int width;
    private int height;
    private double scaleFactor = 1/16.0;

    private int maxLimit = 512;
    private ZoomElements dArgs = new ZoomElements(1, -2, 1.2, -1.2);

    private Stack<ZoomElements> stack = new Stack<>();

    private boolean useCubic = false;
    private boolean useColor = false;

    public Zadatak11_1() {
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
        width = drawable.getSurfaceWidth();
        height = drawable.getSurfaceHeight();
        gl2.glClearColor(0, 1, 0, 1);
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
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    useColor = false;
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    useColor = true;
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    useCubic = false;
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    useCubic = true;
                } else if (e.getKeyCode() == KeyEvent.VK_X) {
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (!stack.isEmpty()) {
                        stack.clear();
                    }
                }
                glCanvas.display();
            }
        });
    }

    private void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateScreen(e.getX(), height - e.getY());
                glCanvas.display();
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private void updateScreen(int x, int y) {
        Complex c = getComplexFromScreen(x, y, width, height);

        ZoomElements elems = getArgs();

        double xw = (elems.uMax - elems.uMin);
        double xc = c.getRe();
        double yw = (elems.vMax - elems.vMin);
        double yc = c.getIm();
        ZoomElements newElems = new ZoomElements(
                xc + scaleFactor * xw / 2.0,
                xc - scaleFactor * xw / 2.0,
                yc + scaleFactor * yw / 2.0,
                yc - scaleFactor * yw / 2.0);
        stack.push(newElems);
    }

    private void renderScene(GL2 gl2, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int n;
                if (useCubic) {
                    n = divergenceTestCubic(getComplexFromScreen(x, y, width, height), maxLimit);
                } else {
                    n = divergenceTestSquare(getComplexFromScreen(x, y, width, height), maxLimit);
                }

                if (useColor) {
                    colorScheme(gl2, n);
                } else {
                    blackAndWhiteScheme(gl2, n);
                }
                gl2.glBegin(GL2.GL_POINTS);
                gl2.glVertex2f(x, y);
                gl2.glEnd();
            }
        }
    }

    private Complex getComplexFromScreen(int x, int y, int width, int height) {
        ZoomElements elems = getArgs();
        double u = (x / (float) width) * (elems.uMax - elems.uMin) + elems.uMin;
        double v = (y / (float) height) * (elems.vMax - elems.vMin) + elems.vMin;

        return new Complex(u, v);
    }

    private int divergenceTestSquare(Complex c, int limit) {
        Complex z = Complex.ZERO;
        for (int i = 1; i <= limit; i++) {
            z = z.multiply(z).add(c);
            if (z.module() > 4) return i;
        }
        return -1;
    }

    private int divergenceTestCubic(Complex c, int limit) {
        Complex z = Complex.ZERO;
        for (int i = 1; i <= limit; i++) {
            z = z.multiply(z).multiply(z).add(c);
            if (z.module() > 4) return i;
        }
        return -1;
    }

    void blackAndWhiteScheme(GL2 gl2, int n) {
        if (n == -1)
            gl2.glColor3f(0, 0, 0);
        else
            gl2.glColor3f(1, 1, 1);
    }

    private void colorScheme(GL2 gl2, int n) {
        if (n == -1) {
            gl2.glColor3f(0, 0, 0);
        } else if (maxLimit < 16) {
            int r = (int) ((n - 1) / (double) (maxLimit - 1) * 255 + 0.5);
            int g = 255 - r;
            int b = ((n - 1) % (maxLimit / 2)) * 255 / (maxLimit / 2);
            gl2.glColor3f(r / 255.f, g / 255.f, b / 255.f);
        } else {
            int lim = maxLimit < 32 ? maxLimit : 32;
            int r = (n - 1) * 255 / lim;
            int g = ((n - 1) % (lim / 4) * 255 / (lim / 4));
            int b = ((n - 1) % (lim / 8)) * 255 / (lim / 8);
            gl2.glColor3f(r / 255.f, g / 255.f, b / 255.f);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new Zadatak11_1();
            frame.setVisible(true);
        });
    }

    private class ZoomElements {
        public double uMax;
        public double uMin;
        public double vMax;
        public double vMin;

        public ZoomElements(double uMax, double uMin, double vMax, double vMin) {
            this.uMax = uMax;
            this.uMin = uMin;
            this.vMax = vMax;
            this.vMin = vMin;
        }
    }

    private ZoomElements getArgs() {
        if (stack.isEmpty()) {
            return dArgs;
        } else {
            return stack.peek();
        }
    }
}
