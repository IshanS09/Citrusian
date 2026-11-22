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
    public static final int SLIME_PUDDLE = 63; // slows the player (see Player.update)
    public int mapTileNum[][][]; // 2D array to hold tile numbers
    boolean drawPath = false; // Flag to control path drawing

    public tileManager(gamePanel gp) {
        this.gp = gp;
        tile = new Tile[100]; // room for the themed area tiles
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

            // Baron Rotjaw's ship (BRJmap)
            setup(31, "hull", true);
            setup(32, "cannon_left", true);
            setup(33, "cannon_right", true);
            setup(34, "barrel", true);
            setup(35, "crate", true);
            setup(36, "hammock", true);
            setup(37, "ship_wheel", true);
            setup(38, "hatch", false);
            setup(39, "gangplank", false);
            setup(40, "pier", false);
            setup(41, "water_foam", true);

            // The dungeon (dungeonFIN)
            setup(42, "d_wall_top", true);
            setup(43, "d_wall_face", true);
            setup(44, "d_wall_torch", true);
            setup(45, "d_floor", false);
            setup(46, "d_floor_moss", false);
            setup(47, "d_floor_crack", false);
            setup(48, "d_floor_shadow", false);

            // Malibroc's lair (dungeonL2)
            setup(49, "m_wall_top", true);
            setup(50, "m_wall_face", true);
            setup(51, "m_wall_ember", true);
            setup(52, "m_floor", false);
            setup(53, "m_floor_ember", false);
            setup(54, "m_floor_ash", false);
            setup(55, "m_floor_shadow", false);

            // The Pea King's throne room (interiorPK3)
            setup(56, "swamp_tree", true);
            setup(57, "mud", false);
            setup(58, "royal_floor", false);
            setup(59, "royal_floor_moss", false);
            setup(60, "rot_wall", true);
            setup(61, "carpet", false);
            setup(62, "pillar", true);
            setup(SLIME_PUDDLE, "slime_puddle", false);
            setup(64, "swamp_grass", false);

            // Expansion: Zest Harbor, Citrus Grove, Rotting Marsh
            setup(65, "cobble", false);
            setup(66, "orange_tree", true);
            setup(67, "fence", true);
            setup(68, "market_stall", true);
            setup(69, "well", true);
            setup(70, "boat", true);
            setup(71, "swamp_water", true);
            setup(72, "lily_water", true);
            setup(73, "reeds", false);
            setup(74, "house", true);      // cottages in town (the walkable 8 is a doorway elsewhere)
            
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
        // Only walk the tiles that can be on screen (plus a one-tile margin)
        int firstCol = Math.max(0, (gp.player.worldX - gp.player.screenX) / gp.tileSize - 1);
        int firstRow = Math.max(0, (gp.player.worldY - gp.player.screenY) / gp.tileSize - 1);
        int lastCol = Math.min(gp.maxWorldCol - 1, firstCol + gp.maxScreenCol + 3);
        int lastRow = Math.min(gp.maxWorldRow - 1, firstRow + gp.maxScreenRow + 3);

        for (int worldRow = firstRow; worldRow <= lastRow; worldRow++) {
            for (int worldCol = firstCol; worldCol <= lastCol; worldCol++) {
                int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
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