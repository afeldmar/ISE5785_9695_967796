package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for class {@link Triangle}.
 * The tests verify:
 * <ul>
 *   <li>{@link Triangle#getNormal(Point)}</li>
 *   <li>{@link Triangle#findIntersections(Ray)}</li>
 * </ul>
 */
class TriangleTests {

    /** Basic default constructor to satisfy documentation tools. */
    TriangleTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** First vertex of the triangle. */
    private static final Point POINT1 = new Point(0, 0, 1);

    /** Second vertex of the triangle. */
    private static final Point POINT2 = new Point(1, 0, 1);

    /** Third vertex of the triangle. */
    private static final Point POINT3 = new Point(0, 1, 1);

    /** A point on the triangle. */
    private static final Point POINT_ON_TRIANGLE = new Point(0.25, 0.25, 1);

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong exception handling. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception thrown";

    /** Error message for wrong normal. */
    private static final String ERROR_NORMAL = "ERROR: wrong normal";

    /** Error message for wrong triangle intersection result. */
    private static final String ERROR_INTERSECTION = "ERROR: wrong triangle intersection result";

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Triangle triangle = new Triangle(POINT1, POINT2, POINT3);

        // Two vectors lying in the triangle plane
        Vector v1 = POINT2.subtract(POINT1);
        Vector v2 = POINT3.subtract(POINT1);

        // ============ Equivalence Partitions Tests ==============

        // TC01: get normal at a point on the triangle
        assertDoesNotThrow(() -> triangle.getNormal(POINT_ON_TRIANGLE), ERROR_EXCEPTION);
        Vector result = triangle.getNormal(POINT_ON_TRIANGLE);

        // Ensure |n| = 1
        assertEquals(1d, result.length(), DELTA, ERROR_NORMAL);

        // Ensure normal is orthogonal to the triangle plane
        assertEquals(0d, result.dotProduct(v1), DELTA, ERROR_NORMAL);
        assertEquals(0d, result.dotProduct(v2), DELTA, ERROR_NORMAL);

        // =============== Boundary Values Tests ==================
    }

    /**
     * Test method for {@link Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(POINT1, POINT2, POINT3);

        // ============ Equivalence Partitions Tests ==============

        // EP01: Ray intersects inside the triangle (1 point)
        List<Point> result = triangle.findIntersections(
                new Ray(new Point(0.25, 0.25, 0), new Vector(0, 0, 1))
        );
        assertNotNull(result, ERROR_INTERSECTION);
        assertEquals(1, result.size(), ERROR_INTERSECTION);
        assertEquals(List.of(new Point(0.25, 0.25, 1)), result, ERROR_INTERSECTION);

        // EP02: Ray intersects the plane outside the triangle, against edge
        assertNull(
                triangle.findIntersections(new Ray(new Point(1, 1, 0), new Vector(0, 0, 1))),
                ERROR_INTERSECTION
        );

        // EP03: Ray intersects the plane outside the triangle, against vertex
        assertNull(
                triangle.findIntersections(new Ray(new Point(-0.5, -0.5, 0), new Vector(0, 0, 1))),
                ERROR_INTERSECTION
        );

        // =============== Boundary Values Tests ==================

        // BV01: Ray intersects exactly on edge
        assertNull(
                triangle.findIntersections(new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1))),
                ERROR_INTERSECTION
        );

        // BV02: Ray intersects exactly on vertex
        assertNull(
                triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))),
                ERROR_INTERSECTION
        );

        // BV03: Ray intersects on edge continuation
        assertNull(
                triangle.findIntersections(new Ray(new Point(1.5, 0, 0), new Vector(0, 0, 1))),
                ERROR_INTERSECTION
        );
    }
}