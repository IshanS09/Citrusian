package object;

import entity.entity;
import main.gamePanel;

/** Quest item for "The Lost Seed". Picked up on touch, then handed to the old apple. */
public class OBJ_GoldenSeed extends entity {
    public static final String objName = "Golden Seed";
    gamePanel gp;

    public OBJ_GoldenSeed(gamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_pickUp;
        name = objName;
        down1 = setup("/res/Objects/GoldenSeed", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nThe old apple's treasure.";
    }

    @Override
    public boolean use(entity entity) {
        gp.playSE(2);
        gp.questManager.onItemFound(objName);
        return true;
    }
}
