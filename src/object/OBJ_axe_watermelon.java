package object;

import entity.entity;
import main.gamePanel;

public class OBJ_axe_watermelon extends entity {
    public static final String objName = "Watermelon Smasher";
    
    public OBJ_axe_watermelon(gamePanel gp) {
        super(gp);
        type = type_axe;
        name = objName;
        down1 = setup("/res/Objects/WatermelonSmasher", gp.tileSize, gp.tileSize);
        attackValue = 4; 
        attackArea.width = 38;
        attackArea.height = 38;
        description = "[" + name + "]\nA massive watermelon axe.\nJuicy and devastating!";
        price = 180;
        knockBackPower = 8;
        motion1_duration = 10;
        motion2_duration = 35;
        durability = 55;
        maxDurability = 55;
    }
}