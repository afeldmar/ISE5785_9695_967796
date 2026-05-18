package lighting;

import primitives.Color;

/**
 * Represents ambient light in a scene.
 * This light illuminates all objects equally, regardless of their position or orientation.
 */
public class AmbientLight {

    /** Constant representing no ambient light (black color) */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /** The intensity of the ambient light */
    private final Color intensity;

    /**
     * Constructs an ambient light with the given intensity color.
     *
     * @param intensity the color intensity of the light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets the intensity of the ambient light.
     *
     * @return the intensity color
     */
    public Color getIntensity() {
        return intensity;
    }
}
