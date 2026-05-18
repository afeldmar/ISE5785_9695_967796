package geometries.api;

import primitives.Point;
import primitives.Vector;
import primitives.Color;
import primitives.Material;

/**
 * Abstract base class for all geometric shapes.
 */
public abstract class Geometry extends Intersectable {
    /** Emission color of the geometry */
    private Color emission = Color.BLACK;

    /** Material of the geometry */
    private Material material = new Material();

    /**
     * Calculates the normal vector to the geometry at a given point.
     * @param point the point on the geometry surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the geometry itself for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the material of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the geometry itself for method chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
