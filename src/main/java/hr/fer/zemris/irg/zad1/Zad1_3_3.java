package hr.fer.zemris.irg.zad1;


import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;

import java.util.Scanner;

/**
 * @author Filip Gulan
 */
public class Zad1_3_3 {

    public static void main(String [] args) {
        Scanner reader = new Scanner(System.in);

        double[][] equations = new double[3][];
        for (int i = 0; i < 3; i++) {
            System.out.print("Unesite koordinate " + (i + 1) + ". tocke trokuta u obliku 'x y z': ");
            equations[i] = getInputEquation(reader);
        }
        while (true) {
            System.out.println("Unesite koordinate tocke T u obliku 'x y z': ");
            double[] results = getInputEquation(reader);

            IMatrix equationsMatrix = new Matrix(3, 3, equations, true);
            IMatrix resultsMatrix = getResultMatrix(3, results);

            IMatrix solvedEquation = equationsMatrix.nInvert().nMultiply(resultsMatrix);
            System.out.println("Baricentricne koordinate tocke T [x, y, z] =");
            System.out.println(solvedEquation);
        }
    }

    private static double[] getInputEquation(Scanner reader) {
        double[] equation = new double[3];
        for (int i = 0; i < equation.length; i++) {
            equation[i] = reader.nextDouble();
        }
        return equation;
    }

    private static IMatrix getResultMatrix(int numOfUnknowns, double ...results) {
        IMatrix matrix = new Matrix(numOfUnknowns, 1);
        for (int i = 0; i < numOfUnknowns; i++) {
            matrix.set(i, 0, results[i]);
        }
        return matrix;
    }
}
