package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader; // ✅ Use the corrected class name
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.UtilityTool;
import main.gamePanel;


public class tileManager {
    gamePanel gp; 
    public Tile[] tile;
    public int mapTileNum[][][]; // 2D array to hold tile numbers
    boolean drawPath = false; // Flag to control path drawing

    public tileManager(gamePanel gp) {
        this.gp = gp;
        tile = new Tile[50]; // Adjust size as needed
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow]; // Initialize the 2D array

        getTileImage();
        loadMap("/res/maps/worldV5.txt", 0);
        loadMap("/res/maps/interior3.txt", 1);
        loadMap("/res/maps/dungeonFIN.txt", 2);
        loadMap("/res/maps/dungeonL2.txt", 3);
        loadMap("/res/maps/BRJmap.txt", 4);
        loadMap("/res/maps/interiorPK3.txt", 5);
    }

    public void getTileImage() {

            setup(0, "floor", false);
            setup(1, "road00", false);
            setup(2, "bridge", false);
            setup(3, "tree", true);
            setup(4, "grass", false);
            setup(5, "wall", true);
            setup(6, "flower", false);
            setup(7, "mountain", true);
            setup(8, "house", false);
            setup(9, "water00", true);
            setup(10, "water01", true);
            setup(11, "village", true);
            setup(12, "flower2", false);
            setup(13, "dirt", true);
            setup(14, "drawer", true);
            setup(15, "table", true);
            setup(16, "bookshelf", true);
            setup(17, "bed", true);
            setup(18, "void", true);
            setup(19, "path", false);
            setup(20, "stairsdown", false);
            setup(21, "stairsup", false);
            setup(22, "water02", true);
            setup(23, "boardwalk", false);
            setup(24, "pirate", false);
            setup(25, "flagpole", true);
            setup(26, "wall", false);
            setup(27, "dirt", false);
            setup(28, "throne1", true);
            setup(29, "throne2", true);
            setup(30, "castle", false);
            
    }
    public void setup(int index, String imageName, boolean collision) {
        
        UtilityTool uTool = new UtilityTool();
        
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/newTiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize); 
            tile[index].collision = collision; 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public void loadMap(String filePath, int map) {
    try {
        InputStream is = getClass().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int row = 0;

        while (row < gp.maxWorldRow) {
            String line = br.readLine();
            if (line == null || line.trim().isEmpty()) {
                continue; // Skip empty lines
            }

            String[] numbers = line.split(",");

            if (numbers.length != gp.maxWorldCol) {
                System.out.println("⚠️ Warning: line " + row + " has " + numbers.length + " columns, expected " + gp.maxWorldCol);
            }

            for (int col = 0; col < Math.min(numbers.length, gp.maxWorldCol); col++) {
                String value = numbers[col].trim();
                if (!value.isEmpty()) {
                    try {
                        mapTileNum[map][col][row] = Integer.parseInt(value);
                    } catch (NumberFormatException nfe) {
                        System.out.println("❌ Invalid number at row " + row + ", col " + col + ": '" + value + "'");
                        mapTileNum[map][col][row] = 0; // Or any fallback tile index
                    }
                }
            }

            row++;
        }

        br.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}




    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow]; // Get the tile number from the 3D array
            
            int worldX = worldCol * gp.tileSize; // Calculate the world X position
            int worldY = worldRow * gp.tileSize; // Calculate the world Y position
            int screenX = worldX - gp.player.worldX + gp.player.screenX; // Calculate the screen X position
            int screenY = worldY - gp.player.worldY + gp.player.screenY; // Calculate the screen Y position
            
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                // Only draw the tile if it's within the player's view
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            
            worldCol++;
            
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
                
            }
        }

        if(drawPath) {
            g2.setColor(java.awt.Color.RED);
            for (int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

               
                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }

    }
}