package gymnasiegame;

import java.awt.Image;

public class ShipEntity extends Entity{
//	private Map map;
 	public ShipEntity (Image image, Map map, double xPos, double yPos, int speed){
      	super(image, map, xPos, yPos, speed);
 	}
 
 	/**
 	 * Ändrar läget i x-led
 	 */
 	public void move(long deltaTime){
    	   xPos += dx*(deltaTime/1000000000.0)*speed;
 	}
} 

