package hr.fer.zemris.irg.zad1;

import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Zad1_3_1 {

    public static void main(String [] args) {
        IVector a = Vector.parseSimple("2 3 -4");
        IVector b = Vector.parseSimple("-1 4 -3");

        IVector v1 = a.nAdd(b);
        System.out.println("v1 = " + v1);

        double s = v1.scalarProduct(b);
        System.out.println("s = " + s);                 // i j  k
                                                        // 1 7 -7
        IVector v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));
        System.out.println("v2 = " + v2);

        IVector v3 = v2.nNormalize();
        System.out.println("v3 = " + v3);

        IVector v4 = v2.nScalarMultiply(-1);
        System.out.println("v4 = " + v4);

        IMatrix matrix1 = Matrix.parseSimple("1 2 3 | " +
                "2 1 3 | " +
                "4 5 1");
        IMatrix matrix2 = Matrix.parseSimple("-1 2 -3 | " +
                "5 -2 7 | " +
                "-4 -1 3");

        IMatrix m1 = matrix1.nAdd(matrix2);
        System.out.println("M1 = \n" + m1);

        IMatrix m2 = matrix1.nMultiply(matrix2.nTranspose(true));
        System.out.println("M2 = \n" + m2);

        IMatrix matrix3 = Matrix.parseSimple("-24 18 5 | " +
                "20 -15 -4 | " +
                "-5 4 1");
        IMatrix matrix4 = Matrix.parseSimple("1 2 3 | " +
                "0 1 4 | " +
                "5 6 0");
        IMatrix m3 = matrix3.nInvert().nMultiply(matrix4.nInvert());
        System.out.println("M3 = \n" + m3);

        System.out.println(matrix3.nInvert());
        System.out.println(matrix4.nInvert());

    }
}
