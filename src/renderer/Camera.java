package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * Camera class representing a camera in 3D space.
 */
public class Camera implements Cloneable {

    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;

    private double width;
    private double height;
    private double distance;

    private int nX = 1;
    private int nY = 1;

    private Point vpCenter;
    private double pixelWidth;
    private double pixelHeight;

    // ===== Stage 5 additions =====
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    private Camera() {}

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int xIndex, int yIndex) {
        Point pIJ = vpCenter;

        double xJ = (xIndex - (nX - 1) / 2d) * pixelWidth;
        double yI = -(yIndex - (nY - 1) / 2d) * pixelHeight;

        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }

        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(yI));
        }

        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Renders the image by casting rays through all pixels.
     *
     * @return this camera
     */
    public Camera renderImage() {
        for (int yIndex = 0; yIndex < nY; yIndex++) {
            for (int xIndex = 0; xIndex < nX; xIndex++) {
                castRay(xIndex, yIndex);
            }
        }

        return this;
    }

    /**
     * Casts one ray through one pixel and writes its color.
     *
     * @param xIndex pixel column
     * @param yIndex pixel row
     */
    private void castRay(int xIndex, int yIndex) {
        Ray ray = constructRay(xIndex, yIndex);
        Color color = rayTracer.traceRay(ray);

        imageWriter.writePixel(xIndex, yIndex, color);
    }

    /**
     * Prints a grid over the rendered image.
     *
     * @param interval grid interval
     * @param color grid color
     * @return this camera
     */
    public Camera printGrid(int interval, Color color) {
        for (int yIndex = 0; yIndex < nY; yIndex++) {
            for (int xIndex = 0; xIndex < nX; xIndex++) {
                if (xIndex % interval == 0 || yIndex % interval == 0) {
                    imageWriter.writePixel(xIndex, yIndex, color);
                }
            }
        }

        return this;
    }

    /**
     * Writes the image to the images folder.
     *
     * @param fileName output file name without .png
     * @return this camera
     */
    public Camera writeToImage(String fileName) {
        imageWriter.writeToImage(fileName);
        return this;
    }

    /**
     * Builder class for Camera.
     */
    public static class Builder {
        private final Camera camera;

        private Vector bTo;
        private Vector bUp = Vector.AXIS_Y;
        private Point bTarget;

        public Builder() {
            this.camera = new Camera();
        }

        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        public Builder setDirection(Vector to, Vector up) {
            this.bTo = to;
            this.bUp = up;
            this.bTarget = null;
            return this;
        }

        public Builder setDirection(Point target, Vector up) {
            this.bTarget = target;
            this.bUp = up;
            this.bTo = null;
            return this;
        }

        public Builder setDirection(Point target) {
            this.bTarget = target;
            this.bUp = Vector.AXIS_Y;
            this.bTo = null;
            return this;
        }

        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to render
         * @param type ray tracer type
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
                return this;
            }

            throw new IllegalArgumentException("Unsupported ray tracer type: " + type);
        }

        private void checkResolution() {
            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("Resolution dimensions must be strictly positive");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
        }

        private void checkLocationAndDirection() {
            if (camera.p0 == null)
                throw new MissingResourceException("Missing location", "Camera", "p0");
            if (bUp == null)
                throw new MissingResourceException("Missing up vector", "Camera", "vUp");
            if (bTo == null && bTarget == null)
                throw new MissingResourceException("Missing direction", "Camera", "vTo");

            if (bTarget != null) {
                camera.vTo = bTarget.subtract(camera.p0).normalize();
            } else {
                camera.vTo = bTo.normalize();
            }

            try {
                camera.vRight = camera.vTo.crossProduct(bUp).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Direction vectors 'to' and 'up' cannot be parallel");
            }

            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
        }

        private void checkViewPlane() {
            if (camera.width <= 0 || camera.height <= 0 || camera.distance <= 0)
                throw new IllegalArgumentException("View plane dimensions and distance must be strictly positive");

            camera.vpCenter = camera.p0.add(camera.vTo.scale(camera.distance));
            camera.pixelWidth = camera.width / camera.nX;
            camera.pixelHeight = camera.height / camera.nY;
        }

        public Camera build() {
            checkResolution();
            checkLocationAndDirection();
            checkViewPlane();

            if (camera.rayTracer == null) {
                setRayTracer(new Scene("test"), RayTracerType.SIMPLE);
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}