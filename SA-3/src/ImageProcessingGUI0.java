import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Spring 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Yenkai Huang for SA-3
 */

public class ImageProcessingGUI0 extends DrawingGUI {
	private ImageProcessor0 proc;		// handles the image processing
	private int brush = 0;				//0 is brush up; 1 is brush down;
	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public ImageProcessingGUI0(ImageProcessor0 proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
		
	}
	
	// press key 'b' to brush down; press  key 'b' again to brush up.
	@Override
	public void handleKeyPress(char key) {
		if(key == 'b'){
			if(brush == 1){
				System.out.println("brush up!!");
				brush = 0;
			}else{
				System.out.println("brush down!!");
				brush = 1;
			}
			// proc.avgSquare(x, y, key);
		}
		repaint(); // Re-draw everything, since image has changed
	}

	// trigger avgSquare when the brush is set to 1
	@Override
	public void handleMouseMotion(int x, int y) {
		if(brush == 1){
			proc.avgSquare(x, y, 20);
		}
		repaint(); // Re-draw, since image has changed
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage("pictures/baker.jpeg");
				// Create a new processor, and a GUI to handle it
				new ImageProcessingGUI0(new ImageProcessor0(baker));
			}
		});
	}
}
