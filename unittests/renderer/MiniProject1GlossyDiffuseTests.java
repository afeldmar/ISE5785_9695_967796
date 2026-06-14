package renderer;

import geometries.impl.Plane;
import geometries.impl.Polygon;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Demonstration images for mini-project 1: glossy surfaces and diffuse glass.
 */
@SuppressWarnings("java:S109")
class MiniProject1GlossyDiffuseTests {
    /** Pyramid base level */
    private static final double BASE_Y = -82.0;
    /** Pyramid apex y coordinate */
    private static final double APEX_Y = 120.0;
    /** Pyramid apex z coordinate */
    private static final double APEX_Z = -165.0;
    /** Pyramid front base z coordinate */
    private static final double FRONT_Z = -72.0;
    /** Pyramid top point */
    private static final Point APEX = new Point(0, APEX_Y, APEX_Z);
    /** Front-left pyramid base point */
    private static final Point FRONT_LEFT = new Point(-165, BASE_Y, -72);
    /** Front-right pyramid base point */
    private static final Point FRONT_RIGHT = new Point(165, BASE_Y, -72);
    /** Back-right pyramid base point */
    private static final Point BACK_RIGHT = new Point(142, BASE_Y, -265);
    /** Back-left pyramid base point */
    private static final Point BACK_LEFT = new Point(-142, BASE_Y, -265);

    /** Default constructor to satisfy JavaDoc generator */
    MiniProject1GlossyDiffuseTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Builds the same scene with or without glossy/diffuse-glass blur.
     *
     * @param blurred true for the mini-project effect, false for the ideal baseline
     * @return scene to render
     */
    private Scene buildScene(boolean blurred) {
        Scene scene = new Scene(blurred ? "Egyptian pyramid at sunset - blurred" : "Egyptian pyramid at sunset - ideal")
                .setBackground(new Color(132, 72, 44))
                .setAmbientLight(new AmbientLight(new Color(18, 14, 12)));

        Material glossyFloor = new Material()
                .setKD(0.12).setKS(0.55).setShininess(120)
                .setKR(0.32);
        Material glassOrnament = new Material()
                .setKD(0.05).setKS(0.65).setShininess(180)
                .setKT(0.48);

        if (blurred) {
            glossyFloor
                    .setKRBlur(3.4)
                    .setBlurTargetDistance(180.0)
                    .setBlurGridSize(2);
            glassOrnament
                    .setKTBlur(1.0)
                    .setBlurTargetDistance(140.0)
                    .setBlurGridSize(2);
        }

        scene.geometries.add(
                new Plane(new Point(0, BASE_Y, 0), Vector.AXIS_Y)
                        .setEmission(new Color(42, 48, 44))
                        .setMaterial(glossyFloor),
                new Polygon(
                        new Point(-420, BASE_Y, -340),
                        new Point(420, BASE_Y, -340),
                        new Point(420, 170, -340),
                        new Point(-420, 170, -340))
                        .setEmission(new Color(104, 58, 42))
                        .setMaterial(new Material().setKD(0.65).setKS(0.08).setShininess(25))
        );

        addBackgroundPyramid(scene, -190, -246, 138, 166, 82, new Color(152, 96, 46));
        addPyramid(scene);
        addBackgroundPyramid(scene, 196, -242, 132, 158, 80, new Color(132, 82, 42));
        addBrickPattern(scene);
        addYellowDecorations(scene);

        scene.geometries.add(
                new Sphere(new Point(0, -48, 48), 28D)
                        .setEmission(new Color(32, 48, 54))
                        .setMaterial(glassOrnament)
        );

        scene.lights.add(new SpotLight(
                new Color(760, 430, 170),
                new Point(-210, 120, 210),
                new Vector(1.4, -0.85, -1.7))
                .setKl(0.00028)
                .setKq(0.000009));
        scene.lights.add(new DirectionalLight(new Color(82, 52, 34), new Vector(-0.5, -0.25, -1)));

        return scene;
    }

    /**
     * Adds the pyramid body.
     *
     * @param scene scene to update
     */
    private void addPyramid(Scene scene) {
        Material frontStone = new Material().setKD(0.58).setKS(0.18).setShininess(45);
        Material sideStone = new Material().setKD(0.52).setKS(0.16).setShininess(38);

        scene.geometries.add(
                new Triangle(FRONT_LEFT, FRONT_RIGHT, APEX)
                        .setEmission(new Color(164, 104, 48))
                        .setMaterial(frontStone),
                new Triangle(FRONT_RIGHT, BACK_RIGHT, APEX)
                        .setEmission(new Color(96, 62, 38))
                        .setMaterial(sideStone),
                new Triangle(BACK_LEFT, FRONT_LEFT, APEX)
                        .setEmission(new Color(118, 72, 38))
                        .setMaterial(sideStone)
        );
    }

