package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;

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
}