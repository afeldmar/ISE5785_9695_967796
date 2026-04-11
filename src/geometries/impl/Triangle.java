package geometries.impl;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

/**
 * Represents a triangle in 3D Cartesian coordinate system.
 */
public class Triangle extends Polygon {

    /**
     * Constructor that builds a triangle from three points.
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = _plane.findIntersections(ray);
        if (intersections == null) {
            return null;
        }

        Point p0 = ray.origin();
        Vector v = ray.direction();

        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);

        double s1 = alignZero(v.dotProduct(v1.crossProduct(v2)));
        double s2 = alignZero(v.dotProduct(v2.crossProduct(v3)));
        double s3 = alignZero(v.dotProduct(v3.crossProduct(v1)));

        // intersection on edge or vertex is not included
        if (isZero(s1) || isZero(s2) || isZero(s3)) {
            return null;
        }

        boolean allPositive = s1 > 0 && s2 > 0 && s3 > 0;
        boolean allNegative = s1 < 0 && s2 < 0 && s3 < 0;

        return (allPositive || allNegative) ? intersections : null;
    }
}