package game.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import game.view.Map;
import se.egy.graphics.Drawable;

/**
 * Klassen består av ett antal functioner för att förenkla 
 * skapelsen av olika entities sant innehåller en grund konstuktor till alla entitiees.
 *
 * @author Karam Matar
 * @version 1.0
 * @since 2023-02-14
 */

public abstract class Entity implements Drawable {
	// Bilden som representerar figuren
	private Image image;
	//x och y koordinaten på skärmen där figuererna skall placeras
	protected double xPos, yPos; 
	//figurernas höjd och bredd
	private int hight, width; 
	// Hastighet i px/sekund
	protected int speed; 
	// Rörelseriktning
	protected int dx = 0, dy = 0; 
	// Gör alla nya objekt aktiva.
	protected boolean active = true; 
	//karta/rutnär som fungerar som spelplan
	protected Map map;

	/** används till kollision hantering
	 *  Värde i rutnätet som inte spelfiguren kan gå till, typ en vägg.
	 *  blockedList
	 */
	private int blocked = 0;//water
	private int TreeBlockV1 = 5;
	private int AnotherTileBlock= 2;

	private Rectangle rec = null;

	/**Konstruktor
	 * @param image	tar emot en bild som Entity skall få ha
	 * @param map	tar emot en map där Entity skall displayas/ rutnät med spelplan
	 * @param xPos	X koordinaten för entity på skärmen
	 * @param yPos	Y koordinaten för Entity på skärmen
	 * @param speed		hastighet för Entity
	 * @param dx		entiys direction för X-led
	 * @param dy		Entities direction för Y-led
	 * @param active	Entities liv status, true/false
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
	 * Metoden skapar en rectangle som förflyttar sig med Entity, 
	 * @return rec, rectangle som förflyttar sig med Entity
	 */
	public Rectangle getRectangle() {
		rec.setLocation((int) xPos, (int) yPos);
		return rec;
	}
	/**
	 * kontrollerar kollision mellan olika entities
	 * @param entity, entity som skall kollidera med den en annan entity( den som kallar på funktionen)
	 * @return rec.intersects(entity.getRectangle()); 
	 */
	public boolean collision(Entity entity) {
		getRectangle(); // Uppdaterar positionen på den egna rektangeln
		return rec.intersects(entity.getRectangle());
	}

	/**
	 * abstact metod, tvingar använding av metoden move för all Entities, 
	 * tar emot param deltaTime och använder den för metoden move som impleminteras i klasser som extendar Entity.
	 * Förflyttar spelfiguren beroende på förfluten tid
	 * @param deltaTime, tiden mellan uppdateringar
	 */
	public abstract void move(long deltaTime);
	
	/**
	 * Kontrollerar om en position på skärmen inte är blockerd
	 * @param nx, det nya xPos värdet
	 * @param ny, det nya yPos värdet
	 * @return	ture/false beroende på om de nya x och y är tillåtna eller inte
	 */
	public boolean validLocation(double nx, double ny) {
		if(hitTile(nx, ny, blocked).x != -1 ||hitTile(nx, ny, TreeBlockV1).x != -1 ||hitTile(nx, ny, AnotherTileBlock).x != -1 ) {
			return false;
		}else {
			return true;
		} 
	}
	/**
	 * 	en metod som beräknar de nya möjliga punkter på kartan där Entity kan förflytta sig.
	 * @param xPos, det nuvarande xPos
	 * @param yPos, det nuvarande yPos
	 * @param tileValue, det nuvarande tileValue ( siffor som lässes in ur map.txt filen)
	 * @return point(x,y), den nya punkten på map som där Entity skulle kunna förflytta sig till
	 * 
	 *  */
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
	 * @param tileValue, värdet på rutorna
	 * @return koordinaten på rutan i rutnätet med sökt värde Om den inte finns returneras(-1,-1)
	 */
	public Point hitTile(int tileValue) {		
		return hitTile(xPos, yPos, tileValue);
	}

	//setters/getters
	
	/**
	 * Vilken riktning i x-led
	 * @param dx 0 = stilla, 1 = höger, -1 = vänster
	 */
	public void setDirectionX(int dx) {
		this.dx = dx;
	}
	/**
	 * retunerar dx
	 * @return Direction X, dx
	 */
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
	/**
	 * retunerar dy, figurens riktning för yled
	 * @return Direction Y, dy
	 */
	public int getDirectionY() {
		return dy;
	}

	/**
	 * retunerar hight/höjd
	 * @return Entities höjd, hight 
	 */
	public int getHight() {
		return hight;
	}
	/**
	 * retunerar width/bredd
	 * @return Entities bredd, width 
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * retunerar x koordinaten
	 * @return Entities X position, xPos 
	 */
	public double getXPos() {
		return xPos;
	}
	/**
	 *  sätter xPos, x positionen,  till ett nytt värde av typen double
	 * @param xPos X positionen
	 */
	public void setXPos(double xPos) {
		this.xPos = xPos;
	}
	/**
	 * retunerar Y koordinaten
	 * @return Entities Y position, yPos 
	 */
	public double getYPos() {
		return yPos;
	}
/**
 * sätter yPos, y positionen, till ett nytt värde av typen double
 * @param yPos, Entities y positionen
 */
	public void setYPos(double yPos) {
		this.yPos = yPos;
	}
	/**
	 * retunerar bilder som är satta för Entity
	 * @return Image
	 */
	public Image getImage() {
		return image;
	}
	/**
	 * Sätter en bilb av typen Image
	 * @param image sätter in en bild som representerar Entity
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	/**
	 * sätter active status true/false på Entity, dvs Entities liv status. 
	 * @param active, sätter active status.
	 */
	public void setActive(boolean active) {
		this.active = active;

	}
	/**retunerar liv status på Entity, True/false
	 * @return active
	 */
	public boolean getActive() {
		return active;
	}
	
	
	

}
