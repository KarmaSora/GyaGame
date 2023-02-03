package gymnasiegame;

import java.awt.Graphics2D;
import java.awt.Image;

import se.egy.graphics.Drawable;

public abstract class Entity implements Drawable {
    protected Image image;
   
    protected double xPos, yPos;
    protected int hight, width;   // Positionen
   
    protected int speed;           // Hastighet i px/sekund
   
    protected int dx = 0, dy = 0;  // Rörelseriktning
   
    private boolean active = true; // Gör alla nya objekt aktiva.
   
    /**
     * Konstruktor
     */
    public Entity (Image image, double xPos, double yPos, int speed){
     	this.image = image;   
     	this.xPos = xPos;
     	this.yPos = yPos;
     	this.speed = speed;
     	this.hight = image.getHeight(null);
     	this.width = image.getWidth(null);
    }
   
    /**
     * Ritar bilden på ytan g
     */
    public void draw(Graphics2D g) {
     	g.drawImage(image,(int)xPos,(int)yPos,null);
    }
    /**
     * Vilken riktning i x-led
     * @param dx 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionX(int dx){
     	this.dx = dx;
    }
    public int getDirectionX(){
    	return dx;
    }    
    /**
     * Vilken riktning i y-led
     * @param dy 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionY(int dy){
     	this.dy = dy;
    }
   
    public int getDirectionY(){
    	return dy;
    }    
    public int getHight(){
    	return hight;
    }
    public int getWidth(){
    	return width;
    }
    
    public double getXPos() {
    	return xPos;
    }
    public void setXPos(int xPos) {
    	this.xPos= xPos;
    }
    
    public double getYPos() {
    	return yPos;
    }
    public Image getImage() {
    	return image;
    }
    public void setImage(Image image) {
    	this.image = image;
    }
    
    
    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     * @param antal nanosekunder sedan förra anropet 
     */
    public abstract void move(long deltaTime);
}
