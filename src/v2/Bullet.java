package v2;

import java.awt.Color;

import v2.GraphicObject;
import processing.core.*;

/**	The Ellipse class rewritten as a subclass of GraphicObject
 * 
 * @author jyh
 *
 */
public class Bullet extends GraphicObject 
{
	
	PImage image_;
	
	public int health = 3;
	public int hit = 0;

	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param color		The color is stored as a single int in hex format
	 * @param vx		Horizontal component of the object's velocity vector (in world units per second)
	 * @param vy		Vertical component of the object's velocity vector (in world units per second)
	 * @param spin		Spin of the object (in rad/s)
	 */
	public Bullet(float x, float y, float angle, float width, float height, int color, float vx, 
					float vy, float spin)
	{
		super(x, y, angle, width, height, color, vx, vy, spin);
		setupDefaultBoundingBoxes_();
	}
	
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public Bullet()
	{
		super();
	}
	
	public Bullet(PImage img, float ellipseWidth)
	{
		super();
		image_  = img;
		//width_ = ellipseWidth;
		
	}

	/**	Rendering code specific to ellipses
	 * 
	 * @param app	The Processing application in which the action takes place
	 */
	protected void draw_(PApplet app)
	{
		if (image_ == null) app.ellipse(0,  0,  width_, height_);
		
		else
		{
			PGraphics mask = app.createGraphics(image_.width, image_.height);
			mask.beginDraw();
			mask.ellipse(x_*2,y_*2,width_,height_);
			mask.endDraw();
			
			PImage copy = image_.copy();
			copy.mask(mask);
			app.imageMode(app.CENTER);
			app.image(copy,0,0);
		}
		
		
	
	}

	@Override
	protected void updateAbsoluteBoxes_() {
	}


	@Override
	public boolean isInside(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void draw_(PGraphics app) {
		// TODO Auto-generated method stub
		if (image_ == null) app.ellipse(0,  0,  width_, height_);
	}
}