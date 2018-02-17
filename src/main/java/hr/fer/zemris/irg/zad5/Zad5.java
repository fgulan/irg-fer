package hr.fer.zemris.irg.zad5;

import hr.fer.zemris.irg.models.ObjectModel;
import hr.fer.zemris.irg.models.PointPosition;
import hr.fer.zemris.irg.models.Vertex3D;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Filip Gulan
 */
public class Zad5 {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide path to .obj file. Closing.");
            System.exit(-1);
        }
        String fileName = args[0];
        ObjectModel model = ObjectModel.readOBJFromFile(fileName);
        System.out.println("Ucitani objekt " + fileName + ": ");
        System.out.println(model.dumpToOBJ());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            readInputs(br, model);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void readInputs(BufferedReader reader, ObjectModel model) throws IOException {
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine().trim();

            if (line == null || line.isEmpty()) {
                continue;
            }

            switch (line) {
                case "normiraj":
                    ObjectModel temp = model.copy().normalize();
                    System.out.println("Normirani objekt:");
                    System.out.println(temp.dumpToOBJ());
                    break;
                case "quit":
                    System.out.println("Zatvaram program");
                    System.exit(0);
                    break;
                case "draw":
                    SwingUtilities.invokeLater(() -> {
                        final ObjectViewer viewer = new ObjectViewer(model);
                        viewer.setVisible(true);
                    });
                    break;
                default:
                    Vertex3D vertex = parseVertex(line);
                    if (vertex == null) {
                        System.out.println("Neispravan unos. Unesite koordinate tocke (x, y, z), 'quit' za izlaz " +
                                "te 'normiraj' za normiranje objekta");
                        continue;
                    }
                    PointPosition position = model.checkPointPosition(vertex);
                    printPositionInfo(position);
                    break;
            }
        }
    }

    private static void printPositionInfo(PointPosition position) {
        switch (position) {
            case ON:
                System.out.println("Tocka se nalazi na obodu tijela.");
                break;
            case INSIDE:
                System.out.println("Tocka se nalazi unutar tijela.");
                break;
            case OUTSIDE:
                System.out.println("Tocka se nalazi izvan tijela.");
                break;
        }
    }

    private static Vertex3D parseVertex(String line) {
        String[] elems = line.split("\\s+");
        if (elems.length != 3) return null;
        try {
            double x = Double.parseDouble(elems[0]);
            double y = Double.parseDouble(elems[1]);
            double z = Double.parseDouble(elems[2]);
            return new Vertex3D(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
