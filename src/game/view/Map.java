package game.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

import se.egy.graphics.Drawable;
/**
 * Klassen består av fler metoder som kan läsa in en .txt fil med siffror som innehåll och sedan ersätter siffror med 
 * bilder som sätts ihop till en bild. Bilden används sedan som spelplan. 
 * @author Karam Matar
 * @version 1.0
 * @since 2023-03-31
 */
public class Map implements Drawable {
	private int cols, rows;
	private int tileSize;

	private int[][] tileMap;

	// TileImages, Bilder som spelplanet skall använda sig av.
	private Image WaterTile, EarthTile, WallTile, ForestTile, GrassTile, TreeTile, TreeTileV2, FullTileGrassImage;
	
	//array av bilder som används för Tiles. 
	private Image[] TileAimages = new Image[6];

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

		for (int i = 0; i < 6; i++) {
			TileAimages[i] = new ImageIcon(getClass().getResource("/MapTile-" + i + ".png")).getImage();
		}

		WaterTile = new ImageIcon(getClass().getResource("/WaterTile.png")).getImage();

		EarthTile = TileAimages[0];
		GrassTile = TileAimages[1];
		WallTile = TileAimages[2];
		ForestTile = TileAimages[3];
		TreeTile = new ImageIcon(getClass().getResource("/TreeTile-0.png")).getImage();
		TreeTileV2 = new ImageIcon(getClass().getResource("/TreeTile-1.png")).getImage();

		FullTileGrassImage = TileAimages[5];

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
					g.drawImage(WaterTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 1:
					g.drawImage(EarthTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 2:
					g.drawImage(WallTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 3:
					g.drawImage(ForestTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 4:

					g.drawImage(GrassTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 5:
					g.drawImage(TreeTile, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
					break;
				case 6:
					g.drawImage(FullTileGrassImage, tileSize * x, tileSize * y, null); //
					// System.out.println("IMG: " + tileSize*y);
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
