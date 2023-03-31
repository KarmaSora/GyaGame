package gymnasiegame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import se.egy.graphics.*;

public class GameMain implements KeyListener {

	private boolean gameRunning = true;
	private boolean gamePause = false;
	private long lastUpdateTime;
	private Map map;
	private int GameScore = 0;

	private double worldX, worldY;
	public Font font = null;
	private TxtContainer msg;

	private HashMap<String, Boolean> keyDown = new HashMap<>();

	private ShipEntity ship;
	private AlienEntity alien;
	private Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
	private ArrayList<Entity> entityList = new ArrayList<>();
	private GameScreen gameScreen = new GameScreen("GameGyAVersion", 700, 600, false);

	private PlayerCharacter myPlayer;
	private Image[] images = new Image[7];

	public GameMain() {
		map = new Map("map2.txt", 32);
		gameScreen.setBackground(map);
		gameScreen.setKeyListener(this);
		keyDown.put("PlayerLeft", false);
		keyDown.put("PlayerRight", false);
		keyDown.put("PlayerUp", false);
		keyDown.put("PlayerDown", false);
		keyDown.put("leftByAButton", false);
		keyDown.put("RightByDButton", false);
		keyDown.put("jumpOrTeleportUp", false);
		keyDown.put("TPDown", false);

		keyDown.put("fireM", false);
		keyDown.put("myPlayerFire", false);

		System.out.println("använd camera för att röra gamescreen!! ");

		try {
			String path = getClass().getResource("/droidloverpi.ttf").getFile();
			path = URLDecoder.decode(path, "utf-8");

			font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
			font = font.deriveFont(32f); // Typsnittsstorlek
		} catch (Exception e) {
			e.printStackTrace();
		}

		loadImages();
		gameLoop();

	}

	public void loadImages() {

		for (int i = 0; i < 7; i++) {
			images[i] = new ImageIcon(getClass().getResource("/player-" + i + ".png")).getImage();
		}
		// reSizedPlayer = new
		// ImageIcon(getClass().getResource("/smallPlayer.png")).getImage();

		double myPlayersXPos = gameScreen.getWidth() / 2;
		double myPlayersYPos = 300;
		int myPlayerSpeed = 200;
		int myPlayerDx = 0;
		int myPlayerDy = 0;

		myPlayer = new PlayerCharacter(images, map, myPlayersXPos, myPlayersYPos, myPlayerSpeed, myPlayerDx, myPlayerDy,
				true);
		entityList.add(myPlayer);

		double x = gameScreen.getWidth() / 2;
		double y = gameScreen.getHeight() - shipImg.getHeight(null);
		int shipSpeed = 200;
		ship = new ShipEntity(shipImg, map, x, y, shipSpeed, 0, 0, true);
		entityList.add(ship);

		Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
		int alienSpeed = 20;
		double xAlien = 200;
		double yAlien = 200;
		int dyAlien = 1;
		int dxAlien = 0;

		for (int i = 1; i < 7; i++) {
			alien = new AlienEntity(alienImg, map, xAlien + 100 * i, yAlien, alienSpeed, dxAlien, dyAlien, true);
			entityList.add(alien);
		}

	}

