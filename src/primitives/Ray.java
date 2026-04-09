package primitives;

import static primitives.Util.*;

/**
 * Class Ray represents a ray in 3D Cartesian coordinate system.
 */
public final class Ray {

    /**
     * Starting point of the ray.
     */
    private final Point _origin;

    /**
     * Direction vector of the ray.
     */
    private final Vector _direction;

    /**
     * Constructor for Ray.
     *
     * @param origin starting point
     * @param direction ray direction
     */
    public Ray(Point origin, Vector direction) {
        this._origin = origin;
        this._direction = direction.normalize();
    }

    /**
     * Returns the origin point of the ray.
     *
     * @return the origin point
     */
    public Point origin() {
        return this._origin;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return the direction vector
     */
    public Vector direction() {
        return this._direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (!(obj instanceof Ray)) {return false;}
        Ray other = (Ray) obj;
        return this._origin.equals(other._origin)
                && this._direction.equals(other._direction);
    }

    @Override
    public String toString() {
        return "Ray: origin=" + _origin + ", direction=" + _direction;
    }

    /**
     * Calculates a point on the ray at a given distance from the head of the ray.
     *
     * @param t the distance from the head of the ray
     * @return the calculated point
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return _origin;
        }
        return _origin.add(_direction.scale(t));
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(_origin, _direction);
    } */
}