package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a tube in 3D Cartesian coordinate system.
 */
public class Tube extends RadialGeometry {

    protected final Ray axis;

    /**
     * Constructor for Tube.
     * * @param radius the radius of the tube
     * @param axis the central axis ray of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}