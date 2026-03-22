package primitives;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link primitives.Ray} class.
 */
class RayTests {

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong origin. */
    private static final String ERROR_ORIGIN = "ERROR: Ray constructor did not keep the origin correctly";

    /** Error message for wrong direction. */
    private static final String ERROR_DIRECTION = "ERROR: Ray constructor did not normalize the direction correctly";

    /**
     * Test method for {@link primitives.Ray#Ray(primitives.Point, primitives.Vector)}.
     */
    @Test
    void testConstructor() {
        Point origin = new Point(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============

        // EP01: Constructor should normalize a non-unit direction vector
        Vector direction = new Vector(0, 3, 4); // length = 5
        Ray ray = new Ray(origin, direction);

        assertEquals(origin, ray.origin(), ERROR_ORIGIN);
        assertEquals(new Vector(0, 3, 4).normalize(), ray.direction(), ERROR_DIRECTION);
        assertEquals(1d, ray.direction().length(), DELTA,
                "ERROR: Ray direction is not normalized");

        // =============== Boundary Values Tests ==================

        // BV01: Constructor with an already normalized direction vector
        Vector unitDirection = new Vector(0, 0, 1);
        Ray ray2 = new Ray(origin, unitDirection);

        assertEquals(origin, ray2.origin(), ERROR_ORIGIN);
        assertEquals(unitDirection, ray2.direction(), ERROR_DIRECTION);
        assertEquals(1d, ray2.direction().length(), DELTA,
                "ERROR: Ray direction should remain a unit vector");
    }
}