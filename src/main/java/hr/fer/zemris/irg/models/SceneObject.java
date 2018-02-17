package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public abstract class SceneObject {
    protected double[] fAmbRGB;
    protected double[] fDifRGB;
    protected double[] fRefRGB;
    protected double fN;
    protected double fKref;

    protected double[] bAmbRGB;
    protected double[] bDifRGB;
    protected double[] bRefRGB;
    protected double bN;
    protected double bKref;

    public SceneObject(double[] fAmbRGB, double[] fDifRGB, double[] fRefRGB, double fN, double fKref,
                       double[] bAmbRGB, double[] bDifRGB, double[] bRefRGB, double bN, double bKref) {
        this.fAmbRGB = fAmbRGB;
        this.fDifRGB = fDifRGB;
        this.fRefRGB = fRefRGB;
        this.fN = fN;
        this.fKref = fKref;
        this.bAmbRGB = bAmbRGB;
        this.bDifRGB = bDifRGB;
        this.bRefRGB = bRefRGB;
        this.bN = bN;
        this.bKref = bKref;
    }

    public SceneObject(double[] fAmbRGB, double[] fDifRGB, double[] fRefRGB, double fN, double fKref) {
        this.fAmbRGB = fAmbRGB;
        this.fDifRGB = fDifRGB;
        this.fRefRGB = fRefRGB;
        this.fN = fN;
        this.fKref = fKref;
    }

    public abstract void updateIntersection(Intersection intersection, IVector start, IVector d);
    public abstract IVector gerNormalInPoint(IVector point);


}
