package gymnasiegame;

import java.awt.Image;

public class MissileEntity extends Entity {
	public MissileEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);

		this.setActive(false);
	}

	@Override
	public void move(long deltaTime) {
		yPos += dy * (deltaTime / 1000000000.0) * speed;
		xPos += dx * (deltaTime / 1000000000.0) * speed;
	}

}
