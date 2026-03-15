package geometries.impl;

import geometries.api.Geometry;

/**
 * Abstract base class for radial geometries.
 */
public abstract class RadialGeometry extends Geometry {

    /** The radius of the geometry */
    protected final double _radius;

    /** The squared radius of the geometry for performance optimization */
    protected final double _radiusSquared;

    /**
     * Constructor for RadialGeometry.
     *
     *  @param radius the radius of the geometry
     */
    protected RadialGeometry(double radius) {
        this._radius = radius;
        this._radiusSquared = radius * radius;
    }
}