package gymnasiegame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ShipEntity extends Entity {

	public MissileEntity missile = null; // ingen missil!

//	private Map map;
	public ShipEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);
	}

	/**
	 * Ändrar läget i x-led
	 */
	public void move(long deltaTime) {
		if (missile != null && missile.getActive()) {
			missile.move(deltaTime);
		}
		xPos += dx * (deltaTime / 1000000000.0) * speed;
		yPos += dy * (deltaTime / 1000000000.0) * speed;

	}

	public boolean tryToFire() {
		if (missile == null || !missile.getActive()) {
			missile = new MissileEntity(new ImageIcon(getClass().getResource("/missile.png")).getImage(), map,
					xPos + 13, yPos, 90, 0, 0, active);

			missile.setActive(true);
			return true;
		} else
			return false;
	}

	public void draw(Graphics2D g) {
		if (missile != null && missile.getActive()) {
			missile.draw(g);
		}
		super.draw(g);
	}
}
