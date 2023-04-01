package gymnasiegame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class PlayerCharacter extends Entity {

	public MissileEntity missileV1 = null; // ingen missil!

	
	// array av bilder som ändras berodende på playercharacters riktning
	private Image[] images;
	//ett antal ticks som används till animering av playercharacter
	private int ticks = 0;
	// bild som representerar missileV1
	private Image  missileImageV1 = new ImageIcon(getClass().getResource("/missile.png")).getImage(); 

/**Konstruktor
 * @param images, array av bilder som playercharacter skall representeras av
 * @param map 		rutnätet med spelplanen
 * @param xPos 		x-position på skärmen
 * @param yPos 		y-position på skärmen
 * @param speed		hastighet med vilket playercharacter rör sig
 * @param dx		X-leds riktning i vilken playercharacter rör sig
 * @param dy		Y-leds riktning i vilken playercharacter rör sig
 * @param active	playercharacters livstatus, true/false
 */
	public PlayerCharacter(Image[] images, Map map, double xPos, double yPos, int speed, int dx, int dy,
			boolean active) {
		super(images[0], map, xPos, yPos, speed, dx, dy, active);
		this.images = images;
	}

	/**
	 * Förflyttar spelfiguren beroende på förfluten tid
	 * @param deltaTime tid som förflutit sedan förra uppdateringen i nano sekunder
	 */
	@Override
	public void move(long deltaTime) {
		// TODO Auto-generated method stub
		
		if (missileV1 != null && missileV1.getActive()) {
			missileV1.move(deltaTime);
		}
//		yPos += dy * (deltaTime / 1000000000.0) * speed;
//		xPos += dx * (deltaTime / 1000000000.0) * speed;

		ticks++;
		setMovingImg();
		
		double nx = xPos + dx*speed*(deltaTime/1000000000.0);
		double ny = yPos + dy*speed*(deltaTime/1000000000.0);

		// Kontrollerar om den nya positionen fungerar
		//blockera Tile 0, Water
		if (!validLocation(nx, ny)) {
			if (validLocation(nx, yPos)) {
				// if it doesn't then change our position to the new position
				xPos = nx;
			}

			if (validLocation(xPos, ny)) {
				// if it doesn't then change our position to the new position
				yPos = ny;
			}	
		}else {
			xPos = nx;
			yPos = ny;
		}	
	}

	/** en function som kontrolerar viken riktning figuren rör sig i efter 8 ticks.
	 * efter 8 ticks så ändras spelfiguren beroende på vilken riktning den rör sig i. På så sätt skapas en animation för figuren
	 */
	private void setMovingImg() {
		if (ticks % 8 == 0) {
			if (getDirectionX() == 1) {
				setImage(images[4]);
			}
			if (getDirectionX() == -1) {
				setImage(images[2]);
			}
			if (getDirectionY() == 1) {
				setImage(images[6]);
			}
			if (getDirectionY() == -1) {
				setImage(images[5]);
			}
		}
	}

	/**
	 * en function som kontrolerar om klassen missile är skapad eller inte. 
	 * Om missileV1 är null, då skall den skapas vid anrop av funtionen
	 * Om missileV1 inte är null, då skall inget ske, false retuneras
	 * @return true/false och skapar missiler beroende på resultatet.
	 */
	public boolean tryToFire() {

		if (missileV1 == null || !missileV1.getActive()) {
			missileV1 = new MissileEntity(missileImageV1, map,
					xPos + 13, yPos, 90, 0, -1, active);
			missileV1.setActive(true);
			return true;
		} else
			return false;
	}

	/**
	 * Ritar ut denna Entity/missileV1 till den givna grafik formatet, Grafiphics2D
	 * @param g Den giva grafikformatet som Entitien skall ritas till. 
	 */
	public void draw(Graphics2D g) {
		if (missileV1 != null && missileV1.getActive()) {
			missileV1.draw(g);
		}
		super.draw(g);
	}

}
