import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Spring 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Returns an image that's the same size & type as the current, but blank
	 */
	private BufferedImage createBlankResult() {
		// Create a new image into which the resulting pixels will be stored.
		return new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Draw a square when triggered. The render color is the average of color within the area.
	 * 
	 * @param cx	  	center x of square
	 * @param cy	  	center y of square
	 * @param r   		radius of square
	 */
	public void avgSquare(int cx, int cy, int r) {
		// Nested loop over nearby pixels
		int R = 0;
		int G = 0;
		int B = 0;
		int pixel_count = 0;
		BufferedImage result = createBlankResult();

		for (int y = Math.max(0, cy-r); y < Math.min(image.getHeight(), cy+r+1); y++) {
			for (int x = Math.max(0, cx-r); x < Math.min(image.getWidth(), cx+r+1); x++) {
				Color c = new Color(image.getRGB(x, y));
				R += c.getRed();
				G += c.getGreen();
				B += c.getBlue();
				pixel_count++;
				Color newColor = new Color(R/pixel_count, G/pixel_count, B/pixel_count);
				image.setRGB(x, y, newColor.getRGB());
			}
		}
	}	
}
