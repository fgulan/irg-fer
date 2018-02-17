package hr.fer.zemris.irg.zad11;

/**
 * @author Filip Gulan
 */
public class IFSRow {

    public double a;
    public double b;
    public double c;
    public double d;
    public double e;
    public double f;
    public double p;

    public IFSRow(double a, double b, double c, double d, double e, double f, double p) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.p = p;
    }

    public IFSRow(double[] fields) {
        this.a = fields[0];
        this.b = fields[1];
        this.c = fields[2];
        this.d = fields[3];
        this.e = fields[4];
        this.f = fields[5];
        this.p = fields[6];
    }
}
