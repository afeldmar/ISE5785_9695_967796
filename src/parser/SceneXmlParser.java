package parser;

import geometries.api.Geometry;
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
 * Designed with modularity and Clean Code principles.
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
            Document doc = loadDocument(filePath);
            Element root = doc.getDocumentElement();

            parseBackgroundColor(root, scene);
            parseAmbientLight(doc, scene);
            parseGeometries(doc, scene);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse XML file: " + e.getMessage(), e);
        }

        return scene;
    }

    /**
     * Loads and normalizes the XML document from the file system.
     */
    private static Document loadDocument(String filePath) throws Exception {
        File xmlFile = new File(System.getProperty("user.dir") + "/" + filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Parses and sets the background color of the scene.
     */
    private static void parseBackgroundColor(Element root, Scene scene) {
        String bgColorStr = root.getAttribute("background-color");
        if (!bgColorStr.isEmpty()) {
            scene.setBackground(parseColor(bgColorStr));
        }
    }

    /**
     * Parses and sets the ambient light of the scene.
     */
    private static void parseAmbientLight(Document doc, Scene scene) {
        NodeList ambientNodes = doc.getElementsByTagName("ambient-light");
        if (ambientNodes.getLength() > 0) {
            Element ambientEl = (Element) ambientNodes.item(0);
            String colorStr = ambientEl.getAttribute("color");
            if (!colorStr.isEmpty()) {
                scene.setAmbientLight(new AmbientLight(parseColor(colorStr), primitives.Double3.ONE));            }
        }
    }

    /**
     * Parses the geometries section and adds all shapes to the scene.
     */
    private static void parseGeometries(Document doc, Scene scene) {
        NodeList geometriesNodes = doc.getElementsByTagName("geometries");
        if (geometriesNodes.getLength() == 0) return;

        Element geometriesEl = (Element) geometriesNodes.item(0);
        NodeList shapes = geometriesEl.getChildNodes();

        for (int i = 0; i < shapes.getLength(); i++) {
            Node node = shapes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                scene.geometries.add(createGeometry((Element) node));
            }
        }
    }

    /**
     * Factory method to create a specific Geometry object based on the XML tag name.
     */
    private static Geometry createGeometry(Element shapeEl) {
        String shapeName = shapeEl.getNodeName();
        return switch (shapeName) {
            case "sphere" -> parseSphere(shapeEl);
            case "triangle" -> parseTriangle(shapeEl);
            // Easy to extend with more cases (e.g., polygon, cylinder) without changing the main flow
            default -> throw new IllegalArgumentException("Unknown shape in XML: " + shapeName);
        };
    }

    /**
     * Parses a sphere element.
     */
    private static Sphere parseSphere(Element shapeEl) {
        Point center = parsePoint(shapeEl.getAttribute("center"));
        double radius = Double.parseDouble(shapeEl.getAttribute("radius"));
        return new Sphere(center, radius);
    }

    /**
     * Parses a triangle element.
     */
    private static Triangle parseTriangle(Element shapeEl) {
        Point p0 = parsePoint(shapeEl.getAttribute("p0"));
        Point p1 = parsePoint(shapeEl.getAttribute("p1"));
        Point p2 = parsePoint(shapeEl.getAttribute("p2"));
        return new Triangle(p0, p1, p2);
    }

    /**
     * Helper method to convert a space-separated string of RGB values into a Color object.
     */
    private static Color parseColor(String colorStr) {
        String[] parts = colorStr.trim().split("\\s+");
        return new Color(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    /**
     * Helper method to convert a space-separated string of coordinates into a Point object.
     */
    private static Point parsePoint(String pointStr) {
        String[] parts = pointStr.trim().split("\\s+");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}