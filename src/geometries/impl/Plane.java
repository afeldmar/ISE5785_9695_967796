package geometries.impl;

import geometries.api.Geometry;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane in 3D Cartesian coordinate system.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    private final Point _point;

    /**
     * The normal vector of the plane.
     */
    private final Vector _normal;

    /**
     * Constructor that builds a plane from three points.
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        this._point = p1;
        this._normal = p2.subtract(p1).crossProduct(p3.subtract(p1)).normalize();
    }

    /**
     * Constructor that builds a plane from a point and a normal vector.
     *
     * @param point a point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this._point = point;
        this._normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return this._normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.origin();
        Vector v = ray.direction();

        double nv = alignZero(_normal.dotProduct(v));

        // no intersection - the ray is parallel to the plane
        if (isZero(nv)) {
            return null;
        }

        // ray starts at the plane reference point
        if (_point.equals(p0)) {
            return null;
        }

        double t = alignZero(_normal.dotProduct(_point.subtract(p0)) / nv);

        // there is intersection only if it is in the direction of the ray
        return t <= 0 ? null : List.of(ray.getPoint(t));
    }
}