package geometries.impl;

import geometries.api.Geometry;

/**
 * Abstract base class for radial geometries.
 */
public abstract class RadialGeometry extends Geometry {

    /** The radius of the geometry */
    protected final double radius;

    /** The squared radius of the geometry for performance optimization */
    protected final double radiusSquared;

    /**
     * Constructor for RadialGeometry.
     * * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }
}