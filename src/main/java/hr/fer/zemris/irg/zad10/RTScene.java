package hr.fer.zemris.irg.zad10;

import hr.fer.zemris.irg.models.Light;
import hr.fer.zemris.irg.models.Patch;
import hr.fer.zemris.irg.models.SceneObject;
import hr.fer.zemris.irg.models.Sphere;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class RTScene {

    private IVector eye;
    private IVector view;
    private IVector viewUp;
    private double h;
    private double xAngle;
    private double yAngle;
    private double[] gaIntensity = new double[] {0,0,0};
    private List<Light> sources;
    private List<SceneObject> objects;

    private double l;
    private double r;
    private double b;
    private double t;
    private IVector xAxis;
    private IVector yAxis;

    public RTScene() {
        sources = new ArrayList<>();
        objects = new ArrayList<>();
    }

    private void computeKS() {

    }

    public static RTScene loadScene(Path path) {
        List<String> lines = null;
        try {
            lines = removeComments(Files.readAllLines(path));
        } catch (IOException e) {
            return null;
        }

        RTScene scene = new RTScene();
        for (String line : lines) {
            if (line.startsWith("e")) {
                scene.eye = Vector.parseSimple(line.replaceFirst("e", ""));
            } else if (line.startsWith("v")) {
                scene.view = Vector.parseSimple(line.replaceFirst("v", ""));
            } else if (line.startsWith("vu")) {
                scene.viewUp = Vector.parseSimple(line.replaceFirst("vu", ""));
            } else if (line.startsWith("h")) {
                scene.h = Double.parseDouble(line.replaceFirst("h", "").trim());
            } else if (line.startsWith("xa")) {
                scene.xAngle = Double.parseDouble(line.replaceFirst("xa", "").trim());
            } else if (line.startsWith("ya")) {
                scene.yAngle = Double.parseDouble(line.replaceFirst("ya", "").trim());
            } else if (line.startsWith("ga")) {
                setupGaIntensity(line.replaceFirst("ga", "").trim(), scene);
            } else if (line.startsWith("i")) {
                setupLights(line.replaceFirst("i", "").trim(), scene);
            } else if (line.startsWith("o")) {
                setupObjects(line.replaceFirst("o", "").trim(), scene);
            }
        }
        scene.computeKS();
        return scene;
    }

    private static void setupObjects(String input, RTScene scene) {
        if (input.startsWith("s")) {
            setupSphere(input.replaceFirst("s", "").trim(), scene);
        } else if (input.startsWith("p")) {
            setupPatch(input.replaceFirst("p", "").trim(), scene);
        }
    }

    private static void setupPatch(String input, RTScene scene) {
        String[] fields = input.split("\\s+");
        if (fields.length != 33) {
            System.out.println("Invalid patch!");
            return;
        }
        double[] elems = parseToDouble(fields);

        IVector center = new Vector(Arrays.copyOfRange(elems, 0, 3));
        IVector v1 = new Vector(Arrays.copyOfRange(elems, 3, 6));
        IVector v2 = new Vector(Arrays.copyOfRange(elems, 6, 9));
        double w = elems[9];
        double h = elems[10];

        double[] fAmbRGB = Arrays.copyOfRange(elems, 11, 14);
        double[] fDifRGB = Arrays.copyOfRange(elems, 14, 17);
        double[] fRefRGB = Arrays.copyOfRange(elems, 17, 20);
        double fN = elems[20];
        double fKref = elems[21];

        double[] bAmbRGB = Arrays.copyOfRange(elems, 22, 25);
        double[] bDifRGB = Arrays.copyOfRange(elems, 25, 28);
        double[] bRefRGB = Arrays.copyOfRange(elems, 28, 31);
        double bN = elems[31];
        double bKref = elems[32];

        Patch patch = new Patch(fAmbRGB, fDifRGB, fRefRGB, fN, fKref,
                                bAmbRGB, bDifRGB, bRefRGB, bN, bKref,
                                center, v1.nVectorProduct(v2), v1, v2, w, h);
        scene.objects.add(patch);
    }

    private static void setupSphere(String input, RTScene scene) {
        String[] fields = input.split("\\s+");
        if (fields.length != 15) {
            System.out.println("Invalid sphere!");
            return;
        }
        double[] elems = parseToDouble(fields);

        IVector center = new Vector(Arrays.copyOfRange(elems, 0, 3));
        double radius = elems[3];
        double[] fAmbRGB = Arrays.copyOfRange(elems, 4, 7);
        double[] fDifRGB = Arrays.copyOfRange(elems, 7, 10);
        double[] fRefRGB = Arrays.copyOfRange(elems, 10, 13);
        double fN = elems[13];
        double fKref = elems[14];

        Sphere sphere = new Sphere(fAmbRGB, fDifRGB, fRefRGB, fN, fKref, center, radius);
        scene.objects.add(sphere);
    }

    private static double[] parseToDouble(String[] fields) {
        double[] input = new double[fields.length];
        for (int i = 0; i <fields.length; i++) {
            input[i] = Double.parseDouble(fields[i]);
        }
        return input;
    }

    private static void setupLights(String input, RTScene scene) {
        String[] fields = input.split("\\s+");
        if (fields.length != 6) {
            System.out.println("Invalid light!");
            return;
        }

        IVector position = new Vector(0, 0, 0);
        double[] rgb = new double[] { 0, 0, 0 };

        try {
            for (int i = 0; i < fields.length; i++) {
                if (i < 3) {
                    position.set(i, Double.parseDouble(fields[i]));
                } else {
                    rgb[i - 3] = Double.parseDouble(fields[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid light!");
            return;
        }
        scene.sources.add(new Light(position, rgb));
    }

    private static void setupGaIntensity(String input, RTScene scene) {
        String[] fields = input.split("\\s+");
        if (fields.length != 3) {
            System.out.println("Invalid global ambient!");
            return;
        }
        double[] ga = new double[] { 0, 0, 0 };
        try {
            for (int i = 0; i < fields.length; i++) {
                ga[i] = Double.parseDouble(fields[i]);
            }
        } catch (Exception e) {
            System.out.println("Invalid global ambient!");
            return;
        }
        scene.gaIntensity = ga;
    }


    private static List<String> removeComments(List<String> input) {
        List<String> newLines = new ArrayList<>();
        for (String line : input) {
            line = line.trim();
            if (line.startsWith("#")) continue;
            int index = line.indexOf("#");
            if (index == -1) {
                newLines.add(line);
            } else {
                line = line.substring(0, index).trim();
                newLines.add(line);
            }
        }
        return newLines;
    }
}
