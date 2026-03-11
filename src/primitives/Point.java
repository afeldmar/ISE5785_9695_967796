package primitives;

/**
 * Class Point represents a point in 3D Cartesian coordinate system.
 */
public class Point {

    /**
     * The coordinates of the point.
     */
    protected final Double3 _xyz;

    /**
     * Constant point at the origin (0,0,0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructor from three double values.
     *
     * @param x first coordinate
     * @param y second coordinate
     * @param z third coordinate
     */
    public Point(double x, double y, double z) {
        this._xyz = new Double3(x, y, z);
    }

    /**
     * Constructor from Double3 object.
     *
     * @param xyz coordinates
     */
    public Point(Double3 xyz) {
        this._xyz = xyz;
    }

    /**
     * Subtracts another point from this point.
     *
     * @param other other point
     * @return vector from other point to this point
     */
    public Vector subtract(Point other) {
        return new Vector(
                this._xyz.d1 - other._xyz.d1,
                this._xyz.d2 - other._xyz.d2,
                this._xyz.d3 - other._xyz.d3
        );
    }

    /**
     * Adds a vector to this point.
     *
     * @param vector vector to add
     * @return new point after addition
     */
    public Point add(Vector vector) {
        return new Point(
                this._xyz.d1 + vector._xyz.d1,
                this._xyz.d2 + vector._xyz.d2,
                this._xyz.d3 + vector._xyz.d3
        );
    }

    /**
     * Calculates squared distance between this point and another point.
     *
     * @param other other point
     * @return squared distance
     */
    public double distanceSquared(Point other) {
        double dx = this._xyz.d1 - other._xyz.d1;
        double dy = this._xyz.d2 - other._xyz.d2;
        double dz = this._xyz.d3 - other._xyz.d3;

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calculates distance between this point and another point.
     *
     * @param other other point
     * @return distance
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (!(obj instanceof Point)) {return false;}
        Point other = (Point) obj;
        return this._xyz.equals(other._xyz);
    }

    @Override
    public String toString() {
        return "Point" + _xyz;
    }
}
