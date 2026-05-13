package geometries.api;

import primitives.Point;
import primitives.Vector;
import primitives.Color;

/**
 * Abstract base class for all geometric shapes.
 */
public abstract class Geometry extends Intersectable {

    /**
     * Calculates the normal vector to the geometry at a given point.
     * @param point the point on the geometry surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);
    private Color emission = Color.BLACK;

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the geometry itself for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}