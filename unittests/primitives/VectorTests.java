package primitives;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link primitives.Vector} class.
 */
class VectorTests {

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Vector (1, 2, 3) used in several tests */
    private static final Vector v1 = new Vector(1, 2, 3);
    /** Vector (-2, -4, -6) used in several tests */
    private static final Vector v2 = new Vector(-2, -4, -6);
    /** Vector (0, 3, -2) orthogonal to v1 */
    private static final Vector v3 = new Vector(0, 3, -2);

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple addition
        assertEquals(new Vector(1, 5, 1), v1.add(new Vector(0, 3, -2)),
                "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // BV01: Add opposite vector (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1, -2, -3)),
                "ERROR: Vector + opposite vector must throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Simple subtraction
        assertEquals(new Vector(3, 6, 9), v1.subtract(v2),
                "ERROR: Vector - Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // BV01: Subtract same vector (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1),
                "ERROR: Vector - itself must throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Scale by a positive number
        assertEquals(new Vector(2, 4, 6), v1.scale(2), "ERROR: scale() wrong result");

        // EP02: Scale by a negative number
        assertEquals(new Vector(-1, -2, -3), v1.scale(-1), "ERROR: scale() wrong result");

        // =============== Boundary Values Tests ==================
        // BV01: Scale by zero (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0),
                "ERROR: scale by 0 must throw exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Dot product of orthogonal vectors
        assertEquals(0, v1.dotProduct(v3), DELTA, "ERROR: dotProduct() for orthogonal vectors is not zero");

        // EP02: Dot product of vectors with acute angle
        assertEquals(-28, v1.dotProduct(v2), DELTA, "ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Cross product of orthogonal vectors
        Vector vr = v1.crossProduct(v3);

        assertEquals(v1.length() * v3.length(), vr.length(), DELTA,
                "ERROR: crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(v1), DELTA,
                "ERROR: crossProduct() result is not orthogonal to its operands");
        assertEquals(0, vr.dotProduct(v3), DELTA,
                "ERROR: crossProduct() result is not orthogonal to its operands");

        // =============== Boundary Values Tests ==================
        // BV01: Cross product of parallel vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2),
                "ERROR: crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: length squared of a vector
        assertEquals(14, v1.lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: length of a vector
        assertEquals(5, new Vector(0, 3, 4).length(), DELTA, "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(1, 2, 3);
        Vector n = v.normalize();

        // ============ Equivalence Partitions Tests ==============
        // EP01: Test that the vector is normalized correctly
        assertEquals(1, n.length(), DELTA, "ERROR: the normalized vector is not a unit vector");

        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(n),
                "ERROR: the normalized vector is not parallel to the original one");

        assertTrue(v.dotProduct(n) > 0,
                "ERROR: the normalized vector is opposite to the original one");
    }
}