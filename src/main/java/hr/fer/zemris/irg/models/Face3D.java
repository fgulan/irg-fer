package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Face3D {

    public int[] elems;
    @Getter
    @Setter
    private Plane plane;

    @Getter
    @Setter
    private boolean visible;

    public Face3D(int first, int second, int third) {
        elems = new int[3];
        elems[0] = first;
        elems[1] = second;
        elems[2] = third;
    }

    @Override
    public String toString() {
        return String.format("f %d %d %d", elems[0] + 1, elems[1] + 1, elems[2] + 1);
    }

    public Face3D copy() {
        return new Face3D(elems[0], elems[1], elems[2]);
    }

    public static Face3D instanceFromString(String input) {
        String[] elems = input.split("\\s+");
        int first = Integer.parseInt(elems[1]) - 1;
        int second = Integer.parseInt(elems[2]) - 1;
        int third = Integer.parseInt(elems[3]) - 1;
        return new Face3D(first, second, third);
    }

    public List<Vertex3D> getVertices(List<Vertex3D> allVertices) {
        List<Vertex3D> vertices = new ArrayList<>(3);
        vertices.add(allVertices.get(elems[0]));
        vertices.add(allVertices.get(elems[1]));
        vertices.add(allVertices.get(elems[2]));
        return vertices;
    }

    public IVector getCenter(List<Vertex3D> allVertices) {
        IVector v0 = allVertices.get(elems[0]).toVector();
        IVector v1 = allVertices.get(elems[1]).toVector();
        IVector v2 = allVertices.get(elems[2]).toVector();

        return v0.add(v1).add(v2).scalarMultiply(1.0/3.0);
    }
}
