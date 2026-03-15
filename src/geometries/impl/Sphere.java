package geometries.impl;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D Cartesian coordinate system.
 */
public class Sphere extends RadialGeometry {

    /**
     * Center point of the sphere.
     */
    private final Point _center;

    /**
     * Constructor for Sphere.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this._center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}