package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Unit tests for the ImageWriter class.
 */
public class ImageWriterTests {

    /**
     * Test method for creating a simple image with a grid.
     */
    @Test
    void testImageWriter() {
        // Define constants according to the assignment requirements
        int nX = 800;          // Horizontal resolution (width)
        int nY = 500;          // Vertical resolution (height)
        int step = 50;         // Size of each grid square

        // Choose contrasting colors (red for the grid, yellow for the background)
        Color gridColor = new Color(java.awt.Color.RED);
        Color backgroundColor = new Color(java.awt.Color.YELLOW);

        // Create the ImageWriter object
        ImageWriter imageWriter = new ImageWriter(nX, nY);

        // Iterate over all pixels in the image
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                // Use a ternary operator to decide which color to write:
                // If the column or row index is divisible by 50 without a remainder, it's a grid line. Otherwise, it's the background.
                imageWriter.writePixel(i, j, i % step == 0 || j % step == 0 ? gridColor : backgroundColor);
            }
        }

        // Write the pixels to the physical file in the 'images' directory
        imageWriter.writeToImage("base_render_test");
    }
}