package hr.fer.zemris.irg.zad6;

/**
 * @author Filip Gulan
 */
public class AngleController {

    private double defaultX;
    private double defaultZ;
    private double defaultY;

    private double x;
    private double z;
    private double y;
    private double increment;

    private double angle;
    private double r;


    public AngleController(double x, double y, double z, double increment) {
        this.x = defaultX = x;
        this.z = defaultZ = z;
        this.y = defaultY = y;
        this.increment = increment;
        this.angle = Math.atan(z / x);
        this.r = 1 / Math.sin(angle);
    }

    public void increment() {
        double angleDegree = Math.toDegrees(angle);
        angleDegree += increment;
        angle = Math.toRadians(angleDegree);
        x = r * Math.cos(angle);
        z = r * Math.sin(angle);
    }

    public void decrement() {
        double angleDegree = Math.toDegrees(angle);
        angleDegree -= increment;
        angle = Math.toRadians(angleDegree);
        x = r * Math.cos(angle);
        z = r * Math.sin(angle);
    }

    public void reset() {
        x = defaultX;
        z = defaultZ;
        y = defaultY;
        angle = Math.atan(z / x);
    }

    public double getY() {
        return y;
    }

    public void incrementY() {
        y += increment/20.0;
    }

    public void decrementY() {
        y -= increment/20.0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
