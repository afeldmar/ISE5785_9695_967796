package renderer;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

import org.junit.jupiter.api.Test;

import geometries.impl.Plane;
import geometries.impl.Polygon;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Stage 8 bonus scenes with many bodies and multiple camera views.
 */
@SuppressWarnings("java:S109")
class Stage8BonusImageTests {
   /** Default constructor to satisfy JavaDoc generator */
   Stage8BonusImageTests() { /* to satisfy JavaDoc generator */ }

   /** Common target point for the bonus cameras */
   private static final Point TARGET = new Point(0, -15, -180);

   /**
    * Build a rich scene with many bodies and all renderable geometry types.
    * @return the bonus scene
    */
   private Scene buildBonusScene() {
      Scene scene = new Scene("Stage 8 bonus scene")
         .setBackground(new Color(10, 13, 20))
         .setAmbientLight(new AmbientLight(new Color(35, 35, 40), new Double3(0.28)));

      Material floorMaterial = new Material().setKD(0.48).setKS(0.25).setShininess(70).setKR(0.12);
      Material mirrorMaterial = new Material().setKD(0.22).setKS(0.4).setShininess(180).setKR(0.38);
      Material matteMaterial = new Material().setKD(0.62).setKS(0.28).setShininess(95);
      Material glassMaterial = new Material().setKD(0.22).setKS(0.35).setShininess(180).setKT(0.55);

      scene.geometries.add(
         new Plane(new Point(0, -115, 0), new Vector(0, 1, 0))
            .setEmission(new Color(24, 27, 30))
            .setMaterial(floorMaterial),
         new Polygon(new Point(-185, -110, -360), new Point(185, -110, -360),
                     new Point(165, 115, -380), new Point(-165, 115, -380))
            .setEmission(new Color(18, 22, 34))
            .setMaterial(mirrorMaterial),
         new Triangle(new Point(-210, -110, -260), new Point(-80, -110, -250), new Point(-145, 40, -280))
            .setEmission(new Color(95, 55, 150))
            .setMaterial(new Material().setKD(0.45).setKS(0.35).setShininess(130).setKR(0.18)),
         new Triangle(new Point(70, -112, -245), new Point(210, -112, -270), new Point(150, 58, -290))
            .setEmission(new Color(45, 115, 95))
            .setMaterial(matteMaterial),
         new Sphere(new Point(-78, -54, -160), 44D)
            .setEmission(new Color(BLUE).reduce(3))
            .setMaterial(glassMaterial),
         new Sphere(new Point(-82, -54, -160), 21D)
            .setEmission(new Color(210, 80, 40))
            .setMaterial(matteMaterial),
         new Sphere(new Point(18, -67, -180), 34D)
            .setEmission(new Color(220, 30, 35))
            .setMaterial(new Material().setKD(0.5).setKS(0.45).setShininess(180)),
         new Sphere(new Point(93, -70, -205), 30D)
            .setEmission(new Color(40, 130, 180))
            .setMaterial(new Material().setKD(0.38).setKS(0.5).setShininess(220).setKR(0.35)),
         new Sphere(new Point(138, -82, -138), 20D)
            .setEmission(new Color(235, 185, 55))
            .setMaterial(matteMaterial),
         new Sphere(new Point(-145, -88, -205), 22D)
            .setEmission(new Color(RED).reduce(2))
            .setMaterial(new Material().setKD(0.55).setKS(0.35).setShininess(120)),
         new Polygon(new Point(-36, -112, -108), new Point(36, -112, -108),
                     new Point(30, -76, -128), new Point(-30, -76, -128))
            .setEmission(new Color(22, 36, 55))
            .setMaterial(new Material().setKD(0.18).setKS(0.22).setShininess(100).setKT(0.55)),
         new Triangle(new Point(-18, -112, -92), new Point(52, -112, -95), new Point(15, -50, -128))
            .setEmission(new Color(28, 120, 80))
            .setMaterial(new Material().setKD(0.5).setKS(0.28).setShininess(95).setKT(0.18))
      );

      scene.lights.add(new SpotLight(new Color(520, 340, 180), new Point(-150, -70, 160), new Vector(1.4, 0.6, -3))
         .setKl(2.5E-5).setKq(3.2E-7));
      scene.lights.add(new PointLight(new Color(70, 100, 190), new Point(125, 70, 30))
         .setKl(1.2E-4).setKq(1.0E-6));
      scene.lights.add(new DirectionalLight(new Color(35, 45, 65), new Vector(-0.4, -0.3, -1)));

      return scene;
   }

   /**
    * Render one bonus view.
    * @param scene    scene to render
    * @param location camera location
    * @param fileName output file name
    */
   private void renderView(Scene scene, Point location, String fileName) {
      Camera.getBuilder()
         .setRayTracer(scene, RayTracerType.SIMPLE)
         .setLocation(location)
         .setDirection(TARGET, Vector.AXIS_Y)
         .setVpDistance(850)
         .setVpSize(320, 240)
         .setResolution(800, 600)
         .build()
         .renderImage()
         .writeToImage(fileName);
   }

   /**
    * Produce the main bonus image with at least ten bodies.
    */
   @Test
   void testStage8BonusMainImage() {
      renderView(buildBonusScene(), new Point(0, 30, 720), "stage8BonusMain");
   }

   /**
    * Produce additional views from different camera positions.
    */
   @Test
   void testStage8BonusCameraViews() {
      Scene scene = buildBonusScene();
      renderView(scene, new Point(-260, 55, 650), "stage8BonusViewLeft");
      renderView(scene, new Point(230, 135, 610), "stage8BonusViewHighRight");
   }
}
