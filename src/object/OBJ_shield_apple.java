package object;

import entity.entity;
import main.gamePanel;

public class OBJ_shield_apple extends entity {
    public static final String objName = "Strawberry Shield";
    
    public OBJ_shield_apple(gamePanel gp) {
        super(gp);
        type = type_shield;
        name = objName;
        down1 = setup("/res/Objects/AppleShield", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nA fresh strawberry shield.\nThe seeds absorb the hit!";
        price = 230;
        durability = 50;
        maxDurability = 50;
    }
}