	public void update(long deltaTime) {
		
		
	
		
		msg = new TxtContainer("Space Invader, Score: " + GameScore , (int) worldX + 30,
				(int) worldY +30, font, Color.GREEN);
		
		
		/*
		if (myPlayer.getXPos() > gameScreen.getWidth() / 2 && myPlayer.getYPos() > gameScreen.getHeight() / 2) {
			msg = new TxtContainer("Space Invader, Score: ", (int) (myPlayer.getXPos() - gameScreen.getWidth() / 2),
					(int) (myPlayer.getYPos() - 270), font, Color.GREEN);
		} else {
			msg = new TxtContainer("Space Invader, Score: ", 30, 30, font, Color.GREEN);
		}
		
		*/
		ship.setDirectionX(0);
		ship.setDirectionY(0);

		myPlayer.setDirectionX(0);
		myPlayer.setDirectionY(0);
		myPlayer.setImage(images[0]);

		/* Camera Controls for myPlayer */

		/*
		 * if (myPlayer.getXPos() < (map.getCols() * map.getTileSize() -
		 * gameScreen.getWidth() / 2) || myPlayer.getYPos() < (map.getRows() *
		 * map.getTileSize() - gameScreen.getHeight() / 2)) {
		 * gameScreen.cameraMoveTo(myPlayer.getXPos() - (gameScreen.getWidth() / 2),
		 * myPlayer.getYPos() - (gameScreen.getHeight() / 2));
		 * 
		 * if (myPlayer.getXPos() < gameScreen.getWidth() / 2) {
		 * gameScreen.cameraMoveTo(0, myPlayer.getYPos() - (gameScreen.getHeight() /
		 * 2)); } if (myPlayer.getYPos() < gameScreen.getHeight() / 2) {
		 * gameScreen.cameraMoveTo(myPlayer.getXPos() - (gameScreen.getWidth() / 2), 0);
		 * } if ((myPlayer.getXPos() < gameScreen.getWidth() / 2) && (myPlayer.getYPos()
		 * < gameScreen.getHeight() / 2)) { gameScreen.cameraMoveTo(0, 0);
		 * 
		 * } }
		 * 
		 */

		if (myPlayer.getActive() == true) {
			if (keyDown.get("PlayerRight")) {
				myPlayer.setDirectionX(1);
				moveMapX(deltaTime);

				myPlayer.setImage(images[3]);
			}
			if (keyDown.get("PlayerLeft") && myPlayer.getXPos() > 0) {
				myPlayer.setDirectionX(-1);
				
				moveMapX(deltaTime);

				myPlayer.setImage(images[1]);
			}
			if (keyDown.get("PlayerUp") && myPlayer.getYPos() > 0) {
				myPlayer.setDirectionY(-1);
				myPlayer.setImage(images[6]);
				moveMapY(deltaTime);
			}
			if (keyDown.get("PlayerDown")) {
				myPlayer.setDirectionY(1);
				myPlayer.setImage(images[5]);
				moveMapY(deltaTime);

			} // keyDown.get("fireM");
		}

		if (ship.getActive() == true) {
			if (keyDown.get("RightByDButton") && ship.getXPos() < (gameScreen.getWidth() - ship.getWidth())) {
				ship.setDirectionX(1);

			}
			if (keyDown.get("leftByAButton") && ship.getXPos() > 0) {
				ship.setDirectionX(-1);

			}
			if (keyDown.get("jumpOrTeleportUp") || keyDown.get("TPDown")) {
//			ship.setYPos(ship.getYPos() + jumpPower);
				System.out.println("ship has teleported");
			}
		}
		for (Entity entity : entityList) {
			entity.move(deltaTime);
		}

		// kontroll över spelfiguren myPlayer så den inte åker ur gameScreen

		if (myPlayer.getYPos() < 0) {
			myPlayer.setYPos(0);
		}

		// kontroll över missiler från myPlayercharacter och ShipEntity, och missilernas
		// kollision med aliens
		if (ship.missile != null && ship.missile.getActive()) {
			// Egen kod för kontroll om missilen är utanför skärmen.
			if (ship.missile.getYPos() < 0) {
				System.out.println("tryToReloadWithR");
				try {
					ship.missile = null;
				} catch (Exception e) {
				}
			}
			try {
				checkCollisionAndRemove();
			} catch (Exception e) {
			}
		}
		if (myPlayer.missileV1 != null && myPlayer.missileV1.getActive()) {
			// Egen kod för kontroll om missilen är utanför skärmen.
			if (myPlayer.missileV1.getYPos() < 0) {
				System.out.println("tryToReloadWithR");
				try {
					myPlayer.missileV1 = null;
				} catch (Exception e) {
				}
			}
			try {
				checkCollisionAndRemove();
			} catch (Exception e) {
			}
		}
	}

