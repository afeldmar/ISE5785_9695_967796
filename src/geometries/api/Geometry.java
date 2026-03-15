package geometries.api;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract base class for all geometric shapes.
 */
public abstract class Geometry {

    /**
     * Calculates the normal vector to the geometry at a given point.
     * @param point the point on the geometry surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);
}