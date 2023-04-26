import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Painting random colors with wanderers
 * Template for SA-2, Dartmouth CS 10, Spring 2016
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 */
public class Pollock extends DrawingGUI {
	private static final int width = 800, height = 600; // setup: window size
	private static final int numBlobs = 20000;			// setup: how many blobs
	private static final int numToMove = 5000;			// setup: how many blobs to animate each frame

	private BufferedImage result;						// the picture being painted
	private ArrayList<ColoredWanderer> blobs;						// the blobs representing the picture

	public Pollock() {
		super("Pollock", width, height);
		result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// Create a bunch of random blobs.
		blobs = new ArrayList<ColoredWanderer>();
		for (int i=0; i<numBlobs; i++) {
			int x = (int)(width*Math.random());
			int y = (int)(height*Math.random());
			// Create a blob with a random color
			
			// I used RGB instead of int value for colors
			int R = (int)(Math.random()*256);
			int G = (int)(Math.random()*256);
			int B = (int)(Math.random()*256);
			blobs.add(new ColoredWanderer(x, y, 1, R, G, B));
		}

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * DrawingGUI method, here just drawing all the blobs
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(result, 0, 0, null);
		for (ColoredWanderer blob : blobs) {
			blob.draw(g);
		}		
	}

	/**
	 * DrawingGUI method, here moving some of the blobs
	 */
	@Override
	public void handleTimer() {
		for (int b = 0; b < numToMove; b++) {
			// Pick a random blob, leave a trail where it is, and ask it to move.
			ColoredWanderer blob = blobs.get((int)(Math.random()*blobs.size()));
			int x = (int)blob.getX(), y = (int)blob.getY();
			// Careful to stay within the image
			if (x>=0 && x<width && y>=0 && y<height) {
				// Leave a trail of the blob's color
				Color color = blob.c;
				result.setRGB(x, y, color.getRGB());				
			}
			blob.step();
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Pollock();
			}
		});
	}
}
