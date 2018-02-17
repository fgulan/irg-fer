package hr.fer.zemris.irg.utility.linear.interfaces;

/**
 * Sučelje predstavlja model jednog proizvoljno
 * velikog vektora (veličina će, naravno, prilikom
 * stvaranja odgovarajućih objekata biti fiksirana
 * u trenutku stvaranja).
 * @author Filip Gulan
 */
public interface IVector {

    double get(int index);

    IVector set(int index, double value) throws UnmodifiableObjectException;

    int getDimension();

    IVector copy();

    IVector copyPart(int n);

    IVector newInstance(int dimension);

    IVector add(IVector other) throws IncompatibleOperandException;

    IVector nAdd(IVector other) throws IncompatibleOperandException;

    IVector sub(IVector other) throws IncompatibleOperandException;

    IVector nSub(IVector other) throws IncompatibleOperandException;

    IVector scalarMultiply(double byValue);

    IVector nScalarMultiply(double byValue);

    double norm();

    IVector normalize();

    IVector nNormalize();

    double cosine(IVector other) throws IncompatibleOperandException;

    double scalarProduct(IVector other) throws IncompatibleOperandException;

    IVector nVectorProduct(IVector other) throws IncompatibleOperandException;

    IVector nFromHomogeneus();

    IMatrix toRowMatrix(boolean liveView);

    IMatrix toColumnMatrix(boolean liveView);

    double[] toArray();
}
