package gymnasiegame;

import java.awt.Image;

public class AlienEntity extends Entity {
	
	public AlienEntity(Image image, Map map, double xPos, double yPos, int speed, int dx, int dy) {
		super(image, map, xPos, yPos, speed);
		// TODO Auto-generated constructor stub
			this.dy = dy;
			this.dx=dx;
	}

	@Override
	public void move(long deltaTime) {
		// TODO Auto-generated method stub
		yPos += dy*(deltaTime/1000000000.0)*speed;
 	 
	}

}
