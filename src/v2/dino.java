package v2;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import processing.sound.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import v2.AnimatedObject;
import v2.AnimationMode;
import v2.GraphicObject;



public class dino extends PApplet implements ApplicationConstants {
	
	
	//-----------------------------
	//	graphical objects for all object with collisions
	//-----------------------------
	ArrayList<GraphicObject> objectList_;
	
	//-----------------------------
	//	graphical objects
	//-----------------------------
	ArrayList<GraphicObject> backgroundList_;
	
	ArrayList<GraphicObject> fireballs;
	
	/**	Previous value of the off-screen buffer (after the last call to draw()
	 */
	private PGraphics lastBuffer_;

	/** Toggles on-off double buffering.
	 */
	private boolean doDoubleBuffer_ = false;
	
	/**	Desired rendering frame rate
	 * 
	 */
	static final float RENDERING_FRAME_RATE = 60;
	
	/**	Ratio of animation frames over rendering frames 
	 * 
	 */
	static final int ANIMATION_RENDERING_FRAME_RATIO = 1;
	
	/**	computed animation frame rate
	 * 
	 */
	static final float ANIMATION_FRAME_RATE = RENDERING_FRAME_RATE * ANIMATION_RENDERING_FRAME_RATIO;
	
	/**	A counter for animation frames
	 * 
	 */
	private int frameCount_;
	
	/**	Used to pause the animation
	 */
	boolean animate_ = true;
	
	/**	Variable keeping track of the last time the update method was invoked.
	 * 	The different between current time and last time is sent to the update
	 * 	method of each object. 
	 */
	private int lastUpdateTime_;
	
	/**	Whether to draw the reference frame of each object (for debugging
	 * purposes.
	 */
	private boolean drawRefFrame_ = false;
	
	/**	 Keep track of score
	 * 
	 */
	private int score = 0;
	
	//-----------------------------
	//	constants
	//-----------------------------
	public final static float ANGLE_INCR = 0.1f;
	public final static int JOINT_COLOR = 0xFFFF0000;
	public final static int LINK_COLOR = 0xFFFFFFFF;
	public final static float JOINT_RADIUS = 0.5f;
	public final static float JOINT_DIAMETER = 2*JOINT_RADIUS;
	public final static float LINK_THICKNESS = 7;
	public final static int TARGET_COLOR = 0xFF00FF00;
	public static final float TARGET_DIAMETER = 0.5f;
	private int state = 0;
	private float movement_v = 0;
	private float Height = 50;
	private float Torso_bottom = 50;
	private float temp = 0;
	private float global_t = 0;
	private boolean new_state = true;
	private float speed = 0;
	private int high_score = 0;
	
	//-----------------------------
	//	limbs (line lengths)
	//-----------------------------
	private float[] L1 = {-10000, 25f, 25f};//Right leg
	private float[] L2 = {-10000, 25f, 25f};//Left leg
	private float[] L3 = {-10000, 20f, 20f};//Right Arm
	private float[] L4 = {-10000, 20f, 20f};//Left Arm
	private float[] L5 = {-10000, 30f, 15f};//torso
	
	//-----------------------------
	//	animation mode
	//-----------------------------
	private AnimationMode animationMode_ = AnimationMode.WINDOW_WORLD;
	private PGraphics offScreenBuffer_;
	
	//-----------------------------
	//	ellipse image
	//-----------------------------
	PImage imageCircle;
	
	//-----------------------------
	//	other images
	//-----------------------------
	PImage imageCloud;
	PImage imageTree;
	PImage imageHeart;
	PImage imageWood;
	PImage angryGuy;
	
	//-----------------------------
	//	Modifies the time interval of the jump, (lower = longer)
	//-----------------------------
	private float modifier = .75f;
	
	//-----------------------------
	//	Health
	//-----------------------------
	private int health = 300;
	
	//----------------------Torso----------------------------------------------
	

