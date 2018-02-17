package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Plane {
    public double a;
    public double b;
    public double c;
    public double d;

    public Plane(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public IVector getNormal() {
        return new Vector(a, b, c);
    }

    @Override
    public String toString() {
        return "Plane{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }
}
