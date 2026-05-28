package object;

import entity.entity;
import main.gamePanel;

public class OBJ_mana extends entity {
    gamePanel gp;
    public static final String objName = "Mana Seed";

    public OBJ_mana(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickUp;
        name = objName;
        value = 1;
        down1 = setup("/res/Objects/Full", gp.tileSize, gp.tileSize);
        image = setup("/res/Objects/Full", gp.tileSize, gp.tileSize);
        image2 = setup("/res/Objects/Empty", gp.tileSize, gp.tileSize);

    }
    public boolean use(entity entity) {
        gp.playSE(2);
        gp.ui.addMessage("FireSeed +" + value);
        entity.seed += value;
        return true;
    }
}
