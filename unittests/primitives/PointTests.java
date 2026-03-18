package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link primitives.Point} class.
 */
class PointTests {

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(-1, -2, -3);
        Vector v = new Vector(1, 1, 1);
        Vector vOpposite = new Vector(-1, -2, -3);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Test that adding a vector to a point results in the correct new point.
        assertEquals(new Point(2, 3, 4), p1.add(v),
                "ERROR: Point + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // BV01: Test adding a vector that results in the origin point (0,0,0).
        assertEquals(new Point(0, 0, 0), p1.add(vOpposite),
                "ERROR: Point + Vector resulting in origin does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 3, 4);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Test that subtracting a point from another point results in the correct vector.
        assertEquals(new Vector(1, 1, 1), p2.subtract(p1),
                "ERROR: Point - Point does not work correctly");

        // =============== Boundary Values Tests ==================
        // BV01: Test subtracting a point from itself (should throw exception since Vector zero is invalid).
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "ERROR: Point - itself must throw IllegalArgumentException");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple distance squared calculation
        assertEquals(9.0, p1.distanceSquared(p2), DELTA,
                "ERROR: distanceSquared() wrong result");

        // =============== Boundary Values Tests ==================
        // BV01: Distance squared to itself is 0
        assertEquals(0.0, p1.distanceSquared(p1), DELTA,
                "ERROR: distanceSquared() to itself must be 0");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(1, 6, 6); // Distance is 5 (sqrt of 0^2 + 4^2 + 3^2)

        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple distance calculation
        assertEquals(5.0, p1.distance(p2), DELTA,
                "ERROR: distance() wrong result");

        // =============== Boundary Values Tests ==================
        // BV01: Distance to itself is 0
        assertEquals(0.0, p1.distance(p1), DELTA,
                "ERROR: distance() to itself must be 0");
    }
}