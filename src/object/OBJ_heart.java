package object;

import entity.entity;
import main.gamePanel;

public class OBJ_heart extends entity {
    gamePanel gp;
    public static final String objName = "Heart";

    public OBJ_heart(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickUp;
        name = objName;
        value = 2;
        down1 = setup("/res/Objects/heart_full", gp.tileSize, gp.tileSize);
        image = setup("/res/Objects/heart_full", gp.tileSize, gp.tileSize); // Load full heart image
        image2 = setup("/res/Objects/heart_half", gp.tileSize, gp.tileSize); // Load half heart image
        image3 = setup("/res/Objects/heart_blank", gp.tileSize, gp.tileSize); // Load blank heart image
}
public boolean use(entity entity) {
    gp.playSE(2);
    gp.ui.addMessage("Life +" + value);
    entity.life += value;
    return true;
    }
}