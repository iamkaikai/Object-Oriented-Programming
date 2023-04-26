import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Spring 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author YENKAI HUANG and Christopher Kang  for PS1 in cs10 spring
 */

/**
 * @author Christopher Kang,
 *
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50;                // how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                    // the image with identified regions recolored
	private BufferedImage visited;                            // the image to keep track of whether the pixel is visited
	private ArrayList<ArrayList<Point>> regions;            // a region is a list of points

	// so the identified regions are in a list of lists of points
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
		this.recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		this.visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.regions = new ArrayList<ArrayList<Point>>();
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setCamPaint(BufferedImage image) {
		this.image = image;
		this.recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		this.visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.regions = new ArrayList<ArrayList<Point>>();
//		this.regions_paint = new ArrayList<ArrayList<Point>>();
	}

	public int regionSize() {
		return regions.size();
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	//Checks if pixel has been already visited.
	//If not visited set the pixel to 1 and return True
	//Otherwise keep the pixel to 0 and return False
	public boolean check(int x, int y) {
		if (visited.getRGB(x, y) == 0) {
			visited.setRGB(x, y, 1);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {

		LinkedList<Point> toVisit = new LinkedList<Point>();

		//iterate through all pixels of the input image
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {

				//get the color of current pixel
				Color color_Cur_pixel = new Color(image.getRGB(x, y));

				//Check the neighbors if we find a pixel of the target color
				if (colorMatch(color_Cur_pixel, targetColor) && check(x, y)) {

					ArrayList<Point> region = new ArrayList<Point>();
					Point point = new Point(x, y);
					toVisit.add(point);

					//while 'toVisit' is not empty, append the first item into the list xy
					//add neighbors that are not checked and color match into the queue
					//remove the queue
					while (toVisit.size() != 0) {
						Point cur_Pixel = toVisit.peek();
						region.add(cur_Pixel);

						//make sure all edges of the neighbor stay in bound
						int neighbor_x_left = (int) Math.max(0, (cur_Pixel.getX() - 1));
						int neighbor_x_right = (int) Math.min(image.getWidth() - 1, cur_Pixel.getX() + 1);
						int neighbor_y_top = (int) Math.max(0, (cur_Pixel.getY() - 1));
						int neighbor_y_bottom = (int) Math.min(image.getHeight() - 1, cur_Pixel.getY() + 1);

						for (int n_x = neighbor_x_left; n_x <= neighbor_x_right; n_x++) {
							for (int n_y = neighbor_y_top; n_y <= neighbor_y_bottom; n_y++) {
								Color n_Color = new Color(image.getRGB(n_x, n_y));
								if (colorMatch(targetColor, n_Color) && (check(n_x, n_y))) {
									Point nnPoint = new Point(n_x, n_y);
									toVisit.add(nnPoint);
								}
							}
						}
						toVisit.remove();
					}

					// if size of the region is greater than 50, add it to the final list
					if ((region.size()) >= minRegion) {
						regions.add(region);
					}
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		return ((Math.abs((c1.getBlue() - c2.getBlue())) <= maxColorDiff) && (Math.abs((c1.getRed() - c2.getRed())) <= maxColorDiff) && (Math.abs((c1.getGreen() - c2.getGreen())) <= maxColorDiff));
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		if (regions.size() != 0) {
			ArrayList<Point> greatest = regions.get(0);
			for (int i = 0; i < regions.size(); i++) {
				if (regions.get(i).size() > greatest.size()) {
					greatest = regions.get(i);
				}
			}
			return greatest;
		} else {
			ArrayList<Point> emptyArray = new ArrayList<Point>();
			return emptyArray;
		}

	}

	/**
	 * Sets recoloredImage to be a copy of image,
	 * but with each region a uniform random color,
	 * so we can see where they are
	 */
	public void recolorImage() {
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		if (regions.size() == 0){return;}
		for(int i = 0; i < regions.size(); i++){
			int blue = (int) (Math.random() * 256);
			int red = (int) (Math.random() * 256);
			int green = (int) (Math.random() * 256);
			Color color = new Color(red, green, blue);
			//iterate through all the regions and re-color it
			for (int j = 0; j< regions.get(i).size(); j++){
				Point point = regions.get(i).get(j);
				int x = (int)(point.getX());
				int y = (int)(point.getY());
				recoloredImage.setRGB(x,y, color.getRGB());
			}
		}

	}

	//pick the largest region from the list of regions
	//and re-color the pixels from value of the points
	public void recolorImage_largest(Color paintColor) {
		if (regions.size() == 0){return;}
		ArrayList<Point> region_largest = largestRegion();
		//iterate through all the regions and re-color it
		for (int j = 0; j< region_largest.size(); j++) {
			Point point = region_largest.get(j);
			int x = (int) (point.getX());
			int y = (int) (point.getY());
			recoloredImage.setRGB(x, y, paintColor.getRGB());
		}
	}
}
