package gymnasiegame;

import java.awt.Image;


public class playercharacter extends Entity {
	protected Image[] images;//standing,up1,up2,right1,right2,left2,left1, playerImage;
	
	private int ticks = 0;
	
	public playercharacter(Image[] images, double xPos, double yPos, int speed) {
		super(images[0], xPos, yPos, speed);
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
			if(dx == -1) {

				
				if(getImage()==images[1] ){
					setImage(images[2]);
				}
				else if(getImage()==images[2]) {
					
					setImage(images[1]);
				}
				else {
					setImage(images[6]);
				}
				
				
			}
			
			
		}
		
	}
	

}
