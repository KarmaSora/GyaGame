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
	private Map map;

	private HashMap<String, Boolean> keyDown = new HashMap<>();

	private ShipEntity ship;
	private AlienEntity alien;
	private Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
	private ArrayList<Entity> entityList = new ArrayList<>();
	private GameScreen gameScreen = new GameScreen("GameGyAVersion", 800, 600, false);

	private playercharacter MyPlayer;
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
		keyDown.put("MyPlayerFire", false);

		System.out.println("använd camera för att röra gamescreen!! ");
		loadImages();
		gameLoop();
	}

	public void loadImages() {

		for (int i = 0; i < 7; i++) {
			images[i] = new ImageIcon(getClass().getResource("/player-" + i + ".png")).getImage();
		}
		// reSizedPlayer = new
		// ImageIcon(getClass().getResource("/smallPlayer.png")).getImage();

		double MyPlayersXPos = gameScreen.getWidth() / 2;
		double MyPlayersYPos = 300;
		int MyPlayerSpeed = 200;
		int MyPlayerDx = 0;
		int MyPlayerDy = 0;

		MyPlayer = new playercharacter(images, map, MyPlayersXPos, MyPlayersYPos, MyPlayerSpeed, MyPlayerDx, MyPlayerDy,
				true);
		entityList.add(MyPlayer);

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
		ship.setDirectionX(0);
		ship.setDirectionY(0);

		MyPlayer.setDirectionX(0);
		MyPlayer.setDirectionY(0);
		MyPlayer.setImage(images[0]);

		if (MyPlayer.getActive() == true) {
			if (keyDown.get("PlayerRight") && MyPlayer.getXPos() < (gameScreen.getWidth() - MyPlayer.getWidth())) {
				MyPlayer.setDirectionX(1);
				MyPlayer.setImage(images[3]);
			}
			if (keyDown.get("PlayerLeft") && MyPlayer.getXPos() > 0) {
				MyPlayer.setDirectionX(-1);
				MyPlayer.setImage(images[1]);
			}
			if (keyDown.get("PlayerUp") && MyPlayer.getYPos() > 0) {
				MyPlayer.setDirectionY(-1);
				MyPlayer.setImage(images[6]);
			}
			if (keyDown.get("PlayerDown") && MyPlayer.getYPos() < gameScreen.getHeight() - MyPlayer.getHight()) {
				MyPlayer.setDirectionY(1);
				MyPlayer.setImage(images[5]);
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

		// kontroll över spelfiguren MyPlayer så den inte åker ur gameScreen
		if (MyPlayer.getXPos() < 0) {
			MyPlayer.setXPos(0);
		}
		if (MyPlayer.getXPos() > gameScreen.getWidth()) {
			MyPlayer.setXPos(gameScreen.getWidth() - MyPlayer.getWidth());
		}
		if (MyPlayer.getYPos() < 0) {
			MyPlayer.setYPos(0);
		}
		if (MyPlayer.getYPos() + MyPlayer.getHight() > gameScreen.getHeight()) {
			MyPlayer.setYPos(gameScreen.getHeight() - MyPlayer.getHight());
		}

		// kontroll över missiler från MyPlayercharacter och ShipEntity, och missilernas
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
		if (MyPlayer.missileV1 != null && MyPlayer.missileV1.getActive()) {
			// Egen kod för kontroll om missilen är utanför skärmen.
			if (MyPlayer.missileV1.getYPos() < 0) {
				System.out.println("tryToReloadWithR");
				try {
					MyPlayer.missileV1 = null;
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
		gameScreen.render(entityList);
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
			keyDown.put("MyPlayerFire", true);
			MyPlayer.tryToFire();

			// MyPlayer.missileV1.setDirectionY(-1);
			/*
			 * if(MyPlayer.getDirectionY()!=0) {
			 * MyPlayer.missileV1.setDirectionY(MyPlayer.getDirectionY()); } else
			 * {MyPlayer.missileV1.setDirectionY(-1);
			 * 
			 * }
			 */
			MyPlayer.missileV1.setDirectionY(MyPlayer.getDirectionY());
			MyPlayer.missileV1.setDirectionX(MyPlayer.getDirectionX());

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
				keyDown.put("MyPlayerFire", false);
				// MyPlayer.setActive(false);
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

			// i börjar med 2 eftersom MyPlayer är nr:0, och ship är nr:1, aliens blir
			// resten
			for (int i = 2; i < entityList.size(); i++) {
				if (entityList.get(i).collision(ship.missile)) {
					removeList.add(entityList.get(i));
					System.out.println("hitAA");
					System.out.println("+10p");
					System.out.println("alien Killed");
					ship.missile = null;
					entityList.removeAll(removeList); // Alt namnet på arraylist
				}
			}
		}
		if (MyPlayer.missileV1 != null && MyPlayer.missileV1.getActive()) {
			for (int i = 2; i < entityList.size(); i++) {
				if (entityList.get(i).collision(MyPlayer.missileV1)) {
					removeList.add(entityList.get(i));
					System.out.println("hitAA");
					System.out.println("+10p");
					System.out.println("alien Killed");
					MyPlayer.missileV1 = null;
					entityList.removeAll(removeList); // Alt namnet på arraylist
				}
			}
		}
	}

	public void PlayerToMapContact() {
		/*
		 * for (int x = 0; x < map.getCols(); x++) { for (int y = 0; y < map.getRows();
		 * y++) { int tile = tileOfBlock[x][y]; } }
		 */

	}

	public static void main(String[] args) {
		System.out.println("this is a game, GYA version");
		System.out.println("note: gameTime starts when game start and can not be paused");
		new GameMain();

	}
}
