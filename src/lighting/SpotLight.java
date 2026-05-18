package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight source (e.g., a flashlight).
 * It has a position and a specific direction, creating a cone of light.
 * The intensity attenuates both by distance and by the angle from its main direction.
 */
public class SpotLight extends PointLight {

    /** The direction the spotlight is pointing towards */
    private final Vector direction;

    /** The exponent to narrow the beam of the spotlight. Default is 1 (regular spotlight). */
    private int narrowBeam = 1;

    /**
     * Constructs a spotlight.
     *
     * @param intensity the original intensity of the light
     * @param position  the position of the light source
     * @param direction the direction of the spotlight (will be normalized)
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the narrow beam factor to focus the spotlight into a tighter cone.
     *
     * @param narrowBeam the exponent to apply to the projection factor (must be >= 1)
     * @return the SpotLight object itself for chaining
     */
    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        // Calculate the angle factor: max(0, dir dot l)
        Vector l = getL(p);
        double projection = direction.dotProduct(l);

        if (projection <= 0) {
            return Color.BLACK; // The point is behind the spotlight
        }

        // --- BONUS ---
        // Narrow the beam by raising the projection to the power of narrowBeam
        if (narrowBeam > 1) {
            projection = Math.pow(projection, narrowBeam);
        }

        // The intensity is the point light intensity scaled by the projection factor
        return super.getIntensity(p).scale(projection);
    }
}