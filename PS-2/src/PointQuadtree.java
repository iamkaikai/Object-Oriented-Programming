import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position,
 * with children at the subdivided quadrants.
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015.
 * @author CBK, Spring 2016, explicit rectangle.
 * @author CBK, Fall 2016, generic with Point2D interface.
 *
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters

	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * A helper function
	 * Return which quadrant the input point is in
	 */
	public int getQuadrant(double x, double y){
		double orin_x = point.getX();
		double orin_y = point.getY();
		if((x < x2 && x > orin_x) && (y > y1 && y < orin_y)) return 1;
		if((x > x1 && x < orin_x) && (y > y1 && y < orin_y)) return 2;
		if((x > x1 && x < orin_x) && (y < y2 && y > orin_y)) return 3;
		if((x < x2 && x > orin_x) && (y < y2 && y > orin_y)) return 4;
		return -1; 	//return -1 for error case
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		double p2_x = p2.getX();
		double p2_y = p2.getY();
		int orin_x = (int)point.getX();
		int orin_y = (int)point.getY();
		int quad = getQuadrant(p2_x, p2_y);

		if(quad == -1) return; // Exit if the dot is not is one of the four quadrants

		if(hasChild(quad)){
			getChild(quad).insert(p2);
		}else {
			if(quad == 1){
				c1 = new PointQuadtree<E>( p2, orin_x, y1, x2, (orin_y));
			}else if(quad == 2) {
				c2 = new PointQuadtree<E>( p2, x1, y1, orin_x, orin_y);
			}else if(quad == 3) {
				c3 = new PointQuadtree<E>( p2, x1, orin_y, orin_x, y2);
			}else if(quad == 4) {
				c4 = new PointQuadtree<E>( p2, orin_x, orin_y, x2, y2);
			}
		}
	}

	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE

		int num = 0;

		if (hasChild(1)){
			num += c1.size();
		}
		if (hasChild(2)){
			num += c2.size();
		}
		if (hasChild(3)){
			num += c3.size();
		}
		if (hasChild(4)){
			num += c4.size();
		}
		return num + 1;
	}

	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		ArrayList<E> list = new ArrayList<E>();
		addPoints(list);
		return list;
	}

	private void addPoints(ArrayList<E> array){
		array.add(point);
		if (hasChild(1)){
			c1.addPoints(array);
		}
		if (hasChild(2)){
			c2.addPoints(array);
		}
		if (hasChild(3)){
			c3.addPoints(array);
		}
		if (hasChild(4)){
			c4.addPoints(array);
		}
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		ArrayList<E> arrayOfPoints = new ArrayList<E>();
		circleHelp(arrayOfPoints, cx, cy,cr);
		return arrayOfPoints;
	}

	// TODO: YOUR CODE HERE for any helper methods.
	private void circleHelp(ArrayList <E> list, double cx, double cy, double cr) {
		Geometry key = new Geometry();
		if (key.circleIntersectsRectangle(cx,cy,cr, x1, y1, x2, y2)) {
			int x = (int) point.getX();
			int y = (int) point.getY();

			if (key.pointInCircle(x, y, cx, cy,  cr)) {
				list.add(point);
			}

			if (!(hasChild(1)) && !hasChild(2) && !hasChild(3) && !hasChild(4)) {
				return;
			}

			if (hasChild(1)) {
				c1.circleHelp(list, cx, cy, cr);
			}

			if (hasChild(2)) {
				c2.circleHelp(list, cx, cy, cr);
			}

			if (hasChild(3)) {
				c3.circleHelp(list, cx, cy, cr);

			}
			if (hasChild(4)) {
				c4.circleHelp(list, cx, cy, cr);
			}
		}
	}
}
