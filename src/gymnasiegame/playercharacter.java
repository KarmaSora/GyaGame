package gymnasiegame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class playercharacter extends Entity {

	public MissileEntity missileV1 = null; // ingen missil!

	private Image[] images;// standing,up1,up2,right1,right2,left2,left1, playerImage;
	private int ticks = 0;
	private Image  missileImageV1 = new ImageIcon(getClass().getResource("/missile.png")).getImage(); 


	public playercharacter(Image[] images, Map map, double xPos, double yPos, int speed, int dx, int dy,
			boolean active) {
		super(images[0], map, xPos, yPos, speed, dx, dy, active);
		this.images = images;
	}

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

	public boolean tryToFire() {
		if (missileV1 == null || !missileV1.getActive()) {
			missileV1 = new MissileEntity(missileImageV1, map,
					xPos + 13, yPos, 90, 0, -1, active);
			missileV1.setActive(true);
			return true;
		} else
			return false;
	}

	public void draw(Graphics2D g) {
		if (missileV1 != null && missileV1.getActive()) {
			missileV1.draw(g);
		}
		super.draw(g);
	}

}
