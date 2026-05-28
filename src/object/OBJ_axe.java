package object;

import entity.entity;
import main.gamePanel;

public class OBJ_axe extends entity{

    public static final String objName = "Stemcutter's Axe";

    public OBJ_axe(gamePanel gp) {
        super(gp);

        type = type_axe;
        name = objName;;
        down1 = setup("/res/Objects/Axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 32;
        attackArea.height = 32;
        description = "[" + name + "]\nCan be used for more \nthan chopping wood...";
        price = 150;
        knockBackPower = 5;
        motion1_duration = 20;
        motion2_duration = 35;
        durability = 45; 
        maxDurability = 45; 


    }

}
