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

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;

        for (Intersectable geometry : geometries) {
            List<Point> currentIntersections = geometry.findIntersections(ray);

            if (currentIntersections != null) {
                if (intersections == null) {
                    intersections = new ArrayList<>();
                }
                intersections.addAll(currentIntersections);
            }
        }

        return intersections;
    }
}