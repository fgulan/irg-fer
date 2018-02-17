package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Filip Gulan
 */
public class ObjectModel {

    private static final double DELTA = 1e-12;
    @Getter
    private List<Vertex3D> vertices;
    @Getter
    private List<Face3D> faces;

    public ObjectModel(List<Vertex3D> vertices, List<Face3D> faces) {
        this.vertices = new ArrayList<>(vertices);
        this.faces = new ArrayList<>(faces);
        calculatePlaneCoef();
    }

    private void calculatePlaneCoef() {
        for (Face3D face : faces) {
            Vertex3D first = vertices.get(face.elems[0]);
            Vertex3D second = vertices.get(face.elems[1]);
            Vertex3D third = vertices.get(face.elems[2]);

            IVector v1 = new Vector(second.x - first.x, second.y - first.y, second.z - first.z);
            IVector v2 = new Vector(third.x - first.x, third.y - first.y, third.z - first.z);
            IVector normal = v1.nVectorProduct(v2);
            double a = normal.get(0);
            double b = normal.get(1);
            double c = normal.get(2);
            double d = -a * first.x - b * first.y - c * first.z;
            face.setPlane(new Plane(a, b, c, d));
        }
    }

    public ObjectModel copy() {
        List<Vertex3D> vertices = this.vertices.stream().map(vertex3D -> vertex3D.copy()).collect(Collectors.toList());
        List<Face3D> faces = this.faces.stream().map(face3D -> face3D.copy()).collect(Collectors.toList());
        return new ObjectModel(vertices, faces);
    }

    public String dumpToOBJ() {
        StringBuilder builder = new StringBuilder();
        for (Vertex3D vertice : vertices) {
            builder.append(vertice + System.lineSeparator());
        }
        for (Face3D face : faces) {
            builder.append(face + System.lineSeparator());
        }
        return builder.toString();
    }

    public ObjectModel normalize() {
        if (faces.size() == 0) return this;
        double xmin, xmax, ymin, ymax, zmin, zmax;
        xmin = xmax = vertices.get(faces.get(0).elems[0]).x;
        ymin = ymax = vertices.get(faces.get(0).elems[0]).y;
        zmin = zmax = vertices.get(faces.get(0).elems[0]).z;

        for (Face3D face : faces) {
            for (int i : face.elems) {
                xmin = Math.min(xmin, vertices.get(i).x);
                xmax = Math.max(xmax, vertices.get(i).x);
                ymin = Math.min(ymin, vertices.get(i).y);
                ymax = Math.max(ymax, vertices.get(i).y);
                zmin = Math.min(zmin, vertices.get(i).z);
                zmax = Math.max(zmax, vertices.get(i).z);
            }
        }

        double centerX = (xmax + xmin) / 2;
        double centerY = (ymax + ymin) / 2;
        double centerZ = (zmax + zmin) / 2;

        double M = Math.max(xmax - xmin, ymax - ymin);
        M = Math.max(M, zmax - zmin);

        for (Vertex3D vertex : vertices) {
            vertex.x = (vertex.x - centerX) * 2 / M;
            vertex.y = (vertex.y - centerY) * 2 / M;
            vertex.z = (vertex.z - centerZ) * 2 / M;
        }
        calculatePlaneCoef();
        calculateVertexNormal();
        return this;
    }

    public PointPosition checkPointPosition(Vertex3D vertex) {
        int on = 0;
        for (Face3D face : faces) {
            Plane plane = face.getPlane();
            double status = plane.a * vertex.x + plane.b * vertex.y + plane.c * vertex.z + plane.d;

            if (Math.abs(status) < DELTA) {
                on++;
            } else if (status > 0) {
                return PointPosition.OUTSIDE;
            }
        }
        if (on > 0) {
            return PointPosition.ON;
        } else {
            return PointPosition.INSIDE;
        }
    }

    private void calculateVertexNormal() {
        for (Vertex3D vertice : vertices) {
            IVector normal = new Vector(0, 0, 0);
            int counter = 0;
            for (Face3D face : faces) {
                for (Vertex3D faceVertice : face.getVertices(vertices)) {
                    if (faceVertice.equals(vertice)) {
                        counter++;
                        normal.add(face.getPlane().getNormal().nNormalize());
                        break;
                    }
                }
            }
            normal.nScalarMultiply(1.0/counter).normalize();
            vertice.setNormal(normal);
        }
    }

    public static ObjectModel readOBJFromFile(String fileName) {
        List<Vertex3D> vertices = new ArrayList<>();
        List<Face3D> faces = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                line = line.trim();
                if (line.startsWith("v")) {
                    vertices.add(Vertex3D.instanceFromString(line));
                } else if (line.startsWith("f")) {
                    faces.add(Face3D.instanceFromString(line));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ObjectModel(vertices, faces);
    }

    public void determineFaceVisibilities1(IVector eye) {
        for (Face3D face : faces) {
            Plane plane = face.getPlane();
            double status = plane.a * eye.get(0) + plane.b * eye.get(1) + plane.c * eye.get(2) + plane.d;
            if (status > 0) {
                face.setVisible(true);
            } else {
                face.setVisible(false);
            }
        }
    }

    public void determineFaceVisibilities2(IVector eye) {
        for (Face3D face : faces) {
            IVector centerVector = new Vector(0, 0, 0);
            for (int index : face.elems) {
                centerVector.add(vertices.get(index).toVector());
            }
            centerVector.scalarMultiply(1.0/3.0);

            IVector ray = eye.nSub(centerVector);
            IVector normal = face.getPlane().getNormal();
            if (ray.scalarProduct(normal) > 0) {
                face.setVisible(true);
            } else {
                face.setVisible(false);
            }
        }
    }
}