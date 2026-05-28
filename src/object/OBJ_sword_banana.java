package object;

import entity.entity;
import main.gamePanel;

public class OBJ_sword_banana extends entity {
    public static final String objName = "Banana Blade";
    
    public OBJ_sword_banana(gamePanel gp) {
        super(gp);
        type = type_sword;
        name = objName;
        down1 = setup("/res/Objects/BananaBlade", gp.tileSize, gp.tileSize);
        attackValue = 3; 
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nA curved golden blade.\nSwift and sharp!";
        price = 200;
        knockBackPower = 5;
        motion1_duration = 5;
        motion2_duration = 25;
        durability = 60;
        maxDurability = 60;
    }
}