package hr.fer.zemris.irg.zad11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class IFSFractal {

    private int pointsNumber;
    private int limit;
    private int eta1;
    private int eta2;
    private int eta3;
    private int eta4;
    private List<IFSRow> rows;

    public IFSFractal() {
        rows = new ArrayList<>();
    }

    public static IFSFractal readFromFile(Path path) {
        List<String> lines = null;
        try {
            lines = removeComments(Files.readAllLines(path));
        } catch (IOException e) {
            return null;
        }
        IFSFractal fractal = new IFSFractal();

        int size = lines.size();
        for (int i = 0; i < size; i++) {
            String line = lines.get(i).trim();
            if (i == 0) {
                fractal.pointsNumber = Integer.parseInt(line);
            } else if (i == 1) {
                fractal.limit = Integer.parseInt(line);
            } else if (i == 2) {
                String[] fields = line.split("\\s+");
                int[] elems = parseToInt(fields);
                fractal.eta1 = elems[0];
                fractal.eta2 = elems[1];
            } else if (i == 3) {
                String[] fields = line.split("\\s+");
                int[] elems = parseToInt(fields);
                fractal.eta3 = elems[0];
                fractal.eta4 = elems[1];
            } else {
                String[] fields = line.split("\\s+");
                double[] elems = parseToDouble(fields);
                IFSRow row = new IFSRow(elems);
                fractal.rows.add(row);
            }
        }
        Collections.sort(fractal.rows, new Comparator<IFSRow>() {
            @Override
            public int compare(IFSRow o1, IFSRow o2) {
                return Double.compare(o1.p, o2.p);
            }
        });
        return fractal;
    }

    private static double[] parseToDouble(String[] fields) {
        double[] input = new double[fields.length];
        for (int i = 0; i <fields.length; i++) {
            input[i] = Double.parseDouble(fields[i]);
        }
        return input;
    }

    private static List<String> removeComments(List<String> input) {
        List<String> newLines = new ArrayList<>();
        for (String line : input) {
            line = line.trim();
            if (line.startsWith("#")) continue;
            int index = line.indexOf("#");
            if (index == -1) {
                if (line.isEmpty()) continue;
                newLines.add(line);
            } else {
                line = line.substring(0, index).trim();
                newLines.add(line);
            }
        }
        return newLines;
    }

    private static int[] parseToInt(String[] fields) {
        int[] input = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            input[i] = Integer.parseInt(fields[i]);
        }
        return input;
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    public int getLimit() {
        return limit;
    }

    public int getEta1() {
        return eta1;
    }

    public int getEta2() {
        return eta2;
    }

    public int getEta3() {
        return eta3;
    }

    public int getEta4() {
        return eta4;
    }

    public List<IFSRow> getRows() {
        return rows;
    }

    public double getX(double x0, double y0, double p) {
        int size = rows.size();
        double counter = 0.0;
        for (int i = 0; i < size; i++) {
            IFSRow row = rows.get(i);
            counter += row.p;
            if (p < counter) {
                return row.a * x0 + row.b * y0 + row.e;
            }
        }

        return 0;
    }

    public double getY(double x0, double y0, double p) {
        int size = rows.size();
        double counter = 0.0;
        for (int i = 0; i < size; i++) {
            IFSRow row = rows.get(i);
            counter += row.p;
            if (p < counter) {
                return row.c * x0 + row.d * y0 + row.f;
            }
        }
        return 0;
    }
}
