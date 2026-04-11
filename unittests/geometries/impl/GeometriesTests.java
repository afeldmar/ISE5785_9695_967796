package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for class {@link Geometries}.
 */
class GeometriesTests {

    /** Basic default constructor to satisfy documentation tools. */
    GeometriesTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** Error message for wrong geometries intersection result. */
    private static final String ERROR_INTERSECTION = "ERROR: wrong Geometries intersection result";

    /**
     * Test method for {@link Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Plane plane1 = new Plane(
                new Point(0, 0, 1),
                new Vector(0, 0, 1)
        ); // z = 1

        Plane plane2 = new Plane(
                new Point(0, 0, 2),
                new Vector(0, 0, 1)
        ); // z = 2

        Plane plane3 = new Plane(
                new Point(0, 0, 3),
                new Vector(0, 0, 1)
        ); // z = 3

        // ============ Equivalence Partitions Tests ==============

        // EP01: Some geometries are intersected
        Geometries geometries1 = new Geometries(plane1, plane2, plane3);
        List<Point> result = geometries1.findIntersections(
                new Ray(new Point(0, 0, 1.5), new Vector(0, 0, 1))
        );
        assertNotNull(result, ERROR_INTERSECTION);
        assertEquals(2, result.size(), ERROR_INTERSECTION);

        // =============== Boundary Values Tests ==================

        // BV01: No geometries are intersected
        Geometries geometries2 = new Geometries(plane1, plane2, plane3);
        assertNull(
                geometries2.findIntersections(new Ray(new Point(0, 0, 4), new Vector(1, 0, 0))),
                ERROR_INTERSECTION
        );

        // BV02: Only one geometry is intersected
        Geometries geometries3 = new Geometries(plane1, plane2, plane3);
        result = geometries3.findIntersections(
                new Ray(new Point(0, 0, 2.5), new Vector(0, 0, 1))
        );
        assertNotNull(result, ERROR_INTERSECTION);
        assertEquals(1, result.size(), ERROR_INTERSECTION);

        // BV03: All geometries are intersected
        Geometries geometries4 = new Geometries(plane1, plane2, plane3);
        result = geometries4.findIntersections(
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))
        );
        assertNotNull(result, ERROR_INTERSECTION);
        assertEquals(3, result.size(), ERROR_INTERSECTION);
    }
}