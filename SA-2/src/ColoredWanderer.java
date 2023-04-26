/**
 * A colored blob that moves randomly.
 */
import java.awt.Color;
import java.awt.*;

public class ColoredWanderer extends Blob {
	protected Color c;

	public ColoredWanderer(double x, double y) {
		super(x, y);
	}
		
	public ColoredWanderer(double x, double y, double r) {
		super(x, y, r);
	}

	public ColoredWanderer(double x, double y, double r, int R, int G, int B){
		super(x,y,r);
		c  = new Color(R,G,B);
		System.out.println(c);
	}
		
	@Override
	public void step() {
		// Choose a new step between -1 and +1 in each of x and y
		dx = 2 * (Math.random()-0.5);
		dy = 2 * (Math.random()-0.5);
		x += dx;
		y += dy;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(c);
		// System.out.println(c);
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}
}