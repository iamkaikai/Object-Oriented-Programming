/**
Make a blob that wanders a little more resolutely than the one from class, 
walking in a straight line for a fixed number of steps before switching. 
E.g., every 6 steps it picks a random new dx and dy, instead of doing that 
every step like Wanderer.

Exercises
You can mostly follow the structure of Wanderer, though when you're reviewing later, 
you might try doing this from a clean slate.

1.  Define a new subclass of Blob; you can choose the name.
2.  Give it instance variables indicating the number of steps between velocity changes, 
	and how many of those it has currently gone since the last change.
3.  Define a constructor that takes initial x and y values and invokes the super constructor with them. 
	The constructor should also use Math.random() to set the number of steps between velocity changes 
	to be a random number between 4 and 9 (inclusive).
4. 	Override the step method so that random new values are assigned to dx and dy 
	between -1 and +1 each time the required number of steps has been taken.
5. 	Test your solution by repeated calling step on an object instantiated from your class 
	and ensure it changes directions as expected.
6. 	(Optional) Modify the BlobGUI to allow creation of an instance of your class.
*/

public class DrunkBlob extends Blob{
	private int step_to_change;
	private int cur_step = 0;

	public DrunkBlob(double x, double y){
		super(x,y);
		step_to_change = (int) Math.round(Math.random()*5 + 4);		//ask chatGPT how to cast double to int
		dx = 2 * (Math.random() - 0.5);
		dy = 2 * (Math.random() - 0.5);
	}
	
	@Override
	public void step(){
		System.out.println("step: " + cur_step);
		System.out.println("dx: " + dx);
		System.out.println("dy: " + dy);
		if (cur_step % step_to_change == 0){
			System.out.println("direction changed!!");
			dx = 2 * (Math.random() - 0.5);
			dy = 2 * (Math.random() - 0.5);
			x += dx;
			y += dy;
			cur_step = 0;
		}else{
			x += dx;
			y += dy;
		}
		cur_step +=1;
	}
	public static void main(String[] args) {
		DrunkBlob blob = new DrunkBlob(400,300);
		while(true){
			blob.step();
		}
	}
}