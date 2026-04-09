package geometries.api;

import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * Interface/Abstract class for all geometries that can be intersected by a Ray.
 */
public abstract class Intersectable {

    /**
     * Finds intersections of a ray with geometric objects.
     * * @param ray the ray intersecting the geometry
     * @return list of intersection points
     */
    public abstract List<Point> findIntersections(Ray ray);
}