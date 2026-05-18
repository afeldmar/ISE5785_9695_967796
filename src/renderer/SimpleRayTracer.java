package renderer;

import geometries.api.Intersectable;
import geometries.api.Intersectable.Intersection;
import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;

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

    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.calcIntersections(ray);
        return intersections == null ? scene.background
                : calcColor(ray.findClosestIntersection(intersections), ray.direction());
    }

    /**
     * Calculates the color of a specific intersection point.
     *
     * @param intersection the intersection point and its geometry
     * @param v            the direction of the ray hitting the point
     * @return the calculated color
     */
    private Color calcColor(Intersectable.Intersection intersection, Vector v) {
        if (!preprocessIntersection(intersection, v)) {
            return Color.BLACK;
        }

        return scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcLocalEffects(intersection));
    }

    /**
     * Calculates the local effects (Emission, Diffuse, and Specular) of all light sources.
     *
     * @param intersection the intersection object containing the cached data
     * @return the combined color from all local light effects
     */
    private Color calcLocalEffects(Intersectable.Intersection intersection) {
        Color color = intersection.geometry.getEmission();

        for (lighting.LightSource lightSource : scene.lights) {
            if (preprocessLightSource(intersection, lightSource)) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);

                // Add the diffuse and specular components scaled by the light intensity
                color = color.add(
                        lightIntensity.scale(calcDiffuse(intersection)),
                        lightIntensity.scale(calcSpecular(intersection))
                );
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse reflection component of the Phong model.
     * Formula: kD * |l * n|
     *
     * @param intersection the intersection object
     * @return the diffuse scaling factor
     */
    private primitives.Double3 calcDiffuse(Intersectable.Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.ln));
    }

    /**
     * Calculates the specular reflection component of the Phong model.
     * Formula: kS * (max(0, -v * r)) ^ nSh
     *
     * @param intersection the intersection object
     * @return the specular scaling factor
     */
    private primitives.Double3 calcSpecular(Intersectable.Intersection intersection) {
        // r = l - 2 * (l * n) * n
        Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.ln)).normalize();

        // -v * r
        double minusVR = alignZero(intersection.v.scale(-1).dotProduct(r));

        if (minusVR <= 0) {
            return primitives.Double3.ZERO;
        }

        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
    }
}
