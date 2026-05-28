package object;

import entity.entity;
import main.gamePanel;

public class OBJ_sword_default extends entity {

    public static final String objName = "Normal Sword";

    public OBJ_sword_default(gamePanel gp) {
        super(gp);
        
        type = type_sword;
        name = objName;
        down1 = setup("/res/Objects/DefaultSword", gp.tileSize, gp.tileSize);
        attackValue = 1; 
        attackArea.width = 32;
        attackArea.height = 32;
        description = "[" + name + "]\nRun of the mill sword.";
        price = 50;
        knockBackPower = 4;
        motion1_duration = 5;
        motion2_duration = 25;
        durability = 40;
        maxDurability = 40;
     
        
    }

}
