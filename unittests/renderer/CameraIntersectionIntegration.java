package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.api.Intersectable;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Integration tests between Camera ray construction and geometry intersections.
 */
class CameraIntersectionIntegration {

    private static final int NX = 3;
    private static final int NY = 3;

    private static final Point P0 = Point.ZERO;
    private static final Vector V_TO = new Vector(0, 0, -1);
    private static final Vector V_UP = new Vector(0, 1, 0);

    private final Camera camera1 = Camera.getBuilder()
            .setLocation(P0)
            .setDirection(V_TO, V_UP)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(NX, NY)
            .build();

    private final Camera camera2 = Camera.getBuilder()
            .setLocation(new Point(0, 0, 0.5))
            .setDirection(V_TO, V_UP)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(NX, NY)
            .build();

    /**
     * Counts all intersections of all camera rays with a geometry body.
     */
    private void assertIntersectionsCount(Camera camera, Intersectable body, int expectedCount, String testName) {
        int count = 0;

        for (int i = 0; i < NY; i++) {
            for (int j = 0; j < NX; j++) {
                Ray ray = camera.constructRay(j, i);
                List<Point> intersections = body.findIntersections(ray);

                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }

        assertEquals(expectedCount, count, testName);
    }

    @Test
    void testCameraRaySphereIntegration() {
        Sphere sphere1 = new Sphere(new Point(0, 0, -3), 1);
        assertIntersectionsCount(camera1, sphere1, 2, "Sphere TC01 failed");

        Sphere sphere2 = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertIntersectionsCount(camera2, sphere2, 18, "Sphere TC02 failed");

        Sphere sphere3 = new Sphere(new Point(0, 0, -2), 2);
        assertIntersectionsCount(camera2, sphere3, 10, "Sphere TC03 failed");

        Sphere sphere4 = new Sphere(new Point(0, 0, -1), 4);
        assertIntersectionsCount(camera2, sphere4, 9, "Sphere TC04 failed");

        Sphere sphere5 = new Sphere(new Point(0, 0, 1), 0.5);
        assertIntersectionsCount(camera1, sphere5, 0, "Sphere TC05 failed");
    }

    @Test
    void testCameraRayPlaneIntegration() {
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        assertIntersectionsCount(camera1, plane1, 9, "Plane TC01 failed");

        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 2));
        assertIntersectionsCount(camera1, plane2, 9, "Plane TC02 failed");

        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(0, 3, 1));
        assertIntersectionsCount(camera1, plane3, 6, "Plane TC03 failed");
    }

    @Test
    void testCameraRayTriangleIntegration() {
        Triangle triangle1 = new Triangle(
                new Point(0, 1, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        assertIntersectionsCount(camera1, triangle1, 1, "Triangle TC01 failed");

        Triangle triangle2 = new Triangle(
                new Point(0, 20, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        assertIntersectionsCount(camera1, triangle2, 2, "Triangle TC02 failed");
    }
}