package object;

import entity.entity;
import main.gamePanel;

/** Reward for the old apple's "Night Watch" quest. Can't be bought. */
public class OBJ_sword_lime extends entity {
    public static final String objName = "Lime Edge";

    public OBJ_sword_lime(gamePanel gp) {
        super(gp);
        type = type_sword;
        name = objName;
        down1 = setup("/res/Objects/LimeEdge", gp.tileSize, gp.tileSize);
        attackValue = 4;
        attackArea.width = 40;
        attackArea.height = 40;
        description = "[" + name + "]\nThe old apple's blade.\nZesty and sharp!";
        price = 300;
        knockBackPower = 5;
        motion1_duration = 5;
        motion2_duration = 25;
        durability = 70;
        maxDurability = 70;
    }
}
