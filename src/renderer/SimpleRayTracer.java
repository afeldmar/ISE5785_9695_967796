package renderer;

import geometries.api.Intersectable;
import geometries.api.Intersectable.Intersection;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;

/**
 * Simple ray tracer.
 */
class SimpleRayTracer extends RayTracerBase {
    /** Maximal recursion level for global effects */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /** Minimal contribution considered significant */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /** Initial accumulated attenuation */
    private static final Double3 INITIAL_K = Double3.ONE;

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
                : calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Calculates the color of a specific intersection point.
     *
     * @param intersection the intersection point and its geometry
     * @param ray          the ray hitting the point
     * @return the calculated color
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity().scale(intersection.material.kA));
    }

    /**
     * Calculates the recursive color of a specific intersection point.
     *
     * @param intersection the intersection point and its geometry
     * @param ray          the ray hitting the point
     * @param level        remaining recursion level
     * @param k            accumulated attenuation
     * @return the calculated color
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray, int level, Double3 k) {
        if (!preprocessIntersection(intersection, ray.direction())) {
            return Color.BLACK;
        }

        Color color = calcLocalEffects(intersection, k);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * Calculates the local effects (Emission, Diffuse, and Specular) of all light sources.
     *
     * @param intersection the intersection object containing the cached data
     * @param k            accumulated attenuation
     * @return the combined color from all local light effects
     */
    private Color calcLocalEffects(Intersectable.Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();

        for (lighting.LightSource lightSource : scene.lights) {
            if (preprocessLightSource(intersection, lightSource)) {
                Double3 ktr = transparency(intersection);
                if (!ktr.product(k).isLowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);

                    color = color.add(
                            lightIntensity.scale(calcDiffuse(intersection)),
                            lightIntensity.scale(calcSpecular(intersection))
                    );
                }
            }
        }
        return color;
    }

    /**
     * Calculates recursive global effects.
     *
     * @param intersection the intersection object
     * @param ray          the ray hitting the point
     * @param level        remaining recursion level
     * @param k            accumulated attenuation
     * @return global reflection and transparency color
     */
    private Color calcGlobalEffects(Intersectable.Intersection intersection, Ray ray, int level, Double3 k) {
        return calcGlobalEffect(constructReflectedRay(intersection, ray), level, k, intersection.material.kR)
                .add(calcGlobalEffect(constructRefractedRay(intersection, ray), level, k, intersection.material.kT));
    }

    /**
     * Calculates one recursive global effect.
     *
     * @param ray   secondary ray
     * @param level remaining recursion level
     * @param k     accumulated attenuation
     * @param kx    current effect attenuation
     * @return effect color
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.isLowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        var intersections = scene.geometries.calcIntersections(ray);
        if (intersections == null) {
            return scene.background.scale(kx);
        }

        Intersection intersection = ray.findClosestIntersection(intersections);
        return calcColor(intersection, ray, level - 1, kkx).scale(kx);
    }

    /**
     * Calculates transparency toward the current light source.
     *
     * @param intersection the intersection object
     * @return transparency attenuation along the light ray
     */
    private Double3 transparency(Intersectable.Intersection intersection) {
        Vector lightDirection = intersection.l.scale(-1);
        Ray lightRay = new Ray(intersection.point, lightDirection, intersection.normal);
        double lightDistance = intersection.light.getDistance(intersection.point);
        var intersections = scene.geometries.calcIntersections(lightRay);

        if (intersections == null) {
            return Double3.ONE;
        }

        Double3 ktr = Double3.ONE;
        for (Intersection blocker : intersections) {
            if (alignZero(blocker.point.distance(intersection.point) - lightDistance) <= 0) {
                ktr = ktr.product(blocker.material.kT);
                if (ktr.isLowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }

        return ktr;
    }

    /**
     * Constructs a reflected ray.
     *
     * @param intersection the intersection object
     * @param ray          the incoming ray
     * @return reflected ray
     */
    private Ray constructReflectedRay(Intersectable.Intersection intersection, Ray ray) {
        Vector v = ray.direction();
        Vector n = intersection.normal;
        double vn = alignZero(v.dotProduct(n));
        Vector reflectedDirection = v.subtract(n.scale(2 * vn));
        return new Ray(intersection.point, reflectedDirection, n);
    }

    /**
     * Constructs a refracted ray.
     *
     * @param intersection the intersection object
     * @param ray          the incoming ray
     * @return refracted ray
     */
    private Ray constructRefractedRay(Intersectable.Intersection intersection, Ray ray) {
        return new Ray(intersection.point, ray.direction(), intersection.normal);
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
