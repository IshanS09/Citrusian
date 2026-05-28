package object;

import entity.entity;
import main.gamePanel;

public class OBJ_pickaxe_pineapple extends entity {
    public static final String objName = "Pineapple Pickaxe";
    
    public OBJ_pickaxe_pineapple(gamePanel gp) {
        super(gp);
        type = type_picaxe;
        name = objName;
        down1 = setup("/res/Objects/PP", gp.tileSize, gp.tileSize);
        attackValue = 3; 
        attackArea.width = 34;
        attackArea.height = 34;
        description = "[" + name + "]\nA spiky tropical tool.\nPerfect for mining!";
        price = 280;
        knockBackPower = 6;
        motion1_duration = 8;
        motion2_duration = 30;
        durability = 60;
        maxDurability = 60;
    }
}
