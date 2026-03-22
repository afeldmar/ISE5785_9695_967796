package geometries.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for class {@link Cylinder}.
 * The tests verify:
 * <ul>
 * <li>{@link Cylinder#getNormal(Point)}</li>
 * </ul>
 */
class CylinderTests {

    /** Basic default constructor to satisfy documentation tools. */
    CylinderTests() { /* Basic default constructor to satisfy documentation tools */ }

    /** Delta value for accuracy when comparing double values. */
    private static final double DELTA = 1e-6;

    /** Error message for wrong exception handling. */
    private static final String ERROR_EXCEPTION = "ERROR: Exception thrown";

    /** Error message for wrong normal. */
    private static final String ERROR_NORMAL = "ERROR: wrong normal";

    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // Cylinder with radius 1, starting at (0,0,0) and going straight up on the Z axis to height 2
        Cylinder cylinder = new Cylinder(1d, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2d);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Point on the side surface of the cylinder
        Point pSide = new Point(1, 0, 1);
        assertDoesNotThrow(() -> cylinder.getNormal(pSide), ERROR_EXCEPTION);
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(pSide), ERROR_NORMAL);

        // TC02: Point on the bottom base of the cylinder
        Point pBottomBase = new Point(0.5, 0, 0);
        assertDoesNotThrow(() -> cylinder.getNormal(pBottomBase), ERROR_EXCEPTION);
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(pBottomBase), ERROR_NORMAL);

        // TC03: Point on the top base of the cylinder
        Point pTopBase = new Point(0.5, 0, 2);
        assertDoesNotThrow(() -> cylinder.getNormal(pTopBase), ERROR_EXCEPTION);
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(pTopBase), ERROR_NORMAL);


        // =============== Boundary Values Tests ==================

        // TC11: Point exactly at the center of the bottom base (head of the ray)
        Point pCenterBottom = new Point(0, 0, 0);
        assertDoesNotThrow(() -> cylinder.getNormal(pCenterBottom), ERROR_EXCEPTION);
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(pCenterBottom), ERROR_NORMAL);

        // TC12: Point exactly at the center of the top base
        Point pCenterTop = new Point(0, 0, 2);
        assertDoesNotThrow(() -> cylinder.getNormal(pCenterTop), ERROR_EXCEPTION);
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(pCenterTop), ERROR_NORMAL);

        // TC13: Point on the edge (seam) of the bottom base and the side surface
        Point pEdgeBottom = new Point(1, 0, 0);
        assertDoesNotThrow(() -> cylinder.getNormal(pEdgeBottom), ERROR_EXCEPTION);
        // Note: Mathematically the normal here is undefined. Usually in this project
        // it's accepted to return either the base's normal or the side's normal.
        // The most common implementation returns the base normal.
        assertEquals(new Vector(0, 0, -1), cylinder.getNormal(pEdgeBottom), ERROR_NORMAL);

        // TC14: Point on the edge (seam) of the top base and the side surface
        Point pEdgeTop = new Point(1, 0, 2);
        assertDoesNotThrow(() -> cylinder.getNormal(pEdgeTop), ERROR_EXCEPTION);
        // Similarly, usually returns the top base normal.
        assertEquals(new Vector(0, 0, 1), cylinder.getNormal(pEdgeTop), ERROR_NORMAL);
    }
}