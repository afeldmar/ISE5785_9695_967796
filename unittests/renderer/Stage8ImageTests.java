package renderer;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

import org.junit.jupiter.api.Test;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Stage 8 custom scene demonstrating shadows, transparency and reflection.
 */
@SuppressWarnings("java:S109")
class Stage8ImageTests {
   /** Default constructor to satisfy JavaDoc generator */
   Stage8ImageTests() { /* to satisfy JavaDoc generator */ }

   /**
    * Produce the mandatory original image for stage 8.
    */
   @Test
   void testStage8CustomImage() {
      Scene scene = new Scene("Stage 8 custom scene")
         .setBackground(new Color(18, 22, 32))
         .setAmbientLight(new AmbientLight(new Color(35, 35, 35), new Double3(0.25)));

      Material matte = new Material().setKD(0.55).setKS(0.25).setShininess(80);

      scene.geometries.add(
         new Triangle(new Point(-220, -120, -260), new Point(230, -120, -260), new Point(120, 140, -310))
            .setEmission(new Color(25, 30, 35))
            .setMaterial(new Material().setKD(0.35).setKS(0.45).setShininess(120).setKR(0.45)),
         new Triangle(new Point(-220, -120, -260), new Point(-150, 130, -300), new Point(120, 140, -310))
            .setEmission(new Color(18, 24, 32))
            .setMaterial(new Material().setKD(0.35).setKS(0.45).setShininess(120).setKR(0.35)),
         new Sphere(new Point(-45, 20, -150), 45D)
            .setEmission(new Color(BLUE).reduce(3))
            .setMaterial(new Material().setKD(0.25).setKS(0.35).setShininess(160).setKT(0.55)),
         new Sphere(new Point(45, -15, -175), 35D)
            .setEmission(new Color(RED).reduce(2))
            .setMaterial(matte),
         new Triangle(new Point(-95, -65, -125), new Point(-25, -85, -150), new Point(-70, 5, -155))
            .setEmission(new Color(30, 120, 80))
            .setMaterial(matte)
      );

      scene.lights.add(
         new SpotLight(new Color(800, 520, 260), new Point(-110, -90, 170), new Vector(1.2, 1, -3))
            .setKl(2E-5).setKq(2E-7));
      scene.lights.add(
         new PointLight(new Color(120, 170, 300), new Point(100, 80, 80))
            .setKl(1E-4).setKq(8E-7));

      Camera.getBuilder()
         .setRayTracer(scene, RayTracerType.SIMPLE)
         .setLocation(new Point(0, 0, 800))
         .setDirection(new Point(0, 0, -180), Vector.AXIS_Y)
         .setVpDistance(800)
         .setVpSize(260, 260)
         .setResolution(600, 600)
         .build()
         .renderImage()
         .writeToImage("stage8CustomImage");
   }
}