	private float [][]keyFrames_torso_default = {
									{-10000, -1.57f, 0, 0}};
	private float [][]keyFrames_torso_ducking = {
									{-10000, -1.57f, 0, 0}, // start 
//									{-10000, -1.57f, -0.3f, (0.1f*modifier)},
//									{-10000, -1.57f, -0.5f, (0.2f*modifier)},
									{-10000, -2.01f, 0, (0.00001f*modifier)}};//bottom of duck
//									{-10000, -1.57f, -0.5f, (0.4f*modifier)},
//									{-10000, -1.57f, -0.3f, (0.5f*modifier)},
//									{-10000, -1.57f, 0, (0.4f*modifier)}}; // start
	
	//--------------------Right-Leg--------------------------------------------
	
	private float [][]keyFrames_rightleg_default = {
									{-10000, -1.1f, -0.3f, 0.0f}};
	
	private float [][]keyFrames_rightleg_walkright = {
									{-10000, -1.1f, -0.3f, 0.0f},	//Start pos
									{-10000, -1.0f, -0.7f, (0.1f*modifier)},	//Knee raise
									{-10000, -1.3f, -0.1f, (0.2f*modifier)}, 	//Bottom Leg foward
									{-10000, -1.7f, -0.1f, (0.3f*modifier)},	//Leg stride Start
									{-10000, -2.2f, -0.3f, (0.4f*modifier)},	//Leg stride ->
									{-10000, -2.5f, -0.5f, (0.5f*modifier)},	//Leg stride End
									{-10000, -2.1f, -0.7f, (0.6f*modifier)},	//Leg stride Returning
									{-10000, -1.1f, -0.3f, (0.8f*modifier)}};	//Return to start

	//--------------------Left-Leg---------------------------------------------
	
	private float [][]keyFrames_leftleg_default = {
									{-10000, -2.1f,  0.3f, 0.0f}};
	
	private float [][]keyFrames_leftleg_walkright = {
									{-10000, -2.1f,  0.3f, 0.0f},	//Start pos
									{-10000, -2.2f, -0.5f, (0.1f*modifier)},	//Leg stride end
									{-10000, -1.9f, -0.7f, (0.2f*modifier)}, 	//
									{-10000, -1.5f, -0.8f, (0.3f*modifier)},	//
									{-10000, -1.0f, -0.4f, (0.4f*modifier)},	//
									{-10000, -0.7f, -0.1f, (0.5f*modifier)},	//Bottom Leg foward
									{-10000, -1.1f, -0.1f, (0.6f*modifier)},	//Leg stride Start
									{-10000, -2.1f,  0.3f, (0.8f*modifier)}};	//Return to start

	
	//--------------------Right-Arm---------------------------------------------
	
	private float [][]keyFrames_rightarm_default = {
									{-10000, -1.1f,  0.0f, 0.0f}};
	
	private float [][]keyFrames_rightarm_jump = {
									{-10000, -1.1f,  0.0f, 0.0f},	//Start pos
									{-10000,  0.2f,  0.9f, (0.2f*modifier)},	//Peak
									{-10000, -1.1f,  0.0f, (0.8f*modifier)}};	//Return to start
	
	//--------------------Left-Arm---------------------------------------------
	
	private float [][]keyFrames_leftarm_default = {
									{-10000, -2.1f,  0.0f, 0.0f}};
	
	private float [][]keyFrames_leftarm_jump = {
									{-10000, -2.1f,  0.0f, 0.0f},	//Start pos
									{-10000, -3.3f, -0.9f, (0.2f*modifier)},	//Peak
									{-10000, -2.1f,  0.0f, (0.8f*modifier)}};	//Return to start
	
	//--------------------Slidiing (Ducking) ----------------------------------
	
	private float [][]keyFrames_leftarm_sliding = {
									{-10000, -5f, 0f, 0f  },
									{-10000, 20f, 0f, -12f}};
	private float [][]keyFrames_rightarm_sliding = {
									{-10000, -2.1f, 0f, 0f  },
									{-10000, 2f, 0f, 0f}};
	private float [][]keyFrames_leftleg_sliding = {
									{-10000, -2.1f, 0f, 0f  },
									{-10000, 3f, -4f, -5f}};
	private float [][]keyFrames_rightleg_sliding = {
									{-10000, -2.1f, 0f, 0f  },
									{-10000, -1f, -3.1f, -4f}};
	
	
	
