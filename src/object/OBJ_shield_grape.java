package object;

import entity.entity;
import main.gamePanel;

public class OBJ_shield_grape extends entity {
    public static final String objName = "Grape Cluster Shield";
    
    public OBJ_shield_grape(gamePanel gp) {
        super(gp);
        type = type_shield;
        name = objName;
        down1 = setup("/res/Objects/GrapeShield", gp.tileSize, gp.tileSize);
        defenseValue = 4;
        description = "[" + name + "]\nA shield made of grapes.\nMany together are strong!";
        price = 260;
        durability = 65;
        maxDurability = 65;
    }
}
