package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source (e.g., the sun).
 * The light has a constant direction and its intensity does not attenuate with distance.
 */
public class DirectionalLight extends Light implements LightSource {

    /** The direction of the light */
    private final Vector direction;

    /**
     * Constructs a directional light.
     *
     * @param intensity the intensity (color) of the light
     * @param direction the direction vector of the light (will be normalized)
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        // Directional light does not attenuate with distance
        return super.getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
