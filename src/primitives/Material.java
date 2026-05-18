package primitives;

/**
 * Material coefficients describing how a geometry reacts to light.
 */
public class Material {
    /** Ambient light attenuation coefficient */
    public Double3 kA = Double3.ONE;

    /**
     * Sets the ambient attenuation coefficient.
     *
     * @param kA ambient attenuation coefficient
     * @return this material for chaining
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient attenuation coefficient equally for all color channels.
     *
     * @param kA ambient attenuation coefficient
     * @return this material for chaining
     */
    public Material setKA(double kA) {
        return setKA(new Double3(kA));
    }
}
