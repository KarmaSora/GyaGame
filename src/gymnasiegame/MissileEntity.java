package gymnasiegame;

import java.awt.Image;

public class MissileEntity extends Entity {
/**Konstruktor
 * @param image,   	bilden som MissileEntity skall representeras av
 * @param map 		rutnätet med spelplanen
 * @param xPos 		x-position på skärmen
 * @param yPos 		y-position på skärmen
 * @param speed		hastighet med vilket MissileEntity rör sig
 * @param dx		X-leds riktning i vilken MissileEntity rör sig
 * @param dy		Y-leds riktning i vilken MissileEntity rör sig
 * @param active	MissileEntity livstatus, true/false
 */
	public MissileEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);
		this.setActive(false);
	}

	/**
	 * Förflyttar spelfiguren beroende på förfluten tid
	 * @param deltaTime tid som förflutit sedan förra uppdateringen i nano sekunder
	 */
	@Override
	public void move(long deltaTime) {
		yPos += dy * (deltaTime / 1000000000.0) * speed;
		xPos += dx * (deltaTime / 1000000000.0) * speed;
	}

}
