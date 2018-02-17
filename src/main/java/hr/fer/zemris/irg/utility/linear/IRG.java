package hr.fer.zemris.irg.utility.linear;

import hr.fer.zemris.irg.utility.linear.interfaces.IMatrix;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class IRG {

    public static IMatrix translate3D(double dx, double dy, double dz) {
        IMatrix translation = new Matrix(4, 4);
        translation.set(0, 0, 1);
        translation.set(1, 1, 1);
        translation.set(2, 2, 1);
        translation.set(3, 0, dx);
        translation.set(3, 1, dy);
        translation.set(3, 2, dz);
        translation.set(3, 3, 1);
        return translation;
    }

    public static IMatrix scale3D(float sx, float sy, float sz) {
        IMatrix scale = new Matrix(4, 4);
        scale.set(0, 0, sx);
        scale.set(1, 1, sy);
        scale.set(2, 2, sz);
        scale.set(3, 3, 1);
        return scale;
    }

    public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) {
        // gluLookAt algorithm
        IVector f = center.nSub(eye).normalize();
        IVector UP = viewUp.nNormalize();

        IVector s = f.nVectorProduct(UP).normalize();
        IVector u = s.nVectorProduct(f).normalize();

        double[][] matrix = new double[4][4];

        // N
        matrix[0][0] = s.get(0);
        matrix[0][1] = s.get(1);
        matrix[0][2] = s.get(2);

        // V
        matrix[1][0] = u.get(0);
        matrix[1][1] = u.get(1);
        matrix[1][2] = u.get(2);

        // U
        matrix[2][0] = -f.get(0);
        matrix[2][1] = -f.get(1);
        matrix[2][2] = -f.get(2);
        matrix[3][3] = 1;

        IMatrix M = new Matrix(4, 4, matrix, true);
        IMatrix translation = translate3D(- eye.get(0), - eye.get(1), - eye.get(2));
        return translation.nMultiply(M.nTranspose(false));
    }

    public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) {
        double[][] matrix = new double[4][4];

        matrix[0][0] = 2 * n / (r - l);
        matrix[1][1] = 2 * n / (t - b);
        matrix[2][0] = (r + l) / (r - l);
        matrix[2][1] = (t + b) / (t - b);
        matrix[2][2] = - (f + n) / (f - n);
        matrix[2][3] = -1;
        matrix[3][2] = (-2) * f * n / (f - n);

        return new Matrix(4, 4, matrix, true);
    }

    public static boolean isAntiClockwise(IVector first, IVector second, IVector third) {
        IVector v0 = first.copy();
        IVector v1 = second.copy();
        IVector v2 = third.copy();

        v0.set(2, 1);
        v1.set(2, 1);
        v2.set(2, 1);

        return v0.nVectorProduct(v1).scalarProduct(v2) > 0;
    }

}
