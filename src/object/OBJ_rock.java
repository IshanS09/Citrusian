package object;

import java.awt.Color;

import entity.Projectile;
import main.gamePanel;

public class OBJ_rock extends Projectile{
        
    gamePanel gp;
    public static final String objName = "Rock";

    public OBJ_rock(gamePanel gp) {
        super(gp);
        this.gp = gp;

        name = objName;
        speed = 8;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize); 
        right2 = setup("/res/projectile/rock", gp.tileSize, gp.tileSize);
    }
    public Color getParticleColor() {
        Color color = new Color(40, 50, 0);
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
