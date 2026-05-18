package scene;

import geometries.impl.Geometries;
import lighting.AmbientLight;
import primitives.Color;
import java.util.List;

/**
 * Represents a 3D scene containing geometries, background color, and ambient lighting.
 * Implemented as a Passive Data Structure (PDS) for ease of access.
 */
public class Scene {
    /** The name of the scene */
    public final String name;

    /** The background color of the scene */
    public Color background = Color.BLACK;

    /** The ambient lighting of the scene */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** List of all external light sources in the scene */
    public List<lighting.LightSource> lights = new java.util.LinkedList<>();

    /** The collection of 3D geometric shapes in the scene */
    public Geometries geometries = new Geometries();

    /**
     * Constructs a new scene with the specified name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color
     * @return the scene itself for method chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light
     * @return the scene itself for method chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     *
     * @param geometries the collection of geometries
     * @return the scene itself for method chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}