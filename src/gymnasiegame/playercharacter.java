package gymnasiegame;

import java.awt.Image;


public class playercharacter extends Entity {
	private Image[] images;//standing,up1,up2,right1,right2,left2,left1, playerImage;
	private int ticks = 0;
	
	public playercharacter(Image[] images, Map map, double xPos, double yPos, int speed) {
		super(images[0], map, xPos, yPos, speed);
		this.images = images;
	}

	@Override
	public void move(long deltaTime) {
		// TODO Auto-generated method stub
		yPos += dy*(deltaTime/1000000000.0)*speed;
		xPos += dx*(deltaTime/1000000000.0)*speed;
		
		ticks++;
		setMovingImg();
	}
	
	private void setMovingImg() {
		if(ticks % 8 == 0) {
	
			if(getDirectionX()==1) {
				setImage(images[4]);
			}	
			
			 if(getDirectionX()==-1) {
				setImage(images[2]);
			}
			 
			 if(getDirectionY()==1) {
				 setImage(images[6]);
			 }
			 if(getDirectionY()==-1) {
				 setImage(images[5]);
			 }
			 
		}	
	}
	
	
	
}
