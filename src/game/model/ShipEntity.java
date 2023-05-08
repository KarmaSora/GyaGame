package game.model;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import game.view.Map;
/**
 * En klass som extendar Entity och utökar Entities med att ha en move metod som inte är abstract,
 * låter figuren föflytta sig i både x och y led. 
 * klassen har även möjlighet att skapa figurer ur en annan klass för att skapa missiler. 
 *	 
 * @author Karam Matar
 * @version 1.0
 * @since 2023-02-14
 */
public class ShipEntity extends Entity {

	// skapar instans av MissileEntity men skapar ingen missil!
	public MissileEntity missile = null; 

	/**Konstruktor
	 * @param image,  	bilden som ShipEntity skall representeras av
	 * @param map 		rutnätet med spelplanen
	 * @param xPos 		x-position på skärmen
	 * @param yPos 		y-position på skärmen
	 * @param speed		hastighet med vilket ShipEntity rör sig
	 * @param dx		X-leds riktning i vilken ShipEntity rör sig
	 * @param dy		Y-leds riktning i vilken ShipEntity rör sig
	 * @param active	ShipEntity livstatus, true/false
	 */
	public ShipEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);
	}

	/**
	 * Förflyttar spelfiguren beroende på förfluten tid
	 * @param deltaTime tid som förflutit sedan förra uppdateringen i nano sekunder
	 */
	public void move(long deltaTime) {
		if (missile != null && missile.getActive()) {
			missile.move(deltaTime);
		}
		xPos += dx * (deltaTime / 1000000000.0) * speed;
		yPos += dy * (deltaTime / 1000000000.0) * speed;

	}
	/**
	 * en function som kontrolerar om klassen missile är skapad eller inte. 
	 * Om missileV1 är null, då skall den skapas vid anrop av funtionen
	 * Om missileV1 inte är null, då skall inget ske, false retuneras
	 * @return true/false och skapar missiler beroende på resultatet.
	 */
	public boolean tryToFire() {
		if (missile == null || !missile.getActive()) {
			missile = new MissileEntity(new ImageIcon(getClass().getResource("/missile.png")).getImage(), map,
					xPos + 13, yPos, 90, 0, 0, active);

			missile.setActive(true);
			return true;
		} else
			return false;
	}

	/**
	 * Ritar ut denna Entity/missileV till den givna grafik formatet, Grafiphics2D
	 * @param g Den giva grafikformatet som Entitien skall ritas till. 
	 */
	public void draw(Graphics2D g) {
		if (missile != null && missile.getActive()) {
			missile.draw(g);
		}
		super.draw(g);
	}
}
