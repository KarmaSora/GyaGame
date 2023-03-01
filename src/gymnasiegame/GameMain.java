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
	//private long gameTime = 0;
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
//		gameScreen.setBackground("/space-background.jpg");
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

		System.out.println("använd camera för att röra gamescreen!! ");
		loadImages();
		gameLoop();
//firstCommit
	}

	public void loadImages() {

		for (int i = 0; i < 7; i++) {
			images[i] = new ImageIcon(getClass().getResource("/player-" + i + ".png")).getImage();
		}
		double MyPlayersXPos = gameScreen.getWidth() / 2;
		double MyPlayersYPos = 300;
		int MyPlayerSpeed = 200;
		MyPlayer = new playercharacter(images, map, MyPlayersXPos, MyPlayersYPos, MyPlayerSpeed);
		entityList.add(MyPlayer);

		double x = gameScreen.getWidth() / 2;
		double y = gameScreen.getHeight() - shipImg.getHeight(null);
		int shipSpeed = 200;
		ship = new ShipEntity(shipImg, map, x, y, shipSpeed);
		entityList.add(ship);

		Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
		int alienSpeed = 20;
		double xAlien = 200;
		double yAlien = 200;
		int dyAlien = 1;
		int dxAlien = 0;
		alien = new AlienEntity(alienImg, map, xAlien, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);

		alien = new AlienEntity(alienImg, map, xAlien + 100, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
		alien = new AlienEntity(alienImg, map,  xAlien + 200, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
		alien = new AlienEntity(alienImg,map, xAlien + 300, yAlien, alienSpeed, dxAlien, dyAlien);
		entityList.add(alien);
	}

	public void update(long deltaTime) {
		ship.setDirectionX(0);
		ship.setDirectionY(0);

		MyPlayer.setDirectionX(0);
		MyPlayer.setDirectionY(0);
		MyPlayer.setImage(images[0]);
		
		if (keyDown.get("PlayerRight") && MyPlayer.getXPos() < (gameScreen.getWidth() - MyPlayer.getWidth())) {
			MyPlayer.setDirectionX(1);
			MyPlayer.setImage(images[3]);
		}  if (keyDown.get("PlayerLeft") && MyPlayer.getXPos() > 0) {
			MyPlayer.setDirectionX(-1);
			MyPlayer.setImage(images[1]);
		}  if (keyDown.get("PlayerUp") && MyPlayer.getYPos() > 0) {
			MyPlayer.setDirectionY(-1);
			MyPlayer.setImage(images[6]);
		}  if (keyDown.get("PlayerDown") && MyPlayer.getYPos() < gameScreen.getHeight() - MyPlayer.getHight()) {
			MyPlayer.setDirectionY(1);
			MyPlayer.setImage(images[5]);
		}

		 if (keyDown.get("RightByDButton") && ship.getXPos() < (gameScreen.getWidth() - ship.getWidth())) {
			ship.setDirectionX(1);

		}  if (keyDown.get("leftByAButton") && ship.getXPos() > 0) {
			ship.setDirectionX(-1);

		}  if (keyDown.get("jumpOrTeleportUp") || keyDown.get("TPDown")) {
//			ship.setYPos(ship.getYPos() + jumpPower);
			System.out.println("ship has teleported");
		}

		for (Entity entity : entityList) {
			entity.move(deltaTime);
		}

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

	}

	public void render() {
		gameScreen.render(entityList);

	}

	public void gameLoop() {
	    long startTime = System.currentTimeMillis();
	    int fps = 10;
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
	                    System.out.println("time passed when game Started: " + timeInSecondsCounter);
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
			int jumpPower = 200;
			ship.setYPos(ship.getYPos() - jumpPower);
		} else if (key == KeyEvent.VK_K) {
			keyDown.put("TPDown", true);
			int jumpPower = 200;

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
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			keyDown.put("PlayerLeft", false);
		}
		else if (key == KeyEvent.VK_RIGHT)
			keyDown.put("PlayerRight", false);
		else if (key == KeyEvent.VK_UP)
			keyDown.put("PlayerUp", false);
		else if (key == KeyEvent.VK_DOWN)
			keyDown.put("PlayerDown", false);

		else if (key == KeyEvent.VK_A)
			keyDown.put("leftByAButton", false);
		else if (key == KeyEvent.VK_D)
			keyDown.put("RightByDButton", false);
		
		else if (key == KeyEvent.VK_J)
			keyDown.put("jumpOrTeleportUp", false);
		else if (key == KeyEvent.VK_K)
			keyDown.put("TPDown", false);
	}

	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		System.out.println("this is a game, GYA version");
		System.out.println("note: gameTime starts when game start and can not be paused");
		new GameMain();

	}
}
