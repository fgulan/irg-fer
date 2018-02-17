package hr.fer.zemris.irg.zad1;


import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;

import java.util.Scanner;

/**
 * @author Filip Gulan
 */
public class Zad1_3_2 {

    public static void main(String [] args) {
        Scanner reader = new Scanner(System.in);

        double[][] equations = new double[3][];
        for (int i = 0; i < 3; i++) {
            System.out.print("Unesite " + (i + 1) + ". jednadzbu u obliku 'x y z r': ");
            equations[i] = getInputEquation(reader);
        }

        IMatrix equationsMatrix = getEquationsMatrix(3, equations);
        IMatrix resultsMatrix = getResultMatrix(3, equations[0][3], equations[1][3], equations[2][3]);

        IMatrix solvedEquation = equationsMatrix.nInvert().nMultiply(resultsMatrix);
        System.out.println("Rjesenje [x, y, z] =");
        System.out.println(solvedEquation);
    }

    private static double[] getInputEquation(Scanner reader) {
        double[] equation = new double[4];
        for (int i = 0; i < equation.length; i++) {
            equation[i] = reader.nextDouble();
        }
        return equation;
    }

    private static IMatrix getEquationsMatrix(int numOfUnknowns, double[] ...equations) {
        IMatrix matrix = new Matrix(numOfUnknowns, numOfUnknowns);
        for (int i = 0; i < numOfUnknowns; i++) {
            for (int j = 0; j < numOfUnknowns; j++) {
                matrix.set(i, j, equations[i][j]);
            }
        }
        return matrix;
    }

    private static IMatrix getResultMatrix(int numOfUnknowns, double ...results) {
        IMatrix matrix = new Matrix(numOfUnknowns, 1);
        for (int i = 0; i < numOfUnknowns; i++) {
            matrix.set(i, 0, results[i]);
        }
        return matrix;
    }
}
