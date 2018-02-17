package hr.fer.zemris.irg.models;

/**
 * @author Filip Gulan
 */
enum Orientation {
    CLOCKWISE, COUNTERCLOCKWISE, UNKNOWN
}

/**
 * @author Filip Gulan
 */
public class PolygonStatus {
    public boolean convex;
    public Orientation orientation;

    public PolygonStatus(boolean convex, Orientation orientation) {
        this.convex = convex;
        this.orientation = orientation;
    }
}
