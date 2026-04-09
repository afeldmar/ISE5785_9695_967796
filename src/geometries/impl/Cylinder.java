package geometries.impl;

import primitives.Point;
import java.util.List;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Represents a cylinder in 3D Cartesian coordinate system.
 */
public class Cylinder extends Tube {

    /** The height of the cylinder */
    private final double height;

    /**
     * Constructor for Cylinder.
     * @param radius the radius of the cylinder
     * @param axis the central axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * Calculates the normal vector to the cylinder at a given point.
     * The normal is determined based on whether the point is located
     * on the bottom base, the top base, or the side surface of the cylinder.
     *
     * @param point a point on the cylinder's surface
     * @return the normalized normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.origin();
        Vector v = axis.direction();

        // Point is exactly at the center of the bottom base
        if (point.equals(p0)) {
            return v.scale(-1);
        }

        Vector pMinusP0 = point.subtract(p0);
        double t = alignZero(v.dotProduct(pMinusP0));

        // Point is on the bottom base
        if (isZero(t)) {
            return v.scale(-1);
        }

        // Point is on the top base
        if (isZero(t - height)) {
            return v;
        }

        // Point is on the side surface
        return super.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}