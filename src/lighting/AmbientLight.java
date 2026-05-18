package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Represents the ambient light in the scene.
 * Ambient light is omnidirectional and has no specific source or position.
 */
public class AmbientLight extends Light {

    /** Constant representing no ambient light (pitch black) */
    public static final AmbientLight NONE = new AmbientLight();

    /**
     * Constructor that calculates the final ambient light intensity.
     * The intensity is scaled by the attenuation factor.
     *
     * @param ia the original light intensity (I_A)
     * @param ka the ambient attenuation factor (k_A)
     */
    public AmbientLight(Color ia, Double3 ka) {
        super(ia.scale(ka));
    }

    /**
     * Default constructor setting the ambient light to pitch black (no light).
     */
    public AmbientLight() {
        super(Color.BLACK);
    }

    /**
     * Constructor for backward compatibility with old tests.
     * Sets the ambient light intensity without an explicit attenuation factor.
     *
     * @param ia the original light intensity (I_A)
     */
    public AmbientLight(Color ia) {
        super(ia);
    }
}