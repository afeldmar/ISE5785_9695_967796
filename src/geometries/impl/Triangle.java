package geometries.impl;

import primitives.Point;

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
}