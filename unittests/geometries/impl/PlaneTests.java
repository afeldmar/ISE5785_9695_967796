package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for class {@link Plane}.
 * The tests verify:
 * <ul>
 *   <li>Constructor {@link Plane#Plane(Point, Vector)}</li>
 *   <li>Constructor {@link Plane#Plane(Point, Point, Point)}</li>
 *   <li>{@link Plane#getNormal(Point)}</li>
 * </ul>
 */
class PlaneTests {

    /** Basic default constructor to satisfy documentation tools. */
    PlaneTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** First reference point on the plane z = 1. */
    private static final Point POINT1 = new Point(0, 0, 1);

    /** Second reference point on the plane z = 1. */
    private static final Point POINT2 = new Point(1, 0, 1);

    /** Third reference poinAt on the plane z = 1. */
    private static final Point POINT3 = new Point(0, 1, 1);

    /** Additional point on the plane z = 1. */
    private static final Point POINT_IN_PLANE = new Point(3, 4, 1);

    /** A point used for point-vector plane constructor. */
    private static final Point ORIGIN = new Point(0, 0, 0);

    /** Non-unit normal vector for constructor test. */
    private static final Vector VECTOR_Z_5 = new Vector(0, 0, 5);

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong exception handling. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception thrown";

    /** Error message for wrong plane construction. */
    private static final String ERROR_PLANE = "ERROR: wrong plane";

    /** Error message for wrong normal. */
    private static final String ERROR_NORMAL = "ERROR: wrong normal";

    /**
     * Test method for {@link Plane#Plane(Point, Vector)}.
     * Verifies that the constructor normalizes the normal vector.
     */
    @Test
    void testConstructorPointVector() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Construct plane with a non-unit normal vector
        Plane plane = new Plane(ORIGIN, VECTOR_Z_5);
        assertDoesNotThrow(() -> plane.getNormal(ORIGIN), ERROR_EXCEPTION);
        Vector result = plane.getNormal(ORIGIN);

        // Ensure |n| = 1
        assertEquals(1d, result.length(), DELTA, ERROR_NORMAL);

        // =============== Boundary Values Tests ==================
    }

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     * Verifies correct and incorrect plane constructions.
     */
    @Test
    void testConstructorThreePoints() {
        // ============ Equivalence Partitions Tests ==============

        // EP01: Correct plane with three different non-collinear points
        assertDoesNotThrow(() -> new Plane(POINT1, POINT2, POINT3), ERROR_EXCEPTION);

        // =============== Boundary Values Tests ==================

        // BV01: First and second points coincide
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(POINT1, POINT1, POINT3), ERROR_PLANE);

        // BV02: First and third points coincide
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(POINT1, POINT2, POINT1), ERROR_PLANE);

        // BV03: Second and third points coincide
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(POINT1, POINT2, POINT2), ERROR_PLANE);

        // BV04: All three points coincide
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(POINT1, POINT1, POINT1), ERROR_PLANE);

        // BV05: Three points are on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(
                        new Point(0, 0, 0),
                        new Point(1, 1, 1),
                        new Point(2, 2, 2)),
                ERROR_PLANE);
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormalPoint() {
        Plane plane = new Plane(POINT1, POINT2, POINT3);

        // Two edges lying in the plane
        Vector edge1 = POINT2.subtract(POINT1);
        Vector edge2 = POINT3.subtract(POINT1);

        // ============ Equivalence Partitions Tests ==============

        // EP01: Get normal at a regular point on the plane that is not the reference point
        assertDoesNotThrow(() -> plane.getNormal(POINT_IN_PLANE), ERROR_EXCEPTION);
        Vector result = plane.getNormal(POINT_IN_PLANE);

        // Ensure |n| = 1
        assertEquals(1d, result.length(), DELTA, ERROR_NORMAL);

        // Ensure normal is orthogonal to plane edges
        assertEquals(0d, result.dotProduct(edge1), DELTA, ERROR_NORMAL);
        assertEquals(0d, result.dotProduct(edge2), DELTA, ERROR_NORMAL);

        // =============== Boundary Values Tests ==================

        // BV01: Get normal at the reference point of the plane
        assertDoesNotThrow(() -> plane.getNormal(POINT1), ERROR_EXCEPTION);
        result = plane.getNormal(POINT1);

        // Ensure |n| = 1
        assertEquals(1d, result.length(), DELTA, ERROR_NORMAL);

        // Ensure normal is orthogonal to plane edges
        assertEquals(0d, result.dotProduct(edge1), DELTA, ERROR_NORMAL);
        assertEquals(0d, result.dotProduct(edge2), DELTA, ERROR_NORMAL);
    }
}