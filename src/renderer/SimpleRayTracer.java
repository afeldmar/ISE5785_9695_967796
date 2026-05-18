package renderer;

import geometries.api.Intersectable.Intersection;
import primitives.Color;
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
     * In stage 6, emission and ambient light attenuated by material kA are used.
     *
     * @param intersection the closest intersection point
     * @return the calculated color
     */
    private Color calcColor(Intersection intersection) {
        return intersection.emission.add(scene.ambientLight.getIntensity().scale(intersection.material.kA));
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
        Intersection intersection = ray.findClosestIntersection(scene.geometries.calcIntersections(ray));

        return intersection == null
                ? scene.background
                : calcColor(intersection);
    }
}
