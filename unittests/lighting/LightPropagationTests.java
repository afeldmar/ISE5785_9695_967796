package lighting;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the light propagation models of the different light sources.
 * Tests focus on the getL and getIntensity methods.
 */
class LightPropagationTests {

    private final Point p = new Point(0, 0, 10);
    private final Color intensity = new Color(1000, 1000, 1000);

    // ================= DirectionalLight Tests =================

    /**
     * Test method for {@link DirectionalLight#getIntensity(Point)}.
     */
    @Test
    void testDirectionalLightGetIntensity() {
        LightSource light = new DirectionalLight(intensity, new Vector(1, 1, 1));

        // TC01: Directional light intensity should be constant everywhere
        assertEquals(intensity, light.getIntensity(p),
                "DirectionalLight intensity should not attenuate");
    }

    /**
     * Test method for {@link DirectionalLight#getL(Point)}.
     */
    @Test
    void testDirectionalLightGetL() {
        Vector dir = new Vector(0, 0, -1);
        LightSource light = new DirectionalLight(intensity, dir);

        // TC01: Directional light 'l' vector should be constant
        assertEquals(dir.normalize(), light.getL(p),
                "DirectionalLight direction is incorrect");
    }

    // ================= PointLight Tests =================

    /**
     * Test method for {@link PointLight#getIntensity(Point)}.
     */
    @Test
    void testPointLightGetIntensity() {
        PointLight light = new PointLight(intensity, new Point(0, 0, 0))
                .setKc(1).setKl(0.1).setKq(0.01);

        // TC01: Point light intensity attenuates with distance
        // Distance from (0,0,0) to (0,0,10) is 10.
        // attenuation = 1 + 0.1*10 + 0.01*100 = 1 + 1 + 1 = 3
        // Expected intensity = 1000 / 3 = 333.333...
        Color expected = intensity.reduce(3);
        assertEquals(expected, light.getIntensity(p),
                "PointLight intensity attenuation is incorrect");
    }

    /**
     * Test method for {@link PointLight#getL(Point)}.
     */
    @Test
    void testPointLightGetL() {
        PointLight light = new PointLight(intensity, new Point(0, 0, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Valid direction from light to point
        assertEquals(new Vector(0, 0, 1), light.getL(p),
                "PointLight direction to point is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: Point coincides with light source position
        assertThrows(IllegalArgumentException.class, () -> light.getL(new Point(0, 0, 0)),
                "getL should throw exception when point is exactly at light position");
    }

    // ================= SpotLight Tests =================

    /**
     * Test method for {@link SpotLight#getIntensity(Point)}.
     */
    @Test
    void testSpotLightGetIntensity() {
        SpotLight light = new SpotLight(intensity, new Point(0, 0, 0), new Vector(0, 0, 1));
        light.setKc(1).setKl(0.1).setKq(0.01);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Point is exactly in front of the spotlight (angle 0, projection 1)
        // Intensity should be same as point light
        Color expectedFront = intensity.reduce(3);
        assertEquals(expectedFront, light.getIntensity(p),
                "SpotLight intensity in front is incorrect");

        // TC02: Point is behind the spotlight
        SpotLight lightBehind = new SpotLight(intensity, new Point(0, 0, 0), new Vector(0, 0, -1));
        lightBehind.setKc(1).setKl(0.1).setKq(0.01);
        assertEquals(Color.BLACK, lightBehind.getIntensity(p),
                "SpotLight intensity behind should be BLACK");

        // =============== Boundary Values Tests ==================
        // TC11: Point is at exactly 90 degrees to the spotlight
        SpotLight light90 = new SpotLight(intensity, new Point(0, 0, 0), new Vector(1, 0, 0));
        light90.setKc(1).setKl(0.1).setKq(0.01);
        assertEquals(Color.BLACK, light90.getIntensity(p),
                "SpotLight intensity at 90 degrees should be BLACK");
    }
}