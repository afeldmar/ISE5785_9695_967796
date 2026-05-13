package geometries.impl;

import geometries.api.Intersectable;
import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite class for a collection of intersectable geometries.
 */
public class Geometries extends Intersectable {

    /**
     * List of geometries in the collection.
     */
    private final List<Intersectable> geometries = new ArrayList<>();

    /**
     * Constructor that initializes the collection with given geometries.
     *
     * @param geometries geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds geometries to the collection.
     *
     * @param geometries geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Calculates the intersections of a ray with all geometries in the collection.
     * * @param ray the intersecting ray
     * @return a list of intersections, or null if no intersections are found
     */
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;

        for (Intersectable geometry : geometries) {
            // Important: Call the public calcIntersections, NOT the helper directly!
            var geoIntersections = geometry.calcIntersections(ray);

            if (geoIntersections != null) {
                if (intersections == null) {
                    // Create a new list if this is the first intersection found
                    intersections = new java.util.LinkedList<>(geoIntersections);
                } else {
                    // Add to existing list
                    intersections.addAll(geoIntersections);
                }
            }
        }
        return intersections;
    }
}