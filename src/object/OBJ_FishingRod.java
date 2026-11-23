package object;

import entity.entity;
import main.gamePanel;

public class OBJ_FishingRod extends entity {

    gamePanel gp;
    public static final String objName = "Fishing Rod";

    public OBJ_FishingRod(gamePanel gp) {
        super(gp);
        this.gp = gp;

        // Consumable so it can be used from the inventory, but use() never uses it up
        type = type_consumable;
        name = objName;
        down1 = setup("/res/Objects/fishingRod", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nFace the water and\npress R to cast.";
        price = 50;
        stackable = false;
    }

    public boolean use(entity user) {
        gp.fishing.tryCast();
        return false;
    }
}
