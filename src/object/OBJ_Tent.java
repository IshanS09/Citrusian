package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Tent extends entity {
    gamePanel gp;
    public static final String objName = "Tent";

    public OBJ_Tent(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = objName;
        down1 = setup("/res/Objects/tent", gp.tileSize, gp.tileSize);
        description = "[Tent]\nSleep.";
        price = 70;
        stackable = true;
    }
    public boolean use(entity entity) {
        gp.gameState = gp.sleepState;
        gp.playSE(14);
        gp.player.life = gp.player.maxLife;
        gp.player.seed = gp.player.maxSeed;
        gp.player.getSleepingImage(down1);
        return true;
    }
    

}
