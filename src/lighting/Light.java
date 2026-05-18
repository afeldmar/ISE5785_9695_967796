package lighting;

import primitives.Color;

/**
 * Abstract class representing a generic light in the scene.
 * Holds the original intensity of the light source.
 */
abstract class Light {

    /** The original intensity of the light */
    protected final Color _intensity;

    /**
     * Protected constructor to initialize the light's original intensity.
     *
     * @param intensity the color intensity of the light
     */
    protected Light(Color intensity) {
        this._intensity = intensity;
    }

    /**
     * Gets the original intensity of the light.
     * Note: This intensity is independent of any point in the scene.
     *
     * @return the original color intensity
     */
    public Color getIntensity() {
        return _intensity;
    }
}