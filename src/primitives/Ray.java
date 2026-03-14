package primitives;

/**
 * Class Ray represents a ray in 3D Cartesian coordinate system.
 */
public final class Ray {

    private final Point origin;
    private final Vector direction;

    /**
     * Constructor for Ray.
     *
     * @param origin starting point
     * @param direction ray direction
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (!(obj instanceof Ray)) {return false;}
        Ray other = (Ray) obj;
        return this.origin.equals(other.origin) &&
                this.direction.equals(other.direction);}

    @Override
    public String toString() {
        return "Ray: origin=" + origin + ", direction=" + direction;
    }

    /**
     * Returns the direction vector of the ray.
     * @return the direction vector
     */
    public Vector direction() {
        return this.direction;
    }
}
