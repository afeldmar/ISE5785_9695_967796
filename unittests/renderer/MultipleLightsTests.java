package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.api.Geometry;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

/**
 * Tests for rendering scenes with multiple light sources.
 */
class MultipleLightsTests {

    /** Constant for tests resolution */
    private static final int RESOLUTION = 500;

    /**
     * Produce a picture of a sphere lighted by multiple light sources.
     * (Directional, Point, and Spot lights)
     */
    @Test
    void testSphereMultipleLights() {
        Scene scene = new Scene("Test scene")
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

        Camera.Builder camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpSize(150, 150).setVpDistance(1000);

        Geometry sphere = new Sphere(new Point(0, 0, -50), 50D)
                .setEmission(new Color(BLUE).reduce(2))
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(300));

        scene.geometries.add(sphere);

        // 1. Directional Light (Dim reddish light from the top right)
        scene.lights.add(new DirectionalLight(new Color(150, 50, 50), new Vector(1, -1, -1)));

        // 2. Point Light (Greenish light from the bottom left)
        scene.lights.add(new PointLight(new Color(0, 255, 0), new Point(-50, -50, 50))
                .setKl(0.00001).setKq(0.000001));

        // 3. Spot Light (Strong white/yellow light focused on the center)
        scene.lights.add(new SpotLight(new Color(255, 255, 200), new Point(50, 50, 50), new Vector(-1, -1, -2))
                .setKl(0.0001).setKq(0.000005));

        camera.setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("lightSphereMultiple");
    }

    /**
     * Produce a picture of two triangles lighted by multiple light sources.
     * (Directional, Point, and Spot lights)
     */
    @Test
    void testTrianglesMultipleLights() {
        Scene scene = new Scene("Test scene")
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

        Camera.Builder camera = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpSize(200, 200).setVpDistance(1000);

        Point[] vertices = {
                new Point(-110, -110, -150),
                new Point(95, 100, -150),
                new Point(110, -110, -150),
                new Point(-75, 78, 100)
        };

        Material material = new Material().setKD(0.5).setKS(0.5).setShininess(300);

        Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2]).setMaterial(material);
        Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3]).setMaterial(material);

        scene.geometries.add(triangle1, triangle2);

        // 1. Directional Light (Blueish light from the left)
        scene.lights.add(new DirectionalLight(new Color(50, 50, 200), new Vector(1, 0, -1)));

        // 2. Point Light (Reddish light inside the crease)
        scene.lights.add(new PointLight(new Color(255, 50, 50), new Point(10, -10, -130))
                .setKl(0.0005).setKq(0.0005));

        // 3. Spot Light (Strong green light from the front right)
        scene.lights.add(new SpotLight(new Color(0, 255, 0), new Point(40, 40, 100), new Vector(-2, -2, -3))
                .setKl(0.0001).setKq(0.00001));

        camera.setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("lightTrianglesMultiple");
    }
}