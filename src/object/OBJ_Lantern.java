package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Lantern extends entity {

    public static final String objName = "Lantern";

    public OBJ_Lantern(gamePanel gp) {
        super(gp);

        type = type_light;
        name = objName;
        down1 = setup("/res/Objects/lantern", gp.tileSize, gp.tileSize);
        description = "[Lantern]\nIlluminates your surroundings.";
        price = 75;
        lightRadius = 500;
        durability = 50;
        maxDurability = 50;

    }
}
