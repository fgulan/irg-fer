package hr.fer.zemris.irg.zad9;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;

/**
 * @author Filip Gulan
 */


public abstract class AbstractCullingWithShading implements ICullingWithShading {

    public IVector lightPosition = new Vector(4, 5, 3);

    private IVector lightAmbient = new Vector(0.2, 0.2, 0.2);
    private IVector lightDiffuse = new Vector(0.8, 0.8, 0);
    private IVector lightSpecular = new Vector(0, 0, 0);

    private IVector materialAmbient = new Vector(1, 1, 1);
    private IVector materialDiffuse = new Vector(1, 1, 1);
    private IVector materialSpecular = new Vector(0.01, 0.01, 0.01);

    private IVector ambientComponent = componentMultiply(lightAmbient, materialAmbient);
    private IVector diffuseComponent = componentMultiply(lightDiffuse, materialDiffuse);
    private IVector specularComponent = componentMultiply(lightSpecular, materialSpecular);
    private double cosinusPower = 96;

    private IVector componentMultiply(IVector light, IVector material) {
        IVector component = new Vector(0, 0, 0);
        for (int i = 0; i < 3; i++) {
            component.set(i, light.get(i) * material.get(i));
        }
        return component;
    }

    protected void shade(GL2 gl2, IVector normal, IVector position, IVector eye) {
        normal = normal.nNormalize();
        IVector enteringRay = lightPosition.nSub(position).normalize();
        IVector reflectionRay = enteringRay.nScalarMultiply(normal.scalarProduct(normal) * 2)
                                            .nSub(enteringRay)
                                            .normalize();

        double diffuseCosFactor = Math.max(enteringRay.scalarProduct(normal), 0);
        IVector diffuse = diffuseComponent.nScalarMultiply(diffuseCosFactor);

        double specularCosFactor = reflectionRay.scalarProduct(eye.nSub(position).normalize());
        if (specularCosFactor <= 0) {
            specularCosFactor = 0;
        } else {
            specularCosFactor = Math.pow(specularCosFactor, cosinusPower);
        }
        IVector specular = specularComponent.nScalarMultiply(specularCosFactor);

        IVector color = ambientComponent.nAdd(diffuse).add(specular);
        gl2.glColor3d(color.get(0), color.get(1), color.get(2));
    }
}
