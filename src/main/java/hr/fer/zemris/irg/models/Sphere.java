package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Sphere extends SceneObject {

    private IVector center;
    private double radius;

    public Sphere(double[] fAmbRGB, double[] fDifRGB, double[] fRefRGB, double fN, double fKref,
                  IVector center, double radius) {
        super(fAmbRGB, fDifRGB, fRefRGB, fN, fKref);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void updateIntersection(Intersection intersection, IVector start, IVector d) {

    }

    @Override
    public IVector gerNormalInPoint(IVector point) {
        return null;
    }
}
