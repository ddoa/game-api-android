package android.gameengine.icadroids.objects.collisions;

import android.gameengine.icadroids.objects.GameObject;

/**
 * @author Edward van Raak
 * This class will create bounding circle for collision detection between game-objects.
 */
public class BoundingCircle {	
	
	/**
	 * Radius of the circle
	 */
	private double radius;
	/**
	 * X coordinate of the center of the circle
	 */
	private double  centerX;
	/**
	 * Y coordinate of the center of the circle
	 */
	private double  centerY;
	/**
	 * GameObject class that is the holder of this class instance
	 */
	private GameObject gameobject;
	/**
	 * Boolean to check what contructor has been used. 
	 */
	private boolean isPartOfObject = false;
	
	/** 
	 * Construct a bounding circle for collision detection between game-objects.
	 * @param gameobject The gameobject that is using this BoundingCircle
	 * @param radius The radius of the circle
	 * @param centerX  X coordinate of the center of the circle
	 * @param centerY  Y coordinate of the center of the circle
	 */
	public BoundingCircle(GameObject gameobject, double radius, double centerX, double centerY) {
		
		this.radius = radius;
		this.centerX = centerX;
		this.centerY = centerY;
		this.gameobject = gameobject;
		isPartOfObject = true;
	}
	
	/**
	 * Construct a bounding circle for collision detection.
	 * @param radius The radius of the circle
	 * @param centerX  X coordinate of the center of the circle
	 * @param centerY  Y coordinate of the center of the circle
	 */
	public BoundingCircle(double radius, double centerX, double centerY) {
		
		this.radius = radius;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	/***
	 * Check whenever the given circle intersects this circle. 
	 * @param intersectedCircle The circle that should be tested for intersection
	 * @return True if collision was detected, false if no collision was detected
	 */
	public boolean intersects(BoundingCircle intersectedCircle){	
		final double a = (this.radius+intersectedCircle.getRadius()) * (this.radius+intersectedCircle.getRadius());
		final double dx = this.getCenterX() - intersectedCircle.getCenterX();
		final double dy = this.getCenterY() - intersectedCircle.getCenterY();	
	    return (a > (dx*dx) + (dy*dy));	//return true if distance between 2 radi are smaller than the total distance
	}
	
	/***	 
	 * Check whenever the given circle CENTER is being contained inside this circle
	 * @param containedCircle
	 * @return True if the center was inside. False is the center was outside. 
	 */
	public boolean contains(BoundingCircle containedCircle)
	{
		double c = getDistancebetweenCenters(containedCircle);
		return (c < this.radius);
	}
	
	/***
	 *  Check whenever the given circle RADIUS is being contained inside this circle
	 * @param containedCircle
	 * @return
	 */
	public boolean fullycontains(BoundingCircle containedCircle)
	{		
		double c = getDistancebetweenCenters(containedCircle);		
		return (c < this.radius && (c+containedCircle.getRadius() < this.radius));
	}
	
	/**
	 * Calculate the distance between the center of this circle with the given circle
	 * @param containedCircle
	 * @return the distance 
	 */
	public double getDistancebetweenCenters(BoundingCircle containedCircle)
	{
		final double dx = this.getCenterX() - containedCircle.getCenterX();
		final double dy = this.getCenterY() - containedCircle.getCenterY();	
		final double c  = Math.sqrt((dx*dx) + (dy*dy));
		return c;			
	}
	
	/**
	 * Place this Bounding Circle in the middle of a GameObject sprite
	 * The location is calculated by taking the width and height of the sprite that it's using. 
	 */
	public void moveToCentorOfGameObject()
	{
		if(isPartOfObject)
		{
			float width = gameobject.getFrameWidth()/2;
			float height = gameobject.getFrameHeight()/2;
			setCenterX(gameobject.getX() + width);
			setCenterY(gameobject.getY() + height);
		}
		else 
		{
			throw new NullPointerException("A bounding circle was tried to be placed on a non-specified gameObject.");
		}
	}
	

	
	/***
	 * Call for a draw method inside the renderder that allows you to see the circle bounds for test purposes. 
	 * @param x
	 * @param y
	 * @param radius
	 */
	@SuppressWarnings("unused")
	private void drawDebugCircle(float x, float y, float radius) {
		/**TODO: Implement method**/
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * @return the diameter
	 */
	public double getDiameter() {
		return radius + radius;
	}
	
	

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @return the centerX
	 */
	public double getCenterX() {
		return centerX;
	}

	/**
	 * @param centerX the centerX to set
	 */
	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	/**
	 * @return the centerY
	 */
	public double getCenterY() {
		return centerY;
	}

	/**
	 * @param centerY the centerY to set
	 */
	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}
	
}
