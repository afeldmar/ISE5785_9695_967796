package parser;

import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.Color;
import primitives.Point;
import scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Parses an XML file to construct a Scene object.
 */
public class SceneXmlParser {

    /**
     * Parses the given XML file and initializes a Scene.
     *
     * @param sceneName the name to give to the constructed scene
     * @param filePath  the relative path to the XML file
     * @return a fully constructed Scene object
     */
    public static Scene parse(String sceneName, String filePath) {
        Scene scene = new Scene(sceneName);

        try {
            // Setup XML Document Builder
            File xmlFile = new File(System.getProperty("user.dir") + "/" + filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // 1. Parse background color
            Element root = doc.getDocumentElement();
            String bgColorStr = root.getAttribute("background-color");
            if (!bgColorStr.isEmpty()) {
                scene.setBackground(parseColor(bgColorStr));
            }

            // 2. Parse ambient light
            NodeList ambientNodes = doc.getElementsByTagName("ambient-light");
            if (ambientNodes.getLength() > 0) {
                Element ambientEl = (Element) ambientNodes.item(0);
                String colorStr = ambientEl.getAttribute("color");
                if (!colorStr.isEmpty()) {
                    scene.setAmbientLight(new AmbientLight(parseColor(colorStr)));
                }
            }

            // 3. Parse geometries
            NodeList geometriesNodes = doc.getElementsByTagName("geometries");
            if (geometriesNodes.getLength() > 0) {
                Element geometriesEl = (Element) geometriesNodes.item(0);
                NodeList shapes = geometriesEl.getChildNodes();

                for (int i = 0; i < shapes.getLength(); i++) {
                    Node node = shapes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element shapeEl = (Element) node;

                        // Parse Sphere
                        if (shapeEl.getNodeName().equals("sphere")) {
                            Point center = parsePoint(shapeEl.getAttribute("center"));
                            double radius = Double.parseDouble(shapeEl.getAttribute("radius"));
                            scene.geometries.add(new Sphere(center, radius));
                        }
                        // Parse Triangle
                        else if (shapeEl.getNodeName().equals("triangle")) {
                            Point p0 = parsePoint(shapeEl.getAttribute("p0"));
                            Point p1 = parsePoint(shapeEl.getAttribute("p1"));
                            Point p2 = parsePoint(shapeEl.getAttribute("p2"));
                            scene.geometries.add(new Triangle(p0, p1, p2));
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse XML file: " + e.getMessage(), e);
        }

        return scene;
    }

    /**
     * Helper method to convert a string of format "R G B" into a Color.
     */
    private static Color parseColor(String colorStr) {
        String[] parts = colorStr.trim().split("\\s+");
        return new Color(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    /**
     * Helper method to convert a string of format "X Y Z" into a Point.
     */
    private static Point parsePoint(String pointStr) {
        String[] parts = pointStr.trim().split("\\s+");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}