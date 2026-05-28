package object;

import entity.entity;
import main.gamePanel;

public class OBJ_picaxe extends entity{

    public static final String objName = "Jackfruit Picaxe";

    public OBJ_picaxe(gamePanel gp) {
        super(gp);

        type = type_picaxe;
        name = objName;
        down1 = setup("/res/Objects/Picaxe", gp.tileSize, gp.tileSize);
        attackValue = 3;
        attackArea.width = 48;
        attackArea.height = 48;
        description = "[" + name + "]\nMade from Jackfruit's husk. \nMay he rest in peace."; 
        price = 250;
        knockBackPower = 6;
        motion1_duration = 20;
        motion2_duration = 40;
        durability = 50;
        maxDurability = 50;

    }

}