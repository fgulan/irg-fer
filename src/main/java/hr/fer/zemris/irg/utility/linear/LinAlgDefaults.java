package hr.fer.zemris.irg.utility.linear;

import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * LinAlgDefault razred implementira statičke metode za izradu
 * predpostavljenih razreda Matrix i Vector.
 *  
 * @author Filip Gulan
 * @version 1.0
 *
 */
public class LinAlgDefaults {

	/**
	 * Stvara matricu ispunjenu nulama sa zadanim brojem
	 * redaka i stupaca.
	 * @param rows Broj redaka.
	 * @param cols Broj stupaca.
	 * @return Nova matrica.
	 */
	public static IMatrix defaultMatrix(int rows, int cols) {
		return new Matrix(rows, cols);
	}


	public static void main(String [] args) {
		IVector vector = new Vector(true, true, 1,2,3,4);
		System.out.println(vector);
	}

	/**
	 * Stvara vektor ispunjen nulama sa zadanom dimenzijom.
	 * @param dimension Broj elemenata.
	 * @return Novi vektor.
	 */
	public static IVector defaultVector(int dimension) {
		double[] elems = new double[dimension];
		return new Vector(elems);
	}
}