	public void render() {
//		gameScreen.render(entityList);

		gameScreen.beginRender();
		gameScreen.openRender(entityList);
		gameScreen.openRender(msg);
		gameScreen.show();
	}

	public void gameLoop() {
		/*
		 * Loop and Pause for game Beräkna tiden från spelstart och skriver ut den på
		 * skärmen
		 */
		long startTime = System.currentTimeMillis();
		int fps = 60;
		int updateTime = (int) (1.0 / fps * 1000000000.0);
		lastUpdateTime = System.nanoTime();
		double timeInSecondsCounter = 0;

		while (gameRunning) {
			long deltaTime = System.nanoTime() - lastUpdateTime;
			if (deltaTime > updateTime) {
				lastUpdateTime = System.nanoTime();

				if (!gamePause) {
					update(deltaTime);
					render();

					gameScreen.cameraMoveTo(worldX, worldY);
				}
				if (gameRunning) { // check if game is still running
					long elapsedTime = System.currentTimeMillis() - startTime;
					timeInSecondsCounter = (double) elapsedTime / 1000.0;

					if (timeInSecondsCounter >= 1.0) {
						/*
						 * gameTime calculator
						 * 
						 * System.out.println("time passed when game Started: " + timeInSecondsCounter);
						 */
						timeInSecondsCounter = 0;
					}
				}
			}
		}
	}

