package renderer;

import geometries.impl.Plane;
import geometries.impl.Polygon;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Mini-project 2 performance measurements for multi-threading and adaptive super-sampling.
 */
@SuppressWarnings("java:S109")
class MiniProject2PerformanceTests {
    /** Image width in pixels. */
    private static final int RES_X = 640;
    /** Image height in pixels. */
    private static final int RES_Y = 360;
    /** Adaptive super-sampling depth used for the acceleration runs. */
    private static final int ASS_LEVEL = 2;
    /** Full MP1 blur grid size used when ASS is disabled. */
    private static final int FULL_BLUR_GRID = 7;
    /** Global effect recursion level for practical measurements. */
    private static final int GLOBAL_EFFECTS_LEVEL = 3;

    /** Default constructor for JavaDoc. */
    MiniProject2PerformanceTests() { /* for JavaDoc */ }

    /** Baseline: no multi-threading and no adaptive super-sampling. */
    @Test
    void testMp2AWithoutMtWithoutAss() {
        render("mp2A_noMt_noAss", 0, 0);
    }

    /** Multi-threading only. */
    @Test
    void testMp2BWithMtWithoutAss() {
        render("mp2B_withMt_noAss", -2, 0);
    }

    /** Adaptive super-sampling only. */
    @Test
    void testMp2CWithoutMtWithAss() {
        render("mp2C_noMt_withAss", 0, ASS_LEVEL);
    }

    /** Multi-threading and adaptive super-sampling together. */
    @Test
    void testMp2DWithMtWithAss() {
        render("mp2D_withMt_withAss", -2, ASS_LEVEL);
    }

    /**
     * Renders the MP2 measurement scene.
     *
     * @param fileName output image name
     * @param threads  camera multithreading mode
     * @param assLevel adaptive super-sampling level
     */
    private void render(String fileName, int threads, int assLevel) {
        long start = System.nanoTime();

        Camera.getBuilder()
                .setLocation(new Point(0, 70, 520))
                .setDirection(new Point(0, 5, -80), Vector.AXIS_Y)
                .setVpDistance(520)
                .setVpSize(360, 202.5)
                .setResolution(RES_X, RES_Y)
                .setRayTracer(buildScene(), RayTracerType.SIMPLE)
                .setMultithreading(threads)
                .setAdaptiveSuperSampling(assLevel)
                .setMaxGlobalEffectsLevel(GLOBAL_EFFECTS_LEVEL)
                .setDebugPrint(1)
                .build()
                .renderImage()
                .writeToImage(fileName);

        double seconds = (System.nanoTime() - start) / 1_000_000_000.0;
        System.out.printf("%n%s | threads=%d | ASS=%d | %.3f seconds%n", fileName, threads, assLevel, seconds);
    }

    /**
     * Builds one rich scene reused by all measurement runs.
     *
     * @return scene with MP1 glossy/diffuse effects enabled
     */
    private Scene buildScene() {
        Scene scene = new Scene("Mini project 2 performance scene")
                .setBackground(new Color(18, 22, 30))
                .setAmbientLight(new AmbientLight(new Color(16, 15, 14)));

        Material glossyFloor = new Material()
                .setKD(0.12).setKS(0.45).setShininess(140)
                .setKR(0.24)
                .setKRBlur(2.4)
                .setBlurTargetDistance(210)
                .setBlurGridSize(FULL_BLUR_GRID);

        Material glass = new Material()
                .setKD(0.05).setKS(0.65).setShininess(180)
                .setKT(0.38).setKR(0.0)
                .setKTBlur(1.0)
                .setBlurTargetDistance(170)
                .setBlurGridSize(FULL_BLUR_GRID);

        Material mirror = new Material()
                .setKD(0.08).setKS(0.55).setShininess(160)
                .setKR(0.28)
                .setKRBlur(1.6)
                .setBlurTargetDistance(190)
                .setBlurGridSize(FULL_BLUR_GRID);

        scene.geometries.add(
                new Plane(new Point(0, -75, 0), Vector.AXIS_Y)
                        .setEmission(new Color(28, 32, 34))
                        .setMaterial(glossyFloor),
                new Polygon(
                        new Point(-260, -75, -310),
                        new Point(260, -75, -310),
                        new Point(260, 185, -310),
                        new Point(-260, 185, -310))
                        .setEmission(new Color(38, 34, 44))
                        .setMaterial(new Material().setKD(0.55).setKS(0.12).setShininess(40)),
                new Triangle(new Point(-220, -75, -210), new Point(-90, 95, -250), new Point(-20, -75, -230))
                        .setEmission(new Color(82, 42, 58))
                        .setMaterial(mirror),
                new Triangle(new Point(220, -75, -220), new Point(90, 105, -255), new Point(20, -75, -230))
                        .setEmission(new Color(48, 72, 74))
                        .setMaterial(mirror),
                new Sphere(new Point(-42, -28, -40), 46)
                        .setEmission(new Color(28, 50, 60))
                        .setMaterial(glass),
                new Sphere(new Point(54, -38, -78), 36)
                        .setEmission(new Color(62, 44, 72))
                        .setMaterial(glass)
        );

        addSphereField(scene, glass, mirror);
        addLights(scene);
        return scene;
    }

    /**
     * Adds several dozen visible objects with reflective and transparent materials.
     */
    private void addSphereField(Scene scene, Material glass, Material mirror) {
        Color[] colors = {
                new Color(150, 70, 54),
                new Color(58, 112, 122),
                new Color(154, 122, 48),
                new Color(92, 78, 144)
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                double x = -120 + col * 60;
                double z = -45 - row * 48;
                double radius = 13 + (row + col) % 4;
                Material material = (row + col) % 3 == 0 ? glass : mirror;
                scene.geometries.add(new Sphere(new Point(x, -75 + radius, z), radius)
                        .setEmission(colors[(row + col) % colors.length])
                        .setMaterial(material));
            }
        }
    }

    /**
     * Adds at least five light sources from different positions.
     */
    private void addLights(Scene scene) {
        scene.lights.add(new SpotLight(new Color(820, 480, 220), new Point(-190, 165, 210),
                new Vector(1.1, -0.9, -1.7)).setKl(0.00022).setKq(0.000006));
        scene.lights.add(new SpotLight(new Color(420, 620, 780), new Point(210, 150, 160),
                new Vector(-1.2, -0.7, -1.4)).setKl(0.00025).setKq(0.000007));
        scene.lights.add(new PointLight(new Color(360, 220, 620), new Point(-120, 55, 55))
                .setKl(0.00035).setKq(0.000009));
        scene.lights.add(new PointLight(new Color(420, 360, 210), new Point(130, 35, -20))
                .setKl(0.00028).setKq(0.000008));
        scene.lights.add(new DirectionalLight(new Color(90, 80, 70), new Vector(-0.4, -0.25, -1)));
    }
}
