package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */
public class Intersection {

    private SceneObject object;
    private double lambda;
    private boolean front;
    private IVector point;

    public SceneObject getObject() {
        return object;
    }

    public void setObject(SceneObject object) {
        this.object = object;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public IVector getPoint() {
        return point;
    }

    public void setPoint(IVector point) {
        this.point = point;
    }
}
