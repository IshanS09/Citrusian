package object;

import entity.Projectile;
import entity.entity;
import java.awt.Color;
import main.gamePanel;

public class OBJ_Fireskull extends Projectile {
    
    gamePanel gp;
    public static final String objName = "Fireskull";

    public OBJ_Fireskull(gamePanel gp) {
        super(gp);
        this.gp = gp;
        name = objName;
        speed = 7;
        maxLife = 80;
        life = maxLife;
        attack = 6; // Base fireskull damage
        knockBackPower = 5;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectile/SkullUp1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectile/SkullUp2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectile/SkullDown1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectile/SkullDown2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectile/SkullLeft1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectile/SkullLeft2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectile/SkullRight1", gp.tileSize, gp.tileSize); 
        right2 = setup("/res/projectile/SkullRight2", gp.tileSize, gp.tileSize);
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
