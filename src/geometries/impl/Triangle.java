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

    /**
     * Calculates the intersections between the given ray and this triangle using
     * the Möller-Trumbore intersection algorithm.
     * Intersections that fall exactly on the edges or vertices are not included.
     *
     * @param ray the ray intersecting the triangle
     * @return a list containing the intersection object, or null if there is no valid intersection
     */
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray) {
        Point p0 = ray.origin();
        Vector v = ray.direction();

        Point v0 = _vertices.get(0);
        Point v1 = _vertices.get(1);
        Point v2 = _vertices.get(2);

        // Calculate triangle edges originating from vertex v0
        Vector e1 = v1.subtract(v0);
        Vector e2 = v2.subtract(v0);

        // Step 1: Calculate the determinant to check if the ray is parallel to the triangle
        Vector pVec = v.crossProduct(e2);
        double det = alignZero(e1.dotProduct(pVec));

        // If the determinant is near zero, the ray is parallel to the triangle (no intersection)
        if (isZero(det)) {
            return null;
        }

        double invDet = 1d / det;

        // Step 2: Calculate the 'u' parameter
        // Prevent zero vector exception if the ray origin is exactly at vertex v0
        if (p0.equals(v0)) {
            return null;
        }
        Vector tVec = p0.subtract(v0);

        double u = alignZero(tVec.dotProduct(pVec) * invDet);

        // Reject intersections outside the triangle or exactly on its edges/vertices
        if (u <= 0 || u >= 1) {
            return null;
        }

        // Step 3: Calculate the 'v' parameter
        Vector qVec = tVec.crossProduct(e1);
        double vParam = alignZero(v.dotProduct(qVec) * invDet);

        // Reject if 'v' is out of bounds, or if u + v >= 1 (outside or on the third edge)
        if (vParam <= 0 || alignZero(u + vParam - 1) >= 0) {
            return null;
        }

        // Step 4: Calculate the distance 't'
        double t = alignZero(e2.dotProduct(qVec) * invDet);

        // Ensure the intersection is in front of the camera (positive ray direction)
        if (t <= 0) {
            return null;
        }

        // Valid intersection found! Return the intersection wrapped with 'this'
        return List.of(new Intersection(this, ray.getPoint(t)));
    }
}