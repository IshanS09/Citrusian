package tile_interactive;

import java.awt.Color;

import entity.entity;
import main.gamePanel;

public class IT_destructibleWall extends interactiveTile {
    gamePanel gp;

    public IT_destructibleWall(gamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/res/tiles_interactive/destructibleWall", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 1;
    }
    public boolean isCorrectItem(entity entity) {
        boolean isCorrectItem = false;

        if(entity.currentWeapon.type == type_picaxe) {
            isCorrectItem = true;
        }
        return isCorrectItem;
    }
    public void playSE() {
        gp.playSE(13);
    }
    public interactiveTile getDestroyedForm() {
        interactiveTile tile = null;
        return tile;
    }
    public Color getParticleColor() {
        Color color = new Color(65, 65, 65);
        return color;
    }
    public int getParticleSize() {
        int size = 6;
        return size;
    }
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }
}
