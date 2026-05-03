package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

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
}