	private KeyframeInterpolator right_leg_interpolator_;
	private KeyframeInterpolator left_leg_interpolator_;
	private KeyframeInterpolator right_arm_interpolator_;
	private KeyframeInterpolator left_arm_interpolator_;
	private KeyframeInterpolator torso_interpolator_;
	private int startTime_;
	
	private String splashtxt = "RUN, DUCK, AND JUMP!";
	private boolean splashOn = true;
	private String splashtxtClick = "     CLICK HERE TO PLAY";
	private final int titleSize = 64;
	private final int txtSize = 25;
	private boolean diedOnce = false;
	private int headX = 0;
	private int time;
	private int entityTime;
	private int scoreTime = millis();

	//Sound files
	SoundFile die;
	SoundFile jump;
	SoundFile fireball;
		
	public void settings() 
	{
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	public void interpolate() { 
		//-----------------------------
		//	set up Interpolators depending on state
		//-----------------------------
		System.out.println((0.8f*modifier));
		if(state == 3) {//Jump
			right_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightleg_walkright);
			left_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftleg_walkright);
			right_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightarm_jump);
			left_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftarm_jump);
			torso_interpolator_ = new LinearKeyframeInterpolator(keyFrames_torso_default);
		}else if(state == 4){
//			right_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightleg_sliding);
//			left_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftleg_sliding);
//			right_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightarm_sliding);
//			left_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftarm_sliding);
			torso_interpolator_ = new LinearKeyframeInterpolator(keyFrames_torso_ducking);
		}
		else{//Default walk
			right_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightleg_walkright);
			left_leg_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftleg_walkright);
			right_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_rightarm_default);
			left_arm_interpolator_ = new LinearKeyframeInterpolator(keyFrames_leftarm_default);
			torso_interpolator_ = new LinearKeyframeInterpolator(keyFrames_torso_default);
		}
	}
	
	
	public void setup() 
	{	
		health = 3;
		score = 0;
		speed = 0;
		
		interpolate();
		imageCircle = loadImage("data/thorn.png");
		imageCloud = loadImage("data/cloud.png");
		imageTree = loadImage("data/tree.png");
		imageHeart = loadImage("data/heart.png");
		imageWood = loadImage("data/wood.png");
		angryGuy = loadImage("data/lil_angry_guy.png");

		//Sound files
		die = new SoundFile(this, "data/death.wav");
		jump = new SoundFile(this, "data/jump.wav");
		fireball = new SoundFile(this, "data/fireball.wav");
		//-----------------------------
		//	add random amount of ellipses
		//-----------------------------
		frameRate(ANIMATION_FRAME_RATE);
		frameCount_ = 0;
		offScreenBuffer_ = createGraphics(width, height);
		GraphicObject.setAnimationMode(animationMode_);
		
		objectList_ = new ArrayList<GraphicObject>();
		backgroundList_ = new ArrayList<GraphicObject>();
		fireballs = new ArrayList<GraphicObject>();
		offScreenBuffer_ = createGraphics(width, height);
		GraphicObject.setAnimationMode(animationMode_);
		addEllipse(backgroundList_, imageCloud);
		addEllipse(backgroundList_, imageTree);
	}
	
	public void addEllipse(ArrayList<GraphicObject> objectList, PImage image) { 
		if(image == imageCloud) { 
			objectList.add(new AnimatedEllipse(XMAX, random(YMAX-300, YMAX), 3.1415f, random(100,200), random(100,200), LINK_COLOR, random(-50, -500)-speed, 0, 0, image));
		}else if(image == imageTree) { 
			objectList.add(new AnimatedEllipse(XMAX, YMIN+160, 3.1415f, 100, 200, LINK_COLOR, random(-50, -500)-speed, 0, 0, image));
		}else if(image == imageWood) {
			objectList.add(new AnimatedBox(XMAX,YMIN+15,0,100,600,image,-300-speed,0,0));
		}else if(image == imageCircle) {
			objectList.add(new AnimatedEllipse(XMAX, YMIN+15, 0, 60, 60, LINK_COLOR, -300-speed, 0, 0, image));
		}
		else {
			objectList_.add(new AnimatedEllipse(XMAX,YMIN+160,PI,100,100,LINK_COLOR,-300-speed,0,0,image));
		}
	}
	
	public void draw() {
		if(state == 4)new_state = true;
		if(splashOn == true) {
			clear();
			PImage splashBackground = loadImage("data/forest.jpg");
			splashBackground.resize(width, height);
			background(splashBackground);
			stroke(255);
			strokeWeight(5);
			fill(0, 0, 0);
			rect(10, 45, 765, 65);
			rect(210, 460, 350, 60);
			textSize(titleSize);
			fill(148, 0, 211);
			text(splashtxt, 30, 100);
			textSize(txtSize);
			text(splashtxtClick, 220, 500);
			if(mouseX >= 210 && mouseX <= 560 && mouseY >= 460 && mouseY <= 520) {
				fill(255, 255, 255);
				rect(210, 460, 350, 60);
				fill(148, 0, 211);
				text(splashtxtClick, 220, 500);
			}
			if(diedOnce == true) {
				fill(0, 0, 0);
				rect(10, 45, 765, 65);
				rect(210, 460, 350, 60);
				rect(210, 620, 340, 60);
				textSize(titleSize);
				fill(148, 0, 211);
				text(splashtxt, 30, 100);
				textSize(txtSize);
				text(splashtxtClick, 220, 500);
				text("You died :(", 310, 650);
				text("Last high score: " + high_score, 270, 670);
				if(mouseX >= 210 && mouseX <= 560 && mouseY >= 460 && mouseY <= 520) {
					fill(255, 255, 255);
					rect(210, 460, 350, 60);
					fill(148, 0, 211);
					text(splashtxtClick, 220, 500);
				}
			}
		}
    else
		{
    	time = millis();
			PGraphics gc;
			// Checks if the objects are past the left side of the screen and deletes them from the list and adds to the score.
			for (int i = 0; i < objectList_.size(); i++) {
				//Check bounds
				if(objectList_.get(i).x_ <= XMIN) { 
					objectList_.remove(i);
					score += 1;
				}
      }
			
		// Check to see if the fireballs is past XMAX number and if so delete from the bullet list
		for (int i = 0; i < fireballs.size(); i++) {
			//Check bounds
			if(fireballs.get(i).x_ >= XMAX) { 
				fireballs.remove(i);
			}
		}
		
		// Deals with the hit detection of the obstacles. 
		for (int i = 0; i < objectList_.size(); i++) {
			//Check hit on the plank
			if (objectList_.get(i) instanceof AnimatedBox) {
				if (objectList_.get(i).x_ <= XMIN + 200 && objectList_.get(i).x_ > XMIN + 150) {
					health--;
					objectList_.remove(i);
				}
			  // Check hit for thornbush, when in the air aswell.
			} else if(objectList_.get(i).x_ <= XMIN + 220 && objectList_.get(i).x_ > XMIN + 150 && objectList_.get(i).y_ ==YMIN +15) {
				if(YMAX-550 + movement_v < objectList_.get(i).y_ + 75) {
					health--;
					objectList_.remove(i);
				}
			}
			// Check hit for the lakitu by checking if in a duck state.
			else if (objectList_.get(i).x_ <= XMIN + 220 && objectList_.get(i).x_ > XMIN + 150)
			{
				if(state != 4)
				{
					health--;
					objectList_.remove(i);
				}
				else if (state == 4 && time - scoreTime > 200 )
				{
					scoreTime = millis();
					score+=1;
				}
				
			}
		}
			// if health is 0 the game will display the splash screen for death
			if(health == 0) {
				if(score > high_score) { 
					high_score = score;
				}
				splashOn = true;
				diedOnce = true;
				die.play();
				setup();
				
			}
			// If any object in the background hits the left side of the screen it is removed from the objectlist
			for (int i = 0; i < backgroundList_.size(); i++) {
				if(backgroundList_.get(i).x_ <= XMIN) { 
					backgroundList_.remove(i);
				}
			}
			
			// This deals with the generations of the obstacles.
			// Wood planks can spawn at a faster rate than thorns, and thorns at a faster rate than lakitu
			// Nothing is able to spawn for atleast 450 millis after something has spawned in.
			if(random(0,4000) < 100 && objectList_.size() < 3 && (time - entityTime > 450)) { 
				addEllipse(objectList_, imageCircle);
				entityTime = millis();
			}
			else if(random(0,4000) < 100 && objectList_.size() < 5 && (time - entityTime > 300)) { 
				addEllipse(objectList_, imageWood);
				entityTime=millis();
			}
			else if(random(0,8000) < 100 && objectList_.size() < 5 && (time - entityTime > 800))
			{
				addEllipse(objectList_,angryGuy);
				entityTime = millis();
			}
			
			if(random(0,10000) < 100 && backgroundList_.size() < 3) { 
				addEllipse(backgroundList_, imageCloud);
			}
			if(random(0,10000) < 100 && backgroundList_.size() < 6) { 
				addEllipse(backgroundList_, imageTree);
			}
			
			
			if (doDoubleBuffer_) 
			{
				//	I say that the drawing will take place inside of my off-screen buffer
				gc = offScreenBuffer_;
				offScreenBuffer_.beginDraw();
			}
			else
				//	Otherwise, the "graphic context" is that of this PApplet
				gc = this.g;
			
			
			if (doDoubleBuffer_)
			{
				offScreenBuffer_.endDraw();
				image(offScreenBuffer_, 0, 0);				
	
				
				lastBuffer_.beginDraw();
				lastBuffer_.image(offScreenBuffer_, 0, 0);
				lastBuffer_.endDraw();
	
				int []pixelLB = lastBuffer_.pixels;
				int []pixelOSB = offScreenBuffer_.pixels;
				//int nbPixels = width*height;
				//	Copy pixel info last buffer
				for (int i=0, k=height-1; i<height; i++,k--)
					for (int j=0; j<width; j++)
						pixelLB[k*width + j] = pixelOSB[i*width + j];
				
				lastBuffer_.updatePixels();
			}
			gc.background(50, 50, 255);
			gc.fill(0,0,0);
			gc.noStroke();
			
			if (drawRefFrame_)
				GraphicObject.drawReferenceFrame(gc);
			
			gc.translate(WORLD_X, WORLD_Y);
	 		
	 		//	change to world units
	 		gc.scale(DRAW_IN_WORLD_UNITS_SCALE, -DRAW_IN_WORLD_UNITS_SCALE);	
	 		
	 		//Background Objects
	 		for (GraphicObject obj : backgroundList_)
				obj.drawAllQuadrants(gc);
	 		
	 		//Score 
	 		pushMatrix();
	 		
	 		scale(-1, 1);
	 		rotate(PI);
	 		scale(5);
	 		translate(-80, -80);
	 		textSize(10);
	 		gc.text(score, 10, 10);
	 		
	 		pushMatrix();
	 		scale(.4f);
	 		gc.translate(-10, 60);
	 		gc.text("High Score:"+high_score, 10, 10);
	 		popMatrix();
	 		
	 		//Health 
	 		gc.scale(.08f, .08f);
	 		for(int i = 0; i < health; i++) { 
	 			gc.image(imageHeart,(150*i), 150);
	 		}
	 		popMatrix();
	 		
	 		//Interactable objects.
	 		outerloop:
	 		for (GraphicObject obj : objectList_)
	 		{
	 			if (obj instanceof AnimatedBox)
	 			{
	 				for (GraphicObject obj2 : fireballs)
	 				{
	 					if ( ((AnimatedBox) obj).isHit(obj2.x_, obj2.y_, obj2.height_/2))
	 					{
	 						//System.out.println(obj2.x_);
	 						//System.out.println(obj.x_);
	 						objectList_.remove(obj);
	 						fireballs.remove(obj2);
	 						score+=1;
	 						break outerloop;
	 					}
	 					
	 				}
	 			}
				obj.drawAllQuadrants(gc);
	 		}

	 		for (GraphicObject obj : fireballs)
				obj.draw(gc);
	 		// 	Draw a horizontal line for the "ground"
	 		gc.translate(0, -200);
	 		gc.stroke(0);
	 		gc.line(XMIN, 0, XMAX, 0);
	 		gc.fill(50, 200, 50);
	 		gc.rect(XMIN, 0, 2*XMAX, -200);
	 		gc.pushMatrix();
	 		g.translate(-200, 0);
			int currentTime = millis();
			
			float t = startTime_ != 0 ? (currentTime - startTime_)*0.001f : 0;
			//Set for global here for other functions to be on the same "time"
			global_t = t;
			
			//	get current interpolated state for walking right (right leg)
	 		float []theta1 = right_leg_interpolator_.computeStateVector(t);
	 		
	 		//  get current interpolated state for walking right (left leg)
	 	 	float []theta2 = left_leg_interpolator_.computeStateVector(t);
	 	 	
	 	 	// 	get current interpolated state for walking right (right arm)
	 	 	float []theta3 = right_arm_interpolator_.computeStateVector(t);
	 	 		
	 	 	//  get current interpolated state for walking right (left arm)
	 	 	float []theta4 = left_arm_interpolator_.computeStateVector(t);
	 	 	
	 	 	// get current interpolated state for torso
	 	 	float []theta5 = torso_interpolator_.computeStateVector(t);
	 		
	 	 	
	 	 	
			if(state == 3 && t <= (.4f*modifier)) { 
				movement_v += 9;
			}else if(state == 3 && t <= (.8f*modifier)) { 
				movement_v -= 9;
			}
			
			
	
			if(t > (.8f*modifier) || (t == 0 && state == 0) ) { 
				movement_v = 0;
				state = 0;
				interpolate();
				startTime_ = millis();
				new_state = true;
			}
			
			gc.translate(0, movement_v);
			gc.stroke(LINK_COLOR);
			gc.strokeWeight(1);
			gc.fill(LINK_COLOR);
			if(state != 4) {
				gc.ellipse(headX, Height+Torso_bottom+15, Height/3, Height/3);
			}
			else
			{
				gc.ellipse(headX+57, Height+20, Height/3, Height/3);
			}
			gc.stroke(LINK_COLOR);
			gc.strokeWeight(LINK_THICKNESS);
			//gc.line(0, Torso_bottom, 0, Height+Torso_bottom);
			//gc.noStroke();
			gc.translate(0, Torso_bottom);


			gc.pushMatrix();
			for (int i=1; i<theta1.length; i++)
			{
				gc.rotate(theta1[i]);
				
	
				gc.stroke(LINK_COLOR);
				gc.strokeWeight(LINK_THICKNESS);
				gc.line(0, 0, L1[i], 0);	
				gc.translate(L1[i], 0);
			}
			gc.popMatrix();
			gc.pushMatrix();
			for (int i=1; i<theta2.length; i++)
			{
				gc.rotate(theta2[i]);
				
	
				gc.stroke(LINK_COLOR);
				gc.strokeWeight(LINK_THICKNESS);
				gc.line(0, 0, L2[i], 0);
			
	
				gc.translate(L2[i], 0);
			}
			gc.popMatrix();
			gc.pushMatrix();
			if(state != 4) {
				gc.translate(0, Height);
			}
			else {
				gc.translate(0, Height-8);
			}
			if(state != 4) {
				for (int i=1; i<theta3.length; i++)
				{	
					gc.rotate(theta3[i]);
					
		
					gc.stroke(LINK_COLOR);
					gc.strokeWeight(LINK_THICKNESS);
					gc.line(0, 0, L3[i], 0);
			
		
					gc.translate(L3[i], 0);
				}
			}
			else {
				gc.stroke(LINK_COLOR);
				gc.strokeWeight(LINK_THICKNESS);
				gc.line(0, 0, 30, -30);
			}
			gc.popMatrix();
			gc.pushMatrix();
			if(state != 4) {
				gc.translate(0, Height);
			}
			else {
				gc.translate(0, Height-8);
			}
			if(state != 4) {
				for (int i=1; i<theta4.length; i++)
				{
					gc.rotate(theta4[i]);
					
		
					gc.stroke(LINK_COLOR);
					gc.strokeWeight(LINK_THICKNESS);
					gc.line(0, 0, L4[i], 0);
			
		
					gc.translate(L4[i], 0);
				}
			}
			else {
				gc.stroke(LINK_COLOR);
				gc.strokeWeight(LINK_THICKNESS);
				gc.line(15, 0, 36, -30);
			}
			gc.popMatrix();
			gc.pushMatrix();
			gc.translate(0, Height/14);
			for (int i=1; i<theta5.length; i++)
			{
				gc.rotate(theta5[i]*3);
				
	
				gc.stroke(LINK_COLOR);
				gc.strokeWeight(LINK_THICKNESS);
				gc.line(L5[i], 0, 0, 0);
		
	
				gc.translate(L5[i], 0);
			}
			gc.popMatrix();
			gc.popMatrix();
			
	
			
			//Update their state
			if (animate_)
			{
				update();
			}
			frameCount_++;
			
				
			//Increase the speed 
			speed += .15f;
		}
	}
	
	public void update() {

		int time = millis();
		if (animate_)
		{
			//  update the state of the objects ---> physics
			float dt = (time - lastUpdateTime_)*0.001f;
			
			for (GraphicObject obj: fireballs)
			{
				obj.update(dt);
			}
			
			for (GraphicObject obj : objectList_)
			{
				if (obj instanceof AnimatedObject)
					obj.update(dt);

			}
			
			for (GraphicObject obj : backgroundList_)
			{
				if (obj instanceof AnimatedObject)
					obj.update(dt);

			}
			for (GraphicObject obj: fireballs)
			{
				obj.update(dt);
			}
		}

		lastUpdateTime_ = time;
	}
	
	
	public void keyPressed() {
		
		switch (key) {
		case 'n':
			GraphicObject.setBoundingBoxMode(BoundingBoxMode.NO_BOX);
			break;

		case 'r':
			GraphicObject.setBoundingBoxMode(BoundingBoxMode.RELATIVE_BOX);
			break;

		case 'a':
			GraphicObject.setBoundingBoxMode(BoundingBoxMode.ABSOLUTE_BOX);
			break;

		case 'f':
			drawRefFrame_ = !drawRefFrame_;
			GraphicObject.setDrafReferenceFrame(drawRefFrame_);
			break;
		case ' ':
			if(fireballs.size() < 1)
				fireballs.add(new Fireball(XMIN+200, YMAX-525 + movement_v,0,20,20,0xFFff0000,400,0,0));

				fireball.play();
			break;
		case 's':
			if(new_state) {
				startTime_ = millis();
				state = 4;
				new_state = false;
				interpolate();
			}
			break;
		case 'w':
			if(new_state) {
				startTime_ = millis();
				state = 3;
				new_state = false;
				jump.play();
				interpolate();

			break;
			}
		}
		
	}

	public void mousePressed() {
		if(splashOn == true) {
			if(mouseX >= 210 && mouseX <= 560 && mouseY >= 460 && mouseY <= 520) {
				splashOn = false;
			}
		}
	}
	
	/**	Converts pixel coordinates into world coordinates
	 * 
	 * @param ix	x coordinate of a window pixel
	 * @param iy	y coordinate of a window pixel
	 * @return	Corresponding world coordinates stored into a Point2D.Float object
	 */
	private Point2D.Float pixelToWorld(int ix, int iy) 
	{
		return new Point2D.Float((ix-WORLD_X)*PIXEL_TO_WORLD, -(iy-WORLD_Y)*PIXEL_TO_WORLD);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("v2.dino");
	}
}