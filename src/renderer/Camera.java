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

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /** 0 = no threads, -1 = parallel stream, -2 = auto raw threads, >0 = exact raw threads */
    private int threadsCount = 0;
    /** Spare threads if trying to use all the cores */
    private static final int SPARE_THREADS = 2;
    /** Printing progress percentage interval (0 = no printing) */
    private double printInterval = 0;
    /** Pixel manager object */
    private renderer.PixelManager pixelManager;

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
     * Render image without multi-threading.
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     * Render image using multi-threading by parallel streaming.
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        java.util.stream.IntStream.range(0, nY).parallel()
                .forEach(i -> java.util.stream.IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads.
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        java.util.LinkedList<Thread> threads = new java.util.LinkedList<>();
        int count = threadsCount;
        while (count-- > 0) {
            threads.add(new Thread(() -> {
                renderer.PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null) {
                    castRay(pixel.col(), pixel.row());
                }
            }));
        }
        for (Thread thread : threads) thread.start();
        try {
            for (Thread thread : threads) thread.join();
        } catch (InterruptedException ignored) {}

        return this;
    }

    /**
     * Main rendering function acting as a router.
     * Initializes the PixelManager and chooses the rendering method.
     * @return the camera object itself
     */
    public Camera renderImage() {
        // Initialize the pixel manager
        pixelManager = new renderer.PixelManager(nY, nX, printInterval);

        // Route to the correct rendering method based on threadsCount
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
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

        if (pixelManager != null) {
            pixelManager.pixelDone();
        }
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

        /**
         * Set multi-threading mode.
         * @param threads -2 for auto raw threads, -1 for parallel stream, 0 for no threads, >0 for exact threads
         * @return the builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Multithreading must be -2 or higher");

            if (threads >= -1) {
                camera.threadsCount = threads;
            } else { // threads == -2
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Set debug printing interval.
         * @param interval printing interval in seconds (0 = no printing)
         * @return the builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0)
                throw new IllegalArgumentException("Interval value must be non-negative");
            camera.printInterval = interval;
            return this;
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