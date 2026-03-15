package primitives;

/**
 * Class Vector represents a vector in 3D Cartesian coordinate system.
 * Zero vector is not allowed.
 */
public final class Vector extends Point {

    /**
     * Unit vector in X axis direction.
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * Unit vector in Y axis direction.
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * Unit vector in Z axis direction.
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);
    /**
     * Constructor from three coordinates.
     *
     * @param x first coordinate
     * @param y second coordinate
     * @param z third coordinate
     * @throws IllegalArgumentException if the vector is zero
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);

        if (this._xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Constructor from Double3.
     *
     * @param xyz coordinates
     * @throws IllegalArgumentException if the vector is zero
     */
    public Vector(Double3 xyz) {
        super(xyz);

        if (this._xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other other vector
     * @return new vector which is the sum
     */
    public Vector add(Vector other) {
        return new Vector(this._xyz.add(other._xyz));
    }

    /**
     * Multiplies the vector by a scalar.
     *
     * @param scalar scale factor
     * @return new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(this._xyz.scale(scalar));
    }

    /**
     * Calculates dot product with another vector.
     *
     * @param other other vector
     * @return dot product
     */
    public double dotProduct(Vector other) {
        return this._xyz._d1() * other._xyz._d1()
                + this._xyz._d2() * other._xyz._d2()
                + this._xyz._d3() * other._xyz._d3();
    }

    /**
     * Calculates cross product with another vector.
     *
     * @param other other vector
     * @return new vector which is the cross product
     */
    public Vector crossProduct(Vector other) {
        double x = this._xyz._d2() * other._xyz._d3() - this._xyz._d3() * other._xyz._d2();
        double y = this._xyz._d3() * other._xyz._d1() - this._xyz._d1() * other._xyz._d3();
        double z = this._xyz._d1() * other._xyz._d2() - this._xyz._d2() * other._xyz._d1();

        return new Vector(x, y, z);
    }

    /**
     * Calculates squared length of the vector.
     *
     * @return squared length
     */
    public double lengthSquared() {
        return this._xyz._d1() * this._xyz._d1()
                + this._xyz._d2() * this._xyz._d2()
                + this._xyz._d3() * this._xyz._d3();
    }

    /**
     * Calculates length of the vector.
     *
     * @return vector length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }


    /**
     * Returns normalized vector.
     *
     * @return new normalized vector
     */
    public Vector normalize() {
        return new Vector(this._xyz.divide(length()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (!(obj instanceof Vector)) {return false;}
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Vector" + _xyz;
    }

}