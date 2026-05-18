package primitives;

import geometries.api.Intersectable.Intersection;

import java.util.List;

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
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Ray)) {
            return false;
        }

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

    /**
     * Finds the closest point to the ray origin from a list of points.
     *
     * @param points list of intersection points
     * @return the closest point to the ray origin, or null if the list is null
     */
    public Point findClosestPoint(List<Point> points) {
        Intersection closestIntersection = points == null ? null
                : findClosestIntersection(points.stream()
                .map(point -> new Intersection(null, point))
                .toList());

        return closestIntersection == null ? null : closestIntersection.point;
    }

    /**
     * Finds the closest intersection to the ray origin from a list of intersections.
     *
     * @param intersections list of intersections
     * @return the closest intersection to the ray origin, or null if the list is null
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null) {
            return null;
        }

        Intersection closestIntersection = null;
        double closestDistanceSquared = Double.POSITIVE_INFINITY;

        for (Intersection intersection : intersections) {
            double distanceSquared = intersection.point.distanceSquared(_origin);

            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                closestIntersection = intersection;
            }
        }

        return closestIntersection;
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(_origin, _direction);
    }
    */
}
