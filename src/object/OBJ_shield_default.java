package object;

import entity.entity;
import main.gamePanel;

public class OBJ_shield_default extends entity{

    public static final String objName = "Normal Shield";
    
    public OBJ_shield_default(gamePanel gp) {
    super(gp);

    type = type_shield;
    name = objName;
    down1 = setup("/res/Objects/DefaultShield", gp.tileSize, gp.tileSize);
    defenseValue = 1;
    description = "[" + name + "]\nIt protects, that's what matters.";
    price = 75;
    durability = 40;
    maxDurability = 40;

    }

}
