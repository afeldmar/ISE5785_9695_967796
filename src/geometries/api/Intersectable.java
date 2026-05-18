package geometries.api;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import java.util.List;

public abstract class Intersectable {

    /**
     * Passive Data Structure (PDS) representing an intersection point and the geometry it belongs to.
     */
    public static class Intersection {
        /** The geometry that was intersected */
        public final Geometry geometry;
        /** The intersection point in 3D space */
        public final Point point;
        /** The emission color of the intersected geometry */
        public final Color emission;
        /** The material of the intersected geometry */
        public final Material material;

        /**
         * Constructs an Intersection object.
         *
         * @param geometry the intersected geometry
         * @param point    the point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.emission = geometry == null ? Color.BLACK : geometry.getEmission();
            this.material = geometry == null ? new Material() : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof Intersection other) {
                // Note: Geometry comparison is by reference (==), point comparison is by equals()
                return this.geometry == other.geometry && this.point.equals(other.point);
            }
            return false;
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    /**
     * Finds all intersections of a ray with the geometry.
     * Preserves backward compatibility by returning a list of Points.
     *
     * @param ray the intersecting ray
     * @return a list of intersection points, or null if no intersections are found
     */
    public final List<Point> findIntersections(Ray ray) {
        var intersections = calcIntersections(ray);
        return intersections == null ? null
                : intersections.stream()
                .map(intersection -> intersection.point)
                .toList();
    }

    /**
     * Calculates the intersections of a ray with the geometry, including the geometry object itself.
     * This public final method implements the NVI (Non-Virtual Interface) pattern.
     *
     * @param ray the intersecting ray
     * @return a list of Intersection objects, or null if no intersections
     */
    public final List<Intersection> calcIntersections(Ray ray) {
        return calcIntersectionsHelper(ray);
    }

    /**
     * Helper method to calculate intersections.
     * Must be implemented by all concrete geometries.
     *
     * @param ray the intersecting ray
     * @return a list of Intersection objects, or null if no intersections
     */
    protected abstract List<Intersection> calcIntersectionsHelper(Ray ray);
}
