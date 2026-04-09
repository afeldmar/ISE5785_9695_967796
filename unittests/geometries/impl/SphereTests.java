package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;


/**
 * Unit tests for class {@link Sphere}.
 * The tests verify:
 * <ul>
 *   <li>{@link Sphere#getNormal(Point)}</li>
 * </ul>
 */
class SphereTests {

    /** Basic default constructor to satisfy documentation tools. */
    SphereTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** Center point of the sphere. */
    private static final Point CENTER = new Point(1, 1, 1);

    /** A point on the sphere surface. */
    private static final Point POINT_ON_SPHERE = new Point(2, 1, 1);

    /** Expected normal vector at the tested point. */
    private static final Vector EXPECTED_NORMAL = new Vector(1, 0, 0);

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong exception handling. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception thrown";

    /** Error message for wrong normal. */
    private static final String ERROR_NORMAL = "ERROR: wrong normal";

    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Sphere sphere = new Sphere(CENTER, 1d);

        // ============ Equivalence Partitions Tests ==============

        // TC01: get normal at a regular point on the sphere surface
        assertDoesNotThrow(() -> sphere.getNormal(POINT_ON_SPHERE), ERROR_EXCEPTION);
        Vector result = sphere.getNormal(POINT_ON_SPHERE);

        assertEquals(EXPECTED_NORMAL, result, ERROR_NORMAL);
        assertEquals(1d, result.length(), DELTA, ERROR_NORMAL);

        // =============== Boundary Values Tests ==================
    }

    /** Sphere used in some tests */
    private static final Sphere SPHERE = new Sphere(new Point(1, 0, 0), 1d);
    /** Error message for sphere intersection failures */
    private static final String ERROR_SPHERE_INTERSECTION = "Wrong sphere intersection result";

    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray's line is outside the sphere (0 points)
        assertNull(SPHERE.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                ERROR_SPHERE_INTERSECTION);

        // EP02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result1 = SPHERE.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));

        assertNotNull(result1, ERROR_SPHERE_INTERSECTION);
        assertEquals(2, result1.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p1, p2), result1, ERROR_SPHERE_INTERSECTION);

        // EP03: Ray starts inside the sphere (1 point)
        // The ray starts at (1.5, 0, 0) inside the sphere and goes right,
        // so it will intersect the sphere only once at the exit point (2, 0, 0).
        Point p3 = new Point(2, 0, 0);
        List<Point> result3 = SPHERE.findIntersections(new Ray(new Point(1.5, 0, 0), new Vector(1, 0, 0)));

        assertNotNull(result3, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result3.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p3), result3, ERROR_SPHERE_INTERSECTION);

        // EP04: Ray starts after the sphere (0 points)
        // The ray starts at (3, 0, 0) which is outside the sphere,
        // and goes right away from it, so there are no intersections.
        assertNull(SPHERE.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))),
                ERROR_SPHERE_INTERSECTION);

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // BV11: Ray starts at sphere and goes inside (1 point)
        List<Point> result11 = SPHERE.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 0)));
        assertNotNull(result11, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result11.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(new Point(1, 1, 0)), result11, ERROR_SPHERE_INTERSECTION);

        // BV12: Ray starts at sphere and goes outside (0 points)
        assertNull(SPHERE.findIntersections(new Ray(new Point(0, 0, 0), new Vector(-1, -1, 0))),
                ERROR_SPHERE_INTERSECTION);

        // **** Group 2: Ray's line goes through the center
        // BV21: Ray starts before the sphere (2 points)
        Point p0_0_0 = new Point(0, 0, 0);
        Point p2_0_0 = new Point(2, 0, 0);
        List<Point> result21 = SPHERE.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0)));
        assertNotNull(result21, ERROR_SPHERE_INTERSECTION);
        assertEquals(2, result21.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p0_0_0, p2_0_0), result21, ERROR_SPHERE_INTERSECTION);

        // BV22: Ray starts at sphere and goes inside (1 point)
        List<Point> result22 = SPHERE.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)));
        assertNotNull(result22, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result22.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p2_0_0), result22, ERROR_SPHERE_INTERSECTION);

        // BV23: Ray starts inside (1 point)
        List<Point> result23 = SPHERE.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0)));
        assertNotNull(result23, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result23.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p2_0_0), result23, ERROR_SPHERE_INTERSECTION);

        // BV24: Ray starts at the center (1 point)
        List<Point> result24 = SPHERE.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)));
        assertNotNull(result24, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result24.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p2_0_0), result24, ERROR_SPHERE_INTERSECTION);

        // BV25: Ray starts at sphere and goes outside (0 points)
        assertNull(SPHERE.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))), ERROR_SPHERE_INTERSECTION);

        // BV26: Ray starts after sphere (0 points)
        assertNull(SPHERE.findIntersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))), ERROR_SPHERE_INTERSECTION);

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // BV31: Ray starts before the tangent point
        assertNull(SPHERE.findIntersections(new Ray(new Point(0, 1, 0), new Vector(1, 0, 0))),
                ERROR_SPHERE_INTERSECTION);

        // BV32: Ray starts at the tangent point
        assertNull(SPHERE.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 0, 0))),
                ERROR_SPHERE_INTERSECTION);

        // BV33: Ray starts after the tangent point
        assertNull(SPHERE.findIntersections(new Ray(new Point(2, 1, 0), new Vector(1, 0, 0))),
                ERROR_SPHERE_INTERSECTION);

        // **** Group 4: Special cases
        // BV41: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(SPHERE.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(0, 0, 1))), ERROR_SPHERE_INTERSECTION);

        // BV42: Ray's line crosses sphere, ray starts inside, orthogonal to ray start to center line
        Point p42 = new Point(0.5, 0, Math.sqrt(0.75));
        List<Point> result42 = SPHERE.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(0, 0, 1)));
        assertNotNull(result42, ERROR_SPHERE_INTERSECTION);
        assertEquals(1, result42.size(), ERROR_SPHERE_INTERSECTION);
        assertEquals(List.of(p42), result42, ERROR_SPHERE_INTERSECTION);
    }
}

