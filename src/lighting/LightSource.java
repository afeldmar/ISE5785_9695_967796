package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing external, physical light sources in the scene.
 * Defines methods for light propagation from the source to a specific point.
 */
public interface LightSource {

    /**
     * Calculates the light intensity at a specific point in the scene.
     *
     * @param p the illuminated point
     * @return the color intensity arriving at the given point (I_L)
     */
    public Color getIntensity(Point p);

    /**
     * Calculates the normalized direction vector from the light source to the illuminated point.
     *
     * @param p the illuminated point
     * @return the normalized direction vector (l) pointing towards the point
     */
    public Vector getL(Point p);
}