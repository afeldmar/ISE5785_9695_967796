package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for class {@link Tube}.
 * The tests verify:
 * <ul>
 *   <li>{@link Tube#getNormal(Point)}</li>
 * </ul>
 */
class TubeTests {

    /** Basic default constructor to satisfy documentation tools. */
    TubeTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** Axis ray of the tube. */
    private static final Ray AXIS_RAY = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong exception handling. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception thrown";

    /** Error message for wrong normal. */
    private static final String ERROR_NORMAL = "ERROR: wrong normal";

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Tube tube = new Tube(1d, AXIS_RAY);

        // ============ Equivalence Partitions Tests ==============

        // TC01: point on tube surface in front of the axis ray head
        Point point1 = new Point(1, 0, 2);
        assertDoesNotThrow(() -> tube.getNormal(point1), ERROR_EXCEPTION);
        Vector result1 = tube.getNormal(point1);

        assertEquals(new Vector(1, 0, 0), result1, ERROR_NORMAL);
        assertEquals(1d, result1.length(), DELTA, ERROR_NORMAL);

        // TC02: point on tube surface behind the axis ray head
        Point point2 = new Point(1, 0, -2);
        assertDoesNotThrow(() -> tube.getNormal(point2), ERROR_EXCEPTION);
        Vector result2 = tube.getNormal(point2);

        assertEquals(new Vector(1, 0, 0), result2, ERROR_NORMAL);
        assertEquals(1d, result2.length(), DELTA, ERROR_NORMAL);

        // =============== Boundary Values Tests ==================

        // TC03: point on tube surface opposite the axis ray head
        Point point3 = new Point(1, 0, 0);
        assertDoesNotThrow(() -> tube.getNormal(point3), ERROR_EXCEPTION);
        Vector result3 = tube.getNormal(point3);

        assertEquals(new Vector(1, 0, 0), result3, ERROR_NORMAL);
        assertEquals(1d, result3.length(), DELTA, ERROR_NORMAL);
    }
}