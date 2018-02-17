package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Patch extends SceneObject {

    private IVector center;
    private IVector normal;
    private IVector v1;
    private IVector v2;
    private double w;
    private double h;

    public Patch(double[] fAmbRGB, double[] fDifRGB, double[] fRefRGB, double fN, double fKref,
                 double[] bAmbRGB, double[] bDifRGB, double[] bRefRGB, double bN, double bKref,
                 IVector center, IVector normal, IVector v1, IVector v2, double w, double h) {
        super(fAmbRGB, fDifRGB, fRefRGB, fN, fKref, bAmbRGB, bDifRGB, bRefRGB, bN, bKref);
        this.center = center;
        this.normal = normal;
        this.v1 = v1;
        this.v2 = v2;
        this.w = w;
        this.h = h;
    }

    @Override
    public void updateIntersection(Intersection intersection, IVector start, IVector d) {

    }

    @Override
    public IVector gerNormalInPoint(IVector point) {
        return null;
    }
}
