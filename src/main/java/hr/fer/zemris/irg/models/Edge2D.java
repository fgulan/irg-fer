package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Edge2D {
    public int a;
    public int b;
    public int c;

    public Edge2D(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Edge2D(IVector coefVector) {
        a = (int) coefVector.get(0);
        b = (int) coefVector.get(1);
        c = (int) coefVector.get(2);
    }

    public IVector vector3D() {
        return new Vector(a, b, c);
    }
}
