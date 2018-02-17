package hr.fer.zemris.irg.models;

import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

import java.util.Arrays;

/**
 * @author Filip Gulan
 */
public class Light {

    private IVector position;
    private double[] rgb;

    public Light(IVector position, double[] rgb) {
        this.position = position;
        this.rgb = rgb;
    }

    public IVector getPosition() {
        return position;
    }

    public void setPosition(IVector position) {
        this.position = position;
    }

    public double[] getRgb() {
        return rgb;
    }

    public void setRgb(double[] rgb) {
        this.rgb = rgb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Light light = (Light) o;

        if (!position.equals(light.position)) return false;
        return Arrays.equals(rgb, light.rgb);

    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + Arrays.hashCode(rgb);
        return result;
    }
}
