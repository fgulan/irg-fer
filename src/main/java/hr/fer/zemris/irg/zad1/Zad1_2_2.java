package hr.fer.zemris.irg.zad1;

import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;

/**
 * @author Filip Gulan
 */
public class Zad1_2_2 {

    public static void main(String [] args) {
        IMatrix a = Matrix.parseSimple("3 5 | 2 10");
        IMatrix r = Matrix.parseSimple("2 | 8");
        IMatrix v = a.nInvert().nMultiply(r);

        System.out.println("Rjesenje sustava: ");
        System.out.println(v);
    }
}