    /**
     * Adds a simpler side pyramid to build the background composition.
     *
     * @param scene scene to update
     * @param x     pyramid center x
     * @param z     pyramid center z
     * @param width base width
     * @param height pyramid height
     * @param depth base depth
     * @param color front face color
     */
    private void addBackgroundPyramid(Scene scene, double x, double z, double width, double height, double depth,
                                      Color color) {
        Point apex = new Point(x, BASE_Y + height, z - depth * 0.55);
        Point frontLeft = new Point(x - width * 0.5, BASE_Y, z + depth * 0.35);
        Point frontRight = new Point(x + width * 0.5, BASE_Y, z + depth * 0.35);
        Point backLeft = new Point(x - width * 0.42, BASE_Y, z - depth * 0.65);
        Point backRight = new Point(x + width * 0.42, BASE_Y, z - depth * 0.65);

        scene.geometries.add(
                new Triangle(frontLeft, frontRight, apex)
                        .setEmission(color)
                        .setMaterial(new Material().setKD(0.58).setKS(0.14).setShininess(35)),
                new Triangle(frontRight, backRight, apex)
                        .setEmission(new Color(90, 58, 34))
                        .setMaterial(new Material().setKD(0.52).setKS(0.12).setShininess(30)),
                new Triangle(backLeft, frontLeft, apex)
                        .setEmission(new Color(106, 66, 34))
                        .setMaterial(new Material().setKD(0.52).setKS(0.12).setShininess(30))
        );
    }

    /**
     * Adds brick-like mortar lines on the pyramid front face.
     *
     * @param scene scene to update
     */
    private void addBrickPattern(Scene scene) {
        Material mortar = new Material().setKD(0.55).setKS(0.06).setShininess(20);

        for (int row = 0; row < 8; row++) {
            double t0 = row / 8.0;
            double t1 = t0 + 0.008;
            addFrontFaceStrip(scene, t0, t1, -1.0, 1.0, new Color(86, 54, 30), mortar);
        }
    }

    /**
     * Adds a narrow quadrilateral on the pyramid front face.
     *
     * @param scene    scene to update
     * @param tBottom  bottom interpolation from base to apex
     * @param tTop     top interpolation from base to apex
     * @param xLeftN   normalized left x inside row
     * @param xRightN  normalized right x inside row
     * @param color    strip emission color
     * @param material strip material
     */
    private void addFrontFaceStrip(Scene scene, double tBottom, double tTop, double xLeftN, double xRightN,
                                   Color color, Material material) {
        Point p1 = frontFacePoint(tBottom, xLeftN);
        Point p2 = frontFacePoint(tBottom, xRightN);
        Point p3 = frontFacePoint(tTop, xRightN);
        Point p4 = frontFacePoint(tTop, xLeftN);

        scene.geometries.add(new Polygon(p1, p2, p3, p4)
                .setEmission(color)
                .setMaterial(material));
    }

    /**
     * Computes a point slightly in front of the pyramid front face.
     *
     * @param t  interpolation from base to apex
     * @param xn normalized horizontal position in the current row
     * @return point on the front face
     */
    private Point frontFacePoint(double t, double xn) {
        double halfWidth = 165.0 * (1.0 - t);
        double x = halfWidth * xn;
        double y = BASE_Y + (APEX_Y - BASE_Y) * t;
        double z = FRONT_Z + 0.9 + (APEX_Z - FRONT_Z) * t;
        return new Point(x, y, z);
    }

    /**
     * Adds yellow decorative bulbs as visible geometry.
     *
     * @param scene scene to update
     */
    private void addYellowDecorations(Scene scene) {
        Material bulb = new Material().setKD(0.12).setKS(0.65).setShininess(120);
        Color bulbColor = new Color(230, 172, 58);

        for (int i = 0; i <= 6; i++) {
            double t = i / 6.0;
            addBulb(scene, edgePoint(FRONT_LEFT, APEX, t), bulbColor, bulb);
            addBulb(scene, edgePoint(FRONT_RIGHT, APEX, t), bulbColor, bulb);
        }

        for (int i = 0; i <= 8; i++) {
            double xN = -0.82 + i * 0.205;
            addBulb(scene, frontFacePoint(0.38 + 0.035 * Math.sin(i * 0.9), xN), bulbColor, bulb);
        }
    }

    /**
     * Adds one visible light bulb.
     *
     * @param scene    scene to update
     * @param center   bulb center
     * @param color    bulb color
     * @param material bulb material
     */
    private void addBulb(Scene scene, Point center, Color color, Material material) {
        scene.geometries.add(new Sphere(center, 4.0)
                .setEmission(color)
                .setMaterial(material));
    }

    /**
     * Computes a point on an edge, slightly shifted toward the camera.
     *
     * @param start edge start
     * @param end   edge end
     * @param t     interpolation parameter
     * @return point on the edge
     */
    private Point edgePoint(Point start, Point end, double t) {
        double startX = start == FRONT_LEFT ? -165.0 : 165.0;
        double x = startX * (1.0 - t);
        double y = BASE_Y + (APEX_Y - BASE_Y) * t;
        double z = FRONT_Z + (APEX_Z - FRONT_Z) * t + 2.0;
        return new Point(x, y, z);
    }

    /** Render the baseline image with ideal reflection and transparency. */
    @Test
    void testIdealReflectionAndTransparency() {
        render(buildScene(false), "mp1GlossyDiffuseIdeal");
    }

    /** Render the mini-project image with glossy reflection and diffuse glass. */
    @Test
    void testGlossyReflectionAndDiffuseGlass() {
        render(buildScene(true), "mp1GlossyDiffuseBlurred");
    }

    /**
     * Renders a scene into the images folder.
     *
     * @param scene    scene to render
     * @param fileName output file name without extension
     */
    private void render(Scene scene, String fileName) {
        Camera.getBuilder()
                .setLocation(new Point(0, 8, 430))
                .setDirection(new Point(0, -18, -125), Vector.AXIS_Y)
                .setVpDistance(430)
                .setVpSize(320, 180)
                .setResolution(3840, 2160)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage(fileName);
    }
}
