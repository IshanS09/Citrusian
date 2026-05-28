package object;

import entity.entity;
import main.gamePanel;

public class OBJ_shield_dragonfruit extends entity {
    public static final String objName = "Dragonfruit Shield";
    
    public OBJ_shield_dragonfruit(gamePanel gp) {
        super(gp);
        type = type_shield;
        name = objName;
        down1 = setup("/res/Objects/DragonfruitShield", gp.tileSize, gp.tileSize);
        defenseValue = 5;
        description = "[" + name + "]\nA legendary dragonfruit shield.\nMystical and exotic!";
        price = 300;
        durability = 75;
        maxDurability = 75;
    }
}