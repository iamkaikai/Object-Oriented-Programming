import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 */
public class Rectangle implements Shape {
	// TODO: YOUR CODE HERE
	private int x1, y1, x2, y2;		// upper left and lower right
	private Color color;

	public Rectangle(int x1, int y1, Color color){
		this.x1 = x1; this.x2 = x1;
		this.y1 = y1; this.y2 = y1;
		this.color = color;
	}

	@Override
	public void moveBy(int dx, int dy) {
		x1 += dx; y1 += dy;
		x2 += dx; y2 += dy;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
		
	@Override
	public boolean contains(int x, int y) {
		return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}

	public String toString() {
		return "rectangle "+ x1 +" " + y1 + " " + x2 + " " + y2 + " " + color.getRGB();
	}
}
