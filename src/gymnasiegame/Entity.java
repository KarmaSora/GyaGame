package gymnasiegame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import se.egy.graphics.Drawable;

public abstract class Entity implements Drawable {
	private Image image;

	protected double xPos, yPos;
	private int hight, width; // Positionen

	protected int speed; // Hastighet i px/sekund

	protected int dx = 0, dy = 0; // Rörelseriktning

	protected boolean active = true; // Gör alla nya objekt aktiva.
	protected Map map;

	// används till kollision hantering
	
	//blockedList
	private int blocked = 0;//water
	private int TreeBlockV1 = 5;
	private int AnotherTileBlock= 2;

	private Rectangle rec = null;

	/**
	 * Konstruktor
	 */
	public Entity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		this.image = image;
		this.map = map;
		this.xPos = xPos;
		this.yPos = yPos;
		this.speed = speed;
		this.dx = dx;
		this.dy = dy;
		this.active = active;

		this.hight = image.getHeight(null);
		this.width = image.getWidth(null);

		rec = new Rectangle((int) xPos, (int) yPos, image.getWidth(null), image.getHeight(null));
	}

	/**
	 * Ritar bilden på ytan g
	 */
	public void draw(Graphics2D g) {
		g.drawImage(image, (int) xPos, (int) yPos, null);
	}

	/**
	 * Vilken riktning i x-led
	 * 
	 * @param dx 0 = stilla, 1 = höger, -1 = vänster
	 */
	public void setDirectionX(int dx) {
		this.dx = dx;
	}

	public int getDirectionX() {
		return dx;
	}

	/**
	 * Vilken riktning i y-led
	 * @param dy 0 = stilla, 1 = ner, -1 = upp
	 */
	public void setDirectionY(int dy) {
		this.dy = dy;
	}

	public int getDirectionY() {
		return dy;
	}

	public int getHight() {
		return hight;
	}

	public int getWidth() {
		return width;
	}

	public double getXPos() {
		return xPos;
	}

	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Metod som gör förflyttningen, dvs ändrar xPos och yPos Måste skapas i klasser
	 * som ärver entity
	 * 
	 * @param antal nanosekunder sedan förra anropet
	 */

	public void setActive(boolean active) {
		this.active = active;

	}

	public boolean getActive() {
		return active;
	}

	public Rectangle getRectangle() {
		rec.setLocation((int) xPos, (int) yPos);
		return rec;
	}

	public boolean collision(Entity entity) {
		getRectangle(); // Uppdaterar positionen på den egna rektangeln
		return rec.intersects(entity.getRectangle());
	}

	public abstract void move(long deltaTime);
	
	
	
	
	public boolean validLocation(double nx, double ny) {
		if(hitTile(nx, ny, blocked).x != -1) {
			return false;
		}else {
			return true;
		} 
	}
	
	
	
	private Point hitTile(double xPos, double yPos, int tileValue) {
		int x = -1, y = -1;
		
		if (map.getTileFromPx(xPos, yPos) == tileValue) {
			x = (int)xPos/map.getTileSize(); 
			y = (int)yPos/map.getTileSize();
			return new Point(x, y);
		}
		if (map.getTileFromPx(xPos + image.getWidth(null)-1, yPos) == tileValue) {
			x = (int)(xPos + image.getWidth(null)-1)/map.getTileSize();
			y = (int)yPos/map.getTileSize();
			return new Point(x, y);
		}
		if (map.getTileFromPx(xPos + image.getWidth(null)-1, yPos + image.getHeight(null)-1) == tileValue) {
			x = (int)(xPos + image.getWidth(null)-1)/map.getTileSize();
			y = (int)(yPos + image.getHeight(null)-1)/map.getTileSize();
			return new Point(x, y);
		}
		if (map.getTileFromPx(xPos, yPos + image.getHeight(null)-1) == tileValue) {
			x = (int)(xPos)/map.getTileSize();
			y = (int)(yPos + image.getHeight(null)-1)/map.getTileSize();
			return new Point(x, y);
		}
		
		return new Point(x, y);
	}
	/**
	 * Kontrollerar om spelfiguren ligger på en ruta med ett visst värde.
	 * 
	 * @param tileValue
	 * @return koordinaten på rutan i rutnätet med sökt värde Om den inte finns returneras(-1,-1)
	 */
	public Point  hitTile(int tileValue) {		
		return hitTile(xPos, yPos, tileValue);
	}

	
	
	
	/*Test, block a new Tile*/

	
	public boolean validLocationV2Tree(double nx, double ny) {
		if(hitTile(nx, ny, TreeBlockV1).x != -1) {
			return false;
		}else {
			return true;
		} 
	}
	
	public boolean validLocationV3Test(double nx, double ny) {
		if(hitTile(nx, ny, AnotherTileBlock).x != -1) {
			return false;
		}else {
			return true;
		} 
	}
	
	
	
	

}
