package object;

import entity.entity;
import main.gamePanel;

public class OBJ_shield_blue extends entity{

    public static final String objName = "Blueberry Shield";

    public OBJ_shield_blue(gamePanel gp) {
    super(gp);

    type = type_shield;
    name = objName;
    down1 = setup("/res/Objects/BlueShield", gp.tileSize, gp.tileSize);
    defenseValue = 2;
    description = "[" + name + "]\nA shield from a forgotten tribe...";
    price = 200;
    durability = 45; 
    maxDurability = 45;


    }
}
