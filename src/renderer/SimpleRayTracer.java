package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

/**
 * Simple ray tracer.
 */
class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor.
     *
     * @param scene the scene to render
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the color at an intersection point.
     * In stage 5, only ambient light is used.
     *
     * @param intersection the closest intersection point
     * @return the ambient light color
     */
    private Color calcColor(Point intersection) {
        return scene.ambientLight.getIntensity();
    }

    /**
     * Traces a ray through the scene.
     * If there is no intersection, returns the background color.
     * If there is an intersection, returns the ambient light color.
     *
     * @param ray the ray from the camera
     * @return the final pixel color
     */
    @Override
    Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);

        Point closestPoint = ray.findClosestPoint(intersections);

        return closestPoint == null
                ? scene.background
                : calcColor(closestPoint);
    }
}