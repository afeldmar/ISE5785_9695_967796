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

    /** Diffuse attenuation factor */
    public Double3 kD = Double3.ZERO;
    /** Specular attenuation factor */
    public Double3 kS = Double3.ZERO;
    /** Shininess factor (size of the specular highlight) */
    public int nShininess = 0;

    /**
     * Sets the diffuse attenuation factor.
     *
     * @param kD the diffuse attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse attenuation factor uniformly for all RGB components.
     *
     * @param kD the diffuse attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular attenuation factor.
     *
     * @param kS the specular attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular attenuation factor uniformly for all RGB components.
     *
     * @param kS the specular attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the shininess factor of the material.
     *
     * @param nShininess the shininess factor
     * @return the Material object itself for chaining
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
