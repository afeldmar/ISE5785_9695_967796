package renderer;

import geometries.api.Intersectable;
import geometries.api.Intersectable.Intersection;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.TargetArea;
import primitives.Vector;
import scene.Scene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static primitives.Util.alignZero;

/**
 * Simple ray tracer.
 */
class SimpleRayTracer extends RayTracerBase {
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
        return calcColor(intersection, ray, maxGlobalEffectsLevel, INITIAL_K)
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
        Material material = intersection.material;
        return calcGlobalEffect(constructReflectedRay(intersection, ray), material.kRBlur, material, level, k, material.kR)
                .add(calcGlobalEffect(constructRefractedRay(intersection, ray), material.kTBlur, material, level, k, material.kT));
    }

    /**
     * Calculates one recursive global effect after pruning insignificant effects.
     *
     * @param ray        ideal secondary ray
     * @param blurRadius material blur radius
     * @param material   material sampling parameters
     * @param level      remaining recursion level
     * @param k          accumulated attenuation
     * @param kx         current effect attenuation
     * @return averaged effect color
     */
    private Color calcGlobalEffect(Ray ray, double blurRadius, Material material, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        return kkx.isLowerThan(MIN_CALC_COLOR_K)
                ? Color.BLACK
                : blurRadius != 0 && adaptiveSuperSamplingLevel > 0
                ? calcAdaptiveGlobalEffect(ray, blurRadius, material, level, kkx, kx)
                : calcGlobalEffect(constructBlurBeam(ray, blurRadius, material), level, kkx, kx);
    }

    /**
     * Calculates one recursive global effect from a beam of secondary rays.
     *
     * @param rays  secondary ray beam
     * @param level remaining recursion level
     * @param kkx   accumulated attenuation including current effect
     * @param kx    current effect attenuation
     * @return averaged effect color
     */
    private Color calcGlobalEffect(List<Ray> rays, int level, Double3 kkx, Double3 kx) {
        Color color = Color.BLACK;
        for (Ray ray : rays) {
            var intersections = scene.geometries.calcIntersections(ray);
            color = color.add(intersections == null
                    ? scene.background.scale(kx)
                    : calcColor(ray.findClosestIntersection(intersections), ray, level - 1, kkx).scale(kx));
        }

        return color.reduce(rays.size());
    }

    /**
     * Builds a beam around an ideal secondary ray when blur is requested.
     *
     * @param ray        ideal reflected or refracted ray
     * @param blurRadius material blur radius
     * @param material   material sampling parameters
     * @return single ideal ray or a sampled beam around it
     */
    private List<Ray> constructBlurBeam(Ray ray, double blurRadius, Material material) {
        return blurRadius == 0
                ? List.of(ray)
                : new TargetArea().generateJittered(material.blurGridSize)
                .constructBeam(ray, material.blurTargetDistance, blurRadius);
    }

    /**
     * Calculates a blurred global effect using adaptive super-sampling over the virtual target area.
     *
     * @param ray        ideal reflected or refracted ray
     * @param blurRadius virtual target area radius
     * @param material   material sampling parameters
     * @param level      remaining color recursion level
     * @param kkx        accumulated attenuation including current effect
     * @param kx         current effect attenuation
     * @return averaged adaptive color
     */
    private Color calcAdaptiveGlobalEffect(Ray ray, double blurRadius, Material material, int level,
                                           Double3 kkx, Double3 kx) {
        int gridSize = 1 << adaptiveSuperSamplingLevel;
        Map<SamplePoint, Color> cache = new HashMap<>();

        return calcAdaptiveGlobalArea(ray, blurRadius, material, level, kkx, kx,
                0, 0, gridSize, gridSize, adaptiveSuperSamplingLevel, gridSize, cache);
    }

    /**
     * Recursively samples one rectangular area of the virtual blur target.
     *
     * @return averaged color for the area
     */
    private Color calcAdaptiveGlobalArea(Ray ray, double blurRadius, Material material, int colorLevel,
                                         Double3 kkx, Double3 kx, int x0, int y0, int x1, int y1,
                                         int samplingLevel, int gridSize, Map<SamplePoint, Color> cache) {
        Color topLeft = calcAdaptiveSample(ray, blurRadius, material, colorLevel, kkx, kx, x0, y0, gridSize, cache);
        Color topRight = calcAdaptiveSample(ray, blurRadius, material, colorLevel, kkx, kx, x1, y0, gridSize, cache);
        Color bottomLeft = calcAdaptiveSample(ray, blurRadius, material, colorLevel, kkx, kx, x0, y1, gridSize, cache);
        Color bottomRight = calcAdaptiveSample(ray, blurRadius, material, colorLevel, kkx, kx, x1, y1, gridSize, cache);

        if (samplingLevel == 0 || topLeft.equalColors(topRight, bottomLeft, bottomRight)) {
            return topLeft.add(topRight, bottomLeft, bottomRight).reduce(4);
        }

        int xMid = (x0 + x1) / 2;
        int yMid = (y0 + y1) / 2;

        return calcAdaptiveGlobalArea(ray, blurRadius, material, colorLevel, kkx, kx,
                x0, y0, xMid, yMid, samplingLevel - 1, gridSize, cache)
                .add(calcAdaptiveGlobalArea(ray, blurRadius, material, colorLevel, kkx, kx,
                                xMid, y0, x1, yMid, samplingLevel - 1, gridSize, cache),
                        calcAdaptiveGlobalArea(ray, blurRadius, material, colorLevel, kkx, kx,
                                x0, yMid, xMid, y1, samplingLevel - 1, gridSize, cache),
                        calcAdaptiveGlobalArea(ray, blurRadius, material, colorLevel, kkx, kx,
                                xMid, yMid, x1, y1, samplingLevel - 1, gridSize, cache))
                .reduce(4);
    }

    /**
     * Calculates or retrieves one cached adaptive sample.
     */
    private Color calcAdaptiveSample(Ray ray, double blurRadius, Material material, int level,
                                     Double3 kkx, Double3 kx, int x, int y, int gridSize,
                                     Map<SamplePoint, Color> cache) {
        SamplePoint point = new SamplePoint(x, y);
        Color cached = cache.get(point);
        if (cached != null) {
            return cached;
        }

        double xOffset = (double) x / gridSize * 2 - 1;
        double yOffset = (double) y / gridSize * 2 - 1;
        Color color = calcGlobalSample(constructBlurRay(ray, material.blurTargetDistance, blurRadius, xOffset, yOffset),
                level, kkx, kx);
        cache.put(point, color);
        return color;
    }

    /**
     * Calculates the color contribution of one secondary global-effect ray.
     */
    private Color calcGlobalSample(Ray ray, int level, Double3 kkx, Double3 kx) {
        var intersections = scene.geometries.calcIntersections(ray);
        return intersections == null
                ? scene.background.scale(kx)
                : calcColor(ray.findClosestIntersection(intersections), ray, level - 1, kkx).scale(kx);
    }

    /**
     * Constructs one ray through an offset in the virtual blur target area.
     */
    private Ray constructBlurRay(Ray centralRay, double targetDistance, double blurRadius,
                                 double xOffset, double yOffset) {
        Point p0 = centralRay.origin();
        Vector dir = centralRay.direction();
        Point target = p0.add(dir.scale(targetDistance));

        Vector vx;
        Vector vUp = Vector.AXIS_Y;
        if (dir.equals(vUp) || dir.equals(vUp.scale(-1))) {
            vUp = Vector.AXIS_X;
        }

        try {
            vx = dir.crossProduct(vUp).normalize();
        } catch (IllegalArgumentException e) {
            vx = Vector.AXIS_X;
        }

        Vector vy = dir.crossProduct(vx).normalize();
        if (xOffset != 0) target = target.add(vx.scale(xOffset * blurRadius));
        if (yOffset != 0) target = target.add(vy.scale(yOffset * blurRadius));

        return new Ray(p0, target.subtract(p0).normalize());
    }

    /** Integer target-area coordinate used as an adaptive sample cache key. */
    private record SamplePoint(int x, int y) {}

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
