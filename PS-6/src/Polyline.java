import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
	private List<Point> list = new ArrayList<Point>();
	private Color color;

	public Polyline(ArrayList<Point> list, Color color){
		this.list = list;
		this.color = color;
	}

	@Override
	public void moveBy(int dx, int dy) {
		if(list.size() > 0) {
			for (Point i : list){
				i.setLocation(i.getX()+dx, i.getY()+dy);
			}
		}
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public boolean contains(int x, int y) {
		if(list.size() > 1) {
			for (int i = 0; i< list.size()-1; i++){
				Point cur = list.get(i);
				Point next = list.get(i+1);
				if(Segment.pointToSegmentDistance(x,y, (int)cur.getX(), (int)cur.getY(), (int)next.getX(), (int)next.getY()) < 2){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		if(list.size() > 1) {
			for (int i = 0; i< list.size()-2; i++){
				Point cur = list.get(i);
				Point next = list.get(i+1);
				g.drawLine((int)cur.getX(), (int)cur.getY(), (int)next.getX(), (int)next.getY());
			}
		}
	}

	@Override
	public String toString() {
		return "Poly-lines: " + list + " " + color;
	}
}
