package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Vertex3D {

    public double x;
    public double y;
    public double z;

    private IVector normal;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IVector getNormal() {
        return normal;
    }

    public void setNormal(IVector normal) {
        this.normal = normal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex3D vertex3D = (Vertex3D) o;

        if (Double.compare(vertex3D.x, x) != 0) return false;
        if (Double.compare(vertex3D.y, y) != 0) return false;
        return Double.compare(vertex3D.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("v %.3f %.3f %.3f", x, y, z);
    }

    public Vertex3D copy() {
        Vertex3D copied = new Vertex3D(x, y, z);
        copied.setNormal(getNormal().copy());
        return copied;
    }

    public IVector toVector() {
        return new Vector(x, y, z);
    }

    public static Vertex3D instanceFromString(String input) {
        String[] elems = input.split("\\s+");
        double x = Double.parseDouble(elems[1]);
        double y = Double.parseDouble(elems[2]);
        double z = Double.parseDouble(elems[3]);
        return new Vertex3D(x, y, z);
    }
}
