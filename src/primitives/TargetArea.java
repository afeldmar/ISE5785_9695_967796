package primitives;

import java.util.LinkedList;
import java.util.List;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * TargetArea is responsible for generating 2D sample points (offsets)
 * in a normalized 2D space (e.g., a square from -1 to 1).
 * This infrastructure is reusable for all super-sampling features.
 */
public class TargetArea {

    /**
     * A simple 2D point class to hold the generated offsets.
     */
    public static class Point2D {
        public final double x;
        public final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /** List of the generated 2D offset points */
    private final List<Point2D> points = new LinkedList<>();

    /**
     * Generates a normalized NxN grid of 2D points.
     * The points are generated in the range of [-1, 1].
     *
     * @param gridSize the number of points in each row/column (N)
     * @return the TargetArea object itself for chaining
     */
    public TargetArea generateGrid(int gridSize) {
        points.clear();

        // If grid size is 1 or less, just return the center (no super-sampling)
        if (gridSize <= 1) {
            points.add(new Point2D(0, 0));
            return this;
        }

        // Calculate the step size between points to fit within [-1, 1]
        double step = 2.0 / (gridSize - 1);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = -1 + j * step;
                double y = -1 + i * step;
                points.add(new Point2D(x, y));
            }
        }
        return this;
    }

    /**
     * Returns the generated list of 2D offset points.
     *
     * @return list of Point2D
     */
    public List<Point2D> getPoints() {
        return points;
    }

    //  Jitter
    private static final java.util.Random random = new java.util.Random();

    /**
     * Generates a Jittered grid of 2D points within a UNIT CIRCLE.
     * Combines the regularity of a grid with stochastic randomness.
     * Points outside the unit circle are filtered out.
     *
     * @param gridSize the number of cells in each row/column
     * @return the TargetArea object itself for chaining
     */
    public TargetArea generateJittered(int gridSize) {
        points.clear();

        if (gridSize <= 1) {
            points.add(new Point2D(0, 0));
            return this;
        }

        // The size of each grid cell
        double step = 2.0 / gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Bottom-left corner of the current cell
                double xBase = -1 + j * step;
                double yBase = -1 + i * step;

                // Add random jitter strictly within the cell
                double xJitter = xBase + random.nextDouble() * step;
                double yJitter = yBase + random.nextDouble() * step;

                // Filter out points that are outside the unit circle (x^2 + y^2 <= 1)
                if (xJitter * xJitter + yJitter * yJitter <= 1) {
                    points.add(new Point2D(xJitter, yJitter));
                }
            }
        }
        return this;
    }

    /**
     * Constructs a beam of rays around a central ideal ray.
     * * @param centralRay     The ideal central ray (reflection or transparency)
     * @param targetDistance The distance from the ray head to the target area (dT)
     * @param blurRadius     The size of the blur (radius of the target area)
     * @return List of rays representing the beam
     */
    public List<Ray> constructBeam(Ray centralRay, double targetDistance, double blurRadius) {
        List<Ray> beam = new LinkedList<>();

        // If there's no blur or no points were generated, just return the central ray
        if (blurRadius == 0 || points.isEmpty()) {
            beam.add(centralRay);
            return beam;
        }

        Point p0 = centralRay.origin();
        Vector dir = centralRay.direction();

        // Center of the target area: Pc = P0 + dir * targetDistance
        Point pc = p0.add(dir.scale(targetDistance));

        // Create orthonormal basis (vx, vy) orthogonal to the central ray direction
        Vector vx;
        try {
            // Pick an arbitrary up vector to cross with dir
            Vector vUp = new Vector(0, 1, 0);

            // If dir is parallel to vUp, pick a different arbitrary vector
            if (dir.equals(vUp) || dir.equals(vUp.scale(-1))) {
                vUp = new Vector(1, 0, 0);
            }
            vx = dir.crossProduct(vUp).normalize();
        } catch (IllegalArgumentException e) {
            vx = new Vector(1, 0, 0); // Fallback
        }

        Vector vy = dir.crossProduct(vx).normalize();

        // Map each 2D point to 3D and create a ray
        for (Point2D p2d : points) {
            Point pt = pc;

            // Move from the center along the X and Y axes of the target area
            if (p2d.x != 0) pt = pt.add(vx.scale(p2d.x * blurRadius));
            if (p2d.y != 0) pt = pt.add(vy.scale(p2d.y * blurRadius));

            // Create the new ray from p0 to the target point
            Vector newDir = pt.subtract(p0).normalize();
            beam.add(new Ray(p0, newDir));
        }

        return beam;
    }
}