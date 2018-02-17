package hr.fer.zemris.irg.zad2;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Filip Gulan
 */
public class SwingJOGLExample {
    static {
        GLProfile.initSingleton();
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GLProfile glProfile = GLProfile.getDefault();
                GLCapabilities glCapabilities = new GLCapabilities(glProfile);
                final GLCanvas glCanvas = new GLCanvas(glCapabilities);

                setupMouseListenersForCanvas(glCanvas);
                setupKeyboardListenersForCanvas(glCanvas);
                setupGLEvenetListenersForCanvas(glCanvas);

                final JFrame jFrame = new JFrame("Test trokut");
                jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
                jFrame.setSize(640, 480);
                jFrame.setVisible(true);
                glCanvas.requestFocusInWindow();
            }
        });
    }

    public static void setupGLEvenetListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addGLEventListener(new GLEventListener() {
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

                gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                // Draw triangle filling the window
                gl2.glLoadIdentity();
                renderScene(gl2, width, height);
            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                GL2 gl2 = drawable.getGL().getGL2();
                gl2.glMatrixMode(GL2.GL_PROJECTION);
                gl2.glLoadIdentity();

                GLU gLu = new GLU();
                gLu.gluOrtho2D(0, width, 0, height);

                gl2.glMatrixMode(GL2.GL_MODELVIEW);
                gl2.glLoadIdentity();
                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    public static void setupKeyboardListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    e.consume();
                    glCanvas.display();
                }
            }
        });
    }

    public static void setupMouseListenersForCanvas(GLCanvas glCanvas) {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mis je kliknut na (x, y) = " +
                        "(" + e.getX() + ", " + e.getY() + ")");
                glCanvas.display();
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("Mis je pomaknut na (x, y) = " +
                        "(" + e.getX() + ", " + e.getY() + ")");
                glCanvas.display();
            }
        });
    }

    public static void renderScene(GL2 gl2, int width, int height) {
        gl2.glBegin(GL.GL_TRIANGLES);
        gl2.glColor3f(143/255.0f, 133/255.f, 0);
        gl2.glVertex2f(0, 0);
        gl2.glColor3f(0, 1, 0);
        gl2.glVertex2f(width, 0);
        gl2.glColor3f(0, 0 , 1);
        gl2.glVertex2f(width/2, height);
        gl2.glEnd();
    }
}