	/** Spelets tangentbordslyssnare */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT)
			keyDown.put("PlayerLeft", true);
		else if (key == KeyEvent.VK_RIGHT)
			keyDown.put("PlayerRight", true);
		else if (key == KeyEvent.VK_UP)
			keyDown.put("PlayerUp", true);
		else if (key == KeyEvent.VK_DOWN)
			keyDown.put("PlayerDown", true);

		else if (key == KeyEvent.VK_A)
			keyDown.put("leftByAButton", true);
		else if (key == KeyEvent.VK_D)
			keyDown.put("RightByDButton", true);

		else if (key == KeyEvent.VK_J) {
			keyDown.put("jumpOrTeleportUp", true);
			// int jumpPower = 200;
			int jumpPower = ship.getHight() + 10;
			ship.setYPos(ship.getYPos() - jumpPower);
		} else if (key == KeyEvent.VK_K) {
			keyDown.put("TPDown", true);
			int jumpPower = ship.getHight() + 10;

			ship.setYPos(ship.getYPos() + jumpPower);

		}

		else if (key == KeyEvent.VK_ESCAPE) {
			System.out.println("Game has ended.");
			System.exit(0);
		}
		if (key == KeyEvent.VK_SPACE) {
			System.out.println("pause/start");
			gamePause = !gamePause;
		}

		if (key == KeyEvent.VK_P) {
			keyDown.put("fireM", true);
			// ship.setActive(true);
			ship.tryToFire();
			ship.missile.setDirectionY(-1);
			System.out.println("missile has been fired");
		}

		if (key == KeyEvent.VK_R) {
			keyDown.put("reload", true);
			ship.missile = null;
			System.out.println("Reloading Ammo");
		}

		if (key == KeyEvent.VK_U) {
			keyDown.put("myPlayerFire", true);
			myPlayer.tryToFire();

			// myPlayer.missileV1.setDirectionY(-1);
			/*
			 * if(myPlayer.getDirectionY()!=0) {
			 * myPlayer.missileV1.setDirectionY(myPlayer.getDirectionY()); } else
			 * {myPlayer.missileV1.setDirectionY(-1);
			 * 
			 * }
			 */
			myPlayer.missileV1.setDirectionY(myPlayer.getDirectionY());
			myPlayer.missileV1.setDirectionX(myPlayer.getDirectionX());

			System.out.println("missile has been fired");
		}

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			keyDown.put("PlayerLeft", false);
		}
		if (key == KeyEvent.VK_RIGHT)
			keyDown.put("PlayerRight", false);
		if (key == KeyEvent.VK_UP)
			keyDown.put("PlayerUp", false);
		if (key == KeyEvent.VK_DOWN)
			keyDown.put("PlayerDown", false);

		if (key == KeyEvent.VK_A)
			keyDown.put("leftByAButton", false);
		if (key == KeyEvent.VK_D)
			keyDown.put("RightByDButton", false);

		if (key == KeyEvent.VK_J)
			keyDown.put("jumpOrTeleportUp", false);
		if (key == KeyEvent.VK_K)
			keyDown.put("TPDown", false);

		if (key == KeyEvent.VK_P) {
			if (gamePause) {
				keyDown.put("fireM", false);
				// ship.setActive(false);
			}
		}

		if (key == KeyEvent.VK_U) {
			if (gamePause) {
				keyDown.put("myPlayerFire", false);
				// myPlayer.setActive(false);
			}
		}

		if (key == KeyEvent.VK_R)
			keyDown.put("reload", false);

	}

	public void keyTyped(KeyEvent e) {
	}
			
	public void checkCollisionAndRemove() {
		ArrayList<Entity> removeList = new ArrayList<>();

		// alien <-> missile
		if (ship.missile != null && ship.missile.getActive()) {
			// Egen kod här! Undersök om missilen kolliderar med en alien.
			// Om så är fallet. Lägg till den i removeList.

			// i börjar med 2 eftersom myPlayer är nr:0, och ship är nr:1, aliens blir
			// resten
			for (int i = 2; i < entityList.size(); i++) {
				if (entityList.get(i).collision(ship.missile)) {
					removeList.add(entityList.get(i));
					System.out.println("hitAA");
					System.out.println("+10p");
					System.out.println("alien Killed by Ship");
					ship.missile = null;
					entityList.removeAll(removeList); // Alt namnet på arraylist
				}
			}
		}
		if (myPlayer.missileV1 != null && myPlayer.missileV1.getActive()) {
			for (int i = 2; i < entityList.size(); i++) {
				if (entityList.get(i).collision(myPlayer.missileV1)) {
					removeList.add(entityList.get(i));
					System.out.println("hitAA");
					System.out.println("+10p");
					System.out.println("alien Killed by Player");
					GameScore += 10;
					myPlayer.missileV1 = null;
					entityList.removeAll(removeList); // Alt namnet på arraylist
				}
			}
		}
	}

	

	public void moveMapX(double deltaTime) {
		/**
		 *  Metoden beräknar av kordinaterna av X kordinaten som sedan används för föflyttning av kamera 
		 * @param  deltaTime  är tidsskillnaden mellan tiden då spelet startas eller gameLoop
		 *  anropas och tiden då while loopen körs, deltaTime blir då tiden i sekunder och används för att uppdateringen 
		 *  inte skall ske för fort
		 *  
		 *  Metoden använder spelfigurens (myPlayer) x kordinaten och jämför WorldX med den och sedan uppdatera värdet på worldX
		 */
			if(worldX <= myPlayer.getXPos())
			worldX = myPlayer.getXPos() - gameScreen.getWidth()/2 ;	
			if( myPlayer.getXPos()< gameScreen.getWidth()/2)
				worldX = 0;
	}
	
	public void moveMapY(double deltaTime) {
		/**
		 *  Metoden beräknar av kordinaterna av Y kordinaten som sedan används för föflyttning av kamera 
		 * @param deltaTime är tidsskillnaden mellan tiden då spelet startas eller gameLoop
		 *  anropas och tiden då while loopen körs, deltaTime blir då tiden i sekunder och används för att uppdateringen 
		 *  inte skall ske för fort
		 *  
		 *  Metoden använder spelfigurens (myPlayer) y kordinaten och jämför WorldY med den och sedan uppdatera värdet på worldY
		 */
		if(worldY < myPlayer.getYPos()){
		worldY = myPlayer.getYPos() - gameScreen.getHeight()/2 ;
		}
		if( myPlayer.getYPos()< gameScreen.getHeight()/2)
			worldY = 0;
	}

	public static void main(String[] args) {
		System.out.println("this is a game, GYA version");
		System.out.println("note: gameTime starts when game start and can not be paused");
		new GameMain();

	}
}
