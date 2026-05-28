package object;

import java.awt.Color;

import entity.Projectile;
import entity.entity;
import main.gamePanel;

public class OBJ_FireSeed extends Projectile {
    
    gamePanel gp;
    public static final String objName = "Fireseed";

    public OBJ_FireSeed(gamePanel gp, int attack) {
    super(gp);
    this.gp = gp;

    name = objName;
    speed = 5;
    maxLife = 80;
    life = maxLife;
    this.attack = attack + 1; // Use player's current attack
    knockBackPower = 5;
    useCost = 1;
    alive = false;
    getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectile/SeedUp1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectile/SeedUp2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectile/SeedDown1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectile/SeedDown2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectile/SeedLeft1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectile/SeedLeft2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectile/SeedRight1", gp.tileSize, gp.tileSize); 
        right2 = setup("/res/projectile/SeedRight2", gp.tileSize, gp.tileSize);
    }
    public boolean haveResource(entity user) {
        
        boolean haveResource = false;
        if(user.seed >= useCost) {
            haveResource = true;   
        }
        return haveResource;
    }
    public void subtractResource(entity user) {
        user.seed -= useCost;
    }
    public Color getParticleColor() {
        Color color = new Color(240, 50, 0);
        return color;
    }
    public int getParticleSize() {
        int size = 10;
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
