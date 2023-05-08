package game.model;

import java.awt.Image;
import game.view.Map;

/**
 * En klass som extendar Entity och utökar Entities med att ha en move metod som inte är abstract
 *
 * @author Karam Matar
 * @version 1.0
 * @since 2023-02-14
 */

public class AlienEntity extends Entity {
	/**Konstruktor
	 * @param image, 	 bilder som AlineEntity skall representeras av
	 * @param map 		rutnätet med spelplanen
	 * @param xPos 		x-position på skärmen
	 * @param yPos 		y-position på skärmen
	 * @param speed		hastighet med vilket AlineEntity rör sig
	 * @param dx		X-leds riktning i vilken AlineEntity rör sig
	 * @param dy		Y-leds riktning i vilken AlineEntity rör sig
	 * @param active	AlineEntity livstatus, true/false
	 */
	public AlienEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);
	}	
	/**
	 * Förflyttar spelfiguren beroende på förfluten tid
	 * @param deltaTime tid som förflutit sedan förra uppdateringen i nano sekunder
	 */
	@Override
	public void move(long deltaTime) {
		yPos += dy * (deltaTime / 1000000000.0) * speed;

	}

}
