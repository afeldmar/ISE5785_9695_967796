package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point light source (e.g., a lightbulb).
 * The light shines equally in all directions from a specific point,
 * and its intensity attenuates with distance.
 */
public class PointLight extends Light implements LightSource {

    /** The position of the light in 3D space */
    private final Point position;

    /** Constant attenuation factor */
    private double kC = 1;
    /** Linear attenuation factor */
    private double kL = 0;
    /** Quadratic attenuation factor */
    private double kQ = 0;

    /**
     * Constructs a point light.
     *
     * @param intensity the original intensity of the light
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     * @param kC the constant attenuation factor
     * @return the PointLight object itself for chaining
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     * @param kL the linear attenuation factor
     * @return the PointLight object itself for chaining
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     * @param kQ the quadratic attenuation factor
     * @return the PointLight object itself for chaining
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double dSquared = position.distanceSquared(p);

        // Attenuation formula: I_0 / (kc + kl*d + kq*d^2)
        double attenuation = kC + kL * d + kQ * dSquared;

        return super.getIntensity().scale(1d / attenuation);    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
