package gymnasiegame;

import java.awt.Image;

public class AlienEntity extends Entity {

	public AlienEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy, boolean active) {
		super(image, map, xPos, yPos, speed, dx, dy, active);
	}	

	@Override
	public void move(long deltaTime) {
		yPos += dy * (deltaTime / 1000000000.0) * speed;

	}

}
