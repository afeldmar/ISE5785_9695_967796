package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a tube in 3D Cartesian coordinate system.
 */
public class Tube extends RadialGeometry {

    /** The axis ray of the tube */
    private final Ray axis;

    /**
     * Constructor for Tube.
     * @param radius the radius of the tube
     * @param axis the central axis ray of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.origin();
        Vector v = axis.direction();

        double t = v.dotProduct(point.subtract(p0));

        Point o = p0;
        if (t != 0) {
            o = p0.add(v.scale(t));
        }

        return point.subtract(o).normalize();
    }
}