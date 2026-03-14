package geometries.impl;

import geometries.api.Geometry;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a plane in 3D Cartesian coordinate system.
 */
public class Plane extends Geometry {

    private final Point point;
    private final Vector normal;

    /**
     * Constructor that builds a plane from three points.
     * * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.point = p1;
        Vector u = p2.subtract(p1);
        Vector v = p3.subtract(p1);
        this.normal = u.crossProduct(v).normalize();
    }

    /**
     * Constructor that builds a plane from a point and a normal vector.
     * * @param point a point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }
}