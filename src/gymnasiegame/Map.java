package gymnasiegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import se.egy.graphics.Drawable;
import se.egy.graphics.ImgContainer;

public class Map implements Drawable {
	private int cols, rows;
	private int tileSize;

	private int[][] tileMap;

	private Image imgForMap;

	/**
	 * Konstruktor Läser in filen och rutornas storlek. I textfilen skall det på
	 * <ul>
	 * <li>rad 1 stå antal kolumner (x)</li>
	 * <li>rad 2 stå antalet rader (y)</li>
	 * <li>övriga rader skrivas in en rad med ett heltal för varje ruta. Separera
	 * rutorna med mellanslag.</li>
	 * <ul>
	 * 
	 * @param pathToMap Sökvägen till textfilen med rutnätet
	 * @param tileSize  Rutornas storlek i px
	 */
	public Map(String pathToMap, int tileSize) {
		imgForMap = new ImageIcon(getClass().getResource("/MapTile-0.png")).getImage();
		this.tileSize = tileSize;


		// Lägger till slash om det saknas innan filnamn
		if (pathToMap.charAt(0) != '/')
			pathToMap = "/" + pathToMap;

		// Öppnar filen
		BufferedReader mapFile = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream(pathToMap)));
		readMapData(mapFile);
	}

	private void readMapData(BufferedReader mapFile) {
		// Läser antal rutor i x- och y-led. >Två första raderna
		try {
			cols = Integer.parseInt(mapFile.readLine());
			rows = Integer.parseInt(mapFile.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}

		tileMap = new int[cols][rows];

		// Laddar in tileMap från filen
		for (int row = 0; row < rows; row++) {
			String rowAsString = null;

			try {
				rowAsString = mapFile.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String[] tile = rowAsString.split(" ");
			for (int col = 0; col < cols; col++) {
				tileMap[col][row] = Integer.parseInt(tile[col]);
			}
		}
	}

	/**
	 * Renderar Rutnätet på ett grafikobjekt
	 * 
	 * @param g grafikobjekt där rutnätet skall renderas
	 */
	public void draw(Graphics2D g) {

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				int tile = tileMap[x][y];

				// Vilka färger som skall väljas beroende på värde i map
				switch (tile) {

				case 0:
					g.setColor(Color.black);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 1:
					g.setColor(Color.red);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 2:
					g.setColor(Color.PINK);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 3:
					g.setColor(Color.gray);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 4:
					g.setColor(Color.lightGray);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 5:
					g.setColor(Color.green);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
					break;
				case 6:
					  
					  g.drawImage(imgForMap, tileSize*x, tileSize*y, null); //
					  System.out.println("IMG: " + tileSize*y);
					 
					break;

				default:
					g.setColor(Color.black);

					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);

				}

			}
		}
	}


	/**
	 * Antal rader i map-filen
	 * 
	 * @return antalet rader
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Antal kolumner i map-filen
	 * 
	 * @return antalet kolumner
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Storleken på varje ruta
	 * 
	 * @return Storleken i px på rutorna
	 */
	public int getTileSize() {
		return tileSize;
	}

	/**
	 * Hämtar en specifik rutas värde i rutnätet
	 * 
	 * @param col rutans kolumnnummer
	 * @param row rutans radnummer
	 * @return Värdet i rutan
	 */
	public int getTile(double col, double row) {
		return tileMap[(int) col][(int) row];
	}

	/**
	 * Hämtar en specifik rutas värde i rutnätet med utgångsdpunkt från en punkt på
	 * skärmen (px).
	 * 
	 * @param x punktens position i x-led
	 * @param y punktens position i y-led
	 * 
	 * @return rutans värde i rutnätet
	 */
	public int getTileFromPx(double x, double y) {
		return tileMap[(int) x / tileSize][(int) y / tileSize];
	}

	/**
	 * Ändrar en specifik rutas värde i rutnätet
	 * 
	 * @param col   rutans kolumnnummer
	 * @param row   rutans radnummer
	 * @param value nya värdet i rutan
	 */
	public void setTile(int col, int row, int value) {
		tileMap[col][row] = value;
	}

	/**
	 * Ändrar en specifik rutas värde i rutnätet med utgångsdpunkt från en punkt på
	 * skärmen (px)
	 * 
	 * @param x     punktens position i x-led
	 * @param y     punktens position i y-led
	 * @param value nya värdet i rutan
	 */
	public void setTileFromPx(double x, double y, int value) {
		tileMap[(int) x / tileSize][(int) y / tileSize] = value;
	}

}
