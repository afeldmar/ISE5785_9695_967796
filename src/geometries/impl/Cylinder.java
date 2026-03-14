package geometries.impl;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a cylinder in 3D Cartesian coordinate system.
 */
public class Cylinder extends Tube {

    private final double height;

    /**
     * Constructor for Cylinder.
     * * @param radius the radius of the cylinder
     * @param axis the central axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }
}