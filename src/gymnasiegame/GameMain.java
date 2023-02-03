package gymnasiegame;

import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import se.egy.graphics.*;

public class GameMain implements KeyListener {

	private boolean gameRunning = true;
	private boolean gamePause = false;
	private long lastUpdateTime;
	private long gameTime = 0;

	private HashMap<String, Boolean> keyDown = new HashMap<>();

	private ShipEntity ship;
	private AlienEntity alien;
	private Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
	private ArrayList<Entity> entityList = new ArrayList<>();
	private GameScreen gameScreen = new GameScreen("GameGyAVersion", 800, 600, false);

	private playercharacter MyPlayer;
	protected Image[] images = new Image[7];

	
//	private Image up1,up2,right1,right2,left1,left2,standing;
//	private Image currentPlayer;
//	private Image currentPlayer = new ImageIcon(getClass().getResource("/playerStanding.png")).getImage();
//	protected String DirectionInfo;

	public GameMain() {
		gameScreen.setBackground("/space-background.jpg");
		gameScreen.setKeyListener(this);
		keyDown.put("left", false);
		keyDown.put("right", false);
		// keyDown.put("pauseTest", false);

		// loadPlayerImages();
		loadImages();
		gameLoop();
	}

	public void loadImages() {
		/*
		 * double myPlayerX = 300; double myPlayerY = 300; int myPlayerSpeed = 30;
		 * MyPlayer = new
		 * playercharacter(currentPlayer,myPlayerX,myPlayerY,myPlayerSpeed);
		 * 
		 * entityList.add(MyPlayer);
		 * 
		 * 
		 */

		//Image NyPlayer = new ImageIcon(getClass().getResource("/playerStanding.png")).getImage();
//		Image[] images = new Image[7];
		
		for(int i = 0; i < 7; i ++) {
			images[i] = new ImageIcon(getClass().getResource("/player-" + i + ".png")).getImage();
		}
		double MyPlayersXPos = gameScreen.getWidth() / 2;
		double MyPlayersYPos = 300;
		int MyPlayerSpeed = 200;
		MyPlayer = new playercharacter(images, MyPlayersXPos,MyPlayersYPos,MyPlayerSpeed);
		entityList.add(MyPlayer);

	
		
		
		
		double x = gameScreen.getWidth() / 2;
		double y = gameScreen.getHeight() - shipImg.getHeight(null);
		int shipSpeed = 200;
		ship = new ShipEntity(shipImg, x, y, shipSpeed);
		entityList.add(ship);

		Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
		int alienSpeed = 20;
		double xAlien = 200;
		double yAlien = 200;
		int dyAlien = 1;
		int dxAlien = 0;
		alien = new AlienEntity(alienImg, xAlien, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);

		alien = new AlienEntity(alienImg, xAlien + 100, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
		alien = new AlienEntity(alienImg, xAlien + 200, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
		alien = new AlienEntity(alienImg, xAlien + 300, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
	}

	public void update(long deltaTime) {
		if (keyDown.get("right") && ship.getXPos() < (gameScreen.getWidth() - ship.getWidth())) {
			ship.setDirectionX(1);
			MyPlayer.setDirectionX(1);
			MyPlayer.setImage(images[3]);
			

		} else if (keyDown.get("left") && ship.getXPos() > 0) {
			ship.setDirectionX(-1);
			MyPlayer.setDirectionX(-1);
			
			
			/*	int ImageSwitchCounter = 0;
			if(ImageSwitchCounter == 0) {
				MyPlayer.setImage(images[1]);
				ImageSwitchCounter++;
				 
			} if(ImageSwitchCounter == 1) {
				MyPlayer.setImage(images[2]);
				ImageSwitchCounter--;
			}
*/
		
			
	



		} else {
			ship.setDirectionX(0);
			ship.setDirectionY(0);
			
			MyPlayer.setDirectionX(0);
			MyPlayer.setDirectionY(0);
			MyPlayer.setImage(images[0]);


		}

		for (Entity entity : entityList) {
			entity.move(deltaTime);
		}

		if (ship.getXPos() < 0)
			ship.setXPos(0);

	}

	public void render() {
		gameScreen.render(entityList);

	}

	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		int fps = 30;
		int updateTime = (int) (1.0 / fps * 1000000000.0);
		lastUpdateTime = System.nanoTime();
		double TimeInSecoundsCounter = 0;
		
		while (gameRunning) {
			long deltaTime = System.nanoTime() - lastUpdateTime;
			if (deltaTime > updateTime) {
				lastUpdateTime = System.nanoTime();

				if (!gamePause) {
					update(deltaTime);
					render();

					gameTime = System.currentTimeMillis() - startTime;
					gameTime = gameTime / 1000;
					
					if (gameTime >= 0.99) {
						TimeInSecoundsCounter++;
						System.out.println(TimeInSecoundsCounter);
					}
				}
			}
		}
	}

	/** Spelets tangentbordslyssnare */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT)
			keyDown.put("left", true);
		else if (key == KeyEvent.VK_RIGHT)
			keyDown.put("right", true);
		else if (key == KeyEvent.VK_ESCAPE) {
			System.out.println("Game has ended.");
			System.exit(0);
		}
		if (key == KeyEvent.VK_SPACE) {
			System.out.println("pause/start");
			gamePause = !gamePause;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT)
			keyDown.put("left", false);
		else if (key == KeyEvent.VK_RIGHT)
			keyDown.put("right", false);
	}

	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		System.out.println("this is a game, GYA version");
		new GameMain();

	}
}
