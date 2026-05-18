package renderer;

import geometries.api.Intersectable;
import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;

/**
 * Base class for ray tracers.
 */
abstract class RayTracerBase {

    /**
     * The scene rendered by the ray tracer.
     */
    protected final Scene scene;

    /**
     * Constructor.
     *
     * @param scene the scene to render
     */
    RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray and returns the color seen by this ray.
     *
     * @param ray the ray to trace
     * @return the color of the ray
     */
    abstract Color traceRay(Ray ray);

    /**
     * Precalculates the geometric values for the intersection and viewer.
     * Saves the normal, viewing vector, and their dot product in the intersection object.
     *
     * @param intersection the intersection object
     * @param v            the viewing direction vector
     * @return true if the ray is not parallel to the surface (vn != 0), false otherwise
     */
    protected boolean preprocessIntersection(Intersectable.Intersection intersection, Vector v) {
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.v = v;
        intersection.vn = alignZero(intersection.v.dotProduct(intersection.normal));

        return intersection.vn != 0;
    }

    /**
     * Precalculates the geometric values for the intersection and a specific light source.
     * Saves the light direction and its dot product with the normal.
     *
     * @param intersection the intersection object
     * @param light        the light source being processed
     * @return true if the light and viewer are on the same side of the surface, false otherwise
     */
    protected boolean preprocessLightSource(Intersectable.Intersection intersection, lighting.LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.ln = alignZero(intersection.l.dotProduct(intersection.normal));

        // Light source and viewer must be on the same side of the surface
        // otherwise, the light contribution is ignored
        return intersection.ln * intersection.vn > 0;
    }
}