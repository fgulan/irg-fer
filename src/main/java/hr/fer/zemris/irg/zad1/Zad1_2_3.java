package hr.fer.zemris.irg.zad1;

import hr.fer.zemris.irg.utility.linear.Matrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;

/**
 * @author Filip Gulan
 */
public class Zad1_2_3 {

    public static void main(String [] args) {
        IMatrix a = Matrix.parseSimple("1 5 3 | 0 0 8 | 1 1 1");
        IMatrix r = Matrix.parseSimple("3 | 4 | 1");
        IMatrix v = a.nInvert().nMultiply(r);

        System.out.println("Rjesenje sustava: ");
        System.out.println(v);
    }
}
