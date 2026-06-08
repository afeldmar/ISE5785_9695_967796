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
    /** Transparency attenuation factor */
    public Double3 kT = Double3.ZERO;
    /** Reflection attenuation factor */
    public Double3 kR = Double3.ZERO;
    /** Reflection blur radius. Zero keeps ideal mirror reflection. */
    public double kRBlur = 0.0;
    /** Transparency blur radius. Zero keeps ideal clear transparency. */
    public double kTBlur = 0.0;
    /** Distance from secondary ray origin to the virtual blur target area. */
    public double blurTargetDistance = 100.0;
    /** Number of sample cells per axis for glossy/diffuse-glass beams. */
    public int blurGridSize = 9;
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
     * Sets the transparency attenuation factor.
     *
     * @param kT the transparency attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency attenuation factor uniformly for all RGB components.
     *
     * @param kT the transparency attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKT(double kT) {
        return setKT(new Double3(kT));
    }

    /**
     * Sets the reflection attenuation factor.
     *
     * @param kR the reflection attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection attenuation factor uniformly for all RGB components.
     *
     * @param kR the reflection attenuation factor
     * @return the Material object itself for chaining
     */
    public Material setKR(double kR) {
        return setKR(new Double3(kR));
    }

    /**
     * Sets the reflection blur radius for glossy surfaces.
     *
     * @param kRBlur radius of the virtual target area for reflected rays
     * @return the Material object itself for chaining
     */
    public Material setKRBlur(double kRBlur) {
        if (kRBlur < 0) {
            throw new IllegalArgumentException("Reflection blur radius must be non-negative");
        }
        this.kRBlur = kRBlur;
        return this;
    }

    /**
     * Sets the transparency blur radius for diffuse glass.
     *
     * @param kTBlur radius of the virtual target area for refracted rays
     * @return the Material object itself for chaining
     */
    public Material setKTBlur(double kTBlur) {
        if (kTBlur < 0) {
            throw new IllegalArgumentException("Transparency blur radius must be non-negative");
        }
        this.kTBlur = kTBlur;
        return this;
    }

    /**
     * Sets the distance from the secondary ray origin to the virtual target area.
     *
     * @param blurTargetDistance virtual target area distance
     * @return the Material object itself for chaining
     */
    public Material setBlurTargetDistance(double blurTargetDistance) {
        if (blurTargetDistance <= 0) {
            throw new IllegalArgumentException("Blur target distance must be positive");
        }
        this.blurTargetDistance = blurTargetDistance;
        return this;
    }

    /**
     * Sets the number of sample cells per axis for glossy/diffuse-glass beams.
     *
     * @param blurGridSize number of sample cells per axis
     * @return the Material object itself for chaining
     */
    public Material setBlurGridSize(int blurGridSize) {
        if (blurGridSize < 1) {
            throw new IllegalArgumentException("Blur grid size must be at least 1");
        }
        this.blurGridSize = blurGridSize;
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
